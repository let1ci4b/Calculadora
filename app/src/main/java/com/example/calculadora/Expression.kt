package com.example.calculadora

import android.util.Log
import androidx.core.text.isDigitsOnly

class Expression {
    var fieldExpression: String = ""
    var fieldResult: String = ""

    fun isSpecialCharacter(value: String): Boolean {
        return !value.isDigitsOnly()
    }

    fun lastCharacterIsSpecial(): Boolean {
        return if(fieldExpression.isNotEmpty()) !fieldExpression.last().isDigit()
        else false
    }

    fun isOnlyNumber(): Boolean {
        return if (fieldExpression.isNotEmpty()) fieldExpression.substring(0, fieldExpression.length).matches(Regex("[0-9].+"))
        else return false
    }

    fun convertExpressionToCalculate(): String {
        val division = DIVISION.toString()
        val multiplication = MULTIPLICATION.toString()
        val percent = PERCENT.toString()

        return fieldExpression.replace(multiplication, "*").replace(division, "/")
            .replace(percent, "/100*")
    }
}