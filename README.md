# ComposeToPdf

**Generate PDF from your Jetpack Compose UI Code**
No XML layouts. No HTML conversion. Just pure Compose.

## Features

- Native: Uses Android's native PdfDocument.
- Vector: Content are vectors, not pixelated
- Multi-Page: Simple API to generate multi-page documents.
- Invisible: Renders in the background without disturbing the UI.

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
    implementation("com.github.jksalcedo:compose-to-pdf:1.0.0-beta01")
}
```

## Usage

```kotlin
val pdfGenerator = PdfGenerator(context)

pdfGenerator.generate(
    // filesDir is the output location
    destination = File(filesDir, "invoice.pdf"),
    pageSize = PdfPageSize.A4,
    pages = listOf(
        {
            // Page 1 Content
            Column(modifier = Modifier.background(Color.White)) {
                Text("Invoice #1024")
            }
        }, {
            // Page 2 Content
            Text("Terms and Conditions")
        }
        // Other pages
    )
)
```
