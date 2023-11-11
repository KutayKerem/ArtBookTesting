package com.kutaykerem.artbooktesting.roomdb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.kutaykerem.artbooktesting.getOrAwaitValue
import com.kutaykerem.artbooktesting.util.Status
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest

class ArtDaoTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()



    @get:Rule
    var hiltRule=HiltAndroidRule(this)


     @Inject
     @Named("testDatabase")
     lateinit var database : ArtDatabase

    private lateinit var artDao : ArtDao


    // işlemden önce
    @Before
    fun setup(){
        // inMemory geçici veri datası: ramde geçici saklar  veriyi
       /*
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),ArtDatabase::class.java
        ).allowMainThreadQueries().build()
*/

        hiltRule.inject()
        artDao = database.artDao()



    }
    // işlem bitince
    @After
    fun tearDown(){
        database.close()
    }

    // run blocking diğer işlemleri duraksatır
    @Test
    fun insertArtTesting() = runBlockingTest{

        val exampleArt = Art("Mona Lisa","Da Vinci",1000,"image.url",1)
        artDao.insertArt(exampleArt)
        val list = artDao.observeArts().getOrAwaitValue() // getOrAwaitValue live datayı normal dataya çevirmeye yarar bunu yapmak için LiveDataUtil file i oluşturmalıyız
        assertThat(list).contains(exampleArt)

        

    }
    @Test
    fun deleteArtTesting() = runBlockingTest{
        val exampleArt = Art("Mona Lisa","Da Vinci",1000,"image.url",1)
        artDao.insertArt(exampleArt)
        artDao.deleteArt(exampleArt)

        val list = artDao.observeArts().getOrAwaitValue()
        assertThat(list).doesNotContain(exampleArt)


    }



}