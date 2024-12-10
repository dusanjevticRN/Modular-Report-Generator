package testApp

import spec.ReportInterface
import java.io.File
import java.io.InputStreamReader
import java.time.format.TextStyle
import java.util.ServiceLoader
import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)

    // Proveravamo da li izlazni direktorijum postoji, ako ne napravimo ga
    val outputDir = File("output")
    if (!outputDir.exists()) {
        outputDir.mkdirs()
    }

    // Service loader učitava sve klase koje implementiraju ReportInterface
    val serviceLoader = ServiceLoader.load(ReportInterface::class.java)
    val exporterServices = mutableMapOf<String, ReportInterface>()

    // Punimo mapu exporterServices sa dostupnim formatima izveštaja
    serviceLoader.forEach { service ->
        exporterServices[service.implName] = service
    }

    do {
        // Prikazujemo dostupne formate izveštaja
        println("Dostupni report formati:")
        exporterServices.keys.forEachIndexed { index, format ->
            println("${index + 1}. $format")
        }

        // Korisnik bira format izveštaja
        print("Unesite broj report formata koji zelite da generisete: ")
        val choice = scanner.nextInt()
        scanner.nextLine() // Consumes newline
        val selectedFormat = exporterServices.keys.elementAtOrNull(choice - 1)

        // Obrada nevalidnog izbora
        if (selectedFormat == null) {
            println("Nevalidan izbor. Pokusajte ponovo.")
            continue
        }

        val selectedService = exporterServices[selectedFormat]
        if (selectedService == null) {
            println("Nije pronadjena usluga za izabrani format.")
            continue
        }

        // Učitavamo podatke iz JSON-a ili koristimo predefinisane podatke
        val inputStream = object {}.javaClass.getResourceAsStream("/data.json")
        var reader : InputStreamReader? = null
        if(inputStream != null)
            reader = InputStreamReader(inputStream)

        // Unos naslova i summary-ja za izveštaj
        println("Unesite naslov za report (ostavite prazno za podrazumevani naslov):")
        val title = scanner.nextLine().ifBlank { "Schedule Report" }

        println("Unesite summary za report (ostavite prazno za podrazumevani summary):")
        val summary = scanner.nextLine().ifBlank { "Summary of schedules for various subjects." }

        // Postavke formatiranja za PDF i Excel
        if (selectedFormat == "PDF" || selectedFormat == "XLS") {
            println("Da li zelite boldovane header-e? (yes/no):")
            val boldHeaders = scanner.nextLine().equals("yes", ignoreCase = true)

            println("Unesite boju za header-e (npr. Grey, Blue, None):")
            val headerColor = scanner.nextLine().ifBlank { "None" }

            println("Unesite debljinu linija tabele (1 = tanak, 2 = srednji, 3 = debeo):")
            val lineThickness = scanner.nextInt()
            scanner.nextLine() // Consumes newline

            println("Unesite boju ivica tabele (npr. Red, Blue, Black):")
            val borderColor = scanner.nextLine().ifBlank { "Black" }

            if (selectedFormat == "PDF" || selectedFormat == "XLS") {
                selectedService.setHeaderStyle(if (boldHeaders) TextStyle.FULL else TextStyle.SHORT)
                selectedService.setTableBorderColor(borderColor)
                selectedService.setTableLineThickness(lineThickness)
            }
        }

        println("Unesite kalkulaciju u formatu sum 1,avg 2,cnt 3")
        val calcInput = scanner.nextLine()
        println("Unesite operacije za kolone u formatu 1 + 2,2 * 3,...")
        val colInput = scanner.nextLine()

        val destination = when (selectedFormat) {
            "XLS" -> "output/report.xlsx"
            "CSV" -> "output/report.csv"
            "PDF" -> "output/report.pdf"
            "TXT" -> "output/report.txt"
            else -> "output/report.txt"
        }

        selectedService.generateReport(
            jsonData = reader,
            destination = destination,
            header = true,
            title = title,
            summary = summary,
            calculations = calcInput,
            addColumn = colInput
        )

        println("Report generisan: $destination")

        println("Unesite 'exit' za izlaz ili pritisnite bilo koji taster za nastavak.")
    } while (!scanner.nextLine().equals("exit", ignoreCase = true))
}