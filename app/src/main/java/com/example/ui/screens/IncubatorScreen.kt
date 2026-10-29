package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ThumbUp
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
import com.example.data.models.CommunityChallenge
import com.example.ui.NextGenViewModel
import com.example.ui.theme.*

@Composable
fun IncubatorScreen(viewModel: NextGenViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filteredChallenges by viewModel.filteredChallenges.collectAsStateWithLifecycle()

    val categories = listOf("All", "Transport", "Civic", "AI", "SME", "Agriculture", "Education")

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("incubator_screen_list"),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = ZambiaGold.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "MOTTO: THINK",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ZambiaCopperLight
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Problem Discovery Hub",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = ZambiaGreenLight,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Real-World Challenges",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Objective 1: 'Identify challenges affecting individuals, businesses, and communities and develop practical solutions.'",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Search
                    OutlinedTextField(
                        value = uiState.challengeSearchQuery,
                        onValueChange = { viewModel.setChallengeSearchQuery(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("challenge_search_field"),
                        placeholder = { Text("Search community problems, cities, keywords...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = ZambiaGold)
                        },
                        trailingIcon = {
                            if (uiState.challengeSearchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setChallengeSearchQuery("") }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ZambiaGold,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Categories
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(categories) { cat ->
                            val isSelected = uiState.challengeCategoryFilter == cat
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setChallengeCategoryFilter(cat) },
                                label = { Text(cat, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ZambiaGold,
                                    selectedLabelColor = Color.Black
                                ),
                                modifier = Modifier.testTag("challenge_category_$cat")
                            )
                        }
                    }
                }
            }

            if (filteredChallenges.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            modifier = Modifier.size(54.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No problems found matching \"${uiState.challengeSearchQuery}\"",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                viewModel.setChallengeSearchQuery("")
                                viewModel.setChallengeCategoryFilter("All")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ZambiaCopper)
                        ) {
                            Text("Reset Search")
                        }
                    }
                }
            } else {
                items(filteredChallenges, key = { it.id }) { challenge ->
                    ChallengeCard(
                        challenge = challenge,
                        onUpvoteToggle = { viewModel.toggleUpvoteChallenge(challenge.id) }
                    )
                }
            }
        }

        // FAB to propose a challenge
        FloatingActionButton(
            onClick = { viewModel.setShowSubmitChallengeDialog(true) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 90.dp)
                .testTag("fab_submit_challenge"),
            containerColor = ZambiaGold,
            contentColor = Color.Black
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.AddComment, contentDescription = "Propose Problem")
                Spacer(modifier = Modifier.width(6.dp))
                Text("Propose Challenge", fontWeight = FontWeight.Bold)
            }
        }
    }

    // Submit Challenge Dialog
    if (uiState.showSubmitChallengeDialog) {
        SubmitChallengeDialog(
            onDismiss = { viewModel.setShowSubmitChallengeDialog(false) },
            onSubmit = { title, cat, desc, loc, author ->
                viewModel.submitChallenge(title, cat, desc, loc, author)
            }
        )
    }
}

@Composable
private fun ChallengeCard(
    challenge: CommunityChallenge,
    onUpvoteToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("challenge_card_${challenge.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Category & Status
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    color = ZambiaCopper.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = challenge.category,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = ZambiaCopperLight,
                            fontSize = 10.5.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Surface(
                    color = ZambiaGreenDark.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = challenge.status,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ZambiaGreenLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = challenge.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = challenge.description,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Footer with author, location and Upvote button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${challenge.submittedBy} • ${challenge.location}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    )
                }

                // Upvote Button
                Button(
                    onClick = onUpvoteToggle,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (challenge.isUpvoted) ZambiaCopper else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (challenge.isUpvoted) Color.White else MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(
                        imageVector = if (challenge.isUpvoted) Icons.Default.ThumbUp else Icons.Outlined.ThumbUp,
                        contentDescription = "Upvote",
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${challenge.votes}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
private fun SubmitChallengeDialog(
    onDismiss: () -> Unit,
    onSubmit: (title: String, category: String, description: String, location: String, author: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Civic Infrastructure") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("Lusaka, Zambia") }
    var author by remember { mutableStateOf("") }

    val categoryList = listOf("Transport & Mobility", "Civic Infrastructure", "Agriculture", "AI & Linguistics", "SME & Commerce", "Healthcare", "Education")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Propose Community Challenge",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 380.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "Highlight a specific friction or hurdle affecting Zambian citizens, businesses, or local communities.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Challenge Title *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    Text("Category:", style = MaterialTheme.typography.labelMedium)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(categoryList) { c ->
                            FilterChip(
                                selected = category == c,
                                onClick = { category = c },
                                label = { Text(c, fontSize = 11.sp) }
                            )
                        }
                    }
                }
                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Detailed Problem Description *") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
                item {
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Zambian City / Province") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = author,
                        onValueChange = { author = it },
                        label = { Text("Your Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank()) {
                        onSubmit(title, category, description, location, author)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ZambiaGold, contentColor = Color.Black),
                enabled = title.isNotBlank() && description.isNotBlank()
            ) {
                Text("Submit Challenge", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
