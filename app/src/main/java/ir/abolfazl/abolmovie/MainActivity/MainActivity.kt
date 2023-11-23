package ir.abolfazl.abolmovie.MainActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.pedant.SweetAlert.SweetAlertDialog
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.selectedItemId = R.id.btn_Home_menu

        binding.bottomNavigation.setOnItemReselectedListener {}

    }

}