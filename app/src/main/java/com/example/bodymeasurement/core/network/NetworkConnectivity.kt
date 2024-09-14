package com.example.bodymeasurement.core.network

import kotlinx.coroutines.flow.Flow


interface NetworkConnectivity {

    fun networkStatus(): Flow<NetworkStatus>

    fun isConnected(): NetworkStatus

}