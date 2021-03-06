// automatically generated by the FlatBuffers compiler, do not modify

package moe.taiwan;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class Dictionary extends Table {
  public static Dictionary getRootAsDictionary(ByteBuffer _bb) { return getRootAsDictionary(_bb, new Dictionary()); }
  public static Dictionary getRootAsDictionary(ByteBuffer _bb, Dictionary obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; }
  public Dictionary __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public Word words(int j) { return words(new Word(), j); }
  public Word words(Word obj, int j) { int o = __offset(4); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int wordsLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }
  public Word wordsByKey(String key) { int o = __offset(4); return o != 0 ? Word.__lookup_by_key(__vector(o), key, bb) : null; }

  public static int createDictionary(FlatBufferBuilder builder,
      int wordsOffset) {
    builder.startObject(1);
    Dictionary.addWords(builder, wordsOffset);
    return Dictionary.endDictionary(builder);
  }

  public static void startDictionary(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addWords(FlatBufferBuilder builder, int wordsOffset) { builder.addOffset(0, wordsOffset, 0); }
  public static int createWordsVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startWordsVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endDictionary(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishDictionaryBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
}

