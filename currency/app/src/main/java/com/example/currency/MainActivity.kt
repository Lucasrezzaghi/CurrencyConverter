package com.example.currency

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //API
        var baseCurrency = "EUR"
        var convertedCurrency = "BRL"
        var conversionRate = 0f

        fun getApiResults() {
            val API =
                "https://api.ratesapi.io/api/latest?base=$baseCurrency&symbols=$convertedCurrency"
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val apiResult = URL(API).readText()
                    val jsonObject = JSONObject(apiResult)

                    conversionRate =
                        jsonObject.getJSONObject("rates").getString(convertedCurrency).toFloat()

                    Log.d("Main", "conversionRate")
                    Log.d("Main", "apiResult")

                    withContext(Dispatchers.Main) {
                        val text =
                            ((inputMoney.text.toString().toFloat()) * conversionRate).toString()
                    }

                } catch (e: Exception) {
                    Log.e("Main", "$e")
                }
            }

        }
        //convert button function
        val exgBtn = findViewById<Button>(R.id.exchangeBtn)
            exgBtn.setOnClickListener {
            textViewResult.text =
                "Result: " + ((inputMoney.text.toString().toFloat()) * conversionRate).toString() + " BRL"
                }


        //dropdown spinner
        val spinner: Spinner = findViewById(R.id.spnTest)

        ArrayAdapter.createFromResource(
            this,
            R.array.currencies,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spnTest.adapter = adapter
        }


        spnTest.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                baseCurrency = parent?.getItemAtPosition(position).toString()
                getApiResults()

            }
        })




    }
}
