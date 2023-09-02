package id.web.devin.mvckolam.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import id.web.devin.mvckolam.R
import id.web.devin.mvckolam.databinding.FragmentPenjualanListBinding

class PenjualanListFragment : Fragment() {
    private lateinit var b:FragmentPenjualanListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentPenjualanListBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = b.viewPagerPenjualan
        val tabLayout = b.tabLayoutPenjualan
        val tabTitles = listOf("Diproses", "Dikirim", "Diterima", "Dibatalkan")

        val adapter = MyStatusAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}