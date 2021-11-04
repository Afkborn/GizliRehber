package com.bilgehankalay.gizlirehber.Databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bilgehankalay.gizlirehber.DAO.KisiDAO
import com.bilgehankalay.gizlirehber.Model.KisiModel

@Database(entities = [KisiModel::class],version = 1)
abstract class KisilerDatabase : RoomDatabase() {
    abstract  fun kisiDAO() : KisiDAO

    companion object{
        private var INSTANCE : KisilerDatabase? = null

        fun getirKisilerDatabase(context: Context) : KisilerDatabase?{
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context,
                    KisilerDatabase::class.java,
                    "kisiler.db"
                ).allowMainThreadQueries().build()
            }
            return INSTANCE

        }
    }

}