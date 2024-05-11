package com.tareq.scanner

sealed class ScannerEffect {
    data object ShowUnSupportedQBcodeMessage: ScannerEffect()
    data class OpenUrl(val url: String) : ScannerEffect()
}