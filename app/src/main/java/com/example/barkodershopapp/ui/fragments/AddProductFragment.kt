package com.example.barkodershopapp.ui.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import coil.load
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentAddProductBinding
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity
import com.example.barkodershopapp.ui.viewmodels.ProductViewModel
import com.example.barkodershopapp.ui.typeconverters.TypeConverterss
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddProductFragment : Fragment() {
    lateinit var binding: FragmentAddProductBinding
    private val productViewModel: ProductViewModel by viewModels()
    private val cameraRequest = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        binding.editTextBarcodeAddProduct.setText("xxxxxx")
        binding = FragmentAddProductBinding.inflate(inflater, container, false)

        getBarcodeString()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClickAdd()
        onClickAddImage()
        onClickScan()
    }

    private fun getBarcodeString() {
        val barcodeNumber = this@AddProductFragment.arguments?.getString("barcodeNum").toString()
        binding.cameraImage.setImageResource(R.drawable.photo_camera)

        if (barcodeNumber == "null") {
            binding.editTextBarcodeAddProduct.setText("xxxxxxx")
        } else {
            binding.editTextBarcodeAddProduct.setText(barcodeNumber)
        }
    }

    private fun onClickScan() {
        binding.btnScanAddProduct.setOnClickListener {
            findNavController().navigate(R.id.scanFragment)
        }
    }

    private fun onClickAdd() {
        val navContr = findNavController()
        val currentAddProduct = ProductDataEntity(
            "productName",
            "productBarcode",
            "productNotes",
            0,
            "",
            0,
            false,
            null,
            1,
            0,
            arrayListOf(),
            0
        )
        binding.btnAddProductToList.setOnClickListener {
            var productName = binding.editTextNameAddProduct.text.toString()
            var productBarcode = binding.editTextBarcodeAddProduct.text.toString()
            var productPrice = binding.editPriceAddProduct.text.toString()
            var productImage = binding.cameraImage
            var productUnit = binding.editUnitAddProduct.text.toString()
            var productQuantity = binding.editQuantityAddProduct.text.toString()


            if (productName.isNotEmpty() && productPrice.isNotEmpty() && productUnit.isNotEmpty()
                && productQuantity.isNotEmpty()
            ) {
                lifecycleScope.launch {
                    currentAddProduct.nameProduct = productName
                    currentAddProduct.barcodeProduct = productBarcode
                    currentAddProduct.priceProduct = productPrice.toInt()
                    currentAddProduct.totalPrice = productPrice.toInt()
                    currentAddProduct.checkout = false
                    currentAddProduct.unitProduct = productUnit
                    currentAddProduct.quantityProduct = productQuantity.toInt()
                    currentAddProduct.defultCount = 0
                    currentAddProduct.count = 1


                    val drawable = productImage.drawable
                    val imageData = when (drawable) {
                        is BitmapDrawable -> {
                            val bitmap = drawable.bitmap
                            TypeConverterss.fromBitmap(bitmap)
                        }
                        is VectorDrawable -> {
                            val bitmap = Bitmap.createBitmap(
                                drawable.intrinsicWidth,
                                drawable.intrinsicHeight,
                                Bitmap.Config.ARGB_8888
                            )
                            val canvas = Canvas(bitmap)
                            drawable.setBounds(0, 0, canvas.width, canvas.height)
                            drawable.draw(canvas)
                            TypeConverterss.fromBitmap(bitmap)
                        }
                        else -> {

                            val defaultImage = requireContext().resources.getDrawable(
                                R.drawable.photo_camera,
                                null
                            ) as BitmapDrawable
                            val defaultImageByteArray =
                                TypeConverterss.fromBitmap(defaultImage.bitmap)
                            // Use the default image data
                            defaultImageByteArray
                        }
                    }
                    currentAddProduct.imageProduct = imageData


                    productViewModel.insert(currentAddProduct)
                }
                navContr.navigate(
                    R.id.selectProductFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.addProductFragment, true).build()
                )
                Toast.makeText(context, "Product is successful created", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun onClickAddImage() {
        binding.addImage.setOnClickListener {
            cameraCheckPremission()
        }
    }

    private fun cameraCheckPremission() {

        Dexter.withContext(requireActivity().applicationContext)
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            ).withListener(
                object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {

                            if (report.areAllPermissionsGranted()) {
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

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                cameraRequest -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    val bitmapScaled = Bitmap.createScaledBitmap(bitmap, 150, 180, true)
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
            .setPositiveButton("add") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", activity?.packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("canccel") { dialog, _ ->
                dialog.dismiss()
            }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                val builder = AlertDialog.Builder(context)
                    .setTitle("List Save")
                builder.setMessage("If you back to home screen ur list created will be delted!")
                    .setCancelable(false)
                    .setNegativeButton("stay") { dialog, id ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("OK") { dialog, id ->
                        requireActivity().finish()
                        dialog.dismiss()
                    }

                val alert = builder.create()
                alert.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("nameAdd", binding.editTextNameAddProduct.text.toString())
        outState.putInt("priceAdd", binding.editPriceAddProduct.text.toString().toInt())
        outState.putString("unitAdd", binding.editUnitAddProduct.text.toString())
        outState.putInt("quantityAdd", binding.editUnitAddProduct.text.toString().toInt())

        // save the image here
    }




}