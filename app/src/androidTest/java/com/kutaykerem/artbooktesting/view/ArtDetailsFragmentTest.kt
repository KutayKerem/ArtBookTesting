package com.kutaykerem.artbooktesting.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.kutaykerem.artbooktesting.R
import com.kutaykerem.artbooktesting.getOrAwaitValue
import com.kutaykerem.artbooktesting.launchFragmentInHiltContainer
import com.kutaykerem.artbooktesting.repo.FakeArtRepositoryTest
import com.kutaykerem.artbooktesting.roomdb.Art
import com.kutaykerem.artbooktesting.viewmodel.ArtViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import javax.inject.Inject


@MediumTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class ArtDetailsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: ArtFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()

    }


    @Test
    fun testNavigationFramArtToArDetailsToImageAPI(){
        val navController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory
        ){
            Navigation.setViewNavController(requireView(),navController)
        }



        // Tuşa basıldığında
       Espresso.onView(ViewMatchers.withId(R.id.artImageView)).perform(click())

        // Doğrula
        Mockito.verify(navController).navigate( ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageApiFragment())

    }

    @Test
    fun testOnBackPress(){
        val navController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory
        ){
            Navigation.setViewNavController(requireView(),navController)
        }

        // Geri tuşuna basıldığında

        Espresso.pressBack()
        // Doğrula

        Mockito.verify(navController).popBackStack()


    }
    @Test
    fun testSave(){

        val testViewModel = ArtViewModel(FakeArtRepositoryTest())

        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory
        ){
            viewModel = testViewModel
        }


        Espresso.onView(ViewMatchers.withId(R.id.nameText)).perform(replaceText("Mona Lisa"))
        Espresso.onView(ViewMatchers.withId(R.id.artistText)).perform(replaceText("Da Vinci"))
        Espresso.onView(ViewMatchers.withId(R.id.yearText)).perform(replaceText("1500"))
        Espresso.onView(ViewMatchers.withId(R.id.saveButton)).perform(click())

        // assertThat idda etmek testlerde kullanılır
        assertThat(testViewModel.artList.getOrAwaitValue()).contains(
            Art("Mona Lisa","Da Vinci",1500,"")

        )


    }


}