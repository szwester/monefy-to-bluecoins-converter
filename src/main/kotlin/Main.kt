import data.DataConverter
import parser.Parser

fun main() {

    val parser = Parser()
    val monefyData = parser.parseInputToMonefyData("/Monefy.Data.13-02-2023.csv")

    println("Number of Monefy transaction: ${monefyData.size}")

    val accountMapping = parser.readMappingToCategory("/mapping/account-to-account_my.txt")
    val categoryMapping = parser.readMappingToCategory("/mapping/category-to-category_my.txt")
    val descriptionMapping = parser.readMappingToCategory("/mapping/description-to-category_my.txt")
    val conversionRates = parser.readConversionRates("/currencyrate/exchangeRate.json", "PLN")


    val dataConverter = DataConverter()
    val bluecoinsData = monefyData.map { dataConverter.monefyDataToBluecoinsData(it, categoryMapping, conversionRates, accountMapping, descriptionMapping) }

    println("Number of Bluecoins transaction after conversion: ${bluecoinsData.size}")
    println("Bluecoins transaction:")
    bluecoinsData.forEach { b -> println(b) }
}



