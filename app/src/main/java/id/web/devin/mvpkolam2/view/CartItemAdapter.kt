package id.web.devin.mvpkolam2.view

import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import id.web.devin.mvpkolam2.databinding.CartItemBinding
import id.web.devin.mvpkolam2.model.ProdukCart
import id.web.devin.mvpkolam2.util.formatCurrency
import id.web.devin.mvpkolam2.util.loadImage

class CartItemAdapter(val cartItemList:ArrayList<ProdukCart>, val cartItemListener: CartItemListener):RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {
    class CartItemViewHolder(val b: CartItemBinding):RecyclerView.ViewHolder(b.root)
    interface CartItemListener {
        fun onRemoveClicked(cartItem: ProdukCart)
        fun onDecreaseClicked(cartItem: ProdukCart)
        fun onIncreaseClicked(cartItem: ProdukCart)
    }
    fun updateCartItem(newCartItem:ArrayList<ProdukCart>){
        cartItemList.clear()
        cartItemList.addAll(newCartItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val b = CartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartItemViewHolder(b)
    }

    override fun getItemCount(): Int {
        return cartItemList.size
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val cartItem = cartItemList[position]
        with(holder.b){
            txtNamaProdukCart.text = cartItem.namaProduk
            val harga = cartItem.harga?.let { formatCurrency(it) }
            if (cartItem.diskon == 0.0){
                txtHargaProdukCart.text = harga
            }else{
                val hargaDiskon = cartItem.harga - (cartItem.harga * cartItem.diskon!! /100)
                txtHargaProdukCart.text = Html.fromHtml("<strike>$harga</strike>", Html.FROM_HTML_MODE_LEGACY)
                txtHargaProdukCart.setTextColor(Color.parseColor("#9BA4B5"))
                txtHargaDiskon.text = formatCurrency(hargaDiskon)
                txtHargaDiskon.setTextColor(Color.parseColor("#F99B7D"))
            }
            var qty = cartItem.qty
            txtQtyProdukCart.text = qty.toString()
            imgProdukCart.loadImage(cartItem.gambar,progressLoadCartItem)

            imgHapus.setOnClickListener {
                cartItemListener.onRemoveClicked(cartItem)
                Toast.makeText(root.context,"Dihapus", Toast.LENGTH_SHORT).show()
            }

            imgKurang.setOnClickListener {
                if(qty < 1){
                    cartItemListener.onRemoveClicked(cartItem)
//                    Toast.makeText(root.context,"Dihapus", Toast.LENGTH_SHORT).show()
                }else{
                    qty = qty - 1
                    txtQtyProdukCart.text = qty.toString()
                    cartItemListener.onDecreaseClicked(cartItem)
                }
            }

            imgTambah.setOnClickListener {
                qty = qty + 1
                txtQtyProdukCart.text = qty.toString()
                cartItemListener.onIncreaseClicked(cartItem)
            }
        }
    }
}