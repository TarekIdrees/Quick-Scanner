package com.tareq.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tareq.data.local.dao.ProductDao
import com.tareq.data.local.dao.WifiDao
import com.tareq.data.local.entity.ProductEntity
import com.tareq.data.local.entity.WifiEntity

@Database(entities = [ProductEntity::class, WifiEntity::class], version = 1)
@TypeConverters(Convertors::class)
abstract class QrBrCodeDatabase : RoomDatabase() {
    abstract fun getProductDao(): ProductDao
    abstract fun getWifiDao(): WifiDao
}