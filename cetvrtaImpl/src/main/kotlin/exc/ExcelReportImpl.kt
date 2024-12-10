package exc

import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import spec.ReportInterface
import java.io.FileOutputStream
import java.time.format.TextStyle

class ExcelReportImpl : ReportInterface {
    override val implName: String = "XLS" //Koristi se od strane aplikacije da prepozna output format

    // Styles
    private var titleStyle: CellStyle? = null
    private var headerStyle: CellStyle? = null
    private var summaryStyle: CellStyle? = null
    private var borderStyle = BorderStyle.THIN
    private var borderColor = IndexedColors.BLACK.index

    override fun generateReport(
        data: Map<String, List<String>>, //Imena kolona su kljucevi a vrednosti kolona su value
        destination: String,
        header: Boolean,
        title: String?,
        summary: String?
    ) {
        val workbook: Workbook = XSSFWorkbook() //Kreiramo novi excel workbook i sheet unutar njega
        val sheet: Sheet = workbook.createSheet("Report")

        // Apply default styles if not set
        if (titleStyle == null || headerStyle == null || summaryStyle == null) {
            applyDefaultStyles(workbook)
        }

        // Add title if provided
        title?.let {
            val titleRow: Row = sheet.createRow(0) //Kreiramo novi red na indexu 0 za title
            val titleCell: Cell = titleRow.createCell(0) //Kreiramo cell u prvoj koloni title row-a
            titleCell.setCellValue(it) //setujemo title row

            //Mergujemo cellove radi title-a
            sheet.addMergedRegion(CellRangeAddress(0, 0, 0, data.size - 1))
            titleCell.cellStyle = titleStyle
        }

        //Adjustujemo header ako je pristuan
        val headerRowIndex = if (title != null) 1 else 0
        if (header) {
            val headerRow: Row = sheet.createRow(headerRowIndex) //Kreiramo header row
            data.keys.forEachIndexed { index, columnName -> ///Iteriramo kroz svaki column name
                val headerCell = headerRow.createCell(index) //Kreiramo cell za heder na specificiranoj poziciji
                headerCell.setCellValue(columnName) //Setujemo column name kao cell value
                headerCell.cellStyle = headerStyle

                // Auto-size columns based on header content
                sheet.autoSizeColumn(index)
            }
        }

        //Index prvog data row-a posle hedera
        val dataRowIndex = headerRowIndex + 1
        val numRows = data.values.first().size //Determinisemo broj redova na osnovu p
        for (i in 0 until numRows) { //Iteriramo kroz svaki red
            val dataRow: Row = sheet.createRow(dataRowIndex + i) //Kreiramo novi red u excelu za svaki indeks
            data.keys.forEachIndexed { index, columnName -> //Iteriramokroz column name
                val cell = dataRow.createCell(index) //kreiramo cell u tom redu na specificiranom indeksu
                cell.setCellValue(data[columnName]?.get(i) ?: "") //setujemo cell value

                // Apply border style
                val cellStyle = workbook.createCellStyle().apply {
                    borderBottom = borderStyle
                    borderTop = borderStyle
                    borderLeft = borderStyle
                    borderRight = borderStyle
                    bottomBorderColor = borderColor
                    topBorderColor = borderColor
                    leftBorderColor = borderColor
                    rightBorderColor = borderColor
                }
                cell.cellStyle = cellStyle
            }
        }

        //Kalukilisemo index za summary row
        val summaryRowIndex = dataRowIndex + numRows
        summary?.let {
            val summaryRow: Row = sheet.createRow(summaryRowIndex) //Kreiramo novi red za summary
            val summaryCell: Cell = summaryRow.createCell(0) //Kreiramo prvi cell u summary row-u
            summaryCell.setCellValue("Summary: $it") //Setujemo summary text
            summaryCell.cellStyle = summaryStyle //Appliujemo summary styling
        }

        // Write the workbook to the file
        FileOutputStream(destination).use { outputStream ->
            workbook.write(outputStream) //Pisemo workbook na specificiranu destinaciju
        }
        workbook.close()
    }

    override fun addHeader(columns: List<String>) {
        println("Adding headers is integrated within generateReport.")
    }

    override fun addTitle(title: String?) {
        println("Title styling is applied dynamically in generateReport.")
    }

    override fun addSummary(summaryData: Map<String, Any>) {
        println("Summary is applied dynamically in generateReport.")
    }

    // Apply default styles
    private fun applyDefaultStyles(workbook: Workbook) {
        // Title style
        titleStyle = workbook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER //center-alignujemo title
            val font = workbook.createFont().apply {
                bold = true //Boldujemo title
                fontHeightInPoints = 18
            }
            setFont(font)
        }

        // Header style
        headerStyle = workbook.createCellStyle().apply {
            fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            val font = workbook.createFont().apply { bold = true }
            setFont(font)
        }

        // Summary style
        summaryStyle = workbook.createCellStyle().apply {
            val font = workbook.createFont().apply { italic = true }
            setFont(font)
        }
    }

    override fun setTitleStyle(style: TextStyle) {
        titleStyle = applyCustomStyle(style)
    }

    override fun setSummaryStyle(style: TextStyle) {
        summaryStyle = applyCustomStyle(style)
    }

    override fun setHeaderStyle(style: TextStyle) {
        headerStyle = applyCustomStyle(style)
    }

    override fun setTableLineThickness(thickness: Int) {
        borderStyle = when (thickness) {
            1 -> BorderStyle.THIN
            2 -> BorderStyle.MEDIUM
            3 -> BorderStyle.THICK
            else -> BorderStyle.THIN
        }
    }

    override fun setTableBorderColor(color: String?) {
        borderColor = when (color?.lowercase()) {
            "red" -> IndexedColors.RED.index
            "blue" -> IndexedColors.BLUE.index
            "green" -> IndexedColors.GREEN.index
            else -> IndexedColors.BLACK.index
        }
    }

    // Custom method to apply styles dynamically based on TextStyle
    private fun applyCustomStyle(style: TextStyle): CellStyle {
        val workbook = XSSFWorkbook()
        val cellStyle = workbook.createCellStyle()
        val font = workbook.createFont()

        when (style) {
            TextStyle.FULL -> {
                font.bold = true
                font.fontHeightInPoints = 16
            }
            else -> {
                font.bold = false
                font.fontHeightInPoints = 12
            }
        }
        cellStyle.setFont(font)
        return cellStyle
    }
}