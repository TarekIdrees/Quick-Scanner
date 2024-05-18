package com.tareq.scanner

import androidx.annotation.StringRes

sealed class ScannerEffect {
    data class ShowToastMessage(@StringRes val message: Int) : ScannerEffect()
    data class OpenUrl(val url: String) : ScannerEffect()
}