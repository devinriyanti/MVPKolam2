package id.web.devin.mvvmkolam.view

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.web.devin.mvckolam.databinding.PembelianListItemBinding
import id.web.devin.mvckolam.model.Transaction

class PembelianListAdapter(val transactionList:ArrayList<Transaction>):RecyclerView.Adapter<PembelianListAdapter.TransactionViewHolder>() {
    class TransactionViewHolder(val binding: PembelianListItemBinding):RecyclerView.ViewHolder(binding.root)

    fun updateTransactionList(newTransactionList: List<Transaction>) {
        transactionList.clear()
        transactionList.addAll(newTransactionList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = PembelianListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaksi = transactionList[position]
        with(holder.binding){
            txtNamaKolamPembelian.text = transaksi.namaKolam
            val pembelianItemAdapter = PembelianItemAdapter(arrayListOf())
            recViewPembelianItem.layoutManager = LinearLayoutManager(root.context)
            recViewPembelianItem.adapter = pembelianItemAdapter
            pembelianItemAdapter.updateTransactionItem(transaksi.produk)
            cardViewPembelian.setOnClickListener {
                val sharedPreferences = holder.itemView.context.getSharedPreferences("idtransaksi", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("status", transaksi.status.toString())
                editor.putString("idtrx", transaksi.id)
                editor.apply()
                Log.d("isdh", transaksi.id)
//                val intent = Intent(holder.itemView.context, PembelianDetailActivity::class.java)
//                holder.itemView.context.startActivity(intent)
            }
        }
    }
}