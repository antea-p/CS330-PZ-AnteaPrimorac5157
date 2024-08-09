package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.navigation

sealed class NavigationRoutes(val route: String) {
    object Diary : NavigationRoutes("diary")
    object Account : NavigationRoutes("account")
    object DiaryEntryDetails : NavigationRoutes("diary_entry_details/{entryId}") {
        fun createRoute(entryId: Int) = "diary_entry_details/$entryId"
    }
    object CreateEntry : NavigationRoutes("create_entry?entryId={entryId}") {
        fun createRoute(entryId: Int? = null) = "create_entry${if (entryId != null) "?entryId=$entryId" else ""}"
    }
}