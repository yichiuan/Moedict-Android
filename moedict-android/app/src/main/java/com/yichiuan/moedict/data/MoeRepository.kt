package com.yichiuan.moedict.data

import android.content.Context
import android.support.annotation.VisibleForTesting
import io.reactivex.Completable
import moe.Dictionary
import moe.Index
import moe.Word
import java.io.InputStream
import java.nio.ByteBuffer
import java.util.*

class MoeRepository internal constructor(private val context: Context) {

    companion object {
        private const val MOD_MASK = 0x3FF // 0b1111111111
        private const val BUFFER_SIZE = (1024.0 * 1024.0 * 1.2).toInt() // 1.2 MB
        private const val MOE_INDEX_DATAPATH = "moe/index.bin"
    }

    private val appendable = StringBuilder()
    private val formatter = Formatter(appendable)

    private var dictBuffer: ByteArray? = null

    private var index: Index? = null

    fun getMoeWord(word: String): Word? {

        val firstWord = word.codePointAt(0)
        val mod = firstWord and MOD_MASK // code point % 1024

        appendable.setLength(0)
        val dataPath = formatter.format("moe/%d.bin", mod).toString()

        val dict = Dictionary.getRootAsDictionary(loadDictData(dataPath))
        return dict.wordsByKey(word)
    }

    @VisibleForTesting
    internal fun getMoeDictionary(mod: Int): Dictionary {
        appendable.setLength(0)
        val dataPath = formatter.format("moe/%d.bin", mod).toString()
        return Dictionary.getRootAsDictionary(loadDictData(dataPath))
    }

    fun loadIndexData(): Completable {
        return Completable.fromAction {
            if (index == null) {
                index = Index.getRootAsIndex(loadData(MOE_INDEX_DATAPATH, null))
            }
        }
    }

    private fun loadDictData(dataPath: String): ByteBuffer? {
        if (dictBuffer == null) {
            dictBuffer = ByteArray(BUFFER_SIZE)
        }
        return loadData(dataPath, dictBuffer)
    }


    private fun loadData(dataPath: String, buffer: ByteArray?): ByteBuffer? {

        var byteBuffer: ByteBuffer? = null

        context.assets.open(dataPath).use {
            byteBuffer = loadData(it, buffer)
        }

        return byteBuffer
    }


    private fun loadData(inputStream: InputStream, buffer: ByteArray?): ByteBuffer {

        var readBuffer = buffer

        val size = inputStream.available()

        if (readBuffer == null || readBuffer.size < size) {
            readBuffer = ByteArray(size)
        }
        val readSize = inputStream.read(readBuffer, 0, size)

        return ByteBuffer.wrap(readBuffer, 0, readSize)
    }

    fun search(query: String): ArrayList<Int> {
        return index!!.search(query)
    }

    fun getWord(position: Int): String? {
        return index!!.words(position)
    }
}