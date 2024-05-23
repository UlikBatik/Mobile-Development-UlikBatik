package com.example.ulikbatik.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ulikbatik.R
import com.example.ulikbatik.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private lateinit var binding : FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.authContainer, LoginFragment())
                addToBackStack(null)
            }.commit()
        }
    }
}