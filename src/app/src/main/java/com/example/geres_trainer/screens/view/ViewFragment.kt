package com.example.geres_trainer.screens.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.geres_trainer.R
import com.example.geres_trainer.database.TranslationDB
import com.example.geres_trainer.databinding.ViewFragmentBinding
import kotlinx.android.synthetic.main.view_fragment.*

class ViewFragment : Fragment () {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: ViewFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.view_fragment, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = TranslationDB.getInstance(application).translationDBDao

        val viewModelFactory = ViewFragmentViewModelFactory(dataSource)

        val viewFragmentViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(ViewFragmentViewModel::class.java)

        binding.viewFragmentViewModel = viewFragmentViewModel



        binding.setLifecycleOwner(this)


        return binding.root
    }



}