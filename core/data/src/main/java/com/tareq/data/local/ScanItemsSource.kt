package com.tareq.data.local

import com.tareq.core.design.system.R
import com.tareq.model.local.ScanItem

object ScanItemsSource {
    val scanItems = listOf(
        ScanItem(
            icon = R.drawable.ic_wifi,
            name = R.string.wifi
        ),
        ScanItem(
            icon = R.drawable.ic_location,
            name = R.string.location
        ),
        ScanItem(
            icon = R.drawable.ic_contact,
            name = R.string.contact
        ),
        ScanItem(
            icon = R.drawable.ic_link,
            name = R.string.link
        ),
        ScanItem(
            icon = R.drawable.ic_email,
            name = R.string.email
        ),
        ScanItem(
            icon = R.drawable.ic_product,
            name = R.string.product
        ),
    )
}