package com.kutaykerem.artbooktesting.view

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.kutaykerem.artbooktesting.R
import com.kutaykerem.artbooktesting.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import javax.inject.Inject


@MediumTest
@HiltAndroidTest
class ArtFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: ArtFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()
    }
    @Test
    fun testNavigationFramArtToArDetails(){

        // navController test etmek için mockito kullanıyoz, face gibi
        val navController = Mockito.mock(NavController::class.java)

        // mock ile fragmenti başlattık
        launchFragmentInHiltContainer<ArtFragment>(
            factory = fragmentFactory

        ){
            Navigation.setViewNavController(requireView(),navController)

        }


        // tuşa basıldığında gibi işlemleri test etmek için Espresso


        // o tuşu bul ve ona tıklat
        Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(click())

        // gerçekten fragmente gittiğini doğrula
        Mockito.verify(navController).navigate(
            ArtFragmentDirections.actionArtFragmentToArtDetailsFragment()
        )


    }

}