package com.mufiid.assetmanagement.ui.detail

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mufiid.assetmanagement.R
import com.mufiid.assetmanagement.helpers.CustomView
import com.mufiid.assetmanagement.models.Asset
import com.mufiid.assetmanagement.ui.addupdate.AddUpdateActivity
import com.mufiid.assetmanagement.utils.Constants
import kotlinx.android.synthetic.main.activity_detail.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private var dataAsset: Asset? = null
    private lateinit var viewModel: DetailViewModel
    private lateinit var loading: ProgressDialog

    companion object {
        var DATA_ASSET = "data_asset"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initSetSupportActionBar()
        init()
        setParcelable()

    }

    private fun init() {
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.getState().observer(this, Observer {
            handleUIState(it)
        })
        loading = ProgressDialog(this)
        btn_edit.setOnClickListener(this)
        btn_delete.setOnClickListener(this)
    }

    private fun handleUIState(it: AssetState?) {
        when (it) {
            is AssetState.IsLoading -> showLoading(it.state)
            is AssetState.IsSuccess -> isSuccess(it.message)
            is AssetState.Error -> showError(it.err)
        }
    }

    private fun showError(err: String?) {
        showToast(err)
    }

    private fun isSuccess(message: String?) {
        showToast(message, true)
        Handler(Looper.myLooper()!!).postDelayed({
            finish()
        }, 500)
    }

    private fun showToast(message: String?, isSuccess: Boolean? = false) {
        CustomView.customToast(this, message, true, null)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            loading.setMessage(getString(R.string.loading))
            loading.setCanceledOnTouchOutside(false)
            loading.show()
        } else {
            loading.dismiss()
        }
    }


    @SuppressLint("SimpleDateFormat")
    private fun setParcelable() {
//        val outputDateOil: String
//        val outputNextDateOil: String

        dataAsset = intent.getParcelableExtra(DATA_ASSET)
        dataAsset?.let {
            tv_plat_mobil.text = it.platMobil
            tv_merk_mobil.text = it.modelMobil
            tv_no_rangka.text = it.noRangka
            tv_no_mesin.text = it.noMesin
            tv_owner_name.text = it.ownerName

//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
//                val date = LocalDate.parse(it.dateOil)
//                outputDateOil = formatter.format(date)
//
//                val date2 = LocalDate.parse(it.dateExpiredOil)
//                outputNextDateOil = formatter.format(date2)
//
//            } else {
//                val formatter = SimpleDateFormat("dd-MM-yyyy")
//
//                val parser = SimpleDateFormat("yyyy-MM-dd")
//                outputDateOil = formatter.format(parser.parse(it.dateOil))
//
//                val parser2 = SimpleDateFormat("yyyy-MM-dd")
//                outputNextDateOil = formatter.format(parser2.parse(it.dateOil))
//            }

            tv_ganti_oli_terakhir.text = it.dateOil
            tv_ganti_oli_berikutnya.text = it.dateExpiredOil
        }
    }

    private fun initSetSupportActionBar() {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = getString(R.string.detail)
        }
    }

    override fun onNavigateUp(): Boolean {
        finish()
        return super.onNavigateUp()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_edit -> {
                startActivity(Intent(this, AddUpdateActivity::class.java).apply {
                    putExtra(AddUpdateActivity.DATA, dataAsset)
                    putExtra(AddUpdateActivity.IS_UPDATE, true)
                })
            }
            R.id.btn_delete -> {
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.confirmation))
                    setMessage(getString(R.string.question_confirmation))
                    setPositiveButton(getString(R.string.yes)) { _, _ ->
                        viewModel.deleteAsset(
                            Constants.getUserData(this@DetailActivity)?.token,
                            dataAsset?.id
                        )
                    }
                    setNegativeButton(getString(R.string.no)) { dialog, _ ->
                        dialog.dismiss()
                    }
                }.show()

            }
        }
    }
}