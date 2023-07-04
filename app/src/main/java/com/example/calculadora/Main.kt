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
    private var calculateExpression: String = ""
    private var isResult: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainlayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClick()

        val test = binding
    }

    private fun onClick() {
        with(binding){

            buttonZero.setOnClickListener { insert("0", false) }
            buttonOne.setOnClickListener { insert("1", false) }
            buttonTwo.setOnClickListener { insert("2", false) }
            buttonThree.setOnClickListener { insert("3", false) }
            buttonFour.setOnClickListener { insert("4", false) }
            buttonFive.setOnClickListener { insert("5", false) }
            buttonSix.setOnClickListener { insert("6", false) }
            buttonSeven.setOnClickListener { insert("7", false) }
            buttonEight.setOnClickListener { insert("8", false) }
            buttonNine.setOnClickListener { insert("9", false) }

            buttonComma.setOnClickListener { insert(".", true) }
            buttonAddition.setOnClickListener { insert("+", true) }
            buttonDivision.setOnClickListener { insert("÷", true) }
            buttonMultiplication.setOnClickListener { insert("x", true) }
            buttonSubtraction.setOnClickListener { insert("-", true) }
            buttonChangeSymbol.setOnClickListener { changeMathSymbol() }
            buttonPercent.setOnClickListener { insert("%", true) }
            buttonAllClear.setOnClickListener { clear() }
            buttonResult.setOnClickListener { showResult(binding.inputResult.text.toString()) }
            iconBackspace.setOnClickListener { backspace() }
        }
    }

    private fun clear() { // limpando os campos
        binding.inputResult.text = ""
        binding.inputExpression.text = ""
    }

    private fun insert(num: String, specialCharacter: Boolean) { // montando expressao
        with(binding) {

            if (specialCharacter) {
                if (lastCharacterIsSpecial()) backspace() // substituindo caracter especial pelo digitado anteriormente
                else if(isResult) isResult = false
                inputExpression.text = inputExpression.text.toString() + num
            } else if(isResult) { // limpando o campo expressão após exibir o resultado oficial
                inputExpression.text = num
                isResult = false
            } else {
                inputExpression.text = inputExpression.text.toString() + num
                calculate()
            }
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

        if (addSymbol && !lastCharacterIsSpecial()){
            val expressionBuilder = StringBuilder(inputExpression)
            binding.inputExpression.text = expressionBuilder.insert(position + 1, '-')
            calculate()
        } else if(changeSymbol && !lastCharacterIsSpecial()) {
            val symbol = if (inputExpression[position] == '+') "-" else  "+"
            binding.inputExpression.text = inputExpression.replaceRange(position, position + 1, symbol)
            calculate()
        }
    }

    private fun backspace() { // exclui o último digito da expressão
        val inputExpression = binding.inputExpression.text.toString()

        if(inputExpression.isNotEmpty()) {
            binding.inputExpression.text = inputExpression.substring(0, inputExpression.length - 1)
            calculate()
        }
    }

    private fun calculate() { // calculando em tempo real
        try {
            if (lastCharacterIsSpecial() || expressionIsOnlyNumber()) { // checando se deve mostrar o resultado
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
        calculateExpression = binding.inputExpression.text.toString()
        val division = "÷"
        val multiplication = "x"
        val percent = "%"

        calculateExpression = calculateExpression.replace(multiplication, "*").replace(division, "/").replace(percent, "/100*")
        return calculateExpression
    }

    private fun showResult(result: String) { // mostrando o resultado no campo da expressão
        if (result.isNotEmpty()) {
            binding.inputExpression.text = result
            binding.inputResult.text = ""
            isResult = true
        } else Toast.makeText(this@Main, "Formato inválido.", Toast.LENGTH_SHORT).show()
    }

    private fun lastCharacterIsSpecial() : Boolean { // checando se o último caractere é especial
        var isSpecial = false

        if (convertExpressionToCalculate().isNotEmpty()) {
            val expressionSize = convertExpressionToCalculate().length
            if (!convertExpressionToCalculate().substring(expressionSize - 1, expressionSize).matches(Regex("[0-9]*"))) return true
        }
        return isSpecial
    }

    private fun expressionIsOnlyNumber() : Boolean { // checando se a expressão é apenas um número
        var isSpecial = false

        if (convertExpressionToCalculate().isNotEmpty()) {
            val expressionSize = convertExpressionToCalculate().length
            if (convertExpressionToCalculate().substring(1, expressionSize).matches(Regex("[0-9.]*"))) return true
        }
        return isSpecial
    }
}