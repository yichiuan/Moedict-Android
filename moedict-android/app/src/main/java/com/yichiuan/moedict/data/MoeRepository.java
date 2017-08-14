package com.yichiuan.moedict.data;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Formatter;

import moe.Dictionary;
import moe.Word;
import timber.log.Timber;

public class MoeRepository {

    private static final int MOD_MASK = 0x3FF; // 0b1111111111
    private static final int BUFFER_SIZE = (int)(1024 * 1024 * 1.2); // 1.2MB

    private Context context;

    private StringBuilder appendable = new StringBuilder();
    private Formatter formatter = new Formatter(appendable);

    private byte[] buffer;

    // Private constructor prevents instantiation from other classes
    MoeRepository(Context context) {
        this.context = context;
    }

    public Word getMoeWord(String word) {

        int firstWord = word.codePointAt(0);
        int mod = firstWord & MOD_MASK; // code point % 1024

        appendable.setLength(0);
        String dataPath = formatter.format("moe/%d.bin", mod).toString();

        Dictionary dict = Dictionary.getRootAsDictionary(loadData(dataPath));
        return dict.wordsByKey(word);
    }

    private ByteBuffer loadData(String dataPath) {

        int readSize = 0;
        try {
            final InputStream is = context.getAssets().open(dataPath);

            int size = is.available();

            if (buffer == null) {
                buffer = new byte[BUFFER_SIZE];
            }
            readSize = is.read(buffer, 0, size);

            is.close();

        } catch (IOException e) {
            Timber.e(e);
        }

        return ByteBuffer.wrap(buffer, 0, readSize);
    }
}