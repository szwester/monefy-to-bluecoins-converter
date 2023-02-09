package data

data class MonefyData(
    val date: String,
    val account: String,
    val category: String,
    val amount: String,
    val currency: String,
    val convertedAmount: String,
    val convertedCurrency: String,
    val description: String
)
