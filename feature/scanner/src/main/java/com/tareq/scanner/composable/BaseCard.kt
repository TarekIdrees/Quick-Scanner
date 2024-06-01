package com.tareq.scanner.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tareq.core.design.system.R
import com.tareq.design_system.components.BarcodeScannerPreview
import com.tareq.design_system.ui.BarcodeScannerTheme
import com.tareq.design_system.ui.card

@Composable
internal fun BaseCard(
    modifier: Modifier = Modifier,
    onClickBackArrow: () -> Unit,
    onClickArchive: () -> Unit,
    isItemArchived: Boolean,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .drawBehind { drawRect(color = card) }
            .padding(vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.background),
                onClick = onClickBackArrow,
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "back arrow",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor =
                    if (isItemArchived)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.background
                ),
                onClick = onClickArchive,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bookmark),
                    contentDescription = "archive icon",
                    tint = if (isItemArchived)
                        MaterialTheme.colorScheme.background
                    else
                        MaterialTheme.colorScheme.primary
                )
            }
        }
        content()
    }
}

@BarcodeScannerPreview
@Composable
private fun BaseCardPreview() {
    BarcodeScannerTheme {
        BaseCard(isItemArchived = false, onClickBackArrow = { }, onClickArchive = { }) {

        }
    }
}