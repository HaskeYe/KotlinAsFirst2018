@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson5.task1

import java.lang.Double.MAX_VALUE

/**
 * Пример
 *
 * Для заданного списка покупок `shoppingList` посчитать его общую стоимость
 * на основе цен из `costs`. В случае неизвестной цены считать, что товар
 * игнорируется.
 */
fun shoppingListCost(
        shoppingList: List<String>,
        costs: Map<String, Double>): Double {
    var totalCost = 0.0

    for (item in shoppingList) {
        val itemCost = costs[item]
        if (itemCost != null) {
            totalCost += itemCost
        }
    }

    return totalCost
}

/**
 * Пример
 *
 * Для набора "имя"-"номер телефона" `phoneBook` оставить только такие пары,
 * для которых телефон начинается с заданного кода страны `countryCode`
 */
fun filterByCountryCode(
        phoneBook: MutableMap<String, String>,
        countryCode: String) {
    val namesToRemove = mutableListOf<String>()
    for ((name, phone) in phoneBook) {
        if (!phone.startsWith(countryCode)) {
            namesToRemove.add(name)
        }
    }

    for (name in namesToRemove) {
        phoneBook.remove(name)
    }
}

/**
 * Пример
 *
 * Для заданного текста `text` убрать заданные слова-паразиты `fillerWords`
 * и вернуть отфильтрованный текст
 */
fun removeFillerWords(
        text: List<String>,
        vararg fillerWords: String): List<String> {
    val fillerWordSet = setOf(*fillerWords)

    val res = mutableListOf<String>()
    for (word in text) {
        if (word !in fillerWordSet) {
            res += word
        }
    }
    return res
}

/**
 * Пример
 *
 * Для заданного текста `text` построить множество встречающихся в нем слов
 */
fun buildWordSet(text: List<String>): MutableSet<String> {
    val res = mutableSetOf<String>()
    for (word in text) res.add(word)
    return res
}

/**
 * Средняя
 *
 * Объединить два ассоциативных массива `mapA` и `mapB` с парами
 * "имя"-"номер телефона" в итоговый ассоциативный массив, склеивая
 * значения для повторяющихся ключей через запятую.
 * В случае повторяющихся *ключей* значение из mapA должно быть
 * перед значением из mapB.
 *
 * Повторяющиеся *значения* следует добавлять только один раз.
 *
 * Например:
 *   mergePhoneBooks(
 *     mapOf("Emergency" to "112", "Police" to "02"),
 *     mapOf("Emergency" to "911", "Police" to "02")
 *   ) -> mapOf("Emergency" to "112, 911", "Police" to "02")
 */
fun mergePhoneBooks(mapA: Map<String, String>, mapB: Map<String, String>): Map<String, String> {

    val a = mapA.toMutableMap()
    for ((key, value) in mapB)
        if ((a.containsKey(key)) && (a[key] != value))
            a[key] = a[key] + ", $value"
        else a[key] = value
    return a
}

/**
 * Простая
 *
 * По заданному ассоциативному массиву "студент"-"оценка за экзамен" построить
 * обратный массив "оценка за экзамен"-"список студентов с этой оценкой".
 *
 * Например:
 *   buildGrades(mapOf("Марат" to 3, "Семён" to 5, "Михаил" to 5))
 *     -> mapOf(5 to listOf("Семён", "Михаил"), 3 to listOf("Марат"))
 */

fun buildGrades(grades: Map<String, Int>): Map<Int, List<String>> {
    val mapS = mutableMapOf<Int, List<String>>()
    for ((name, grade) in grades)
        if (mapS.containsKey(grade)) {
            mapS[grade] = mapS[grade]!! + name
        } else mapS[grade] = emptyList<String>() + name
    for ((key, value) in mapS) mapS[key] = value.sortedDescending()
    return mapS
}

/**
 * Простая
 *
 * Определить, входит ли ассоциативный массив a в ассоциативный массив b;
 * это выполняется, если все ключи из a содержатся в b с такими же значениями.
 *
 * Например:
 *   containsIn(mapOf("a" to "z"), mapOf("a" to "z", "b" to "sweet")) -> true
 *   containsIn(mapOf("a" to "z"), mapOf("a" to "zee", "b" to "sweet")) -> false
 */
fun containsIn(a: Map<String, String>, b: Map<String, String>): Boolean = a.all { (key, value) -> b[key] == value }

