package com.uma.logicsofttask.ui

import android.Manifest.permission.READ_PHONE_STATE
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.uma.logicsofttask.R
import com.uma.logicsofttask.databinding.ActivityDeviceInfoBinding
import com.uma.logicsofttask.ui.worker.DeviceInfoWorkerManager
import com.uma.umruntimepermissions.PermissionListener
import com.uma.umruntimepermissions.UmPermissionManager
import kotlinx.android.synthetic.main.activity_device_info.*

class DeviceInfoActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var deviceInfoBinding: ActivityDeviceInfoBinding
    val REQUEST_READ_PHONE_STATE = 100
    lateinit var permissionManager: UmPermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        deviceInfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_device_info)
        deviceInfoBinding.deviceInfoBinding = DeviceInfoViewModel()

        autSyncSwitch.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.sync -> {
//                initPermissions()
//                val permissionCheck =
//                    ContextCompat.checkSelfPermission(this, READ_PHONE_STATE)
//
//                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(
//                        this,
//                        arrayOf(READ_PHONE_STATE),
//                        REQUEST_READ_PHONE_STATE
//                    )
//                } else {
//                    Log.e("imei", getDeviceIMEI())
//                }
                deviceInfo()
                Toast.makeText(this, "Sync", Toast.LENGTH_SHORT).show()
                val request =
                    OneTimeWorkRequest.Builder(DeviceInfoWorkerManager::class.java).build()
                WorkManager.getInstance().enqueue(request)
                Log.e("imei", getDeviceIMEI())
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            autSyncSwitch -> {
                Toast.makeText(this, "${autSyncSwitch.isChecked}", Toast.LENGTH_SHORT).show()
            }
            else -> {
            }
        }
    }

    private fun initPermissions() {
        permissionManager = UmPermissionManager.builder()
            .with(this)
            .requestCode(REQUEST_READ_PHONE_STATE)
            .neededPermissions(arrayOf(READ_PHONE_STATE))
            .setPermissionListner(object : PermissionListener {
                override fun onPermissionsGranted(requestCode: Int, acceptedPermission: String) {
                    val request =
                        OneTimeWorkRequest.Builder(DeviceInfoWorkerManager::class.java).build()
                    WorkManager.getInstance().enqueue(request)
                    Log.e("imei", getDeviceIMEI())
                }

                override fun showGrantDialog(grantPermissionTo: String): Boolean {

                    return super.showGrantDialog(grantPermissionTo)
                }

                override fun showRationalDialog(
                    requestCode: Int,
                    deniedPermission: String
                ): Boolean {

                    return super.showRationalDialog(requestCode, deniedPermission)
                }
            })
            .build()
    }

    private fun deviceInfo() {
        val details = ("\nVERSION.RELEASE : " + Build.VERSION.RELEASE
                + "\nVERSION.INCREMENTAL : " + Build.VERSION.INCREMENTAL
                + "\nVERSION.SDK.NUMBER : " + Build.VERSION.SDK_INT
                + "\nBOARD : " + Build.BOARD
                + "\nBOOTLOADER : " + Build.BOOTLOADER
                + "\nBRAND : " + Build.BRAND
                + "\nCPU_ABI : " + Build.CPU_ABI
                + "\nCPU_ABI2 : " + Build.CPU_ABI2
                + "\nDISPLAY : " + Build.DISPLAY
                + "\nFINGERPRINT : " + Build.FINGERPRINT
                + "\nHARDWARE : " + Build.HARDWARE
                + "\nHOST : " + Build.HOST
                + "\nID : " + Build.ID
                + "\nMANUFACTURER : " + Build.MANUFACTURER
                + "\nMODEL : " + Build.MODEL
                + "\nPRODUCT : " + Build.PRODUCT
                + "\nSERIAL : " + Build.SERIAL
                + "\nTAGS : " + Build.TAGS
                + "\nTIME : " + Build.TIME
                + "\nTYPE : " + Build.TYPE
                + "\nUNKNOWN : " + Build.UNKNOWN
                + "\nUSER : " + Build.USER)
        Log.e("Device Details", details)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Returns the unique identifier for the device
     *
     * @return unique identifier for the device
     */
    fun getDeviceIMEI(): String? {
        var deviceUniqueIdentifier: String?
        val tm = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        deviceUniqueIdentifier = tm.imei
        if (null == deviceUniqueIdentifier || deviceUniqueIdentifier.isEmpty())
            deviceUniqueIdentifier =
                Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        return deviceUniqueIdentifier
    }
}
