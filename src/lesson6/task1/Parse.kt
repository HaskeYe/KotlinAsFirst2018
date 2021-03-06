@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson6.task1

import lesson2.task2.daysInMonth
import java.lang.IllegalArgumentException
import java.lang.Math.pow
import java.lang.NumberFormatException
import java.lang.StringBuilder
import kotlin.math.abs
import kotlin.math.floor

/**
 * Пример
 *
 * Время представлено строкой вида "11:34:45", содержащей часы, минуты и секунды, разделённые двоеточием.
 * Разобрать эту строку и рассчитать количество секунд, прошедшее с начала дня.
 */
fun timeStrToSeconds(str: String): Int {
    val parts = str.split(":")
    var result = 0
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
    }
    return result
}

/**
 * Пример
 *
 * Дано число n от 0 до 99.
 * Вернуть его же в виде двухсимвольной строки, от "00" до "99"
 */
fun twoDigitStr(n: Int) = if (n in 0..9) "0$n" else "$n"

/**
 * Пример
 *
 * Дано seconds -- время в секундах, прошедшее с начала дня.
 * Вернуть текущее время в виде строки в формате "ЧЧ:ММ:СС".
 */
fun timeSecondsToStr(seconds: Int): String {
    val hour = seconds / 3600
    val minute = (seconds % 3600) / 60
    val second = seconds % 60
    return String.format("%02d:%02d:%02d", hour, minute, second)
}

/**
 * Пример: консольный ввод
 */
fun main(args: Array<String>) {
    println("Введите время в формате ЧЧ:ММ:СС")
    val line = readLine()
    if (line != null) {
        val seconds = timeStrToSeconds(line)
        if (seconds == -1) {
            println("Введённая строка $line не соответствует формату ЧЧ:ММ:СС")
        } else {
            println("Прошло секунд с начала суток: $seconds")
        }
    } else {
        println("Достигнут <конец файла> в процессе чтения строки. Программа прервана")
    }
}

fun dateTest(date: List<String>, month: Int): Boolean {
    val year = date.last().toIntOrNull()
    val day = date.first().toIntOrNull()
    return ((day == null || year == null) || ((day < 1)
            || (year < 0)) || (daysInMonth(month, year) < day)
            || (date[0].contains(Regex("""^0-9""")))
            || (date[2].contains(Regex("""^0-9"""))))

}

/**
 * Средняя
 *
 * Дата представлена строкой вида "15 июля 2016".
 * Перевести её в цифровой формат "15.07.2016".
 * День и месяц всегда представлять двумя цифрами, например: 03.04.2011.
 * При неверном формате входной строки вернуть пустую строку.
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30.02.2009) считается неверными
 * входными данными.
 */
fun dateStrToDigit(str: String): String {
    val date = str.split(" ")
    if (date.size != 3) return ""
    var month = -1
    val list = listOf("января", "февраля", "марта",
            "апреля", "мая", "июня", "июля",
            "августа", "сентября", "октября",
            "ноября", "декабря")
    if (list.contains(date[1]))
        month = list.indexOf(date[1]) + 1
    if (month < 1) return ""
    if (dateTest(date, month)) return ""
    val year = date.last().toInt()
    val day = date.first().toInt()
    return String.format("%02d.%02d.%d", day,
            month, year)
}


/**
 * Средняя
 *
 * Дата представлена строкой вида "15.07.2016".
 * Перевести её в строковый формат вида "15 июля 2016".
 * При неверном формате входной строки вернуть пустую строку
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30 февраля 2009) считается неверными
 * входными данными.
 */
fun dateDigitToStr(digital: String): String {
    val date = digital.split(".")
    if (date.size != 3) return ""
    val month = date[1].toIntOrNull()
    val list = listOf("января", "февраля", "марта",
            "апреля", "мая", "июня", "июля",
            "августа", "сентября", "октября",
            "ноября", "декабря")
    if (month == null)
        return ""
    if (dateTest(date, date[1].toInt())) return ""
    if (date[1].toInt() !in 1..12) return ""
    val year = date.last().toInt()
    val day = date.first().toInt()
    val monthNew = list[date[1].toInt() - 1]
    return String.format("%d %s %d", day,
            monthNew, year)
}

