package com.tareq.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "email_table")
data class EmailEntity(
    val email: String,
    val body: String,
    val subject: String,
    val scanDate: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