/**
 * Средняя
 *
 * Для заданного списка пар "акция"-"стоимость" вернуть ассоциативный массив,
 * содержащий для каждой акции ее усредненную стоимость.
 *
 * Например:
 *   averageStockPrice(listOf("MSFT" to 100.0, "MSFT" to 200.0, "NFLX" to 40.0))
 *     -> mapOf("MSFT" to 150.0, "NFLX" to 40.0)
 */
fun averageStockPrice(stockPrices: List<Pair<String, Double>>): Map<String, Double> {
    val a = mutableMapOf<String, Double>()
    val repeats = mutableMapOf<String, Int>()
    for ((name, price) in stockPrices) {
        if (a[name] != null) {
            a[name] = a[name]!! + price
            repeats[name] = repeats[name]!! + 1
        } else {
            a[name] = price
            repeats[name] = 1
        }
    }
    a.forEach { (name, price) -> a[name] = price / (repeats[name]!!) }
    return a
}

/**
 * Средняя
 *
 * Входными данными является ассоциативный массив
 * "название товара"-"пара (тип товара, цена товара)"
 * и тип интересующего нас товара.
 * Необходимо вернуть название товара заданного типа с минимальной стоимостью
 * или null в случае, если товаров такого типа нет.
 *
 * Например:
 *   findCheapestStuff(
 *     mapOf("Мария" to ("печенье" to 20.0), "Орео" to ("печенье" to 100.0)),
 *     "печенье"
 *   ) -> "Мария"
 */
fun findCheapestStuff(stuff: Map<String, Pair<String, Double>>, kind: String): String? = stuff
        .filter { (_, product) -> product.first == kind }
        .minBy { (_, product) -> product.second }
        ?.component1()

/**
 * Сложная
 *
 * Для заданного ассоциативного массива знакомых через одно рукопожатие `friends`
 * необходимо построить его максимальное расширение по рукопожатиям, то есть,
 * для каждого человека найти всех людей, с которыми он знаком через любое
 * количество рукопожатий.
 * Считать, что все имена людей являются уникальными, а также что рукопожатия
 * являются направленными, то есть, если Марат знает Свету, то это не означает,
 * что Света знает Марата.
 *
 * Например:
 *   propagateHandshakes(
 *     mapOf(
 *       "Marat" to setOf("Mikhail", "Sveta"),
 *       "Sveta" to setOf("Marat"),
 *       "Mikhail" to setOf("Sveta")
 *     )
 *   ) -> mapOf(
 *          "Marat" to setOf("Mikhail", "Sveta"),
 *          "Sveta" to setOf("Marat", "Mikhail"),
 *          "Mikhail" to setOf("Sveta", "Marat")
 *        )
 */
/*fun propagateHandshakes(friends: Map<String, Set<String>>): Map<String, Set<String>> {
    val a = mutableMapOf<String, MutableSet<String>>()
    for ((name, people) in friends) {
        if (people != emptySet<String>())
            a[name] = people.toMutableSet() else
            a[name] = emptySet<String>().toMutableSet()
        for (man in people) if (a.containsKey(man)) {
        } else a[man] = emptySet<String>().toMutableSet()
    }
    for ((name, friend) in a)
        for ((friendName, newFriends) in a)
            if ((friendName != name) && (friend.contains(friendName))) {
                friend += newFriends
                friend.remove(name)
            }

    return a
}*/

fun propagateHandshakes(friends: Map<String, Set<String>>): Map<String, Set<String>> {
    val a = mutableMapOf<String, MutableSet<String>>()
    for ((name, people) in friends) {
        a[name] = ((a[name] ?: emptySet<String>()) + people).toMutableSet()
        for (man in people) {
            a[name]!! += (friends[man] ?: emptySet())
            if (!a.containsKey(man))
                a[man] = (friends[man] ?: emptySet()).toMutableSet()
            else for (thirdFriend in a[man]!!)
                a[man]!! += (friends[thirdFriend] ?: emptySet()).toMutableSet()
            for (p in friends[man] ?: emptySet())
                a[name]!! += (friends[p] ?: emptySet())
        }
    }
    for ((name, people) in a)
        if (people.contains(name))
            people.remove(name)
    return a
}

/**
 * Простая
 *
 * Удалить из изменяемого ассоциативного массива все записи,
 * которые встречаются в заданном ассоциативном массиве.
 * Записи считать одинаковыми, если и ключи, и значения совпадают.
 *
 * ВАЖНО: необходимо изменить переданный в качестве аргумента
 *        изменяемый ассоциативный массив
 *
 * Например:
 *   subtractOf(a = mutableMapOf("a" to "z"), mapOf("a" to "z"))
 *     -> a changes to mutableMapOf() aka becomes empty
 */
