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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PdfGenerator(private val context: Context) {

    /**
     * Generates a PDF with a single page from the given Composable.
     */
    suspend fun generate(
        destination: File,
        pageSize: PdfPageSize = PdfPageSize.A4, // Default to A4
        pages: List<@Composable () -> Unit>
    ) = withContext(Dispatchers.Main) {

        val pdfDocument = PdfDocument()

        try {
            pages.forEachIndexed { index, pageContent ->

                val contentReady = CompletableDeferred<Unit>()
                val pageInfo = PdfDocument.PageInfo.Builder(
                    pageSize.width, pageSize.height, index + 1
                ).create()
                val page = pdfDocument.startPage(pageInfo)

                // Create the View
                val composeView = ComposeView(context).apply {
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
                    setContent {
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

                // keep the view invisible
                val rootLayout =
                    (context as? Activity)?.window?.decorView?.findViewById<ViewGroup>(R.id.content)
                        ?: throw IllegalStateException("Context must be an Activity")

                // Add view with 0 alpha
                composeView.alpha = 0f
                rootLayout.addView(composeView, LayoutParams(pageSize.width, pageSize.height))

                // Safely wait for the content
                contentReady.await()

                composeView.measure(
                    View.MeasureSpec.makeMeasureSpec(pageSize.width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(pageSize.height, View.MeasureSpec.EXACTLY)
                )
                composeView.layout(0, 0, pageSize.width, pageSize.height)

                // Draw
                composeView.draw(page.canvas)

                rootLayout.removeView(composeView)
                pdfDocument.finishPage(page)
            }

            //Write to File
            withContext(Dispatchers.IO) {
                FileOutputStream(destination).use {
                    pdfDocument.writeTo(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            pdfDocument.close()
        }
    }

    private fun Int.toDp(): Dp {
        val density = context.resources.displayMetrics.density
        return (this / density).dp
    }
}