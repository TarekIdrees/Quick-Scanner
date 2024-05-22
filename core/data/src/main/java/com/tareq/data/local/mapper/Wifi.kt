package com.tareq.data.local.mapper

import com.tareq.data.local.entity.WifiEntity
import com.tareq.model.Wifi

internal fun Wifi.toWifiEntity(scanDate: String) = WifiEntity(
    ssid = ssid,
    password = password,
    encryptionType = encryptionType,
    scanDate = scanDate
)