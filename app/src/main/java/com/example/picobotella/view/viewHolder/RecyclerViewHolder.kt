package com.example.picobotella.view.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.example.picobotella.databinding.ItemChallengeBinding
import com.example.picobotella.model.Challenge

class RecyclerViewHolder(binding: ItemChallengeBinding) : RecyclerView.ViewHolder(binding.root) {

  val bindingItem = binding

  fun setItemChallenge(challenge: Challenge) {
    bindingItem.tvDescription.text = challenge.descripcion
  }
}
