import data.DataConverter
import parser.Parser

fun main() {

    val parser = Parser()
    val monefyData = parser.parseInputToMonefyData("/Monefy.Data.Input.Test.csv")

    val accountMapping = parser.readMappingToCategory("/mapping/account-to-account.txt")
    val categoryMapping = parser.readMappingToCategory("/mapping/category-to-category.txt")
    val descriptionMapping = parser.readMappingToCategory("/mapping/description-to-category.txt")
    val conversionRates = parser.readConversionRates("/currencyrate/exchangeRate.json")

    val dataConverter = DataConverter()
    val bluecoinsData = monefyData.map { dataConverter.monefyDataToBluecoinsData(it, categoryMapping, conversionRates, accountMapping, descriptionMapping) }

    bluecoinsData.forEach { b -> println(b) }

}



