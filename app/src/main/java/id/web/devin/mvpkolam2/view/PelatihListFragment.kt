package id.web.devin.mvpkolam2.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import id.web.devin.mvpkolam2.presenter.KolamDetailPresenter
import id.web.devin.mvpkolam2.databinding.FragmentPelatihListBinding
import id.web.devin.mvpkolam2.model.Kolam
import id.web.devin.mvpkolam2.model.Role
import id.web.devin.mvpkolam2.util.Global
import id.web.devin.mvpkolam2.util.KolamDetailPresenterListener

class PelatihListFragment : Fragment(), KolamDetailPresenterListener {
    private lateinit var b:FragmentPelatihListBinding
    private lateinit var cKolamDetail:KolamDetailPresenter
    private lateinit var pelatihListAdapter: PelatihListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cKolamDetail = KolamDetailPresenter(this.requireContext(), this)
        b= FragmentPelatihListBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b.txtErorPelatih.visibility = View.GONE
        val role = context?.let { Global.getRole(it) }
        val sharedPreferences = requireActivity().getSharedPreferences("kolam", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", null)
        Log.d("das",id.toString());
        cKolamDetail.fetchDetailKolam(id.toString())
        pelatihListAdapter = PelatihListAdapter(arrayListOf())

        b.recViewPelatih.layoutManager = LinearLayoutManager(context)
        b.recViewPelatih.adapter = pelatihListAdapter

        if(role == Role.Admin.name){
            b.fabTambahPelatih.visibility = View.VISIBLE

            b.fabTambahPelatih.setOnClickListener{
                val action = KolamDetailFragmentDirections.actionPelatihAddFragment()
                Navigation.findNavController(it).navigate(action)
            }
        }else{
            b.fabTambahPelatih.visibility = View.GONE
        }

        b.refreshLayoutPelatih.setOnRefreshListener {
            b.recViewPelatih.visibility = View.GONE
            b.txtErorPelatih.visibility = View.GONE
            b.progressLoadPelatih.visibility = View.VISIBLE
            cKolamDetail.fetchDetailKolam(id.toString())
            b.refreshLayoutPelatih.isRefreshing = false
        }
    }

    override fun showError(message: String) {
        Log.d("error",message)
        b.progressLoadPelatih.visibility = View.GONE
        Toast.makeText(context, message,Toast.LENGTH_SHORT).show()
    }

    override fun showKolam(kolam: List<Kolam>) {
        b.progressLoadPelatih.visibility = View.GONE
        kolam.forEach {
            if(!it.pelatih.isNullOrEmpty()){
                pelatihListAdapter.updatePelatihList(it.pelatih)
            }else{
                b.txtPelatihAvailable.setText("Tidak Ada Pelatih")
            }
        }
    }
}