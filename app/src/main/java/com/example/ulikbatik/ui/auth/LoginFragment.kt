package com.example.ulikbatik.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.registerBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.authContainer, RegisterFragment())
                addToBackStack(null)
            }.commit()
        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}