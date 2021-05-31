package com.example.expirationdatecontrolapp.Database

import com.example.expirationdatecontrolapp.Model.Product
import com.google.gson.annotations.SerializedName

data class ProductApiJson (
    @SerializedName("barcode")
    var barcode: String?,

    @SerializedName("productName")
    var productName: String?
    ) {}