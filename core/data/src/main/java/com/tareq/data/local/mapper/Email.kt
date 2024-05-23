package com.tareq.data.local.mapper

import com.tareq.data.local.entity.EmailEntity
import com.tareq.model.Email

internal fun Email.toEmailEntity(scanDate: String) = EmailEntity(
    email = email,
    subject = subject,
    body = body,
    scanDate = scanDate
)