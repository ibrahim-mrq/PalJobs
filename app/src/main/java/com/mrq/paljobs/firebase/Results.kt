package com.mrq.paljobs.firebase

interface Results<T> {
    fun onSuccess(t: T)
    fun onFailureInternet(offline: String)
    fun onEmpty()
    fun onLoading(boolean: Boolean)
}