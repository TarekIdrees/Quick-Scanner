package com.tareq.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tareq.data.local.entity.EmailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmail(email: EmailEntity)

    @Query("DELETE FROM email_table WHERE email = :email")
    suspend fun deleteEmail(email: String)

    @Query("SELECT * FROM email_table")
    fun getAllEmails(): Flow<List<EmailEntity>>

}