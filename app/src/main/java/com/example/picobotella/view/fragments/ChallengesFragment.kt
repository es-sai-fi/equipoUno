package com.example.picobotella.view.fragments

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.picobotella.R
import com.example.picobotella.databinding.DialogChallengeFormBinding
import com.example.picobotella.databinding.DialogDeleteChallengeBinding
import com.example.picobotella.databinding.FragmentChallengesBinding
import com.example.picobotella.model.Challenge
import com.example.picobotella.view.adapter.ChallengeAdapter
import com.example.picobotella.viewModel.ChallengeViewModel

class ChallengesFragment : Fragment() {
    private var _binding: FragmentChallengesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChallengeViewModel by viewModels()
    private lateinit var challengeAdapter: ChallengeAdapter
    private var activeDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentChallengesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        setupRecyclerView()
        observeChallenges()
        setupActions()
    }

    override fun onDestroyView() {
        activeDialog?.dismiss()
        activeDialog = null
        binding.recyclerview.adapter = null
        _binding = null
        super.onDestroyView()
    }

    private fun setupRecyclerView() {
        challengeAdapter = ChallengeAdapter(
            onEdit = ::showEditDialog,
            onDelete = ::showDeleteDialog,
        )
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = challengeAdapter
            setHasFixedSize(false)
        }
    }

    private fun observeChallenges() {
        viewModel.challenges.observe(viewLifecycleOwner) { challenges ->
            challengeAdapter.submitList(challenges)
        }
    }

    private fun setupActions() {
        binding.btnChallengesBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnAddChallenge.setOnClickListener {
            showChallengeForm()
        }
    }

    private fun showEditDialog(challenge: Challenge) {
        showChallengeForm(challenge)
    }

    private fun showChallengeForm(challenge: Challenge? = null) {
        val formBinding = DialogChallengeFormBinding.inflate(layoutInflater)
        val dialog = createDialog(formBinding.root)
        val isEditing = challenge != null

        formBinding.tvDialogTitle.setText(
            if (isEditing) R.string.edit_challenge else R.string.add_challenge,
        )
        formBinding.etChallenge.setText(challenge?.description.orEmpty())
        formBinding.etChallenge.setSelection(formBinding.etChallenge.text?.length ?: 0)

        fun updateSaveButton() {
            val hasDescription = !formBinding.etChallenge.text.isNullOrBlank()
            formBinding.btnSave.isEnabled = hasDescription
            val color = if (hasDescription) {
                R.color.challenge_orange
            } else {
                R.color.challenge_button_disabled
            }
            formBinding.btnSave.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), color),
            )
        }

        formBinding.etChallenge.doAfterTextChanged {
            updateSaveButton()
        }
        formBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        formBinding.btnSave.setOnClickListener {
            val description = formBinding.etChallenge.text?.toString()?.trim().orEmpty()
            if (description.isEmpty()) return@setOnClickListener

            if (challenge == null) {
                viewModel.addChallenge(description)
            } else {
                viewModel.updateChallenge(challenge, description)
            }
            dialog.dismiss()
        }

        updateSaveButton()
        showDialog(dialog)
        formBinding.etChallenge.requestFocus()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    private fun showDeleteDialog(challenge: Challenge) {
        val deleteBinding = DialogDeleteChallengeBinding.inflate(layoutInflater)
        val dialog = createDialog(deleteBinding.root)

        deleteBinding.tvChallengeDescription.text = challenge.description
        deleteBinding.btnNo.setOnClickListener {
            dialog.dismiss()
        }
        deleteBinding.btnYes.setOnClickListener {
            viewModel.deleteChallenge(challenge)
            dialog.dismiss()
        }

        showDialog(dialog)
    }

    private fun createDialog(contentView: View): Dialog {
        activeDialog?.dismiss()
        return Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(contentView)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setOnDismissListener {
                if (activeDialog === this) activeDialog = null
            }
        }
    }

    private fun showDialog(dialog: Dialog) {
        activeDialog = dialog
        dialog.show()
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val width = (resources.displayMetrics.widthPixels * 0.9f).toInt()
            setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        }
    }
}
