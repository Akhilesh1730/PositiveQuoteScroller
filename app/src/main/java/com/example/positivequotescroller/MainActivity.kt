package com.example.positivequotescroller

import QuotesPagerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.positivequotescroller.databinding.ActivityMainBinding
import com.example.positivequotescroller.db.QuoteDBHelper
import com.example.positivequotescroller.repo.QuotesRepository
import com.example.positivequotescroller.retro.RetrofitHelper
import com.example.positivequotescroller.viewmodel.QuotesViewModel
import com.example.positivequotescroller.viewmodel.QuotesViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

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
        Log.d("###", "onCreate: $viewModel")
        TabLayoutMediator(tab, viewPager) { tab, position ->
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

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }
}
