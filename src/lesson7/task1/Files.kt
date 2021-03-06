@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson7.task1

import java.io.File
import java.lang.Math.pow
import kotlin.math.floor
import kotlin.math.min

/**
 * Пример
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Вывести его в выходной файл с именем outputName, выровняв по левому краю,
 * чтобы длина каждой строки не превосходила lineLength.
 * Слова в слишком длинных строках следует переносить на следующую строку.
 * Слишком короткие строки следует дополнять словами из следующей строки.
 * Пустые строки во входном файле обозначают конец абзаца,
 * их следует сохранить и в выходном файле
 */
fun alignFile(inputName: String, lineLength: Int, outputName: String) {
    val outputStream = File(outputName).bufferedWriter()
    var currentLineLength = 0
    for (line in File(inputName).readLines()) {
        if (line.isEmpty()) {
            outputStream.newLine()
            if (currentLineLength > 0) {
                outputStream.newLine()
                currentLineLength = 0
            }
            continue
        }
        for (word in line.split(" ")) {
            if (currentLineLength > 0) {
                if (word.length + currentLineLength >= lineLength) {
                    outputStream.newLine()
                    currentLineLength = 0
                } else {
                    outputStream.write(" ")
                    currentLineLength++
                }
            }
            outputStream.write(word)
            currentLineLength += word.length
        }
    }
    outputStream.close()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * На вход подаётся список строк substrings.
 * Вернуть ассоциативный массив с числом вхождений каждой из строк в текст.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 */
fun countSubstrings(inputName: String, substrings: List<String>): Map<String, Int> {
    val map = mutableMapOf<String, Int>()
    val input = File(inputName).readText()
    for (line in substrings) {
        map[line] = line.toRegex(RegexOption.IGNORE_CASE).findAll(input).toList().size
    }
    return map
}


/**
 * Средняя
 *
 * В русском языке, как правило, после букв Ж, Ч, Ш, Щ пишется И, А, У, а не Ы, Я, Ю.
 * Во входном файле с именем inputName содержится некоторый текст на русском языке.
 * Проверить текст во входном файле на соблюдение данного правила и вывести в выходной
 * файл outputName текст с исправленными ошибками.
 *
 * Регистр заменённых букв следует сохранять.
 *
 * Исключения (жюри, брошюра, парашют) в рамках данного задания обрабатывать не нужно
 *
 */
fun sibilants(inputName: String, outputName: String) {
    var input = File(inputName).readText()
    val symbols = listOf('ж', 'Ж', 'ч', 'Ч', 'ш', 'Ш', 'щ', 'Щ')
    val nextSymbols = listOf('ы', 'Ы', 'я', 'Я', 'ю', 'Ю')
    val correctSymbols = listOf('и', 'И', 'а', 'А', 'у', 'У')
    for (symbol in symbols)
        for (i in 0 until nextSymbols.size) {
            val wrong = (symbol.toString() + nextSymbols[i]).toRegex()
            val right = symbol.toString() + correctSymbols[i]
            input = input.replace(wrong, right)
        }
    File(outputName).writeText(input)
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по центру
 * относительно самой длинной строки.
 *
 * Выравнивание следует производить путём добавления пробелов в начало строки.
 *
 *
 * Следующие правила должны быть выполнены:
 * 1) Пробелы в начале и в конце всех строк не следует сохранять.
 * 2) В случае невозможности выравнивания строго по центру, строка должна быть сдвинута в ЛЕВУЮ сторону
 * 3) Пустые строки не являются особым случаем, их тоже следует выравнивать
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых)
 *
 */
fun centerFile(inputName: String, outputName: String) {
    val inputLines = File(inputName).readLines().map { it.trim() }
    var max = 0
    inputLines.forEach { it -> if (it.length > max) max = it.length }
    File(outputName).writeText(
            inputLines.joinToString("\n") {
                " ".repeat((max - it.length) / 2) + it
            }
    )
}


