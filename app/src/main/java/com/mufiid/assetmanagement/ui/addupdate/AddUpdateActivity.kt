package com.mufiid.assetmanagement.ui.addupdate

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mufiid.assetmanagement.R
import com.mufiid.assetmanagement.models.Asset
import kotlinx.android.synthetic.main.activity_add_update.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddUpdateActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    View.OnClickListener {

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

    private fun init() {
        et_date_oil.setOnFocusChangeListener { _, b ->
            if (b) {
                showCalendar()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setParcelable() {
        val data = intent.getParcelableExtra<Asset>(DATA)
        val isUpdate = intent.getBooleanExtra(IS_UPDATE, false)
        val outputDateOil: String
        val outputNextDateOil: String

        if (isUpdate) {
            data?.let {
                et_plat_mobil.setText(it.platMobil)
                et_merk_mobil.setText(it.merkMobil)
                et_no_mesin.setText(it.noMesin)
                et_no_rangka.setText(it.noRangka)
                et_owner_name.setText(it.ownerName)

//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
//                    val date = LocalDate.parse(it.dateOil)
//                    outputDateOil = formatter.format(date)
//
//                    val date2 = LocalDate.parse(it.dateExpiredOil)
//                    outputNextDateOil = formatter.format(date2)
//
//                } else {
//                    val formatter = SimpleDateFormat("dd-MM-yyyy")
//
//                    val parser = SimpleDateFormat("yyyy-MM-dd")
//                    outputDateOil = formatter.format(parser.parse(it.dateOil))
//
//                    val parser2 = SimpleDateFormat("yyyy-MM-dd")
//                    outputNextDateOil = formatter.format(parser2.parse(it.dateOil))
//                }

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

    override fun onNavigateUp(): Boolean {
        Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show()
        return super.onNavigateUp()
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