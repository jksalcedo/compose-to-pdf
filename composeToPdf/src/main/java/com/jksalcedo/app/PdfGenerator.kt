package com.jksalcedo.app

//noinspection SuspiciousImport
import android.R
import android.app.Activity
import android.content.Context
import android.graphics.pdf.PdfDocument
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.OutputStream

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

                        delay(500)
                        // Wait for the images to fully load
                        imageMonitor.waitForImages()

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
                        delay(100)
                        composeView.draw(page.canvas)
                    } catch (e: Exception) {
                        e.printStackTrace()
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
}