package com.example.picobotella.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.picobotella.databinding.ItemChallengeBinding
import com.example.picobotella.model.Challenge
import com.example.picobotella.view.viewHolder.ChallengeViewHolder

class ChallengeAdapter(
    private val onEdit: (Challenge) -> Unit,
    private val onDelete: (Challenge) -> Unit,
) : ListAdapter<Challenge, ChallengeViewHolder>(ChallengeDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val binding = ItemChallengeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return ChallengeViewHolder(binding, onEdit, onDelete)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object ChallengeDiffCallback : DiffUtil.ItemCallback<Challenge>() {
        override fun areItemsTheSame(oldItem: Challenge, newItem: Challenge): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Challenge, newItem: Challenge): Boolean {
            return oldItem == newItem
        }
    }
}
