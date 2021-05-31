package com.example.expirationdatecontrolapp.GUI

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.expirationdatecontrolapp.Database.ProductRepository
import com.example.expirationdatecontrolapp.R
import com.google.android.gms.common.GooglePlayServicesNotAvailableException

import com.google.android.gms.common.GooglePlayServicesRepairableException

import com.google.android.gms.security.ProviderInstaller
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext


class MainActivity : AppCompatActivity() {

    lateinit var repo: ProductRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // Google Play will install latest OpenSSL
            ProviderInstaller.installIfNeeded(applicationContext)
            val sslContext: SSLContext
            sslContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, null, null)
            sslContext.createSSLEngine()
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

        setContentView(R.layout.activity_main)


        ProductRepository.initialize(this)
        repo = ProductRepository.get()
    }

    fun onClickScan(view: View) {
        val intent = Intent(this, ScannerActivity::class.java)
        startActivity(intent)
    }

    fun onClickManual(view: View) {
        val intent = Intent(this, InputActivity::class.java)
        startActivity(intent)
    }

}