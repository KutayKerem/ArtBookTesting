package com.kutaykerem.artbooktesting.repo

import androidx.lifecycle.LiveData
import com.kutaykerem.artbooktesting.model.ImageResponse
import com.kutaykerem.artbooktesting.roomdb.Art
import com.kutaykerem.artbooktesting.util.Resource

interface ArtReposityInterface {

    suspend fun insertArt(art:Art)

    suspend fun deleteArt(art:Art)

    fun getArt():LiveData<List<Art>>

    suspend fun searchImage(imageString:String):  Resource<ImageResponse>


}
