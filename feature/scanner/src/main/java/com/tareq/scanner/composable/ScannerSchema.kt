package com.tareq.scanner.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tareq.core.design.system.R
import com.tareq.design_system.components.BarcodeScannerPreview
import com.tareq.design_system.ui.BarcodeScannerTheme
import com.tareq.design_system.ui.background

@Composable
internal fun ScannerSchema(
    modifier: Modifier = Modifier,
    onClickScannerIcon: () -> Unit,
) {
    Column(
        modifier = modifier
            .drawBehind {
                drawRect(color = background)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            modifier = Modifier.size(150.dp),
            onClick = onClickScannerIcon
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_scanner_placeholder),
                contentDescription = "scanner placeholder",
                tint = Color.Unspecified
            )
        }
        Text(
            text = stringResource(R.string.qr_barcode_scanner),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = stringResource(R.string.scanner_click_instruction),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onTertiary
        )
    }
}

@Composable
@BarcodeScannerPreview
private fun ScannerSchemaPreview() {
    BarcodeScannerTheme {
        ScannerSchema {

        }
    }
}