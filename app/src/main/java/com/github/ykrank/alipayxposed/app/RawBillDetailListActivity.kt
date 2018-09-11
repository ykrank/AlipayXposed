package com.github.ykrank.alipayxposed.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.ykrank.alipayxposed.R
import com.github.ykrank.androidtools.ui.LibBaseActivity

class RawBillDetailListActivity : LibBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_raw_bill_detail_list)
    }

    companion object {
        fun start(context: Context){
            context.startActivity(Intent(context, RawBillDetailListActivity::class.java))
        }
    }
}