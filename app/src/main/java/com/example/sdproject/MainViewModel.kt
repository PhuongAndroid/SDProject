package com.example.sdproject

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.sdproject.model.Employ

class MainViewModel : ViewModel() {
    val allData: MutableList<Employ?> = mutableListOf()
}