package com.example.expirationdatecontrolapp.Database

import com.example.expirationdatecontrolapp.Model.Product
import com.example.expirationdatecontrolapp.Model.ProductInStore
import com.google.gson.annotations.SerializedName
import java.util.*

data class ProductInStoreApiJson (
    @SerializedName("id")
    var id: String?,

    @SerializedName("product")
    var product: Product?,

    @SerializedName("amount")
    var amount: String?,

    @SerializedName("expirationDate")
    var expirationDate: Date?
    ) {}