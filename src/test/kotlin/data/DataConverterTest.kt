package data

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class DataConverterTest {

    private val dataConverter = DataConverter()

    @Test
    fun monefyDataToBluecoinsDataTestWithMapping_without_currency_update() {

        val categoryMapping = mapOf("Category1" to ("Parent1" to "SubCat1"))
        val conversionRate = mapOf("30/01/2023" to 4.00)
        val accountMapping = mapOf("Account1" to ("Bank" to "Bank1"))

        val monefyData = MonefyData("30/01/2023", "Account1", "Category1", "100", "EUR", "100", "EUR", "Some note")
        val bluecoinsData = dataConverter.monefyDataToBluecoinsData(monefyData, categoryMapping, conversionRate, accountMapping)

        assertEquals("i", bluecoinsData.type)
        assertEquals("01/30/2023", bluecoinsData.date)
        assertEquals("Category1", bluecoinsData.itemOrPayee)
        assertEquals("100.0", bluecoinsData.amount)
        assertEquals("EUR", bluecoinsData.currency)
        assertEquals("1.0", bluecoinsData.conversionRate) // currency conversion should be ignored
        assertEquals("Parent1", bluecoinsData.parentCategory)
        assertEquals("SubCat1", bluecoinsData.category)
        assertEquals("Bank", bluecoinsData.accountType)
        assertEquals("Bank1", bluecoinsData.account)
        assertEquals("Some note", bluecoinsData.notes)
    }

    @Test
    fun monefyDataToBluecoinsDataTest_with_currency_conversion_update() {

        val categoryMapping = mapOf("Category1" to ("Parent1" to "SubCat1"))
        val conversionRate = mapOf("30/01/2023" to 0.25)
        val accountMapping = mapOf("Account1" to ("Bank" to "Bank1"))

        val monefyData = MonefyData("30/01/2023", "Account1", "Category1", "100", "PLN", "20", "EUR", "Some note")
        val bluecoinsData = dataConverter.monefyDataToBluecoinsData(monefyData, categoryMapping, conversionRate, accountMapping)

        assertEquals("i", bluecoinsData.type)
        assertEquals("01/30/2023", bluecoinsData.date)
        assertEquals("Category1", bluecoinsData.itemOrPayee)
        assertEquals("100.0", bluecoinsData.amount)
        assertEquals("PLN", bluecoinsData.currency)
        assertEquals("0.25", bluecoinsData.conversionRate)

        val bluecoinsString = bluecoinsData.toString()
        assertEquals("i,01/30/2023,Category1,100.0,PLN,0.25,Parent1,SubCat1,Bank,Bank1,Some note,,,", bluecoinsString)
    }

}