package com.bilgehankalay.gizlirehber.Adapter


import android.content.pm.PackageManager
import android.provider.Settings.System.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bilgehankalay.gizlirehber.Model.KisiModel
import com.bilgehankalay.gizlirehber.R
import com.bilgehankalay.gizlirehber.databinding.RehberCardTasarimBinding



class RehberRecyclerAdapter (private var kisiList: List<KisiModel?>, val wpInstalled : Boolean ) : RecyclerView.Adapter<RehberRecyclerAdapter.RehberCardTasarim>() {
    var onItemClick : (KisiModel) -> Unit = {}

    var onCallClick : (KisiModel) -> Unit = {}
    var onWhatsappCallClick : (KisiModel) -> Unit = {}

    var onSmsClick : (KisiModel) -> Unit  = {}
    var onWhatsappSmsClick : (KisiModel) -> Unit = {}
    class RehberCardTasarim(val rehberCardTasarimBinding: RehberCardTasarimBinding) : RecyclerView.ViewHolder(rehberCardTasarimBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RehberCardTasarim {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rehberCardTasarimBinding = RehberCardTasarimBinding.inflate(layoutInflater,parent,false)
        return RehberCardTasarim(rehberCardTasarimBinding)
    }

    override fun onBindViewHolder(holder: RehberCardTasarim, position: Int) {
        val kisi = kisiList[position]
        holder.rehberCardTasarimBinding.root.setOnClickListener {
            if (kisi !=null){
                onItemClick(kisi)
            }
        }

        holder.rehberCardTasarimBinding.apply {
            if (kisi!= null){
                var clickCallButton = false
                var clickSmsButton = false

                //CALL
                kisiCepAraButton.visibility = View.GONE
                kisiWpAraButton.visibility = View.GONE

                //MESSAGE
                kisiWpSmsButton.visibility = View.GONE
                kisiCepSmsButton.visibility = View.GONE

                kisiAdTextView.text =  kisi.ad + " " + kisi.soyad//"${kisi.ad} ${kisi.soyad}"
                kisiTelnoTextView.text =  "+" + kisi.ulkeKodu + " " + kisi.telefonNumarasi

                //CALL DESIGN
                kisiAraButton.setOnClickListener {
                    if (!clickCallButton ){
                        if (wpInstalled){
                            kisiWpAraButton.visibility = View.VISIBLE
                        }
                        kisiCepAraButton.visibility = View.VISIBLE

                    }
                    else{
                        kisiCepAraButton.visibility = View.GONE
                        kisiWpAraButton.visibility = View.GONE
                    }
                    clickCallButton = !clickCallButton
                }

                //MESSAGE DESIGN
                kisiSmsButton.setOnClickListener {
                    if (!clickSmsButton){
                        if (wpInstalled){
                            kisiWpSmsButton.visibility = View.VISIBLE
                        }
                        kisiCepSmsButton.visibility = View.VISIBLE
                    }
                    else{
                        kisiCepSmsButton.visibility = View.GONE
                        kisiWpSmsButton.visibility = View.GONE
                    }
                    clickSmsButton = !clickSmsButton
                }

                //CALL RETURN
                kisiCepAraButton.setOnClickListener {
                    onCallClick(kisi)
                }
                kisiWpAraButton.setOnClickListener {
                    onWhatsappCallClick(kisi)
                }

                //MESSAGE RETURN
                kisiWpSmsButton.setOnClickListener {
                    onWhatsappSmsClick(kisi)
                }
                kisiCepSmsButton.setOnClickListener {
                    onSmsClick(kisi)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return kisiList.size
    }

}