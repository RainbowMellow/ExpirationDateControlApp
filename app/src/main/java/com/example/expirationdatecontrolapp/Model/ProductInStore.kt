package com.example.expirationdatecontrolapp.Model

import java.time.LocalDate
import java.util.*

data class ProductInStore (
   val id: Int?,
   val product: Product,
   var amount: Int,
   val expirationDate: Date) {}