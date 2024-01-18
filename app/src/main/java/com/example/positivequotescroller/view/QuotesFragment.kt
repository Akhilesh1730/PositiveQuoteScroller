package com.example.positivequotescroller.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.positivequotescroller.Resource
import com.example.positivequotescroller.adapter.QuotesAdapter
import com.example.positivequotescroller.databinding.FragmentQuotesBinding
import com.example.positivequotescroller.db.QuoteDBHelper
import com.example.positivequotescroller.model.QuoteListItem
import com.example.positivequotescroller.viewmodel.QuotesViewModel
import kotlinx.coroutines.launch

class QuotesFragment : Fragment(), QuotesAdapter.OnItemClickListener {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentQuotesBinding
    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : QuotesAdapter
    private var list: List<QuoteListItem>? = null
    private val viewModel: QuotesViewModel by activityViewModels()
    private lateinit var  dbHelper : QuoteDBHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuotesBinding.inflate(inflater, container, false)
        val view = binding.root
        recyclerView = binding.recyclerviewQuotes
        dbHelper = QuoteDBHelper(requireContext(), null)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getQuotes()
        setUpRecyclerView()
        setUpObservers()
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
    }

    private fun setUpObservers() {
        lifecycleScope.launch {
            viewModel.getQuotesFlow().collect {resource ->
                when(resource) {
                    is Resource.Success -> {
                        list = resource.data
                        setAdapter(resource.data)
                        hideProgressDialog()
                    }
                    is Resource.Error -> {
                        showToast(resource.message)
                        hideProgressDialog()
                    }
                    else -> showProgressDialog()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.getQuotesAddedFlow().collect {resource ->
                when(resource) {
                    is Resource.Success -> {
                        if (resource.data == 1L) {
                            showToast("Quotes Saved Successfully")
                        }
                    }
                    is Resource.Error -> {
                        showToast(resource.message)
                    }
                    else -> showProgressDialog()
                }
            }
        }
    }

    private fun setAdapter(dataSet : List<QuoteListItem>?) {
        adapter = QuotesAdapter(dataSet , this)
        recyclerView.adapter = adapter
    }
    private fun showToast(message : String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showProgressDialog() {
        binding.progressBarQuotes.visibility = View.VISIBLE
    }

    private fun hideProgressDialog() {
        binding.progressBarQuotes.visibility = View.GONE
    }
    override fun onItemSaved(position: Int) {
        val item = list?.get(position)
        viewModel.addSavedQuote(item)
    }
}