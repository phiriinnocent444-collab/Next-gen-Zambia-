package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.models.ProjectItem
import com.example.data.models.ProjectStage
import com.example.ui.NextGenViewModel
import com.example.ui.components.StageChip
import com.example.ui.components.TechChip
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(viewModel: NextGenViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filteredProjects by viewModel.filteredProjects.collectAsStateWithLifecycle()

    val tracks = listOf("All", "AgriTech", "FinTech", "HealthTech", "EduTech", "CleanTech")

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("projects_list"),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // Header Info
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Projects & Innovation Lab",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Motto: Create & Solve. Practical technological solutions responding to real-world challenges in Zambia.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Search Field
                    OutlinedTextField(
                        value = uiState.projectSearchQuery,
                        onValueChange = { viewModel.setProjectSearchQuery(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("project_search_field"),
                        placeholder = { Text("Search solutions, tech stack, problems...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = ZambiaCopper)
                        },
                        trailingIcon = {
                            if (uiState.projectSearchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setProjectSearchQuery("") }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ZambiaCopper,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Track filter chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(tracks) { track ->
                            val isSelected = uiState.projectTrackFilter == track
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setProjectTrackFilter(track) },
                                label = { Text(track, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ZambiaCopper,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.testTag("filter_chip_$track")
                            )
                        }
                    }
                }
            }

            if (filteredProjects.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            modifier = Modifier.size(54.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No projects matching \"${uiState.projectSearchQuery}\"",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                viewModel.setProjectSearchQuery("")
                                viewModel.setProjectTrackFilter("All")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ZambiaCopper)
                        ) {
                            Text("Reset Filters")
                        }
                    }
                }
            } else {
                items(filteredProjects, key = { it.id }) { project ->
                    ProjectCard(
                        project = project,
                        onStarToggle = { viewModel.toggleStarProject(project.id) },
                        onSelect = { viewModel.selectProject(project) }
                    )
                }
            }
        }

        // Floating Action Button to Pitch Project
        FloatingActionButton(
            onClick = { viewModel.setShowAddProjectDialog(true) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 90.dp)
                .testTag("fab_add_project"),
            containerColor = ZambiaCopper,
            contentColor = Color.White
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Add, contentDescription = "Pitch Project")
                Spacer(modifier = Modifier.width(6.dp))
                Text("Pitch Project", fontWeight = FontWeight.Bold)
            }
        }
    }

    // Project Details Sheet
    if (uiState.selectedProject != null) {
        ProjectDetailBottomSheet(
            project = uiState.selectedProject!!,
            onDismiss = { viewModel.selectProject(null) },
            onStarToggle = { viewModel.toggleStarProject(uiState.selectedProject!!.id) }
        )
    }

    // Add Project Dialog
    if (uiState.showAddProjectDialog) {
        AddProjectDialog(
            onDismiss = { viewModel.setShowAddProjectDialog(false) },
            onSubmit = { title, track, tagline, problem, solution, techList, stage, location, lead ->
                viewModel.submitProject(title, track, tagline, problem, solution, techList, stage, location, lead)
            }
        )
    }
}

@Composable
private fun ProjectCard(
    project: ProjectItem,
    onStarToggle: () -> Unit,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onSelect() }
            .testTag("project_card_${project.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Row: Track & Stage
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = ZambiaGreenDark.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, ZambiaGreenLight.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = project.track,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = ZambiaGreenLight,
                                fontSize = 11.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    StageChip(stage = project.stage)
                }

                // Star Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onStarToggle() }
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = if (project.isStarred) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Star Project",
                        tint = if (project.isStarred) ZambiaGold else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${project.stars}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = project.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = project.tagline,
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Tech Stack chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                project.techStack.take(3).forEach { tech ->
                    TechChip(tech = tech)
                }
                if (project.techStack.size > 3) {
                    TechChip(tech = "+${project.techStack.size - 3}")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = ZambiaCopperLight
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = project.location,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    )
                }

                Text(
                    text = "View Architecture →",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = ZambiaCopperLight
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectDetailBottomSheet(
    project: ProjectItem,
    onDismiss: () -> Unit,
    onStarToggle: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StageChip(stage = project.stage)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = project.track,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ZambiaCopperLight
                        )
                    )
                }
                IconButton(onClick = onStarToggle) {
                    Icon(
                        imageVector = if (project.isStarred) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Star",
                        tint = if (project.isStarred) ZambiaGold else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = project.title,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = project.tagline,
                style = MaterialTheme.typography.bodyMedium.copy(color = ZambiaCopperLight)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // The Zambian Problem
            Text(
                text = "The Real-World Challenge (Zambia)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = ZambiaRed
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = project.problemStatement,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // The Technological Solution
            Text(
                text = "Practical Technological Solution",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = ZambiaGreenLight
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = project.solutionDescription,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tech Stack
            Text(
                text = "Tech Stack & Tools",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                project.techStack.forEach { tech ->
                    TechChip(tech = tech)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lead & Location
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Lead / Squad",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Text(
                        text = project.leadDeveloper,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Deployment Target",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Text(
                        text = project.location,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = ZambiaCopper)
            ) {
                Icon(Icons.Default.GroupAdd, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Join Project Squad / Collaborate", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun AddProjectDialog(
    onDismiss: () -> Unit,
    onSubmit: (
        title: String,
        track: String,
        tagline: String,
        problem: String,
        solution: String,
        techList: List<String>,
        stage: ProjectStage,
        location: String,
        lead: String
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var track by remember { mutableStateOf("AgriTech") }
    var tagline by remember { mutableStateOf("") }
    var problem by remember { mutableStateOf("") }
    var solution by remember { mutableStateOf("") }
    var techStackInput by remember { mutableStateOf("Kotlin, Compose, Room") }
    var location by remember { mutableStateOf("Lusaka, Zambia") }
    var lead by remember { mutableStateOf("") }

    val trackOptions = listOf("AgriTech", "FinTech", "HealthTech", "EduTech", "CleanTech", "CivicTech")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Pitch New Project Prototype",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Project Title *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                item {
                    Text("Select Track:", style = MaterialTheme.typography.labelMedium)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(trackOptions) { t ->
                            FilterChip(
                                selected = track == t,
                                onClick = { track = t },
                                label = { Text(t, fontSize = 11.sp) }
                            )
                        }
                    }
                }
                item {
                    OutlinedTextField(
                        value = tagline,
                        onValueChange = { tagline = it },
                        label = { Text("Short Tagline *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = problem,
                        onValueChange = { problem = it },
                        label = { Text("Zambian Problem Statement *") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
                item {
                    OutlinedTextField(
                        value = solution,
                        onValueChange = { solution = it },
                        label = { Text("Technological Solution *") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
                item {
                    OutlinedTextField(
                        value = techStackInput,
                        onValueChange = { techStackInput = it },
                        label = { Text("Tech Stack (comma separated)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location (e.g. Lusaka, Kitwe, Livingstone)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = lead,
                        onValueChange = { lead = it },
                        label = { Text("Your Name / Squad Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && problem.isNotBlank() && solution.isNotBlank()) {
                        val stack = techStackInput.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        onSubmit(
                            title, track, tagline.ifBlank { "Innovative solution for Zambia" },
                            problem, solution, stack, ProjectStage.IDEA, location, lead
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ZambiaCopper),
                enabled = title.isNotBlank() && problem.isNotBlank() && solution.isNotBlank()
            ) {
                Text("Publish to Lab", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
