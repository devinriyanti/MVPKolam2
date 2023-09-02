package id.web.devin.mvckolam.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.web.devin.mvckolam.R
import id.web.devin.mvckolam.databinding.FragmentDaftarSebagaiBinding

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
}