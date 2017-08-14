package com.yichiuan.moedict.ui.main;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yichiuan.moedict.R;
import com.yichiuan.moedict.common.ui.StaticTextView;
import com.yichiuan.moedict.common.TextUtil;

import moe.Heteronym;
import moe.Word;

public class HeteronymAdapter extends RecyclerView.Adapter<HeteronymAdapter.HeteronymViewHolder> {

    LayoutInflater inflater;

    Word word;

    public HeteronymAdapter(Context context, Word word) {
        inflater = LayoutInflater.from(context);
        this.word = word;
    }

    public void setWord(Word word) {
        this.word = word;
        notifyDataSetChanged();
    }

    @Override
    public HeteronymAdapter.HeteronymViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HeteronymAdapter.HeteronymViewHolder(
                inflater.inflate(R.layout.item_heteronym, parent, false));
    }

    @Override
    public void onBindViewHolder(HeteronymViewHolder holder, int position) {
        holder.title.setText(
                TextUtil.obtainSpannedFrom(word.titleAsByteBuffer()));

        Heteronym heteronym = word.heteronyms(position);
        String bopomofo = heteronym.bopomofo();
        holder.title.setPinyin(bopomofo);
        holder.recyclerView.setAdapter(
                new DefinitionAdapter(holder.recyclerView.getContext(), heteronym));
    }

    @Override
    public int getItemCount() {
        return word.heteronymsLength();
    }

    static class HeteronymViewHolder extends RecyclerView.ViewHolder {

        StaticTextView title;
        RecyclerView recyclerView;

        public HeteronymViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.staticview_title);
            recyclerView = itemView.findViewById(R.id.recyclerview_definition);
            recyclerView.setLayoutManager(
                    new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL,
                            false));
        }
    }
}
