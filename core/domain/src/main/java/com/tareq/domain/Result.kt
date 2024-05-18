package com.tareq.domain

typealias RootError = Error
sealed interface Result<out D, out E: RootError> {
    data class Success<out D>(val value: D): Result<D, Nothing>
    data class Error<out E: RootError>(val error: E): Result<Nothing, E>

}