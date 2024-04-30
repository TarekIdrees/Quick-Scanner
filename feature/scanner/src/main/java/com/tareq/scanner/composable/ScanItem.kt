package com.tareq.scanner.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tareq.design_system.components.BarcodeScannerPreview
import com.tareq.design_system.ui.BarcodeScannerTheme
import com.tareq.core.design.system.R

@Composable
internal fun ScanItem(
    modifier: Modifier = Modifier,
    @DrawableRes itemIcon: Int,
    @StringRes itemName: Int
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = itemIcon),
            contentDescription = "scan item",
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(id = itemName),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
@BarcodeScannerPreview
private fun ScanItemPreview() {
    BarcodeScannerTheme {
        ScanItem(itemIcon = R.drawable.ic_wifi, itemName = R.string.wifi)
    }
}