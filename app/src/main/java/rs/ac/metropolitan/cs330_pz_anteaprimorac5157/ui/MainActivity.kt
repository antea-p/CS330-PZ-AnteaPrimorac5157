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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.navigation.NavigationRoutes
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.theme.CS330PZAnteaPrimorac5157Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CS330PZAnteaPrimorac5157Theme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = navController.currentDestination?.route == NavigationRoutes.Diary.route,
                    onClick = { navController.navigate(NavigationRoutes.Diary.route) },
                    icon = { Icon(Icons.Default.List, contentDescription = "Diary") },
                    label = { Text("Diary") }
                )
                NavigationBarItem(
                    selected = navController.currentDestination?.route == NavigationRoutes.Account.route,
                    onClick = { navController.navigate(NavigationRoutes.Account.route) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Account") },
                    label = { Text("Account") }
                )
            }
        }
    ) { innerPadding ->
        // TODO: refaktorirati kad se povea broj ruta
        NavHost(
            navController = navController,
            startDestination = NavigationRoutes.Diary.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavigationRoutes.Diary.route) {
                DiaryScreen()
            }
            composable(NavigationRoutes.Account.route) {
                AccountScreen()
            }
        }
    }
}

@Composable
fun DiaryScreen() {
    Text("Diary Screen")
}

@Composable
fun AccountScreen() {
    Text("Account Screen")
}