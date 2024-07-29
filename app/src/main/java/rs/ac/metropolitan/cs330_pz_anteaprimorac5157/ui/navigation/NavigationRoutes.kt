package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.navigation

sealed class NavigationRoutes(val route: String) {
    object Diary : NavigationRoutes("diary")
    object Account : NavigationRoutes("account")
}