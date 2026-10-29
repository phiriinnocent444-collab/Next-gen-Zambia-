package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.models.ProjectStage
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NextGenTopAppBar(
    currentTab: Int,
    onTabSelect: (Int) -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(ZambiaGreenDark)
                        .border(1.dp, ZambiaCopperLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = R.drawable.ic_app_logo),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Next Gen Developers",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        ),
                        maxLines = 1
                    )
                    Text(
                        text = "ZAMBIA \uD83C\uDDFF\uD83C\uDDF2 \u2022 Think. Create. Solve.",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ZambiaCopper,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        ),
                        maxLines = 1
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = { onTabSelect(4) }, // Navigate to developer pass
                modifier = Modifier.testTag("nav_profile_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Badge,
                    contentDescription = "Developer Pass",
                    tint = ZambiaCopperLight
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    badgeText: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(ZambiaCopper, ZambiaGreenLight)
                            )
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            if (badgeText != null) {
                Surface(
                    color = ZambiaCopper.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = ZambiaCopperLight
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@Composable
fun StageChip(stage: ProjectStage) {
    val (bgColor, textColor, label) = when (stage) {
        ProjectStage.IDEA -> Triple(Color(0xFF384358), Color(0xFF90CAF9), "Idea Phase")
        ProjectStage.PROTOTYPING -> Triple(Color(0xFF5A3800), Color(0xFFFFB74D), "Prototyping")
        ProjectStage.TESTING -> Triple(Color(0xFF4A148C).copy(alpha = 0.6f), Color(0xFFCE93D8), "Testing")
        ProjectStage.DEPLOYED -> Triple(Color(0xFF1B5E20).copy(alpha = 0.6f), Color(0xFFA5D6A7), "Active & Live")
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = textColor
                )
            )
        }
    }
}

@Composable
fun TechChip(tech: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(6.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        Text(
            text = tech,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
    }
}

fun resolveIcon(key: String): ImageVector {
    return when (key) {
        "ProblemSolving", "Psychology" -> Icons.Default.Psychology
        "Innovation", "Lightbulb" -> Icons.Default.Lightbulb
        "Collaboration", "Group" -> Icons.Default.Groups
        "Code", "LaptopMac" -> Icons.Default.Code
        "Experiment", "Build" -> Icons.Default.Science
        "Projects" -> Icons.Default.WorkOutline
        "Country" -> Icons.Default.Public
        "VolunteerActivism" -> Icons.Default.VolunteerActivism
        "TrendingUp" -> Icons.Default.TrendingUp
        "VerifiedUser" -> Icons.Default.VerifiedUser
        "AutoAwesome" -> Icons.Default.AutoAwesome
        "Storefront" -> Icons.Default.Storefront
        "Hub" -> Icons.Default.Hub
        "MenuBook" -> Icons.Default.MenuBook
        else -> Icons.Default.Star
    }
}
