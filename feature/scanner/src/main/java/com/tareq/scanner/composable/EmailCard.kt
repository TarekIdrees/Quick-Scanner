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
internal fun EmailCard(
    modifier: Modifier = Modifier,
    email: String,
    body: String,
    subject: String,
    scanDate: String,
    onClickBackArrow: () -> Unit,
    onClickArchive: () -> Unit
) {
    BaseCard(
        modifier = modifier,
        isItemArchived = false,
        onClickBackArrow = onClickBackArrow,
        onClickArchive = onClickArchive
    ) {
        Icon(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.ic_email),
            contentDescription = "email icon",
            tint = MaterialTheme.colorScheme.primary
        )
        SingleInformationHeadline(titleStringId = R.string.email, headline = email)
        SingleInformationHeadline(titleStringId = R.string.subject, headline = subject)
        SingleInformationHeadline(titleStringId = R.string.body, headline = body)
        SingleInformationHeadline(titleStringId = R.string.scan_date, headline = scanDate)
    }
}

@BarcodeScannerPreview
@Composable
private fun EmailCardPreview() {
    BarcodeScannerTheme {
        EmailCard(
            email = "tarek.idrees.ad@gmail.com",
            body = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. ",
            subject = "Android developer",
            scanDate = "1-5-2024",
            onClickBackArrow = { },
            onClickArchive = {}
        )
    }
}