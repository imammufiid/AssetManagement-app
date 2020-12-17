package com.mufiid.assetmanagement.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mufiid.assetmanagement.R
import com.mufiid.assetmanagement.adapter.AssetAdapter
import com.mufiid.assetmanagement.helpers.CustomView
import com.mufiid.assetmanagement.models.Asset
import com.mufiid.assetmanagement.ui.addupdate.AddUpdateActivity
import com.mufiid.assetmanagement.ui.detail.DetailActivity
import com.mufiid.assetmanagement.ui.login.LoginActivity
import com.mufiid.assetmanagement.utils.Constants
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: AssetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initSupportActionBar()
        init()
        searchAsset()
    }

    private fun searchAsset() {
        search_view.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                homeViewModel.searchData(
                    Constants.getUserData(this@HomeActivity)?.token,
                    query,
                    Constants.getUserData(this@HomeActivity)?.id
                )
                search_view.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    override fun onResume() {
        super.onResume()
        search_view.setQuery("", false)
        search_view.isIconified = true
        showData()
        setRecyclerView()
    }

    private fun init() {
        homeViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(HomeViewModel::class.java)

        homeViewModel.getState().observer(this, Observer {
            handleUIState(it)
        })

        btn_add.setOnClickListener(this)

        swipe_refresh?.setOnRefreshListener {
            swipe_refresh.isRefreshing = true
            showDataSwipeRefresh()
            swipe_refresh.isRefreshing = false
        }
    }

    private fun handleUIState(it: AssetState?) {
        when(it) {
            is AssetState.IsLoading -> showLoading(it.state)
        }
    }

    private fun showLoading(state: Boolean) {
        if(state) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
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
            } else {
                CustomView.customToast(this, "Data Not Available")
            }
        })
    }

    private fun showDataSwipeRefresh() {
        Constants.getUserData(this)?.let {
            homeViewModel.getAllDataSwipeRefresh(it.id, it.token)
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
                AlertDialog.Builder(this).apply {
                    setMessage("Apa anda yakin Keluar?")
                    setPositiveButton("Iya") { _, _ ->
                        Constants.clear(this@HomeActivity)
                        startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                        finish()
                    }
                    setNegativeButton("Tidak") { dialog, _ ->
                        dialog.dismiss()
                    }
                }.show()
                true

            }
            else -> false
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_add -> {
                startActivity(Intent(this, AddUpdateActivity::class.java))
                true
            }
        }
    }
}