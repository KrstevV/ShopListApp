package com.example.barkodershopapp.presentation

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.barkodershopapp.presentation.viewmodel.HistoryViewModel
import com.example.barkodershopapp.presentation.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class CurrentListFragment : Fragment() {
    lateinit var binding : FragmentCurrentListBinding
    lateinit var currentAdapter : CurrentListAdapter
    val listViewModel : ListViewModel by viewModels()
    val historyViewModel : HistoryViewModel by viewModels()
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
        var visibleLayout : Boolean = false
        var scanLayout = binding.scanLayout



        binding.currentRecView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = currentAdapter
        }
        binding.textView25.text = args.currentList.listProducts.size.toString()
        binding.textTotalCostP.text = sumTotalCost(args.currentList.listProducts).toString() + " $"


        binding.btnEditList.setOnClickListener {
                val intent = Intent(requireActivity(), MainActivity::class.java)
                intent.putExtra("editMode", true)
                intent.putExtra("currentListId", args.currentList.id)
                intent.putExtra("checkedDate", args.currentList.checkedDate)
                intent.putExtra("listName", args.currentList.listName)
                startActivity(intent)
                requireActivity().finish()
            for (i in  args.currentList.listProducts) {
                listViewModel.insert(i)
            }

            if(listViewModel.isListStarted){
                scanLayout.visibility = View.VISIBLE
            }else {
                scanLayout.visibility = View.GONE
            }
        }
        binding.startList.setOnClickListener {
            listViewModel.isListStarted = !listViewModel.isListStarted
            currentAdapter.showCheckboxes = !currentAdapter.showCheckboxes
            if(listViewModel.isListStarted && currentAdapter.showCheckboxes) {
                currentAdapter.notifyDataSetChanged()
                scanLayout.visibility = View.VISIBLE
            } else {
                currentAdapter.notifyDataSetChanged()
                scanLayout.visibility = View.GONE
            }
            binding.startList.visibility = View.GONE
            binding.btnEditList.visibility = View.GONE
            binding.stopList.visibility = View.VISIBLE
        }
        binding.stopList.setOnClickListener {
            listViewModel.isListStarted = !listViewModel.isListStarted
            currentAdapter.showCheckboxes = !currentAdapter.showCheckboxes
            if(!listViewModel.isListStarted && !currentAdapter.showCheckboxes) {
                currentAdapter.notifyDataSetChanged()
                scanLayout.visibility = View.GONE
            } else {
                currentAdapter.notifyDataSetChanged()
                scanLayout.visibility = View.VISIBLE
            }
            binding.stopList.visibility = View.GONE
            binding.startList.visibility = View.VISIBLE
            binding.btnEditList.visibility = View.VISIBLE
        }


        binding.btnScan2.setOnClickListener {
            findNavController().navigate(R.id.action_currentListFragment_to_scanTwoFragment)
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("barcodeNum2")?.observe(viewLifecycleOwner) { result ->
            highlightProductByBarcode(result)
        }


    }

    private fun getCurrentDate(): String {
        val currentDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return formatter.format(currentDate)
    }



    private fun sucessfullCheckout(list : ArrayList<ListDataEntity>)  {
        var allCheckedOut = true
        for (listData in list) {
            if (listData.listProducts.checkout == false) {
                allCheckedOut = false
                break
            }
        }
        if (allCheckedOut) {
            binding.sucessfullCard.visibility = View.VISIBLE
            val handler = Handler(Looper.getMainLooper())
            binding.scanLayout.visibility = View.GONE
            args.currentList.checkedList = true
            args.currentList.checkedDate = getCurrentDate()
            historyViewModel.updateItem(args.currentList)

            for(i in args.currentList.listProducts) {
                i.listProducts.defultCount = 0
                i.listProducts.checkout = false
                listViewModel.isListStarted = false
            }

            handler.postDelayed(object : Runnable {
                override fun run() {
//                    findNavController().navigate(R.id.action_currentListFragment_to_historyListFragment)
                    findNavController().previousBackStackEntry
                    findNavController().popBackStack()
                }

            }, 3000)



        }




    }

    private fun scannedPro() {
        var numScannedProduct = 0
        for (i in args.currentList.listProducts){
            if(i.listProducts.checkout) {
                numScannedProduct++
            }
            binding.scannedProductsP.text = numScannedProduct.toString()

        }
    }

    private fun sumTotalCost(products : MutableList<ListDataEntity>) : Int {
        var sum = 0

        for (product in products) {
            sum += product.listProducts.totalPrice
        }
        return sum
    }

    private fun highlightProductByBarcode(barcode: String) {
        for (i in args.currentList.listProducts.indices) {
            if (args.currentList.listProducts[i].listProducts.barcodeProduct == barcode &&
                args.currentList.listProducts[i].listProducts.defultCount < args.currentList.listProducts[i].listProducts.count) {
                    args.currentList.listProducts[i].listProducts.defultCount++
                if(args.currentList.listProducts[i].listProducts.count == args.currentList.listProducts[i].listProducts.defultCount ){
                    args.currentList.listProducts[i].listProducts.checkout = true
                    args.currentList.listProducts[i].listProducts.defultCount = args.currentList.listProducts[i].listProducts.count
                    currentAdapter.notifyItemChanged(i)
                    break
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()

        // Update the visibility of the button and layout based on the state
        if (listViewModel.isListStarted) {
            currentAdapter.showCheckboxes = true
            binding.startList.visibility = View.GONE
            binding.scanLayout.visibility = View.VISIBLE
            binding.stopList.visibility = View.VISIBLE
        } else {
            currentAdapter.showCheckboxes = false
            binding.scanLayout.visibility = View.GONE
        }
        scannedPro()
        sucessfullCheckout(args.currentList.listProducts)
    }

}