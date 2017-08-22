package com.yichiuan.moedict.ui.main;

import android.arch.lifecycle.ViewModel;

import com.yichiuan.moedict.data.MoeRepository;

import java.io.IOException;

import moe.Word;

public class MoeViewModel extends ViewModel {
    private MoeRepository moeRepository;

    public MoeViewModel(MoeRepository moeRepository) {
        this.moeRepository = moeRepository;
    }

    public Word getMoeWord(String query) throws IOException {
        return moeRepository.getMoeWord(query);
    }
}
