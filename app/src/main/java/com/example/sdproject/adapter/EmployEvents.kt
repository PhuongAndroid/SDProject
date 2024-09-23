package com.example.sdproject.adapter

import com.example.sdproject.model.Employ

interface EmployEvents {
    fun onClick(item: Employ)
    fun onEdit(item: Employ)
    fun onDelete(item: Employ)
}