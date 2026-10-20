package com.example.etsppblibraryreg.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.etsppblibraryreg.ui.screens.DashboardScreen
import com.example.etsppblibraryreg.ui.screens.RegistrationScreen
import com.example.etsppblibraryreg.ui.screens.MemberListScreen
import com.example.etsppblibraryreg.viewmodel.LibraryViewModel

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Registration : Screen("registration")
    object MemberList : Screen("member_list")
    object EditMember : Screen("edit_member/{memberId}") {
        fun createRoute(memberId: Int) = "edit_member/$memberId"
    }
}

@Composable
fun AppNavigation(
    viewModel: LibraryViewModel,
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Dashboard.route) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                viewModel = viewModel,
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onNavigateToRegistration = { navController.navigate(Screen.Registration.route) },
                onNavigateToMemberList = { navController.navigate(Screen.MemberList.route) }
            )
        }
        composable(Screen.Registration.route) {
            RegistrationScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.MemberList.route) {
            MemberListScreen(
                viewModel = viewModel,
                onNavigateToEdit = { memberId ->
                    navController.navigate(Screen.EditMember.createRoute(memberId))
                },
                onNavigateToRegistration = { navController.navigate(Screen.Registration.route) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.EditMember.route) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getString("memberId")?.toIntOrNull()
            RegistrationScreen(
                viewModel = viewModel,
                memberIdToEdit = memberId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}