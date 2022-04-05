package employee.location.site.utils

import android.content.Context
import android.graphics.*
import android.util.Log
import com.itextpdf.text.*
import com.itextpdf.text.pdf.Barcode128
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import employee.location.site.models.Work
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream

const val PDF_REPORT_TYPE_EMPLOYEE = 0
const val PDF_REPORT_TYPE_LOCATION = 1
const val PDF_REPORT_TYPE_ACTIVITY = 2

internal object PDFUtility {
    private val TAG = PDFUtility::class.java.simpleName
    private val FONT_TITLE: Font = Font(Font.FontFamily.TIMES_ROMAN, 16f, Font.BOLD)
    private val FONT_SUBTITLE: Font = Font(Font.FontFamily.TIMES_ROMAN, 10f, Font.NORMAL)
    private val FONT_CELL: Font = Font(Font.FontFamily.TIMES_ROMAN, 12f, Font.NORMAL)
    private val FONT_COLUMN: Font = Font(Font.FontFamily.TIMES_ROMAN, 14f, Font.NORMAL)

    @Throws(Exception::class)
    fun createPdf(
        mContext: Context,
        mCallback: OnDocumentClose?,
        items: List<Work>,
        reportType: Int,
        headings: String,
        totalText: String,
        fileOutputStream: FileOutputStream?,
        isPortrait: Boolean
    ) {
        if (fileOutputStream == null) {
            throw NullPointerException("PDF File Name can't be null or blank. PDF File can't be created")
        }

        val document = Document()
        document.setMargins(24f, 24f, 32f, 32f)
        document.pageSize = if (isPortrait) PageSize.A4 else PageSize.A4.rotate()
        val pdfWriter = PdfWriter.getInstance(document, fileOutputStream)
        pdfWriter.setFullCompression()
        pdfWriter.pageEvent = PageNumeration()
        document.open()
        setMetaData(document)

        addHeader(mContext, document, headings);
        addEmptyLine(document, 3)
        document.add(createDataTable(items, reportType))
        document.add(createTotalText(totalText))
        document.close()
        try {
            pdfWriter.close()
        } catch (ex: Exception) {
            Log.e(TAG, "Error While Closing pdfWriter : $ex")
        }
        mCallback?.onPDFDocumentClose()
    }

    @Throws(DocumentException::class)
    private fun addEmptyLine(document: Document, number: Int) {
        for (i in 0 until number) {
            document.add(Paragraph(" "))
        }
    }

    private fun setMetaData(document: Document) {
        document.addCreationDate()
        //document.add(new Meta("",""));
        document.addAuthor("RAVEESH G S")
        document.addCreator("RAVEESH G S")
        document.addHeader("DEVELOPER", "RAVEESH G S")
    }

    @Throws(Exception::class)
    private fun addHeader(mContext: Context, document: Document, heading: String) {
        val table = PdfPTable(1)
        table.widthPercentage = 100f
        table.setWidths(floatArrayOf(1f))
        table.defaultCell.border = PdfPCell.NO_BORDER
        table.defaultCell.verticalAlignment = Element.ALIGN_CENTER
        table.defaultCell.horizontalAlignment = Element.ALIGN_CENTER
        var cell: PdfPCell

        run {
            /*MIDDLE TEXT*/cell = PdfPCell()
            cell.horizontalAlignment = Element.ALIGN_CENTER
            cell.border = PdfPCell.NO_BORDER
            cell.setPadding(8f)
            cell.isUseAscender = true
            var temp = Paragraph(heading, FONT_TITLE)
            temp.alignment = Element.ALIGN_CENTER
            cell.addElement(temp)
            temp = Paragraph("", FONT_SUBTITLE)
            temp.alignment = Element.ALIGN_CENTER
            cell.addElement(temp)
            table.addCell(cell)
        }

        //Paragraph paragraph=new Paragraph("",new Font(Font.FontFamily.TIMES_ROMAN, 2.0f, Font.NORMAL, BaseColor.BLACK));
        //paragraph.add(table);
        //document.add(paragraph);
        document.add(table)
    }

