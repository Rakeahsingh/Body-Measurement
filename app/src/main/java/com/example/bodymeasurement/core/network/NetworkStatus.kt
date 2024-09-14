package com.example.bodymeasurement.core.network

enum class NetworkStatus(val message: String) {

    Available("Back Online"),
    Unavailable("Network Connection Error"),
    Lost("Connection Lost"),
    Losing("Network connectivity loss")

}