// automatically generated by the FlatBuffers compiler, do not modify

package moe;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class Translation extends Table {
  public static Translation getRootAsTranslation(ByteBuffer _bb) { return getRootAsTranslation(_bb, new Translation()); }
  public static Translation getRootAsTranslation(ByteBuffer _bb, Translation obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; }
  public Translation __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public String English(int j) { int o = __offset(4); return o != 0 ? __string(__vector(o) + j * 4) : null; }
  public int EnglishLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }
  public String Deutsch(int j) { int o = __offset(6); return o != 0 ? __string(__vector(o) + j * 4) : null; }
  public int DeutschLength() { int o = __offset(6); return o != 0 ? __vector_len(o) : 0; }
  public String francais(int j) { int o = __offset(8); return o != 0 ? __string(__vector(o) + j * 4) : null; }
  public int francaisLength() { int o = __offset(8); return o != 0 ? __vector_len(o) : 0; }

  public static int createTranslation(FlatBufferBuilder builder,
      int EnglishOffset,
      int DeutschOffset,
      int francaisOffset) {
    builder.startObject(3);
    Translation.addFrancais(builder, francaisOffset);
    Translation.addDeutsch(builder, DeutschOffset);
    Translation.addEnglish(builder, EnglishOffset);
    return Translation.endTranslation(builder);
  }

  public static void startTranslation(FlatBufferBuilder builder) { builder.startObject(3); }
  public static void addEnglish(FlatBufferBuilder builder, int EnglishOffset) { builder.addOffset(0, EnglishOffset, 0); }
  public static int createEnglishVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startEnglishVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addDeutsch(FlatBufferBuilder builder, int DeutschOffset) { builder.addOffset(1, DeutschOffset, 0); }
  public static int createDeutschVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startDeutschVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addFrancais(FlatBufferBuilder builder, int francaisOffset) { builder.addOffset(2, francaisOffset, 0); }
  public static int createFrancaisVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startFrancaisVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endTranslation(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}

