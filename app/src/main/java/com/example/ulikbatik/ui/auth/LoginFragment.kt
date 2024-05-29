package com.example.ulikbatik.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.ThemedSpinnerAdapter
import androidx.fragment.app.activityViewModels
import com.example.ulikbatik.R
import com.example.ulikbatik.databinding.FragmentLoginBinding
import com.example.ulikbatik.ui.dashboard.DashboardActivity
import com.example.ulikbatik.utils.Helper

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

                if (validateData(email, password)) {
                    authViewModel.login(email, password).observe(requireActivity()) {
                        if (it.status) {
                            Helper.showToast(requireContext(), it.message)

                            startActivity(Intent(requireActivity(), DashboardActivity::class.java))
                            requireActivity().finish()
                        } else  {
                            when(it.message){
                                "400" -> {
                                    Helper.showToast(requireContext(),
                                        requireContext().getString(R.string.error_invalid_data))
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

    private fun validateData(email: String, password: String): Boolean {
        var isValid = true

        binding.apply {
            when {
                email.isEmpty() -> {
                    emailTextInputLayout.error = getString(R.string.email_error_empty)
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
                    passwordTextInputLayout.error = getString(R.string.password_error_empty)
                    isValid = false
                }
                !Helper.validatePassword(password) -> {
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

    companion object {
        fun newInstance() = LoginFragment()
    }
}