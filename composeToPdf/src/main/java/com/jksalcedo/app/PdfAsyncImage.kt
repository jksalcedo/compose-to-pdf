package com.jksalcedo.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware

@Composable
fun PdfAsyncImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
    clipToBound: Boolean = true
) {
    val monitor = LocalPdfImageMonitor.current
    val context = LocalContext.current

    val request = remember(model) {
        ImageRequest.Builder(context)
            .data(model)
            .allowHardware(false)
            .build()
    }

    AsyncImage(
        model = request,
        contentDescription = contentDescription,
        modifier = modifier,
        onLoading = {
            monitor?.notifyLoadingStarted()
        },
        onSuccess = {
            monitor?.notifyLoadingFinished()
        },
        onError = {
            monitor?.notifyLoadingFinished()
        },
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
        filterQuality = filterQuality,
        placeholder = null,
        error = null,
        fallback = null,
        clipToBounds = clipToBound
    )
}