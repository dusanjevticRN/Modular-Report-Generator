package pdf

import com.lowagie.text.*
import com.lowagie.text.pdf.*
import spec.ReportInterface
import java.awt.Color
import java.io.FileOutputStream
import java.time.format.TextStyle

class PdfReportImpl : ReportInterface {
    override val implName: String = "PDF"

    private var titleStyle: Font = Font(Font.HELVETICA, 18f, Font.BOLD)
    private var summaryStyle: Font = Font(Font.HELVETICA, 12f, Font.ITALIC)
    private var headerStyle: Font = Font(Font.HELVETICA, 12f, Font.BOLD)
    private var tableBorderThickness: Float = 0.5f
    private var tableBorderColor: Color = Color.BLACK

    override fun generateReport(
        data: Map<String, List<String>>,
        destination: String,
        header: Boolean,
        title: String?,
        summary: String?
    ) {
        val document = Document()

        try {
            // Initialize PdfWriter
            PdfWriter.getInstance(document, FileOutputStream(destination))

            // Open the document for writing
            document.open()

            // Add title if provided
            title?.let {
                val titleParagraph = Paragraph(it, titleStyle)
                titleParagraph.alignment = Element.ALIGN_CENTER
                document.add(titleParagraph)
                document.add(Chunk.NEWLINE)  // Add a new line after the title
            }

            // Create a table based on the number of columns in the data
            val columns = data.keys.toList()
            val numColumns = columns.size
            val table = PdfPTable(numColumns)
            table.widthPercentage = 100f
            table.defaultCell.borderWidth = tableBorderThickness
            table.defaultCell.borderColor = tableBorderColor

            // Add header row if necessary
            if (header) {
                columns.forEach { column ->
                    val headerCell = PdfPCell(Paragraph(column, headerStyle))
                    headerCell.backgroundColor = Color.LIGHT_GRAY
                    headerCell.horizontalAlignment = Element.ALIGN_CENTER
                    headerCell.borderWidth = tableBorderThickness
                    headerCell.borderColor = tableBorderColor
                    table.addCell(headerCell)
                }
            }

            // Add data rows
            val numRows = data.values.first().size
            for (i in 0 until numRows) {
                columns.forEach { column ->
                    val cellData = data[column]?.get(i) ?: ""
                    val dataCell = PdfPCell(Paragraph(cellData))
                    dataCell.borderWidth = tableBorderThickness
                    dataCell.borderColor = tableBorderColor
                    table.addCell(dataCell)
                }
            }

            // Add the table to the document
            document.add(table)

            // Add summary if provided
            summary?.let {
                document.add(Chunk.NEWLINE)
                val summaryParagraph = Paragraph(it, summaryStyle)
                document.add(summaryParagraph)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // Close the document
            document.close()
        }
    }

    override fun addHeader(columns: List<String>) {
        // This function could add a styled header row if implemented
        println("addHeader is not implemented yet.")
    }

    override fun addTitle(title: String?) {
        // Example implementation for custom title
        title?.let {
            titleStyle = Font(Font.HELVETICA, 20f, Font.BOLDITALIC, Color.BLUE)
        }
    }

    override fun addSummary(summaryData: Map<String, Any>) {
        // Example implementation for custom summary styling
        summaryStyle = Font(Font.HELVETICA, 14f, Font.ITALIC, Color.DARK_GRAY)
    }

    override fun setTitleStyle(style: TextStyle) {
        // Example of setting custom title style
        titleStyle = when (style) {
            TextStyle.FULL -> Font(Font.HELVETICA, 18f, Font.BOLDITALIC, Color.BLUE)
            else -> Font(Font.HELVETICA, 18f, Font.BOLD)
        }
    }

    override fun setSummaryStyle(style: TextStyle) {
        // Example of setting custom summary style
        summaryStyle = when (style) {
            TextStyle.FULL -> Font(Font.HELVETICA, 14f, Font.ITALIC, Color.GRAY)
            else -> Font(Font.HELVETICA, 12f, Font.ITALIC)
        }
    }

    override fun setHeaderStyle(style: TextStyle) {
        // Example of setting custom header style
        headerStyle = when (style) {
            TextStyle.FULL -> Font(Font.HELVETICA, 14f, Font.BOLD, Color.BLUE)
            else -> Font(Font.HELVETICA, 12f, Font.BOLD)
        }
    }

    override fun setTableLineThickness(thickness: Int) {
        // Set table border thickness
        tableBorderThickness = thickness.toFloat()
    }

    override fun setTableBorderColor(color: String?) {
        // Set table border color based on input
        tableBorderColor = when (color?.lowercase()) {
            "blue" -> Color.BLUE
            "red" -> Color.RED
            "green" -> Color.GREEN
            else -> Color.BLACK
        }
    }
}
