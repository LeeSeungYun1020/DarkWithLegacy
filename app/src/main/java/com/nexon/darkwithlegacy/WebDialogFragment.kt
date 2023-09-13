package com.nexon.darkwithlegacy

import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nexon.darkwithlegacy.databinding.FragmentWebBinding

class WebDialogFragment : DialogFragment() {
    private var _binding: FragmentWebBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWebBinding.inflate(inflater, container, false)
        binding.webView.loadUrl("https://web.dev/")
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}