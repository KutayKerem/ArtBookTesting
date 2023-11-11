package com.kutaykerem.artbooktesting.repo

import androidx.lifecycle.LiveData
import com.kutaykerem.artbooktesting.api.RetrofitAPI
import com.kutaykerem.artbooktesting.model.ImageResponse
import com.kutaykerem.artbooktesting.roomdb.Art
import com.kutaykerem.artbooktesting.roomdb.ArtDao
import com.kutaykerem.artbooktesting.util.Resource
import javax.inject.Inject



// Fake arayüz oluşturduk verileri çekmeden önce bi kontrol et yani çek hata varsa hata ver
class ArtRepository @Inject constructor(
    private val artDao:ArtDao,
    private val retrofitAPI: RetrofitAPI

    )

    : ArtReposityInterface {
    override suspend fun insertArt(art: Art) {
        artDao.insertArt(art)
    }

    override suspend fun deleteArt(art: Art) {
        artDao.deleteArt(art)
    }

    override fun getArt(): LiveData<List<Art>> {
        return artDao.observeArts()
    }

    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {
        return try {
            val response = retrofitAPI.imageSearch(imageString)



            if(response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                }?: Resource.error("Error",null)
            }else{
                Resource.error("Error",null)
            }

        }catch (e:Exception){
            Resource.error("No data!",null)
        }
    }
}