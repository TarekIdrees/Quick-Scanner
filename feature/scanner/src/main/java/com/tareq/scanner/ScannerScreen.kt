package com.tareq.scanner


import android.content.Intent
import android.net.Uri
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.tareq.scanner.composable.ScanItem
import com.tareq.scanner.composable.ScannerSchema
import com.tareq.core.design.system.R
import com.tareq.design_system.components.ContentVisibilityAnimation
import com.tareq.scanner.composable.ContactCard
import com.tareq.scanner.composable.ScanLoadingPlaceholder
import com.tareq.scanner.composable.WifiCard

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ScannerScreen(
    modifier: Modifier = Modifier,
    viewModel: ScannerViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val isContentVisible by remember {
        derivedStateOf { uiState.isContentVisible() }
    }

    LaunchedEffect(key1 = viewModel.effect) {
        viewModel.effect.collect { scannerEffect ->
            when (scannerEffect) {
                is ScannerEffect.OpenUrl -> {
                    with(scannerEffect) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(context, intent, null)
                    }
                }

                is ScannerEffect.ShowMessage -> {}
            }
        }
    }

    ContentVisibilityAnimation(state = uiState.isLoading) {
        ScanLoadingPlaceholder()
    }
    ContentVisibilityAnimation(state = isContentVisible) {
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
    ContentVisibilityAnimation(state = uiState.scanItemCategory == ScanItemCategory.CONTACT_INFO) {
        ContactCard(
            name = uiState.contactFields.name,
            title = uiState.contactFields.title,
            emails = uiState.contactFields.emails,
            phoneNumbers = uiState.contactFields.phoneNumbers,
            addressees = uiState.contactFields.addresses,
            organization = uiState.contactFields.organization,
            urls = uiState.contactFields.urls,
            scanDate = uiState.scanDate,
            onClickBackArrow = viewModel::onClickBackArrow,
            onClickArchive = {},
        )
    }
}