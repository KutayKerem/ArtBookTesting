package com.kutaykerem.artbooktesting.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutaykerem.artbooktesting.model.ImageResponse
import com.kutaykerem.artbooktesting.repo.ArtReposityInterface
import com.kutaykerem.artbooktesting.roomdb.Art
import com.kutaykerem.artbooktesting.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class  ArtViewModel @Inject constructor(
    private val reposity: ArtReposityInterface


) : ViewModel()  {


    // Art Fragment
    val artList = reposity.getArt()


    // Details Fragment

    private val images = MutableLiveData<Resource<ImageResponse>>()
    val imageList: LiveData<Resource<ImageResponse>>  get() = images





    private val selectedImage = MutableLiveData<String>() // seçme

    val selectedImageUrl : LiveData<String> get() = selectedImage // seçtiğini kaydet


    // Art Details Fragment

    private var insertArtMsg = MutableLiveData<Resource<Art>>()
    val insertArtMessage : LiveData<Resource<Art>> get() = insertArtMsg



    fun resetInsertArtMsg(){
        insertArtMsg = MutableLiveData<Resource<Art>>()
    }



    fun setSelectedImage(url:String){
        selectedImage.postValue(url)
    }

    fun deleteArt(art:Art) = viewModelScope.launch{
        reposity.deleteArt(art)
    }

    fun insertArt(art:Art) = viewModelScope.launch {
        reposity.insertArt(art)
    }




    fun makeArt(name:String,artistName:String,year:String){
        if(name.isEmpty()  || artistName.isEmpty()  || year.isEmpty()){
            insertArtMsg.postValue(Resource.error("Enter name,artist,year",null))
            return
        }

        val yearInt = try {
            year.toInt()
        }catch (e:Exception){
            insertArtMsg.postValue(Resource.error("Year should be number",null))
            return
        }

        val art = Art(name,artistName,yearInt,selectedImage.value ?: "")
        insertArt(art)
        setSelectedImage("")
        insertArtMsg.postValue(Resource.success(art))

    }






    fun searchForImage(searchString: String){
        if(searchString.isEmpty()){
            return
        }

        images.value = Resource.loading(null)
        viewModelScope.launch {
            val response = reposity.searchImage(searchString)
            images.value = response
        }

    }








}