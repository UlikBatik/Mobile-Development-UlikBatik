package com.example.ulikbatik.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.example.ulikbatik.R
import com.example.ulikbatik.databinding.FragmentLoginBinding
import com.example.ulikbatik.ui.dashboard.DashboardActivity

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val authViewModel: AuthViewModel by activityViewModels {
        AuthViewModelFactory.getInstance(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        onBack()
    }

    private fun setupView() {
        binding.apply {
            loginBtn.setOnClickListener {
                val email = emailEdit.text.toString()
                val password = passwordEdit.text.toString()
                emailTextInputLayout.error = null
                passwordTextInputLayout.error = null
                if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailTextInputLayout.error = getString(R.string.email_error_empty)
                }
                if (password.isEmpty() || password.length < 8) {
                    passwordTextInputLayout.error = getString(R.string.password_error_empty)
                }
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    authViewModel.login(email, password).observe(requireActivity()) {
                        if (it.status) {
                            val intent =
                                Intent(requireActivity(), DashboardActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        } else {
                            when (it.message) {
                                "400" -> {
                                    emailTextInputLayout.error =
                                        getString(R.string.error_invalid_input)
                                    passwordTextInputLayout.error =
                                        getString(R.string.error_invalid_input)
                                }
                            }
                        }
                    }
                }
            }
            registerBtn.setOnClickListener {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.authContainer, RegisterFragment())
                    addToBackStack(null)
                }.commit()
            }
        }
    }

    private fun onBack() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}