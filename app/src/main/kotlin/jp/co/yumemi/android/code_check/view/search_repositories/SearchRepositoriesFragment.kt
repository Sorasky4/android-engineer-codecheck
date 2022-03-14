/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.view.search_repositories

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.databinding.FragmentSearchRepositoriesBinding
import jp.co.yumemi.android.code_check.model.entity.Item
import jp.co.yumemi.android.code_check.viewmodel.SearchRepositoriesViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * リポジトリ検索画面
 */
class SearchRepositoriesFragment : Fragment(R.layout.fragment_search_repositories) {
    private val viewModel: SearchRepositoriesViewModel by viewModels()
    private var _binding: FragmentSearchRepositoriesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSearchRepositoriesBinding.bind(view)

        val layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration =
            DividerItemDecoration(context, layoutManager.orientation)
        val adapter = SearchRepositoryAdapter(object : SearchRepositoryAdapter.OnItemClickListener {
            override fun itemClick(item: Item) {
                gotoRepositoryFragment(item)
            }
        })
        binding.recyclerView.also {
            it.layoutManager = layoutManager
            it.addItemDecoration(dividerItemDecoration)
            it.adapter = adapter
        }

        binding.searchInputText.setOnEditorActionListener { editText, action, _ ->
            if (action == EditorInfo.IME_ACTION_SEARCH) {
                editText.text.toString().let {
                    viewModel.searchResults(it)
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.error.onEach {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }.launchIn(lifecycleScope)
    }

    fun gotoRepositoryFragment(item: Item) {
        val action = SearchRepositoriesFragmentDirections
            .actionRepositoriesFragmentToRepositoryFragment(item = item)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
