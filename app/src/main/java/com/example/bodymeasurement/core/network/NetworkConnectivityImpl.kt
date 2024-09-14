package com.example.bodymeasurement.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class NetworkConnectivityImpl @Inject constructor(
    private val context: Context
): NetworkConnectivity {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                               as ConnectivityManager

    override fun networkStatus(): Flow<NetworkStatus> {
        return callbackFlow {
            val callBack = object : NetworkCallback(){
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch { send(NetworkStatus.Available) }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch { send(NetworkStatus.Losing) }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch { send(NetworkStatus.Lost) }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch { send(NetworkStatus.Unavailable) }
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callBack)

            awaitClose {
                connectivityManager.unregisterNetworkCallback(callBack)
            }

        }.distinctUntilChanged()

    }

    override fun isConnected(): NetworkStatus {
        return if (
            connectivityManager.activeNetwork != null &&
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) != null
        )
           NetworkStatus.Available

        else
            NetworkStatus.Unavailable
    }

}