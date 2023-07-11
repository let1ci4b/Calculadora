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
    private var expression = com.example.calculadora.Expression()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainlayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
    }

    private fun setupListeners() {
        with(binding){
            val buttons = listOf (
                buttonZero, buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix, buttonSeven, buttonEight, buttonNine,
                buttonMultiplication, buttonDivision, buttonAddition, buttonSubtraction, buttonPercent, buttonComma
            )

            buttons.forEach { button ->
                button.setOnClickListener {
                    val value = button.text.toString()
                    val specialCharacter = expression.isSpecialCharacter(value)
                    insertExpressionCharacters(value, specialCharacter)
                }
            }

            buttonAllClear.setOnClickListener { clearFields() }
            buttonResult.setOnClickListener { showResult(inputResult.text.toString()) }
            iconBackspace.setOnClickListener { backspace() }
            buttonChangeSymbol.setOnClickListener { changeMathSymbol() }
        }
    }

    private fun clearFields() { // limpando os campos
        with(binding){
            inputResult.text = ""
            inputExpression.text = ""
        }
    }

    private fun insertExpressionCharacters(num: String, specialCharacter: Boolean) {
        with(binding) {
            when{
                specialCharacter && expression.lastCharacterIsSpecial() -> {
                    backspace()
                    inputExpression.text = inputExpression.text.toString().plus(num)
                }
                isResult -> {
                    if(specialCharacter) inputExpression.text = inputExpression.text.toString().plus(num)
                    else inputExpression.text = num
                    isResult = false
                }
                else -> {
                    inputExpression.text = inputExpression.text.toString().plus(num)
                    calculateExpression()
                }
            }
            expression.calculateExpression = inputExpression.text.toString()
        }
    }

    private fun changeMathSymbol() {
        val inputExpression = binding.inputExpression.text.toString()
        var addSymbol = false
        var changeSymbol = false
        var position = 0

        for (i in inputExpression.length - 1 downTo 0) { // varrendo a expressão de trás pra frente
            if (inputExpression[i] == '%' || inputExpression[i] == '÷' || inputExpression[i] == 'x') {
                addSymbol = true
                position = i
                break
            } else if(inputExpression[i] == '-' || inputExpression[i] == '+') {
                changeSymbol = true
                position = i
                break
            }
        }

        if (addSymbol && !expression.lastCharacterIsSpecial()) {
            val expressionBuilder = StringBuilder(inputExpression)
            binding.inputExpression.text = expressionBuilder.insert(position + 1, '-')
            calculateExpression()
        } else if(changeSymbol && !expression.lastCharacterIsSpecial()) {
            val symbol = if (inputExpression[position] == '+') "-" else  "+"
            binding.inputExpression.text = inputExpression.replaceRange(position, position + 1, symbol)
            calculateExpression()
        }
    }

    private fun backspace() { // exclui o último digito da expressão
        val inputExpression = binding.inputExpression.text.toString()

        if(inputExpression.isNotEmpty()) {
            binding.inputExpression.text = inputExpression.substring(0, inputExpression.length - 1)
            calculateExpression()
        }
    }

    private fun calculateExpression() { // calculando em tempo real
        try {
            if (expression.lastCharacterIsSpecial() || expression.isOnlyNumber()) { // checando se deve mostrar o resultado
                binding.inputResult.text = ""
            } else { // calculando a expressao
                val expression: Expression =
                    ExpressionBuilder(convertExpressionToCalculate()).build()
                val resultado: Double = expression.evaluate()
                val longResult: Long = resultado.toLong()

                binding.inputResult.text = if (resultado == longResult.toDouble()) longResult.toString() else resultado.toString()
            }
        } catch (e: Exception){
            Log.i("", "Formato inválido.")
        } finally {
            showExpression()
        }
    }

    private fun showExpression() { // mostrando os caracteres especiais bonitinhos
        var inputExpression = binding.inputExpression.text.toString()
        val division = "/"
        val multiplication = "*"
        val percent = "/100*"

        inputExpression = inputExpression.replace(multiplication, "x").replace(division, "÷").replace(percent, "%")
        binding.inputExpression.text = inputExpression
    }

    private fun convertExpressionToCalculate() : String { // convertendo os caracteres especiais para calcular
        expression.calculateExpression = binding.inputExpression.text.toString()
        val division = "÷"
        val multiplication = "x"
        val percent = "%"

        expression.calculateExpression = expression.calculateExpression.replace(multiplication, "*").replace(division, "/").replace(percent, "/100*")
        return expression.calculateExpression
    }

    private fun showResult(result: String) { // mostrando o resultado no campo da expressão
        if (result.isNotEmpty()) {
            binding.inputExpression.text = result
            binding.inputResult.text = ""
            isResult = true
        } else Toast.makeText(this@Main, "Formato inválido.", Toast.LENGTH_SHORT).show()
    }

}