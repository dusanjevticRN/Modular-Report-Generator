package model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
/**
 * Example data class.
 */
data class Schedule(
    val subject: String,
    val classroom: String,
    val year: Int,
    val group: String,
    val day: String,
    val time_from: String,
    val time_to: String
)

// Priprema podataka iz JSON-a ako je dostupan, u suprotnom koristi predefinisane vrednosti
/**
 *Optionally takes json data from a string reader, to fetch data, otherwise uses default data.
 * @return Returns a Map<String, List<String>> with the data inside.
 *
 * @param jsonData InputStreamReader for reading the json data path.
 */
fun prepareData(jsonData: InputStreamReader? = null): Map<String, List<String>> {
    val schedules: List<Schedule> = if (jsonData != null) {
        // Učitavanje podataka iz JSON-a koristeći Gson
        val gson = Gson()
        val scheduleType = object : TypeToken<List<Schedule>>() {}.type
        gson.fromJson(jsonData, scheduleType)
    } else {
        // Korišćenje predefinisanih podataka ako JSON nije obezbeđen
        listOf(
            Schedule("Math", "101", 2024, "A", "Monday", "08:00", "10:00"),
            Schedule("Science", "102", 2024, "B", "Tuesday", "10:00", "12:00"),
            Schedule("History", "103", 2024, "A", "Wednesday", "13:00", "15:00")
        )
    }

    // Pretvara listu u Map<String, List<String>> gde je svaki ključ naziv kolone
    return mapOf(
        "subject" to schedules.map { it.subject },
        "classroom" to schedules.map { it.classroom },
        "year" to schedules.map { it.year.toString() },
        "group" to schedules.map { it.group },
        "day" to schedules.map { it.day },
        "time_from" to schedules.map { it.time_from },
        "time_to" to schedules.map { it.time_to }
    )
}
/**
 * Calculates operations for columns, and adds the new column to the data.
 * @param input String with the desired operations.
 * @param data Map with the data.
 */
fun addColumnData(input: String, data: MutableMap<String, List<String>>) {
    var column = "year" // Podrazumevana kolona za kalkulacije
    var columnData = data[column]?.mapNotNull { it.toDoubleOrNull() }!!.toMutableList()
    var column2 = "year"
    var columnData2 = data[column]?.mapNotNull { it.toDoubleOrNull() }!!.toMutableList()
    var inputs = input.split(",") //splitujemo input ("1 + 2", "2 - 1"), znaci odvojeni ce biti
    for (x in inputs) {
        var cmd = x.split(" ") // "1" "+" "2" cmd1 se referise na znak operacije
        column = data.keys.toList().get(cmd[0].toInt())
        columnData = data[column]?.mapNotNull { it.toDoubleOrNull() }!!.toMutableList() //Extraktujemo i konvertujemo podatak
        column2 = data.keys.toList().get(cmd[2].toInt())
        columnData2 = data[column2]?.mapNotNull { it.toDoubleOrNull() }!!.toMutableList()

        when (cmd[1]) {
            "+" -> {
                var i = 0
                while(i < columnData.size){
                    columnData[i] = (columnData[i].toDouble() + columnData2[i].toDouble())
                    i++
                }

                val resultList = columnData.map { y -> y.toString() }
                data["ADD($column + $column2)"] = resultList}
            "-" -> {
                var i = 0
                while(i < columnData.size){
                    columnData[i] = (columnData[i].toDouble() - columnData2[i].toDouble())
                    i++
                }

                val resultList = columnData.map { y -> y.toString() }

                data["SUB($column - $column2)"] = resultList
            }

            "*" -> {
                var i = 0
                while(i < columnData.size){
                    columnData[i] = (columnData[i].toDouble() * columnData2[i].toDouble())
                    i++
                }

                val resultList = columnData.map { x -> x.toString() }

                data["MUL($column * $column2)"] = resultList
            }
            "/" -> {
                var i = 0
                while(i < columnData.size){
                    columnData[i] = (columnData[i].toDouble() / columnData2[i].toDouble())
                    i++
                }

                val resultList = columnData.map { y -> y.toString() }

                data["DIV($column / $column2)"] = resultList
            }
            else -> println("Nepoznata operacija: $input")
        }
    }
}

/**
 * Does calculations on columns, and adds them to the data.
 * @param input String with the desired calculations.
 * @param data Map with the data.
 */
fun addCalculationToData(input: String, data: MutableMap<String, List<String>>) {
    var column = "year" // Podrazumevana kolona za kalkulacije
    var columnData = data[column]?.mapNotNull { it.toDoubleOrNull() }
    var inputs = input.split(",")
    for (x in inputs) {
        var cmd = x.split(" ")
        column = data.keys.toList().get(cmd[1].toInt())
        columnData = data[column]?.mapNotNull { it.toDoubleOrNull() }
        if(columnData == null) {
            continue
        }
        when (cmd[0].lowercase()) {
            "sum" -> data["SUM($column)"] = List(columnData.size) { columnData.sum().toString() }
            "avg" -> data["AVG($column)"] =
                List(columnData.size) { if (columnData.isNotEmpty()) columnData.average().toString() else "0.0" }

            "cnt" -> data["COUNT($column)"] = List(columnData.size) { columnData.size.toString() }
            else -> println("Nepoznata operacija: $input")
        }
    }
}