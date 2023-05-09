package com.example.barkodershopapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentCurrentListBinding
import com.example.barkodershopapp.data.db.listdatabase.ListDataEntity
import com.example.barkodershopapp.ui.adapters.CurrentListAdapter
import com.example.barkodershopapp.ui.viewmodels.HistoryViewModel
import com.example.barkodershopapp.ui.viewmodels.ListViewModel
import com.example.barkodershopapp.ui.activities.MainActivity
import com.example.barkodershopapp.ui.listeners.OnCheckedListener
import com.example.barkodershopapp.ui.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.descriptors.StructureKind
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class CurrentListFragment : Fragment(){
    lateinit var binding: FragmentCurrentListBinding
    lateinit var currentAdapter: CurrentListAdapter
    val listViewModel: ListViewModel by viewModels()
    val historyViewModel: HistoryViewModel by viewModels()
    val productViewModel : ProductViewModel by viewModels()
    private val args by navArgs<CurrentListFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentListBinding.inflate(inflater, container, false)

        setupRecView()
        setupTextViews()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClickEdit()
        onClickScan()
        onClickStart()
        onClickStop()
        getBarcodeString()
        onClickRestore()
    }

    private fun getBarcodeString(){
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("barcodeNum2")
            ?.observe(viewLifecycleOwner) { result ->
                highlightProductByBarcode(result)
            }
    }
    private fun setupTextViews(){
        binding.textView25.text = args.currentList.listProducts.size.toString()
        binding.textTotalCostP.text = sumTotalCost(args.currentList.listProducts).toString() + " $"
    }


    private fun onClickScan(){
        binding.btnScan2.setOnClickListener {
            findNavController().navigate(R.id.action_currentListFragment_to_scanTwoFragment)
        }
    }

    private fun onClickStop() {
        var scanLayout = binding.scanLayout
        binding.stopList.setOnClickListener {
            listViewModel.isListStarted = !listViewModel.isListStarted
            currentAdapter.showCheckboxes = !currentAdapter.showCheckboxes
            if (!listViewModel.isListStarted && !currentAdapter.showCheckboxes) {
                currentAdapter.notifyDataSetChanged()
                scanLayout.visibility = View.GONE
            } else {
                currentAdapter.notifyDataSetChanged()
                scanLayout.visibility = View.VISIBLE
            }
            binding.stopList.visibility = View.GONE
            binding.startList.visibility = View.VISIBLE
            binding.btnEditList.visibility = View.VISIBLE
            binding.btnRestoreCheckout.visibility = View.VISIBLE
        }
    }

    private fun onClickStart(){
        var scanLayout = binding.scanLayout
        binding.startList.setOnClickListener {
            listViewModel.isListStarted = !listViewModel.isListStarted
            currentAdapter.showCheckboxes = !currentAdapter.showCheckboxes
            if (listViewModel.isListStarted && currentAdapter.showCheckboxes) {
                currentAdapter.notifyDataSetChanged()
                scanLayout.visibility = View.VISIBLE
            } else {
                currentAdapter.notifyDataSetChanged()
                scanLayout.visibility = View.GONE
            }
            binding.startList.visibility = View.GONE
            binding.btnEditList.visibility = View.GONE
            binding.stopList.visibility = View.VISIBLE
            binding.btnRestoreCheckout.visibility = View.GONE
        }
    }

    private fun onClickRestore(){
        binding.btnRestoreCheckout.setOnClickListener {
            for (i in args.currentList.listProducts) {
                i.listProducts.defultCount = 0
                i.listProducts.checkout = false
            }
        }
    }
    private fun onClickEdit(){
        var scanLayout = binding.scanLayout
        binding.btnEditList.setOnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.putExtra("editMode", true)
            intent.putExtra("currentListId", args.currentList.id)
            intent.putExtra("checkedDate", args.currentList.checkedDate)
            intent.putExtra("listName", args.currentList.listName)
            startActivity(intent)
            requireActivity().finish()
            for (i in args.currentList.listProducts) {
                listViewModel.insert(i)
            }
            if (listViewModel.isListStarted) {
                scanLayout.visibility = View.VISIBLE
            } else {
                scanLayout.visibility = View.GONE
            }
        }
    }
    private fun setupRecView() {
        currentAdapter = CurrentListAdapter(args.currentList.listProducts, productViewModel, onCheckedListener)
        currentAdapter.setNotesList(args.currentList.listProducts)
        binding.currentRecView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = currentAdapter
        }
    }

    private fun getCurrentDate(): String {
        val currentDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return formatter.format(currentDate)
    }
    private fun sucessfullCheckout() {
        var allCheckedOut = true
        for (listData in args.currentList.listProducts) {
            if (listData.listProducts.checkout == false) {
                allCheckedOut = false
                break
            }
        }
        if (allCheckedOut) {
            if (listViewModel.isListStarted) {
                binding.sucessfullCard.visibility = View.VISIBLE
                val handler = Handler(Looper.getMainLooper())
                binding.scanLayout.visibility = View.GONE
                args.currentList.checkedList = true
                args.currentList.checkedDate = getCurrentDate()
                historyViewModel.updateItem(args.currentList)

                for (i in args.currentList.listProducts) {
                    i.listProducts.defultCount = 0
                    i.listProducts.checkout = false
                    listViewModel.isListStarted = false
                }
                handler.postDelayed(object : Runnable {
                    override fun run() {
                        findNavController().previousBackStackEntry
                        findNavController().popBackStack()
                    }
                }, 2000)
            }
        }

    }
    private fun scannedPro() {
        var numScannedProduct = 0
        for (i in args.currentList.listProducts) {
            if (i.listProducts.checkout) {
                numScannedProduct++
            }
            binding.scannedProductsP.text = numScannedProduct.toString()
        }
    }

    private fun sumTotalCost(products: MutableList<ListDataEntity>): Int {
        var sum = 0

        for (product in products) {
            sum += product.listProducts.totalPrice
        }
        return sum
    }

    private fun highlightProductByBarcode(barcode: String) {
        for (i in args.currentList.listProducts.indices) {
            if (args.currentList.listProducts[i].listProducts.barcodeProduct == barcode &&
                args.currentList.listProducts[i].listProducts.defultCount < args.currentList.listProducts[i].listProducts.count
            ) {
                args.currentList.listProducts[i].listProducts.defultCount++
                if (args.currentList.listProducts[i].listProducts.count == args.currentList.listProducts[i].listProducts.defultCount) {
                    args.currentList.listProducts[i].listProducts.checkout = true
                    args.currentList.listProducts[i].listProducts.defultCount =
                        args.currentList.listProducts[i].listProducts.count
                    currentAdapter.notifyItemChanged(i)
                    break
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (listViewModel.isListStarted) {
            currentAdapter.showCheckboxes = true
            binding.startList.visibility = View.GONE
            binding.scanLayout.visibility = View.VISIBLE
            binding.stopList.visibility = View.VISIBLE
            binding.btnEditList.visibility = View.GONE
            binding.btnRestoreCheckout.visibility = View.GONE
        } else {
            currentAdapter.showCheckboxes = false
            binding.scanLayout.visibility = View.GONE
            binding.btnEditList.visibility = View.VISIBLE
            binding.btnRestoreCheckout.visibility = View.VISIBLE
        }
        scannedPro()
        sucessfullCheckout()

    }

    val onCheckedListener = object  : OnCheckedListener {
        override fun onCheckedChanged(list : ListDataEntity) {
            list.listProducts.checkout = !list.listProducts.checkout
            for(i in args.currentList.listProducts.indices){
                if(args.currentList.listProducts[i].listProducts.checkout){
                    args.currentList.listProducts[i].listProducts.defultCount =
                        args.currentList.listProducts[i].listProducts.count
                    currentAdapter.notifyItemChanged(i)
                } else {
                    args.currentList.listProducts[i].listProducts.defultCount = 0
                    currentAdapter.notifyItemChanged(i)
                }
            }

           listViewModel.updateItem(list)

            sucessfullCheckout()
        }

    }

}