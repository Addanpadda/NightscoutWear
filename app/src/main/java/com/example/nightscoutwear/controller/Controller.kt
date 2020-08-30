package com.example.nightscoutwear.controller

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.widget.TextView
import com.example.nightscoutwear.NightscoutView
import java.util.*

interface NightscoutController {
    var nightscoutApi: NightscoutApi
}

abstract class AndroidController(base: Context) : ContextWrapper(base), NightscoutController


class Controller(private val view: NightscoutView) : AndroidController(view as Context) {
    override var nightscoutApi: NightscoutApi = NightscoutApi(this, "https://ahcgm.azurewebsites.net")

    init {
        nightscoutApi.fetch({
            if(it.type == "sgv") {
                val splitDateString = it.dateString.split(" ")
                val date = splitDateString[1] + " " + splitDateString[2] + " " + splitDateString[3]
                val value = ((it.sgv.mmoll*10+0.5).toInt()/10f)

                view.displaySgvDate(date)
                view.displaySgv(value)
            }
        }, {
            view.displayError(it)
        })
    }
}