    @Throws(DocumentException::class)
    private fun createDataTable(works: List<Work>, reportType: Int): PdfPTable {
        val table1: PdfPTable

        if (reportType == PDF_REPORT_TYPE_EMPLOYEE) {
            table1 = PdfPTable(3)
            table1.setWidths(floatArrayOf(2f, 1f, 1f))
        } else {
            table1 = PdfPTable(4)
            table1.setWidths(floatArrayOf(2f, 4f, 1f, 1f))
        }
        table1.widthPercentage = 100f
        table1.headerRows = 1
        table1.defaultCell.verticalAlignment = Element.ALIGN_CENTER
        table1.defaultCell.horizontalAlignment = Element.ALIGN_CENTER
        var cell: PdfPCell
        run {
            if (reportType != PDF_REPORT_TYPE_EMPLOYEE) {
                cell = PdfPCell(Phrase("Employee Name", FONT_COLUMN))
                cell.horizontalAlignment = Element.ALIGN_CENTER
                cell.verticalAlignment = Element.ALIGN_MIDDLE
                cell.setPadding(4f)
                table1.addCell(cell)
            }

            cell = PdfPCell(Phrase("Activity", FONT_COLUMN))
            cell.horizontalAlignment = Element.ALIGN_CENTER
            cell.verticalAlignment = Element.ALIGN_MIDDLE
            cell.setPadding(4f)
            table1.addCell(cell)

            cell = PdfPCell(Phrase("Value", FONT_COLUMN))
            cell.horizontalAlignment = Element.ALIGN_CENTER
            cell.verticalAlignment = Element.ALIGN_MIDDLE
            cell.setPadding(4f)
            table1.addCell(cell)

            cell = PdfPCell(Phrase("Cost", FONT_COLUMN))
            cell.horizontalAlignment = Element.ALIGN_CENTER
            cell.verticalAlignment = Element.ALIGN_MIDDLE
            cell.setPadding(4f)
            table1.addCell(cell)
        }

        val top_bottom_Padding = 8f
        val left_right_Padding = 4f
        var alternate = false
        val lt_gray = BaseColor(221, 221, 221) //#DDDDDD
        var cell_color: BaseColor?

        for (work in works) {
            cell_color = if (alternate) lt_gray else BaseColor.WHITE

            if (reportType != PDF_REPORT_TYPE_EMPLOYEE) {
                cell = PdfPCell(Phrase(work.empName, FONT_CELL))
                cell.horizontalAlignment = Element.ALIGN_LEFT
                cell.verticalAlignment = Element.ALIGN_MIDDLE
                cell.paddingLeft = left_right_Padding
                cell.paddingRight = left_right_Padding
                cell.paddingTop = top_bottom_Padding
                cell.paddingBottom = top_bottom_Padding
                cell.backgroundColor = cell_color
                table1.addCell(cell)
            }

            cell = PdfPCell(Phrase(work.activity.activityName, FONT_CELL))
            cell.horizontalAlignment = Element.ALIGN_CENTER
            cell.verticalAlignment = Element.ALIGN_MIDDLE
            cell.paddingLeft = left_right_Padding
            cell.paddingRight = left_right_Padding
            cell.paddingTop = top_bottom_Padding
            cell.paddingBottom = top_bottom_Padding
            cell.backgroundColor = cell_color
            table1.addCell(cell)

            cell = PdfPCell(Phrase(work.value.toString(), FONT_CELL))
            cell.horizontalAlignment = Element.ALIGN_CENTER
            cell.verticalAlignment = Element.ALIGN_MIDDLE
            cell.paddingLeft = left_right_Padding
            cell.paddingRight = left_right_Padding
            cell.paddingTop = top_bottom_Padding
            cell.paddingBottom = top_bottom_Padding
            cell.backgroundColor = cell_color
            table1.addCell(cell)

            cell = PdfPCell(Phrase(work.activity.cost.toString(), FONT_CELL))
            cell.horizontalAlignment = Element.ALIGN_CENTER
            cell.verticalAlignment = Element.ALIGN_MIDDLE
            cell.paddingLeft = left_right_Padding
            cell.paddingRight = left_right_Padding
            cell.paddingTop = top_bottom_Padding
            cell.paddingBottom = top_bottom_Padding
            cell.backgroundColor = cell_color
            table1.addCell(cell)

            alternate = !alternate
        }
        return table1
    }

    @Throws(DocumentException::class)
    private fun createTotalText(totalText: String): PdfPTable {
        val outerTable = PdfPTable(1)
        outerTable.widthPercentage = 100f
        outerTable.defaultCell.border = PdfPCell.NO_BORDER
        val innerTable = PdfPTable(2)
        run {
            innerTable.widthPercentage = 100f
            innerTable.setWidths(floatArrayOf(1f, 1f))
            innerTable.defaultCell.border = PdfPCell.NO_BORDER

            //ROW-1 : EMPTY SPACE
            var cell = PdfPCell()
            cell.border = PdfPCell.NO_BORDER
            cell.fixedHeight = 60f
            innerTable.addCell(cell)

            //ROW-4 : Content Right Aligned
            cell = PdfPCell(Phrase(totalText, FONT_SUBTITLE))
            cell.horizontalAlignment = Element.ALIGN_RIGHT
            cell.border = PdfPCell.NO_BORDER
            cell.setPadding(4f)
            innerTable.addCell(cell)
        }
        val signRow = PdfPCell(innerTable)
        signRow.horizontalAlignment = Element.ALIGN_LEFT
        signRow.border = PdfPCell.NO_BORDER
        signRow.setPadding(4f)
        outerTable.addCell(signRow)
        return outerTable
    }

    @Throws(Exception::class)
    private fun getImage(imageByte: ByteArray, isTintingRequired: Boolean): Image {
        val paint = Paint()
        if (isTintingRequired) {
            paint.colorFilter = PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN)
        }
        val input = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.size)
        val output = Bitmap.createBitmap(input.width, input.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        canvas.drawBitmap(input, 0f, 0f, paint)
        val stream = ByteArrayOutputStream()
        output.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image = Image.getInstance(stream.toByteArray())
        image.widthPercentage = 80f
        return image
    }

    private fun getBarcodeImage(pdfWriter: PdfWriter, barcodeText: String): Image {
        val barcode = Barcode128()
        //barcode.setBaseline(-1); //BARCODE TEXT ABOVE
        barcode.font = null
        barcode.code = barcodeText
        barcode.codeType = Barcode128.CODE128
        barcode.textAlignment = Element.ALIGN_BASELINE
        return barcode.createImageWithBarcode(pdfWriter.directContent, BaseColor.BLACK, null)
    }

    interface OnDocumentClose {
        fun onPDFDocumentClose()
    }
}