package com.example.barkodershopapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentHistoryListBinding
import com.example.barkodershopapp.data.room.HistoryDataEntity
import com.example.barkodershopapp.presentation.Adapters.HistoryAdapter
import com.example.barkodershopapp.presentation.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryListFragment : Fragment() {
    lateinit var binding : FragmentHistoryListBinding
    lateinit var historyAdapter : HistoryAdapter
    val historyViewmodel : HistoryViewModel by viewModels()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryListBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listH = arrayListOf<HistoryDataEntity>()

        historyAdapter = HistoryAdapter(listH)

        binding.recViewHistory2.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }


        historyViewmodel.allNotes.observe(viewLifecycleOwner, Observer {products4 ->
                historyAdapter.setNotesList(products4)

        })


        binding.btnAddList2.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            val targetFragment = ListProductsFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction!!.replace(R.id.cont, targetFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }





//
//        var list = arrayListOf<HistoryDataEntity>()
//
//        historyAdapter = HistoryAdapter(list)
//        binding.recViewHistory.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = historyAdapter
//        }
//
//        historyViewmodel.allNotes.observe(viewLifecycleOwner, {history ->
//            historyAdapter.setNotesList(history as ArrayList<HistoryDataEntity>)
//
//        })



    }
}