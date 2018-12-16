package com.cryptopia.android.pPeterle.ui.fragment

import android.arch.lifecycle.Observer
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptopia.android.pPeterle.R
import com.cryptopia.android.pPeterle.presentation.Failure
import com.cryptopia.android.pPeterle.presentation.Success
import com.cryptopia.android.pPeterle.presentation.ViewStateList
import com.cryptopia.android.pPeterle.presentation.viewModel.CandleStickViewModel
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import kotlinx.android.synthetic.main.fragment_candle_stick_chart.view.*
import org.koin.android.viewmodel.ext.android.viewModel


class CandleStickChartFragment : Fragment() {

    private val mVieModel: CandleStickViewModel by viewModel()

    private lateinit var candleStickChart: CandleStickChart

    companion object {
        private const val LABEL_KEY = "label_key"

        fun newInstance(label: String) = CandleStickChartFragment().apply {
            val bundle = Bundle().apply { putString(LABEL_KEY, label) }
            arguments = bundle
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_candle_stick_chart, container, false)

        arguments?.getString(LABEL_KEY)?.let { mVieModel.label = it }

        candleStickChart = view.candleStick_chart.apply {
            setBackgroundColor(Color.TRANSPARENT)
            description.isEnabled = false
            setDrawGridBackground(false)
            legend.isEnabled = false
            setDrawBorders(false)
            setNoDataText("")

            axisLeft.textColor = Color.WHITE
            axisLeft.axisMinimum = 0f

            axisLeft.gridColor = resources.getColor(R.color.colorPrimary)
            axisRight.setDrawGridLines(false)
            axisRight.setDrawLabels(false)

            xAxis.gridColor = resources.getColor(R.color.colorPrimary)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textColor = Color.WHITE

            setTouchEnabled(true)
            setScaleEnabled(true)
            setPinchZoom(true)

            animateXY(600, 600)
        }

        mVieModel.getMarketHistory()

        mVieModel.getState().observe(this, Observer {
            it?.let { handleState(it) }
        })

        return view
    }




    private fun handleState(viewState: ViewStateList<CandleEntry>) {
        when (viewState) {
            is Success<List<CandleEntry>> -> handleSuccess(viewState.data)
            is Failure -> handleError(viewState.error)
            //is Loading -> handleLoading()
        }
    }

    private fun handleSuccess(data: List<CandleEntry>) {
        val candleDataSet = CandleDataSet(data, "Grafico").apply {

            setDrawIcons(false)
            axisDependency = YAxis.AxisDependency.RIGHT
            shadowColor = Color.DKGRAY
            decreasingColor = Color.RED
            decreasingPaintStyle = Paint.Style.FILL
            increasingColor = Color.rgb(122, 242, 84)
            increasingPaintStyle = Paint.Style.STROKE
            neutralColor = Color.BLUE
        }

        val candleData = CandleData(candleDataSet).apply {
            setDrawValues(false)
            isHighlightEnabled = false
        }
        candleStickChart.data = candleData
        candleStickChart.invalidate()

    }

    private fun handleError(error: Throwable) {
        candleStickChart.setNoDataText("No Data Available")
        error.printStackTrace()
    }

}

