package com.example.calculadora

import androidx.core.text.isDigitsOnly

class Expression(){
    var calculateExpression: String = ""

    fun isSpecialCharacter(value: String) : Boolean {
        return !value.isDigitsOnly()
    }

    fun lastCharacterIsSpecial() : Boolean { // checando se o último caractere é especial
        val isSpecial =

        if (calculateExpression.isNotEmpty()) if (calculateExpression.last().isDigit()) false else true
        else false

        return isSpecial
    }

    fun isOnlyNumber() : Boolean { // checando se a expressão é apenas um número
        var isSpecial =

        if (calculateExpression.isNotEmpty()) if (calculateExpression.substring(1, calculateExpression.length).isDigitsOnly()) true else false
        else false

        return isSpecial
    }
}