package id.web.devin.mvpkolam2.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
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
import id.web.devin.mvpkolam2.presenter.PelatihPresenter
import id.web.devin.mvpkolam2.presenter.UploadPresenter
import id.web.devin.mvpkolam2.databinding.FragmentPelatihAddBinding
import id.web.devin.mvpkolam2.model.Pelatih
import id.web.devin.mvpkolam2.model.UploadResponse
import id.web.devin.mvpkolam2.util.PelatihPresenterListener
import id.web.devin.mvpkolam2.util.UploadPresenterListener
import id.web.devin.mvpkolam2.util.formatDate2
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PelatihAddFragment : Fragment(), UploadPresenterListener,PelatihPresenterListener {
    private lateinit var b: FragmentPelatihAddBinding
    private lateinit var cPelatih:PelatihPresenter
    private lateinit var cUpload:UploadPresenter
    private var nama:String = ""
    private var tglLahir:String = ""
    private var deskripsi:String = ""
    private var noTelepon:String = ""
    private var mulaiKarir:String = ""
    private var kolamID:String = ""
    val today = Calendar.getInstance()
    val year = today.get(Calendar.YEAR)
    val month = today.get(Calendar.MONTH)
    val day = today.get(Calendar.DAY_OF_MONTH)
    private val imagePickRequestCode = 100
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cUpload = UploadPresenter(this)
        cPelatih = PelatihPresenter(this.requireContext(),this)
        b = FragmentPelatihAddBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireActivity().getSharedPreferences("kolam", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", null)
        kolamID = id.toString()

        b.editTextTglKarirPelatihAdd.setOnClickListener {
            var picker = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { datePicker, selYear, selMonth, selDay ->
                        val calender = Calendar.getInstance()
                        calender.set(selYear,selMonth,selDay)

                        var dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                        var str = dateFormat.format(calender.time)
                        b.editTextTglKarirPelatihAdd.setText(str)
                    }, year, month, day)
            }
            picker?.show()
        }

        b.editTextTglLahirPelatihAdd.setOnClickListener {
            var picker = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { datePicker, selYear, selMonth, selDay ->
                        val calender = Calendar.getInstance()
                        calender.set(selYear,selMonth,selDay)

                        var dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                        var str = dateFormat.format(calender.time)
                        b.editTextTglLahirPelatihAdd.setText(str)
                    }, year, month, day)
            }
            picker?.show()
        }

        b.btnPilihFilePelatih.setOnClickListener {
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

        b.btnAddPelatih.setOnClickListener {
            nama = b.editTextNamaPelatihAdd.text.toString()
            tglLahir = formatDate2(b.editTextTglLahirPelatihAdd.text.toString())
            deskripsi = b.editTextDeskripsiPelatihAdd.text.toString()
            noTelepon = b.editTextTeleponPelatihAdd.text.toString()
            mulaiKarir = formatDate2(b.editTextTglKarirPelatihAdd.text.toString())

            if(nama.isNotEmpty() && tglLahir.isNotEmpty() && deskripsi.isNotEmpty() && noTelepon.isNotEmpty() && mulaiKarir.isNotEmpty()){
                if (selectedImageUri != null){
                    // Dapatkan path file dari Uri
                    val pelatih = nama.replace(" ", "")
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = requireContext().contentResolver.query(selectedImageUri!!, filePathColumn, null, null, null)

                    if (cursor != null) {
                        cursor.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val imagePath = cursor.getString(columnIndex)
                        cursor.close()
                        val file = File(imagePath)
                        val requestBody = RequestBody.create("application/octet-stream".toMediaTypeOrNull(), file)
                        val imagePart = MultipartBody.Part.createFormData("image", "PL$pelatih.jpg", requestBody)
                        val url = "https://lokowai.shop/image/pelatih/PL$pelatih.jpg"
                        val folderValue = "pelatih"
                        val folderRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), folderValue)
                        b.txtPilihFilePelatih.setText("PL$pelatih.jpg")
                        cUpload.uploadImage(imagePart,folderRequestBody)
                        cPelatih.insertPelatih(nama,tglLahir,noTelepon,mulaiKarir,url,deskripsi,kolamID)
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

        b.btnBatalAddPelatih.setOnClickListener {
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
                    val action = PelatihAddFragmentDirections.actionPAKolamDetailFragment()
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
            b.txtPilihFilePelatih.setText("Gambar Terpilih")
        }
    }

    override fun showError(message: String) {
        Log.d("error",message)
        AlertDialog.Builder(context).apply {
            val message = SpannableString("Gagal Menambahkan Pelatih")
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

    override fun succes() {
        AlertDialog.Builder(context).apply {
            val message = SpannableString("Berhasil Menambah Pelatih")
            message.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0,
                message.length,
                0
            )
            setMessage(message)
            setPositiveButton("OK") { _, _ ->
                val action = PelatihAddFragmentDirections.actionPAKolamDetailFragment()
                findNavController().navigate(action)
            }
            create().show()
        }
    }

    override fun uploadError(message: String) {}
    override fun UploadSuccess(upload: UploadResponse) {}
    override fun showPelatih(pelatih: List<Pelatih>) {}
}