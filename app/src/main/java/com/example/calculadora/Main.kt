package com.example.calculadora

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import com.example.calculadora.databinding.MainlayoutBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class Main : ComponentActivity() {

    private lateinit var binding: MainlayoutBinding
    private var calculateExpression: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainlayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClick()
    }

    private fun onClick() {
        with(binding){

            buttonZero.setOnClickListener { insert("0", false, false) }
            buttonOne.setOnClickListener { insert("1", false, false) }
            buttonTwo.setOnClickListener { insert("2", false, false) }
            buttonThree.setOnClickListener { insert("3", false, false) }
            buttonFour.setOnClickListener { insert("4", false, false) }
            buttonFive.setOnClickListener { insert("5", false, false) }
            buttonSix.setOnClickListener { insert("6", false, false) }
            buttonSeven.setOnClickListener { insert("7", false, false) }
            buttonEight.setOnClickListener { insert("8", false, false) }
            buttonNine.setOnClickListener { insert("9", false, false) }
            buttonComma.setOnClickListener { insert(".", false, false) }

            buttonAddition.setOnClickListener { insert("+", true, false) }
            buttonDivision.setOnClickListener { insert("÷", true, false) }
            buttonMultiplication.setOnClickListener { insert("x", true, false) }
            buttonSubtraction.setOnClickListener { insert("-", true, false) }
            buttonParentheses.setOnClickListener { insert("(", true, false) }
            buttonPercent.setOnClickListener { insert("%", true, false) }
            buttonAllClear.setOnClickListener { clear() }
            buttonResult.setOnClickListener { insert(binding.inputResult.text.toString(), true, true) }
            iconBackspace.setOnClickListener { backspace() }
        }
    }

    private fun clear() {
        binding.inputResult.text = ""
        binding.inputExpression.text = ""
    }

    private fun insert(num: String, specialCharacter: Boolean, result: Boolean) {
        with(binding) {

            var expression = inputExpression.text.toString() + num

            if (specialCharacter) {
                inputResult.text = ""
                inputExpression.text = expression
            } else if(result) {
                inputExpression.text = num
                inputResult.text = ""
            }else{
                inputExpression.text = expression
                calculate()
            }
        }
    }

    private fun backspace() {
        var numero = binding.inputExpression.text.toString()

        if(numero.length > 0) {
            binding.inputExpression.text = numero.substring(0, numero.length - 1)
            calculate()
        }
    }

    private fun calculate() {
        if (convertExpressionToCalculate() != null && convertExpressionToCalculate().length > 0) {
            var expressionSize = convertExpressionToCalculate().length

            if (!convertExpressionToCalculate().substring(expressionSize - 1, expressionSize).matches(Regex("[0-9]*")) || convertExpressionToCalculate().matches(Regex("[0-9]*"))) { // se toda a string e numero ou se seu ultimo digito nao e um numero
                    binding.inputResult.text = ""
            } else{
                val expression: Expression =
                    ExpressionBuilder(convertExpressionToCalculate()).build()
                val resultado: Double = expression.evaluate();
                val longResult: Long = resultado.toLong()

                if (resultado == longResult.toDouble()) {
                    binding.inputResult.text = longResult.toString() // resultado sem virgula
                } else {
                    binding.inputResult.text = resultado.toString()
                }
            }
        }
        showExpression()
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

}