/**
 * Средняя
 *
 * Номер телефона задан строкой вида "+7 (921) 123-45-67".
 * Префикс (+7) может отсутствовать, код города (в скобках) также может отсутствовать.
 * Может присутствовать неограниченное количество пробелов и чёрточек,
 * например, номер 12 --  34- 5 -- 67 -98 тоже следует считать легальным.
 * Перевести номер в формат без скобок, пробелов и чёрточек (но с +), например,
 * "+79211234567" или "123456789" для приведённых примеров.
 * Все символы в номере, кроме цифр, пробелов и +-(), считать недопустимыми.
 * При неверном формате вернуть пустую строку
 */
fun flattenPhoneNumber(phone: String): String {
    if (phone == "") return ""
    val s = StringBuilder()
    var n = true
    var b = true
    if (phone.contains(Regex("""\+.*\+"""))) return ""
    var firstCounter = 0
    var secondCounter = 0
    if (phone[0] == '+' && phone.length == 1) return ""
    if ((phone[0] == '+') && ((phone[1].toInt() < '0'.toInt())
                    || (phone[1].toInt() > '9'.toInt()))) return ""
    for (symbol in phone) {
        when (symbol) {
            '+' -> if (n) {
                s.append("+")
                n = false
            }
            '-' -> {
            }
            '(' -> {
                firstCounter++
                if (firstCounter > 1) return ""
            }
            ')' -> {
                if (firstCounter == 0) return ""
                secondCounter++
                if (secondCounter > 1) return ""
            }
            in '1'..'9' -> s.append(symbol)
            ' ' -> {
            }
            else -> b = false
        }
        if (!b) break
    }
    return if ((b) && (secondCounter == firstCounter)) s.toString() else ""
}

/**
 * Средняя
 *
 * Результаты спортсмена на соревнованиях в прыжках в длину представлены строкой вида
 * "706 - % 717 % 703".
 * В строке могут присутствовать числа, черточки - и знаки процента %, разделённые пробелами;
 * число соответствует удачному прыжку, - пропущенной попытке, % заступу.
 * Прочитать строку и вернуть максимальное присутствующее в ней число (717 в примере).
 * При нарушении формата входной строки или при отсутствии в ней чисел, вернуть -1.
 */
fun bestLongJump(jumps: String): Int {
    val s = jumps.split(Regex("""\s+"""))
    var max = -1
    for (result in s) {
        if ((result != "-") && (result != "%")) {
            if (result.toIntOrNull() == null) return -1
            if (result.toInt() > max)
                max = result.toInt()
        }
    }
    return max
}

/**
 * Сложная
 *
 * Результаты спортсмена на соревнованиях в прыжках в высоту представлены строкой вида
 * "220 + 224 %+ 228 %- 230 + 232 %%- 234 %".
 * Здесь + соответствует удачной попытке, % неудачной, - пропущенной.
 * Высота и соответствующие ей попытки разделяются пробелом.
 * Прочитать строку и вернуть максимальную взятую высоту (230 в примере).
 * При нарушении формата входной строки вернуть -1.
 */
fun bestHighJump(jumps: String): Int {
    val s = jumps.split(" ")
    var max = -1
    if (s.size % 2 != 0) return -1
    for (i in 0 until s.size step 2) {
        if (s[i].toIntOrNull() == null)
            return -1
        if (!s[i + 1].contains(Regex("""[^+\-%]"""))) {
            if ((s[i + 1].contains('+')) &&
                    (s[i].toInt() > max))
                max = s[i].toInt()
        } else {
            max = -1
            break
        }
    }
    return max
}

/**
 * Сложная
 *
 * В строке представлено выражение вида "2 + 31 - 40 + 13",
 * использующее целые положительные числа, плюсы и минусы, разделённые пробелами.
 * Наличие двух знаков подряд "13 + + 10" или двух чисел подряд "1 2" не допускается.
 * Вернуть значение выражения (6 для примера).
 * Про нарушении формата входной строки бросить исключение IllegalArgumentException
 */
