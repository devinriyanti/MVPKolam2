package id.web.devin.mvpkolam2.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import id.web.devin.mvpkolam2.presenter.CartPresenter
import id.web.devin.mvpkolam2.presenter.ProductPresenter
import id.web.devin.mvpkolam2.databinding.FragmentProductDetailBinding
import id.web.devin.mvpkolam2.model.Cart
import id.web.devin.mvpkolam2.model.Produk
import id.web.devin.mvpkolam2.model.Role
import id.web.devin.mvpkolam2.util.*

class ProductDetailFragment : Fragment(), ProductPresenterListener, CartPresenterListener {
    private lateinit var b:FragmentProductDetailBinding
    private lateinit var cProduk:ProductPresenter
    private lateinit var cCart:CartPresenter
    private var idKolam:String? = null
    private var qty:Int? = 0
    private var total:Double? = null
    private var email:String? = null
    private var role:String? = null
    private var produkID:String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cProduk = ProductPresenter(this.requireContext(),this)
        cCart = CartPresenter(this.requireContext(),this)
        b = FragmentProductDetailBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = context?.let { Global.getEmail(it) }.toString()
        role = context?.let { Global.getRole(it) }.toString()
        if(arguments != null){
            produkID = ProductDetailFragmentArgs.fromBundle(requireArguments()).produkID
            cProduk.fetchProduct(produkID.toString())
            b.btnEditProdukDetail.setOnClickListener {
                val action = ProductDetailFragmentDirections.actionProductEditFragment(produkID.toString())
                Navigation.findNavController(it).navigate(action)
            }

            b.switchArsipProduk.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    cProduk.updateStatus(1,produkID.toString())
                } else {
                    cProduk.updateStatus(0,produkID.toString())
                }
            }
        }
    }

    override fun showError(message: String) {
        b.progressBarDetailProduk.visibility = View.GONE
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showProduk(produk: List<Produk>) {
        b.progressBarDetailProduk.visibility = View.GONE
        produk.forEach {
            b.switchArsipProduk.isChecked = it.status.equals("1")
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
                b.switchArsipProduk.visibility = View.VISIBLE
            }else{
                b.btnTambahKeranjang.visibility = View.VISIBLE
                b.switchArsipProduk.visibility = View.GONE
                b.btnTambahKeranjang.setOnClickListener {
                    qty = qty!! + 1 // salah, contoh aja
                    total = qty!! * (harga2!!-(harga2 * diskon!!/100))
                    cCart.addToCart(idKolam!!, total!!,email!!, idproduk!!, qty!!, harga2, diskon)
                    Toast.makeText(context,"Berhasil Ditambahkan $qty", Toast.LENGTH_SHORT).show()
                }
                b.btnEditProdukDetail.visibility = View.GONE
            }
        }
    }

    override fun success() {}
    override fun showCart(cart: List<Cart>) {}
}