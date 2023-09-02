package id.web.devin.mvckolam.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import id.web.devin.mvckolam.controller.KolamController
import id.web.devin.mvckolam.databinding.FragmentHomeListBinding
import id.web.devin.mvckolam.model.Kolam
import id.web.devin.mvckolam.model.Role
import id.web.devin.mvckolam.util.Global
import id.web.devin.mvckolam.util.KolamControllerListener
import id.web.devin.mvvmkolam.view.KolamListAdapter

class HomeListFragment : Fragment(), KolamControllerListener {
    private lateinit var b:FragmentHomeListBinding
    private lateinit var kolamListAdapter: KolamListAdapter
    private lateinit var cKolam:KolamController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentHomeListBinding.inflate(layoutInflater)
        cKolam = KolamController(this.requireContext(),this)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val role = Global.getRole(requireContext())
        b.txtError.visibility = View.GONE
        if(role == Role.Admin.name){

        }else{
            cKolam.fetchKolam()
        }
        b.refreshHome.setOnRefreshListener {
            b.recViewKolam.visibility = View.GONE
            b.txtError.visibility = View.GONE
            b.txtKolamTersedia.visibility = View.GONE
            b.progressLoad.visibility = View.VISIBLE
            if (role == Role.Admin.name){
//                cKolam.fetchKolamAdmin(email,it.role.toString())
            }else{
                cKolam.fetchKolam()
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
        }else{
            b.txtKolamTersedia.text = "Tidak Ada Kolam"
        }
    }
}