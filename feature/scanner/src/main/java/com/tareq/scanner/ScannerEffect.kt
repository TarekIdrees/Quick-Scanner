package com.tareq.scanner

sealed class ScannerEffect {
    data class ShowMessage(val message: String) : ScannerEffect()
    data class OpenUrl(val url: String) : ScannerEffect()
}