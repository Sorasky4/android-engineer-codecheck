/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import jp.co.yumemi.android.code_check.model.entity.item
import jp.co.yumemi.android.code_check.repository.api.SearchRepository
import kotlinx.coroutines.launch

/**
 * TwoFragment で使う
 */
class OneViewModel(application: Application): AndroidViewModel(application) {
    private val context get() = getApplication<Application>().applicationContext
    private val _data = MutableLiveData<List<item>>()
    val data: LiveData<List<item>> get() = _data

    // 検索結果
    fun searchResults(inputText: String){
        viewModelScope.launch {
            _data.postValue(SearchRepository().getRepositories(context, inputText))
        }
    }
}