package com.kutaykerem.artbooktesting.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kutaykerem.artbooktesting.R
import com.kutaykerem.artbooktesting.adapter.ArtRecyclerAdapter
import com.kutaykerem.artbooktesting.databinding.FragmentArtsBinding
import com.kutaykerem.artbooktesting.viewmodel.ArtViewModel
import javax.inject.Inject

class ArtFragment @Inject constructor(
    private val artRecyclerAdapter: ArtRecyclerAdapter


) : Fragment(R.layout.fragment_arts){

    private var binding :FragmentArtsBinding? = null
    lateinit var viewModel : ArtViewModel


    // item sağa veya sola kaydılırdığında o itemi silme işlemi yaptık
    private val swipeCallBack = object  : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val layoutPosition = viewHolder.layoutPosition
            val selectedArt = artRecyclerAdapter.arts[layoutPosition]
            viewModel.deleteArt(selectedArt)

        }

    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bindings = FragmentArtsBinding.bind(view)
        binding = bindings

        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)

        subsribeToObservers()

        bindings.recyclerViewArts.adapter = artRecyclerAdapter
        bindings.recyclerViewArts.layoutManager = LinearLayoutManager(requireContext())
        ItemTouchHelper(swipeCallBack).attachToRecyclerView(bindings.recyclerViewArts)

        binding!!.fab.setOnClickListener {
            findNavController().navigate(ArtFragmentDirections.actionArtFragmentToArtDetailsFragment())

        }


    }


    private fun subsribeToObservers(){
        viewModel.artList.observe(viewLifecycleOwner, Observer {
           artRecyclerAdapter.arts = it


        })

    }






    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }


}