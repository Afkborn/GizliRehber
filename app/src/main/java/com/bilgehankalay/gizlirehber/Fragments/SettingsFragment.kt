package com.bilgehankalay.gizlirehber.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.bilgehankalay.gizlirehber.R
import com.bilgehankalay.gizlirehber.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {
    private lateinit var binding : FragmentSettingsBinding
    private var themeType = true
    private var darkTheme = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        themeType = readThemeType()
        darkTheme = readDarkTheme()

        loadTheme()

        if (themeType) loadDefault() else loadCustom()



        binding.settingsDarkTheme.isChecked = darkTheme



        binding.apply {

            //RADIO GROUP LISTENER
            binding.settingsThemeRadioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { _, checkedId -> // checkedId is the RadioButton selected
                if (checkedId == R.id.settings_default_theme_rb){
                    loadDefault()
                    writeThemeType(true)
                }
                else if (checkedId == R.id.settings_custom_theme_rb){
                    loadCustom()
                    writeThemeType(false)
                }
            })


            //SWITCH LISTENER
            binding.settingsDarkTheme.setOnCheckedChangeListener { _, stateBool ->
                if (stateBool) {
                    //DARK
                    writeDarkTheme(true)
                } else {
                    //DEFAULT
                    writeDarkTheme(false)
                }
                loadTheme()
            }


        }


    }


    private fun loadDefault(){
        binding.apply {
            settingsDefaultThemeRb.isChecked = true
            settingsCustomThemeRb.isChecked = false
            settingsCustomThemeLayout.visibility = View.GONE
            settingsDefaultThemeLayout.visibility = View.VISIBLE
        }
    }

    private fun loadTheme(){
        darkTheme = readDarkTheme()
        if (darkTheme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun loadCustom(){
        binding.apply {
            settingsDefaultThemeRb.isChecked = false
            settingsCustomThemeRb.isChecked = true
            settingsCustomThemeLayout.visibility = View.VISIBLE
            settingsDefaultThemeLayout.visibility = View.GONE
        }
    }


    fun writeThemeType(type : Boolean) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putBoolean("theme_type", type)
            apply()
        }
    }
    fun writeDarkTheme(type : Boolean){
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putBoolean("dark_theme", type)
            apply()
        }
    }

    fun readThemeType() : Boolean{
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return true
        return sharedPref.getBoolean("theme_type",true)
    }
    fun readDarkTheme() : Boolean{
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return true
        return sharedPref.getBoolean("dark_theme",false)
    }




}