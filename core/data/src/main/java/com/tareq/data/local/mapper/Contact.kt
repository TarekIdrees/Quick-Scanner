package com.tareq.data.local.mapper

import com.tareq.data.local.entity.ContactEntity
import com.tareq.model.Contact

internal fun Contact.toContactEntity(scanDate: String)= ContactEntity(
    name= name,
    title = title,
    phoneNumbers = phoneNumbers,
    emails = emails,
    addresses = addresses,
    urls = urls,
    organization = organization,
    scanDate = scanDate
)