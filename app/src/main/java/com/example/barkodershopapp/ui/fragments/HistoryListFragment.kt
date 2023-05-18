package com.example.barkodershopapp.ui.fragments

import android.database.CursorWindow
import android.graphics.Canvas
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentHistoryListBinding
import com.example.barkodershopapp.ui.listeners.swipecallback.SwipeToDelete
import com.example.barkodershopapp.data.db.historydatabase.HistoryDataEntity
import com.example.barkodershopapp.ui.adapters.HistoryAdapter
import com.example.barkodershopapp.ui.listeners.swipeicons.SwipeHelper
import com.example.barkodershopapp.ui.viewmodels.HistoryViewModel
import com.example.barkodershopapp.ui.viewmodels.ListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.lang.reflect.Field

@AndroidEntryPoint
class HistoryListFragment : Fragment() {
    lateinit var binding: FragmentHistoryListBinding
    lateinit var historyAdapter: HistoryAdapter
    private val historyViewmodel: HistoryViewModel by viewModels()
    private val listViewModel : ListViewModel by viewModels()
    private val listH = arrayListOf<HistoryDataEntity>()
    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryListBinding.inflate(inflater, container, false)

        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.setAccessible(true)
            field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
        } catch (e: Exception) {
            e.printStackTrace()
        }

        setupRecView()
        observeList()
        onClickNewList()
        onBackButton()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCLickScan()
//        swipeDelete()
    }
    private fun onCLickScan() {
        var btnScan = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        btnScan.setOnClickListener {
            findNavController().navigate(
                R.id.scanFragment,
                null,
                NavOptions.Builder().setPopUpTo(R.id.historyListFragment2, true).build()
            )
        }
    }

    private fun onClickNewList(){
        binding.fabNewList.setOnClickListener {
            findNavController().navigate(
                R.id.listProductsFragment
            )
            listViewModel.delete()

        }
    }

    private fun setupRecView() {
        historyAdapter = HistoryAdapter(listH)

        binding.recViewHistory2.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter

            val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.recViewHistory2) {
                override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                    var buttons = listOf<UnderlayButton>()
                    val deleteButton = deleteButton(position)

                    buttons = listOf(deleteButton)
                    return buttons
                }
            })

            itemTouchHelper.attachToRecyclerView(binding.recViewHistory2)
        }
    }

    private fun deleteButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            "Delete",
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    historyAdapter.notifyItemRemoved(position)
                    historyViewmodel.deleteItem(historyAdapter.getHistoryInt(position))

                }
            })
    }

    private fun observeList() {
        historyViewmodel.allNotes.observe(viewLifecycleOwner, Observer { products4 ->
            historyAdapter.setNotesList(products4)
            binding.progressBar4.visibility = View.GONE
        })
    }
    private fun swipeDelete() {
        val itemTouchHelper = ItemTouchHelper(swipteToDelete)
        itemTouchHelper.attachToRecyclerView(binding.recViewHistory2)
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
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.designColor
                    )
                )
                .addSwipeLeftActionIcon(R.drawable.delete_item)
                .create()
                .decorate()

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        }
    }

    private fun onBackButton(){
        callback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {

                findNavController().navigate(
                    R.id.homeScreenFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.historyListFragment2, true).build()
                )

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onResume() {
        super.onResume()
        binding.progressBar4.visibility = View.VISIBLE
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            binding.progressBar4.visibility = View.GONE
        }, 2000)
    }

}
