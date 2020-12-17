package com.mufiid.assetmanagement.ui.addupdate

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mufiid.assetmanagement.R
import com.mufiid.assetmanagement.helpers.CustomView
import com.mufiid.assetmanagement.models.Asset
import com.mufiid.assetmanagement.utils.Constants
import kotlinx.android.synthetic.main.activity_add_update.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddUpdateActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    View.OnClickListener {

    private lateinit var viewModel: AddUpdateViewModel
    private lateinit var loading: ProgressDialog
    private var isUpdate: Boolean? = null
    private var data: Asset? = null
    private var listOfMerkName: MutableList<String>? = ArrayList()
    private var listOfMerkId: MutableList<String>? = ArrayList()
    private var merkId: String? = null
    private var listOfModelName: MutableList<String>? = ArrayList()
    private var listOfModelId: MutableList<String>? = ArrayList()
    private var modelId: String? = null
    private var adapterModel: ArrayAdapter<Any>? = null
    private var adapterMerk: ArrayAdapter<Any>? = null

    companion object {
        const val DATA = "data_asset"
        const val IS_UPDATE = "is_update"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update)

        initSupportActionBar()
        setParcelable()
        init()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setMerkMobil(Constants.getUserData(this)?.token)
    }

    private fun init() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(AddUpdateViewModel::class.java)
        viewModel.getState().observer(this, androidx.lifecycle.Observer {
            handleUIState(it)
        })

        loading = ProgressDialog(this)
        btn_save.setOnClickListener(this)

        et_date_oil.setOnFocusChangeListener { _, b ->
            if (b) {
                showCalendar()
            }
        }

        viewModel.getMerkMobil().observe(this, androidx.lifecycle.Observer {
            if (it != null) {

                for (i in it.indices) {
                    listOfMerkName?.add(it[i].nama.toString())
                    listOfMerkId?.add(it[i].id.toString())
                }
                adapterMerk =
                    ArrayAdapter(
                        this,
                        R.layout.support_simple_spinner_dropdown_item,
                        listOfMerkName as List<*>
                    )
                et_merk_mobil.setAdapter(adapterMerk)
            }
        })

        et_merk_mobil.setOnItemClickListener { _, _, i, l ->
            merkId = listOfMerkId?.get(i)
            viewModel.setModelMobil(Constants.getUserData(this)?.token, merkId)
            et_merk_mobil.dismissDropDown()
        }

        viewModel.getModelMobil().observe(this, androidx.lifecycle.Observer { it ->
            if (it != null) {
                listOfModelName?.clear()
                listOfModelId?.clear()
                for (i in it.indices) {
                    listOfModelName?.add(it[i].nama.toString())
                    listOfModelId?.add(it[i].id.toString())
                }
                adapterModel =
                    ArrayAdapter(
                        this,
                        R.layout.support_simple_spinner_dropdown_item,
                        listOfModelName as List<*>
                    )
                et_model_mobil.setAdapter(adapterModel)

            }
        })

        et_model_mobil.setOnItemClickListener { _, _, i, _ ->
            modelId = listOfModelId?.get(i)
            et_model_mobil.dismissDropDown()
        }

        isUpdate?.let { update ->
            if (update) {
                et_merk_mobil.setText(data?.merkMobil)
                et_model_mobil.setText(data?.modelMobil)
            }
        }

    }

    private fun handleUIState(it: AssetState?) {
        when (it) {
            is AssetState.Reset -> {
                setPlatMobilError(null)
                setMerkMobilError(null)
                setNoRangkaError(null)
                setNoMesinError(null)
                setDateError(null)
                setOwnerNameError(null)
            }
            is AssetState.IsLoading -> showLoading(it.state)
            is AssetState.ShowToast -> showToast(it.message)
            is AssetState.IsSuccess -> isSuccess(it.message)
            is AssetState.Error -> showError(it.err)
            is AssetState.AssetValidation -> {
                it.plat_mobil?.let {
                    setPlatMobilError(it)
                }
                it.merk_mobil?.let {
                    setMerkMobilError(it)
                }
                it.no_rangka?.let {
                    setNoRangkaError(it)
                }
                it.no_mesin?.let {
                    setNoMesinError(it)
                }
                it.owner_name?.let {
                    setOwnerNameError(it)
                }
                it.date_oil?.let {
                    setDateError(it)
                }
            }
        }
    }

    private fun setOwnerNameError(it: String?) {
        et_owner_name.error = it
    }

    private fun setDateError(it: String?) {
        et_date_oil.error = it
    }

    private fun setNoMesinError(it: String?) {
        et_no_mesin.error = it
    }

    private fun setNoRangkaError(it: String?) {
        et_no_rangka.error = it
    }

    private fun setMerkMobilError(it: String?) {
        et_merk_mobil.error = it
    }

    private fun setPlatMobilError(it: String?) {
        et_plat_mobil.error = it
    }

    private fun showError(err: String?) {
        showToast(err)
    }

    private fun isSuccess(message: String?) {
        showToast(message, true)
    }

    private fun showToast(message: String?, isSuccess: Boolean? = false) {
        CustomView.customToast(this, message, true, isSuccess)
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
        data = intent.getParcelableExtra<Asset>(DATA)
        isUpdate = intent.getBooleanExtra(IS_UPDATE, false)
        val outputDateOil: String
        val outputNextDateOil: String

        if (isUpdate as Boolean) {
            data?.let {
                et_plat_mobil.setText(it.platMobil)
                et_no_mesin.setText(it.noMesin)
                et_no_rangka.setText(it.noRangka)
                et_owner_name.setText(it.ownerName)

                et_date_oil.apply {
                    isEnabled = false
                    setText(it.dateOil)
                }
                et_next_date_oil.setText(it.dateExpiredOil)
            }
        } else {

            val date = Calendar.getInstance()
            val currentDate = date.time

            date.add(Calendar.MONTH, +4)
            val next4Date = date.time

            val format = SimpleDateFormat("dd-MM-yyyy")
            outputDateOil = format.format(currentDate)
            outputNextDateOil = format.format(next4Date)

            et_date_oil.apply {
                setText(outputDateOil)
            }

            et_next_date_oil.apply {
                setText(outputNextDateOil)
            }
        }
    }

    private fun initSupportActionBar() {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onDateSet(v: DatePicker?, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val date = calendar.time

        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val dateStr = dateFormat.format(date)
        et_date_oil.setText(dateStr)

        calendar.add(Calendar.MONTH, +4)
        val nextDate = calendar.time
        val nextDateStr = dateFormat.format(nextDate)
        et_next_date_oil.setText(nextDateStr)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_save -> {
                val platMobil = et_plat_mobil.text.toString()
                val noRangka = et_no_rangka.text.toString()
                val noMesin = et_no_mesin.text.toString()
                val ownerName = et_owner_name.text.toString()
                val dateOil = et_date_oil.text.toString()

                if (viewModel.validate(
                        platMobil,
                        modelId,
                        noRangka,
                        noMesin,
                        ownerName,
                        dateOil
                    )
                ) {
                    if (isUpdate != null) {
                        if (isUpdate as Boolean) {
                            viewModel.updateAsset(
                                Constants.getUserData(this)?.token,
                                data?.id,
                                Constants.getUserData(this)?.id,
                                platMobil, modelId, noRangka, noMesin, ownerName, dateOil
                            )
                        } else {
                            viewModel.insertAsset(
                                Constants.getUserData(this)?.token,
                                Constants.getUserData(this)?.id,
                                platMobil, modelId, noRangka, noMesin, ownerName, dateOil
                            )
                        }
                    }

                }

            }
        }
    }

    private fun showCalendar() {
        val calender = Calendar.getInstance()
        val datePick = DatePickerDialog(
            this, this,
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        )

        datePick.show()
    }
}