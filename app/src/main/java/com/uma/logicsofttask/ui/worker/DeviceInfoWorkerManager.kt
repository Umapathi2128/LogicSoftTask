package com.uma.logicsofttask.ui.worker

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.database.FirebaseDatabase
import android.hardware.usb.UsbDevice.getDeviceId
import android.provider.Settings
import androidx.core.content.ContextCompat.getSystemService
import android.telephony.TelephonyManager
import android.provider.Settings.Secure
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemService
import android.app.Activity
import android.content.pm.PackageManager

/**
 * Created by Umapathi on 2019-10-09.
 * Copyright Indyzen Inc, @2019
 */
class DeviceInfoWorkerManager(val ctx : Context,val workerParameters: WorkerParameters) : Worker(ctx,workerParameters) {
    override fun doWork(): Result {

//        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
//        telephonyManager!!.deviceId

        val db = FirebaseDatabase.getInstance()
        val dRef = db.getReference("DeviceInfo")
        dRef.setValue("uma")

//        Log.e("Imei",getDeviceIMEI())
       return Result.success()
    }



}