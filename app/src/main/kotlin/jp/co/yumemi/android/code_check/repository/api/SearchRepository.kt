package jp.co.yumemi.android.code_check.repository.api

import android.content.Context
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.TopActivity
import jp.co.yumemi.android.code_check.model.entity.Item
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class SearchRepository {
    suspend fun getRepositories(context: Context, inputText: String): List<Item> {
        val client = HttpClient(Android)

        val response: HttpResponse = client.get("https://api.github.com/search/repositories") {
            header("Accept", "application/vnd.github.v3+json")
            parameter("q", inputText)
        }

        val jsonBody = JSONObject(response.receive<String>())
        val jsonItems = jsonBody.optJSONArray("items") ?: JSONArray()
        val items = mutableListOf<Item>()

        // アイテムの個数分ループする
        for (i in 0 until jsonItems.length()) {
            val jsonItem = jsonItems.optJSONObject(i)
            val name = jsonItem.optString("full_name")
            val ownerIconUrl = jsonItem.optJSONObject("owner")?.optString("avatar_url") ?: ""
            val language = jsonItem.optString("language")
            val stargazersCount = jsonItem.optLong("stargazers_count")
            val watchersCount = jsonItem.optLong("watchers_count")
            val forksCount = jsonItem.optLong("forks_count")
            val openIssuesCount = jsonItem.optLong("open_issues_count")

            items.add(
                Item(
                    name = name,
                    ownerIconUrl = ownerIconUrl,
                    language = context.getString(R.string.written_language, language),
                    stargazersCount = stargazersCount,
                    watchersCount = watchersCount,
                    forksCount = forksCount,
                    openIssuesCount = openIssuesCount
                )
            )
        }

        TopActivity.lastSearchDate = Date()
        return items.toList()
    }
}