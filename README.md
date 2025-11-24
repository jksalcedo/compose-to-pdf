# ComposeToPdf

[![](https://jitpack.io/v/jksalcedo/compose-to-pdf.svg)](https://jitpack.io/#jksalcedo/compose-to-pdf)

**Generate PDF from your Jetpack Compose UI Code**
No XML layouts. No HTML conversion. Just pure Compose.

## Features

- Native: Uses Android's native PdfDocument.
- Vector: Content are vectors, not pixelated (except for images)
- Multi-Page: Simple API to generate multi-page documents.
- Invisible: Renders in the background without disturbing the UI.
- Asynchronous: Supports asynchronous image loading.
- Page Size: Supports all standard page sizes (A*, B*, etc.).
- Custom timeouts
- Page Orientation

## Installation

Add the JitPack repository to your build file:

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}
```

Add the dependency to `app/build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.jksalcedo:compose-to-pdf:1.1.0")
}
```

## Usage

```kotlin
val pdfGenerator = PdfGenerator(context = context)
val path = context.getExternalFilesDir("PDF")

path?.let {
val file = File(it, "test.pdf")
val outputStream = FileOutputStream(file)
val result = pdfGenerator.generate(
    outputStream = outputStream,
    // Allows custom DPI
    pageSize = PdfPageSize.A4(100),
    pages = listOf({
        Text(
            text = "Sample Text"
        )
    }, {
        // Custom Asynchronous Image Loader
        PdfAsyncImage(
            model = "https://example.com/10.jpg",
            contentDescription = ""
        )
    }
    )
)
```
