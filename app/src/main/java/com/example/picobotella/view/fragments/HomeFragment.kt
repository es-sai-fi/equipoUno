package com.example.picobotella.view.fragments

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.picobotella.R
import com.example.picobotella.databinding.FragmentHomeBinding
import kotlin.random.Random

private const val SPIN_DURATION = 3000L

class HomeFragment : Fragment() {
  private lateinit var binding: FragmentHomeBinding

  private var backgroundPlayer: MediaPlayer? = null
  private var spinPlayer: MediaPlayer? = null
  private var countdownTimer: CountDownTimer? = null

  private var isAudioOn: Boolean = true
  private var bottleRotation: Float = 0f

    override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?,
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setupBackPress()
    setupAudio()
    setupToolbar()
    setupBottleButton()
    startBlinkAnimation()
  }

  override fun onDestroyView() {
    super.onDestroyView()

    countdownTimer?.cancel()

    backgroundPlayer?.release()
    backgroundPlayer = null

    spinPlayer?.release()
    spinPlayer = null
  }

  private fun setupBackPress() {
    requireActivity()
        .onBackPressedDispatcher
        .addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
              override fun handleOnBackPressed() {
                requireActivity().finish()
              }
            },
        )
  }

  private fun setupAudio() {
    backgroundPlayer = MediaPlayer.create(requireContext(), R.raw.background_music)
    backgroundPlayer?.isLooping = true
    backgroundPlayer?.start()
    isAudioOn = true
  }

  private fun setupToolbar() {
    binding.btnRate.setOnClickListener {
      animateButton(it){
      rateApp()}
    }

    binding.btnShare.setOnClickListener {
      animateButton(it){
      shareApp()}
    }

    binding.btnAudio.setOnClickListener {
      animateButton(it) {
      toggleAudio()}
    }

    binding.btnInstructions.setOnClickListener {
      animateButton(it) {
      pauseAudioIfNeeded()
      findNavController().navigate(R.id.action_homeFragment_to_instructionsFragment)}
    }

    binding.btnChallenges.setOnClickListener {
      animateButton(it) {
      pauseAudioIfNeeded()
      findNavController().navigate(R.id.action_homeFragment_to_challengesFragment) }
    }
  }

  private fun setupBottleButton() {
    binding.btnPresioname.setOnClickListener { spinBottle() }
  }

    private fun animateButton(view: View, onEnd: () -> Unit) {
        view.animate().cancel()

        view.animate()
            .scaleX(0.85f)
            .scaleY(0.85f)
            .setDuration(80)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(80)
                    .withEndAction(onEnd)
                    .start()
            }
            .start()
    }

  private fun startBlinkAnimation() {
    val blink =
        AlphaAnimation(0.3f, 1f).apply {
          duration = 600
          repeatMode = Animation.REVERSE
          repeatCount = Animation.INFINITE
        }
    binding.btnPresioname.startAnimation(blink)
  }

    private fun spinBottle() {
        binding.btnPresioname.clearAnimation()
        binding.btnPresioname.visibility = View.INVISIBLE

        pauseAudioIfNeeded()
        playSpinSound()

        val targetRotation = nextBottleRotation()

        startCountdown()

        binding.bottleImage
            .animate()
            .rotation(targetRotation)
            .setDuration(SPIN_DURATION)
            .withEndAction {
                saveBottleRotation(targetRotation)
                binding.tvCountdown.visibility = View.GONE
                binding.btnPresioname.visibility = View.VISIBLE
                startBlinkAnimation()

                resetSpinSound()
                resumeAudioIfNeeded()
            }
            .start()
    }

  private fun nextBottleRotation(): Float {
    val randomExtra = Random.nextInt(0, 361)
    val fullSpins = 3 * 360
    return bottleRotation + fullSpins + randomExtra
  }

  private fun nextSpinDuration(): Long {
    return (3..5).random() * 1000L
  }

  private fun saveBottleRotation(rotation: Float) {
    bottleRotation = rotation
  }

    private fun startCountdown() {
        binding.tvCountdown.visibility = View.VISIBLE
        binding.tvCountdown.text = "3"

        countdownTimer?.cancel()

        countdownTimer = object : CountDownTimer(SPIN_DURATION, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val seconds = kotlin.math.ceil(millisUntilFinished / 1000.0).toInt()
                binding.tvCountdown.text = seconds.toString()
            }

            override fun onFinish() {
                binding.tvCountdown.text = "0"
            }

        }.start()
    }

  private fun toggleAudio() {
    isAudioOn = !isAudioOn

    if (isAudioOn) {
      backgroundPlayer?.start()
      binding.btnAudio.setImageResource(R.drawable.ic_volume_on)
    } else {
      backgroundPlayer?.pause()
      binding.btnAudio.setImageResource(R.drawable.ic_volume_off)
    }
  }

  private fun playSpinSound() {
    spinPlayer?.release()
    spinPlayer = MediaPlayer.create(requireContext(), R.raw.bottle_spin)
    spinPlayer?.start()
  }

  private fun resetSpinSound() {
    spinPlayer?.release()
    spinPlayer = null
  }

  private fun pauseAudioIfNeeded() {
    if (isAudioOn) {
      backgroundPlayer?.pause()
    }
  }

  private fun resumeAudioIfNeeded() {
    if (isAudioOn) {
      backgroundPlayer?.start()
    }
  }

  private fun rateApp() {
    val intent =
        Intent(
            Intent.ACTION_VIEW,
            getString(R.string.play_store_url)
                .toUri(),
        )
    startActivity(intent)
  }

  private fun shareApp() {
    val shareText = getString(R.string.share_app_message)

    val intent =
        Intent(Intent.ACTION_SEND).apply {
          putExtra(Intent.EXTRA_TEXT, shareText)
          type = "text/plain"
        }

    startActivity(Intent.createChooser(intent, getString(R.string.share_app_title)))
  }
}