fun subtractOf(a: MutableMap<String, String>, b: Map<String, String>) = a.keys.removeIf { b[it] == a[it] }

/**
 * Простая
 *
 * Для двух списков людей найти людей, встречающихся в обоих списках
 */
fun whoAreInBoth(a: List<String>, b: List<String>): List<String> =
        a.filter { it -> b.contains(it) }.toSet().toList()

/**
 * Средняя
 *
 * Для заданного набора символов определить, можно ли составить из него
 * указанное слово (регистр символов игнорируется)
 *
 * Например:
 *   canBuildFrom(listOf('a', 'b', 'o'), "baobab") -> true
 */
fun canBuildFrom(chars: List<Char>, word: String): Boolean {
    val neWord = mutableSetOf<Char>()
    val s = mutableSetOf<Char>()
    for (char in chars) s.add(char.toLowerCase())
    for (letter in word) neWord.add(letter.toLowerCase())
    return (s.containsAll(neWord))
}


/**
 * Средняя
 *
 * Найти в заданном списке повторяющиеся элементы и вернуть
 * ассоциативный массив с информацией о числе повторений
 * для каждого повторяющегося элемента.
 * Если элемент встречается только один раз, включать его в результат
 * не следует.
 *
 * Например:
 *   extractRepeats(listOf("a", "b", "a")) -> mapOf("a" to 2)
 */
fun extractRepeats(list: List<String>): Map<String, Int> {
    val a = mutableMapOf<String, Int>()
    list.forEach { letter -> a[letter] = (a[letter] ?: 0) + 1 }
    return a.filter { (_, number) -> number > 1 }
}

/**
 * Средняя
 *
 * Для заданного списка слов определить, содержит ли он анаграммы
 * (два слова являются анаграммами, если одно можно составить из второго)
 *
 * Например:
 *   hasAnagrams(listOf("тор", "свет", "рот")) -> true
 */
fun hasAnagrams(words: List<String>): Boolean {
    var b = false
    val anagrams = mutableSetOf<MutableList<Char>>()
    for (word in words) {
        val newSet = word.toMutableList()
        newSet.sort()
        if (anagrams.contains(newSet)) {
            b = true
            break
        } else anagrams.add(newSet)
    }
    return b
}

/**
 * Сложная
 *
 * Для заданного списка неотрицательных чисел и числа определить,
 * есть ли в списке пара чисел таких, что их сумма равна заданному числу.
 * Если да, верните их индексы в виде Pair<Int, Int>;
 * если нет, верните пару Pair(-1, -1).
 *
 * Индексы в результате должны следовать в порядке (меньший, больший).
 *
 * Постарайтесь сделать ваше решение как можно более эффективным,
 * используя то, что вы узнали в данном уроке.
 *
 * Например:
 *   findSumOfTwo(listOf(1, 2, 3), 4) -> Pair(0, 2)
 *   findSumOfTwo(listOf(1, 2, 3), 6) -> Pair(-1, -1)
 */
fun findSumOfTwo(list: List<Int>, number: Int): Pair<Int, Int> {
    val a = Pair(-1, -1)
    val map = HashMap<Int, Pair<Int, Int>>()
    for (i in 0..(list.size - 1))
        for (j in i + 1 until list.size) {
            map[list[i] + list[j]] = Pair(i, j)
            if (map.containsKey(number)) break
        }
    return (map[number] ?: a)
}

/**
 * Очень сложная
 *
 * Входными данными является ассоциативный массив
 * "название сокровища"-"пара (вес сокровища, цена сокровища)"
 * и вместимость вашего рюкзака.
 * Необходимо вернуть множество сокровищ с максимальной суммарной стоимостью,
 * которые вы можете унести в рюкзаке.
 *
 * Например:
 *   bagPacking(
 *     mapOf("Кубок" to (500 to 2000), "Слиток" to (1000 to 5000)),
 *     850
 *   ) -> setOf("Кубок")
 *   bagPacking(
 *     mapOf("Кубок" to (500 to 2000), "Слиток" to (1000 to 5000)),
 *     450
 *   ) -> emptySet()
 */


