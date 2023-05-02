package com.example.barkodershopapp.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentCurrentListBinding
import com.example.barkodershopapp.data.room.HistoryDataEntity
import com.example.barkodershopapp.data.room.ListDataEntity
import com.example.barkodershopapp.data.room.ProductDataEntity
import com.example.barkodershopapp.presentation.Adapters.CurrentListAdapter
import com.example.barkodershopapp.presentation.Adapters.ProductAdapter
import com.example.barkodershopapp.presentation.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrentListFragment : Fragment() {
    lateinit var binding : FragmentCurrentListBinding
    lateinit var currentAdapter : CurrentListAdapter
    val listViewModel : ListViewModel by viewModels()
    private val args by navArgs<CurrentListFragmentArgs>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentCurrentListBinding.inflate(inflater, container, false)
//        val barcodeNumber2 = this@CurrentListFragment.arguments?.getString("barcodeNum2").toString()
        val bundle = arguments
        val value = bundle?.getString("barcodeNum2")


        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = arrayListOf<ListDataEntity>()
        currentAdapter = CurrentListAdapter(args.currentList.listProducts)

        binding.currentRecView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = currentAdapter
        }

        binding.btnEditList.setOnClickListener {
                val intent = Intent(requireActivity(), MainActivity::class.java)
                intent.putExtra("editMode", true)
                intent.putExtra("currentListId", args.currentList.id)
                startActivity(intent)
                requireActivity().finish()
            for (i in  args.currentList.listProducts) {
                listViewModel.insert(i)
            }
        }



        binding.btnScan2.setOnClickListener {
            findNavController().navigate(R.id.action_currentListFragment_to_scanTwoFragment)
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("barcodeNum2")?.observe(viewLifecycleOwner) { result ->
            highlightProductByBarcode(result)
        }


    }

    private fun highlightProductByBarcode(barcode: String) {
        for (i in args.currentList.listProducts.indices) {
            if (args.currentList.listProducts[i].listProducts.barcodeProduct == barcode) {
                args.currentList.listProducts[i].listProducts.checkout = true
                currentAdapter.notifyItemChanged(i)
                break
            }
        }
    }



}