package hu.bme.aut.U5FT4N.easyshopping.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import com.example.easyshopping.R

class ConnectivityReceiver(
    private val onConnectionChange: (Boolean) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val isConnected = isInternetAvailable(context)
        if (isConnected) {
            Toast.makeText(context,
                context.getString(R.string.internet_is_back_fetching_latest_data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context,
                context.getString(R.string.no_internet_some_features_may_be_limited), Toast.LENGTH_SHORT).show()
        }
        onConnectionChange(isConnected) // Notify the caller about connection status
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}