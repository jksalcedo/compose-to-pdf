/*
 * Copyright (C) 2025 Jaressen Kyle Salcedo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
*/
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