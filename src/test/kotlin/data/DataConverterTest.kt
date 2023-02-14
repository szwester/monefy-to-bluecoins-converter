package data

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DataConverterTest {

    private val dataConverter = DataConverter()

    private val categoryMapping = mapOf("Category1" to ("Parent1" to "SubCat1"))
    private val conversionRate = mapOf("2023-01-30" to "4.00")
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
        assertEquals("2023-01-30", bluecoinsData.date.toString())
        assertEquals("Category1", bluecoinsData.itemOrPayee)
        assertEquals(100.0, bluecoinsData.amount.toDouble())
        assertEquals("EUR", bluecoinsData.currency)
        assertEquals(1.00, bluecoinsData.conversionRate.toDouble()) // currency conversion should be ignored
        assertEquals("Parent1", bluecoinsData.parentCategory)
        assertEquals("SubCat1", bluecoinsData.category)
        assertEquals("Bank", bluecoinsData.accountType)
        assertEquals("Bank1", bluecoinsData.account)
        assertEquals("Some note", bluecoinsData.notes)
    }

    @Test
    fun monefyDataToBluecoinsDataTestWithMapping_without_currency_update_with_different_account_currency() {

        val monefyData = MonefyData("30/01/2023", "Account1", "Category1", "100", "EUR", "400", "PLN", "Some note")
        val bluecoinsData = dataConverter.monefyDataToBluecoinsData(
            monefyData,
            categoryMapping,
            conversionRate,
            accountMapping,
            emptyDescriptionMapping
        )

        assertEquals("i", bluecoinsData.type)
        assertEquals("2023-01-30", bluecoinsData.date.toString())
        assertEquals("Category1", bluecoinsData.itemOrPayee)
        assertEquals(100.0, bluecoinsData.amount.toDouble())
        assertEquals("EUR", bluecoinsData.currency)
        assertEquals(1.0, bluecoinsData.conversionRate.toDouble()) // currency conversion should be ignored
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
        assertEquals("2023-01-30", bluecoinsData.date.toString())
        assertEquals("Category1", bluecoinsData.itemOrPayee)
        assertEquals(100.0, bluecoinsData.amount.toDouble())
        assertEquals("Parent2", bluecoinsData.parentCategory)
        assertEquals("SubCat1", bluecoinsData.category)
        assertEquals("EUR", bluecoinsData.currency)
        assertEquals(1.00, bluecoinsData.conversionRate.toDouble())

        val bluecoinsString = bluecoinsData.toString()
        assertEquals("i,01/30/2023,Category1,100.0,EUR,1.0,Parent2,SubCat1,Bank,Bank1,Some note,,,", bluecoinsString)
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
        assertEquals("2023-01-30", bluecoinsData.date.toString())
        assertEquals("Category1", bluecoinsData.itemOrPayee)
        assertEquals(100.0, bluecoinsData.amount.toDouble())
        assertEquals("Parent2", bluecoinsData.parentCategory)
        assertEquals("SubCat1", bluecoinsData.category)
        assertEquals("EUR", bluecoinsData.currency)
        assertEquals(1.00, bluecoinsData.conversionRate.toDouble())

        val bluecoinsString = bluecoinsData.toString()
        assertEquals("e,01/30/2023,Category1,100.0,EUR,1.0,Parent2,SubCat1,Bank,Bank1,Some note,,,", bluecoinsString)
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
        assertEquals("2023-01-30", bluecoinsData.date.toString())
        assertEquals("Category1", bluecoinsData.itemOrPayee)
        assertEquals(25.0, bluecoinsData.amount.toDouble())
        assertEquals("PLN", bluecoinsData.currency)
        assertEquals(4.0, bluecoinsData.conversionRate.toDouble())

        val bluecoinsString = bluecoinsData.toString()
        assertEquals("i,01/30/2023,Category1,25.0,PLN,4.0,Parent1,SubCat1,Bank,Bank1,Some note,,,", bluecoinsString)
    }

    @Test
    fun monefyDataToBluecoinsDataTest_with_currency_exchange_from_previous_day() {

        val descriptionMapping: Map<String, Pair<String, String>> = mapOf()

        val monefyData = MonefyData("31/01/2023", "Account1", "Category1", "100", "PLN", "20", "EUR", "Some note")
        val bluecoinsData = dataConverter.monefyDataToBluecoinsData(
            monefyData,
            categoryMapping,
            conversionRate,
            accountMapping,
            descriptionMapping,
        )

        assertEquals("i", bluecoinsData.type)
        assertEquals("2023-01-31", bluecoinsData.date.toString())
        assertEquals("Category1", bluecoinsData.itemOrPayee)
        assertEquals(25.0, bluecoinsData.amount.toDouble())
        assertEquals("PLN", bluecoinsData.currency)
        assertEquals(4.00, bluecoinsData.conversionRate.toDouble())

        val bluecoinsString = bluecoinsData.toString()
        assertEquals("i,01/31/2023,Category1,25.0,PLN,4.0,Parent1,SubCat1,Bank,Bank1,Some note,,,", bluecoinsString)
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