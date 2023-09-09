package id.web.devin.mvpkolam2.view

import android.content.Context
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import id.web.devin.mvpkolam2.databinding.ProductListItemBinding
import id.web.devin.mvpkolam2.model.Produk
import id.web.devin.mvpkolam2.model.Role
import id.web.devin.mvpkolam2.util.Global
import id.web.devin.mvpkolam2.util.formatCurrency
import id.web.devin.mvpkolam2.util.loadImage

class ProductListAdapter(private val context: Context, val produkList:ArrayList<Produk>):RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {
    class ProductViewHolder(val b: ProductListItemBinding):RecyclerView.ViewHolder(b.root)

    fun updateProductList(newProductList:ArrayList<Produk>){
        produkList.clear()
        produkList.addAll(newProductList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val b = ProductListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(b)
    }

    override fun getItemCount(): Int {
        return produkList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val produk = produkList[position]
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val colorFilter = ColorMatrixColorFilter(colorMatrix)

        with(holder.b){
            txtNamaProduk.text = produk.nama
            val harga = produk?.harga?.let { formatCurrency(it) }
            txtHargaProduk.text = harga
            txtKota.text = produk.kota
            val id = produk.idproduk.toString()
            val role = Global.getRole(context)
            imageProduct.loadImage(produk.gambarUrl.toString(), progressBarProduk)

            if(produk.qty == 0 && produk.status.equals("0")){
                txtStatusProduk.visibility = View.VISIBLE
                txtStatusProduk.setText("Produk Habis")
                txtStatusProduk.setTextColor(Color.RED)
                imageProduct.colorFilter = colorFilter
                cardViewProduk.setOnClickListener {
                    val action = KolamDetailFragmentDirections.actionProductDetailFragment(id)
                    Navigation.findNavController(it).navigate(action)
                }
            }else{
                txtStatusProduk.visibility = View.GONE
                cardViewProduk.setOnClickListener {
                    val action = KolamDetailFragmentDirections.actionProductDetailFragment(id)
                    Navigation.findNavController(it).navigate(action)
                }
            }

            if(produk.status.equals("1") && produk.qty != 0 || produk.status.equals("1") && produk.qty == 0){
                txtStatusProduk.visibility = View.VISIBLE
                txtStatusProduk.setText("Produk Tidak Tersedia")
                txtStatusProduk.setTextColor(Color.RED)
                imageProduct.colorFilter = colorFilter
                if(role == Role.Admin.name){
                    cardViewProduk.setOnClickListener {
                        val action = KolamDetailFragmentDirections.actionProductDetailFragment(id)
                        Navigation.findNavController(it).navigate(action)
                    }
                }else{
                    cardViewProduk.setOnClickListener {
                        Toast.makeText(context, "Tidak Tersedia",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


}