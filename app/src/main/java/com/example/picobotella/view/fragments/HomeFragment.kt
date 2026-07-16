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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.picobotella.R
import com.example.picobotella.databinding.FragmentHomeBinding
import kotlin.random.Random
import com.example.picobotella.viewModel.ChallengeViewModel
import androidx.fragment.app.viewModels
import com.example.picobotella.databinding.CustomDialogueBinding
import android.app.Dialog
import android.view.Window
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.example.picobotella.model.PokemonModelResponse
import android.util.Log
import com.example.picobotella.model.Challenge

private const val SPIN_DURATION = 3000L

class HomeFragment : Fragment() {

  private lateinit var binding: FragmentHomeBinding

  private var isAudioEnabled = true
  private var isSpinning = false
  private var bottleRotation: Float = 0f

  private var backgroundPlayer: MediaPlayer? = null
  private var spinPlayer: MediaPlayer? = null
  private var countdownTimer: CountDownTimer? = null

  private var pokemonList = mutableListOf<PokemonModelResponse>()

  private var randomChallenge: Challenge? = null

  private val challengeViewModel: ChallengeViewModel by viewModels()

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?,
  ): View {
    binding = FragmentHomeBinding.inflate(inflater)
    binding.lifecycleOwner = this
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupBackPress()
    setupAudio()
    setupUI()
    setupBottle()
    startBlinkAnimation()
    observerViewModel()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    cleanup()
  }

  private fun setupUI() {
    setupToolbar()
  }

  private fun setupBottle() {
    binding.btnPresioname.setOnClickListener { spinBottle() }
  }

  private fun setupToolbar() {
    binding.btnRate.setOnClickListener { animateButton(it) { rateApp() } }
    binding.btnShare.setOnClickListener { animateButton(it) { shareApp() } }
    binding.btnAudio.setOnClickListener { animateButton(it) { toggleAudio() } }
    binding.btnInstructions.setOnClickListener {
      animateButton(it) { navigateSafely(R.id.action_homeFragment_to_instructionsFragment) }
    }
    binding.btnChallenges.setOnClickListener {
      animateButton(it) { navigateSafely(R.id.action_homeFragment_to_challengesFragment) }
    }
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
    backgroundPlayer =
        MediaPlayer.create(requireContext(), R.raw.background_music).apply {
          isLooping = true
        }

    if (isAudioEnabled) backgroundPlayer?.start()
  }

  private fun navigateSafely(action: Int) {
    backgroundPlayer?.pause()
    findNavController().navigate(action)
  }

  private fun animateButton(view: View, onEnd: () -> Unit) {
    view.animate().cancel()
    view
        .animate()
        .scaleX(0.85f)
        .scaleY(0.85f)
        .setDuration(80)
        .withEndAction {
          view.animate().scaleX(1f).scaleY(1f).setDuration(80).withEndAction(onEnd).start()
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
    startSpinState()

    val targetRotation = nextBottleRotation()

    playSpinAnimation(targetRotation)
  }

  private fun showChallengeDialog(challenge: Challenge?) {

    pauseAudioForChallenge()

    val dialogBinding = CustomDialogueBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext())

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(dialogBinding.root)

    dialog.setCancelable(false)
    dialog.setCanceledOnTouchOutside(false)

    dialog.window?.apply {
      setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

      val width = (resources.displayMetrics.widthPixels * 0.9f).toInt()
      setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    if (challenge == null) {

      dialogBinding.tvChallenge.text = "No se han añadido retos"

      // Opcional: ocultar el Pokémon cuando no hay retos
     // dialogBinding.ivImagenApi.visibility = View.GONE

    } else {

      dialogBinding.tvChallenge.text = challenge.description

      val randomPokemon = pokemonList.randomOrNull()

      randomPokemon?.let {
        val imageUrl =
          "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${it.id}.png"

        Glide.with(requireContext())
          .load(imageUrl)
          .into(dialogBinding.ivImagenApi)
      }
    }

    dialogBinding.cancelBtn.setOnClickListener {
      dialog.dismiss()

      if (isAudioEnabled) {
        backgroundPlayer?.start()
      }
    }

    dialog.show()
  }

  private fun pauseAudioForChallenge() {
    backgroundPlayer?.pause()
    stopSpinSound()
  }

  private fun startSpinState() {
    isSpinning = true
    binding.btnPresioname.clearAnimation()
    binding.btnPresioname.visibility = View.INVISIBLE
    backgroundPlayer?.pause()
    playSpinSound()
  }

  private fun playSpinAnimation(targetRotation: Float) {
    binding.bottleImage
        .animate()
        .rotation(targetRotation)
        .setDuration(SPIN_DURATION)
        .withEndAction { finishSpin(targetRotation) }
        .start()
  }

  private fun finishSpin(targetRotation: Float) {
    saveBottleRotation(targetRotation)

    binding.btnPresioname.visibility = View.VISIBLE
    startBlinkAnimation()

    isSpinning = false

    stopSpinSound()

    startCountdown {
      challengeViewModel.getRandomChallenge()
    }
  }

  private fun nextBottleRotation(): Float {
    val randomExtra = Random.nextInt(0, 361)
    val fullSpins = 3 * 360
    return bottleRotation + fullSpins + randomExtra
  }

  private fun saveBottleRotation(rotation: Float) {
    bottleRotation = rotation
  }

  private fun startCountdown(onComplete: () -> Unit) {

    binding.tvCountdown.visibility = View.VISIBLE

    countdownTimer?.cancel()

    countdownTimer =
      object : CountDownTimer(4000L, 1000L) {

        override fun onTick(millisUntilFinished: Long) {
          val seconds = millisUntilFinished / 1000
          binding.tvCountdown.text = seconds.toString()
        }

        override fun onFinish() {
          binding.tvCountdown.text = "0"

          binding.tvCountdown.visibility = View.GONE

          onComplete()
        }
      }.start()
  }

  private fun toggleAudio() {
    isAudioEnabled = !isAudioEnabled

    if (!isAudioEnabled) {
      backgroundPlayer?.pause()
      stopSpinSound()
    } else {
      if (!isSpinning) backgroundPlayer?.start()
    }

    binding.btnAudio.setImageResource(
        if (isAudioEnabled) R.drawable.ic_volume_on else R.drawable.ic_volume_off
    )
  }

  private fun playSpinSound() {
    spinPlayer?.release()
    spinPlayer =
        MediaPlayer.create(requireContext(), R.raw.bottle_spin).apply {
          seekTo(0)
          start()
        }
  }

  private fun stopSpinSound() {
    spinPlayer?.apply {
      if (isPlaying) {
        pause()
        seekTo(0)
      }
    }
  }

  private fun rateApp() {
    backgroundPlayer?.pause()
    val intent = Intent(Intent.ACTION_VIEW, getString(R.string.play_store_url).toUri())
    startActivity(intent)
  }

  private fun shareApp() {
    backgroundPlayer?.pause()

    val intent =
        Intent(Intent.ACTION_SEND).apply {
          type = "text/plain"
          putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_message))
        }

    val chooser = Intent.createChooser(intent, getString(R.string.share_app_title))
    startActivity(chooser)
  }

  private fun cleanup() {
    countdownTimer?.cancel()
    countdownTimer = null

    backgroundPlayer?.stop()
    backgroundPlayer?.release()
    backgroundPlayer = null

    spinPlayer?.stop()
    spinPlayer?.release()
    spinPlayer = null
  }
  private fun observerNoChallenges() {

    challengeViewModel.showNoChallenges.observe(viewLifecycleOwner) { show ->

      if (show) {
        showChallengeDialog(null)
        challengeViewModel.clearNoChallenges()
      }

    }
  }
  private fun observerViewModel() {
    observerPokemonList()
    observerRandomChallenge()
    observerNoChallenges()
  }

  private fun observerRandomChallenge() {

    challengeViewModel.randomChallenge.observe(viewLifecycleOwner) { challenge ->

      if (challenge != null) {
        showChallengeDialog(challenge)
        challengeViewModel.clearRandomChallenge()
      }

    }
  }

  private fun observerPokemonList() {

    challengeViewModel.getPokemons()

    challengeViewModel.pokemonList.observe(viewLifecycleOwner) { lista ->
      pokemonList = lista
    }
  }
}
