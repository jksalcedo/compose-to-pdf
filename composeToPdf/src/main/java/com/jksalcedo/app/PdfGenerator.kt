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

//noinspection SuspiciousImport
import android.R
import android.app.Activity
import android.content.Context
import android.graphics.pdf.PdfDocument
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewTreeObserver
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.OutputStream
import kotlin.coroutines.resume

class PdfGenerator(private val context: Context) {

    /**
     * Generates a PDF from the given Composables.
     */
    suspend fun generate(
        outputStream: OutputStream,
        pageSize: PdfPageSize = PdfPageSize.A4(), // Default to A4
        pages: List<@Composable () -> Unit>
    ): Result<String> = withContext(Dispatchers.Main) {

        outputStream.use { out ->
            val pdfDocument = PdfDocument()
            val imageMonitor = PdfImageMonitor()

            try {
                pages.forEachIndexed { index, pageContent ->

                    val contentReady = CompletableDeferred<Unit>()
                    val pageInfo = PdfDocument.PageInfo.Builder(
                        pageSize.width, pageSize.height, index + 1
                    ).create()
                    val page = pdfDocument.startPage(pageInfo)

                    // Create the View
                    var viewAttached = false
                    // keep the view invisible
                    val composeView = ComposeView(context = context)
                    val rootLayout =
                        (context as? Activity)?.window?.decorView?.findViewById<ViewGroup>(R.id.content)
                            ?: throw IllegalStateException("Context must be an Activity")

                    try {
                        composeView.apply {
                            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
                            setContent {
                                CompositionLocalProvider(LocalPdfImageMonitor provides imageMonitor) {
                                    Box(
                                        modifier = Modifier
                                            .size(
                                                pageSize.width.toDp(),
                                                pageSize.height.toDp()
                                            )
                                            .onGloballyPositioned {
                                                if (!contentReady.isCompleted) {
                                                    contentReady.complete(Unit)
                                                }
                                            }
                                    ) {
                                        pageContent()
                                    }
                                }
                            }
                        }

                        // Add view with 0 alpha
                        composeView.alpha = 0f
                        rootLayout.addView(
                            composeView,
                            LayoutParams(pageSize.width, pageSize.height)
                        )

                        // Safely wait for the content
                        contentReady.await()
                        viewAttached = true

                        // Wait for the next frame
                        waitForNextFrame(composeView)

                        // Wait for the images to fully load
                        imageMonitor.waitForImages(composeView)

                        composeView.measure(
                            View.MeasureSpec.makeMeasureSpec(
                                pageSize.width,
                                View.MeasureSpec.EXACTLY
                            ),
                            View.MeasureSpec.makeMeasureSpec(
                                pageSize.height,
                                View.MeasureSpec.EXACTLY
                            )
                        )
                        composeView.layout(0, 0, pageSize.width, pageSize.height)

                        // Wait for the view to be ready to draw
                        waitForDrawReady(composeView)
                        composeView.draw(page.canvas)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        if (viewAttached) {
                            rootLayout.removeView(composeView)
                        }
                        return@withContext Result.failure(e)
                    } finally {
                        if (viewAttached) {
                            rootLayout.removeView(composeView)
                        }

                        pdfDocument.finishPage(page)
                    }
                }

                //Write to File
                withContext(Dispatchers.IO) {
                    pdfDocument.writeTo(out)
                    out.flush()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext Result.failure(e)
            } finally {
                pdfDocument.close()
            }
            return@withContext Result.success("Successfully generated ${pages.size} pages!")
        }
    }

    private fun Int.toDp(): Dp {
        val density = context.resources.displayMetrics.density
        return (this / density).dp
    }

    private suspend fun waitForNextFrame(view: View) = suspendCancellableCoroutine { continuation ->
        view.post {
            continuation.resume(Unit)
        }
    }

    private suspend fun waitForDrawReady(view: View) = suspendCancellableCoroutine { continuation ->
        var resumed = false
        val observer = view.viewTreeObserver
        val listener = object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                if (!resumed) {
                    resumed = true
                    view.viewTreeObserver.removeOnPreDrawListener(this)
                    continuation.resume(Unit)
                }
                return true
            }
        }
        observer.addOnPreDrawListener(listener)

        // post to check if view is already ready
        view.post {
            if (!resumed && view.width > 0 && view.height > 0 && view.isAttachedToWindow) {
                resumed = true
                observer.removeOnPreDrawListener(listener)
                continuation.resume(Unit)
            }
        }
    }
}