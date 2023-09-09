package id.web.devin.mvpkolam2.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import id.web.devin.mvpkolam2.presenter.TransaksiPresenter
import id.web.devin.mvpkolam2.databinding.FragmentDikirimBinding
import id.web.devin.mvpkolam2.model.Role
import id.web.devin.mvpkolam2.model.StatusTransaksi
import id.web.devin.mvpkolam2.model.Transaction
import id.web.devin.mvpkolam2.util.Global
import id.web.devin.mvpkolam2.util.TransaksiPresenterListener

class DikirimFragment : Fragment(), TransaksiPresenterListener {
    private lateinit var b:FragmentDikirimBinding
    private lateinit var pembelianListAdapter: PembelianListAdapter
    private lateinit var cTransaksi: TransaksiPresenter
    lateinit var email:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cTransaksi = TransaksiPresenter(this.requireContext(),this)
        b = FragmentDikirimBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b.txtErrorDikirim.visibility = View.GONE
        email = context?.let { Global.getEmail(it) }.toString()
        pembelianListAdapter = PembelianListAdapter(arrayListOf())
        val role = Global.getRole(requireContext())
        if(role == Role.Admin.toString()){
            cTransaksi.fetchTransaksiAdmin(email, StatusTransaksi.Dikirim.name)
        }else{
            cTransaksi.fetchTransaksi(email, StatusTransaksi.Dikirim.name)
        }
    }

    override fun showError(message: String) {
        if(message == "Tidak Ada Transaksi"){
            b.txtStatusDikirim.text = message
        }else{
            b.txtErrorDikirim.visibility = View.VISIBLE
        }
        Log.d("eror",message)
        b.progressBarDikirim.visibility = View.GONE
    }

    override fun showTransaksi(transaksi: List<Transaction>) {
        transaksi.forEach {
            if(it.id != "null"){
                pembelianListAdapter.updateTransactionList(transaksi)
                b.recViewDikirim.layoutManager = LinearLayoutManager(context)
                b.recViewDikirim.adapter = pembelianListAdapter
                b.progressBarDikirim.visibility = View.GONE
            }else{
                b.txtStatusDikirim.text = "Tidak Ada Transaksi"
                b.progressBarDikirim.visibility = View.GONE
            }
        }
    }

    override fun success() {}
}