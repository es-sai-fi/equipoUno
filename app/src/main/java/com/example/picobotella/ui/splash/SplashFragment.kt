package com.example.picobotella.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.picobotella.R
import com.example.picobotella.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

  private lateinit var binding: FragmentSplashBinding

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

    binding.rateButton.setOnClickListener { rateApp() }

    binding.shareButton.setOnClickListener { shareApp() }

    /*
    view.postDelayed({

        if (isAdded) {

            findNavController().navigate(
                R.id.action_splashFragment_to_homeFragment
            )

        }

    }, 5000)
    */
  }

  private fun rateApp() {

    val intent =
        Intent(
            Intent.ACTION_VIEW,
            "https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es"
                .toUri(),
        )

    startActivity(intent)
  }

  private fun shareApp() {

    val shareText =
        """
        App Pico Botella

        Solo los valientes lo juegan !!

        https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es
        """
            .trimIndent()

    val intent =
        Intent().apply {
          action = Intent.ACTION_SEND

          putExtra(Intent.EXTRA_TEXT, shareText)

          type = "text/plain"
        }

    startActivity(Intent.createChooser(intent, "Compartir aplicación"))
  }
}
