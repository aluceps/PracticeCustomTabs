package me.aluceps.practicecustomtabs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.aluceps.practicecustomtabs.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
    }

    private fun setupView() {
        binding.action.setOnClickListener {
            val url = binding.textUrl.text?.toString()
            if (url.isNullOrEmpty()) return@setOnClickListener
        }
    }
}
