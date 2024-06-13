package com.example.ulikbatik.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.example.ulikbatik.R
import com.example.ulikbatik.databinding.FragmentLoginBinding
import com.example.ulikbatik.ui.dashboard.DashboardActivity
import com.example.ulikbatik.ui.factory.AuthViewModelFactory
import com.example.ulikbatik.utils.helper.ValidatorAuthHelper

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
        authViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }

        val email = arguments?.getString("email")
        val password = arguments?.getString("password")

        binding.emailEdit.setText(email)
        binding.passwordEdit.setText(password)

        binding.apply {
            loginBtn.setOnClickListener {
                val emailT = emailEdit.text.toString()
                val passwordT = passwordEdit.text.toString()

                if (validateData(emailT, passwordT)) {
                    authViewModel.login(emailT, passwordT).observe(requireActivity()) {
                        if (it.status) {
                            startActivity(Intent(requireActivity(), DashboardActivity::class.java))
                            requireActivity().finish()
                        } else {
                            handlePostError(it.message.toInt())
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

    private fun validateData(email: String, password: String): Boolean {
        var isValid = true

        binding.apply {
            when {
                email.isEmpty() -> {
                    emailTextInputLayout.error = getString(R.string.email_error_empty)
                    isValid = false
                }

                !ValidatorAuthHelper.validateEmail(email) -> {
                    emailTextInputLayout.error = getString(R.string.error_invalid_input)
                    isValid = false
                }

                else -> {
                    emailTextInputLayout.error = null
                }
            }

            when {
                password.isEmpty() -> {
                    passwordTextInputLayout.error = getString(R.string.password_error_empty)
                    isValid = false
                }

                !ValidatorAuthHelper.validatePassword(password) -> {
                    passwordTextInputLayout.error = getString(R.string.error_invalid_input)
                    isValid = false
                }

                else -> {
                    passwordTextInputLayout.error = null
                }
            }
        }
        return isValid
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

    private fun handlePostError(error: Int) {
        when (error) {
            400 -> ValidatorAuthHelper.showToast(
                requireContext(),
                getString(R.string.error_invalid_input)
            )

            401 -> ValidatorAuthHelper.showToast(
                requireContext(),
                getString(R.string.error_unauthorized_401)
            )

            500 -> ValidatorAuthHelper.showToast(
                requireContext(),
                getString(R.string.error_server_500)
            )

            503 -> ValidatorAuthHelper.showToast(
                requireContext(),
                getString(R.string.error_server_500)
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}