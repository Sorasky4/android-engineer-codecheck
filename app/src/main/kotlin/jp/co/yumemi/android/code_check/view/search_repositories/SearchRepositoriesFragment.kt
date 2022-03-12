/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.view.search_repositories

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.databinding.FragmentSearchRepositoriesBinding
import jp.co.yumemi.android.code_check.model.entity.Item
import jp.co.yumemi.android.code_check.viewmodel.SearchRepositoriesViewModel

/**
 * リポジトリ検索画面
 */
class SearchRepositoriesFragment : Fragment(R.layout.fragment_search_repositories) {
    val viewModel: SearchRepositoriesViewModel by viewModels()
    private var _binding: FragmentSearchRepositoriesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSearchRepositoriesBinding.bind(view)

        val layoutManager = LinearLayoutManager(context!!)
        val dividerItemDecoration =
            DividerItemDecoration(context!!, layoutManager.orientation)
        val adapter = SearchRepositoryAdapter(object : SearchRepositoryAdapter.OnItemClickListener {
            override fun itemClick(Item: Item) {
                gotoRepositoryFragment(Item)
            }
        })

        binding.searchInputText
            .setOnEditorActionListener { editText, action, _ ->
                if (action == EditorInfo.IME_ACTION_SEARCH) {
                    editText.text.toString().let {
                        viewModel.searchResults(it).apply {
                        }
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

        binding.recyclerView.also {
            it.layoutManager = layoutManager
            it.addItemDecoration(dividerItemDecoration)
            it.adapter = adapter
        }

        viewModel.data.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    fun gotoRepositoryFragment(Item: Item) {
        val action = SearchRepositoriesFragmentDirections
            .actionRepositoriesFragmentToRepositoryFragment(item = Item)
        findNavController().navigate(action)
    }
}
