package id.web.devin.mvpkolam2.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.web.devin.mvpkolam2.databinding.CheckoutItemBinding
import id.web.devin.mvpkolam2.model.ProdukCart
import id.web.devin.mvpkolam2.util.formatCurrency
import id.web.devin.mvpkolam2.util.loadImage

class CheckoutItemAdapter(val checkoutList:ArrayList<ProdukCart>):RecyclerView.Adapter<CheckoutItemAdapter.CartDetailViewHolder>() {
    class CartDetailViewHolder(val b: CheckoutItemBinding):RecyclerView.ViewHolder(b.root)

    fun updateCheckoutItem(newCheckoutItem:List<ProdukCart>){
        checkoutList.clear()
        checkoutList.addAll(newCheckoutItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartDetailViewHolder {
        val b = CheckoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartDetailViewHolder(b)
    }

    override fun getItemCount(): Int {
        return checkoutList.size
    }

    override fun onBindViewHolder(holder: CartDetailViewHolder, position: Int) {
        var item = checkoutList[position]
        with(holder.b){
            imgProdukCO.loadImage(item.gambar, progressProdukCO)
            txtProdukCO.text = item.namaProduk
            if(item.diskon == 0.0){
                txtHargaCO.text = formatCurrency(item.harga)
            }else{
                val harga = item.harga - (item.harga* item.diskon!! /100)
                txtHargaCO.text = formatCurrency(harga)
            }
            txtQtyCO.text = "x ${item.qty}"
        }
    }

}