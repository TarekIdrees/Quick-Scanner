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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf


@Composable
internal fun ContactCard(
    modifier: Modifier = Modifier,
    name: String,
    title: String,
    emails: ImmutableList<String>,
    phoneNumbers: ImmutableList<String>,
    addressees: ImmutableList<String>,
    organization: String,
    urls: ImmutableList<String>,
    scanDate: String,
    isContactArchived: Boolean,
    onClickBackArrow: () -> Unit,
    onClickArchive: () -> Unit
) {
    BaseCard(
        modifier = modifier,
        onClickBackArrow = onClickBackArrow,
        isItemArchived = isContactArchived,
        onClickArchive = onClickArchive
    ) {
        Icon(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.ic_contact),
            contentDescription = "contact icon",
            tint = MaterialTheme.colorScheme.primary
        )
        SingleInformationHeadline(titleStringId = R.string.name, headline = name)
        SingleInformationHeadline(titleStringId = R.string.title, headline = title)
        SingleInformationHeadline(titleStringId = R.string.organization, headline = organization)
        MultiInformationHeadline(titleStringId = R.string.emails, headlines = emails)
        MultiInformationHeadline(titleStringId = R.string.phone_numbers, headlines = phoneNumbers)
        MultiInformationHeadline(titleStringId = R.string.addressees, headlines = addressees)
        MultiInformationHeadline(titleStringId = R.string.websites, headlines = urls)
        SingleInformationHeadline(titleStringId = R.string.scan_date, headline = scanDate)
    }
}

@BarcodeScannerPreview
@Composable
private fun ContactCardPreview() {
    BarcodeScannerTheme {
        ContactCard(
            name = "Tareq",
            title = "Android developer",
            emails = persistentListOf("tarek.idrees.ad@gmail.com", "tarek.idrees.ad@gmail.com"),
            phoneNumbers = persistentListOf("+31624212992", "01156675655"),
            addressees = persistentListOf("Nederland,Eindhoven", "Egypt,Cairo"),
            organization = "Google",
            urls = persistentListOf("www.google.com", "www.facebook.com"),
            scanDate = "1-5-2024",
            isContactArchived = false,
            onClickBackArrow = {},
            onClickArchive = {}
        )
    }
}