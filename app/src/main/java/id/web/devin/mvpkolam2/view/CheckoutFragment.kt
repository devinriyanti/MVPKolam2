package id.web.devin.mvpkolam2.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import id.web.devin.mvpkolam2.presenter.*
import id.web.devin.mvpkolam2.databinding.FragmentCheckoutBinding
import id.web.devin.mvpkolam2.model.*
import id.web.devin.mvpkolam2.util.*

class CheckoutFragment : Fragment(), ProfilePresenterListener,CartDetailPresenterListener,TransaksiPresenterListener, ShippingPresenterListener {
    private lateinit var b:FragmentCheckoutBinding
    private lateinit var cPengguna:ProfilePresenter
    private lateinit var cCartDetail:CartDetailPresenter
    private lateinit var cTransaksi:TransaksiPresenter
    private lateinit var cShippingCost: ShippingPresenter
    private lateinit var checkoutItemAdapter: CheckoutItemAdapter
    private var email:String = ""
    private var idKolam:String = ""
    private var tujuan:String = ""
    private var asal:String = ""
    private var berat:Int = 0
    private var subtotal:Int = 0
    private var pengiriman:Int = 0
    private var totalBerat:Int = 0
    private var idkeranjang: Int = 0
    private var alamat: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cPengguna = ProfilePresenter(this.requireContext(), this)
        cCartDetail = CartDetailPresenter(this.requireContext(), this)
        cTransaksi = TransaksiPresenter(this.requireContext(), this)
        cShippingCost = ShippingPresenter(this)
        b = FragmentCheckoutBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = context?.let { Global.getEmail(it) }.toString()
        checkoutItemAdapter = CheckoutItemAdapter(arrayListOf())

        val sharedPreferences = requireActivity().getSharedPreferences("idkolam", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", null)
        if (id != null) {
            idKolam = id
        }
        Log.d("id, cart", "$email dan $idKolam")
        cPengguna.fetchProfil(email)
        cCartDetail.fetchCartDetail(email, idKolam)

        checkoutItemAdapter = CheckoutItemAdapter(arrayListOf())
        b.recViewProdukCO.layoutManager = LinearLayoutManager(context)
        b.recViewProdukCO.adapter = checkoutItemAdapter
    }

    override fun showError(message: String) {
        Log.d("error", message)
    }
    override fun showShipping(result: ShippingResponse?) {
        b.progressCheckout.visibility = View.GONE
        result?.rajaongkir?.results?.forEach {
            var ongkir =  it.costs[0].cost[0].value
            val jasaKirim = it.name
            val layanan = it.costs[0].service
            val estimasi = it.costs[0].cost[0].etd
            pengiriman = ongkir
            b.txtJasaKirim.text = jasaKirim
            b.txtLayananPengiriman.text = layanan
            b.txtEstimasiPengiriman.text = "$estimasi Hari"
            b.txtTotalPengirimanCO.text = formatCurrency(ongkir.toDouble())

            updateTotal()
        }
    }
    override fun showCart(cart: List<Cart>) {
        Log.d("cart", cart.toString())
        cart.forEach {
            checkoutItemAdapter.updateCheckoutItem(it.produk)

            b.txtNamaKolamCO.text = it.nama
            asal = it.idkota

            it.produk.forEach {produk->
                b.txtSubtotalCO.text = formatCurrency(produk.total_harga)
                b.txtTotalPesananCO.text = formatCurrency(produk.total_harga)
                subtotal = produk.total_harga.toInt()
                idkeranjang = produk.idkeranjangs
                if(produk.qty > 1){
                    berat = produk.berat.toInt() * produk.qty
                }else{
                    berat = produk.berat.toInt()
                }

                totalBerat += berat
                Log.d("asal", it.idkota)
                updateTotal()
            }
            cShippingCost.fetchShippingCosts(asal,tujuan,totalBerat)
        }

    }

    override fun showProfile(profileData: List<Pengguna>) {
        profileData.forEach {
            b.txtNamaCO.text = "${it.nama} |"
            b.txtAlamatCO.text = "${it.alamat}, ${it.kota}"
            b.txtTeleponCO.text = it.telepon
            tujuan = it.idkota
            alamat = it.alamat.toString()
            Log.d("tujuan", it.idkota.toString())
            updateTotal()
            cShippingCost.fetchShippingCosts(asal,tujuan,totalBerat)
        }
    }
    private fun updateTotal(){
        val total = subtotal + pengiriman
        val sharedPreferences = context?.getSharedPreferences("totalPembelian", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor!!.putString("total", total.toString())
        editor!!.apply()
        b.txtTotalPembayaranCO.text = formatCurrency(total.toDouble())
        b.btnBuatPesananCO.setOnClickListener {
            cTransaksi.insertTransaksi(pengiriman, idkeranjang, idKolam, email, tujuan.toInt(), alamat)
        }
    }
    override fun showTransaksi(transaksi: List<Transaction>) {}
    override fun success() {
        AlertDialog.Builder(context).apply {
            val message = SpannableString("Pesanan Berhasil Dibuat")
            message.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0,
                message.length,
                0
            )
            setMessage(message)
            setPositiveButton("OK") { _, _ ->
                val action = CheckoutFragmentDirections.actionPembayaranFragment()
                findNavController().navigate(action)
            }
            create().show()
        }
    }

}