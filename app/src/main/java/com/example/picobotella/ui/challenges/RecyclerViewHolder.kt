package com.example.picobotella.ui.challenges

import androidx.recyclerview.widget.RecyclerView
import com.example.picobotella.data.local.Challenge
import com.example.picobotella.databinding.ItemChallengeBinding

class RecyclerViewHolder (binding: ItemChallengeBinding): RecyclerView.ViewHolder(binding.root){

    val bindingItem = binding

    fun setItemChallenge(challenge: Challenge){
        bindingItem.tvDescription.text = challenge.descripcion
    }
}