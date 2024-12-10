package txt

import spec.ReportInterface
import java.io.File
import java.io.PrintWriter
import java.time.format.TextStyle

class TxtReportImpl(private val rowNumbering: Boolean = false) : ReportInterface {

    override val implName: String = "TXT"

    override fun generateReport(
        data: Map<String, List<String>>, //Kljucevi su imena kolona, a vrednosti su redovi podataka
        destination: String, //Gde cuvamo
        header: Boolean, //header, boolean jer mozda nije obavezan
        title: String?, //title je takodje opcionalan
        summary: String?
    ) {
        // Convert data to a MutableMap to allow modifications for additional columns
        val mutableData = data.toMutableMap()
        val columns = mutableData.keys.toMutableList()
        val numRows = data.values.first().size

        // Trazi maksimalnu sirinu svake kolone, da bi videli da sve moze da upadne u cell kako valja
        val columnWidths = columns.map { column ->
            val maxDataWidth = mutableData[column]?.maxOfOrNull { it.length } ?: 0 //Trazimo najduzi string u koloni
            maxOf(column.length, maxDataWidth) //Osiguravamo da je sirina kolone barem siroka kao i ime
        }
        val rowNumberWidth = if (rowNumbering) numRows.toString().length + 2 else 0  //

        //Otvaramo fajl i spremamo ga da pisemo kontent u njemu
        File(destination).printWriter().use { writer ->
            //Pisemo title ako je dat
            title?.let {
                writer.println(it)
                writer.println()
            }

            // Da li korisnik ima heder
            if (header) {
                if (rowNumbering) writer.print("No.".padEnd(rowNumberWidth))  //Ako je row numbering enablovan dodavacemo No. u redovima
                columns.forEachIndexed { index, column ->
                    writer.print(column.padEnd(columnWidths[index] + 2))  //Proveravamo da li ce sve stati, odnosno osiguravamo
                }
                writer.println()

                //Ako je row numbering enablovano brisemo separator liniju
                if (rowNumbering) writer.print("-".repeat(rowNumberWidth))  // Line for row numbers if enabled
                columnWidths.forEach { width -> //Printujemo dashove za svaku kolonu za duzinu koja je jedanka sirini kolone plus 2
                    writer.print("-".repeat(width + 2))  // +2 for spacing
                }
                writer.println()
            }

            // Write each row of data, properly spaced
            for (i in 0 until numRows) { //Loopojemo kroz sve redove, ako je numbering enablovan pisem brojeve
                if (rowNumbering) writer.print("${i + 1}".padEnd(rowNumberWidth))  // Add row number for each row if enabled
                columns.forEachIndexed { index, column ->
                    val cell = mutableData[column]?.get(i) ?: "" //Uzimam vrednost trenutne kolone, ako nema onda je null
                    writer.print(cell.padEnd(columnWidths[index] + 2))  // padujemo cell kako bi obezbedili da staje sve lepo
                }
                writer.println()
            }

            // Write summary if provided, and include calculations
            summary?.let {
                writer.println()
                writer.println(it)
            }
        }
    }

    override fun addHeader(columns: List<String>) { /* Not needed for this implementation */ }

    override fun addTitle(title: String?) { /* Not needed for this implementation */ }

    override fun addSummary(summaryData: Map<String, Any>) { /* Not needed for this implementation */ }

    override fun setTitleStyle(style: TextStyle) { /* Not applicable for plain text */ }

    override fun setSummaryStyle(style: TextStyle) { /* Not applicable for plain text */ }

    override fun setHeaderStyle(style: TextStyle) { /* Not applicable for plain text */ }

    override fun setTableLineThickness(thickness: Int) { /* Not applicable for plain text */ }

    override fun setTableBorderColor(color: String?) { /* Not applicable for plain text */ }
}
