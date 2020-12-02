package com.mufiid.assetmanagement.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.mufiid.assetmanagement.R
import com.mufiid.assetmanagement.ui.addupdate.AddUpdateActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initSupportActionBar()
    }

    private fun init() {
        TODO("Not yet implemented")
    }

    private fun initSupportActionBar() {
        supportActionBar?.let {
            it.elevation = 0F
            it.title = getString(R.string.app_name)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.add -> {
                startActivity(Intent(this, AddUpdateActivity::class.java))
                true
            }
            else -> false
        }
    }
}