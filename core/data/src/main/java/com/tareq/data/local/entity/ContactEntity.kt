package com.tareq.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_table")
data class ContactEntity(
    val name: String,
    val title: String,
    val phoneNumbers: List<String>,
    val emails: List<String>,
    val addresses: List<String>,
    val urls: List<String>,
    val organization: String,
    val scanDate: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
