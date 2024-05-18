package com.tareq.scanner


import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tareq.scanner.composable.ScanItem
import com.tareq.scanner.composable.ScannerSchema
import com.tareq.core.design.system.R
import com.tareq.design_system.components.ContentVisibilityAnimation
import com.tareq.model.local.ScanItem
import com.tareq.scanner.composable.ContactCard
import com.tareq.scanner.composable.EmailCard
import com.tareq.scanner.composable.ErrorPlaceholder
import com.tareq.scanner.composable.ProductCard
import com.tareq.scanner.composable.ScanLoadingPlaceholder
import com.tareq.scanner.composable.WifiCard
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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

                is ScannerEffect.ShowToastMessage -> {
                    Toast.makeText(
                        context,
                        uiState.errorMessageFile,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    ScannerContent(
        isContentVisible = isContentVisible,
        isLoading = uiState.isLoading,
        isError = uiState.isError,
        errorMessageFile = uiState.errorMessageFile,
        scanItems = uiState.scanItems,
        scanItemCategory = uiState.scanItemCategory,
        wifiFields = uiState.wifiFields,
        contactFields = uiState.contactFields,
        emailFields = uiState.emailFields,
        productFields = uiState.productFields,
        scanDate = uiState.scanDate,
        onClickBackArrow = viewModel::onClickBackArrow,
        onClickScanButton = viewModel::onClickScanButton
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ScannerContent(
    isContentVisible: Boolean,
    isLoading: Boolean,
    isError: Boolean,
    errorMessageFile: Int,
    scanItems: ImmutableList<ScanItem>,
    scanItemCategory: ScanItemCategory,
    wifiFields: WifiFields,
    contactFields: ContactFields,
    emailFields: EmailFields,
    productFields: ProductFields,
    scanDate: String,
    onClickBackArrow: () -> Unit,
    onClickScanButton: () -> Unit,
) {
    ContentVisibilityAnimation(state = isLoading) {
        ScanLoadingPlaceholder()
    }
    ContentVisibilityAnimation(state = isError) {
        ErrorPlaceholder(
            messageFile = errorMessageFile,
            onClickBackArrow = onClickBackArrow
        )
    }
    ContentVisibilityAnimation(state = isContentVisible) {
        Column(
            modifier = Modifier
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
                    scanItems.forEach { scanItem ->
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
                onClickScannerIcon = onClickScanButton
            )
        }
    }
    ContentVisibilityAnimation(state = scanItemCategory == ScanItemCategory.WIFI) {
        WifiCard(
            password = wifiFields.password,
            ssid = wifiFields.ssid,
            encryptionType = wifiFields.encryptionType,
            scanDate = scanDate,
            onClickArchive = {},
            onClickBackArrow = onClickBackArrow
        )
    }
    ContentVisibilityAnimation(state = scanItemCategory == ScanItemCategory.CONTACT_INFO) {
        ContactCard(
            name = contactFields.name,
            title = contactFields.title,
            emails = contactFields.emails,
            phoneNumbers = contactFields.phoneNumbers,
            addressees = contactFields.addresses,
            organization = contactFields.organization,
            urls = contactFields.urls,
            scanDate = scanDate,
            onClickBackArrow = onClickBackArrow,
            onClickArchive = {},
        )
    }
    ContentVisibilityAnimation(state = scanItemCategory == ScanItemCategory.EMAIL) {
        EmailCard(
            email = emailFields.email,
            body = emailFields.body,
            subject = emailFields.subject,
            scanDate = scanDate,
            onClickBackArrow = onClickBackArrow,
            onClickArchive = {},
        )
    }
    ContentVisibilityAnimation(state = scanItemCategory == ScanItemCategory.PRODUCT) {
        ProductCard(
            barcode = productFields.barcode,
            title = productFields.title,
            description = productFields.description,
            brand = productFields.brand,
            manufacturer = productFields.manufacturer,
            category = productFields.category,
            images = productFields.images,
            ingredients = productFields.ingredients,
            size = productFields.size,
            scanDate = scanDate,
            onClickBackArrow = onClickBackArrow,
            onClickArchive = {},
        )
    }
}