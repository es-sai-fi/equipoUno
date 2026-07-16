package com.example.picobotella.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.picobotella.databinding.FragmentInstructionsBinding
import com.bumptech.glide.Glide
import com.example.picobotella.R

class InstructionsFragment : Fragment() {
  private lateinit var binding: FragmentInstructionsBinding

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?,
  ): View {
    binding = FragmentInstructionsBinding.inflate(inflater)
    binding.lifecycleOwner = this
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    goBackHome()

    Glide.with(this)
      .asGif()
      .load(R.drawable.winner_pico_botella)
      .into(binding.winnerIcon)
  }

  private fun goBackHome() {
    binding.btnInstructionsBack.setOnClickListener {
      findNavController().navigateUp()
    }
  }
}
