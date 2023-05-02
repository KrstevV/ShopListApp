package com.example.barkodershopapp.presentation

import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentSelectProductBinding
import com.example.barkodershopapp.data.listhistorydata.swipecallback.SwipeToDelete
import com.example.barkodershopapp.data.room.ProductDataEntity
import com.example.barkodershopapp.domain.userdataacc.Product
import com.example.barkodershopapp.presentation.Adapters.SelectProductAdapter
import com.example.barkodershopapp.presentation.viewmodel.ListViewModel
import com.example.barkodershopapp.presentation.viewmodel.ProductApiViewModel
import com.example.barkodershopapp.presentation.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

@AndroidEntryPoint
class SelectProductFragment : Fragment() {
    val productApiViewModel : ProductApiViewModel by viewModels()
    val productViewModel : ProductViewModel by viewModels()
    lateinit var selectAdapter : SelectProductAdapter
        lateinit var binding : FragmentSelectProductBinding
        val listViewMOdel : ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val itemTouchHelper = ItemTouchHelper(swipteToDelete)
        itemTouchHelper.attachToRecyclerView(binding.recViewSelect)




    }



    private fun searchView(list : List<ProductDataEntity>) {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    selectAdapter.setProductsList(list as ArrayList<ProductDataEntity>)
                } else {
                    val filteredList = list.filter { product ->
                        product.nameProduct!!.contains(newText, ignoreCase = true)
                    } as ArrayList<ProductDataEntity>
                    selectAdapter.setProductsList(filteredList)
                }
                return false
            }

        })
    }








    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentSelectProductBinding.inflate(inflater, container, false)

        var productS = ArrayList<ProductDataEntity>()

        selectAdapter = SelectProductAdapter(productS,listViewMOdel)


        selectAdapter.setProductsList2(productS)

        binding.recViewSelect.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = selectAdapter
        }


        productViewModel.allNotes.observe(viewLifecycleOwner, Observer {products ->
            selectAdapter.setProductsList2(products as ArrayList<ProductDataEntity>)
            searchView(products)

        })


        return  binding.root

    }

    private val swipteToDelete = object : SwipeToDelete() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.absoluteAdapterPosition
            selectAdapter.notifyItemRemoved(position)
            productViewModel.deleteItem(selectAdapter.getSelectInt(position))


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

}

