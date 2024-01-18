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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SavedQuotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedQuotesFragment : Fragment(), SavedQuotesAdapter.OnItemClickListeners {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding : FragmentSavedQuotesBinding
    lateinit var recyclerView: RecyclerView
    var adapter : SavedQuotesAdapter? = null
    var list : ArrayList<SavedItem>? = null

    private val viewModel: QuotesViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("###", "onCreate: ")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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
                        Log.d("@@@", "setUpObservers: ${list!!.isEmpty()}")
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
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SavedQuotesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SavedQuotesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemDeleted(position: Int) {
        viewModel.deleteSavedQuotes(list!!.get(position))
        list!!.removeAt(position)
        if (list!!.isEmpty()) {
            binding.textviewSavedQuotesStatus.visibility = View.VISIBLE
        }
    }
}