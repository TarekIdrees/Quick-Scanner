package com.tareq.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tareq.data.local.dao.ProductDao
import com.tareq.data.local.entity.ProductEntity

@Database(entities = [ProductEntity::class], version = 1)
@TypeConverters(Convertors::class)
abstract class QrBrCodeDatabase : RoomDatabase() {
    abstract fun getProductDao(): ProductDao
}