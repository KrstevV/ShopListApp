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
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import coil.Coil
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentAddProductBinding
import com.example.barkodershopapp.data.room.ProductDataEntity
import com.example.barkodershopapp.presentation.viewmodel.ProductViewModel
import com.example.barkodershopapp.typeconverters.TypeConverterss
import com.google.android.gms.fido.fido2.api.common.RequestOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.lang.Integer.max


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



        binding = FragmentAddProductBinding.inflate(inflater, container, false)

        val barcodeNumber = this@AddProductFragment.arguments?.getString("barcodeNum").toString()
        binding.cameraImage.setImageResource(R.drawable.photo_camera)

        if(barcodeNumber == "null") {
            binding.editTextBarcodeAddProduct.setText("")
        } else {
            binding.editTextBarcodeAddProduct.setText(barcodeNumber)
        }


        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        binding.addImage.setOnClickListener {
            cameraCheckPremission()
        }

        var currentAddProduct = ProductDataEntity("productName","productBarcode","productNotes", 0, "", 0, false, null,
            1,0,arrayListOf(),0)

        binding.btnScanAddProduct.setOnClickListener {
            findNavController().navigate(R.id.scanFragment)
        }



        var navContr = findNavController()
        binding.btnAddProductToList.setOnClickListener {


            var productName = binding.editTextNameAddProduct.text.toString()
            var productBarcode = binding.editTextBarcodeAddProduct.text.toString()
            var productPrice = binding.editPriceAddProduct.text.toString()
            var productImage = binding.cameraImage
            var productUnit = binding.editUnitAddProduct.text.toString()
            var productQuantity = binding.editQuantityAddProduct.text.toString()




            if(productName.isNotEmpty() && productPrice.isNotEmpty() && productUnit.isNotEmpty()
                &&productQuantity.isNotEmpty()) {

                lifecycleScope.launch {
                    currentAddProduct.nameProduct = productName
                    currentAddProduct.barcodeProduct = productBarcode
                    currentAddProduct.priceProduct = productPrice.toInt()
                    currentAddProduct.totalPrice = productPrice.toInt()
                    currentAddProduct.checkout = false
                    currentAddProduct.unitProduct = productUnit
                    currentAddProduct.quantityProduct = productQuantity.toInt()


                    val bitmap = (productImage.drawable as BitmapDrawable).bitmap
                    currentAddProduct.imageProduct = TypeConverterss.fromBitmap(bitmap)


                    productViewModel.insert(currentAddProduct)
                    navContr.navigate(
                        R.id.selectProductFragment,
                        null,
                        NavOptions.Builder().setPopUpTo(R.id.addProductFragment, true).build()
                    )
                    Toast.makeText(context, "Product is successful created", Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
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
                    val bitmapScaled = Bitmap.createScaledBitmap(bitmap,150,180,true)





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