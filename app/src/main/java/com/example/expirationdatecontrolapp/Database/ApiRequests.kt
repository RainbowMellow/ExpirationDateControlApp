package com.example.expirationdatecontrolapp.Database

import com.example.expirationdatecontrolapp.Model.ProductInStore
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiRequests {

    @GET("product/{barcode}")
    suspend fun getProduct(@Path(value = "barcode") barcode: String): Response<ProductApiJson>

    @POST("productInStore")
    suspend fun createProductInStore(@Body requestBody: RequestBody): Response<ResponseBody>

}