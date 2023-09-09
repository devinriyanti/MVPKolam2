package id.web.devin.mvpkolam2.view

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import id.web.devin.mvpkolam2.databinding.FragmentDaftarSebagaiBinding

class DaftarSebagaiFragment : Fragment() {
    private lateinit var b:FragmentDaftarSebagaiBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentDaftarSebagaiBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b.btnAkunPribadi.setOnClickListener {
            val action = DaftarSebagaiFragmentDirections.actionRegisPribadiFragment()
            Navigation.findNavController(it).navigate(action)
        }
        b.btnAkunBisnis.setOnClickListener {
            AlertDialog.Builder(context).apply {
                val title = SpannableString("PENTING!")
                title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                title.setSpan(ForegroundColorSpan(Color.RED), 0, title.length, 0)
                val message = SpannableString("Pastikan domisili bisnis Anda berada\ndi Surabaya sebelum mendaftar akun bisnis.")
                message.setSpan(
                    AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    0,
                    message.length,
                    0
                )
                setTitle(title)
                setMessage(message)
                setPositiveButton("LANJUT"){ _,_->
                    val action = DaftarSebagaiFragmentDirections.actionRegisBisnisFragment()
                    Navigation.findNavController(it).navigate(action)
                }
                setNegativeButton("BATAL"){ dialog,_->
                    dialog.dismiss()
                }
                create().show()
            }
        }
    }
}