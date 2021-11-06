package com.bilgehankalay.gizlirehber.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "logs")
data class LogModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id : Int = 0,

    //cevapsız, açıldı, reddedildi vb.
    @ColumnInfo(name = "durum")
    val durum : String,

    @ColumnInfo(name="zaman")
    val zaman : Int,


    @ColumnInfo(name="tarihOlusturulma")
    val tarihOlusturulma : String,

    @ColumnInfo(name="telefonNumarasi")
    val telefonNumarasi : String,





) : Serializable
