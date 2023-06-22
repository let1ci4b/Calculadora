package com.example.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.calculadora.databinding.MainlayoutBinding

class Main : ComponentActivity() {

    private lateinit var binding: MainlayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainlayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClick()
    }

    private fun onClick(){
        with(binding){

            buttonZero.setOnClickListener() { insert(0) }
            buttonOne.setOnClickListener() { insert(1) }
            buttonTwo.setOnClickListener() { insert(2) }
            buttonThree.setOnClickListener() { insert(3) }
            buttonFour.setOnClickListener() { insert(4) }
            buttonFive.setOnClickListener() { insert(5) }
            buttonSix.setOnClickListener() { insert(6) }
            buttonSeven.setOnClickListener() { insert(7) }
            buttonEight.setOnClickListener() { insert(8) }
            buttonNine.setOnClickListener() { insert(9) }
        }
    }

    private fun clear(){

    }

    private fun insert(num: Int){

    }

    private fun backspace(){

    }

    private fun calculate(){

    }
}