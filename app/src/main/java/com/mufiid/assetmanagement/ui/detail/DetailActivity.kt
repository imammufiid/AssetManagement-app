package com.mufiid.assetmanagement.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mufiid.assetmanagement.R
import com.mufiid.assetmanagement.models.Asset
import com.mufiid.assetmanagement.ui.addupdate.AddUpdateActivity
import kotlinx.android.synthetic.main.activity_detail.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private var dataAsset: Asset? = null
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
        btn_edit.setOnClickListener(this)
        btn_delete.setOnClickListener(this)
    }

    @SuppressLint("SimpleDateFormat")
    private fun setParcelable() {
        val outputDateOil: String
        val outputNextDateOil: String

        dataAsset = intent.getParcelableExtra<Asset>(DATA_ASSET)
        dataAsset?.let {
            tv_plat_mobil.text = it.platMobil
            tv_merk_mobil.text = it.merkMobil
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
        when(v?.id) {
            R.id.btn_edit -> {
                startActivity(Intent(this, AddUpdateActivity::class.java).apply {
                    putExtra(AddUpdateActivity.DATA, dataAsset)
                    putExtra(AddUpdateActivity.IS_UPDATE, true)
                })
            }
            R.id.btn_delete -> {}
        }
    }
}