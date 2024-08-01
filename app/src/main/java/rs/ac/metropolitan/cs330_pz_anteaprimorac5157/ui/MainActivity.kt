package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.navigation.NavSetup
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.navigation.NavigationRoutes
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.theme.CS330PZAnteaPrimorac5157Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CS330PZAnteaPrimorac5157Theme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomNavigation(navController)
                    }
                ) { innerPadding ->
                    NavSetup(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.List, contentDescription = "Diary") },
            label = { Text("Diary") },
            selected = navController.currentDestination?.route == NavigationRoutes.Diary.route,
            onClick = { navController.navigate(NavigationRoutes.Diary.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Account") },
            label = { Text("Account") },
            selected = navController.currentDestination?.route == NavigationRoutes.Account.route,
            onClick = { navController.navigate(NavigationRoutes.Account.route) }
        )
    }
}