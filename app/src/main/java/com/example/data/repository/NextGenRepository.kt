package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class NextGenRepository(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("next_gen_zambia_prefs", Context.MODE_PRIVATE)

    val introductionText = """
Next Gen Developers Zambia is a group of individuals brought together by a shared interest in technology, innovation, and problem-solving. We aim to explore ideas, develop practical solutions, and use our collective skills and creativity to address real-world challenges.

We believe that every problem presents an opportunity for innovation. Through collaboration, experimentation, and continuous improvement, we seek to turn ideas into practical solutions that make a meaningful difference.

As we begin our journey, we are committed to thinking creatively, working together, and developing solutions that address real needs in our communities and beyond.
    """.trimIndent()

    val visionText = "To become a recognised team of innovative developers in Zambia, known for creating practical technological solutions that address real-world problems."

    val missionText = "To identify problems, explore innovative ideas, and develop effective technological solutions through collaboration, creativity, and the practical application of our skills and knowledge."

    val commitmentText = """
We are committed to turning ideas into action, approaching challenges with creativity, and working together to develop practical solutions.

We understand that meaningful innovation takes time, experimentation, and persistence. Every project is an opportunity to discover something new, improve our abilities, and move closer to our goals.

We are Next Gen Developers Zambia. We think, we create, and we solve.
    """.trimIndent()

    val objectives: List<ObjectiveItem> = listOf(
        ObjectiveItem(
            id = 1,
            number = 1,
            title = "Solve Real-World Problems",
            description = "To identify challenges affecting individuals, businesses, and communities and develop practical solutions to address them.",
            iconKey = "ProblemSolving",
            zambianImpact = "Focusing on high-impact pain points across Zambian commerce, agriculture, and healthcare."
        ),
        ObjectiveItem(
            id = 2,
            number = 2,
            title = "Develop Innovative Solutions",
            description = "To transform ideas into useful applications, systems, and other solutions that respond to existing needs.",
            iconKey = "Innovation",
            zambianImpact = "Building mobile-first, low-bandwidth, and offline-capable digital tools tailored for Zambian users."
        ),
        ObjectiveItem(
            id = 3,
            number = 3,
            title = "Encourage Teamwork and Collaboration",
            description = "To combine our ideas, knowledge, and abilities to develop solutions more effectively as a team.",
            iconKey = "Collaboration",
            zambianImpact = "Uniting student developers, self-taught coders, and senior engineers nationwide."
        ),
        ObjectiveItem(
            id = 4,
            number = 4,
            title = "Apply Technology Practically",
            description = "To use programming, software development, and other relevant technologies to address practical challenges.",
            iconKey = "Code",
            zambianImpact = "Pragmatic engineering over theoretical hype—shipping working software that solves real friction."
        ),
        ObjectiveItem(
            id = 5,
            number = 5,
            title = "Experiment and Improve",
            description = "To test our ideas, learn from mistakes, evaluate our solutions, and continuously improve the quality of our work.",
            iconKey = "Experiment",
            zambianImpact = "Embracing iterative testing in real Zambian communities and listening to direct user feedback."
        ),
        ObjectiveItem(
            id = 6,
            number = 6,
            title = "Build Practical Projects",
            description = "To develop projects that demonstrate our capabilities and provide opportunities to apply our knowledge in real situations.",
            iconKey = "Projects",
            zambianImpact = "Maintaining an active portfolio of open-source and community-tested software solutions."
        ),
        ObjectiveItem(
            id = 7,
            number = 7,
            title = "Contribute to Zambia's Technological Development",
            description = "To create solutions that respond to relevant challenges in Zambia and contribute to the country's technological progress.",
            iconKey = "Country",
            zambianImpact = "Fostering local digital sovereignity, tech talent retention, and empowering Zambia's digital economy."
        )
    )

    val coreValues: List<CoreValueItem> = listOf(
        CoreValueItem(
            title = "Innovation",
            tagline = "Welcome new ideas",
            description = "We welcome new ideas and explore creative approaches to solving problems.",
            iconKey = "Lightbulb"
        ),
        CoreValueItem(
            title = "Collaboration",
            tagline = "Strength in unity",
            description = "We work together, share ideas, and value each member's contribution.",
            iconKey = "Group"
        ),
        CoreValueItem(
            title = "Problem-Solving",
            tagline = "Understand first",
            description = "We focus on understanding problems before developing appropriate solutions.",
            iconKey = "Psychology"
        ),
        CoreValueItem(
            title = "Commitment",
            tagline = "Dedication to results",
            description = "We dedicate time and effort to turning our ideas into practical results.",
            iconKey = "VolunteerActivism"
        ),
        CoreValueItem(
            title = "Continuous Improvement",
            tagline = "Learn and evolve",
            description = "We learn from our experiences and strive to make our work better.",
            iconKey = "TrendingUp"
        ),
        CoreValueItem(
            title = "Integrity",
            tagline = "Ethical engineering",
            description = "We aim to develop solutions responsibly, honestly, and with respect for the people who use them.",
            iconKey = "VerifiedUser"
        )
    )

    val activities: List<ActivityItem> = listOf(
        ActivityItem(
            title = "Software & Computer Systems",
            description = "Developing software applications, mobile systems, and web tools for local enterprises and citizens.",
            iconKey = "LaptopMac",
            tag = "Software Dev"
        ),
        ActivityItem(
            title = "Innovative Ideas & Emerging Tech",
            description = "Exploring cutting-edge technologies including on-device AI, IoT telemetry, and offline-first mobile architecture.",
            iconKey = "AutoAwesome",
            tag = "R & D"
        ),
        ActivityItem(
            title = "Business & Community Challenges",
            description = "Tackling grassroots hurdles in agriculture, healthcare clinics, transport logistics, and SME trade.",
            iconKey = "Storefront",
            tag = "Community"
        ),
        ActivityItem(
            title = "Technical Collaboration",
            description = "Code reviews, pair programming, open-source hack sessions, and shared architecture workshops.",
            iconKey = "Hub",
            tag = "Teamwork"
        ),
        ActivityItem(
            title = "Testing & Iteration",
            description = "Rigorous real-world user testing, error monitoring, and continuous deployment to ensure robust quality.",
            iconKey = "Build",
            tag = "Quality"
        ),
        ActivityItem(
            title = "Documentation & Knowledge Sharing",
            description = "Open documenting of our project findings, open-source repositories, and mentoring upcoming developers.",
            iconKey = "MenuBook",
            tag = "Knowledge"
        )
    )

    private val initialProjects: List<ProjectItem> = listOf(
        ProjectItem(
            id = "proj_zam_crop",
            title = "ZamCrop AI Leaf Guard",
            track = "AgriTech",
            tagline = "Offline crop disease diagnostic for smallholder maize and cassava farmers",
            problemStatement = "Smallholder farmers in Chongwe, Mumbwa, and Central Province lose up to 35% of yields to maize streak virus and cassava mosaic disease before agricultural extension officers can visit.",
            solutionDescription = "An on-device mobile vision app that diagnoses crop leaf anomalies without internet connectivity, prescribing accessible organic treatments and linking farmers to local input suppliers.",
            techStack = listOf("Kotlin", "TensorFlow Lite", "Jetpack Compose", "Room"),
            stage = ProjectStage.PROTOTYPING,
            location = "Lusaka & Chongwe",
            leadDeveloper = "NextGen Agri Squad",
            stars = 42
        ),
        ProjectItem(
            id = "proj_chikwama",
            title = "Chikwama QR & USSD Pay Hub",
            track = "FinTech",
            tagline = "Unified micro-merchant payment aggregator for Zambian market traders",
            problemStatement = "Informal traders at Soweto, Chisokone, and City Market juggle multiple Airtel Money, MTN MoMo, and Zamtel numbers with high friction and untracked bookkeeping.",
            solutionDescription = "A lightweight mobile hub generating a single universal printed QR code that auto-routes payments across all Zambian mobile money networks with instantaneous audio confirmation.",
            techStack = listOf("Android", "USSD Gateway", "Moshi", "Coroutines"),
            stage = ProjectStage.TESTING,
            location = "Soweto Market, Lusaka",
            leadDeveloper = "FinTech Working Group",
            stars = 58
        ),
        ProjectItem(
            id = "proj_klinika",
            title = "Klinika Care Flow",
            track = "HealthTech",
            tagline = "Community health clinic triage & medicine inventory dashboard",
            problemStatement = "Patients in peri-urban townships queue for hours only to discover basic medicines (paracetamol, amoxicillin, malaria kits) are temporarily out of stock.",
            solutionDescription = "SMS and mobile app portal giving citizens real-time queue estimates and stock indicators at local primary health clinics before traveling.",
            techStack = listOf("Kotlin", "Retrofit", "Compose M3", "Coroutines"),
            stage = ProjectStage.DEPLOYED,
            location = "Kanyama & Chawama Clinics",
            leadDeveloper = "Health Systems Team",
            stars = 64
        ),
        ProjectItem(
            id = "proj_zam_code",
            title = "ZedCode Academy",
            track = "EduTech",
            tagline = "Offline-first programming playground for Zambian youth",
            problemStatement = "High data costs and inconsistent electricity restrict Zambian youth outside major universities from learning modern software development.",
            solutionDescription = "Interactive offline mobile curriculum teaching programming logic, algorithms, and Kotlin fundamentals with zero data consumption.",
            techStack = listOf("Jetpack Compose", "SQLite", "Material 3"),
            stage = ProjectStage.PROTOTYPING,
            location = "Countrywide",
            leadDeveloper = "NextGen Education Circle",
            stars = 79
        ),
        ProjectItem(
            id = "proj_solar_grid",
            title = "SunTelemetry Zambia",
            track = "CleanTech",
            tagline = "IoT telemetry monitor for off-grid rural solar kiosks",
            problemStatement = "Rural solar mini-grids frequently fail due to unmonitored battery depletion and inverter overloading, stranding off-grid communities.",
            solutionDescription = "Bluetooth Low Energy and GSM telemetry receiver that logs battery health, sends preventive alerts, and optimizes load balancing.",
            techStack = listOf("Kotlin", "BLE APIs", "StateFlow", "Vico"),
            stage = ProjectStage.IDEA,
            location = "Southern Province (Choma)",
            leadDeveloper = "Hardware & Embedded Guild",
            stars = 31
        )
    )

    private val initialChallenges: List<CommunityChallenge> = listOf(
        CommunityChallenge(
            id = "chal_1",
            title = "Smart Bus Fare & Route Tracking for Lusaka Minibuses",
            category = "Transport & Mobility",
            description = "Commuters face fluctuating minibus fares and unpredictable schedules along Great East Road and Lumumba. Can we build a crowd-sourced fare & arrival beacon?",
            submittedBy = "Bwalya M.",
            location = "Lusaka",
            votes = 87,
            timestamp = "2 days ago"
        ),
        CommunityChallenge(
            id = "chal_2",
            title = "Water Point Status & Borehole Breakdown Alerts",
            category = "Civic Infrastructure",
            description = "Rural boreholes in Eastern Province often remain broken for weeks because local councils are not notified promptly. Need an SMS/USSD ticketing system.",
            submittedBy = "Chileshe T.",
            location = "Chipata",
            votes = 114,
            timestamp = "4 days ago"
        ),
        CommunityChallenge(
            id = "chal_3",
            title = "Local Language Translation Model for Bemba, Nyanja, and Tonga",
            category = "AI & Linguistics",
            description = "Important civic and health notices are only in English. Let's create high-quality open datasets and lightweight models for Zambian national languages.",
            submittedBy = "Mulenga K.",
            location = "Kitwe",
            votes = 142,
            timestamp = "1 week ago"
        ),
        CommunityChallenge(
            id = "chal_4",
            title = "Artisan & Handyman Verification Platform for Copperbelt",
            category = "SME & Commerce",
            description = "Electricians, plumbers, and carpenters struggle to showcase verified credentials, while homeowners fear scams. We need a peer-reviewed trust network.",
            submittedBy = "Kangwa P.",
            location = "Ndola",
            votes = 63,
            timestamp = "1 week ago"
        )
    )

    private val _projects = MutableStateFlow<List<ProjectItem>>(emptyList())
    val projects: StateFlow<List<ProjectItem>> = _projects.asStateFlow()

    private val _challenges = MutableStateFlow<List<CommunityChallenge>>(emptyList())
    val challenges: StateFlow<List<CommunityChallenge>> = _challenges.asStateFlow()

    private val _memberProfile = MutableStateFlow<MemberProfile?>(null)
    val memberProfile: StateFlow<MemberProfile?> = _memberProfile.asStateFlow()

    private val _triviaScore = MutableStateFlow(0)
    val triviaScore: StateFlow<Int> = _triviaScore.asStateFlow()

    init {
        loadProjects()
        loadChallenges()
        loadMemberProfile()
        loadTriviaScore()
    }

    private fun loadProjects() {
        val saved = prefs.getString("custom_projects", null)
        val list = initialProjects.toMutableList()
        val starredSet = prefs.getStringSet("starred_projects", emptySet()) ?: emptySet()

        if (saved != null) {
            try {
                val array = JSONArray(saved)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    val techStackArray = obj.getJSONArray("techStack")
                    val techList = mutableListOf<String>()
                    for (j in 0 until techStackArray.length()) {
                        techList.add(techStackArray.getString(j))
                    }
                    list.add(
                        0,
                        ProjectItem(
                            id = obj.getString("id"),
                            title = obj.getString("title"),
                            track = obj.getString("track"),
                            tagline = obj.getString("tagline"),
                            problemStatement = obj.getString("problemStatement"),
                            solutionDescription = obj.getString("solutionDescription"),
                            techStack = techList,
                            stage = ProjectStage.valueOf(obj.optString("stage", "IDEA")),
                            location = obj.optString("location", "Lusaka, Zambia"),
                            leadDeveloper = obj.optString("leadDeveloper", "Community Contributor"),
                            stars = obj.optInt("stars", 1)
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        _projects.value = list.map { proj ->
            proj.copy(isStarred = starredSet.contains(proj.id))
        }
    }

    private fun loadChallenges() {
        val saved = prefs.getString("custom_challenges", null)
        val list = initialChallenges.toMutableList()
        val upvotedSet = prefs.getStringSet("upvoted_challenges", emptySet()) ?: emptySet()

        if (saved != null) {
            try {
                val array = JSONArray(saved)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        0,
                        CommunityChallenge(
                            id = obj.getString("id"),
                            title = obj.getString("title"),
                            category = obj.getString("category"),
                            description = obj.getString("description"),
                            submittedBy = obj.getString("submittedBy"),
                            location = obj.getString("location"),
                            votes = obj.getInt("votes"),
                            timestamp = obj.getString("timestamp"),
                            status = obj.optString("status", "Open for Collaboration")
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        _challenges.value = list.map { chal ->
            val isUp = upvotedSet.contains(chal.id)
            chal.copy(
                isUpvoted = isUp,
                votes = if (isUp) chal.votes + 1 else chal.votes
            )
        }
    }

    private fun loadMemberProfile() {
        val exists = prefs.getBoolean("has_profile", false)
        if (exists) {
            val name = prefs.getString("prof_name", "Innocent Phiri") ?: "Innocent Phiri"
            val handle = prefs.getString("prof_handle", "@innocent_zed") ?: "@innocent_zed"
            val email = prefs.getString("prof_email", "phiriinnocent444@gmail.com") ?: "phiriinnocent444@gmail.com"
            val role = prefs.getString("prof_role", "Software Innovator") ?: "Software Innovator"
            val skill = prefs.getString("prof_skill", "Kotlin & Android") ?: "Kotlin & Android"
            val location = prefs.getString("prof_loc", "Lusaka, Zambia") ?: "Lusaka, Zambia"
            val org = prefs.getString("prof_org", "Next Gen Developers Zambia") ?: "Next Gen Developers Zambia"
            val memberId = prefs.getString("prof_id", "NGDZ-2026-0442") ?: "NGDZ-2026-0442"
            val date = prefs.getString("prof_date", "October 2026") ?: "October 2026"
            val bio = prefs.getString("prof_bio", "Passionate about building scalable mobile and AI solutions to solve everyday Zambian challenges. Motto: Think. Create. Solve.") ?: ""

            _memberProfile.value = MemberProfile(
                fullName = name,
                developerHandle = handle,
                email = email,
                role = role,
                primarySkill = skill,
                secondarySkills = listOf("Jetpack Compose", "Coroutines", "API Design", "Agile"),
                location = location,
                institutionOrOrg = org,
                memberId = memberId,
                joinedDate = date,
                bio = bio
            )
        } else {
            // Default active member profile
            _memberProfile.value = MemberProfile(
                fullName = "Innocent Phiri",
                developerHandle = "@innocent_zed",
                email = "phiriinnocent444@gmail.com",
                role = "Lead Developer & Innovator",
                primarySkill = "Android & Kotlin",
                secondarySkills = listOf("Jetpack Compose", "System Architecture", "Problem Solving", "Cloud APIs"),
                location = "Lusaka, Zambia",
                institutionOrOrg = "Next Gen Developers Zambia",
                memberId = "NGDZ-2026-0101",
                joinedDate = "October 2026",
                bio = "Dedicated to turning ideas into action. Passionate about empowering Zambian youth through technology. Motto: Think. Create. Solve."
            )
        }
    }

    fun saveMemberProfile(profile: MemberProfile) {
        prefs.edit()
            .putBoolean("has_profile", true)
            .putString("prof_name", profile.fullName)
            .putString("prof_handle", profile.developerHandle)
            .putString("prof_email", profile.email)
            .putString("prof_role", profile.role)
            .putString("prof_skill", profile.primarySkill)
            .putString("prof_loc", profile.location)
            .putString("prof_org", profile.institutionOrOrg)
            .putString("prof_id", profile.memberId)
            .putString("prof_date", profile.joinedDate)
            .putString("prof_bio", profile.bio)
            .apply()

        _memberProfile.value = profile
    }

    fun toggleStarProject(projectId: String) {
        val currentSet = prefs.getStringSet("starred_projects", emptySet())?.toMutableSet() ?: mutableSetOf()
        val willBeStarred = !currentSet.contains(projectId)
        if (willBeStarred) {
            currentSet.add(projectId)
        } else {
            currentSet.remove(projectId)
        }
        prefs.edit().putStringSet("starred_projects", currentSet).apply()

        _projects.value = _projects.value.map { proj ->
            if (proj.id == projectId) {
                proj.copy(
                    isStarred = willBeStarred,
                    stars = if (willBeStarred) proj.stars + 1 else maxOf(0, proj.stars - 1)
                )
            } else {
                proj
            }
        }
    }

    fun toggleUpvoteChallenge(challengeId: String) {
        val currentSet = prefs.getStringSet("upvoted_challenges", emptySet())?.toMutableSet() ?: mutableSetOf()
        val willBeUpvoted = !currentSet.contains(challengeId)
        if (willBeUpvoted) {
            currentSet.add(challengeId)
        } else {
            currentSet.remove(challengeId)
        }
        prefs.edit().putStringSet("upvoted_challenges", currentSet).apply()

        _challenges.value = _challenges.value.map { chal ->
            if (chal.id == challengeId) {
                chal.copy(
                    isUpvoted = willBeUpvoted,
                    votes = if (willBeUpvoted) chal.votes + 1 else maxOf(0, chal.votes - 1)
                )
            } else {
                chal
            }
        }
    }

    fun submitChallenge(title: String, category: String, description: String, location: String, submittedBy: String) {
        val newId = "chal_${System.currentTimeMillis()}"
        val newChallenge = CommunityChallenge(
            id = newId,
            title = title,
            category = category,
            description = description,
            submittedBy = submittedBy.ifBlank { "NextGen Member" },
            location = location.ifBlank { "Lusaka, Zambia" },
            votes = 1,
            isUpvoted = true,
            timestamp = "Just now",
            status = "Open for Collaboration"
        )

        val updatedList = listOf(newChallenge) + _challenges.value
        _challenges.value = updatedList

        // Mark as upvoted
        val currentSet = prefs.getStringSet("upvoted_challenges", emptySet())?.toMutableSet() ?: mutableSetOf()
        currentSet.add(newId)
        prefs.edit().putStringSet("upvoted_challenges", currentSet).apply()

        // Persist to custom_challenges
        saveCustomChallenges(newChallenge)
    }

    private fun saveCustomChallenges(newChallenge: CommunityChallenge) {
        val saved = prefs.getString("custom_challenges", null)
        val array = if (saved != null) JSONArray(saved) else JSONArray()
        val obj = JSONObject().apply {
            put("id", newChallenge.id)
            put("title", newChallenge.title)
            put("category", newChallenge.category)
            put("description", newChallenge.description)
            put("submittedBy", newChallenge.submittedBy)
            put("location", newChallenge.location)
            put("votes", newChallenge.votes)
            put("timestamp", newChallenge.timestamp)
            put("status", newChallenge.status)
        }
        array.put(obj)
        prefs.edit().putString("custom_challenges", array.toString()).apply()
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
        val newId = "proj_${System.currentTimeMillis()}"
        val newProject = ProjectItem(
            id = newId,
            title = title,
            track = track,
            tagline = tagline,
            problemStatement = problemStatement,
            solutionDescription = solutionDescription,
            techStack = techStack,
            stage = stage,
            location = location.ifBlank { "Lusaka, Zambia" },
            leadDeveloper = leadDeveloper.ifBlank { "NextGen Innovator" },
            stars = 1,
            isStarred = true
        )

        _projects.value = listOf(newProject) + _projects.value

        val starredSet = prefs.getStringSet("starred_projects", emptySet())?.toMutableSet() ?: mutableSetOf()
        starredSet.add(newId)
        prefs.edit().putStringSet("starred_projects", starredSet).apply()

        // Persist
        val saved = prefs.getString("custom_projects", null)
        val array = if (saved != null) JSONArray(saved) else JSONArray()
        val obj = JSONObject().apply {
            put("id", newProject.id)
            put("title", newProject.title)
            put("track", newProject.track)
            put("tagline", newProject.tagline)
            put("problemStatement", newProject.problemStatement)
            put("solutionDescription", newProject.solutionDescription)
            val stackArr = JSONArray()
            techStack.forEach { stackArr.put(it) }
            put("techStack", stackArr)
            put("stage", newProject.stage.name)
            put("location", newProject.location)
            put("leadDeveloper", newProject.leadDeveloper)
            put("stars", newProject.stars)
        }
        array.put(obj)
        prefs.edit().putString("custom_projects", array.toString()).apply()
    }

    private fun loadTriviaScore() {
        _triviaScore.value = prefs.getInt("trivia_score", 0)
    }

    fun incrementTriviaScore(points: Int = 10) {
        val newScore = _triviaScore.value + points
        _triviaScore.value = newScore
        prefs.edit().putInt("trivia_score", newScore).apply()
    }

    val triviaQuestions: List<TriviaQuestion> = listOf(
        TriviaQuestion(
            id = 1,
            question = "In Next Gen Developers Zambia's motto 'Think. Create. Solve.', which phase focuses on understanding user needs before writing code?",
            codeSnippet = null,
            options = listOf("Create", "Think", "Solve", "Deploy"),
            correctIndex = 1,
            explanation = "Under the 'Think' phase and our core value of Problem-Solving, we prioritize deeply understanding challenges before jumping into implementation.",
            category = "Manifesto & Philosophy"
        ),
        TriviaQuestion(
            id = 2,
            question = "Which Kotlin coroutine builder is recommended for launching asynchronous background work that should be cancelled when a ViewModel is cleared?",
            codeSnippet = "class MyViewModel : ViewModel() {\n  fun fetchData() {\n    // Which scope?\n  }\n}",
            options = listOf("GlobalScope.launch", "viewModelScope.launch", "Thread.run", "runBlocking"),
            correctIndex = 1,
            explanation = "viewModelScope is tied to the ViewModel lifecycle and automatically cancels coroutines when onCleared() is triggered, preventing memory leaks.",
            category = "Kotlin & Android Architecture"
        ),
        TriviaQuestion(
            id = 3,
            question = "When building mobile apps for Zambian users in rural and peri-urban areas, which architectural priority is most critical?",
            codeSnippet = null,
            options = listOf(
                "Offline-first local caching and low bandwidth resilience",
                "High-resolution 4K video autoplay",
                "Mandatory 5G persistent web sockets",
                "Web-only rendering without native device support"
            ),
            correctIndex = 0,
            explanation = "Zambian developers prioritize offline-first data caching (via Room or local datastores) and low data consumption to ensure practical accessibility for all citizens.",
            category = "Practical Engineering in Zambia"
        ),
        TriviaQuestion(
            id = 4,
            question = "In Jetpack Compose, which state holder API allows a composable to survive configuration changes (e.g. screen rotation)?",
            codeSnippet = "var counter by remember { mutableStateOf(0) } // Survives rotation?",
            options = listOf("remember", "rememberSaveable", "derivedStateOf", "produceState"),
            correctIndex = 1,
            explanation = "rememberSaveable automatically saves state in the Android SavedStateRegistry (Bundle), surviving activity recreation on rotation.",
            category = "Jetpack Compose"
        ),
        TriviaQuestion(
            id = 5,
            question = "What is the primary tech hub and innovation space historically known in Lusaka for fostering Zambian developers and startups?",
            codeSnippet = null,
            options = listOf("Silicon Valley Hub", "BongoHive Technology and Innovation Hub", "Nairobi Garage", "Jozi Tech Zone"),
            correctIndex = 1,
            explanation = "BongoHive, founded in Lusaka in 2011, is Zambia's pioneer technology and innovation hub, inspiring the growth of developer groups like Next Gen Developers Zambia.",
            category = "Zambian Tech Ecosystem"
        ),
        TriviaQuestion(
            id = 6,
            question = "Which Objective from the Next Gen Developers Zambia charter specifically emphasizes transforming ideas into useful applications?",
            codeSnippet = null,
            options = listOf("Objective 2: Develop Innovative Solutions", "Objective 5: Experiment and Improve", "Objective 3: Encourage Teamwork", "Objective 7: Technological Development"),
            correctIndex = 0,
            explanation = "Objective 2 explicitly states: 'To transform ideas into useful applications, systems, and other solutions that respond to existing needs.'",
            category = "Charter & Objectives"
        )
    )
}
