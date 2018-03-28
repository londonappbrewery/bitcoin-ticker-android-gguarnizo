package com.londonappbrewery.bitcointicker

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AdapterView.OnItemSelectedListener
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*



import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    // Constants:
    private val BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC"
    private val LOG_D = "Bitcoin"

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Crear ArrayAdapter desde recursos
        val adapter = ArrayAdapter.createFromResource(this,R.array.currency_array,R.layout.spinner_item)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        // Apply the adapter to the spinner
        currency_spinner.adapter = adapter

        // Set an OnItemSelected listener on the spinner
        currency_spinner.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var currency = parent!!.getItemAtPosition(position).toString()
                //Log.d(LOG_D,"" + BASE_URL + currency)
                letsDoSomeNetworking(BASE_URL+currency)

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d(LOG_D,"Nothing selected !")
            }
        }

    }

    // API CALL
    private fun letsDoSomeNetworking(url: String) {

        var client :AsyncHttpClient = object : AsyncHttpClient(){}
        client.get(url, object: JsonHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                super.onSuccess(statusCode, headers, response)
                var price:String = response!!.getString("last")
                priceLabel.text = price
                //Log.d(LOG_D,"JSON object : "+ price)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                super.onFailure(statusCode, headers, throwable, errorResponse)
                Log.e(LOG_D, "ERROR: " + errorResponse.toString())
            }

        })

    }

}


