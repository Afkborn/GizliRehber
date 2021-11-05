package com.bilgehankalay.gizlirehber.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bilgehankalay.gizlirehber.R
import com.bilgehankalay.gizlirehber.databinding.FragmentLogBinding


class LogFragment : Fragment() {
    private lateinit var binding : FragmentLogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLogBinding.inflate(inflater,container,false)
        return binding.root
    }


}