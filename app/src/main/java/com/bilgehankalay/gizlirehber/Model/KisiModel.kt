package com.bilgehankalay.gizlirehber.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "kisiler")
data class KisiModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id : Int = 0,

    @ColumnInfo(name="Ad")
    var ad :String,

    @ColumnInfo(name = "Soyad")
    var soyad : String,

    @ColumnInfo(name="TelefonNumarasi")
    var telefonNumarasi : String,

    @ColumnInfo(name = "Aciklama")
    var aciklama : String?



) : Serializable
