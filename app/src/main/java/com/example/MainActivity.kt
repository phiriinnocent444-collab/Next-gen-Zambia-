package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.NextGenViewModel
import com.example.ui.components.NextGenTopAppBar
import com.example.ui.screens.*
import com.example.ui.theme.*

data class NavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

class MainActivity : ComponentActivity() {

    private val viewModel: NextGenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NextGenZambiaTheme {
                NextGenApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun NextGenApp(viewModel: NextGenViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle back button: if not on Tab 0 (Charter), navigate to Tab 0
    BackHandler(enabled = uiState.currentTab != 0) {
        viewModel.setTab(0)
    }

    // Display snackbars
    LaunchedEffect(uiState.messageSnackbar) {
        uiState.messageSnackbar?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    val navItems = listOf(
        NavItem("Charter", Icons.Default.MenuBook, Icons.Outlined.MenuBook, "nav_charter"),
        NavItem("Projects", Icons.Default.RocketLaunch, Icons.Outlined.RocketLaunch, "nav_projects"),
        NavItem("Think", Icons.Default.Lightbulb, Icons.Outlined.Lightbulb, "nav_incubator"),
        NavItem("Solve", Icons.Default.Psychology, Icons.Outlined.Psychology, "nav_trivia"),
        NavItem("Pass", Icons.Default.Badge, Icons.Outlined.Badge, "nav_pass")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            NextGenTopAppBar(
                currentTab = uiState.currentTab,
                onTabSelect = { viewModel.setTab(it) }
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("main_navigation_bar"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                navItems.forEachIndexed { index, item ->
                    val isSelected = uiState.currentTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.setTab(index) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title,
                                tint = if (isSelected) ZambiaCopperLight else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 11.sp
                                )
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = ZambiaCopper.copy(alpha = 0.2f),
                            selectedTextColor = ZambiaCopperLight,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.testTag(item.testTag)
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(
                targetState = uiState.currentTab,
                label = "ScreenTransition"
            ) { tabIndex ->
                when (tabIndex) {
                    0 -> ManifestoScreen(viewModel = viewModel, onNavigateToTab = { viewModel.setTab(it) })
                    1 -> ProjectsScreen(viewModel = viewModel)
                    2 -> IncubatorScreen(viewModel = viewModel)
                    3 -> TriviaScreen(viewModel = viewModel)
                    4 -> MemberPassScreen(viewModel = viewModel)
                }
            }
        }
    }
}
