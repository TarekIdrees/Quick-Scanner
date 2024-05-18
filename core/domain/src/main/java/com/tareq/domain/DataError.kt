package com.tareq.domain

sealed interface DataError: Error {
    enum class Network: DataError {
        NO_INTERNET,
        FORBIDDEN,
        UNAUTHORIZED,
        SERVER_ERROR,
        SERIALIZATION_ERROR,
        UNKNOWN,
        EXCEED_LIMIT,
        NOT_FOUND,
    }
}