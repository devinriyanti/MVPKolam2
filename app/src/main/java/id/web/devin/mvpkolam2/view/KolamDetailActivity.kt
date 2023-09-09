package id.web.devin.mvpkolam2.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import id.web.devin.mvpkolam2.R
import id.web.devin.mvpkolam2.databinding.ActivityKolamDetailBinding

class KolamDetailActivity : AppCompatActivity() {
    private lateinit var b:ActivityKolamDetailBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityKolamDetailBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.title = "Kolam Renang"
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostDetail) as NavHostFragment
        navController = navHostFragment.findNavController()
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,null)
    }
}