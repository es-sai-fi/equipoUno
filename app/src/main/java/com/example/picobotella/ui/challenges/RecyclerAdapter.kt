package com.example.picobotella.ui.challenges


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.picobotella.data.local.Challenge
import com.example.picobotella.databinding.ItemChallengeBinding

class RecyclerAdapter (private val challengesList:MutableList<Challenge>):
    RecyclerView.Adapter<RecyclerViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewHolder {
        val binding = ItemChallengeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecyclerViewHolder,
        position: Int
    ) {
        val challenge = challengesList[position]
        holder.setItemChallenge(challenge)
    }

    override fun getItemCount(): Int {
        return challengesList.size
    }

}