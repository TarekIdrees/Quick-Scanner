package com.tareq.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tareq.data.local.entity.WifiEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WifiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWifi(wifi: WifiEntity)

    @Query("DELETE FROM wifi_table WHERE ssid = :ssid")
    suspend fun deleteWifi(ssid: String)

    @Query("SELECT * FROM wifi_table")
    fun getAllWifi(): Flow<List<WifiEntity>>

}