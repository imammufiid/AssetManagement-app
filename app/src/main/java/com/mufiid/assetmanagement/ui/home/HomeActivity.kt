package com.mufiid.assetmanagement.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.mufiid.assetmanagement.R

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
                Toast.makeText(this, "Add Data", Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }
    }
}