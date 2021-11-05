package com.bilgehankalay.gizlirehber.Fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.bilgehankalay.gizlirehber.Databases.KisilerDatabase
import com.bilgehankalay.gizlirehber.Model.KisiModel
import com.bilgehankalay.gizlirehber.R
import com.bilgehankalay.gizlirehber.databinding.FragmentKisiEkleBinding


class KisiEkleFragment : Fragment() {
    private lateinit var binding : FragmentKisiEkleBinding
    private lateinit var kisilerDb : KisilerDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kisilerDb = KisilerDatabase.getirKisilerDatabase(requireContext())!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKisiEkleBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.yapilacakIslemSecenek,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                kisiEkleYapilacakIslemSpinner.adapter = adapter
            }


            kisiEkleEkleButton.setOnClickListener {
                val kisiAdInput = kisiEkleAdText.text.toString()
                val kisiSoyadInput = kisiEkleSoyadText.text.toString()
                val kisiTelNoInput = kisiEkleTelNoText.text.toString()
                //TODO kisiTelNoInput kontrol et
                val kisiAciklamaInput = kisiEkleAciklama.text.toString()
                val kisiYapilacakIslemId = kisiEkleYapilacakIslemSpinner.selectedItemId.toInt()
                kisilerDb.kisiDAO().kisiEkle(
                    KisiModel(ad = kisiAdInput,
                        soyad = kisiSoyadInput,
                        telefonNumarasi = kisiTelNoInput,
                        aciklama = kisiAciklamaInput,
                        yapilacakIslem = kisiYapilacakIslemId)
                )
                findNavController().navigate(R.id.kisiEkle_to_anasayfa)
            }
        }
    }
}