/**
 * Сложная
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по левому и правому краю относительно
 * самой длинной строки.
 * Выравнивание производить, вставляя дополнительные пробелы между словами: равномерно по всей строке
 *
 * Слова внутри строки отделяются друг от друга одним или более пробелом.
 *
 * Следующие правила должны быть выполнены:
 * 1) Каждая строка входного и выходного файла не должна начинаться или заканчиваться пробелом.
 * 2) Пустые строки или строки из пробелов трансформируются в пустые строки без пробелов.
 * 3) Строки из одного слова выводятся без пробелов.
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых).
 *
 * Равномерность определяется следующими формальными правилами:
 * 5) Число пробелов между каждыми двумя парами соседних слов не должно отличаться более, чем на 1.
 * 6) Число пробелов между более левой парой соседних слов должно быть больше или равно числу пробелов
 *    между более правой парой соседних слов.
 *
 * Следует учесть, что входной файл может содержать последовательности из нескольких пробелов  между слвоами. Такие
 * последовательности следует учитывать при выравнивании и при необходимости избавляться от лишних пробелов.
 * Из этого следуют следующие правила:
 * 7) В самой длинной строке каждая пара соседних слов должна быть отделена В ТОЧНОСТИ одним пробелом
 * 8) Если входной файл удовлетворяет требованиям 1-7, то он должен быть в точности идентичен выходному файлу
 */
