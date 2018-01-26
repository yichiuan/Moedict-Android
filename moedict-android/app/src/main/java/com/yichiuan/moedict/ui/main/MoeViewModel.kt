package com.yichiuan.moedict.ui.main

import android.arch.lifecycle.ViewModel
import com.yichiuan.moedict.data.MoeRepository
import moe.Word

class MoeViewModel(private val moeRepository: MoeRepository) : ViewModel() {

    fun getMoeWord(query: String): Word? {
        return moeRepository.getMoeWord(query)
    }
}
