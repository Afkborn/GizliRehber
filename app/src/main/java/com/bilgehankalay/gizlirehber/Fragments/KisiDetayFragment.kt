package com.bilgehankalay.gizlirehber.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bilgehankalay.gizlirehber.Databases.KisilerDatabase
import com.bilgehankalay.gizlirehber.Model.KisiModel
import com.bilgehankalay.gizlirehber.R
import com.bilgehankalay.gizlirehber.databinding.FragmentKisiDetayBinding


class KisiDetayFragment : Fragment() {
    private lateinit var binding : FragmentKisiDetayBinding
    private val args : KisiDetayFragmentArgs by navArgs()
    private lateinit var kisiDb : KisilerDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kisiDb = KisilerDatabase.getirKisilerDatabase(requireContext())!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKisiDetayBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val kisi = args.kisi
        binding.apply {

            kisiDetayAd.setText(kisi.ad)
            kisiDetaySoyad.setText(kisi.soyad)
            kisiDetayTelefonNumarasi.setText(kisi.telefonNumarasi)
            kisiDetayAciklama.setText(kisi.aciklama)
            kisiDetayGuncelleButton.setOnClickListener {
                val kisiAdInput = kisiDetayAd.text.toString()
                val kisiSoyAdInput = kisiDetaySoyad.text.toString()
                val kisiTelefonNumarasiInput = kisiDetayTelefonNumarasi.text.toString()
                val kisiAciklamaInput = kisiDetayAciklama.text.toString()
                val guncellenmisKisi = KisiModel(
                    id = kisi.id,
                    ad = kisiAdInput,
                    soyad = kisiSoyAdInput,
                    telefonNumarasi = kisiTelefonNumarasiInput,
                    aciklama = kisiAciklamaInput

                )
                kisiGuncelle(guncellenmisKisi)

            }
        }
    }

    fun kisiGuncelle(guncellenecekKisi : KisiModel){
        kisiDb.kisiDAO().kisiGuncelle(guncellenecekKisi)
        Toast.makeText(requireContext(),"Kişi başarıyla güncellendi", Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.kisiDetay_to_anasayfa)
    }

}