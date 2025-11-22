package com.jksalcedo.composetopdf

data class PdfPageSize(val width: Int, val height: Int) {
    companion object {
        // 150 dpi
        // (8.27 x 150) x (11.69 x 150) inches = 595 x 842
        val A4 = PdfPageSize(1240, 1753)

        // US Letter: 8.5 x 11 inches = 612 x 792 points
        val Letter = PdfPageSize(1275, 1650)
    }
}