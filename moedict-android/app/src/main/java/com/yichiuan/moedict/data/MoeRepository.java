package com.yichiuan.moedict.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Formatter;

import moe.Dictionary;
import moe.Index;
import moe.Word;
import timber.log.Timber;

public class MoeRepository {

    private static final int MOD_MASK = 0x3FF; // 0b1111111111
    private static final int BUFFER_SIZE = (int)(1024 * 1024 * 1.2); // 1.2 MB

    private static final String MOE_INDEX_DATAPATH = "moe/index.bin";

    private Context context;

    private StringBuilder appendable = new StringBuilder();
    private Formatter formatter = new Formatter(appendable);

    private byte[] dictBuffer;

    private ByteBuffer indexBuffer;

    // Private constructor prevents instantiation from other classes
    MoeRepository(Context context) {
        this.context = context;
    }

    public Word getMoeWord(String word) {

        int firstWord = word.codePointAt(0);
        int mod = firstWord & MOD_MASK; // code point % 1024

        appendable.setLength(0);
        String dataPath = formatter.format("moe/%d.bin", mod).toString();

        Dictionary dict = Dictionary.getRootAsDictionary(loadDictData(dataPath));
        return dict.wordsByKey(word);
    }

    public Index getIndex() {
        if (indexBuffer == null) {
            indexBuffer = loadData(MOE_INDEX_DATAPATH, null);
        }
        return Index.getRootAsIndex(indexBuffer);
    }

    private ByteBuffer loadDictData(String dataPath) {
        if (dictBuffer == null) {
            dictBuffer = new byte[BUFFER_SIZE];
        }
        return loadData(dataPath, dictBuffer);
    }

    private ByteBuffer loadData(@NonNull String dataPath, @Nullable byte[] buffer) {

        int readSize = 0;
        byte[] readBuffer = buffer;

        try {
            final InputStream is = context.getAssets().open(dataPath);

            int size = is.available();

            if (readBuffer == null || readBuffer.length < size) {
                readBuffer = new byte[size];
            }
            readSize = is.read(readBuffer, 0, size);

            is.close();

        } catch (IOException e) {
            Timber.e(e);
        }

        return ByteBuffer.wrap(readBuffer, 0, readSize);
    }
}