fun bagPacking(treasures: Map<String, Pair<Int, Int>>, capacity: Int): Set<String> {
    //maps for treasure chars access
    val treasureWeight = mutableMapOf<String, Int>()
    treasures.forEach { treasureWeight[it.key] = it.value.first }
    val treasurePrice = mutableMapOf<String, Int>()
    treasures.forEach { treasurePrice[it.key] = it.value.second }

    var minWeight = Pair(MAX_VALUE.toInt(), 0)
    //searching element with a smallest weight
    for (treasure in treasures)
        if (minWeight.first > treasure.value.first) {
            minWeight = treasure.value
        }
    if (minWeight.first > capacity) return emptySet()

    //making array of weights
    //Pair<weight of all included treasures, price of all ones>
    val variableBagPacking = mutableMapOf<Pair<Int, Int>, Set<String>>()
    for (treasure in treasures) {
        if (treasure.value.first <= capacity)
            variableBagPacking[treasure.value] = emptySet<String>() + treasure.key
    }
    for (i in (minWeight.first + 1)..capacity) {

        //making map of all available weights to stack in
        //Pair here is identical to pair in BagPacking
        val weights = mutableMapOf<Int, Pair<Int, Int>>()
        for (pair in variableBagPacking.keys)
            if (i - pair.first > 0)
                weights[i - pair.first] = pair
        if (weights.isNotEmpty()) {
            //Searching for the most suitable treasure
            var chosenWeight = Pair(0, 0)
            var bestTreasure = Pair(0, "")
            for (weight in weights) {
                //best for this weight left
                var maxPrice = Pair(0, "")
                if (treasureWeight.containsValue(weight.key)) {
                    var fastTreasureMap = emptySet<String>()
                    for (element in treasureWeight)
                        if (element.value == weight.key)
                            fastTreasureMap += element.key
                    for (element in fastTreasureMap) {
                        if ((treasurePrice[element]!! >= maxPrice.first)
                                && (!variableBagPacking[weight.value]!!.contains(element)))
                            maxPrice = Pair(treasurePrice[element]!!, element)
                    }
                }
                if (weight.value.second + maxPrice.first >= bestTreasure.first)
                    if (maxPrice != Pair(0, "")) {
                        bestTreasure = maxPrice
                        chosenWeight = weight.value
                    }
            }
            if (bestTreasure != Pair(0, ""))
                variableBagPacking[Pair(i, (chosenWeight.second + bestTreasure.first))] =
                        (variableBagPacking[chosenWeight] ?: emptySet()) + bestTreasure.second
        }
    }
    //finding best result
    var output = emptySet<String>()
    var bestChoice = 0


    for (treasure in variableBagPacking)
        if (bestChoice < treasure.key.second) {
            output = treasure.value
            bestChoice = treasure.key.second
        }
    return output


    /*//maps for treasure chars access
    val treasureWeight = mutableMapOf<Int, String>()
    treasures.forEach { treasureWeight[it.value.first] = it.key }
    val treasurePrice = mutableMapOf<String, Int>()
    treasures.forEach { treasurePrice[it.key] = it.value.second }

    var minWeight = Pair(MAX_VALUE.toInt(), 0)
    var minElement = ""
    //searching element with a smallest weight
    for (treasure in treasures)
        if (minWeight.first > treasure.value.first) {
            minWeight = treasure.value
            minElement = treasure.key
        }
    if (minWeight.first > capacity) return emptySet()

    //making array of weights
    //Pair<weight of all included treasures, price of all ones>
    val variableBagPacking = mutableMapOf<Pair<Int, Int>, Set<String>>()
    variableBagPacking[minWeight] = emptySet<String>() + minElement
    for (i in (minWeight.first + 1)..capacity) {

        //making map of all available weights to stack in
        //Pair here is identical to pair in BagPacking
        val weights = mutableMapOf<Int, Pair<Int, Int>>()
        for (pair in variableBagPacking.keys)
            if (i - pair.first > 0)
                weights[i - pair.first] = pair
        if (weights.isNotEmpty()) {
            //Searching for the most suitable treasure
            var chosenWeight = Pair(0, 0)
            var bestTreasure = Pair(0, "")
            for (weight in weights) {
                //best for this weight left
                var maxPrice = Pair(0, "")
                if (treasureWeight.containsKey(weight.key)) {
                    val currentTreasure = treasureWeight[weight.key]
                    maxPrice = Pair(treasurePrice[currentTreasure]!!,
                            currentTreasure!!)
                }
                if (weight.value.second + maxPrice.first >= bestTreasure.first)
                    bestTreasure = maxPrice
                chosenWeight = weight.value
            }
            variableBagPacking[Pair(i, (chosenWeight.second + bestTreasure.first))] =
                    (variableBagPacking[chosenWeight] ?: emptySet()) + bestTreasure.second
        }
    }
    //finding best result
    var output = emptySet<String>()
    var bestChoice = 0
    for (treasure in variableBagPacking)
        if (bestChoice < treasure.key.second) {
            output = treasure.value
            bestChoice = treasure.key.second
        }
    return output*/
}