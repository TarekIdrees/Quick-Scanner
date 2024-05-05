package com.tareq.scanner


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tareq.scanner.composable.ScanItem
import com.tareq.scanner.composable.ScannerSchema
import com.tareq.core.design.system.R
import com.tareq.design_system.components.ContentVisibilityAnimation
import com.tareq.scanner.composable.ScanLoadingPlaceholder
import com.tareq.scanner.composable.WifiCard

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ScannerScreen(
    modifier: Modifier = Modifier,
    viewModel: ScannerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    ContentVisibilityAnimation(state = uiState.isLoading) {
        ScanLoadingPlaceholder()
    }
    ContentVisibilityAnimation(state = uiState.isContentVisible()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.supported_qr_barcode_items),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                FlowRow(
                    modifier = Modifier.wrapContentHeight(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    uiState.scanItems.forEach { scanItem ->
                        ScanItem(
                            modifier = Modifier.wrapContentWidth(align = Alignment.Start),
                            itemIcon = scanItem.icon,
                            itemName = scanItem.name
                        )
                    }
                }
            }
            ScannerSchema(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .weight(1f),
                onClickScannerIcon = viewModel::onClickScanButton
            )
        }
    }
    ContentVisibilityAnimation(state = uiState.scanItemCategory == ScanItemCategory.WIFI) {
        WifiCard(
            password = uiState.wifiFields.password,
            ssid = uiState.wifiFields.ssid,
            encryptionType = uiState.wifiFields.encryptionType,
            scanDate = uiState.scanDate,
            onClickArchive = {},
            onClickBackArrow = viewModel::onClickBackArrow
        )
    }
}