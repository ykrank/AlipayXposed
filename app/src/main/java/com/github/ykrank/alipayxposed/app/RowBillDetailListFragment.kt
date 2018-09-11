package com.github.ykrank.alipayxposed.app

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.ykrank.alipayxposed.App
import com.github.ykrank.alipayxposed.R
import com.github.ykrank.alipayxposed.app.data.db.dbmodel.BillDetailsRaw
import com.github.ykrank.alipayxposed.databinding.ItemRawBillDetailBinding
import com.github.ykrank.androidtools.databinding.FragmentRecycleviewBinding
import com.github.ykrank.androidtools.extension.toast
import com.github.ykrank.androidtools.ui.LibBaseLoadMoreRecycleViewFragment
import com.github.ykrank.androidtools.ui.adapter.LibBaseRecyclerViewAdapter
import com.github.ykrank.androidtools.ui.adapter.simple.SimpleRecycleViewAdapter
import com.github.ykrank.androidtools.ui.internal.LoadingViewModelBindingDelegate
import com.github.ykrank.androidtools.ui.internal.LoadingViewModelBindingDelegateBaseImpl
import io.reactivex.Single

class RowBillDetailListFragment : LibBaseLoadMoreRecycleViewFragment<List<BillDetailsRaw>>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = recyclerViewAdapter
    }

    override val recyclerViewAdapter: LibBaseRecyclerViewAdapter by lazy {
        SimpleRecycleViewAdapter(context!!, R.layout.item_raw_bill_detail, createViewHolderCallback = { binding ->
            val bind = binding as ItemRawBillDetailBinding
            bind.root.setOnClickListener {
                context?.toast(bind.model?.rawJson.toString())
            }
        })
    }

    override fun appendNewData(oldData: List<BillDetailsRaw>?, newData: List<BillDetailsRaw>): List<BillDetailsRaw> {
        if (oldData != null) {
            val result = mutableListOf<BillDetailsRaw>()
            result.addAll(oldData)
            result.addAll(newData)
            return result
        }
        return newData
    }

    override fun getLibPageSourceObservable(pageNum: Int): Single<List<BillDetailsRaw>> {
        return Single.fromCallable {
            val page = (App.app.billDb.getCount() - 1) / PAGE_LIMIT + 1
            setTotalPages(page.toInt())
            return@fromCallable App.app.billDb.getList(PAGE_LIMIT, PAGE_LIMIT * (pageNum - 1))
        }
    }

    override fun getLoadingViewModelBindingDelegateImpl(inflater: LayoutInflater, container: ViewGroup?): LoadingViewModelBindingDelegate {
        val binding = DataBindingUtil.inflate<FragmentRecycleviewBinding>(inflater, R.layout.fragment_recycleview,
                container, false)
        return LoadingViewModelBindingDelegateBaseImpl(binding)
    }

    override fun onNext(data: List<BillDetailsRaw>) {
        super.onNext(data)
        recyclerViewAdapter.swapDataSet(data)
    }

    companion object {
        const val PAGE_LIMIT = 20
    }
}