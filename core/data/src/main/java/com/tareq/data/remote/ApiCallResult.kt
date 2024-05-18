package com.tareq.data.remote

import com.tareq.domain.DataError

sealed interface ApiCallResult<T> {
    data class Success<T>(val data: T) : ApiCallResult<T>
    data class Failure<T>(val exception: DataError.Network) : ApiCallResult<T>

    fun onSuccess(action: (T) -> Unit): ApiCallResult<T> {
        if (this is Success) action(data)
        return this
    }

    fun onFailure(action: (DataError.Network) -> Unit): ApiCallResult<T> {
        if (this is Failure) action(exception)
        return this
    }
}