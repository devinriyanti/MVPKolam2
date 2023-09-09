package id.web.devin.mvpkolam2.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import id.web.devin.mvpkolam2.presenter.KolamPresenter
import id.web.devin.mvpkolam2.presenter.UploadPresenter
import id.web.devin.mvpkolam2.databinding.FragmentKolamAddBinding
import id.web.devin.mvpkolam2.model.Kolam
import id.web.devin.mvpkolam2.model.UploadResponse
import id.web.devin.mvpkolam2.util.Global
import id.web.devin.mvpkolam2.util.KolamPresenterListener
import id.web.devin.mvpkolam2.util.UploadPresenterListener
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class KolamAddFragment : Fragment(), UploadPresenterListener, KolamPresenterListener {
    private lateinit var b: FragmentKolamAddBinding
    private lateinit var cUpload:UploadPresenter
    private lateinit var cKolam:KolamPresenter
    private var namaKolam:String = ""
    private var alamat:String = ""
    private var deskripsi:String = ""
    private var urlLokasi:String = ""
    private var email:String =""
    private val imagePickRequestCode = 100
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cUpload = UploadPresenter(this)
        cKolam = KolamPresenter(this.requireContext(),this)
        b = FragmentKolamAddBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = context?.let { Global.getEmail(it) }.toString()

        b.btnPilihFileKolam.setOnClickListener {
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

        b.btnAddKolam.setOnClickListener {
            namaKolam = b.txtNamaKolamAdd.text.toString()
            alamat = b.txtAlamatKolamAdd.text.toString()
            deskripsi = b.txtDeskripsiKolamAdd.text.toString()
            urlLokasi = b.txtLokasiKolamAdd.text.toString()

            if(namaKolam.isNotEmpty() && alamat.isNotEmpty() && deskripsi.isNotEmpty() && urlLokasi.isNotEmpty()){
                if (selectedImageUri != null){
                    // Dapatkan path file dari Uri
                    val kolam = namaKolam.replace(" ", "")
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = requireContext().contentResolver.query(selectedImageUri!!, filePathColumn, null, null, null)

                    if (cursor != null) {
                        cursor.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val imagePath = cursor.getString(columnIndex)
                        cursor.close()
                        val file = File(imagePath)
                        val requestBody = RequestBody.create("application/octet-stream".toMediaTypeOrNull(), file)
                        val imagePart = MultipartBody.Part.createFormData("image", "KL$kolam.jpg", requestBody)
                        val url = "https://lokowai.shop/image/kolam/KL$kolam.jpg"
                        val folderValue = "kolam"
                        val folderRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), folderValue)
                        b.txtPilihFileKolam.setText("KL$kolam.jpg")
                        cUpload.uploadImage(imagePart,folderRequestBody)
                        cKolam.insertKolam(namaKolam,alamat,deskripsi,url,urlLokasi,email)
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

        b.btnBatalAdd.setOnClickListener {
            AlertDialog.Builder(context).apply {
                val title = SpannableString("Peringatan")
                title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                val message = SpannableString("Batal Menambah Kolam?")
                message.setSpan(
                    AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    0,
                    message.length,
                    0
                )
                setTitle(title)
                setMessage(message)
                setPositiveButton("Batal"){ dialog,_->
                    val action = KolamAddFragmentDirections.actionAddToItemRumah()
                    Navigation.findNavController(it).navigate(action)
                }
                setNegativeButton("Tidak"){ dialog,_->
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
            b.txtPilihFileKolam.setText("Gambar Terpilih")
        }
    }

    override fun showError(message: String) {
        AlertDialog.Builder(context).apply {
            val message = SpannableString("Gagal Menambahkan Kolam")
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

    override fun success() {
        AlertDialog.Builder(context).apply {
            val message = SpannableString("Berhasil Menambah Kolam")
            message.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0,
                message.length,
                0
            )
            setMessage(message)
            setPositiveButton("OK") { _, _ ->
                val action = KolamAddFragmentDirections.actionAddToItemRumah()
                findNavController().navigate(action)
            }
            create().show()
        }
    }

    override fun showKolam(kolam: List<Kolam>) {}
    override fun uploadError(message: String) {}
    override fun UploadSuccess(upload: UploadResponse) {}
}