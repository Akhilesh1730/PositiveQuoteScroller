package com.example.positivequotescroller.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.positivequotescroller.R
import com.example.positivequotescroller.Resource
import com.example.positivequotescroller.adapter.QuotesAdapter
import com.example.positivequotescroller.adapter.SavedQuotesAdapter
import com.example.positivequotescroller.databinding.FragmentSavedQuotesBinding
import com.example.positivequotescroller.model.QuoteListItem
import com.example.positivequotescroller.model.SavedItem
import com.example.positivequotescroller.viewmodel.QuotesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedQuotesFragment : Fragment(), SavedQuotesAdapter.OnItemClickListeners {


    lateinit var binding : FragmentSavedQuotesBinding
    lateinit var recyclerView: RecyclerView
    var adapter : SavedQuotesAdapter? = null
    var list : ArrayList<SavedItem>? = null

    private val viewModel: QuotesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedQuotesBinding.inflate(inflater, container, false)
        val view = binding.root
        recyclerView = binding.recyclerViewSavedQuotes
        setUpRecyclerView()
        setUpObservers()
        return view
    }

    override fun onResume() {
        super.onResume()
        showProgressDialog()
        viewModel.getSavedQuotes()
    }

    override fun onPause() {
        super.onPause()
        adapter?.notifyDataSetChanged()
    }
    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
    }

    private fun setUpObservers() {
        lifecycleScope.launch {
            viewModel.getSavedQuotesFlow().collect {resource ->
                when(resource) {
                    is Resource.Success -> {
                        list = resource.data as ArrayList<SavedItem>?
                        if (list!!.isEmpty()) {
                            binding.textviewSavedQuotesStatus.visibility = View.VISIBLE
                            hideProgressDialog()
                        } else {
                            binding.textviewSavedQuotesStatus.visibility = View.GONE
                            setAdapter(list)
                        }
                    }
                    is Resource.Error -> {
                        binding.textviewSavedQuotesStatus.visibility = View.VISIBLE
                        showToast("Something went wrong")
                        hideProgressDialog()
                    }
                    is Resource.Loading -> {
                        showProgressDialog()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.getSavedDeletedQuotesFlow().collect {resource ->
                when(resource) {
                    is Resource.Success -> {
                        if (resource.data == 1L) {
                            adapter!!.notifyDataSetChanged()
                            if (list!!.size == 0) {
                                binding.textviewSavedQuotesStatus.visibility = View.VISIBLE
                            }
                            showToast("Deleted successfully")
                        }
                    }
                    is Resource.Error -> {
                        showToast(resource.message)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setAdapter(dataSet : List<SavedItem>?) {
        adapter = SavedQuotesAdapter(this , dataSet)
        recyclerView.adapter = adapter
        hideProgressDialog()
    }

    private fun showToast(message : String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showProgressDialog() {
        binding.progressBarSavedquotes.visibility = View.VISIBLE
    }

    private fun hideProgressDialog() {
        binding.progressBarSavedquotes.visibility = View.GONE
    }

    override fun onItemDeleted(position: Int) {
        viewModel.deleteSavedQuotes(list!!.get(position))
        list!!.removeAt(position)
        if (list!!.isEmpty()) {
            binding.textviewSavedQuotesStatus.visibility = View.VISIBLE
        }
    }
}