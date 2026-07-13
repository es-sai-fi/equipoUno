package com.example.picobotella.view.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.picobotella.databinding.ItemChallengeBinding
import com.example.picobotella.model.Challenge

class ChallengeViewHolder(
    private val binding: ItemChallengeBinding,
    private val onEdit: (Challenge) -> Unit,
    private val onDelete: (Challenge) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

  fun bind(challenge: Challenge) {
    binding.tvDescription.text = challenge.description
    binding.btnEdit.setOnClickListener {
      animateTouch(it) { onEdit(challenge) }
    }
    binding.btnDelete.setOnClickListener {
      animateTouch(it) { onDelete(challenge) }
    }
  }

  private fun animateTouch(view: View, action: () -> Unit) {
    view.isEnabled = false
    view
        .animate()
        .scaleX(0.85f)
        .scaleY(0.85f)
        .setDuration(90)
        .withEndAction {
          view
              .animate()
              .scaleX(1f)
              .scaleY(1f)
              .setDuration(90)
              .withEndAction {
                view.isEnabled = true
                action()
              }
              .start()
        }
        .start()
  }
}
