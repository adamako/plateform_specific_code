package com.kotech.batterylevel
import android.annotation.TargetApi
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.NonNull

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity(){
    private val channel= "batterylevel"

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger,channel).setMethodCallHandler{
            call, result ->
            if(call.method=="getBatteryLevel"){
                val batteryLevel= getBatteryLevel()
                if(batteryLevel!=-1){
                    result.success(batteryLevel)
                }else{
                    result.error("UNAVAILABLE","Battery level is not available",null)
                }
            }else if(call.method=="toast"){
                    val toast=tost(call.arguments.toString())
                    result.success(toast)
            }
            else{
                result.notImplemented()
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private fun getBatteryLevel(): Int {
        val batteryLevel:Int
        batteryLevel = if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            val batteryManager= getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        }else{
            val intent= ContextWrapper(applicationContext).registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)*100/intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1)
        }
        return batteryLevel
    }

    private fun tost(message:String){
         Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
    }
}
