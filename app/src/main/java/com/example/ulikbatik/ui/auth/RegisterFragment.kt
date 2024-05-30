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
import com.example.ulikbatik.databinding.FragmentRegisterBinding
import com.example.ulikbatik.ui.dashboard.DashboardActivity
import com.example.ulikbatik.utils.Helper

class RegisterFragment : Fragment() {
    private lateinit var binding : FragmentRegisterBinding
    private val authViewModel: AuthViewModel by activityViewModels {
        AuthViewModelFactory.getInstance(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        onBack()
    }

    private fun setupView() {
        binding.apply{

            registerBtn.setOnClickListener{
                val username = usernameEdit.text.toString()
                val email = emailEdit.text.toString()
                val password = passwordEdit.text.toString()
                val confirmPassword = confirmPasswordEdit.text.toString()

                if(validateData(username, email, password, confirmPassword)){
                    authViewModel.register(username,email, password, confirmPassword).observe(requireActivity()){
                        if (it.status) {
                            Helper.showToast(requireContext(), it.message)
                            val loginFragment = LoginFragment().apply {
                                arguments = Bundle().apply {
                                    putString("email", email)
                                    putString("password", password)
                                }
                            }
                            parentFragmentManager.beginTransaction().apply {
                                replace(R.id.authContainer, loginFragment)
                                addToBackStack(null)
                            }.commit()
                        } else {
                            when (it.message) {
                                "400" -> {
                                    Helper.showToast(requireContext(), requireContext().getString(R.string.error_invalid_input))
                                }
                            }
                        }
                    }
                }

                loginBtn.setOnClickListener {
                    parentFragmentManager.beginTransaction().apply {
                        replace(R.id.authContainer, LoginFragment())
                        addToBackStack(null)
                    }.commit()
                }
            }
        }
    }

    private fun validateData(username: String, email: String, password: String, confirmPassword: String): Boolean {
        var isValid = true

        binding.apply{
            when{
                email.isEmpty() -> {
                    emailTextInputLayout.error = getString(R.string.error_empty)
                    isValid = false
                }
                !Helper.validateEmail(email) -> {
                    emailTextInputLayout.error = getString(R.string.error_invalid_input)
                    isValid = false
                }
                else -> {
                    emailTextInputLayout.error = null
                }
            }

            when {
                password.isEmpty() -> {
                    passwordTextInputLayout.error = getString(R.string.error_empty)
                    isValid = false
                }
                !Helper.validatePassword(password) -> {
                    passwordTextInputLayout.error = getString(R.string.password_error_8)
                    isValid = false
                }
                else -> {
                    passwordTextInputLayout.error = null
                }
            }

            when {
                username.isEmpty() ->{
                    usernameTextInputLayout.error = getString(R.string.error_empty)
                    isValid = false
                }
                else -> {
                    usernameTextInputLayout.error = null
                }
            }

            when {
                confirmPassword.isEmpty() -> {
                    confirmPasswordTextInputLayout.error = getString(R.string.error_empty)
                    isValid = false
                }
                !Helper.confirmPassword(password, confirmPassword) -> {
                    confirmPasswordTextInputLayout.error = getString(R.string.error_match)
                    isValid = false
                }
                else -> {
                    confirmPasswordTextInputLayout.error = null
                }
            }
        }

        return isValid
    }

    private fun onBack() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }
}