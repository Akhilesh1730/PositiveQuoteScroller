package com.example.positivequotescroller

import QuotesPagerAdapter
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.lifecycle.ViewModelProvider
import com.example.positivequotescroller.databinding.ActivityMainBinding
import com.example.positivequotescroller.db.QuoteDBHelper
import com.example.positivequotescroller.repo.QuotesRepository
import com.example.positivequotescroller.retro.RetrofitHelper
import com.example.positivequotescroller.view.QuotesFragment
import com.example.positivequotescroller.viewmodel.QuotesViewModel
import com.example.positivequotescroller.viewmodel.QuotesViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    lateinit var tab : TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tab = binding.tablayout
        val viewPager = binding.viewPager
        val adapter = QuotesPagerAdapter(this)
        viewPager.adapter = adapter
        val factory = QuotesViewModelFactory(
            QuotesRepository(RetrofitHelper.apiService, QuoteDBHelper(this, null)))
        val viewModel = ViewModelProvider(this, factory).get(QuotesViewModel::class.java)
        TabLayoutMediator(tab, viewPager) { tab, position ->
            // Customize tab labels or icons if needed
            /*tab.text = "Tab $position"*/
            when(position) {
                0 -> tab.text = "Quotes"
                else -> tab.text = "Saved"
            }
        }.attach()
        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Do nothing
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Do nothing
            }
        })
        tab.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            adjustTabMargins()
        }
        /* if (savedInstanceState == null) {
             supportFragmentManager.beginTransaction()
                 .replace(R.id.fragmentContainerView_quotes, QuotesFragment())
                 .commit()
         }*/
    }
    private fun adjustTabMargins() {
        val tabLayoutView = tab.getChildAt(0) as LinearLayout

        val tabCount = tab.tabCount
        for (i in 0 until tabCount) {
            val tab = tabLayoutView.getChildAt(i)

            val params = tab.layoutParams as LinearLayout.LayoutParams

            // Set your desired margin values directly
            val marginStart = 16 // Example value, replace with your desired value in pixels
            val marginEnd = 16   // Example value, replace with your desired value in pixels

            // Check if the device is in landscape mode
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // Add margins for landscape mode
                params.marginStart = marginStart
                params.marginEnd = marginEnd
            } else {
                // Remove margins for portrait mode
                params.marginStart = 0
                params.marginEnd = 0
            }

            tab.layoutParams = params
        }
    }
}