package id.web.devin.mvckolam.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.web.devin.mvckolam.R
import id.web.devin.mvckolam.databinding.FragmentCheckoutBinding

class CheckoutFragment : Fragment() {
    private lateinit var b:FragmentCheckoutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentCheckoutBinding.inflate(layoutInflater)
        return b.root
    }
}