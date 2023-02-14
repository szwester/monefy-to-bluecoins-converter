# Monefy to Bluecoins data converter

The program converts **Monefy** data to **Bluecoins** with the **Bluecoins** account currency set to EUR (might be changed in the code).\
Can handle Monefy transactions in any currency.

Monefy transaction looks like this:

```date;account;category;amount;currency;converted amount;currency;description```

`amount` is the actual value of the transaction, and `currency` the original currency of this transaction.
The `converted amount` and the second `currency` are ignored by the converter and their values in Monefy transaction depend on the set 
exchange rate and the currency set as the default Monefy account currency.

* For transactions in EUR, the converter will take the amount and currency and set the exchange rate to 1.00 - because  this is
the default currency set in Bluecoins.
* For transactions in a currency other than EUR, the converter will convert the value of such a transaction into EUR at the exchange rate
from the mapping (see below) and set the same currency as in the Monefy transaction.

Although in the Bluecoins application it is possible to add a transaction in a currency other than the default one set for the account
and specify the exchange rate, unfortunately the import is only possible in the default currency - hence the transaction value must always
be converted to the currency set on the account - in this case EUR.

Mapping can only handle 1 additional currency.

### Export data from Monefy
Data from Monefy should be exported:
`Settings -> Export to file`
with the following options:

* **Character set**: UTF-8
* **Decimal separator**: Decimal point ','
* **Delimiter character**: Semicolon ';'


### Mapping
In order for the data to be converted, it is necessary to set:

#### Mapping of Monefy category to Parent Category and Sub Category in Bluecoins:
This mapping should be in file `resource/mapping/category-to-category.txt`

It must contain mapping of all Monefy categories in the following format: `MonefyCategory->BluecoinsParentCategory->BluecoinsSubCategory`

To get a list of all Monefy categories that need to be mapped, one can call the `DataConverter::getAllCategories` method.

File `resources/categories.txt` contains sample categories (Parent Category and Subcategory) that can be used when setting up mapping. 

#### Currency exchange - for transactions in currencies other than EUR

An example of a transaction with a currency other than EUR:

```09/02/2023;Account;Category;-25;EUR;-100;PLN;Comment```

The program will convert it to a transaction with the value of `100.00` and the `PLN` currency and the Exchange Rate `0.210438` - because 
that exchange rate was in effect on the day of the transaction.

To provide current rates, add them to the `resource/currencyrate/exchangeRate.json` file in the same form as current data.
Data was obtained using api [Frankfurter](https://github.com/hakanensari/frankfurter).

#### Account mapping
To map the monefy account to the new Bluecoins account, use the `resources/mapping/account-to-account.txt` file in the form:

`MonefyAccount->Account Type->Account`.

#### Additional Description mapping:
Since Monefy categories are more limited than Bluecoins categories, one can additionally map Description from Monefy to Bluecoins categories.
This mapping looks the same as for Category mapping and is in the file: `resources/mapping/description-to-category.txt`.

Description based mapping (optional) is checked for each transaction and if found it is used, otherwise Category based mapping is used.

To display all existing descriptions, one can call the `DataConverter::getAllDescriptions` method on the loaded Monefy data.

### Import to Bluecoins
After setting the mapping, the program should convert and display on the console all transactions that can be pasted into a CSV file
(with the original header) and imported with the `Advanced Template` option.