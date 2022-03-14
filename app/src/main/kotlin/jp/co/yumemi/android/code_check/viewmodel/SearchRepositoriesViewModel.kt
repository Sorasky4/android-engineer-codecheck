/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import jp.co.yumemi.android.code_check.model.entity.Item
import jp.co.yumemi.android.code_check.repository.api.SearchRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

/**
 * リポジトリ検索画面のViewModel
 */
class SearchRepositoriesViewModel(application: Application) : AndroidViewModel(application) {
    private val context get() = getApplication<Application>().applicationContext

    private val _data = MutableLiveData<List<Item>>()
    val data: LiveData<List<Item>> get() = _data

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> get() = _error

    // 検索結果
    fun searchResults(inputText: String) {
        viewModelScope.launch {
            try {
                val repositories = SearchRepository().getRepositories(context, inputText)
                _data.postValue(repositories)
            } catch (e: Exception) {
                val message = e.message.toString()
                Log.e("SearchRepositoriesViewModel", message)
                when {
                    Regex("invalid: 422").containsMatchIn(message) -> {
                        _error.emit("エラーが発生しました。別の文字を入力して検索してください。")
                    }
                    Regex("Unable").containsMatchIn(message) -> {
                        _error.emit("エラーが発生しました。通信環境を確認してください。")
                    }
                    else -> {
                        _error.emit("検索処理でエラーが発生しました")
                    }
                }
            }
        }
    }
}