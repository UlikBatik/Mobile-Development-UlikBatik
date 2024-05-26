package com.example.ulikbatik.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.ulikbatik.R
import com.example.ulikbatik.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        onBack()
    }

    private fun setupView() {
        binding.loginBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.authContainer, RegisterFragment())
                addToBackStack(null)
            }.commit()
        }
    }

    private fun onBack() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}