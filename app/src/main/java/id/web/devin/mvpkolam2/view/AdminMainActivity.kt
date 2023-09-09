package id.web.devin.mvpkolam2.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import id.web.devin.mvpkolam2.R
import id.web.devin.mvpkolam2.databinding.ActivityAdminMainBinding

class AdminMainActivity : AppCompatActivity() {
    private lateinit var b:ActivityAdminMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostAdmin) as NavHostFragment
        navController = navHostFragment.findNavController()
        b.bottomNavAdmin.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,null)
    }
}