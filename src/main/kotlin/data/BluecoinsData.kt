package data

data class BluecoinsData(
    val type: String,
    val date: String,
    val itemOrPayee: String,
    val amount: String,
    val currency: String,
    val conversionRate: String,
    val parentCategory: String,
    val category: String,
    val accountType: String,
    val account: String,
    val notes: String,
    val label: String,
    val status: String,
    val split: String
) {
    override fun toString(): String {
        return "$type,$date,$itemOrPayee,$amount,$currency,$conversionRate,$parentCategory,$category,$accountType,$account,$notes,$label,$status,$split"
    }
}
