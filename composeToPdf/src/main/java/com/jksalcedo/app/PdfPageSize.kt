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

data class PdfPageSize(val width: Int, val height: Int, val dpi: Int) {
    companion object {

        // A4: 8.27 x 11.69 inches
        fun A4(dpi: Int = 72) = PdfPageSize((8.27 * dpi).toInt(), (11.69 * dpi).toInt(), dpi)

        // US Letter: 8.5 x 11 inches
        fun Letter(dpi: Int = 72) = PdfPageSize((8.5 * dpi).toInt(), (11 * dpi), dpi)

    }
}