package com.tareq.domain

interface DatabaseOperation {
    data object Complete : DatabaseOperation
    data class InComplete(val error: DataError.Local) : DatabaseOperation
}