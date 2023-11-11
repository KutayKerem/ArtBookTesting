package com.kutaykerem.artbooktesting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kutaykerem.artbooktesting.MainCoroutineRule
import com.kutaykerem.artbooktesting.repo.FakeArtRepository
import com.kutaykerem.artbooktesting.util.Status
import com.google.common.truth.Truth.assertThat
import com.kutaykerem.artbooktesting.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ArtViewModelTest {



    // trading dinlemeden direk test eder
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()



    /*
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

     */

    private lateinit var viewModel : ArtViewModel

    @Before
    fun setup() {
        viewModel = ArtViewModel(FakeArtRepository())
    }

    @Test
    fun `insert art without year returns error`() {
        viewModel.makeArt("Mona Lisa","Da Vinci","")
        val value = viewModel.insertArtMessage.getOrAwaitValue()
        assertThat(value.status).isEqualTo(Status.ERROR)


    }



    @Test
    fun `insert art without name returns error`() {
        viewModel.makeArt("","Da Vinci","1800")
        val value = viewModel.insertArtMessage.getOrAwaitValue()


    }

    @Test
    fun `insert art without artistName returns error`() {
        viewModel.makeArt("Mona Lisa","","1800")
        val value = viewModel.insertArtMessage.getOrAwaitValue()
        assertThat(value.status).isEqualTo(Status.ERROR)


    }
}