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
- Custom page size
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
val file = File(it, "test.pdf")
val outputStream = FileOutputStream(file)
val result = pdfGenerator.generate(
    outputStream = outputStream,
    pageSize = PdfPageSize.A4(72) // with 72 dpi (defaultt)
        .orientation(Orientation.LANDSCAPE), // new feature
    margin = 160.dp, // new feature (1 inch)
    pages = listOf({
        Text(
            text = content.text.toString()
        )
    }, {
        PdfAsyncImage(
            model = "https://www.pixelstalk.net/wp-content/uploads/2016/06/HD-images-of-nature-download.jpg",
            contentDescription = ""
        )
    }
    )
)
```
