package parser

import data.MonefyData
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

class ParserTest {

    private val parser = Parser()

    @Test
    fun parseInputToMonefyData() {
        val input = "date;account;category;amount;currency;converted amount;currency;description\n" +
                "09/02/2023;Account1;Category1;100;EUR;400;PLN;Comment1\n" +
                "09/02/2023;Account2;Category2;-100;EUR;-400;PLN;Comment2\n" +
                "09/02/2023;Account2;Category3;-100;PLN;-100;PLN;Comment3"

        val expected1 = MonefyData("09/02/2023", "Account1", "Category1", "100", "EUR", "400", "PLN", "Comment1")
        val expected2 = MonefyData("09/02/2023", "Account2", "Category2", "-100", "EUR", "-400", "PLN", "Comment2")
        val expected3 = MonefyData("09/02/2023", "Account2", "Category3", "-100", "PLN", "-100", "PLN", "Comment3")

        val actualParsedData = parser.parseTextInputToMonefyData(input)
        assertContentEquals(listOf(expected1, expected2, expected3), actualParsedData)
    }
}
