/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.view.searchRepositories

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.databinding.FragmentOneBinding
import jp.co.yumemi.android.code_check.model.entity.Item
import jp.co.yumemi.android.code_check.viewmodel.OneViewModel

class OneFragment : Fragment(R.layout.fragment_one) {
    val viewModel: OneViewModel by viewModels()
    private var _binding: FragmentOneBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentOneBinding.bind(view)

        val _layoutManager = LinearLayoutManager(context!!)
        val _dividerItemDecoration =
            DividerItemDecoration(context!!, _layoutManager.orientation)
        val _adapter = CustomAdapter(object : CustomAdapter.OnItemClickListener {
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
            it.layoutManager = _layoutManager
            it.addItemDecoration(_dividerItemDecoration)
            it.adapter = _adapter
        }

        viewModel.data.observe(viewLifecycleOwner, {
            _adapter.submitList(it)
        })
    }

    fun gotoRepositoryFragment(Item: Item) {
        val _action = OneFragmentDirections
            .actionRepositoriesFragmentToRepositoryFragment(item = Item)
        findNavController().navigate(_action)
    }
}
