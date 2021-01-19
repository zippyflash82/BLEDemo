package com.example.bledemo

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.scan.ScanSettings
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rxBleClient = RxBleClient.create(this)

        btn_enable.setOnClickListener {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            val REQUEST_ENABLE_BT = 1
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

        btn_scan.setOnClickListener {
            val scanSubscription = rxBleClient.scanBleDevices(
                    ScanSettings.Builder()
                             .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // change if needed
                             .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES) // change if needed
                             .build()
                    // add filters if needed
            ).doOnError {
                Log.d("Scan Error"," Scanning Error")
            }
                    .subscribe(
                            { scanResult->
                                print(scanResult.toString())
                            },
                            { throwable->
                                println("Ble Scan Failed")
                            }
                    )
               scanSubscription.dispose()
        }


        btn_connect.setOnClickListener {

            val macAddress = "C6:37:89:5E:85:43"
            val rxBleDevice = rxBleClient.getBleDevice(macAddress)
            val disposable = rxBleDevice.establishConnection(true) // <-- autoConnect flag
                    .subscribe({ rxBleConnection->
                    })
            disposable.dispose()

        }


    }
}