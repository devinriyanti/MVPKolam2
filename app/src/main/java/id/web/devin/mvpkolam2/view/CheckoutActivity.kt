package id.web.devin.mvpkolam2.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import id.web.devin.mvpkolam2.R
import id.web.devin.mvpkolam2.databinding.ActivityCheckoutBinding
import id.web.devin.mvpkolam2.databinding.ActivityPembelianDetailBinding

class CheckoutActivity : AppCompatActivity() {
    private lateinit var b:ActivityCheckoutBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.title = "Checkout"
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostCheckout) as NavHostFragment
        navController = navHostFragment.findNavController()
        NavigationUI.setupActionBarWithNavController(this, navController)
    }
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,null)
    }
}