package com.tareq.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wifi_table")
data class WifiEntity(
    val ssid: String,
    val password: String,
    val encryptionType: String,
    val scanDate: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
