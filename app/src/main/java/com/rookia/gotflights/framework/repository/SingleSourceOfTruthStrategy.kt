package com.rookia.gotflights.framework.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.rookia.gotflights.domain.vo.Result
import kotlinx.coroutines.Dispatchers


fun <T, A> resultLiveData(
    persistedDataQuery: () -> LiveData<T>,
    networkCall: suspend () -> Result<A>,
    persistCallResult: suspend (A?) -> Unit,
    runNetworkCall: Boolean
): LiveData<Result<T>> =
    liveData(Dispatchers.IO) {
        val disposable = emitSource(
            persistedDataQuery.invoke().map {
                if (runNetworkCall) {
                    //show data from db but keep the loading state, as a network call will be done
                    Result.loading(it)
                } else {
                    //no network call -> show success
                    Result.success(it)
                }
            }
        )
        if (runNetworkCall) {
            val responseStatus = networkCall.invoke()
            // Stop the previous emission to avoid dispatching the updated user
            // as `loading`.
            disposable.dispose()
            emitSource(
                if (responseStatus.status == Result.Status.ERROR) {
                    persistedDataQuery.invoke().map {
                        Result.error(responseStatus.message, it)
                    }
                } else {
                    persistCallResult.invoke(responseStatus.data)
                    persistedDataQuery.invoke().map {
                        Result.success(it)
                    }
                }
            )
        }

    }

// TODO: 16/01/2020 test para ver si se llama a persistData