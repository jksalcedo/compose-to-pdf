package com.jksalcedo.app

data class PdfPageSize(val width: Int, val height: Int, val dpi: Int) {
    companion object {

        // A4: 8.27 x 11.69 inches
        fun A4(dpi: Int = 72) = PdfPageSize((8.27 * dpi).toInt(), (11.69 * dpi).toInt(), dpi)

        // US Letter: 8.5 x 11 inches
        fun Letter(dpi: Int = 72) = PdfPageSize((8.5 * dpi).toInt(), (11 * dpi), dpi)

    }
}