package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.NextGenViewModel
import com.example.ui.theme.*

@Composable
fun TriviaScreen(viewModel: NextGenViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val triviaScore by viewModel.triviaScore.collectAsStateWithLifecycle()
    val questions = viewModel.repository.triviaQuestions
    val currentQuestion = questions.getOrNull(uiState.currentTriviaIndex)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("trivia_screen_list"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Top Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Daily Dev Challenge",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Think. Create. Solve. • Engineering Quiz",
                        style = MaterialTheme.typography.labelMedium.copy(color = ZambiaCopperLight)
                    )
                }

                // Points Badge
                Surface(
                    color = ZambiaGreenDark,
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ZambiaGreenLight)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Points",
                            tint = ZambiaGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$triviaScore XP",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (currentQuestion != null) {
            // Question Progress
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = ZambiaCopper.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = currentQuestion.category,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = ZambiaCopperLight
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = "Question ${uiState.currentTriviaIndex + 1} of ${questions.size}",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { (uiState.currentTriviaIndex + 1) / questions.size.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = ZambiaCopper,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Question Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = currentQuestion.question,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 24.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // Code snippet box if present
                        if (currentQuestion.codeSnippet != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = DarkBackground,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
                            ) {
                                Text(
                                    text = currentQuestion.codeSnippet,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                        color = ZambiaGreenLight
                                    ),
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Options
            items(currentQuestion.options.size) { index ->
                val optionText = currentQuestion.options[index]
                val isSelected = uiState.selectedAnswerIndex == index
                val isSubmitted = uiState.answerSubmitted
                val isCorrectAnswer = index == currentQuestion.correctIndex

                val (borderColor, containerColor, textColor) = when {
                    isSubmitted && isCorrectAnswer -> Triple(ZambiaGreenLight, ZambiaGreenDark.copy(alpha = 0.3f), ZambiaGreenLight)
                    isSubmitted && isSelected && !isCorrectAnswer -> Triple(ZambiaRed, ZambiaRed.copy(alpha = 0.15f), ZambiaRed)
                    isSelected -> Triple(ZambiaCopper, ZambiaCopper.copy(alpha = 0.12f), MaterialTheme.colorScheme.onSurface)
                    else -> Triple(MaterialTheme.colorScheme.outline, MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.onSurface)
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(enabled = !isSubmitted) {
                            viewModel.selectTriviaAnswer(index)
                        }
                        .testTag("trivia_option_$index"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = containerColor),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected || (isSubmitted && isCorrectAnswer)) borderColor else MaterialTheme.colorScheme.surfaceVariant
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = ('A' + index).toString(),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected || (isSubmitted && isCorrectAnswer)) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = textColor
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        if (isSubmitted && isCorrectAnswer) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Correct",
                                tint = ZambiaGreenLight
                            )
                        } else if (isSubmitted && isSelected && !isCorrectAnswer) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "Incorrect",
                                tint = ZambiaRed
                            )
                        }
                    }
                }
            }

            // Action / Explanation
            item {
                Spacer(modifier = Modifier.height(14.dp))

                if (!uiState.answerSubmitted) {
                    Button(
                        onClick = { viewModel.submitTriviaAnswer() },
                        enabled = uiState.selectedAnswerIndex != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("trivia_submit_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = ZambiaCopper)
                    ) {
                        Text("Check Answer", fontWeight = FontWeight.Bold)
                    }
                } else {
                    // Explanation Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (uiState.isAnswerCorrect) ZambiaGreenDark.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (uiState.isAnswerCorrect) ZambiaGreenLight else MaterialTheme.colorScheme.outline
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (uiState.isAnswerCorrect) Icons.Default.CheckCircle else Icons.Default.Info,
                                    contentDescription = null,
                                    tint = if (uiState.isAnswerCorrect) ZambiaGreenLight else ZambiaCopperLight,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (uiState.isAnswerCorrect) "Correct! +15 XP" else "Learning Opportunity",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (uiState.isAnswerCorrect) ZambiaGreenLight else ZambiaCopperLight
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = currentQuestion.explanation,
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.nextTriviaQuestion() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("trivia_next_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = ZambiaGreen)
                    ) {
                        Text("Next Question →", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}
