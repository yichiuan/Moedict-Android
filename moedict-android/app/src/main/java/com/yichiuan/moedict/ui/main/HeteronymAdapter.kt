package com.yichiuan.moedict.ui.main

import android.arch.lifecycle.Lifecycle
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.yichiuan.moedict.BuildConfig
import com.yichiuan.moedict.R
import com.yichiuan.moedict.common.TextUtil
import com.yichiuan.moedict.common.VoicePlayer
import com.yichiuan.moedict.common.ui.StaticTextView
import moe.Word

class HeteronymAdapter(context: Context, private var word: Word?,lifecycle: Lifecycle)
    : RecyclerView.Adapter<HeteronymAdapter.HeteronymViewHolder>() {

    companion object {
        private const val VOICE_URL = BuildConfig.VOICE_BASE_URL + "%s.mp3"
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    internal var voicePlayer: VoicePlayer = VoicePlayer(context, lifecycle)

    fun setWord(word: Word) {
        this.word = word
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeteronymAdapter.HeteronymViewHolder {
        return HeteronymAdapter.HeteronymViewHolder(
                inflater.inflate(R.layout.item_heteronym, parent, false))
    }

    override fun onBindViewHolder(holder: HeteronymViewHolder, position: Int) {
        holder.title.setText(
                TextUtil.obtainSpannedFrom(word!!.titleAsByteBuffer()))

        val heteronym = word!!.heteronyms(position)

        holder.playButton.setOnClickListener { v ->
            val button = v as Button
            val voiceId = heteronym.audioId()

            if (voicePlayer.isPlaying) {
                voicePlayer.stopVoice()
                button.text = "Play"
            } else {
                playVoice(voiceId)

                voicePlayer.setOnLoadedListener(object : VoicePlayer.OnLoadedListener {
                    override fun onLoaded() {
                        button.text = "Stop"
                    }
                })

                button.text = "Loading"
            }
        }

        val bopomofo = heteronym.bopomofo()
        holder.title.setPinyin(bopomofo!!)
        holder.recyclerView.adapter = DefinitionAdapter(holder.recyclerView.context, heteronym)
    }

    internal fun playVoice(voiceId: String?) {
        val url = String.format(VOICE_URL, voiceId)
        voicePlayer.playVoice(url)
    }

    override fun getItemCount(): Int {
        return word!!.heteronymsLength()
    }

    class HeteronymViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: StaticTextView = itemView.findViewById(R.id.staticview_title)
        var playButton: Button = itemView.findViewById(R.id.button_play)
        var recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerview_definition)

        init {
            recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL,
                    false)
        }
    }
}
