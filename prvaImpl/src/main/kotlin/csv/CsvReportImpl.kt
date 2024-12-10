package csv

import spec.ReportInterface
import java.io.File
import java.time.format.TextStyle

class CsvReportImpl : ReportInterface{

    override val implName: String = "CSV"

    override fun generateReport(
        data: Map<String, List<String>>,
        destination: String,
        header: Boolean,
        title: String?,
        summary: String?
    ) {
        val columns = data.keys.toList()
        val numRows = data.values.first().size

        // Write to CSV file
        File(destination).printWriter().use { writer ->
            if(header)
                writer.println(columns.joinToString(","))  // Write the header
            for (i in 0 until numRows) {
                val row = columns.map { column -> data[column]?.get(i) ?: "" }
                writer.println(row.joinToString(","))   // Write each row
            }
        }
    }

    //Ignore, CSV doesn't support
    override fun addHeader(columns: List<String>) {
        TODO("Not yet implemented")
    }

    override fun addTitle(title: String?) {
        TODO("Not yet implemented")
    }

    override fun addSummary(summaryData: Map<String, Any>) {
        TODO("Not yet implemented")
    }

    override fun setTitleStyle(style: TextStyle) {
        TODO("Not yet implemented")
    }

    override fun setSummaryStyle(style: TextStyle) {
        TODO("Not yet implemented")
    }

    override fun setHeaderStyle(style: TextStyle) {
        TODO("Not yet implemented")
    }

    override fun setTableLineThickness(thickness: Int) {
        TODO("Not yet implemented")
    }

    override fun setTableBorderColor(color: String?) {
        TODO("Not yet implemented")
    }
}
