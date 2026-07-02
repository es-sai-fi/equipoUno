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

class HomeFragment : Fragment() {
  private var _binding: FragmentHomeBinding? = null
  private val binding get() = _binding!!

  private var backgroundPlayer: MediaPlayer? = null
  private var spinPlayer: MediaPlayer? = null
  private var countdownTimer: CountDownTimer? = null

  private var isAudioOn: Boolean = true
  private var bottleRotation: Float = 0f

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    isAudioOn = savedInstanceState?.getBoolean(STATE_AUDIO_ON) ?: isAudioOn
    bottleRotation = savedInstanceState?.getFloat(STATE_BOTTLE_ROTATION) ?: bottleRotation
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?,
  ): View {
    _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

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
    countdownTimer?.cancel()
    countdownTimer = null

    backgroundPlayer?.release()
    backgroundPlayer = null

    spinPlayer?.release()
    spinPlayer = null

    _binding = null
    super.onDestroyView()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    outState.putBoolean(STATE_AUDIO_ON, isAudioOn)
    outState.putFloat(STATE_BOTTLE_ROTATION, bottleRotation)
    super.onSaveInstanceState(outState)
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
    binding.btnAudio.setImageResource(
        if (isAudioOn) R.drawable.ic_volume_on else R.drawable.ic_volume_off,
    )
    if (isAudioOn) {
      backgroundPlayer?.start()
    }
  }

  private fun setupToolbar() {
    binding.btnRate.setOnClickListener {
      animateButton(it)
      rateApp()
    }

    binding.btnShare.setOnClickListener {
      animateButton(it)
      shareApp()
    }

    binding.btnAudio.setOnClickListener {
      animateButton(it)
      toggleAudio()
    }

    binding.btnInstructions.setOnClickListener {
      animateButton(it)
      pauseAudioIfNeeded()
      findNavController().navigate(R.id.action_homeFragment_to_instructionsFragment)
    }

    binding.btnChallenges.setOnClickListener {
      animateButton(it)
      pauseAudioIfNeeded()
      findNavController().navigate(R.id.action_homeFragment_to_challengesFragment)
    }
  }

  private fun setupBottleButton() {
    binding.btnPresioname.setOnClickListener { spinBottle() }
  }

  private fun animateButton(view: View) {
    view
        .animate()
        .scaleX(0.75f)
        .scaleY(0.75f)
        .rotation(-2f)
        .setDuration(120)
        .withEndAction {
          view.animate().scaleX(1f).scaleY(1f).rotation(0f).setDuration(120).start()
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
    val duration = nextSpinDuration()

    startCountdown(duration)

    binding.bottleImage
        .animate()
        .rotation(targetRotation)
        .setDuration(duration)
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

  private fun startCountdown(duration: Long) {
    binding.tvCountdown.visibility = View.VISIBLE

    val totalSeconds = (duration / 1000).toInt()
    var count = totalSeconds

    countdownTimer =
        object : CountDownTimer(duration, 1000) {
              override fun onTick(millisUntilFinished: Long) {
                binding.tvCountdown.text = (millisUntilFinished / 1000).toString()
              }

              override fun onFinish() {
                binding.tvCountdown.text = "0"
              }
            }
            .start()
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

  companion object {
    private const val STATE_AUDIO_ON = "state_audio_on"
    private const val STATE_BOTTLE_ROTATION = "state_bottle_rotation"
  }
}