fun alignFileByWidth(inputName: String, outputName: String) {
    TODO()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * Вернуть ассоциативный массив, содержащий 20 наиболее часто встречающихся слов с их количеством.
 * Если в тексте менее 20 различных слов, вернуть все слова.
 *
 * Словом считается непрерывная последовательность из букв (кириллических,
 * либо латинских, без знаков препинания и цифр).
 * Цифры, пробелы, знаки препинания считаются разделителями слов:
 * Привет, привет42, привет!!! -привет?!
 * ^ В этой строчке слово привет встречается 4 раза.
 *
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 * Ключи в ассоциативном массиве должны быть в нижнем регистре.
 *
 */
fun top20Words(inputName: String): Map<String, Int> {
    val map = mutableMapOf<String, Int>()
    val input = File(inputName).readText().toLowerCase()
    for (word in input.split(Regex("""[^a-zа-яё]+"""))) {
        if (map.containsKey(word.toLowerCase()))
            map[word.toLowerCase()] = map[word.toLowerCase()]!! + 1
        else map[word.toLowerCase()] = 1
    }
    val result = mutableMapOf<String, Int>()
    for (i in 1..min(20, map.size)) {
        var max = Pair("", 0)
        for ((word, entries) in map) if (entries > max.second)
            max = Pair(word, entries)
        result[max.first] = max.second
        map.remove(max.first)
    }
    return if ((result.size == 1) && result.containsKey(""))
        emptyMap() else
        if (result.containsKey("")) {
            result.remove("")
            return result
        } else return result
}

/**
 * Средняя
 *
 * Реализовать транслитерацию текста из входного файла в выходной файл посредством динамически задаваемых правил.

 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * В ассоциативном массиве dictionary содержится словарь, в котором некоторым символам
 * ставится в соответствие строчка из символов, например
 * mapOf('з' to "zz", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "yy", '!' to "!!!")
 *
 * Необходимо вывести в итоговый файл с именем outputName
 * содержимое текста с заменой всех символов из словаря на соответствующие им строки.
 *
 * При этом регистр символов в словаре должен игнорироваться,
 * но при выводе символ в верхнем регистре отображается в строку, начинающуюся с символа в верхнем регистре.
 *
 * Пример.
 * Входной текст: Здравствуй, мир!
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Пример 2.
 *
 * Входной текст: Здравствуй, мир!
 * Словарь: mapOf('з' to "zZ", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "YY", '!' to "!!!")
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */

fun generalLetter(string: String): String {
    val s = StringBuilder()
    val first = string.first()
    s.append(first.toUpperCase())
    if (string.length > 1)
        s.append(string, 1, string.length)
    return s.toString()
}

fun transliterate(inputName: String, dictionary: Map<Char, String>, outputName: String) {
    var text = File(inputName).readText()
    val newDictionary = mutableMapOf<Char, String>()
    for ((char, string) in dictionary)
        newDictionary[char.toLowerCase()] = string.toLowerCase()
    for ((char, string) in newDictionary) {
        text = text.replace(char.toString(), string)
        if (char.toUpperCase() != char) {
            val regUp = char.toUpperCase()
            val newReplace = generalLetter(string)
            text = text.replace(regUp.toString(), newReplace)
        }
    }
    File(outputName).writeText(text)
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName имеется словарь с одним словом в каждой строчке.
 * Выбрать из данного словаря наиболее длинное слово,
 * в котором все буквы разные, например: Неряшливость, Четырёхдюймовка.
 * Вывести его в выходной файл с именем outputName.
 * Если во входном файле имеется несколько слов с одинаковой длиной, в которых все буквы разные,
 * в выходной файл следует вывести их все через запятую.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 * Пример входного файла:
 * Карминовый
 * Боязливый
 * Некрасивый
 * Остроумный
 * БелогЛазый
 * ФиолетОвый

 * Соответствующий выходной файл:
 * Карминовый, Некрасивый
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun chooseLongestChaoticWord(inputName: String, outputName: String) {
    val words = File(inputName).readLines().map { it.trim() }
    var max = mutableListOf<String>()
    if (File(inputName).readText() == "")
        File(outputName).writeText("")
    for (word in words) {
        var set = emptySet<Char>()
        var b = true
        for (symbol in word)
            if (!set.contains(symbol.toLowerCase()))
                set += symbol.toLowerCase()
            else b = false
        if (b) when {
            max.size == 0 -> max.add(word)
            word.length > (max[0].length) -> {
                max = mutableListOf()
                max[0] = word
            }
            word.length == (max[0].length) -> {
                val newSet = max.map { it.toLowerCase() }
                if (!newSet.contains(word.toLowerCase()))
                    max.add(word)
            }
        }
    }
    val s = StringBuilder()
    for (word in max) s.append("$word, ")
    s.delete(s.length - 2, s.length)
    File(outputName).writeText(s.toString())
}

/**
 * Сложная
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе элементы текстовой разметки следующих типов:
 * - *текст в курсивном начертании* -- курсив
 * - **текст в полужирном начертании** -- полужирный
 * - ~~зачёркнутый текст~~ -- зачёркивание
 *
 * Следует вывести в выходной файл этот же текст в формате HTML:
 * - <i>текст в курсивном начертании</i>
 * - <b>текст в полужирном начертании</b>
 * - <s>зачёркнутый текст</s>
 *
 * Кроме того, все абзацы исходного текста, отделённые друг от друга пустыми строками, следует обернуть в теги <p>...</p>,
 * а весь текст целиком в теги <html><body>...</body></html>.
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 * Отдельно следует заметить, что открывающая последовательность из трёх звёздочек (***) должна трактоваться как "<b><i>"
 * и никак иначе.
 *
 * Пример входного файла:
Lorem ipsum *dolor sit amet*, consectetur **adipiscing** elit.
Vestibulum lobortis, ~~Est vehicula rutrum *suscipit*~~, ipsum ~~lib~~ero *placerat **tortor***,

Suspendisse ~~et elit in enim tempus iaculis~~.
 *
 * Соответствующий выходной файл:
<html>
<body>
<p>
Lorem ipsum <i>dolor sit amet</i>, consectetur <b>adipiscing</b> elit.
Vestibulum lobortis. <s>Est vehicula rutrum <i>suscipit</i></s>, ipsum <s>lib</s>ero <i>placerat <b>tortor</b></i>.
</p>
<p>
Suspendisse <s>et elit in enim tempus iaculis</s>.
</p>
</body>
</html>
 *
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */

fun htmlReplacer(string: String): String {
    val charCounter = mutableListOf(false, false, false, false)
    val s = StringBuilder()
    var extrareplacement = mutableListOf(0, 0)
    if (!string.contains(Regex("""[^\s]""")))
        return ("</p><p>")
    val controlList = mutableListOf(0, 0, 0, 0)
    if ((string.split("***").size - 1) % 2 == 0)
        controlList[0] = string.split("***").size - 1 else
        controlList[0] = string.split("***").size - 2
    if ((string.split(Regex("""[^*]\*\*[^*]""")).size - 1) % 2 == 0)
        controlList[1] = string.split(Regex("""[^*]\*\*[^*]""")).size - 1 else
        controlList[1] = string.split(Regex("""[^*]\*\*[^*]""")).size - 2
    if ((string.split(Regex("""[^*]\*[^*]""")).size - 1) % 2 == 0)
        controlList[2] = string.split(Regex("""[^*]\*[^*]""")).size - 1 else
        controlList[2] = string.split(Regex("""[^*]\*[^*]""")).size - 2
    if ((string.split("~~").size - 1) % 2 == 0)
        controlList[3] = string.split("~~").size - 1 else
        controlList[3] = string.split("~~").size - 2
    if ((string.split(Regex("""[^*]\*[^*]""")).size - 1) % 2 != 0
            && (string.split(Regex("""[^*]\*\*[^*]""")).size - 1) % 2 != 0
            && (string.split("***").size - 1) % 2 != 0) {
        extrareplacement = mutableListOf(2, 2)

    }
    var i = 0
    while (i < string.length)
        when {
            string[i] == '*'
                    && string[i + 1] == '*'
                    && string[i + 2] == '*'
                    && controlList[0] > 0 -> if (charCounter[0]) {
                s.append("</b></i>")
                charCounter[0] = false
                i += 3
                controlList[0] -= 1
            } else {
                s.append("<b><i>")
                charCounter[0] = true
                i += 3
                controlList[0] -= 1
            }
            string[i] == '*'
                    && string[i + 1] == '*'
                    && (controlList[1] > 0
                    || extrareplacement[0] > 0) -> if (charCounter[1]) {
                s.append("</b>")
                charCounter[1] = false
                i += 2
                controlList[1] -= 1
                if (controlList[1] == 0
                        && extrareplacement[0] > 0)
                    extrareplacement[0] -= 1
            } else {
                s.append("<b>")
                charCounter[1] = true
                i += 2
                controlList[1] -= 1
                if (controlList[1] == 0
                        && extrareplacement[0] > 0)
                    extrareplacement[0] -= 1
            }
            string[i] == '*'
                    && (controlList[2] > 0
                    || extrareplacement[1] > 0) -> if (charCounter[2]) {
                s.append("</i>")
                charCounter[2] = false
                i++
                controlList[2] -= 1
                if (controlList[2] == 0
                        && extrareplacement[1] > 0)
                    extrareplacement[1] -= 1
            } else {
                s.append("<i>")
                charCounter[2] = true
                i++
                controlList[2] -= 1
                if (controlList[2] == 0
                        && extrareplacement[1] > 0)
                    extrareplacement[1] -= 1
            }
            string[i] == '~'
                    && string[i + 1] == '~'
                    && controlList[3] > 0 -> if (charCounter[3]) {
                s.append("</s>")
                charCounter[3] = false
                i += 2
                controlList[3] -= 1
            } else {
                s.append("<s>")
                charCounter[3] = true
                i += 2
                controlList[3] -= 1
            }
            else -> {
                s.append(string[i])
                i++
            }
        }
    return s.toString()
}

fun markdownToHtmlSimple(inputName: String, outputName: String) {
    val textLines = File(inputName).readLines()
    val s = StringBuilder()
    s.append("<html><body><p>")
    for (line in textLines) s.append(htmlReplacer(line))
    s.append("</p></body></html>")
    File(outputName).writeText(s.toString())

}

/**
 * Сложная
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе набор вложенных друг в друга списков.
 * Списки бывают двух типов: нумерованные и ненумерованные.
 *
 * Каждый элемент ненумерованного списка начинается с новой строки и символа '*', каждый элемент нумерованного списка --
 * с новой строки, числа и точки. Каждый элемент вложенного списка начинается с отступа из пробелов, на 4 пробела большего,
 * чем список-родитель. Максимально глубина вложенности списков может достигать 6. "Верхние" списки файла начинются
 * прямо с начала строки.
 *
 * Следует вывести этот же текст в выходной файл в формате HTML:
 * Нумерованный список:
 * <ol>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ol>
 *
 * Ненумерованный список:
 * <ul>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ul>
 *
 * Кроме того, весь текст целиком следует обернуть в теги <html><body>...</body></html>
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 *
 * Пример входного файла:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
 * Утка по-пекински
 * Утка
 * Соус
 * Салат Оливье
1. Мясо
 * Или колбаса
2. Майонез
3. Картофель
4. Что-то там ещё
 * Помидоры
 * Фрукты
1. Бананы
23. Яблоки
1. Красные
2. Зелёные
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 *
 *
 * Соответствующий выходной файл:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
<html>
<body>
<ul>
<li>
Утка по-пекински
<ul>
<li>Утка</li>
<li>Соус</li>
</ul>
</li>
<li>
Салат Оливье
<ol>
<li>Мясо
<ul>
<li>
Или колбаса
</li>
</ul>
</li>
<li>Майонез</li>
<li>Картофель</li>
<li>Что-то там ещё</li>
</ol>
</li>
<li>Помидоры</li>
<li>
Яблоки
<ol>
<li>Красные</li>
<li>Зелёные</li>
</ol>
</li>
</ul>
</body>
</html>
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlLists(inputName: String, outputName: String) {
    TODO()
}

/**
 * Очень сложная
 *
 * Реализовать преобразования из двух предыдущих задач одновременно над одним и тем же файлом.
 * Следует помнить, что:
 * - Списки, отделённые друг от друга пустой строкой, являются разными и должны оказаться в разных параграфах выходного файла.
 *
 */
fun markdownToHtml(inputName: String, outputName: String) {
    TODO()
}

/**
 * Средняя
 *
 * Вывести в выходной файл процесс умножения столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 111):
19935
 *    111
--------
19935
+ 19935
+19935
--------
2212785
 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 * Нули в множителе обрабатывать так же, как и остальные цифры:
235
 *  10
-----
0
+235
-----
2350
 *
 */
fun printMultiplicationProcess(lhv: Int, rhv: Int, outputName: String) {
    val stringLength = (lhv * rhv).toString().length + 1
    val outputStream = File(outputName).bufferedWriter()
    outputStream.write(" ".repeat(stringLength - lhv.toString().length
    ) + lhv.toString())
    outputStream.newLine()
    outputStream.write('*'
            + " ".repeat(stringLength - rhv.toString().length - 1)
            + rhv.toString())
    outputStream.newLine()
    outputStream.write("-".repeat(stringLength))
    outputStream.newLine()
    outputStream.write(" ".repeat(stringLength - ((rhv % 10) * lhv).toString().length)
            + ((rhv % 10) * lhv).toString())
    outputStream.newLine()
    if (rhv.toString().length > 1) {
        for (i in 1 until rhv.toString().length) {
            val symbol = floor((rhv % pow(10.0, (i + 1).toDouble())) /
                    pow(10.0, i.toDouble())).toInt()
            val length = (symbol * lhv).toString().length
            outputStream.write("+" + " ".repeat(stringLength -
                    length - i - 1) + (symbol * lhv).toString() /*+ " ".repeat(i)*/)
            outputStream.newLine()
        }
    }
    outputStream.write("-".repeat(stringLength))
    outputStream.newLine()
    outputStream.write(" " + (lhv * rhv).toString())
    outputStream.close()
}


/**
 * Сложная
 *
 * Вывести в выходной файл процесс деления столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 22):
19935 | 22
-198     906
----
13
-0
--
135
-132
----
3

 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 *
 */

fun printHelper(a: Int, b: Int): Int = floor(a.toDouble() / b).toInt()

fun printDivisionProcess(lhv: Int, rhv: Int, outputName: String) {
    val stringLength = lhv.toString().length + 1
    val outputStream = File(outputName).bufferedWriter()
    var a = lhv


    //first string
    if (rhv > lhv) {
        //for other realization
        /* var zeroCounter = 0
        (rhv / lhv).toString().forEach { it -> if (it == '0') zeroCounter++ }
        a = lhv * pow(10.0, zeroCounter.toDouble()).toInt()*/
        outputStream.write("$lhv | $rhv\n-0   0\n--\n 1")
        outputStream.close()
    }
    outputStream.write(" "
            + a.toString() + " " + "|" + " " + rhv.toString())
    outputStream.newLine()


    //writing second string
    var firstDigits = 0
    for (i in 1..a.toString().length) {
        firstDigits *= 10
        firstDigits +=
                floor((a % pow(10.0, a.toString().length.toDouble() - i + 1)).toInt() /
                        pow(10.0, a.toString().length.toDouble() - i)).toInt()
        if (firstDigits >= rhv) break
    }
    val newNumber = printHelper(firstDigits, rhv)
    outputStream.write("-" + (newNumber * rhv).toString()
            + " ".repeat(stringLength -
            (newNumber * rhv).toString().length + 2)
            + floor(a.toDouble() / rhv).toInt().toString())
    outputStream.newLine()
    outputStream.write("-".repeat((newNumber * rhv).toString().length + 1))
    outputStream.newLine()
    //control
    var control = a.toString().length - firstDigits.toString().length


//next strings
    if (firstDigits != a) {
        // if we need to write more than 1 string
        var lastUpperNumber = firstDigits
        var lastLowerNumber = newNumber * rhv
        val newChar = mutableListOf<Int>()
        for (i in 1..(a.toString().length - firstDigits.toString().length))
            newChar.add(((a % pow(10.0, a.toString().length.toDouble() -
                    firstDigits.toString().length - i + 1)).toInt() /
                    pow(10.0, a.toString().length.toDouble()
                            - firstDigits.toString().length - i)).toInt())
        var j = 0
        for (i in 1..newChar.size) {
            var newUpperNumber = ((lastUpperNumber - lastLowerNumber) * 10
                    + newChar[j])
            j++
            control -= 1
            if (newUpperNumber < rhv) {
                while ((control > 0) && (newUpperNumber < rhv)) {
                    newUpperNumber = newUpperNumber * 10 + newChar[j]
                    j++
                    control -= 1
                }
            }
            if (newUpperNumber < rhv) {
                outputStream.write(" ".repeat(
                        firstDigits.toString().length + j + 1
                                - newUpperNumber.toString().length) +
                        newUpperNumber.toString())
                outputStream.close()
                break
            }
            //writing upper number (after the previous "------")
            outputStream.write(" ".repeat(firstDigits.toString().length + j
                    - newUpperNumber.toString().length) + newUpperNumber.toString())
            outputStream.newLine()
            //writing lower number (before the next "------")
            val n = printHelper(newUpperNumber, rhv)
            outputStream.write(" ".repeat(firstDigits.toString().length + j - 1
                    - (rhv * n).toString().length) + "-" + (rhv * n).toString())
            outputStream.newLine()
            //writing the next "--------"
            outputStream.write(" ".repeat(firstDigits.toString().length + j - 1
                    - (rhv * n).toString().length)
                    + "-".repeat((rhv * n).toString().length + 1))
            outputStream.newLine()
            lastUpperNumber = newUpperNumber
            lastLowerNumber = (rhv * n)
            if (control < 1) {
                outputStream.write(" ".repeat(
                        stringLength - (lastUpperNumber - lastLowerNumber).toString().length
                ) + (lastUpperNumber - lastLowerNumber))
                break
            }
        }
    } else {
        outputStream.write((a - (newNumber * rhv)).toString())
    }
    outputStream.close()
}