//Works with first element = +4/-4
fun plusMinusOther(expression: String): Int {
    val s = expression.split(" ")
    var summ = 0
    val j: Int
    when {
        s[0] == "-" -> {
            try {
                summ -= s[1].toInt()
                j = 1
            } catch (e: IllegalArgumentException) {
                throw e
            }
        }
        s[0] == "+" -> {
            try {
                summ += s[1].toInt()
                j = 1
            } catch (e: IllegalArgumentException) {
                throw e
            }
        }
        else -> {
            if (s[0].toInt().toString() == s[0])
                try {
                    summ += s[0].toInt()
                    j = 0
                } catch (e: IllegalArgumentException) {
                    throw e
                }
            else
                throw IllegalArgumentException()
        }
    }
    if (s.size >= 3)
        for (i in j + 1 until s.size step 2)
            when {
                s[i] == "-" -> {
                    if ((s[i + 1].toInt().toString() != s[i + 1]) ||
                            (s[i + 1].toInt() < 0))
                        throw IllegalArgumentException()
                    summ -= s[i + 1].toInt()

                }
                s[i] == "+" -> {
                    if ((s[i + 1].toInt().toString() != s[i + 1]) ||
                            (s[i + 1].toInt() < 0))
                        throw IllegalArgumentException()
                    summ += s[i + 1].toInt()
                }
                else -> throw IllegalArgumentException()
            }
    return summ
}


fun plusMinusHelper(n: String): Boolean =
        (n.toIntOrNull().toString() != n) ||
                (n.toInt() < 0)


fun plusMinus(expression: String): Int {
    val s = expression.split(" ")
    if (s.size % 2 != 1) throw IllegalArgumentException()
    var summ = 0
    val j: Int
    if ((!expression.contains(Regex("""[0-9]""")))
            || (expression.contains(Regex("""-[0-9]|[0-9]-|[0-9]\+|\+[0-9]""")))
            || (expression.contains(Regex("""[^\d-+ ]""")))
            || (expression == ""))
        throw IllegalArgumentException()
    when {
        s[0] == "-" ->
            throw IllegalArgumentException()
        s[0] == "+" ->
            throw IllegalArgumentException()
        else -> {
            if (plusMinusHelper(s[0]))
                throw IllegalArgumentException() else
                summ += s[0].toInt()
            j = 0
        }
    }
    if (s.size >= 3)
        for (i in j + 1 until s.size step 2) {
            when {
                s[i] == "-" -> {
                    if (plusMinusHelper(s[i + 1]))
                        throw IllegalArgumentException()
                    summ -= s[i + 1].toInt()
                }
                s[i] == "+" -> {
                    if (plusMinusHelper(s[i + 1]))
                        throw IllegalArgumentException()
                    summ += s[i + 1].toInt()
                }
                else ->
                    throw IllegalArgumentException()
            }
        }
    return summ
}

/**
 * Сложная
 *
 * Строка состоит из набора слов, отделённых друг от друга одним пробелом.
 * Определить, имеются ли в строке повторяющиеся слова, идущие друг за другом.
 * Слова, отличающиеся только регистром, считать совпадающими.
 * Вернуть индекс начала первого повторяющегося слова, или -1, если повторов нет.
 * Пример: "Он пошёл в в школу" => результат 9 (индекс первого 'в')
 */

/*fun generalLeter(string: String): String {
    val s = StringBuilder()
    val first = string.first()
    if (first.isLowerCase()) s.append(first.toUpperCase())
    else s.append(first.toLowerCase())
    if (string.length > 1)
        s.append(string, 1, string.length)
    return s.toString()
}*/

fun firstDuplicateIndex(str: String): Int {
    val s = str.split(" ")
    var number = 0
    for (i in 0 until (s.size - 1)) {
        val word = s[i].toLowerCase()
        if (word == s[i + 1].toLowerCase())
            return number else
            number += word.length + 1
    }
    return -1
}

