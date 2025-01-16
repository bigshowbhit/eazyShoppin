package hu.bme.aut.U5FT4N.easyshopping

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.U5FT4N.easyshopping.navigation.MainNavigation
import hu.bme.aut.U5FT4N.easyshopping.receiver.BatteryChargingReceiver
import hu.bme.aut.U5FT4N.easyshopping.receiver.ConnectivityReceiver
import hu.bme.aut.U5FT4N.easyshopping.ui.screen.ShoppingListScreen
import hu.bme.aut.U5FT4N.easyshopping.ui.screen.SplashScreen
import hu.bme.aut.U5FT4N.easyshopping.ui.screen.SummaryScreen
import hu.bme.aut.U5FT4N.easyshopping.ui.screen.money.MoneyApiScreen
import hu.bme.aut.U5FT4N.easyshopping.ui.screen.money.MoneyViewModel
import hu.bme.aut.U5FT4N.easyshopping.ui.theme.EasyShoppingTheme

//import hu.ait.todocompose.ui.screen.ShoppingListScreen
//import hu.ait.todocompose.navigation.MainNavigation
//import hu.ait.todocompose.receiver.BatteryChargingReceiver
//import hu.ait.todocompose.receiver.ConnectivityReceiver
//import hu.ait.todocompose.ui.screen.SplashScreen
//import hu.ait.todocompose.ui.screen.SummaryScreen
//import hu.ait.todocompose.ui.screen.money.MoneyApiScreen
//import hu.ait.todocompose.ui.screen.money.MoneyViewModel
//import hu.ait.todocompose.ui.theme.TodoComposeTheme
//import kotlin.text.Typography.dagger


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var batteryChargingReceiver: BatteryChargingReceiver
    private lateinit var connectivityReceiver: ConnectivityReceiver
    private val isInternetAvailable = mutableStateOf(true) // State for internet status

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val moneyViewModel: MoneyViewModel = hiltViewModel() // Initialize MoneyViewModel
            EasyShoppingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TodoAppNavHost(
                        modifier = Modifier.padding(innerPadding),
                        isInternetAvailable = isInternetAvailable.value
                    )
                }
            }

            // Register ConnectivityReceiver
            connectivityReceiver = ConnectivityReceiver { isConnected ->
                isInternetAvailable.value = isConnected
                if (isConnected) {
                    moneyViewModel.getRates() // Trigger API call when internet is available
                }
            }
            val connectivityFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            registerReceiver(connectivityReceiver, connectivityFilter)

            // Register BatteryChargingReceiver
            batteryChargingReceiver = BatteryChargingReceiver()
            val batteryFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            registerReceiver(batteryChargingReceiver, batteryFilter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister both receivers
        unregisterReceiver(connectivityReceiver)
        unregisterReceiver(batteryChargingReceiver)
    }
}

@Composable
fun TodoAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainNavigation.SplashScreen.route,
    isInternetAvailable: Boolean // Pass the connectivity status here
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(MainNavigation.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable("shoppinglistscreen") {
            ShoppingListScreen(
                isInternetAvailable = isInternetAvailable, // Pass status to ShoppingListScreen
                onNavigateToSummary = { totalItems, foodItemsCount, electronicsItemsCount ->
                    navController.navigate(
                        MainNavigation.SummaryScreen.createRoute(totalItems, foodItemsCount, electronicsItemsCount)
                    )
                },
                onNavigateToMoneyApi = { itemName ->
                    navController.navigate("moneyapiscreen/$itemName")
                }
            )
        }
        composable(
            route = "moneyapiscreen/{itemName}",
            arguments = listOf(navArgument("itemName") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemName = backStackEntry.arguments?.getString("itemName") ?: "Unknown Item"
            MoneyApiScreen(
                itemName = itemName,
                isInternetAvailable = isInternetAvailable // Pass status here
            )
        }

        composable(MainNavigation.SummaryScreen.route) { backStackEntry ->
            val totalItems = backStackEntry.arguments?.getString("totalItems")?.toIntOrNull() ?: 0
            val foodItemsCount = backStackEntry.arguments?.getString("foodItemsCount")?.toIntOrNull() ?: 0
            val electronicsItemsCount = backStackEntry.arguments?.getString("electronicsItemsCount")?.toIntOrNull() ?: 0
            SummaryScreen(
                totalItems = totalItems,
                foodItemsCount = foodItemsCount,
                electronicsItemsCount = electronicsItemsCount
            )
        }
    }
}
