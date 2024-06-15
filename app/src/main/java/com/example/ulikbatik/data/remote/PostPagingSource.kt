package com.example.ulikbatik.data.remote



import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ulikbatik.data.model.PostModel
import com.example.ulikbatik.data.remote.config.ApiService

class PostPagingSource(private val apiService: ApiService) : PagingSource<Int, PostModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostModel> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getAllPosts(page)
            val responseData = response.data
            LoadResult.Page(
                data = responseData,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PostModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}