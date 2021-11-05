package com.bilgehankalay.gizlirehber.DAO

import androidx.room.*
import com.bilgehankalay.gizlirehber.Model.KisiModel

@Dao
interface KisiDAO {
    @Insert
    fun kisiEkle(kisi : KisiModel)

    @Update
    fun kisiGuncelle(kisi : KisiModel)

    @Delete
    fun kisiSil(kisi : KisiModel)

    @Query("SELECT * from kisiler")
    fun tumKisiler() : List<KisiModel?>

    @Query("SELECT * from kisiler WHERE TelefonNumarasi= :phoneNumber")
    fun telefonNoIleKisiGetir(phoneNumber : String) : KisiModel?


}