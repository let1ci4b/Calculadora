package com.example.calculadora

import androidx.core.text.isDigitsOnly

class Expression {
    var fieldExpression: String = ""
    var fieldResult: String = ""

    fun isSpecialCharacter(value: String): Boolean {
        return !value.isDigitsOnly()
    }

    fun lastCharacterIsSpecial(): Boolean {
        return if(!fieldExpression.isNullOrEmpty()) !fieldExpression.last().isDigit()
        else false
    }

    fun isOnlyNumber(): Boolean {
        return if (!fieldExpression.isNullOrEmpty()) fieldExpression.substring(1, fieldExpression.length).matches(Regex("[0-9.]*"))
        else false
    }

    fun convertExpressionToCalculate(): String {
        val division = DIVISION.toString()
        val multiplication = MULTIPLICATION.toString()
        val percent = PERCENT.toString()

        return fieldExpression.replace(multiplication, "*").replace(division, "/").replace(percent, "/100*")
    }

    fun checkExpressionSize() : Boolean {
        return fieldExpression.length >= 50
    }
}