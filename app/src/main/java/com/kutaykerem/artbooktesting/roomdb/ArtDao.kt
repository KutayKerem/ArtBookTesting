package com.kutaykerem.artbooktesting.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kutaykerem.artbooktesting.roomdb.Art

@Dao
interface ArtDao {


    // id çakışırsa üstüne yaz değiştir
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArt(art: Art)

    @Delete
    suspend fun deleteArt(art: Art)


    // Live data zaten asenkron çalıştığı için suspend yapmaya gerek yok
    @Query("SELECT * FROM arts")
    fun observeArts(): LiveData<List<Art>>



}