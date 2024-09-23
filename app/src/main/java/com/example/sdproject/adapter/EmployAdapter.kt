package com.example.sdproject.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sdproject.common.loadImage
import com.example.sdproject.databinding.ItemEmployBinding
import com.example.sdproject.databinding.ItemLoadingBinding
import com.example.sdproject.model.Employ

class EmployAdapter(
    private var data: MutableList<Employ?>,
    val events: EmployEvents
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val employBinding = ItemEmployBinding.inflate(inflater)
                EmployViewHolder(employBinding)
            }

            VIEW_TYPE_LOADING -> {
                LoadingViewHolder(ItemLoadingBinding.inflate(inflater))
            }

            else -> {
                val employBinding = ItemEmployBinding.inflate(inflater)
                EmployViewHolder(employBinding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        if (holder is EmployViewHolder) {
            holder.onBind(item!!)
            holder.onEvents(item)
        } else {
            holder as LoadingViewHolder
            holder.onBind()
        }

    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int =
        if (data[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM


    inner class EmployViewHolder(private val binding: ItemEmployBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(employ: Employ) {
            binding.imgAvatar.loadImage(employ.avatar)
            binding.txtFullName.text = employ.fullName
            binding.txtEmail.text = employ.email
            binding.txtPhone.text = employ.phone
            binding.txtNumOffice.text = employ.numberOffice.nameOffice
        }

        fun onEvents(employ: Employ) {
            binding.root.setOnClickListener {
                events.onClick(employ)
            }
            binding.btnDelete.setOnClickListener {
                events.onDelete(employ)
            }
            binding.btnEdit.setOnClickListener {
                events.onEdit(employ)
            }
        }
    }

    class LoadingViewHolder(private val binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() {
            val circleProgress = binding.circleProgress
            circleProgress.isIndeterminate = true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Employ?>) {
        this.data = data.toMutableList()
        notifyDataSetChanged()
    }
}