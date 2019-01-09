package com.cryptopia.android.pPeterle.ui.fragment


import android.arch.lifecycle.Observer
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptopia.android.pPeterle.R
import com.cryptopia.android.pPeterle.presentation.Failure
import com.cryptopia.android.pPeterle.presentation.Success
import com.cryptopia.android.pPeterle.presentation.ViewStateList
import com.cryptopia.android.pPeterle.presentation.viewModel.DepthViewModel
import com.cryptopia.android.pPeterle.utils.getColor
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.fragment_depth_chart.view.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 *
 */
class DepthChartFragment : Fragment() {

    private val mVieModel: DepthViewModel by viewModel()

    private lateinit var lineChart: LineChart

    companion object {
        private const val LABEL_KEY = "label_key"

        fun newInstance(label: String) = DepthChartFragment().apply {
            val bundle = Bundle().apply { putString(LABEL_KEY, label) }
            arguments = bundle
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_depth_chart, container, false)

        arguments?.getString(LABEL_KEY)?.let { mVieModel.label = it }

        mVieModel.getMarketOrders()

        lineChart = view.lineChart_depth.apply {
            setBackgroundColor(Color.TRANSPARENT)
            description.isEnabled = false
            setDrawGridBackground(false)
            legend.isEnabled = false
            setDrawBorders(false)

            axisLeft.textColor = Color.WHITE
            axisLeft.axisMinimum = 0f

            axisLeft.gridColor = getColor(R.color.colorPrimary)
            axisRight.setDrawGridLines(false)
            axisRight.setDrawLabels(false)

            xAxis.gridColor = getColor(R.color.colorPrimary)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textColor = Color.WHITE

            setTouchEnabled(true)
            setScaleEnabled(true)
            setPinchZoom(true)

            animateXY(600, 600)
        }

        mVieModel.getState().observe(this, Observer {
            it?.let { handleState(it) }
        })


        return view
    }


    private fun handleState(viewState: ViewStateList<ILineDataSet>) {
        when (viewState) {
            is Success<List<ILineDataSet>> -> handleSuccess(viewState.data)
            is Failure -> handleError(viewState.error)
            //is Loading -> handleLoading()
        }
    }

    private fun handleSuccess(data: List<ILineDataSet>) {

        val lineData = LineData(data).apply {
            setDrawValues(false)
            isHighlightEnabled = false
        }
        lineChart.data = lineData
        lineChart.invalidate()
    }

    private fun handleError(error: Throwable) {
        error.printStackTrace()
    }

}
