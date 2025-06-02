package core.utils

fun formatSumFromTiyin(amountInTiyin: Long): String {
    val integerPart = amountInTiyin / 100
    val fractionalPart = (amountInTiyin % 100).toInt()

    // Форматирование целой части с разделителем разрядов (пробел)
    val integerStr = integerPart
        .toString()
        .reversed()
        .chunked(3)
        .joinToString(" ")
        .reversed()

    // Форматирование дробной части (ровно 2 знака)
    val fractionalStr = fractionalPart.toString().padStart(2, '0')

    return "$integerStr.$fractionalStr"
}
