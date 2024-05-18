package com.tareq.scanner.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tareq.core.design.system.R
import com.tareq.design_system.components.BarcodeScannerPreview
import com.tareq.design_system.ui.BarcodeScannerTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    barcode: String,
    title: String,
    description: String,
    brand: String,
    manufacturer: String,
    category: String,
    images: ImmutableList<String>,
    ingredients: String,
    size: String,
    scanDate: String,
    onClickBackArrow: () -> Unit,
    onClickArchive: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { images.size })
    BaseCard(
        modifier = modifier,
        onClickBackArrow = onClickBackArrow,
        onClickArchive = onClickArchive
    ) {
        if (images.isNotEmpty()) {
            HorizontalPager(
                state = pagerState,
                pageSpacing = 16.dp,
                contentPadding = PaddingValues(horizontal = 32.dp),
            ) { pageIndex ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(images[pageIndex]).crossfade(true)
                        .build(),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .graphicsLayer {
                            val pageOffset =
                                ((pagerState.currentPage - pageIndex) + pagerState.currentPageOffsetFraction).absoluteValue
                            lerp(
                                start = 0.7f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                        },
                    error = painterResource(id = R.drawable.image_error_placeholder),
                    placeholder = painterResource(id = R.drawable.image_loading_placeholder),
                    contentDescription = "product image",
                    contentScale = ContentScale.Crop,
                )
            }
        }
        SingleInformationHeadline(titleStringId = R.string.barcode, headline = barcode)
        SingleInformationHeadline(titleStringId = R.string.title, headline = title)
        SingleInformationHeadline(titleStringId = R.string.description, headline = description)
        SingleInformationHeadline(titleStringId = R.string.brand, headline = brand)
        SingleInformationHeadline(titleStringId = R.string.manufacturer, headline = manufacturer)
        SingleInformationHeadline(titleStringId = R.string.category, headline = category)
        SingleInformationHeadline(titleStringId = R.string.ingredients, headline = ingredients)
        SingleInformationHeadline(titleStringId = R.string.size, headline = size)
        SingleInformationHeadline(titleStringId = R.string.scan_date, headline = scanDate)
    }
}

@BarcodeScannerPreview
@Composable
fun ProductCardPreview() {
    BarcodeScannerTheme {
        ProductCard(
            barcode = "9780140157376",
            title = "G7 Coffee",
            description = "G7 coffee. Allergens: En:milk. Labels: Halal. Country of origin: France,Vietnam.",
            brand = "Trung Nguyên",
            manufacturer = "Trung Nguyen",
            category = "Food, Beverages & Tobacco",
            images = persistentListOf(
                "https://images.barcodelookup.com/5457/54572416-1.jpg",
                "https://images.barcodelookup.com/5457/54572416-2.jpg",
                "https://images.barcodelookup.com/5457/54572416-3.jpg"
            ),
            ingredients = "Sucre, Crème , Cafe Solube, Sei, Maltodextrin.",
            size = "21 x 16g",
            scanDate = "1-5-2024",
            onClickBackArrow = { },
            onClickArchive = {}
        )
    }
}