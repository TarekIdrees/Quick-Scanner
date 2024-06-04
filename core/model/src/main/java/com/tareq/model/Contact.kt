package com.tareq.model

data class Contact(
    val name: String,
    val title: String,
    val phoneNumbers: List<String>,
    val emails: List<String>,
    val addresses: List<String>,
    val urls: List<String>,
    val organization: String,
    val scanDate: String,
)
