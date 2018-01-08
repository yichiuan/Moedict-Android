package com.yichiuan.moedict.ui.main;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yichiuan.moedict.BuildConfig;
import com.yichiuan.moedict.R;
import com.yichiuan.moedict.common.TextUtil;
import com.yichiuan.moedict.common.VoicePlayer;
import com.yichiuan.moedict.common.ui.StaticTextView;

import moe.Heteronym;
import moe.Word;

public class HeteronymAdapter extends RecyclerView.Adapter<HeteronymAdapter.HeteronymViewHolder> {

    private static final String VOICE_URL = BuildConfig.VOICE_BASE_URL + "%s.mp3";

    private LayoutInflater inflater;

    private Word word;

    VoicePlayer voicePlayer;

    public HeteronymAdapter(Context context, Word word, Lifecycle lifecycle) {
        inflater = LayoutInflater.from(context);
        this.word = word;

        voicePlayer = new VoicePlayer(context, lifecycle);
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

        holder.playButton.setOnClickListener(v -> {
            Button button = (Button) v;
            String voiceId = heteronym.audioId();

            if (voicePlayer.isPlaying()) {
                voicePlayer.stopVoice();
                button.setText("Play");
            } else {
                playVoice(voiceId);

                voicePlayer.setOnLoadedListener(() -> button.setText("Stop"));
                button.setText("Loading");
            }
        });

        String bopomofo = heteronym.bopomofo();
        holder.title.setPinyin(bopomofo);
        holder.recyclerView.setAdapter(
                new DefinitionAdapter(holder.recyclerView.getContext(), heteronym));
    }

    void playVoice(String voiceId) {
        String url = String.format(VOICE_URL, voiceId);
        voicePlayer.playVoice(url);
    }

    @Override
    public int getItemCount() {
        return word.heteronymsLength();
    }

    static class HeteronymViewHolder extends RecyclerView.ViewHolder {

        StaticTextView title;
        Button playButton;
        RecyclerView recyclerView;

        HeteronymViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.staticview_title);
            playButton = itemView.findViewById(R.id.button_play);
            recyclerView = itemView.findViewById(R.id.recyclerview_definition);
            recyclerView.setLayoutManager(
                    new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL,
                            false));
        }
    }
}
