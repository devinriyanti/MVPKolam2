package id.web.devin.mvpkolam2.view

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.web.devin.mvpkolam2.databinding.CartListItemBinding
import id.web.devin.mvpkolam2.model.Cart
import id.web.devin.mvpkolam2.view.CheckoutActivity

class CartListAdapter(private val context: Context, val cartList: ArrayList<Cart>, private val listener: CartItemAdapter.CartItemListener) : RecyclerView.Adapter<CartListAdapter.CartViewHolder>() {
    class CartViewHolder(val binding: CartListItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun updateCartList(newCartList: List<Cart>) {
        cartList.clear()
        cartList.addAll(newCartList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = cartList[position]
        with(holder.binding){
            val kolam = cart.nama
            txtNamaKolamCart.text = kolam
            btnCheckout.setOnClickListener {
                var intent = Intent(holder.itemView.context, CheckoutActivity::class.java)
                holder.itemView.context.startActivity(intent)
                val sharedPreferences = context.getSharedPreferences("idkolam", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("id", cart.id)
                editor.apply()
            }
            cart.produk.forEach {
                if (cart.id == it.idkolam){
                    val cartItemAdapter = CartItemAdapter(arrayListOf(), listener)
                    cartItemRecView.layoutManager = LinearLayoutManager(root.context)
                    cartItemRecView.adapter = cartItemAdapter

                    cartItemAdapter.updateCartItem(cart.produk)
                    Log.d("produkList",cart.produk.toString())
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return cartList.size
    }
}