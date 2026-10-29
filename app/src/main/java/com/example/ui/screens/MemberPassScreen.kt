package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.models.MemberProfile
import com.example.ui.NextGenViewModel
import com.example.ui.components.TechChip
import com.example.ui.theme.*

@Composable
fun MemberPassScreen(viewModel: NextGenViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val memberProfile by viewModel.memberProfile.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val profile = memberProfile ?: MemberProfile(
        fullName = "Innocent Phiri",
        developerHandle = "@innocent_zed",
        email = "phiriinnocent444@gmail.com",
        role = "Lead Developer & Innovator",
        primarySkill = "Android & Kotlin",
        secondarySkills = listOf("Jetpack Compose", "System Architecture", "Problem Solving"),
        location = "Lusaka, Zambia",
        institutionOrOrg = "Next Gen Developers Zambia",
        memberId = "NGDZ-2026-0101",
        joinedDate = "October 2026",
        bio = "Dedicated to turning ideas into action. Passionate about empowering Zambian youth through technology. Motto: Think. Create. Solve."
    )

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("member_pass_screen_list"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
        ) {
            // Header
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Developer Pass",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Next Gen Developers Zambia Credential",
                            style = MaterialTheme.typography.labelMedium.copy(color = ZambiaCopperLight)
                        )
                    }

                    OutlinedButton(
                        onClick = { viewModel.setShowEditProfileDialog(true) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ZambiaCopperLight),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ZambiaCopperLight),
                        modifier = Modifier.testTag("edit_profile_button")
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Edit", fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Digital Developer ID Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("digital_developer_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = DarkSurface
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, ZambiaCopperLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column {
                        // Zambian Flag Ribbon on top
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f).fillMaxHeight().background(ZambiaGreen))
                            Box(modifier = Modifier.weight(1f).fillMaxHeight().background(ZambiaRed))
                            Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color.Black))
                            Box(modifier = Modifier.weight(1f).fillMaxHeight().background(ZambiaCopper))
                        }

                        Column(modifier = Modifier.padding(18.dp)) {
                            // Card Top: Logo + Org Name + ID
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    androidx.compose.foundation.Image(
                                        painter = painterResource(id = R.drawable.ic_app_logo),
                                        contentDescription = "Emblem",
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .border(1.dp, ZambiaCopperLight, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "NEXT GEN DEVELOPERS",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                letterSpacing = 1.sp,
                                                color = Color.White
                                            )
                                        )
                                        Text(
                                            text = "ZAMBIA \uD83C\uDDFF\uD83C\uDDF2 \u2022 MEMBER",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = ZambiaCopperLight,
                                                fontSize = 9.sp
                                            )
                                        )
                                    }
                                }

                                Surface(
                                    color = DarkBackground,
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(0.5.dp, DarkSurfaceBorder)
                                ) {
                                    Text(
                                        text = profile.memberId,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            color = ZambiaGold
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // Member Name & Role
                            Text(
                                text = profile.fullName,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = profile.developerHandle,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = ZambiaCopperLight,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                                Text(
                                    text = " • ${profile.role}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = DarkOnSurfaceVariant)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Details Row with Simulated QR & Info
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    DetailLabel(label = "PRIMARY STACK", value = profile.primarySkill)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    DetailLabel(label = "LOCATION", value = profile.location)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    DetailLabel(label = "JOINED", value = profile.joinedDate)
                                }

                                // Canvas QR Code Pattern
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White)
                                        .padding(4.dp)
                                ) {
                                    CanvasQrCode(seed = profile.memberId.hashCode())
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Bottom Motto Bar on the card
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = ZambiaGreenDark.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(0.5.dp, ZambiaGreenLight.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "MOTTO: THINK. CREATE. SOLVE.",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            letterSpacing = 0.5.sp,
                                            color = ZambiaGreenLight,
                                            fontSize = 9.sp
                                        )
                                    )
                                    Text(
                                        text = "VERIFIED DEVELOPER",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = ZambiaGold,
                                            fontSize = 9.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Copy Credentials Action
                Button(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText(
                            "NextGen Zambia Pass",
                            "Next Gen Developers Zambia | Member: ${profile.fullName} (${profile.memberId}) | Motto: Think. Create. Solve."
                        )
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Credentials copied to clipboard!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("copy_credentials_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = ZambiaCopper)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share / Copy Developer Pass", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Bio & Skills Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Developer Focus & Bio",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = profile.bio,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Core Competencies",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            profile.secondarySkills.forEach { skill ->
                                TechChip(tech = skill)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Official Community Channels
            item {
                Text(
                    text = "Official Community Hubs",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                CommunityHubCard(
                    icon = Icons.Default.Chat,
                    title = "Developer WhatsApp & Discord Guild",
                    description = "Join active technical discussions, solve bugs together, and coordinate local hackathons across Lusaka, Ndola & Kitwe.",
                    actionLabel = "Active Hub"
                )

                Spacer(modifier = Modifier.height(8.dp))

                CommunityHubCard(
                    icon = Icons.Default.Code,
                    title = "Open Source GitHub Organization",
                    description = "Contribute code to public repositories, review PRs, and collaborate on Zambia-focused software tools.",
                    actionLabel = "NextGenDevsZM"
                )

                Spacer(modifier = Modifier.height(8.dp))

                CommunityHubCard(
                    icon = Icons.Default.LocationCity,
                    title = "Lusaka & Regional Meetups",
                    description = "Bi-weekly community coding jams, tech talks, and project showcase demos.",
                    actionLabel = "BongoHive & Innovation Hubs"
                )

                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }

    // Edit Profile Dialog
    if (uiState.showEditProfileDialog) {
        EditProfileDialog(
            currentProfile = profile,
            onDismiss = { viewModel.setShowEditProfileDialog(false) },
            onSave = { updated -> viewModel.saveMemberProfile(updated) }
        )
    }
}

@Composable
private fun DetailLabel(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = DarkOnSurfaceVariant
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        )
    }
}

@Composable
private fun CanvasQrCode(seed: Int) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val gridSize = 6
        val cellW = size.width / gridSize
        val cellH = size.height / gridSize

        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                // Fixed corner anchors
                val isAnchor = (r < 2 && c < 2) || (r < 2 && c >= gridSize - 2) || (r >= gridSize - 2 && c < 2)
                val bit = ((seed + r * 7 + c * 13) % 2 == 0) || isAnchor

                if (bit) {
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(c * cellW, r * cellH),
                        size = Size(cellW, cellH)
                    )
                }
            }
        }
    }
}

