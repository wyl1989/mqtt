package com.yurnero.mqtt.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yurnero.mqtt.databinding.ItemLayoutBinding

class MessageAdapter constructor(var msgs: MutableList<String>) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    inner class ViewHolder constructor(item: ItemLayoutBinding) :
        RecyclerView.ViewHolder(item.root) {
        val one = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLayoutBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.one.itemMsg.text = msgs[position]
    }

    override fun getItemCount(): Int {
        return msgs.size
    }

    fun newMessage(string: String) {
        msgs.add(string)
        notifyDataSetChanged()
    }
}