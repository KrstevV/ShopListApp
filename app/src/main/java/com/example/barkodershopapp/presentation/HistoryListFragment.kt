package com.example.barkodershopapp.presentation

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentHistoryListBinding
import com.example.barkodershopapp.data.listhistorydata.swipecallback.SwipeToDelete
import com.example.barkodershopapp.data.room.HistoryDataEntity
import com.example.barkodershopapp.presentation.Adapters.HistoryAdapter
import com.example.barkodershopapp.presentation.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

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
        val itemTouchHelper = ItemTouchHelper(swipteToDelete)
        itemTouchHelper.attachToRecyclerView(binding.recViewHistory2)


        historyViewmodel.allNotes.observe(viewLifecycleOwner, Observer {products4 ->
                historyAdapter.setNotesList(products4)

        })


        }

    private val swipteToDelete = object : SwipeToDelete() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.absoluteAdapterPosition
            historyAdapter.notifyItemRemoved(position)
            historyViewmodel.deleteItem(historyAdapter.getHistoryInt(position))


        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {

            RecyclerViewSwipeDecorator.Builder(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive

            )
                .addSwipeLeftBackgroundColor(
                    ContextCompat.getColor(requireActivity(),
                    R.color.designColor))
                .addSwipeLeftActionIcon(R.drawable.delete_item)
                .create()
                .decorate()


            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)



        }
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
