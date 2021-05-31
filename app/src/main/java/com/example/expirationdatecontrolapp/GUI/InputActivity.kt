package com.example.expirationdatecontrolapp.GUI

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.expirationdatecontrolapp.Model.Product
import com.example.expirationdatecontrolapp.Model.ProductInStore
import com.example.expirationdatecontrolapp.Database.ProductRepository
import com.example.expirationdatecontrolapp.R
import kotlinx.android.synthetic.main.activity_input.*
import okhttp3.internal.wait
import java.text.SimpleDateFormat
import java.util.*

class InputActivity : AppCompatActivity() {

    var chosenExpDate: Date? = null
    lateinit var repo: ProductRepository
    val cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        repo = ProductRepository.get()

        if (intent.extras != null) {
            val extras: Bundle = intent.extras!!
            val barcode = extras.getString("barcode")

            etBarcode.setText(barcode)
            search()
        }
    }

    val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.GERMAN)
        tvExpDate.text = sdf.format(cal.time)

        chosenExpDate = cal.time
    }

    fun onClickCalendar(view: View) {
        DatePickerDialog(this, dateSetListener, cal
        .get(Calendar.YEAR), cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    fun onClickSearch(view: View) {
        search()
    }

    fun onClickCancel(view: View) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Cancel Creation Of Product")
        builder.setMessage("Are you sure that you want to cancel?")

        builder.setPositiveButton("Yes")
        { dialogInterface, which ->
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        builder.setNeutralButton("No")
        { dialogInterface, which -> println("Clicked Cancel")}

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    fun onClickSave(view: View) {

        val builder = AlertDialog.Builder(this)

        builder.setTitle("Save Product To Store")
        builder.setMessage("Do you want to save this product?")
        builder.setIcon(R.drawable.save)

        builder.setPositiveButton("Yes")
        { dialogInterface, which ->

            val barcode = etBarcode.text.toString()
            val name = etProductName.text.toString()

            val product = Product(barcode, name)

            val amount = Integer.parseInt(etAmount.text.toString())

            val productInStore = ProductInStore(0, product, amount, chosenExpDate!!)

            repo.createProductInStore(productInStore)

            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        builder.setNeutralButton("Cancel")
        { dialogInterface, which -> println("Clicked Cancel")}

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    fun search()
    {
        repo.getProduct(etBarcode.text.toString())

        val product = repo.chosenProduct
        println("Searched")

        if(product != null)
        {
            println("not null")
            etProductName.setText(product.productName)
        }
        else {
            println("null")
        }
    }

}