package com.example.picobotella.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.picobotella.model.Challenge
import com.example.picobotella.databinding.FragmentChallengesBinding
import com.example.picobotella.view.adapter.RecyclerAdapter

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
        setupRecyclerView()
        goBackHome()
    }

    private fun setupRecyclerView() {
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        val exampleChallenges = mutableListOf<Challenge>(
            Challenge(
                descripcion = "Realiza un RecyclerView que liste una api de Pokemones"
            ),
            Challenge(
                descripcion = "Completa el desafío de la botella"
            ),
            Challenge(
                descripcion = "Diseña la interfaz de usuario"
            )
        )

        val adapter = RecyclerAdapter(exampleChallenges)
        binding.recyclerview.adapter = adapter
    }

    private fun goBackHome() {
        binding.btnChallengesBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}