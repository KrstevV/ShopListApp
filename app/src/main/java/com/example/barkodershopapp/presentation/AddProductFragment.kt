package com.example.barkodershopapp.presentation

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentAddProductBinding
import com.example.barkodershopapp.data.room.ProductDataEntity
import com.example.barkodershopapp.presentation.viewmodel.ProductViewModel
import com.example.barkodershopapp.typeconverters.TypeConverterss
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


@AndroidEntryPoint
class AddProductFragment : Fragment() {
    lateinit var binding : FragmentAddProductBinding
    val productViewModel : ProductViewModel by viewModels()
    private val cameraRequest = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)





        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddProductBinding.inflate(inflater, container, false)


        val barcodeNumber = this@AddProductFragment.arguments?.getString("barcodeNum").toString()


        binding.editTextBarcodeAddProduct.setText(barcodeNumber)

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.addImage.setOnClickListener {
            cameraCheckPremission()
        }



        var currentAddProduct = ProductDataEntity("productName","productBarcode","productNotes", 0, true, null, 1, 0)


        binding.btnScanAddProduct.setOnClickListener {
            findNavController().navigate(R.id.scanFragment)
        }


        var navContr = findNavController()
        binding.btnAddProductToList.setOnClickListener {

            var productName = binding.editTextNameAddProduct.text.toString()
            var productBarcode = binding.editTextBarcodeAddProduct.text.toString()
            var productPrice = binding.editPriceAddProduct.text.toString()
            var productImage = binding.cameraImage

            val bitmap = (binding.cameraImage.drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100 ,stream)
            val byteArray = stream.toByteArray()


            if(productName.isNotEmpty() && productPrice.isNotEmpty()) {

                lifecycleScope.launch {
                    currentAddProduct.nameProduct = productName
                    currentAddProduct.barcodeProduct = productBarcode
                    currentAddProduct.priceProduct = productPrice.toInt()
                    currentAddProduct.totalPrice = productPrice.toInt()

                    val bitmap = (productImage.drawable as BitmapDrawable).bitmap
                    currentAddProduct.imageProduct = TypeConverterss.fromBitmap(bitmap)



                }

                productViewModel.insert(currentAddProduct)
//                navContr.navigate(R.id.selectProductFragment)
                navContr.navigate(R.id.selectProductFragment, null, NavOptions.Builder().setPopUpTo(R.id.addProductFragment, true).build())


            }



//
//                findNavController().navigate(R.id.action_addProductFragment_to_selectProductFragment2)
//                findNavController().clearBackStack(R.id.addProductFragment)






//            } else {
//                Toast.makeText(, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
//            }


        }


    }

//    private suspend fun getBitmap() : Bitmap {
//        val loading = ImageLoader(requireActivity().applicationContext)
//        val request = ImageRequest.Builder(requireActivity().applicationContext)
//            .data(binding.cameraImage)
//            .build()
//        val result = (loading.execute(request) as SuccessResult).drawable
//
//        return  (result as BitmapDrawable).bitmap
//
//    }

    private suspend fun getBitmap(): Bitmap? {
        val loading = ImageLoader(requireActivity().applicationContext)
        val request = ImageRequest.Builder(requireActivity().applicationContext)
            .data(binding.cameraImage)
            .build()
        val result = try {
            (loading.execute(request) as SuccessResult).drawable
        } catch (e: Exception) {
            // Handle the error case
            Log.e(TAG, "Error loading image: ${e.message}")
            null
        }

        return (result as? BitmapDrawable)?.bitmap
    }

    private fun cameraCheckPremission() {

        Dexter.withContext(requireActivity().applicationContext)
            .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA).withListener(
                object  : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {

                            if(report.areAllPermissionsGranted()) {
                                camera()
                            }

                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        showRorationalDialogForPermission()
                    }

                }
            ).onSameThread().check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                cameraRequest->{

                    val bitmap = data?.extras?.get("data") as Bitmap

                    binding.cameraImage.load(bitmap)
                }
            }
        }

    }

    private fun camera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, cameraRequest)
    }

    private fun showRorationalDialogForPermission() {
        AlertDialog.Builder(requireActivity().applicationContext)
            .setMessage("asdas")
            .setPositiveButton("add"){_,_ ->

                try{
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", activity?.packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e : ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("canccel"){dialog, _->
                dialog.dismiss()
            }

    }



}