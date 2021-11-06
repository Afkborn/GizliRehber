package com.bilgehankalay.gizlirehber.DAO



import androidx.room.*
import com.bilgehankalay.gizlirehber.Model.LogModel

@Dao
interface LogDAO {
    @Insert
    fun logEkle(log :LogModel)

    @Delete
    fun logDelete(log : LogModel)

    @Update
    fun logUpdate(log : LogModel)

    @Query("SELECT * from logs")
    fun tumLog() : List<LogModel?>


    @Query("SELECT * from logs WHERE id = :id ")
    fun getLogWithId(id : Int) : LogModel?
}