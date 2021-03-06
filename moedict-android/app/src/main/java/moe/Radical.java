// automatically generated by the FlatBuffers compiler, do not modify

package moe;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class Radical extends Table {
  public static Radical getRootAsRadical(ByteBuffer _bb) { return getRootAsRadical(_bb, new Radical()); }
  public static Radical getRootAsRadical(ByteBuffer _bb, Radical obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; }
  public Radical __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public String index() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer indexAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public Words stroke(int j) { return stroke(new Words(), j); }
  public Words stroke(Words obj, int j) { int o = __offset(6); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int strokeLength() { int o = __offset(6); return o != 0 ? __vector_len(o) : 0; }

  public static int createRadical(FlatBufferBuilder builder,
      int indexOffset,
      int strokeOffset) {
    builder.startObject(2);
    Radical.addStroke(builder, strokeOffset);
    Radical.addIndex(builder, indexOffset);
    return Radical.endRadical(builder);
  }

  public static void startRadical(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addIndex(FlatBufferBuilder builder, int indexOffset) { builder.addOffset(0, indexOffset, 0); }
  public static void addStroke(FlatBufferBuilder builder, int strokeOffset) { builder.addOffset(1, strokeOffset, 0); }
  public static int createStrokeVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startStrokeVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endRadical(FlatBufferBuilder builder) {
    int o = builder.endObject();
    builder.required(o, 4);  // index
    return o;
  }

  @Override
  protected int keysCompare(Integer o1, Integer o2, ByteBuffer _bb) { return compareStrings(__offset(4, o1, _bb), __offset(4, o2, _bb), _bb); }

  public static Radical __lookup_by_key(int vectorLocation, String key, ByteBuffer bb) {
    byte[] byteKey = key.getBytes(Table.UTF8_CHARSET.get());
    int span = bb.getInt(vectorLocation - 4);
    int start = 0;
    while (span != 0) {
      int middle = span / 2;
      int tableOffset = __indirect(vectorLocation + 4 * (start + middle), bb);
      int comp = compareStrings(__offset(4, bb.capacity() - tableOffset, bb), byteKey, bb);
      if (comp > 0) {
        span = middle;
      } else if (comp < 0) {
        middle++;
        start += middle;
        span -= middle;
      } else {
        return new Radical().__assign(tableOffset, bb);
      }
    }
    return null;
  }
}

