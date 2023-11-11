package com.kutaykerem.artbooktesting.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kutaykerem.artbooktesting.R
import com.kutaykerem.artbooktesting.adapter.ImageRecyclerAdapter
import com.kutaykerem.artbooktesting.databinding.FragmentImageApiBinding
import com.kutaykerem.artbooktesting.util.Status
import com.kutaykerem.artbooktesting.viewmodel.ArtViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageApiFragment @Inject constructor(
     val imageRecyclerAdapter: ImageRecyclerAdapter

) :Fragment(R.layout.fragment_image_api) {


    lateinit var viewModel : ArtViewModel
    private var fragmentBinding : FragmentImageApiBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentImageApiBinding.bind(view)
        fragmentBinding = binding

        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)
        binding.imageRecyclerView.adapter = imageRecyclerAdapter
        binding.imageRecyclerView.layoutManager = GridLayoutManager(requireContext(),3)

        imageRecyclerAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setSelectedImage(it)

        }

        var job :  Job? = null
        binding.searchText.addTextChangedListener {

            job?.cancel()
            job = lifecycleScope.launch {

                // 1 saniyede bir arat
                delay(1000)
                it?.let {
                    if(it.toString().isNotEmpty()){
                        viewModel.searchForImage(it.toString())
                    }

                }
            }

        }



        subscribeToObserver()


    }
    fun subscribeToObserver(){
        viewModel.imageList.observe(viewLifecycleOwner, Observer {

            when(it.status){
                Status.SUCCESS ->{
                    val urls = it.data?.hits?.map { imageResult ->
                        imageResult.previewURL
                    }
                    imageRecyclerAdapter.images = urls ?: listOf()
                    fragmentBinding?.progressBar?.visibility = View.GONE

                }
                Status.ERROR->{
                    Toast.makeText(requireContext(),it.message ?: "Error",Toast.LENGTH_LONG).show()
                    fragmentBinding?.progressBar?.visibility = View.GONE
                }
                Status.LOADING->{
                    fragmentBinding?.progressBar?.visibility = View.VISIBLE
                }
            }

        })



    }
}