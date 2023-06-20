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
    }
}