/**
 * Сложная
 *
 * Строка содержит названия товаров и цены на них в формате вида
 * "Хлеб 39.9; Молоко 62; Курица 184.0; Конфеты 89.9".
 * То есть, название товара отделено от цены пробелом,
 * а цена отделена от названия следующего товара точкой с запятой и пробелом.
 * Вернуть название самого дорогого товара в списке (в примере это Курица),
 * или пустую строку при нарушении формата строки.
 * Все цены должны быть больше либо равны нуля.
 */
fun mostExpensive(description: String): String {
    var maxVal = -1.0
    var goodH = ""
    val s = description.split("; ")
    for (good in s) {
        val n = good.split(" ")
        if (n.size != 2) return ""
        val price = n[1].split(".")
        if (price.size > 2)
            return ""
        if (price.size == 2) {
            if ((price[0].contains(Regex("""[^0-9]""")))
                    || (price[1].contains(Regex("""[^0-9]"""))))
                return ""
            if ((price[0].toDouble() + price[1].toDouble() /
                            pow(10.0, price[1].length.toDouble())) > maxVal) {
                goodH = n[0]
                maxVal = (price[0].toInt() + price[1].toDouble() / 10)
            }
        } else {
            if (price[0].toIntOrNull() == null) return ""
            if (price[0].toInt() > maxVal) {
                goodH = n[0]
                maxVal = price[0].toDouble()
            }
        }
    }
    return goodH
}

/**
 * Сложная
 *
 * Перевести число roman, заданное в римской системе счисления,
 * в десятичную систему и вернуть как результат.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: XXIII = 23, XLIV = 44, C = 100
 *
 * Вернуть -1, если roman не является корректным римским числом
 */


fun fromRoman(roman: String): Int {
    var number = 0
    var b = false
    var hop = 0
    if (roman.contains(Regex("""III(I)+|IV(IV)+|V(V)+|IX(IX)+|XXX(X)+
|XL(XL)+|L(L)+|XC(XC)+|CCC(C)+|CD(CD)+|D(D)+|CM(CM)+|IV(I)+|IX(I)+
|XL(L)+|XC(X)+|CD(C)+|CM(C)+"""))) return -1
    val letterMap = mapOf(0 to Pair(1, "I"), 1 to Pair(4, "IV"),
            2 to Pair(5, "V"), 3 to Pair(9, "IX"), 4 to Pair(10, "X"),
            5 to Pair(40, "XL"), 6 to Pair(50, "L"), 7 to Pair(90, "XC"),
            8 to Pair(100, "C"), 9 to Pair(400, "CD"), 10 to Pair(500, "D"),
            11 to Pair(900, "CM"), 12 to Pair(1000, "M"))
    var i = 1
    for (g in 0 until roman.length) {
        b = false
        for (j in i..letterMap.size) {
            if (roman[g].toString() == letterMap[letterMap.size - j]!!.second) {
                number += letterMap[letterMap.size - j]!!.first
                i = j
                b = true
            }
            if (((g + 1) <= roman.length - 1) && !b) {
                val n = (roman[g].toString() + roman[g + 1].toString())
                if (n == letterMap[letterMap.size - j]!!.second) {
                    number += letterMap[letterMap.size - j]!!.first
                    i = j
                    b = true
                    hop = 2
                }
            }
            if (hop >= 1) {
                b = true
                hop -= 1
            }
            if (b) break
        }
        if (!b) break
    }
    return if (b) number else -1
}

