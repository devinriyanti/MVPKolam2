package id.web.devin.mvvmkolam.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import id.web.devin.mvckolam.databinding.HomeListItemBinding
import id.web.devin.mvckolam.model.Kolam
import id.web.devin.mvckolam.util.loadImage
import id.web.devin.mvckolam.view.KolamDetailActivity

class KolamListAdapter(private val context: Context,val kolamList:ArrayList<Kolam>):RecyclerView.Adapter<KolamListAdapter.KolamViewHolder>() {
    class KolamViewHolder(val binding: HomeListItemBinding):RecyclerView.ViewHolder(binding.root)

    fun updateKolamList(newKolamList:List<Kolam>){
        kolamList.clear()
        kolamList.addAll(newKolamList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KolamViewHolder {
        val binding = HomeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KolamViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KolamViewHolder, position: Int) {
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val colorFilter = ColorMatrixColorFilter(colorMatrix)

        val kolam = kolamList[position]
        with(holder.binding){

            txtNamaKolam.text = kolam.nama
            txtAlamatKolam.text = kolam.alamat
            imageKolam.loadImage(kolam.gambarUrl.toString(), progressBar)

            if(kolam.is_maintenance.equals("0")){
                txtStatus.setText("Buka")
                txtStatus.setTextColor(Color.GREEN)
                cardListKolam.setOnClickListener {
                    var intent = Intent(holder.itemView.context, KolamDetailActivity::class.java)
                    holder.itemView.context.startActivity(intent)
                    val sharedPreferences = context.getSharedPreferences("kolam", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("id", kolam.id.toString())
                    editor.apply()
                }
            }else{
                txtStatus.setText("Tutup")
                txtStatus.setTextColor(Color.RED)
                imageKolam.colorFilter= colorFilter
            }
        }
    }

    override fun getItemCount(): Int {
        return kolamList.size
    }

}