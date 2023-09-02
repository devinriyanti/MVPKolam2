package id.web.devin.mvckolam.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import id.web.devin.mvckolam.R
import id.web.devin.mvckolam.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var b: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate((layoutInflater))
        setContentView(b.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostLogin) as NavHostFragment
        navController = navHostFragment.findNavController()
        NavigationUI.setupActionBarWithNavController(this, navController)
    }
}