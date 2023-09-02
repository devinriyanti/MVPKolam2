package id.web.devin.mvckolam.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import id.web.devin.mvckolam.controller.KolamDetailController
import id.web.devin.mvckolam.databinding.FragmentPelatihListBinding
import id.web.devin.mvckolam.model.Kolam
import id.web.devin.mvckolam.util.KolamDetailControllerListener
import id.web.devin.mvvmkolam.view.PelatihListAdapter

class PelatihListFragment : Fragment(), KolamDetailControllerListener {
    private lateinit var b:FragmentPelatihListBinding
    private lateinit var cKolamDetail:KolamDetailController
    private lateinit var pelatihListAdapter: PelatihListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cKolamDetail = KolamDetailController(this.requireContext(), this)
        b= FragmentPelatihListBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        b.txtErorPelatih.visibility = View.GONE
        val sharedPreferences = requireActivity().getSharedPreferences("kolam", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", null)
        Log.d("das",id.toString());
        cKolamDetail.fetchDetailKolam(id.toString())
        pelatihListAdapter = PelatihListAdapter(arrayListOf())

        b.recViewPelatih.layoutManager = LinearLayoutManager(context)
        b.recViewPelatih.adapter = pelatihListAdapter

//        b.refreshLayoutPelatih.setOnRefreshListener {
//            b.recViewPelatih.visibility = View.GONE
//            b.txtErorPelatih.visibility = View.GONE
//            b.progressLoadPelatih.visibility = View.VISIBLE
//            viewModel.fetchData(id.toString())
//            b.refreshLayoutPelatih.isRefreshing = false
//        }
    }

    override fun showError(message: String) {
        Toast.makeText(context, message,Toast.LENGTH_SHORT).show()
    }

    override fun showKolam(kolam: List<Kolam>) {
        kolam.forEach {
            if(!it.pelatih.isNullOrEmpty()){
                pelatihListAdapter.updatePelatihList(it.pelatih)
            }else{
                b.txtPelatihAvailable.setText("Tidak Ada Pelatih")
            }
        }
    }
}