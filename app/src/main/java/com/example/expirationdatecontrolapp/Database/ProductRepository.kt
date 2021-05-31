package com.example.expirationdatecontrolapp.Database

import android.content.Context
import com.example.expirationdatecontrolapp.Model.Product
import com.example.expirationdatecontrolapp.Model.ProductInStore
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.conscrypt.Conscrypt
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.security.Security

class ProductRepository (context: Context) {

    var chosenProduct : Product? = null

    fun getProduct(barcodeId: String) {

        val api = NetworkHandler.service

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.getProduct(barcodeId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val items = response.body()

                        if (items != null) {
                            val barcode = items.barcode
                            val productName = items.productName

                            sendProduct(barcode, productName)
                        }
                    }
                }
            }
            catch (e: Exception) {
                println("Something went wront" + e.toString())
            }

        }
    }

    private fun sendProduct(barcode: String?, name: String?)
    {
        val product = Product (
            barcode = barcode,
            productName = name,
        )

        chosenProduct = product
    }


    fun createProductInStore(product: ProductInStore) {

        val retrofit = NetworkHandler.service

        val main = JSONObject()
        val productForNest = JSONObject()

        productForNest.put("barcode", product.product.barcode)
        productForNest.put("productName", product.product.productName)

        main.put("product", productForNest)
        main.put("amount", product.amount)
        main.put("expirationDate", product.expirationDate)

        val jsonObjectString = main.toString()

        println(jsonObjectString)

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        println(requestBody.toString())

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = retrofit.createProductInStore(requestBody)

                withContext(Dispatchers.Main) {
                    if(response.isSuccessful) {

                        println(response.toString())
                    }
                }
            }
            catch (e: Exception) {
                println("Something went wront" + e.toString())
            }

        }
    }

    companion object {
        private var Instance: ProductRepository? = null

        fun initialize(context: Context) {
            if (Instance == null)
                Instance = ProductRepository(context)
        }

        fun get(): ProductRepository {
            if (Instance != null) return Instance!!
            throw IllegalStateException("Friend repo not initialized")
        }
    }
}