package com.example.expirationdatecontrolapp.GUI

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.SparseArray
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.expirationdatecontrolapp.R
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_scanner.*
import java.io.IOException


class ScannerActivity : AppCompatActivity() {

    lateinit var cameraSource: CameraSource
    lateinit var barcodeDetector: BarcodeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.EAN_13).build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(640, 480)
            .setAutoFocusEnabled(true)
            .build()

        camerapreview!!.holder!!.addCallback(object : SurfaceHolder.Callback {

            override fun surfaceCreated(holder: SurfaceHolder) {
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                try {
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }

        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {

            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barCodes: SparseArray<Barcode> = detections.detectedItems

                if (barCodes.size() != 0) {

                    val handler = Handler(Looper.getMainLooper())
                    handler.post {
                        val intent = Intent(applicationContext, InputActivity::class.java)
                        intent.putExtra("barcode", barCodes.valueAt(0).rawValue)

                        startActivity(intent)

                        cameraSource.release()
                    }

                    /* textView.post(object : Runnable {

                        override fun run() {
                            val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                            vibrator.vibrate(1000)
                            textView.setText(barCodes.valueAt(0).displayValue)
                        }
                    })*/
                }
            }
        })

    }

}