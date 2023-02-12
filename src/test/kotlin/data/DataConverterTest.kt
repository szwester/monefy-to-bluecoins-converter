package data

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DataConverterTest {

    private val dataConverter = DataConverter()

    private val categoryMapping = mapOf("Category1" to ("Parent1" to "SubCat1"))
    private val conversionRate = mapOf("30/01/2023" to "0.25")
    private val emptyConversionRate = mapOf<String, String>()
    private val accountMapping = mapOf("Account1" to ("Bank" to "Bank1"))
    private val descriptionMapping = mapOf("Some note" to ("Parent2" to "SubCat1"))
    private val emptyDescriptionMapping = mapOf<String, Pair<String, String>>()

    @Test
    fun monefyDataToBluecoinsDataTestWithMapping_without_currency_update() {

        val monefyData = MonefyData("30/01/2023", "Account1", "Category1", "100", "EUR", "100", "EUR", "Some note")
        val bluecoinsData = dataConverter.monefyDataToBluecoinsData(
            monefyData,
            categoryMapping,
            conversionRate,
            accountMapping,
            emptyDescriptionMapping
        )

        assertEquals("i", bluecoinsData.type)
        assertEquals("01/30/2023", bluecoinsData.date)
        assertEquals("Category1", bluecoinsData.itemOrPayee)
        assertEquals("100.00", bluecoinsData.amount)
        assertEquals("EUR", bluecoinsData.currency)
        assertEquals("1.00", bluecoinsData.conversionRate) // currency conversion should be ignored
        assertEquals("Parent1", bluecoinsData.parentCategory)
        assertEquals("SubCat1", bluecoinsData.category)
        assertEquals("Bank", bluecoinsData.accountType)
        assertEquals("Bank1", bluecoinsData.account)
        assertEquals("Some note", bluecoinsData.notes)
    }

    @Test
    fun monefyDataToBluecoinsDataTest_with_description_mapping() {


        val monefyData = MonefyData("30/01/2023", "Account1", "Category1", "100", "EUR", "100", "EUR", "Some note")
        val bluecoinsData = dataConverter.monefyDataToBluecoinsData(
            monefyData,
            categoryMapping,
            conversionRate,
            accountMapping,
            descriptionMapping,
        )

        assertEquals("i", bluecoinsData.type)
        assertEquals("01/30/2023", bluecoinsData.date)
        assertEquals("Category1", bluecoinsData.itemOrPayee)
        assertEquals("100.00", bluecoinsData.amount)
        assertEquals("Parent2", bluecoinsData.parentCategory)
        assertEquals("SubCat1", bluecoinsData.category)
        assertEquals("EUR", bluecoinsData.currency)
        assertEquals("1.00", bluecoinsData.conversionRate)

        val bluecoinsString = bluecoinsData.toString()
        assertEquals("i,01/30/2023,Category1,100.00,EUR,1.00,Parent2,SubCat1,Bank,Bank1,Some note,,,", bluecoinsString)
    }

    @Test
    fun monefyDataToBluecoinsDataTest_expense() {

        val monefyData = MonefyData("30/01/2023", "Account1", "Category1", "-100", "EUR", "-100", "EUR", "Some note")
        val bluecoinsData = dataConverter.monefyDataToBluecoinsData(
            monefyData,
            categoryMapping,
            emptyConversionRate,
            accountMapping,
            descriptionMapping,
        )

        assertEquals("e", bluecoinsData.type)
        assertEquals("01/30/2023", bluecoinsData.date)
        assertEquals("Category1", bluecoinsData.itemOrPayee)
        assertEquals("100.00", bluecoinsData.amount)
        assertEquals("Parent2", bluecoinsData.parentCategory)
        assertEquals("SubCat1", bluecoinsData.category)
        assertEquals("EUR", bluecoinsData.currency)
        assertEquals("1.00", bluecoinsData.conversionRate)

        val bluecoinsString = bluecoinsData.toString()
        assertEquals("e,01/30/2023,Category1,100.00,EUR,1.00,Parent2,SubCat1,Bank,Bank1,Some note,,,", bluecoinsString)
    }

    @Test
    fun monefyDataToBluecoinsDataTest_with_currency_conversion_update() {

        val descriptionMapping: Map<String, Pair<String, String>> = mapOf()

        val monefyData = MonefyData("30/01/2023", "Account1", "Category1", "100", "PLN", "20", "EUR", "Some note")
        val bluecoinsData = dataConverter.monefyDataToBluecoinsData(
            monefyData,
            categoryMapping,
            conversionRate,
            accountMapping,
            descriptionMapping,
        )

        assertEquals("i", bluecoinsData.type)
        assertEquals("01/30/2023", bluecoinsData.date)
        assertEquals("Category1", bluecoinsData.itemOrPayee)
        assertEquals("100.00", bluecoinsData.amount)
        assertEquals("PLN", bluecoinsData.currency)
        assertEquals("0.25", bluecoinsData.conversionRate)

        val bluecoinsString = bluecoinsData.toString()
        assertEquals("i,01/30/2023,Category1,100.00,PLN,0.25,Parent1,SubCat1,Bank,Bank1,Some note,,,", bluecoinsString)
    }

    @Test
    fun convertAmountTest() {
        assertEquals("2.00", dataConverter.convertAmount("2"))
        assertEquals("2.20", dataConverter.convertAmount("2,2"))
        assertEquals("2.34", dataConverter.convertAmount("2,34"))
        assertEquals("-2.00", dataConverter.convertAmount("-2"))
        assertEquals("-2.20", dataConverter.convertAmount("-2,2"))
        assertEquals("-2.34", dataConverter.convertAmount("-2,34"))
        assertEquals("12.34", dataConverter.convertAmount("12,34"))
        assertEquals("123.40", dataConverter.convertAmount("123,4"))
        assertEquals("1234.56", dataConverter.convertAmount("1.234,56"))
        assertEquals("-123.40", dataConverter.convertAmount("-123,4"))
        assertEquals("-1234.56", dataConverter.convertAmount("-1.234,56"))
        assertEquals("-1000.00", dataConverter.convertAmount("-1.000"))
    }
}