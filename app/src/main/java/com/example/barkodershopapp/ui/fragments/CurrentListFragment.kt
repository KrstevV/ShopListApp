package com.example.barkodershopapp.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.StatusBarManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
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
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentListBinding.inflate(inflater, container, false)

        setupRecView()
        setupTextViews()
        navInvisible()
        toolBarName()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        onClickScan()
        onClickStart()
        onClickStop()
        getBarcodeString()
        onClickRestore()
        onBackButton()
    }

    private fun toolBarName() {
        requireActivity().title = args.currentList.listName
    }
    private fun navInvisible(){
        var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
        bottomNav.visibility = View.GONE
        var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        bottomFab.visibility = View.GONE
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
            findNavController().navigate(R.id.action_currentListFragment2_to_scanTwoFragment)
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
            changeStatusBarColor(ContextCompat.getColor(requireContext(), R.color.toolBarColor))
            binding.include.toolBarr.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.toolBarColor))
            binding.textActivityName.text = args.currentList.listName
            binding.stopList.visibility = View.GONE
            binding.startList.visibility = View.VISIBLE
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

            changeStatusBarColor(ContextCompat.getColor(requireContext(), R.color.checkoutModeToolBar))
            binding.include.toolBarr.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.checkoutModeToolBar))
            binding.textActivityName.text = "Checkout Mode"
            binding.startList.visibility = View.GONE
            binding.stopList.visibility = View.VISIBLE
            binding.btnRestoreCheckout.visibility = View.GONE
        }
    }
    private fun onClickRestore(){
        binding.btnRestoreCheckout.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.restoreList))
                .setMessage(getString(R.string.restoreListDialog))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    for (i in args.currentList.listProducts) {
                        i.listProducts.defultCount = 0
                        i.listProducts.checkout = false
                    }
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
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
                        changeStatusBarColor(ContextCompat.getColor(requireContext(), R.color.toolBarColor))
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
            changeStatusBarColor(ContextCompat.getColor(requireContext(), R.color.checkoutModeToolBar))
            binding.include.toolBarr.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.checkoutModeToolBar))
            binding.textActivityName.text = "Checkout Mode"
            currentAdapter.showCheckboxes = true
            binding.startList.visibility = View.GONE
            binding.scanLayout.visibility = View.VISIBLE
            binding.stopList.visibility = View.VISIBLE
            binding.btnRestoreCheckout.visibility = View.GONE
        } else {
            changeStatusBarColor(ContextCompat.getColor(requireContext(), R.color.toolBarColor))
            binding.include.toolBarr.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.toolBarColor))
            binding.textActivityName.text = args.currentList.listName
            currentAdapter.showCheckboxes = false
            binding.scanLayout.visibility = View.GONE
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
                scannedPro()
            }

           listViewModel.updateItem(list)

            sucessfullCheckout()
        }
    }

    override fun onPause() {
        super.onPause()

        var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
        bottomNav.visibility = View.VISIBLE
        var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        bottomFab.visibility = View.VISIBLE
    }

    private fun onBackButton(){
        callback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {

                findNavController().popBackStack()
                changeStatusBarColor(ContextCompat.getColor(requireContext(), R.color.toolBarColor))

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun changeStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = requireActivity().window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }
    }


}