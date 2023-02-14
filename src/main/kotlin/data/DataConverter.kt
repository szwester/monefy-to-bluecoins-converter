package data

import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class DataConverter {

    fun monefyDataToBluecoinsData(
        monefyData: MonefyData,
        categoryMapping: Map<String, Pair<String, String>>,
        conversionRates: Map<String, String?>,
        accountToAccount: Map<String, Pair<String, String>>,
        descriptionMapping: Map<String, Pair<String, String>>
    ): BluecoinsData {

        val date = convertDate(monefyData.date)

        val itemOrPayee = monefyData.category
        var amount = convertAmount(monefyData.amount)
        val type = if (amount.startsWith("-")) "e" else "i"

        // remove leading '-'
        amount = if (amount.startsWith("-")) amount.substring(1) else amount

        val conversionRate: Double
        var doubleAmount = amount.toDouble()
        if (monefyData.currency != "EUR") {
            // original currency is not EUR and should be converted
            var transactionDate = date
            while (!conversionRates.containsKey(transactionDate.toString())) {
                transactionDate = transactionDate.minusDays(1)
            }
            conversionRate = conversionRates[transactionDate.toString()]!!.toDouble()
            doubleAmount /= conversionRate
        } else {
            conversionRate = 1.00
        }
        val categories = descriptionMapping[monefyData.description] ?: getCategoryMapping(monefyData.category, categoryMapping)

        val (accountType, account) = getAccountMapping(monefyData.account, accountToAccount)
        val notes = monefyData.description

        return BluecoinsData(
            type,
            date,
            itemOrPayee,
            doubleAmount.toString(),
            monefyData.currency,
            conversionRate.toString(),
            categories.first,
            categories.second,
            accountType,
            account,
            notes,
        )
    }

    private fun getAccountMapping(
        account: String,
        accountToAccount: Map<String, Pair<String, String>>
    ): Pair<String, String> {
        if (!accountToAccount.containsKey(account)) {
            throw Exception("There is not mapping for account: $account")
        } else {
            return accountToAccount[account]!!
        }
    }

    fun convertAmount(originalAmount: String): String {
        val df = DecimalFormat("0.00")
        val replacedCharacters = originalAmount.replace(".", "").replace(",", ".")
        return df.format(replacedCharacters.toDouble())
    }

    private fun convertDate(monefyDate: String): LocalDate {
        val monefyFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return LocalDate.parse(monefyDate, monefyFormatter)
    }

    private fun getCategoryMapping(
        monefyCategory: String,
        mapping: Map<String, Pair<String, String>>
    ): Pair<String, String> {
        return mapping[monefyCategory]!!
    }

    /**
     * Returns all found categories in the Monefy data that should be mapped to Bluecoins categories
     */
    fun getAllCategories(monefyData: List<MonefyData>): Set<String> {
        return monefyData.map { md -> md.category }.toSet()
    }

    /**
     * Returns all found accounts in the Monefy data that should be mapped to Bluecoins accounts
     */
    fun getAccounts(monefyData: List<MonefyData>): Set<String> {
        return monefyData.map { md -> md.account }.toSet()
    }

    /**
     * Returns all found descriptions in the Monefy data that might be mapped to Bluecoins categories
     */
    fun getAllDescriptions(monefyData: List<MonefyData>): Set<String> {
        return monefyData.map { md -> md.description }.toSet()
    }
}