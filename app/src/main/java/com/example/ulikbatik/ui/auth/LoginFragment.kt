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
import com.example.ulikbatik.ui.MainActivity

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

                authViewModel.login(email, password).observe(viewLifecycleOwner){
                    if (it != null){
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
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