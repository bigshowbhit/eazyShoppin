package hu.bme.aut.U5FT4N.easyshopping.navigation

sealed class MainNavigation(val route: String) {
    object SplashScreen : MainNavigation("splashscreen")

    object SummaryScreen : MainNavigation("summaryscreen?totalItems={totalItems}&foodItemsCount={foodItemsCount}&electronicsItemsCount={electronicsItemsCount}") {
        fun createRoute(totalItems: Int, foodItemsCount: Int, electronicsItemsCount: Int): String {
            return "summaryscreen?totalItems=$totalItems&foodItemsCount=$foodItemsCount&electronicsItemsCount=$electronicsItemsCount"
        }
    }
}