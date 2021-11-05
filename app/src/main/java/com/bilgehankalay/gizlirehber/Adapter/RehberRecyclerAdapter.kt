package com.bilgehankalay.gizlirehber.Adapter


import android.provider.Settings.System.getString
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bilgehankalay.gizlirehber.Model.KisiModel
import com.bilgehankalay.gizlirehber.R
import com.bilgehankalay.gizlirehber.databinding.RehberCardTasarimBinding



class RehberRecyclerAdapter (private var kisiList: List<KisiModel?>) : RecyclerView.Adapter<RehberRecyclerAdapter.RehberCardTasarim>() {
    var onDeleteClick : (KisiModel) -> Unit = {}
    var onItemClick : (KisiModel) -> Unit = {}
    var onCallClick : (KisiModel) -> Unit = {}
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

                kisiAdTextView.text =  kisi.ad + " " + kisi.soyad//"${kisi.ad} ${kisi.soyad}"
                kisiTelnoTextView.text = kisi.telefonNumarasi

                kisiSilButton.setOnClickListener {
                    onDeleteClick(kisi)
                }
                kisiAraButton.setOnClickListener {
                    onCallClick(kisi)

                }
            }

        }
    }

    override fun getItemCount(): Int {
        return kisiList.size
    }

}