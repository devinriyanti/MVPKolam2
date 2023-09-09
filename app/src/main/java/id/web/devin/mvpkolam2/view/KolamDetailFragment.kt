package id.web.devin.mvpkolam2.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayoutMediator
import id.web.devin.mvpkolam2.presenter.KolamDetailPresenter
import id.web.devin.mvpkolam2.databinding.FragmentKolamDetailBinding
import id.web.devin.mvpkolam2.model.Kolam
import id.web.devin.mvpkolam2.util.KolamDetailPresenterListener
import id.web.devin.mvpkolam2.util.loadImage

class KolamDetailFragment : Fragment(), KolamDetailPresenterListener {
    private lateinit var b:FragmentKolamDetailBinding
    private lateinit var navController: NavController
    private var kolamID:String? = null
    private lateinit var cKolamDetail: KolamDetailPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cKolamDetail = KolamDetailPresenter(this.requireContext(),this)
        b = FragmentKolamDetailBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = b.viewPagerTab
        val tabLayout = b.tabLayout
        val tabTitles = listOf("Produk", "Pelatih")

        //Setup ViewPager
        val adapter = MyPagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]

        }.attach()

        val sharedPreferences = requireActivity().getSharedPreferences("kolam", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", null)
        kolamID = id
        cKolamDetail.fetchDetailKolam(kolamID!!)

        navController = Navigation.findNavController(requireParentFragment().requireView())
    }

    override fun showError(message: String) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

    override fun showKolam(kolam: List<Kolam>) {
        kolam.forEach {
            val id = it.id.toString()
            b.txtNamaKolamDetail.setText(it.nama)
            b.imageKolamDetail.loadImage(it.gambarUrl.toString(),b.progressBarDetailKolam)
            b.txtNamaKolamDetail.setOnClickListener {
                val action = KolamDetailFragmentDirections.actionRincianKolamFragment(id)
                Navigation.findNavController(it).navigate(action)
            }
        }
    }
}