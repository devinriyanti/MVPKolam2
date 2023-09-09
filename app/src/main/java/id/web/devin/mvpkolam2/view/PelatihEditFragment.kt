package id.web.devin.mvpkolam2.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import id.web.devin.mvpkolam2.presenter.PelatihPresenter
import id.web.devin.mvpkolam2.databinding.FragmentPelatihEditBinding
import id.web.devin.mvpkolam2.model.Pelatih
import id.web.devin.mvpkolam2.util.PelatihPresenterListener
import id.web.devin.mvpkolam2.util.formatDate
import id.web.devin.mvpkolam2.util.formatDate2
import java.text.SimpleDateFormat
import java.util.*

class PelatihEditFragment : Fragment(),PelatihPresenterListener {
    private lateinit var b:FragmentPelatihEditBinding
    private lateinit var cPelatih:PelatihPresenter
    private var nama:String = ""
    private var tglLahir:String = ""
    private var deskripsi:String = ""
    private var noTelepon:String = ""
    private var mulaiKarir:String = ""
    val today = Calendar.getInstance()
    val year = today.get(Calendar.YEAR)
    val month = today.get(Calendar.MONTH)
    val day = today.get(Calendar.DAY_OF_MONTH)
    private var idpelatih:String=""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        cPelatih = PelatihPresenter(this.requireContext(),this)
        b = FragmentPelatihEditBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(arguments != null){
            idpelatih= PelatihEditFragmentArgs.fromBundle(requireArguments()).idpelatih
            cPelatih.fetchPelatih(idpelatih)

            b.editTextTglLahirPelatihEdit.setOnClickListener {
                var picker = context?.let { it1 ->
                    DatePickerDialog(
                        it1,
                        DatePickerDialog.OnDateSetListener { datePicker, selYear, selMonth, selDay ->
                            val calender = Calendar.getInstance()
                            calender.set(selYear,selMonth,selDay)

                            var dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                            var str = dateFormat.format(calender.time)
                            b.editTextTglLahirPelatihEdit.setText(str)
                        }, year, month, day)
                }
                picker?.show()
            }

            b.editTextTglKarirPelatihEdit.setOnClickListener {
                var picker = context?.let { it1 ->
                    DatePickerDialog(
                        it1,
                        DatePickerDialog.OnDateSetListener { datePicker, selYear, selMonth, selDay ->
                            val calender = Calendar.getInstance()
                            calender.set(selYear,selMonth,selDay)

                            var dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                            var str = dateFormat.format(calender.time)
                            b.editTextTglKarirPelatihEdit.setText(str)
                        }, year, month, day)
                }
                picker?.show()
            }

            b.btnEditPelatih.setOnClickListener {
                nama = b.editTextNamaPelatihEdit.text.toString()
                tglLahir = b.editTextTglLahirPelatihEdit.text.toString()
                deskripsi = b.editTextDeskripsiPelatihEdit.text.toString()
                noTelepon = b.editTextTeleponPelatihEdit.text.toString()
                mulaiKarir = b.editTextTglKarirPelatihEdit.text.toString()

                if(nama.isNotEmpty() && tglLahir.isNotEmpty() && deskripsi.isNotEmpty() && noTelepon.isNotEmpty() && mulaiKarir.isNotEmpty()){
                    cPelatih.updatePelatih(nama, formatDate2(tglLahir),noTelepon,
                        formatDate2(mulaiKarir),deskripsi,idpelatih)
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
            b.btnBatalEditPelatih.setOnClickListener {
                AlertDialog.Builder(context).apply {
                    val title = SpannableString("Peringatan")
                    title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                    val message = SpannableString("Batal Mengubah Data Pelatih?")
                    message.setSpan(
                        AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0,
                        message.length,
                        0
                    )
                    setTitle(title)
                    setMessage(message)
                    setPositiveButton("Batal"){ dialog,_->
                        val action = PelatihEditFragmentDirections.actionEditToPelatihDetailFragment(idpelatih)
                        Navigation.findNavController(it).navigate(action)
                    }
                    setNegativeButton("Tidak"){ dialog,_->
                        dialog.dismiss()
                    }
                    create().show()
                }
            }
        }
    }

    override fun showError(message: String) {
        Log.d("error",message)
        AlertDialog.Builder(context).apply {
            val message = SpannableString("Gagal Mengubah Data Pelatih")
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

    override fun showPelatih(pelatih: List<Pelatih>) {
        pelatih.forEach {
            b.editTextNamaPelatihEdit.setText(it.nama)
            b.editTextTglLahirPelatihEdit.setText(formatDate(it.tglLahir.toString()))
            b.editTextDeskripsiPelatihEdit.setText(it.deskripsi)
            b.editTextTeleponPelatihEdit.setText(it.kontak)
            b.editTextTglKarirPelatihEdit.setText(formatDate(it.tglKarir.toString()))
        }
    }

    override fun succes() {
        AlertDialog.Builder(context).apply {
            val message = SpannableString("Berhasil Mengubah Data Pelatih")
            message.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0,
                message.length,
                0
            )
            setMessage(message)
            setPositiveButton("OK") { _, _ ->
                val action = PelatihEditFragmentDirections.actionEditToPelatihDetailFragment(idpelatih)
                findNavController().navigate(action)
            }
            create().show()
        }
    }
}