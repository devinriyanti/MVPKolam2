package id.web.devin.mvpkolam2.view

import android.app.AlertDialog
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import id.web.devin.mvpkolam2.presenter.ProductPresenter
import id.web.devin.mvpkolam2.databinding.FragmentProductEditBinding
import id.web.devin.mvpkolam2.model.Produk
import id.web.devin.mvpkolam2.util.ProductPresenterListener

class ProductEditFragment : Fragment(), ProductPresenterListener {
    private lateinit var b: FragmentProductEditBinding
    private lateinit var cProduk:ProductPresenter
    private var nama:String = ""
    private var qty:Int = 0
    private var deskripsi:String = ""
    private var harga:Double = 0.0
    private var diskon:Double = 0.0
    private var berat:Int = 0
    private var idproduk:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cProduk = ProductPresenter(this.requireContext(),this)
         b= FragmentProductEditBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(arguments != null){
            idproduk = ProductEditFragmentArgs.fromBundle(requireArguments()).idproduk
            cProduk.fetchProduct(idproduk)

            b.btnEditProduk.setOnClickListener {
                nama = b.editTextNamaProdukEdit.text.toString()
                qty = b.editTextKuantitasProdukEdit.text.toString().toInt()
                deskripsi = b.editTextDeskripsiProdukEdit.text.toString()
                harga = b.editTextHargaProdukEdit.text.toString().toDouble()
                diskon = b.editTextDiskonProdukEdit.text.toString().toDouble()
                berat = b.editTextBeratProdukEdit.text.toString().toInt()

                if(nama.isNotEmpty() && qty.toString().isNotEmpty() && deskripsi.isNotEmpty() && harga.toString().isNotEmpty() && diskon.toString().isNotEmpty() && berat.toString().isNotEmpty()){
                    cProduk.updateProduk(nama, deskripsi,qty,harga,diskon,berat,idproduk)
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
            b.btnBatalEditProduk.setOnClickListener {
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
                        val action = ProductEditFragmentDirections.actioEditToProductDetailFragment(idproduk)
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
        AlertDialog.Builder(context).apply {
            val message = SpannableString("Gagal Mengubah Data Produk")
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

    override fun showProduk(produk: List<Produk>) {
        produk.forEach {
            b.editTextNamaProdukEdit.setText(it.nama)
            b.editTextDeskripsiProdukEdit.setText(it.deskripsi)
            b.editTextKuantitasProdukEdit.setText(it.qty.toString())
            b.editTextHargaProdukEdit.setText(it.harga.toString())
            b.editTextDiskonProdukEdit.setText(it.diskon.toString())
            b.editTextBeratProdukEdit.setText(it.berat.toString())
        }
    }

    override fun success() {
        AlertDialog.Builder(context).apply {
            val message = SpannableString("Berhasil Mengubah Data Produk")
            message.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0,
                message.length,
                0
            )
            setMessage(message)
            setPositiveButton("OK") { _, _ ->
                val action = ProductEditFragmentDirections.actioEditToProductDetailFragment(idproduk)
                findNavController().navigate(action)
            }
            create().show()
        }
    }
}