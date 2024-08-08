package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.screens.AccountScreen
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.screens.CreateEntryScreen
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.screens.DiaryScreen
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.screens.DiaryEntryDetailsScreen

@Composable
fun NavSetup(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var needsDiaryRefresh by remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.Diary.route,
        modifier = modifier
    ) {
        composable(NavigationRoutes.Diary.route) {
            DiaryScreen(
                onCreateEntry = { navController.navigate(NavigationRoutes.CreateEntry.route) },
                onEntryClick = { id ->
                    navController.navigate("${NavigationRoutes.DiaryEntryDetails.route}/${id}")
                },
                needsRefresh = needsDiaryRefresh,
                onRefreshComplete = { needsDiaryRefresh = false }
            )
        }
        composable(NavigationRoutes.Account.route) {
            AccountScreen()
        }
        composable(
            route = "${NavigationRoutes.DiaryEntryDetails.route}/{entryId}",
            arguments = listOf(navArgument("entryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val entryId = backStackEntry.arguments?.getInt("entryId") ?: return@composable
            DiaryEntryDetailsScreen(
                entryId = entryId,
                onNavigateBack = {
                    needsDiaryRefresh = true
                    navController.popBackStack()
                },
//                onEntryDeleted = {
//                    navController.popBackStack(NavigationRoutes.Diary.route, false)
//                }
            )
        }
        composable(NavigationRoutes.CreateEntry.route) {
            CreateEntryScreen(
                onNavigateBack = {
                    needsDiaryRefresh = true
                    navController.popBackStack()
                }
            )
        }
    }
}