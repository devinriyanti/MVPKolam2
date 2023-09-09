package id.web.devin.mvpkolam2.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import id.web.devin.mvpkolam2.presenter.KolamPresenter
import id.web.devin.mvpkolam2.databinding.FragmentHomeListBinding
import id.web.devin.mvpkolam2.model.Kolam
import id.web.devin.mvpkolam2.model.Role
import id.web.devin.mvpkolam2.util.Global
import id.web.devin.mvpkolam2.util.KolamPresenterListener

class HomeListFragment : Fragment(), KolamPresenterListener {
    private lateinit var b:FragmentHomeListBinding
    private lateinit var kolamListAdapter: KolamListAdapter
    private lateinit var cKolam:KolamPresenter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentHomeListBinding.inflate(layoutInflater)
        cKolam = KolamPresenter(this.requireContext(),this)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val role = Global.getRole(requireContext())
        val email = Global.getEmail(requireContext())
        b.txtError.visibility = View.GONE
        var cari =""
        if(role == Role.Admin.name){
            cKolam.fetchKolamAdmin(email.toString(),role)
            b.txtPencarianKolamHome.visibility = View.GONE
            b.fabTambahKolam.visibility = View.VISIBLE
        }else{
            b.txtPencarianKolam.addTextChangedListener {
                cari = b.txtPencarianKolam.text.toString()
                if(cari != ""){
                    cari = cari
                }else{
                    cari = ""
                }
                cKolam.fetchKolam(cari)
            }
            cKolam.fetchKolam(cari)
            b.fabTambahKolam.visibility = View.GONE
        }
        b.refreshHome.setOnRefreshListener {
            b.recViewKolam.visibility = View.GONE
            b.txtError.visibility = View.GONE
            b.txtKolamTersedia.visibility = View.GONE
            b.progressLoad.visibility = View.VISIBLE
            if (role == Role.Admin.name){
                cKolam.fetchKolamAdmin(email!!,role.toString())
                b.txtPencarianKolamHome.visibility = View.GONE
            }else{
                b.txtPencarianKolam.addTextChangedListener {
                    cari = b.txtPencarianKolam.text.toString()
                    if(cari != ""){
                        cari = cari
                    }else{
                        cari = ""
                    }
                    cKolam.fetchKolam(cari)
                }
                cKolam.fetchKolam(cari)
            }
            b.refreshHome.isRefreshing = false
        }
        kolamListAdapter = KolamListAdapter(requireContext(), arrayListOf())
        b.recViewKolam.layoutManager = LinearLayoutManager(context)
        b.recViewKolam.adapter = kolamListAdapter
    }

    override fun showError(message: String) {
        if(message == "Tidak Ada Kolam"){
            b.txtKolamTersedia.text = message
        }else{
            b.txtError.visibility = View.VISIBLE
        }
        Log.d("eror",message)
        b.progressLoad.visibility = View.GONE
    }

    override fun showKolam(kolam: List<Kolam>) {
        b.recViewKolam.visibility = View.VISIBLE
        b.progressLoad.visibility = View.GONE
        if(!kolam.isNullOrEmpty()){
            kolamListAdapter.updateKolamList(kolam)
            b.txtKolamTersedia.text = ""
            if(kolam.size >= 1){
                b.fabTambahKolam.isEnabled = false
            }else{
                b.fabTambahKolam.setOnClickListener {
                    val action = HomeListFragmentDirections.actionKolamAddFragment()
                    Navigation.findNavController(it).navigate(action)
                }
            }
        }else{
            kolamListAdapter.updateKolamList(emptyList())
            b.txtKolamTersedia.text = "Tidak Ada Kolam"
        }
    }

    override fun success() {}
}