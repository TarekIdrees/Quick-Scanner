package com.tareq.model.local

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ScanItem(
    @DrawableRes val icon: Int,
    @StringRes val name: Int
)
