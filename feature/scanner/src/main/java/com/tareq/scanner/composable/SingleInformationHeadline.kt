package com.tareq.scanner.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tareq.core.design.system.R
import com.tareq.design_system.components.BarcodeScannerPreview
import com.tareq.design_system.ui.BarcodeScannerTheme

@Composable
internal fun SingleInformationHeadline(
    modifier: Modifier = Modifier,
    @StringRes titleStringId: Int,
    headline: String
) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Text(
            text = stringResource(titleStringId),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onTertiary
        )
        if (headline.isEmpty()) {
            Text(
                text = stringResource(R.string.not_found_label),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        } else {
            SelectionContainer {
                Text(
                    text = headline,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@BarcodeScannerPreview
@Composable
private fun SingleInformationHeadlinePreview() {
    BarcodeScannerTheme {
        SingleInformationHeadline(titleStringId = R.string.name, headline = "Tarek")
    }
}