package com.example.picobotella.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.picobotella.R
import com.example.picobotella.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

  lateinit var binding: FragmentSplashBinding

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?,
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.bottleIcon.animate().rotation(360f).setDuration(2000).start()

    view.postDelayed(
        {
          if (isAdded) {

            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
          }
        },
        5000,
    )
  }
}
