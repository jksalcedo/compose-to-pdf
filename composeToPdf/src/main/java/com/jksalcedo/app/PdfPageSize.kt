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

data class PdfPageSize(val width: Int, val height: Int, val dpi: Int = 72) {

    fun orientation(orientation: Orientation): PdfPageSize {
        return when (orientation) {
            Orientation.PORTRAIT -> {
                if (width > height) {
                    PdfPageSize(width = height, height = width, dpi = dpi)
                } else {
                    this
                }
            }

            Orientation.LANDSCAPE -> {
                if (width < height) {
                    PdfPageSize(width = height, height = width, dpi = dpi)
                } else {
                    this
                }
            }
        }
    }

    companion object {

        // A0: 841 x 1189 mm (33.1 x 46.8 inches)
        fun A0(dpi: Int = 72) = PdfPageSize((33.11 * dpi).toInt(), (46.81 * dpi).toInt(), dpi)

        // A1: 594 x 841 mm (23.4 x 33.1 inches)
        fun A1(dpi: Int = 72) = PdfPageSize((23.39 * dpi).toInt(), (33.11 * dpi).toInt(), dpi)

        // A2: 420 x 594 mm (16.5 x 23.4 inches)
        fun A2(dpi: Int = 72) = PdfPageSize((16.54 * dpi).toInt(), (23.39 * dpi).toInt(), dpi)

        // A3: 297 x 420 mm (11.7 x 16.5 inches)
        fun A3(dpi: Int = 72) = PdfPageSize((11.69 * dpi).toInt(), (16.54 * dpi).toInt(), dpi)

        // A4: 210 x 297 mm (8.27 x 11.69 inches)
        fun A4(dpi: Int = 72) = PdfPageSize((8.27 * dpi).toInt(), (11.69 * dpi).toInt(), dpi)

        // A5: 148 x 210 mm (5.83 x 8.27 inches)
        fun A5(dpi: Int = 72) = PdfPageSize((5.83 * dpi).toInt(), (8.27 * dpi).toInt(), dpi)

        // A6: 105 x 148 mm (4.13 x 5.83 inches)
        fun A6(dpi: Int = 72) = PdfPageSize((4.13 * dpi).toInt(), (5.83 * dpi).toInt(), dpi)

        // A7: 74 x 105 mm (2.91 x 4.13 inches)
        fun A7(dpi: Int = 72) = PdfPageSize((2.91 * dpi).toInt(), (4.13 * dpi).toInt(), dpi)

        // US Letter: 8.5 x 11 inches
        fun Letter(dpi: Int = 72) = PdfPageSize((8.5 * dpi).toInt(), (11.0 * dpi).toInt(), dpi)

        // US Legal: 8.5 x 14 inches
        fun Legal(dpi: Int = 72) = PdfPageSize((8.5 * dpi).toInt(), (14.0 * dpi).toInt(), dpi)

        // US Tabloid/Ledger: 11 x 17 inches
        fun Tabloid(dpi: Int = 72) = PdfPageSize((11.0 * dpi).toInt(), (17.0 * dpi).toInt(), dpi)

        // US Executive: 7.25 x 10.5 inches
        fun Executive(dpi: Int = 72) = PdfPageSize((7.25 * dpi).toInt(), (10.5 * dpi).toInt(), dpi)

        // B4: 250 x 353 mm (9.84 x 13.90 inches)
        fun B4(dpi: Int = 72) = PdfPageSize((9.84 * dpi).toInt(), (13.90 * dpi).toInt(), dpi)

        // B5: 176 x 250 mm (6.93 x 9.84 inches)
        fun B5(dpi: Int = 72) = PdfPageSize((6.93 * dpi).toInt(), (9.84 * dpi).toInt(), dpi)

        // C4 Envelope: 229 x 324 mm (9.02 x 12.76 inches) - fits A4 unfolded
        fun C4(dpi: Int = 72) = PdfPageSize((9.02 * dpi).toInt(), (12.76 * dpi).toInt(), dpi)

        // C5 Envelope: 162 x 229 mm (6.38 x 9.02 inches) - fits A4 folded once
        fun C5(dpi: Int = 72) = PdfPageSize((6.38 * dpi).toInt(), (9.02 * dpi).toInt(), dpi)

        // DL Envelope: 110 x 220 mm (4.33 x 8.66 inches) - fits A4 folded twice
        fun DL(dpi: Int = 72) = PdfPageSize((4.33 * dpi).toInt(), (8.66 * dpi).toInt(), dpi)

        // #10 Envelope: 4.125 x 9.5 inches - Common US business envelope
        fun Envelope10(dpi: Int = 72) = PdfPageSize((4.125 * dpi).toInt(), (9.5 * dpi).toInt(), dpi)

        // US Business Card: 3.5 x 2 inches
        fun BusinessCardUS(dpi: Int = 72) =
            PdfPageSize((3.5 * dpi).toInt(), (2.0 * dpi).toInt(), dpi)

        // EU Business Card: 85 x 55 mm (3.35 x 2.17 inches)
        fun BusinessCardEU(dpi: Int = 72) =
            PdfPageSize((3.35 * dpi).toInt(), (2.17 * dpi).toInt(), dpi)

        // 4x6 Photo
        fun Photo4x6(dpi: Int = 72) = PdfPageSize((4.0 * dpi).toInt(), (6.0 * dpi).toInt(), dpi)

        // 5x7 Photo
        fun Photo5x7(dpi: Int = 72) = PdfPageSize((5.0 * dpi).toInt(), (7.0 * dpi).toInt(), dpi)

        // 8x10 Photo
        fun Photo8x10(dpi: Int = 72) = PdfPageSize((8.0 * dpi).toInt(), (10.0 * dpi).toInt(), dpi)

        // Small Poster: 11 x 17 inches (same as Tabloid)
        fun PosterSmall(dpi: Int = 72) = Tabloid(dpi)

        // Medium Poster: 18 x 24 inches
        fun PosterMedium(dpi: Int = 72) =
            PdfPageSize((18.0 * dpi).toInt(), (24.0 * dpi).toInt(), dpi)

        // Large Poster: 24 x 36 inches
        fun PosterLarge(dpi: Int = 72) =
            PdfPageSize((24.0 * dpi).toInt(), (36.0 * dpi).toInt(), dpi)

        // 16:9 HD: 1920 x 1080 pixels (scaled to inches at given DPI)
        fun HD1080(dpi: Int = 72) = PdfPageSize(1920, 1080, dpi)

        // 16:9 4K: 3840 x 2160 pixels (scaled to inches at given DPI)
        fun UHD4K(dpi: Int = 72) = PdfPageSize(3840, 2160, dpi)


        fun fromInches(widthInches: Double, heightInches: Double, dpi: Int = 72) =
            PdfPageSize((widthInches * dpi).toInt(), (heightInches * dpi).toInt(), dpi)

        fun fromMillimeters(widthMm: Double, heightMm: Double, dpi: Int = 72) =
            PdfPageSize(
                (widthMm / 25.4 * dpi).toInt(),
                (heightMm / 25.4 * dpi).toInt(),
                dpi
            )
    }
}

enum class Orientation {
    PORTRAIT, LANDSCAPE
}