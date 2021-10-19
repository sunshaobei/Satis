package com.satis.sliver.recyclerview.wrapper

class LoadMoreStateManager(val loadMoreWrapper: LoadMoreWrapper){
    fun setLoadMoreState(loadMoreState: LoadMoreState){
        loadMoreWrapper.mLoadMoreState =loadMoreState
    }

}