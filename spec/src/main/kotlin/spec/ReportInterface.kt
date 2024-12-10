package spec

import model.addCalculationToData
import model.addColumnData
import model.prepareData
import java.io.InputStreamReader
import java.time.format.TextStyle

/**
 * An interface for generating formatted or non-formatted reports from a map of column data to different formats.
 *
 * Implementations of this interface should define how the report is formatted and saved.
 */
interface ReportInterface {

    abstract val implName: String
    /**
     * Generates a report based on the provided data and writes it to the specified destination.
     *
     * @param data A map where the key is the column name and the value is a list of strings representing the column data.
     *             All lists in the map should have the same size to ensure proper row alignment.
     * @param destination The file path where the report will be saved.
     * @param header Indicates if header is provided in data
     * @param title An optional title for the report, used only in the formatted reports.
     * @param summary An optional summary for the report, used only in the formatted reports.
     *
     */
    fun generateReport(data: Map<String, List<String>>, destination: String, header: Boolean, title: String? = null, summary: String? = null)

    /**
     * Adding a header
     * @param columns List with string names of columns.
     */
    fun addHeader(columns: List<String>)

    /**
     * Adding a title
     * @param title String that holds title.
     */
    fun addTitle(title: String?)

    /**
     * Adding a summary
     * @param summaryData Map with pairs of string keys and values of any type for storing summary data
     */
    fun addSummary(summaryData: Map<String, Any>)

    /**
     * Setting title style
     * @param style Containing style for the title
     */
    fun setTitleStyle(style: TextStyle)

    /**
     * Setting summary style
     * @param style Containing style for the summary
     */
    fun setSummaryStyle(style: TextStyle)

    /**
     * Setting header style
     * @param style Containing style for the header
     */
    fun setHeaderStyle(style: TextStyle)

    /**
     * Setting table line thickness
     * @param thickness Integer value for line thickness
     */
    fun setTableLineThickness(thickness: Int)

    /**
     * Setting table border color
     * @param color String containing the color for table border
     */
    fun setTableBorderColor(color: String?)

    /**
     * Calls prepareData to get data from json or default data. Calls addCalculationsToData and addColumnData to add
     * additional data to the table. And finally, calls generateReport for the given implementation.
     * @param jsonData InputStreamReader which takes the string path of a json file for data input. If null then use default data
     * @param destination The file path where the report will be saved.
     * @param header Indicates if header is provided in data
     * @param title An optional title for the report, used only in the formatted reports.
     * @param summary An optional summary for the report, used only in the formatted reports.
     * @param calculations String format for calculations on columns. Example : sum 1,avg 2,cnt 3,...
     * @param addColumn String format for operation on columns. Example : 1 + 2,2 - 3,3 * 4,4 / 5
     */
    fun generateReport(jsonData: InputStreamReader? = null, destination: String, header: Boolean, title: String? = null, summary: String? = null, calculations: String? = null, addColumn: String? = null){
        var preparedData: Map<String, List<String>> = prepareData(jsonData)
        preparedData = preparedData.toMutableMap() //mutable da bi mogli da editujemo i cackamo i jer ce ona na licu mesta uvatiti promenu
        if (calculations != null && calculations.isNotEmpty())
            addCalculationToData(calculations, preparedData)
        if(addColumn != null && addColumn.isNotEmpty())
            addColumnData(addColumn, preparedData)
        generateReport(preparedData, destination, header, title, summary)
    }
}