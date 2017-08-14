// automatically generated by the FlatBuffers compiler, do not modify

package moe;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class Heteronym extends Table {
  public static Heteronym getRootAsHeteronym(ByteBuffer _bb) { return getRootAsHeteronym(_bb, new Heteronym()); }
  public static Heteronym getRootAsHeteronym(ByteBuffer _bb, Heteronym obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; }
  public Heteronym __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public String bopomofo() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer bopomofoAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public String pinyin() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer pinyinAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public String audioId() { int o = __offset(8); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer audioIdAsByteBuffer() { return __vector_as_bytebuffer(8, 1); }
  public Definition definitions(int j) { return definitions(new Definition(), j); }
  public Definition definitions(Definition obj, int j) { int o = __offset(10); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int definitionsLength() { int o = __offset(10); return o != 0 ? __vector_len(o) : 0; }

  public static int createHeteronym(FlatBufferBuilder builder,
      int bopomofoOffset,
      int pinyinOffset,
      int audio_idOffset,
      int definitionsOffset) {
    builder.startObject(4);
    Heteronym.addDefinitions(builder, definitionsOffset);
    Heteronym.addAudioId(builder, audio_idOffset);
    Heteronym.addPinyin(builder, pinyinOffset);
    Heteronym.addBopomofo(builder, bopomofoOffset);
    return Heteronym.endHeteronym(builder);
  }

  public static void startHeteronym(FlatBufferBuilder builder) { builder.startObject(4); }
  public static void addBopomofo(FlatBufferBuilder builder, int bopomofoOffset) { builder.addOffset(0, bopomofoOffset, 0); }
  public static void addPinyin(FlatBufferBuilder builder, int pinyinOffset) { builder.addOffset(1, pinyinOffset, 0); }
  public static void addAudioId(FlatBufferBuilder builder, int audioIdOffset) { builder.addOffset(2, audioIdOffset, 0); }
  public static void addDefinitions(FlatBufferBuilder builder, int definitionsOffset) { builder.addOffset(3, definitionsOffset, 0); }
  public static int createDefinitionsVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startDefinitionsVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endHeteronym(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}

