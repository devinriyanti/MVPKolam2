package id.web.devin.mvvmkolam.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.web.devin.mvckolam.databinding.PembelianItemBinding
import id.web.devin.mvckolam.model.ProductTransaction
import id.web.devin.mvckolam.util.formatCurrency
import id.web.devin.mvckolam.util.loadImage

class PembelianItemAdapter(val transactionItem: ArrayList<ProductTransaction>):RecyclerView.Adapter<PembelianItemAdapter.PembelianItemViewHolder>() {
    class PembelianItemViewHolder (val b: PembelianItemBinding):RecyclerView.ViewHolder(b.root)

    fun updateTransactionItem(newTransactionItem:ArrayList<ProductTransaction>){
        transactionItem.clear()
        transactionItem.addAll(newTransactionItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PembelianItemViewHolder {
        val b = PembelianItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PembelianItemViewHolder(b)
    }

    override fun getItemCount(): Int {
        return transactionItem.size
    }

    override fun onBindViewHolder(holder: PembelianItemViewHolder, position: Int) {
        val item = transactionItem[position]
        with(holder.b){
            txtNamaProdukPembelian.text = item.nama
            var harga = 0.0
            if(item.qty > 1){
                harga = item.harga/item.qty
            }else{
                harga = item.harga
            }
            txtHargaPembelian.text = formatCurrency(harga)
            txtQtyPembelian.text = "x${item.qty}"
            imgProdukPembelian.loadImage(item.gambar, progressLoadProdukPembelian)
        }
    }
}