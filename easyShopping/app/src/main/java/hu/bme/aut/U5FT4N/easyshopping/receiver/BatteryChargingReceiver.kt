package hu.bme.aut.U5FT4N.easyshopping.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.widget.Toast
import com.example.easyshopping.R

class BatteryChargingReceiver : BroadcastReceiver() {
    private var previousIsCharging: Boolean? = null

    override fun onReceive(context: Context, intent: Intent) {
        val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

        if (previousIsCharging == null || previousIsCharging != isCharging) {
            previousIsCharging = isCharging
            if (isCharging) {
                Toast.makeText(context,
                    context.getString(R.string.battery_is_charging), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context,
                    context.getString(R.string.battery_is_not_charging), Toast.LENGTH_SHORT).show()
            }
        }
    }
}