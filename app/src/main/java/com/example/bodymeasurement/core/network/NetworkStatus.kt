package com.example.bodymeasurement.core.network

enum class NetworkStatus(val message: String) {

    Available("Back Online"),
    Unavailable("Network Connection Failed"),
    Lost("Connection Lost"),
    Losing("Network connection losing")

}