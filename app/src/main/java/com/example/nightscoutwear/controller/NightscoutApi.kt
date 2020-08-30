package com.example.nightscoutwear.controller

import android.content.Context
import android.content.ContextWrapper
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.example.nightscoutwear.model.NightscoutEntry
import com.example.nightscoutwear.model.SGV
import org.json.JSONArray
import org.json.JSONObject


interface ApiFetcher {
    var uri: String
}

interface JsonApiFetcher : ApiFetcher {
    fun fetch(fetchCallback: (data: JSONArray) -> Unit = {}, fetchErrorCallback: (error: String) -> Unit = {})
}

open class NightscoutApiFetcher(base: Context, var nightscoutFollowURL: String) : JsonApiFetcher, ContextWrapper(base) {
    override var uri = "$nightscoutFollowURL/api/v1/entries.json"

    override fun fetch(fetchCallback: (data: JSONArray) -> Unit, fetchErrorCallback: (error: String) -> Unit) {
        var queue = Volley.newRequestQueue(this)

        val jsonRequest = JsonArrayRequest(Request.Method.GET, uri, null, Response.Listener(fetchCallback), Response.ErrorListener { fetchErrorCallback(it.toString()) })

        queue.add(jsonRequest)
    }
}

interface NightscoutApiStructure {
    var nightscoutApiFetcher: NightscoutApiFetcher
    var lastNightscoutEntry: NightscoutEntry?

    fun fetch(fetchCallback: (nightscoutEntry: NightscoutEntry) -> Unit, fetchErrorCallback: (error: String) -> Unit = {})
}

class NightscoutApi(base: Context, var nightscoutFollowURL: String) : NightscoutApiStructure, ContextWrapper(base) {
    override var nightscoutApiFetcher = NightscoutApiFetcher(this, nightscoutFollowURL)
    override var lastNightscoutEntry: NightscoutEntry? = null

    override fun fetch(fetchCallback: (nightscoutEntry: NightscoutEntry) -> Unit, fetchErrorCallback: (error: String) -> Unit) {
        nightscoutApiFetcher.fetch ({
            val id = JSONObject(it[0].toString()).getString("_id")
            val sgv = SGV(JSONObject(it[0].toString()).getInt("sgv"))
            val direction = JSONObject(it[0].toString()).getString("direction")
            val device = JSONObject(it[0].toString()).getString("device")
            val type = JSONObject(it[0].toString()).getString("type")
            val date = JSONObject(it[0].toString()).getString("date")
            val dateString = JSONObject(it[0].toString()).getString("dateString")

            lastNightscoutEntry = NightscoutEntry(id, sgv, direction, device, type, date, dateString)

            fetchCallback(lastNightscoutEntry!!)
        }, {
            fetchErrorCallback(it)
        })
    }
}
