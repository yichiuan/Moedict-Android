package com.yichiuan.moedict.data;

import android.content.Context;
import android.content.res.AssetManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import moe.Dictionary;
import moe.Heteronym;
import moe.Word;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class MoeDictDataTest {

    @Mock
    private Context context;

    @Mock
    private AssetManager assetManager;

    private MoeRepository moeRepository;

    @Before
    public void setUp() {
        moeRepository = new MoeRepository(context);
    }

    @Test
    public void test_bopomofo_format() throws IOException {
        // this's going to be used in moeRepository.getMoeDictionary()
        InputStream is = getClass().getClassLoader().getResourceAsStream("assets/moe/1.bin");

        when(context.getAssets()).thenReturn(assetManager);
        when(assetManager.open(anyString())).thenReturn(is);

        Dictionary dict = moeRepository.getMoeDictionary(121);

        int wordLength = dict.wordsLength();

        for (int i = 0; i < wordLength; i++) {
            Word word = dict.words(i);
            String title = obtainCleanString(word.titleAsByteBuffer());

            int heteronymLength = word.heteronymsLength();

            for (int j = 0; j < heteronymLength; j++) {

                Heteronym heteronym = word.heteronyms(j);

                String bopomofo = heteronym.bopomofo();
                if (bopomofo != null) {
                    String[] pinyins = bopomofo.split("[ 　]");

                    if (pinyins.length != title.length()) {
                        System.out.println(title + " : " + bopomofo);
                    }
                }
            }
        }
    }

    private static String obtainCleanString(ByteBuffer bytes) {
        CharBuffer buf = bytes.asCharBuffer();

        final String removeChars = "`~，";

        final StringBuilder builder = new StringBuilder();

        final int endPos = buf.limit();
        for (int i = buf.position(); i < endPos; i++) {
            final char current = buf.get(i);
            if (removeChars.indexOf(current) > -1) continue;
            builder.append(current);
        }

        return builder.toString();
    }
}