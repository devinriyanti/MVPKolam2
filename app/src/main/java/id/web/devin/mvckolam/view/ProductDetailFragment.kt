package id.web.devin.mvckolam.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import id.web.devin.mvckolam.R
import id.web.devin.mvckolam.controller.ProductController
import id.web.devin.mvckolam.databinding.FragmentProductDetailBinding
import id.web.devin.mvckolam.model.Produk
import id.web.devin.mvckolam.model.Role
import id.web.devin.mvckolam.util.Global
import id.web.devin.mvckolam.util.ProductControllerListener
import id.web.devin.mvckolam.util.formatCurrency
import id.web.devin.mvckolam.util.loadImage

class ProductDetailFragment : Fragment(), ProductControllerListener {
    private lateinit var b:FragmentProductDetailBinding
    private lateinit var cProduk:ProductController
//    private val cartViewModel: CartViewModel by viewModels()
    private var idKolam:String? = null
    private var qty:Int? = 0
    private var total:Double? = null
    private var email:String? = null
    private var role:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cProduk = ProductController(this.requireContext(),this)
        b = FragmentProductDetailBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = context?.let { Global.getEmail(it) }.toString()
        role = context?.let { Global.getRole(it) }.toString()
        if(arguments != null){
            val produkID = ProductDetailFragmentArgs.fromBundle(requireArguments()).produkID
            cProduk.fetchProduct(produkID)
        }
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showProduk(produk: List<Produk>) {
        produk.forEach {
            b.txtNamaProductDetail.text = it.nama
            val harga = it.harga?.let { it -> formatCurrency(it) }
            val diskon = it.diskon
            b.txtHargaProductDetail.text = harga
            b.txtDeskripsi.text = it.deskripsi
            b.txtBeratProduk.text = "${it.berat?.toInt()} gr"
            if(!diskon!!.equals(0.0)){
                b.txtDiskonProduk.text = "Diskon ${diskon.toInt()}%"
            }else{
                b.txtDiskonProduk.visibility = View.GONE
            }
            b.imageProductDetail.loadImage(it.gambarUrl.toString(),b.progressBarDetailProduk)

            val sharedPreferences = requireActivity().getSharedPreferences("kolam", Context.MODE_PRIVATE)
            idKolam = sharedPreferences.getString("id", null)
            val harga2 = it.harga
            val idproduk = it.idproduk

            //jangan lupa cek qty di produk ada atau tidak
            //...code here
            if(role == Role.Admin.toString()){
                b.btnTambahKeranjang.visibility = View.GONE
                b.btnEditProdukDetail.visibility = View.VISIBLE
                b.btnHapusProdukDetail.visibility = View.VISIBLE
            }else{
                b.btnTambahKeranjang.visibility = View.VISIBLE
                b.btnTambahKeranjang.setOnClickListener {
                    qty = qty!! + 1 // salah, contoh aja
                    total = qty!! * (harga2!!-(harga2 * diskon!!/100))
//                    cartViewModel.addToCart(idKolam!!, total!!,email!!, idproduk!!, qty!!, harga2, diskon)
                    Toast.makeText(context,"Berhasil Ditambahkan $qty", Toast.LENGTH_SHORT).show()
                }
                b.btnEditProdukDetail.visibility = View.GONE
                b.btnHapusProdukDetail.visibility = View.GONE
            }
        }
    }
}