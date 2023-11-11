package com.kutaykerem.artbooktesting.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.kutaykerem.artbooktesting.R
import com.kutaykerem.artbooktesting.databinding.ArtDetailsBinding
import com.kutaykerem.artbooktesting.databinding.FragmentArtsBinding
import com.kutaykerem.artbooktesting.util.Status
import com.kutaykerem.artbooktesting.viewmodel.ArtViewModel
import javax.inject.Inject

class ArtDetailsFragment @Inject constructor(
    private val glide : RequestManager

)  : Fragment(R.layout.art_details) {


    private var artDetailsBinding : ArtDetailsBinding? = null

    lateinit var viewModel : ArtViewModel



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ArtDetailsBinding.bind(view)
        artDetailsBinding = binding


        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)


        binding!!.artImageView.setOnClickListener {
            findNavController().navigate(ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageApiFragment())
        }


        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)


        subsribeToObservers()

        binding.saveButton.setOnClickListener {
            viewModel.makeArt(
                binding.nameText.toString(),
                binding.artistText.toString(),
                binding.yearText.toString()
            )



        }


    }
    private fun subsribeToObservers(){
        viewModel.selectedImageUrl.observe(viewLifecycleOwner, Observer {url->
            artDetailsBinding?.let { binding->
               glide.load(url).into(binding.artImageView)
            }
        })

        viewModel.insertArtMessage.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS ->{
                    Toast.makeText(requireContext(),"Succes",Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                    viewModel.resetInsertArtMsg()
                }
                Status.ERROR ->{
                    Toast.makeText(requireContext(),it.message ?: "Error",Toast.LENGTH_LONG).show()

                }
                Status.LOADING->{

                }
            }


        })



    }


    override fun onDestroy() {
        artDetailsBinding = null
        super.onDestroy()
    }
}