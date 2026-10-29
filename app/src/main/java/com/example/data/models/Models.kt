package com.example.data.models

data class ObjectiveItem(
    val id: Int,
    val number: Int,
    val title: String,
    val description: String,
    val iconKey: String,
    val zambianImpact: String
)

data class CoreValueItem(
    val title: String,
    val tagline: String,
    val description: String,
    val iconKey: String
)

data class ActivityItem(
    val title: String,
    val description: String,
    val iconKey: String,
    val tag: String
)

enum class ProjectStage {
    IDEA,
    PROTOTYPING,
    TESTING,
    DEPLOYED
}

data class ProjectItem(
    val id: String,
    val title: String,
    val track: String,
    val tagline: String,
    val problemStatement: String,
    val solutionDescription: String,
    val techStack: List<String>,
    val stage: ProjectStage,
    val location: String,
    val leadDeveloper: String,
    val stars: Int,
    val isStarred: Boolean = false
)

data class CommunityChallenge(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val submittedBy: String,
    val location: String,
    val votes: Int,
    val isUpvoted: Boolean = false,
    val timestamp: String,
    val status: String = "Open for Collaboration"
)

data class MemberProfile(
    val fullName: String,
    val developerHandle: String,
    val email: String,
    val role: String,
    val primarySkill: String,
    val secondarySkills: List<String>,
    val location: String,
    val institutionOrOrg: String,
    val memberId: String,
    val joinedDate: String,
    val bio: String
)

data class TriviaQuestion(
    val id: Int,
    val question: String,
    val codeSnippet: String? = null,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val category: String
)
