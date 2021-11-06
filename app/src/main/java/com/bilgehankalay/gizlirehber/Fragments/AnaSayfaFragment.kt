package com.bilgehankalay.gizlirehber.Fragments

import android.os.Bundle

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
import com.bilgehankalay.gizlirehber.databinding.FragmentAnaSayfaBinding
import android.content.Intent
import android.net.Uri
import android.app.AlertDialog;
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bilgehankalay.gizlirehber.R
import com.google.android.material.snackbar.Snackbar
import android.provider.ContactsContract

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult

import com.bilgehankalay.gizlirehber.Activitys.MainActivity
import timber.log.Timber


class AnaSayfaFragment : Fragment() {
    private lateinit var binding : FragmentAnaSayfaBinding
    private lateinit var kisilerDb : KisilerDatabase
    private lateinit var kisilerList : List<KisiModel?>
    private var wpInstalled : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        kisilerDb = KisilerDatabase.getirKisilerDatabase(requireContext())!!
        kisilerList = kisilerDb.kisiDAO().tumKisiler()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnaSayfaBinding.inflate(inflater,container,false)

        wpInstalled = checkPackage("com.whatsapp")

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tumKisileriGetir()

        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val silinecekKisiModel = kisilerList[viewHolder.adapterPosition]
                if (silinecekKisiModel != null){
                    kisiSilClick(silinecekKisiModel)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallBack)
        itemTouchHelper.attachToRecyclerView(binding.rehberRecyclerView)


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
                val kisilerAdapter = RehberRecyclerAdapter(kisiList = kisilerList,wpInstalled)

                //kisilerAdapter.onDeleteClick = ::kisiSilClick
                kisilerAdapter.onItemClick = ::secilenKisiOnClick

                kisilerAdapter.onCallClick = ::kisiAraClick
                kisilerAdapter.onWhatsappCallClick = ::kisiWhatsappAraClick

                kisilerAdapter.onSmsClick = ::kisiSmsClick
                kisilerAdapter.onWhatsappSmsClick = :: kisiWhatsappSmsClick

                rehberRecyclerView.adapter = kisilerAdapter
                rehberRecyclerView.layoutManager = LinearLayoutManager(requireContext(),
                    LinearLayoutManager.VERTICAL,false)
                rehberRecyclerView.setHasFixedSize(true)
            }
        }
    }

    fun kisiSilClick(gelenKisi : KisiModel){
        val uyariMesaji = AlertDialog.Builder(requireContext())
        uyariMesaji.setMessage("${gelenKisi.ad} ${gelenKisi.soyad}'ı silmek istediğinden emin misin?")
        uyariMesaji.setPositiveButton("Evet", DialogInterface.OnClickListener { _, _ ->

            kisilerDb.kisiDAO().kisiSil(gelenKisi)
            kisilerList = kisilerDb.kisiDAO().tumKisiler()
            tumKisileriGetir()

            Snackbar.make(requireView(),"${gelenKisi.ad} ${gelenKisi.soyad} başarılı bir şekilde silindi.",1000).show()
        })
        uyariMesaji.setNegativeButton("Hayır",null)
        uyariMesaji.show()

    }

    fun secilenKisiOnClick(gelenKisi : KisiModel){
        val gecisAction = AnaSayfaFragmentDirections.anasayfaToKisidetay(gelenKisi)
        findNavController().navigate(gecisAction)

    }

    fun kisiAraClick(gelenKisi:KisiModel){
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + gelenKisi.fullTelefonNumarasi))
        startActivity(intent)
    }

    fun kisiWhatsappAraClick(gelenKisi:KisiModel){
        if(wpInstalled){
            //TODO WHATSAPP CALL
        }
        else{
            Toast.makeText(requireContext(),R.string.install_Whatsapp_first,Toast.LENGTH_LONG).show()
        }
    }

    fun kisiSmsClick(gelenKisi : KisiModel){
        //TODO SMS
        val smsNumber = String.format(
            "smsto: %s",
            gelenKisi.fullTelefonNumarasi
        )
        val sms = ""
        val smsIntent = Intent(Intent.ACTION_SENDTO)
        smsIntent.data = Uri.parse(smsNumber)
        smsIntent.putExtra("sms_body",sms)

        startActivity(smsIntent)
    }

    fun kisiWhatsappSmsClick(gelenKisi : KisiModel){
        //TODO WHATSAPP SMS
        //TODO SMS ATILACAK KISI REHBERDE EKLI DEGILSE GONDERMIYOR. KISI WPDE EKLI DEGILSE WP CALL VE WP SMS BUTTON INVISIBLE
        if (wpInstalled){
            val uri = Uri.parse("smsto:${gelenKisi.fullTelefonNumarasi}")
            val i = Intent(Intent.ACTION_SENDTO, uri)
            i.setPackage("com.whatsapp")
            startActivity(i)
        }
        else{
            Toast.makeText(requireContext(),R.string.install_Whatsapp_first,Toast.LENGTH_LONG).show()
        }

    }

    fun checkPackage(packageName : String) : Boolean{
        val isInstalledApp: Boolean = try
        {
            context?.packageManager?.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        }
        catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return isInstalledApp
    }
}