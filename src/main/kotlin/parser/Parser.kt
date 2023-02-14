package parser

import data.MonefyData
import org.json.JSONObject

class Parser {

    fun parseInputToMonefyData(path: String): List<MonefyData> {
        val input = getResourceAsText(path) ?: throw Exception("Cannot read input!")
        return parseTextInputToMonefyData(input)
    }

    fun parseTextInputToMonefyData(inputString: String): List<MonefyData> {
        return inputString
            .split("\n")
            .filter { line -> line.isNotBlank() }
            .drop(1)
            .map { row -> row.split(";") }
            .map { arr -> MonefyData(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], arr[7].trim()) }
    }

    fun readMappingToCategory(path: String): Map<String, Pair<String, String>> {
        return getResourceAsText(path)!!
            .split("\n")
            .filter { it.isNotEmpty() }
            .map { line -> line.split("->") }
            .associate { strings -> strings[0] to (strings[1] to strings[2]) }
    }

    /**
     * Parses the json file in the format returned from the API:
     * https://github.com/Formicka/exchangerate.host
     * @return String Date in a format used by Bluecoins mapped to the Conversion Rate as a String
     */
    fun readConversionRates(path: String, currency: String): Map<String, String?> {
        val conversionRate = getResourceAsText(path)
        val jsonObj = JSONObject(conversionRate)
        val map = jsonObj.toMap()
        val rates = map["rates"] as Map<String, Map<String, String>>
        return rates.map { rate ->
            rate.key to rate.value[currency].toString()
        }.associate { it.first to it.second }
    }

    private fun getResourceAsText(path: String): String? =
        {}.javaClass.getResource(path)?.readText()
}