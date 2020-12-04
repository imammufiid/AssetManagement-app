package com.mufiid.assetmanagement.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mufiid.assetmanagement.R
import com.mufiid.assetmanagement.adapter.AssetAdapter
import com.mufiid.assetmanagement.models.Asset
import com.mufiid.assetmanagement.ui.addupdate.AddUpdateActivity
import com.mufiid.assetmanagement.ui.detail.DetailActivity
import com.mufiid.assetmanagement.utils.Constants
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: AssetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initSupportActionBar()
        init()
    }

    override fun onResume() {
        super.onResume()
        setRecyclerView()
        showData()
    }

    private fun init() {
        homeViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(HomeViewModel::class.java)

        homeViewModel.getState().observer(this, Observer {
            handleUIState(it)
        })
    }

    private fun handleUIState(it: AssetState?) {
        when(it) {
            is AssetState.IsLoading -> showLoading(it.state)
        }
    }

    private fun showLoading(state: Boolean) {
        if(state) {
            shimmer_container_asset.visibility = View.VISIBLE
            shimmer_container_asset.startShimmer()
        } else {
            shimmer_container_asset.stopShimmer()
            shimmer_container_asset.visibility = View.GONE
        }
    }

    private fun setRecyclerView() {
        adapter = AssetAdapter { asset ->
            showSelectedData(asset)
        }.apply {
            notifyDataSetChanged()
        }

        rv_data.layoutManager = LinearLayoutManager(this)
        rv_data.adapter = adapter
    }

    private fun showData() {
        Constants.getUserData(this)?.let {
            homeViewModel.getAllData(it.id, it.token)
        }
        homeViewModel.getAssets().observe(this, Observer {
            if(it != null) {
                adapter.setAsset(it)
            }
        })
    }

    private fun showSelectedData(asset: Asset) {
        startActivity(Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.DATA_ASSET, asset)
        })
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
        return when (item.itemId) {
            R.id.add -> {
                startActivity(Intent(this, AddUpdateActivity::class.java))
                true
            }
            else -> false
        }
    }
}