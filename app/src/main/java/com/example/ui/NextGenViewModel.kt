package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.*
import com.example.data.repository.NextGenRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class UiState(
    val currentTab: Int = 0,
    val projectTrackFilter: String = "All",
    val projectSearchQuery: String = "",
    val challengeCategoryFilter: String = "All",
    val challengeSearchQuery: String = "",
    val selectedProject: ProjectItem? = null,
    val showAddProjectDialog: Boolean = false,
    val showSubmitChallengeDialog: Boolean = false,
    val showEditProfileDialog: Boolean = false,
    val currentTriviaIndex: Int = 0,
    val selectedAnswerIndex: Int? = null,
    val answerSubmitted: Boolean = false,
    val isAnswerCorrect: Boolean = false,
    val messageSnackbar: String? = null
)

class NextGenViewModel(application: Application) : AndroidViewModel(application) {

    val repository = NextGenRepository(application.applicationContext)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val projects: StateFlow<List<ProjectItem>> = repository.projects
    val challenges: StateFlow<List<CommunityChallenge>> = repository.challenges
    val memberProfile: StateFlow<MemberProfile?> = repository.memberProfile
    val triviaScore: StateFlow<Int> = repository.triviaScore

    // Filtered projects
    val filteredProjects: StateFlow<List<ProjectItem>> = combine(
        projects,
        _uiState
    ) { projList, state ->
        projList.filter { proj ->
            val matchesTrack = state.projectTrackFilter == "All" || proj.track.equals(state.projectTrackFilter, ignoreCase = true)
            val matchesSearch = state.projectSearchQuery.isBlank() ||
                    proj.title.contains(state.projectSearchQuery, ignoreCase = true) ||
                    proj.tagline.contains(state.projectSearchQuery, ignoreCase = true) ||
                    proj.techStack.any { it.contains(state.projectSearchQuery, ignoreCase = true) }
            matchesTrack && matchesSearch
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered challenges
    val filteredChallenges: StateFlow<List<CommunityChallenge>> = combine(
        challenges,
        _uiState
    ) { chalList, state ->
        chalList.filter { chal ->
            val matchesCategory = state.challengeCategoryFilter == "All" || chal.category.contains(state.challengeCategoryFilter, ignoreCase = true)
            val matchesSearch = state.challengeSearchQuery.isBlank() ||
                    chal.title.contains(state.challengeSearchQuery, ignoreCase = true) ||
                    chal.description.contains(state.challengeSearchQuery, ignoreCase = true) ||
                    chal.location.contains(state.challengeSearchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setTab(index: Int) {
        _uiState.update { it.copy(currentTab = index) }
    }

    fun setProjectTrackFilter(track: String) {
        _uiState.update { it.copy(projectTrackFilter = track) }
    }

    fun setProjectSearchQuery(query: String) {
        _uiState.update { it.copy(projectSearchQuery = query) }
    }

    fun setChallengeCategoryFilter(cat: String) {
        _uiState.update { it.copy(challengeCategoryFilter = cat) }
    }

    fun setChallengeSearchQuery(query: String) {
        _uiState.update { it.copy(challengeSearchQuery = query) }
    }

    fun selectProject(project: ProjectItem?) {
        _uiState.update { it.copy(selectedProject = project) }
    }

    fun setShowAddProjectDialog(show: Boolean) {
        _uiState.update { it.copy(showAddProjectDialog = show) }
    }

    fun setShowSubmitChallengeDialog(show: Boolean) {
        _uiState.update { it.copy(showSubmitChallengeDialog = show) }
    }

    fun setShowEditProfileDialog(show: Boolean) {
        _uiState.update { it.copy(showEditProfileDialog = show) }
    }

    fun toggleStarProject(projectId: String) {
        repository.toggleStarProject(projectId)
        // If selected project is the one toggled, update it
        val currentSelected = _uiState.value.selectedProject
        if (currentSelected?.id == projectId) {
            _uiState.update {
                it.copy(
                    selectedProject = currentSelected.copy(
                        isStarred = !currentSelected.isStarred,
                        stars = if (!currentSelected.isStarred) currentSelected.stars + 1 else maxOf(0, currentSelected.stars - 1)
                    )
                )
            }
        }
    }

    fun toggleUpvoteChallenge(challengeId: String) {
        repository.toggleUpvoteChallenge(challengeId)
    }

    fun submitChallenge(
        title: String,
        category: String,
        description: String,
        location: String,
        submittedBy: String
    ) {
        repository.submitChallenge(title, category, description, location, submittedBy)
        _uiState.update {
            it.copy(
                showSubmitChallengeDialog = false,
                messageSnackbar = "Challenge submitted to community incubator!"
            )
        }
    }

    fun submitProject(
        title: String,
        track: String,
        tagline: String,
        problemStatement: String,
        solutionDescription: String,
        techStack: List<String>,
        stage: ProjectStage,
        location: String,
        leadDeveloper: String
    ) {
        repository.submitProject(
            title, track, tagline, problemStatement, solutionDescription, techStack, stage, location, leadDeveloper
        )
        _uiState.update {
            it.copy(
                showAddProjectDialog = false,
                messageSnackbar = "Project prototype published!"
            )
        }
    }

    fun saveMemberProfile(profile: MemberProfile) {
        repository.saveMemberProfile(profile)
        _uiState.update {
            it.copy(
                showEditProfileDialog = false,
                messageSnackbar = "Developer Pass updated successfully!"
            )
        }
    }

    fun selectTriviaAnswer(index: Int) {
        if (!_uiState.value.answerSubmitted) {
            _uiState.update { it.copy(selectedAnswerIndex = index) }
        }
    }

    fun submitTriviaAnswer() {
        val state = _uiState.value
        val selected = state.selectedAnswerIndex ?: return
        val currentQ = repository.triviaQuestions.getOrNull(state.currentTriviaIndex) ?: return

        val isCorrect = selected == currentQ.correctIndex
        if (isCorrect) {
            repository.incrementTriviaScore(15)
        }

        _uiState.update {
            it.copy(
                answerSubmitted = true,
                isAnswerCorrect = isCorrect
            )
        }
    }

    fun nextTriviaQuestion() {
        val nextIdx = (_uiState.value.currentTriviaIndex + 1) % repository.triviaQuestions.size
        _uiState.update {
            it.copy(
                currentTriviaIndex = nextIdx,
                selectedAnswerIndex = null,
                answerSubmitted = false,
                isAnswerCorrect = false
            )
        }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(messageSnackbar = null) }
    }
}
