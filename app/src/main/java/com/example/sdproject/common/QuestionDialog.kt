package com.example.sdproject.common

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import com.example.sdproject.databinding.DialogAvatarBinding
import com.example.sdproject.databinding.DialogQuestionBinding

fun createQuestionDialog(
    context: Context,
    yes: () -> Unit,
): Dialog {
    val factory = LayoutInflater.from(context)
    val binding = DialogQuestionBinding.inflate(factory)
    val deleteDialog = AlertDialog.Builder(context).create()
    deleteDialog.setView(binding.root)

    val width = (context.resources.displayMetrics.widthPixels * 0.80).toInt()
    val height = (context.resources.displayMetrics.heightPixels * 0.80).toInt()
    deleteDialog.window?.setLayout(width, height)
    binding.txtCancel.setOnClickListener {
        deleteDialog.dismiss()
    }
    binding.txtYes.setOnClickListener {
        yes.invoke()
        deleteDialog.dismiss()
    }
    return deleteDialog
}

fun seeAvatar(
    context: Context,
    uri: Uri,
    update: () -> Unit
): Dialog {
    val factory = LayoutInflater.from(context)
    val binding = DialogAvatarBinding.inflate(factory)
    val deleteDialog = AlertDialog.Builder(context).create()
    deleteDialog.setView(binding.root)

    val width = (context.resources.displayMetrics.widthPixels * 0.80).toInt()
    val height = (context.resources.displayMetrics.heightPixels * 0.80).toInt()
    deleteDialog.window?.setLayout(width, height)

    binding.btnCancel.setOnClickListener {
        deleteDialog.dismiss()
    }

    binding.btnYes.setOnClickListener {
        update.invoke()
        deleteDialog.dismiss()
    }

    binding.imgAvatar.loadImage(uri)
    return deleteDialog
}