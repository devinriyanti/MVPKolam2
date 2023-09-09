package id.web.devin.mvpkolam2.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import id.web.devin.mvpkolam2.presenter.ProductPresenter
import id.web.devin.mvpkolam2.presenter.UploadPresenter
import id.web.devin.mvpkolam2.databinding.FragmentProductAddBinding
import id.web.devin.mvpkolam2.model.Produk
import id.web.devin.mvpkolam2.model.UploadResponse
import id.web.devin.mvpkolam2.util.ProductPresenterListener
import id.web.devin.mvpkolam2.util.UploadPresenterListener
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ProductAddFragment : Fragment(), ProductPresenterListener, UploadPresenterListener {
    private lateinit var b: FragmentProductAddBinding
    private lateinit var cProduk: ProductPresenter
    private lateinit var cUpload: UploadPresenter
    private var nama:String = ""
    private var qty:Int = 0
    private var deskripsi:String = ""
    private var harga:Double = 0.0
    private var diskon:Double = 0.0
    private var berat:Int = 0
    private var kolamID:String = ""
    private val imagePickRequestCode = 100
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cProduk = ProductPresenter(this.requireContext(),this)
        cUpload = UploadPresenter(this)
        b = FragmentProductAddBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireActivity().getSharedPreferences("kolam", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", null)
        kolamID = id.toString()

        b.btnPilihFileProduk.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openImagePicker()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    imagePickRequestCode
                )
            }
        }

        b.btnAddProduk.setOnClickListener {
            nama = b.editTextNamaProdukAdd.text.toString()
            qty = b.editTextKuantitasProdukAdd.text.toString().toInt()
            deskripsi = b.editTextDeskripsiProdukAdd.text.toString()
            harga = b.editTextHargaProdukAdd.text.toString().toDouble()
            diskon = b.editTextDiskonProdukAdd.text.toString().toDouble()
            berat = b.editTextBeratProdukAdd.text.toString().toInt()

            if (nama.isNotEmpty() && qty.toString().isNotEmpty() && deskripsi.isNotEmpty() && harga.toString().isNotEmpty() && diskon.toString().isNotEmpty() && berat.toString().isNotEmpty()){
                if (selectedImageUri != null){
                    // Dapatkan path file dari Uri
                    val produk = nama.replace(" ", "")
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = requireContext().contentResolver.query(selectedImageUri!!, filePathColumn, null, null, null)

                    if (cursor != null) {
                        cursor.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val imagePath = cursor.getString(columnIndex)
                        cursor.close()
                        val file = File(imagePath)
                        val requestBody = RequestBody.create("application/octet-stream".toMediaTypeOrNull(), file)
                        val imagePart = MultipartBody.Part.createFormData("image", "PD$produk.jpg", requestBody)
                        val url = "https://lokowai.shop/image/produk/PD$produk.jpg"
                        val folderValue = "produk"
                        val folderRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), folderValue)
                        b.txtPilihFileProduk.setText("PD$produk.jpg")
                        cUpload.uploadImage(imagePart,folderRequestBody)
                        cProduk.insertProduct(nama,deskripsi,qty,harga,diskon,url,berat,kolamID)
                    }
                }else{
                    AlertDialog.Builder(context).apply {
                        val message = SpannableString("Anda belum memilih foto")
                        message.setSpan(
                            AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                            0,
                            message.length,
                            0
                        )
                        setMessage(message)
                        setPositiveButton("OK", null)
                        create().show()
                    }
                }
            }else{
                AlertDialog.Builder(context).apply {
                    val message = SpannableString("Data Tidak Boleh Kosong!")
                    message.setSpan(
                        AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0,
                        message.length,
                        0
                    )
                    setMessage(message)
                    setPositiveButton("OK", null)
                    create().show()
                }
            }
        }
        b.btnBatalAddProduk.setOnClickListener {
            AlertDialog.Builder(context).apply {
                val title = SpannableString("Peringatan")
                title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                val message = SpannableString("Batal Menambahkan Pelatih?")
                message.setSpan(
                    AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    0,
                    message.length,
                    0
                )
                setTitle(title)
                setMessage(message)
                setPositiveButton("BATAL"){ dialog,_->
                    val action = ProductAddFragmentDirections.actionPDAKolamDetailFragment()
                    Navigation.findNavController(it).navigate(action)
                }
                setNegativeButton("TIDAK"){ dialog,_->
                    dialog.dismiss()
                }
                create().show()
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, imagePickRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == imagePickRequestCode && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            b.txtPilihFileProduk.setText("Gambar Terpilih")
        }
    }

    override fun success() {
        AlertDialog.Builder(context).apply {
            val message = SpannableString("Berhasil Menambah Produk")
            message.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0,
                message.length,
                0
            )
            setMessage(message)
            setPositiveButton("OK") { _, _ ->
                val action = ProductAddFragmentDirections.actionPDAKolamDetailFragment()
                findNavController().navigate(action)
            }
            create().show()
        }
    }

    override fun showError(message: String) {
        Log.d("error",message)
        AlertDialog.Builder(context).apply {
            val message = SpannableString("Gagal Menambahkan Produk")
            message.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0,
                message.length,
                0
            )
            setMessage(message)
            setPositiveButton("OK", null)
            create().show()
        }
    }

    override fun uploadError(message: String) {}
    override fun showProduk(produk: List<Produk>) {}
    override fun UploadSuccess(upload: UploadResponse) {}
}