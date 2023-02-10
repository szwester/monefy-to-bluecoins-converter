package parser

import data.MonefyData

class Parser {

    fun readInput(path: String) = getResourceAsText(path)

    fun parseInputToMonefyData(input: String): List<MonefyData> {
        return input
            .split("\n")
            .drop(1)
            .map { row -> row.split(";") }
            .map { arr -> MonefyData(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], arr[7]) }
    }

    fun getAllCategories(monefyData: List<MonefyData>): List<String> {
        return monefyData.map { md -> md.category }
    }

    fun getAllDescriptions(monefyData: List<MonefyData>): List<String> {
        return monefyData.map { md -> md.description }
    }

    fun readCategoryMapping(path: String): Map<String, Pair<String, String>> {
        return getResourceAsText(path)!!
            .split("\n")
            .map { line -> line.split("->") }
            .associate { strings -> strings[0] to (strings[1] to strings[2]) }
    }

    fun readAccountMapping(path: String): Map<String, Pair<String, String>> {
        return getResourceAsText(path)!!
            .split("\n")
            .map { line -> line.split("->") }
            .associate { strings -> strings[0] to (strings[1] to strings[2]) }
    }

    private fun getResourceAsText(path: String): String? =
        object {}.javaClass.getResource(path)?.readText()
}