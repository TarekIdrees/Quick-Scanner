package com.tareq.scanner.composable

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tareq.core.design.system.R
import com.tareq.design_system.components.BarcodeScannerPreview
import com.tareq.design_system.ui.BarcodeScannerTheme

@Composable
internal fun WifiCard(
    modifier: Modifier = Modifier,
    password: String,
    ssid: String,
    encryptionType: String,
    scanDate: String,
    isWifiArchived: Boolean,
    onClickBackArrow: () -> Unit,
    onClickArchive: () -> Unit
) {
    BaseCard(
        modifier = modifier,
        isItemArchived = isWifiArchived,
        onClickBackArrow = onClickBackArrow,
        onClickArchive = onClickArchive
    ) {
        Icon(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.ic_wifi),
            contentDescription = "wifi icon",
            tint = MaterialTheme.colorScheme.primary
        )
        SingleInformationHeadline(titleStringId = R.string.password, headline = password)
        SingleInformationHeadline(titleStringId = R.string.ssid, headline = ssid)
        SingleInformationHeadline(titleStringId = R.string.encryption_type, headline = encryptionType)
        SingleInformationHeadline(titleStringId = R.string.scan_date, headline = scanDate)
    }
}

@BarcodeScannerPreview
@Composable
private fun WifiCardPreview() {
    BarcodeScannerTheme {
        WifiCard(
            password = "123456",
            ssid = "test",
            encryptionType = "WPA",
            scanDate = "15-1-2024",
            isWifiArchived = false,
            onClickBackArrow = {},
            onClickArchive = {})
    }
}