@Composable
private fun CommunityHubCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    actionLabel: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(ZambiaCopper.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ZambiaCopperLight,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 16.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = actionLabel,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = ZambiaGreenLight
                    )
                )
            }
        }
    }
}

@Composable
private fun EditProfileDialog(
    currentProfile: MemberProfile,
    onDismiss: () -> Unit,
    onSave: (MemberProfile) -> Unit
) {
    var name by remember { mutableStateOf(currentProfile.fullName) }
    var handle by remember { mutableStateOf(currentProfile.developerHandle) }
    var email by remember { mutableStateOf(currentProfile.email) }
    var role by remember { mutableStateOf(currentProfile.role) }
    var primarySkill by remember { mutableStateOf(currentProfile.primarySkill) }
    var location by remember { mutableStateOf(currentProfile.location) }
    var bio by remember { mutableStateOf(currentProfile.bio) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Edit NextGen Developer Pass", fontWeight = FontWeight.Bold)
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 380.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = handle,
                        onValueChange = { handle = it },
                        label = { Text("Developer Handle (e.g. @zed_coder)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = role,
                        onValueChange = { role = it },
                        label = { Text("Role / Focus (e.g. Android Engineer)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = primarySkill,
                        onValueChange = { primarySkill = it },
                        label = { Text("Primary Tech Stack") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location in Zambia") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = bio,
                        onValueChange = { bio = it },
                        label = { Text("Personal Bio & Motivation") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onSave(
                            currentProfile.copy(
                                fullName = name,
                                developerHandle = if (handle.startsWith("@")) handle else "@$handle",
                                email = email,
                                role = role.ifBlank { "Software Innovator" },
                                primarySkill = primarySkill.ifBlank { "Kotlin & Android" },
                                location = location.ifBlank { "Lusaka, Zambia" },
                                bio = bio
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ZambiaCopper),
                enabled = name.isNotBlank()
            ) {
                Text("Save Pass", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
