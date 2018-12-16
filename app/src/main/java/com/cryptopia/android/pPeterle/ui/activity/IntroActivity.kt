package com.cryptopia.android.pPeterle.ui.activity

import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cryptopia.android.pPeterle.R
import com.cryptopia.android.pPeterle.ui.fragment.KeysFragment
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {


    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        fab.setOnClickListener {
            container.currentItem = container.currentItem + 1
        }

        //CRIA HISTORIO DE ORDENS
        //MELHORAR TELA INICIAL
        //FILTRO NA HOME
        //home page + alerta de preco
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int) = when (position) {
            0 -> IntroFragment.newInstance()
            1 -> IntroFragment2.newInstance()
            2 -> KeysFragment()
            else -> IntroFragment.newInstance()
        }

        override fun getCount() = 3
    }

    class IntroFragment : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View = inflater.inflate(R.layout.fragment_intro, container, false)


        companion object {

            fun newInstance() = IntroFragment()
        }
    }

    class IntroFragment2 : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View = inflater.inflate(R.layout.fragment_intro2, container, false)


        companion object {

            fun newInstance() = IntroFragment2()
        }
    }
}