/**
 * Очень сложная
 *
 * Имеется специальное устройство, представляющее собой
 * конвейер из cells ячеек (нумеруются от 0 до cells - 1 слева направо) и датчик, двигающийся над этим конвейером.
 * Строка commands содержит последовательность команд, выполняемых данным устройством, например +>+>+>+>+
 * Каждая команда кодируется одним специальным символом:
 *	> - сдвиг датчика вправо на 1 ячейку;
 *  < - сдвиг датчика влево на 1 ячейку;
 *	+ - увеличение значения в ячейке под датчиком на 1 ед.;
 *	- - уменьшение значения в ячейке под датчиком на 1 ед.;
 *	[ - если значение под датчиком равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей следующей командой ']' (с учётом вложенности);
 *	] - если значение под датчиком не равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей предыдущей командой '[' (с учётом вложенности);
 *      (комбинация [] имитирует цикл)
 *  пробел - пустая команда
 *
 * Изначально все ячейки заполнены значением 0 и датчик стоит на ячейке с номером N/2 (округлять вниз)
 *
 * После выполнения limit команд или всех команд из commands следует прекратить выполнение последовательности команд.
 * Учитываются все команды, в том числе несостоявшиеся переходы ("[" при значении под датчиком не равном 0 и "]" при
 * значении под датчиком равном 0) и пробелы.
 *
 * Вернуть список размера cells, содержащий элементы ячеек устройства после завершения выполнения последовательности.
 * Например, для 10 ячеек и командной строки +>+>+>+>+ результат должен быть 0,0,0,0,0,1,1,1,1,1
 *
 * Все прочие символы следует считать ошибочными и формировать исключение IllegalArgumentException.
 * То же исключение формируется, если у символов [ ] не оказывается пары.
 * Выход за границу конвейера также следует считать ошибкой и формировать исключение IllegalStateException.
 * Считать, что ошибочные символы и непарные скобки являются более приоритетной ошибкой чем выход за границу ленты,
 * то есть если в программе присутствует некорректный символ или непарная скобка, то должно быть выброшено
 * IllegalArgumentException.
 * IllegalArgumentException должен бросаться даже если ошибочная команда не была достигнута в ходе выполнения.
 *
 */
fun computeDeviceCells(cells: Int, commands: String, limit: Int): List<Int> {
    val list = mutableListOf<Int>()
    val loopList = mutableListOf<Int>()
    var hop = 0
    var currCom = 0
    var passCounter = 0
    var cell = floor(cells.toDouble() / 2).toInt()
    if (commands.contains(Regex("""[^><\-+\[\] ]""")))
    /*try {
        var e = "+".toInt()
    } catch (s: IllegalArgumentException) {
    */
        throw IllegalArgumentException("Неверный формат входной строки")
    //}


    // Brand new verification
    if (commands.split('[').size != commands.split(']').size)
        throw  IllegalArgumentException()





    for (i in 0 until cells) list.add(i, 0)
    while ((hop < limit) && (currCom < commands.length)) {
        when (commands[currCom]) {
            ' ' -> {
                hop++
                currCom++
            }
            '>' -> when {
                (passCounter == 0) && (cell == list.size - 1) ->
                    throw IllegalStateException("Выход за пределы строки")
                (passCounter == 0) && (cell != list.size - 1) -> {
                    cell++
                    currCom++
                    hop++
                }
                else -> {
                    hop++
                    currCom++
                }
            }
            '<' -> when {
                (passCounter == 0) && (cell == 0) ->
                    throw IllegalStateException("Выход за пределы строки")
                (passCounter == 0) && (cell != 0) -> {
                    cell -= 1
                    currCom++
                    hop++
                }
                else -> {
                    hop++
                    currCom++
                }
            }
            '+' -> if (passCounter == 0) {
                list[cell]++
                hop++
                currCom++
            } else {
                hop++
                currCom++
            }
            '-' -> if (passCounter == 0) {
                list[cell] -= 1
                hop++
                currCom++
            } else {
                hop++
                currCom++
            }
            '[' -> if (list[cell] == 0) {
                passCounter++ //Need to pass next symbols
                hop++
                currCom++
            } else {
                loopList.add(currCom)
                hop++
                currCom++
            }
            ']' -> if (passCounter != 0) {
                passCounter -= 1
                hop++
                currCom++
            } else {
                if (list[cell] == 0) {
                    loopList.remove(loopList.last())
                    hop++
                    currCom++
                } else {
                    currCom = loopList.last() + 1
                    hop++
                }
            }
        }
    }
    if (passCounter == 0) return list else
        throw IllegalArgumentException("Неверный формат входной строки")

}
//Исполнитель каждый раз при значении под ']' равном не нулю будет возвращаться к элементу следующему после
// соответсвующей '['. Значение счётчика сделанных оперций увеличивается даже если мы проходим
// операции мимо, при нулевом значении '['.