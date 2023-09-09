package id.web.devin.mvpkolam2.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.web.devin.mvpkolam2.R
import id.web.devin.mvpkolam2.databinding.FragmentPembayaranWaitBinding

class PembayaranWaitFragment : Fragment() {
    private lateinit var b:FragmentPembayaranWaitBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentPembayaranWaitBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b.btnBerandaWait.setOnClickListener {
            val intent = Intent(context,MainActivity::class.java)
            startActivity(intent)
        }
    }
}