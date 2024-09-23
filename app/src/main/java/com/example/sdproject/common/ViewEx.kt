package com.example.sdproject.common

import android.app.Activity
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.sdproject.R

fun Activity.addFragment(fragmentManage: FragmentManager, idContainer: Int, fragment: Fragment) {
    fragmentManage
        .beginTransaction()
        .add(idContainer, fragment, fragment::class.java.name)
        .commitAllowingStateLoss()
}

fun Activity.replaceFragment(fragmentManage: FragmentManager, idContainer: Int, fragment: Fragment) {
    fragmentManage
        .beginTransaction()
        .replace(idContainer, fragment, fragment::class.java.name)
        .addToBackStack(fragment::class.java.name)
        .commitAllowingStateLoss()
}

fun ImageView.loadImage(uri: String?) {
    Glide
        .with(this.context)
        .load(uri)
        .centerCrop()
        .placeholder(R.drawable.ic_employ_default)
        .into(this)
}

fun ImageView.loadImage(uri: Uri?) {
    Glide
        .with(this.context)
        .load(uri)
        .centerCrop()
        .placeholder(R.drawable.ic_employ_default)
        .into(this)
}

fun Fragment.showToast(msg: String) {
    Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show()
}