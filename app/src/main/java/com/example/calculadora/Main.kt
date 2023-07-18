package com.example.calculadora

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.calculadora.databinding.MainlayoutBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class Main : ComponentActivity() {

    private lateinit var binding: MainlayoutBinding
    private var isResult: Boolean = false
    private var expression = Expression()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainlayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
    }

    private fun setupListeners() {
        with(binding) {
            val buttons = listOf(
                buttonZero, buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix, buttonSeven, buttonEight,
                buttonNine, buttonMultiplication, buttonDivision, buttonAddition, buttonSubtraction, buttonPercent, buttonComma
            )

            buttons.forEach { button ->
                button.setOnClickListener {
                    val value = button.text.toString()
                    val specialCharacter = expression.isSpecialCharacter(value)
                    validateCharactersInsertion(value, specialCharacter)
                }
            }

            buttonAllClear.setOnClickListener { clearFields() }
            buttonResult.setOnClickListener { showResult() }
            iconBackspace.setOnClickListener { backspace() }
            buttonChangeSymbol.setOnClickListener { changeMathSymbol() }
        }
    }

    private fun clearFields() {
        expression.fieldResult = ""
        expression.fieldExpression = ""
        with(binding) {
            inputResult.text = expression.fieldResult
            inputExpression.text = expression.fieldExpression
        }
    }

    private fun validateCharactersInsertion(value: String, specialCharacter: Boolean) {
        when {
            expression.checkExpressionSize() -> {
                Toast.makeText(this@Main, "Limite de caracteres!", Toast.LENGTH_SHORT).show()
            }
            specialCharacter && expression.lastCharacterIsSpecial() -> {
                backspace()
                insertExpressionCharacters(value)
            }
            (value == ".") && validateCommaInsertion() -> { }
            isResult -> {
                if (specialCharacter) insertExpressionCharacters(value)
                else expression.fieldExpression = value
                isResult = false
            }
            else -> insertExpressionCharacters(value)
        }
        binding.inputExpression.text = expression.fieldExpression
        calculateExpression()
    }

    private fun validateCommaInsertion(): Boolean {
        var isComma = false
        for (i in expression.fieldExpression.length - 1 downTo 0) {
            when {
                (expression.fieldExpression[i] == COMMA) -> {
                    isComma = true
                    break
                }
                (!expression.fieldExpression[i].toString().matches(Regex("[0-9.]*"))) -> break
                else -> continue
            }
        }
        return isComma
    }

    private fun insertExpressionCharacters(value: String) {
        expression.fieldExpression = expression.fieldExpression.plus(value)
    }

    private fun changeMathSymbol() {
        var addSymbol = false
        var changeSymbol = false
        var position = 0

        for (i in expression.fieldExpression.length - 1 downTo 0) {
            if (
                expression.fieldExpression[i] == PERCENT ||
                expression.fieldExpression[i] == MULTIPLICATION ||
                expression.fieldExpression[i] == DIVISION
            ) {
                addSymbol = true
                position = i
                break
            } else if (
                expression.fieldExpression[i] == ADDITION ||
                expression.fieldExpression[i] == SUBTRACTION
            ) {
                changeSymbol = true
                position = i
                break
            }
        }

        if (!expression.lastCharacterIsSpecial()) {
            when {
                addSymbol -> {
                    val stringBuilder = StringBuilder(expression.fieldExpression)
                    expression.fieldExpression = stringBuilder.insert(position + 1, SUBTRACTION).toString()
                }
                changeSymbol -> {
                    val symbol = if (expression.fieldExpression[position] == ADDITION) SUBTRACTION else ADDITION
                    expression.fieldExpression = expression.fieldExpression.replaceRange(position, position + 1, symbol.toString())
                }
            }
            binding.inputExpression.text = expression.fieldExpression
            calculateExpression()
        }
    }

    private fun backspace() {
        try {
            expression.fieldExpression = expression.fieldExpression.substring(0, expression.fieldExpression.length - 1)
        } catch (e: StringIndexOutOfBoundsException) {
            Toast.makeText(this@Main, "Seu campo já está limpo.", Toast.LENGTH_SHORT).show()
        } finally {
            binding.inputExpression.text = expression.fieldExpression
            calculateExpression()
        }
    }

    private fun calculateExpression() {
        try {
            if (expression.lastCharacterIsSpecial() || expression.isOnlyNumber()) expression.fieldResult = ""
            else {
                val calculateExpression: Expression =
                    ExpressionBuilder(expression.convertExpressionToCalculate()).build()
                val result: Double = calculateExpression.evaluate()
                val longResult: Long = result.toLong()
                expression.fieldResult = if (result == longResult.toDouble()) longResult.toString() else result.toString()
            }
        } catch (e: IllegalArgumentException) {
            Log.i("", "Formato de operação inválido!")
        } finally {
            binding.inputResult.text = expression.fieldResult
        }
    }

    private fun showResult() {
        if (expression.fieldResult.isNotEmpty()) {
            expression.fieldExpression = expression.fieldResult
            expression.fieldResult = ""
            binding.inputResult.text = expression.fieldResult
            binding.inputExpression.text = expression.fieldExpression
            isResult = true
        } else Toast.makeText(this@Main, "Formato inválido.", Toast.LENGTH_SHORT).show()
    }
}
