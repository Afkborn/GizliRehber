package com.bilgehankalay.gizlirehber.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bilgehankalay.gizlirehber.Adapter.RehberRecyclerAdapter
import com.bilgehankalay.gizlirehber.Databases.KisilerDatabase
import com.bilgehankalay.gizlirehber.Model.KisiModel
import com.bilgehankalay.gizlirehber.R
import com.bilgehankalay.gizlirehber.commons.CONSTANT_KISILER_LIST
import com.bilgehankalay.gizlirehber.databinding.FragmentAnaSayfaBinding


class AnaSayfaFragment : Fragment() {
    private lateinit var binding : FragmentAnaSayfaBinding
    private lateinit var kisilerDb : KisilerDatabase
    private lateinit var kisilerList : List<KisiModel?>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        kisilerDb = KisilerDatabase.getirKisilerDatabase(requireContext())!!
        kisilerList = kisilerDb.kisiDAO().tumKisiler()
        CONSTANT_KISILER_LIST = kisilerDb.kisiDAO().tumKisiler()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnaSayfaBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tumKisileriGetir()

        binding.apply {
            kisiEkleFab.setOnClickListener {
                findNavController().navigate(R.id.anasayfa_to_kisiekle)

            }
        }
    }
    fun tumKisileriGetir(){

        binding.apply {

            if (kisilerList.isEmpty()){
                Toast.makeText(requireContext(),"Kimse eklenmemiş", Toast.LENGTH_LONG).show()
            }
            else{
                val kisilerAdapter = RehberRecyclerAdapter(kisiList = kisilerList)
                kisilerAdapter.onDeleteClick = ::kisiDeleteClick
                kisilerAdapter.onItemClick = ::secilenKisiOnClick
                rehberRecyclerView.adapter = kisilerAdapter
                rehberRecyclerView.layoutManager = LinearLayoutManager(requireContext(),
                    LinearLayoutManager.VERTICAL,false)
                rehberRecyclerView.setHasFixedSize(true)
            }
        }
    }

    fun kisiDeleteClick(gelenKisi : KisiModel){
        kisilerDb.kisiDAO().kisiSil(gelenKisi)
        kisilerList = kisilerDb.kisiDAO().tumKisiler()
        tumKisileriGetir()
    }
    fun secilenKisiOnClick(gelenKisi : KisiModel){
        val gecisAction = AnaSayfaFragmentDirections.anasayfaToKisidetay(gelenKisi)
        findNavController().navigate(gecisAction)

    }




}