package id.web.devin.mvckolam.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import id.web.devin.mvckolam.R
import id.web.devin.mvckolam.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var b:ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostMain) as NavHostFragment
        navController = navHostFragment.findNavController()
        b.bottomNavMain.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,null)
    }
}