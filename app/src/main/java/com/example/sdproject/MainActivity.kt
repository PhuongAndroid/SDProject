package com.example.sdproject

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.sdproject.common.addFragment
import com.example.sdproject.databinding.ActivityMainBinding
import com.example.sdproject.view.MainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        addFragment(supportFragmentManager, R.id.fragment_container_view_tag, MainFragment())
    }
}