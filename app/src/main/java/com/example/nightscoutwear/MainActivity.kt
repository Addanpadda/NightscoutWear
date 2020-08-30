package com.example.nightscoutwear

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.util.Log
import android.widget.TextView
import com.example.nightscoutwear.controller.Controller
import com.example.nightscoutwear.controller.NightscoutApi
import com.example.nightscoutwear.model.SGV


class MainActivity : NightscoutWearableView() {
    lateinit var controller: Controller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        setAmbientEnabled()
        controller = Controller(this)
        //updateBsViewerText()
    }

    override fun displaySgv(value: Float) {
        val bsViewerTextView = findViewById<TextView>(R.id.bs_viewer_text)
        bsViewerTextView.textSize = 50f
        bsViewerTextView.text = value.toString()
    }

    private fun updateBsViewerText() {
        val nightscoutApi = NightscoutApi(this, "https://ahcgm.azurewebsites.net")

        nightscoutApi.fetch({
            if(it.type == "sgv") {
                val splitDateString = it.dateString.split(" ")
                val lastTimeUpdateTextView = findViewById<TextView>(R.id.last_update_time)
                lastTimeUpdateTextView.text = splitDateString[1] + " " + splitDateString[2] + " " + splitDateString[3]
                var bsViewerText = findViewById<TextView>(R.id.bs_viewer_text)
                var text = ((it.sgv.mmoll*10+0.5).toInt()/10f).toString()
                bsViewerText.text = text
                bsViewerText.textSize = 50f
                Log.d("Response", text)
            }
        }, {
            var bsViewerText = findViewById<TextView>(R.id.bs_viewer_text)
            bsViewerText.text = it
            Log.d("Response", it)
        })
    }
}
