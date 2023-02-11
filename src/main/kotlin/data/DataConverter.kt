package data

import kotlin.math.abs

class DataConverter {

    fun monefyDataToBluecoinsData(
        monefyData: MonefyData,
        categoryMapping: Map<String, Pair<String, String>>,
        conversionRates: Map<String, Double>,
        accountToAccount: Map<String, Pair<String, String>>
    ): BluecoinsData {

        val date = convertDate(monefyData.date)

        val itemOrPayee = monefyData.category
        var amount = monefyData.amount.toDouble()
        val type = if (amount > 0) "i" else "e"

        amount = abs(amount)

        val currency = monefyData.currency

        val conversionRate = if (monefyData.currency != monefyData.convertedCurrency) getConversionRate(
            monefyData.date,
            conversionRates
        ) else 1.00

        val categories = getCategoryMapping(monefyData.category, categoryMapping)

        val parentCategory = categories.first
        val category = categories.second

        val accountMapping = getAccountMapping(monefyData.account, accountToAccount)
        var accountType = accountMapping.first
        var account = accountMapping.second
        val notes = monefyData.description
        val label = ""
        val status = ""
        val split = ""

        return BluecoinsData(
            type, date, itemOrPayee, amount.toString(), currency, conversionRate.toString(), parentCategory, category, accountType,
            account, notes, label, status, split
        )
    }

    private fun getAccountMapping(
        account: String,
        accountToAccount: Map<String, Pair<String, String>>
    ): Pair<String, String> {
        return accountToAccount[account]!!
    }

    private fun getConversionRate(date: String, conversionRates: Map<String, Double>): Double {
        return conversionRates[date]!!
    }

    private fun convertDate(monefyDate: String): String {
        val split = monefyDate.split("/")
        return split[1] + "/" + split[0] + "/" + split[2]
    }

    private fun getCategoryMapping(
        monefyCategory: String,
        mapping: Map<String, Pair<String, String>>
    ): Pair<String, String> {
        return mapping[monefyCategory]!!
    }

    fun getAllCategories(monefyData: List<MonefyData>): Set<String> {
        return monefyData.map { md -> md.category }.toSet()
    }

    fun getAllDescriptions(monefyData: List<MonefyData>): Set<String> {
        return monefyData.map { md -> md.description }.toSet()
    }
}