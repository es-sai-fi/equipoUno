package com.example.picobotella.ui.challenges

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.picobotella.databinding.FragmentChallengesBinding

class ChallengesFragment: Fragment() {
    private lateinit var binding : FragmentChallengesBinding;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChallengesBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goBackHome()
    }

    private fun goBackHome() {
        binding.btnChallengesBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}