package com.yichiuan.moedict.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Formatter;

import io.reactivex.Completable;
import moe.Dictionary;
import moe.Index;
import moe.Word;

public class MoeRepository {

    private static final int MOD_MASK = 0x3FF; // 0b1111111111
    private static final int BUFFER_SIZE = (int)(1024 * 1024 * 1.2); // 1.2 MB

    private static final String MOE_INDEX_DATAPATH = "moe/index.bin";

    private Context context;

    private StringBuilder appendable = new StringBuilder();
    private Formatter formatter = new Formatter(appendable);

    private byte[] dictBuffer;

    private Index index;

    // Private constructor prevents instantiation from other classes
    MoeRepository(Context context) {
        this.context = context;
    }

    public Word getMoeWord(String word) throws IOException {

        int firstWord = word.codePointAt(0);
        int mod = firstWord & MOD_MASK; // code point % 1024

        appendable.setLength(0);
        String dataPath = formatter.format("moe/%d.bin", mod).toString();

        Dictionary dict = Dictionary.getRootAsDictionary(loadDictData(dataPath));
        return dict.wordsByKey(word);
    }

    @VisibleForTesting
    Dictionary getMoeDictionary(int mod) throws IOException {
        appendable.setLength(0);
        String dataPath = formatter.format("moe/%d.bin", mod).toString();
        return Dictionary.getRootAsDictionary(loadDictData(dataPath));
    }

    public Completable loadIndexData() {
        return Completable.fromAction(() -> {
            if (index == null) {
                index = Index.getRootAsIndex(loadData(MOE_INDEX_DATAPATH, null));
            }
        });
    }

    private ByteBuffer loadDictData(String dataPath) throws IOException {
        if (dictBuffer == null) {
            dictBuffer = new byte[BUFFER_SIZE];
        }
        return loadData(dataPath, dictBuffer);
    }

    private ByteBuffer loadData(@NonNull String dataPath, @Nullable byte[] buffer) throws IOException {

        InputStream is = null;
        ByteBuffer byteBuffer = null;
        try {
            is = context.getAssets().open(dataPath);
            byteBuffer = loadData(is, buffer);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {

                }
            }
        }
        return byteBuffer;
    }

    private ByteBuffer loadData(@NonNull InputStream is, @Nullable byte[] buffer) throws IOException {

        int readSize = 0;
        byte[] readBuffer = buffer;

        int size = is.available();

        if (readBuffer == null || readBuffer.length < size) {
            readBuffer = new byte[size];
        }
        readSize = is.read(readBuffer, 0, size);

        is.close();

        return ByteBuffer.wrap(readBuffer, 0, readSize);
    }

    public ArrayList<Integer> search(String query) {
        return index.search(query);
    }

    public String getWord(int position) {
        return index.words(position);
    }
}