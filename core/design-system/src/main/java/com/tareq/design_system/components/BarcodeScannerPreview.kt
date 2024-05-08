package com.tareq.design_system.components

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview


@Preview(
    name = "Preview Day",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    backgroundColor = 0
)
@Preview(
    name = "Preview Night",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0,
    locale = "ar"
)
annotation class BarcodeScannerPreview