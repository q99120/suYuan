package cn.work.suyuan.ui.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.work.suyuan.R
import cn.work.suyuan.common.extensions.setOnClickListener
import cn.work.suyuan.common.extensions.toast
import cn.work.suyuan.common.ui.BaseFragment
import cn.work.suyuan.util.InjectorUtil
import cn.work.suyuan.util.SinglePopUtil
import kotlinx.android.synthetic.main.fragment_send_manage.*
import kotlinx.android.synthetic.main.layout_send_manage_fm.*

/**
 * 整箱发货
 */
class PackSendFragment: BaseFragment(){


    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getSendViewModelFactory()
        ).get(SendPackViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater.inflate(R.layout.fragment_send_manage, container, false))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvSendQrTitle.text = "外箱条码"
        layoutFloWater.visibility = View.GONE
        initViews()
        observer()
    }



    private fun initViews() {
        setOnClickListener(tvChooseDistributor,btnSend,tvChooseProduct){
            when(this){
                tvChooseDistributor->{ viewModel.getDistributor() }
                tvChooseProduct->{viewModel.getProduct()}
                btnSend->{ viewModel.sendProduct(2,productId,distributorId,productCode,productTime,productFile) }
            }
        }
    }

    private var productId = -1
    private var distributorId = -1
    private var productCode = ""
    private var productTime = ""
    private var productFile = ""
    private fun observer() {
        viewModel.distributorLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull()?:return@Observer
            val popUtil = SinglePopUtil(requireContext(),object : SinglePopUtil.popClick{
                override fun clickPop(type: String, pi: Int) {
                    tvChooseDistributor.text = type
                    distributorId = pi }

            })
            popUtil.setData(rp.data,1)
            popUtil.showAsDropDown(tvChooseDistributor)
        })
        viewModel.productLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull()?:return@Observer
            val popUtil = SinglePopUtil(requireContext(),object : SinglePopUtil.popClick{
                override fun clickPop(type: String, pi: Int) {
                    tvChooseProduct.text = type
                    productId = pi }

            })
            popUtil.setData(rp.data,2)
            popUtil.showAsDropDown(tvChooseProduct)
        })
        viewModel.sendProductLiveData.observe(viewLifecycleOwner, Observer {
            val rp = it.getOrNull()?:return@Observer
            if (rp.code == 200) rp.msg.toast()
        })
    }


    override fun loadDataOnce() {
        super.loadDataOnce()
    }


    companion object {
        fun newInstance() = PackSendFragment()
    }

}