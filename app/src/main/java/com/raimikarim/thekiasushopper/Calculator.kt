package com.raimikarim.thekiasushopper

import java.util.Locale

/***
 * Usage:
 * 1. Initialise
 * 2. Call `isAllDouble`
 * 3. Call `isAllValid`
 * 4. Call `parseStrings`
 * 5. Call `getRates`
 * 6. Use getters to get rates
 */
class Calculator(
        private val priceAString: String,
        private val quantityAString: String,
        private val priceBString: String,
        private val quantityBString: String) {

    private var quantityA: Double = 0.toDouble()
    private var quantityB: Double = 0.toDouble()
    private var priceA: Double = 0.toDouble()
    private var priceB: Double = 0.toDouble()
    private var rateA: Double = 0.toDouble()
    private var rateB: Double = 0.toDouble()

    /***
     * Compares between two rates.
     *
     * @return 1 if left>right, -1 if left< right and 0 otherwise
     */
    val isRateAHigher: Int
        get() = rateA.compareTo(rateB)

    /***
     * To be called
     * @return true if all strings are parseable to doubles
     */
    val isAllDouble: Boolean
        get() = isDouble(priceAString) &&
                isDouble(priceBString) &&
                isDouble(quantityAString) &&
                isDouble(quantityBString)

    val isAllValid: Boolean
        get() = quantityA != 0.0 && quantityB != 0.0

    /***
     * Parse strings to doubles
     */
    fun parseStrings() {
        priceA = java.lang.Double.parseDouble(priceAString)
        priceB = java.lang.Double.parseDouble(priceBString)
        quantityA = java.lang.Double.parseDouble(quantityAString)
        quantityB = java.lang.Double.parseDouble(quantityBString)
    }

    /***
     * Get rates for the two items
     */
    fun calculateRates() {
        rateA = priceA / quantityA
        rateB = priceB / quantityB
    }

    private fun isDouble(str: String): Boolean {
        try {
            java.lang.Double.parseDouble(str)
        } catch (e: NumberFormatException) {
            return false
        }
        return true
    }

    fun getRateARounded(): String {
        val rateAString = rateA.toString()
        return if (rateAString.length - (rateAString.indexOf(".") + 1) > 5) {
            String.format(Locale.ENGLISH, "%.5f", rateA)
        } else {
            rateAString
        }
    }

    fun getRateBRounded(): String {
        val rateBString = rateB.toString()
        return if (rateBString.length - (rateBString.indexOf(".") + 1) > 5) {
            String.format(Locale.ENGLISH, "%.5f", rateB)
        } else {
            rateBString
        }
    }
}
