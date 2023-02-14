package data

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class BluecoinsData(
    val type: String,
    val date: LocalDate,
    val itemOrPayee: String,
    val amount: String,
    val currency: String,
    val conversionRate: String,
    val parentCategory: String,
    val category: String,
    val accountType: String,
    val account: String,
    val notes: String,
) {
    override fun toString(): String {
        val bluecoinsFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        val outputDate = bluecoinsFormatter.format(date)
        return "$type,$outputDate,$itemOrPayee,$amount,$currency,$conversionRate,$parentCategory,$category,$accountType,$account,$notes,,,"
    }
}
