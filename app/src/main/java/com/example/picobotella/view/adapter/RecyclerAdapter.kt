package com.example.picobotella.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.picobotella.databinding.ItemChallengeBinding
import com.example.picobotella.model.Challenge
import com.example.picobotella.view.viewHolder.RecyclerViewHolder

class RecyclerAdapter(private val challengesList: MutableList<Challenge>) :
    RecyclerView.Adapter<RecyclerViewHolder>() {
  override fun onCreateViewHolder(
      parent: ViewGroup,
      viewType: Int,
  ): RecyclerViewHolder {
    val binding = ItemChallengeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return RecyclerViewHolder(binding)
  }

  override fun onBindViewHolder(
      holder: RecyclerViewHolder,
      position: Int,
  ) {
    val challenge = challengesList[position]
    holder.setItemChallenge(challenge)
  }

  override fun getItemCount(): Int {
    return challengesList.size
  }
}
