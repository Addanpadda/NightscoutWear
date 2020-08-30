package com.example.nightscoutwear

import android.support.wearable.activity.WearableActivity

interface NightscoutView {
    fun displaySgv(value: Float)
    fun displaySgvDate(date: String)
    fun displayError(error: String)
}

abstract class NightscoutWearableView: WearableActivity(), NightscoutView