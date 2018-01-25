// automatically generated by the FlatBuffers compiler, do not modify

package moe;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class Definition extends Table {
  public static Definition getRootAsDefinition(ByteBuffer _bb) { return getRootAsDefinition(_bb, new Definition()); }
  public static Definition getRootAsDefinition(ByteBuffer _bb, Definition obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; }
  public Definition __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int def(int j) { int o = __offset(4); return o != 0 ? bb.getShort(__vector(o) + j * 2) & 0xFFFF : 0; }
  public int defLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }
  public ByteBuffer defAsByteBuffer() { return __vector_as_bytebuffer(4, 2); }
  public String type() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer typeAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public UTF16String quotes(int j) { return quotes(new UTF16String(), j); }
  public UTF16String quotes(UTF16String obj, int j) { int o = __offset(8); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int quotesLength() { int o = __offset(8); return o != 0 ? __vector_len(o) : 0; }
  public UTF16String examples(int j) { return examples(new UTF16String(), j); }
  public UTF16String examples(UTF16String obj, int j) { int o = __offset(10); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int examplesLength() { int o = __offset(10); return o != 0 ? __vector_len(o) : 0; }
  public UTF16String links(int j) { return links(new UTF16String(), j); }
  public UTF16String links(UTF16String obj, int j) { int o = __offset(12); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int linksLength() { int o = __offset(12); return o != 0 ? __vector_len(o) : 0; }
  public int synonyms(int j) { int o = __offset(14); return o != 0 ? bb.getShort(__vector(o) + j * 2) & 0xFFFF : 0; }
  public int synonymsLength() { int o = __offset(14); return o != 0 ? __vector_len(o) : 0; }
  public ByteBuffer synonymsAsByteBuffer() { return __vector_as_bytebuffer(14, 2); }
  public int antonyms(int j) { int o = __offset(16); return o != 0 ? bb.getShort(__vector(o) + j * 2) & 0xFFFF : 0; }
  public int antonymsLength() { int o = __offset(16); return o != 0 ? __vector_len(o) : 0; }
  public ByteBuffer antonymsAsByteBuffer() { return __vector_as_bytebuffer(16, 2); }

  public static int createDefinition(FlatBufferBuilder builder,
      int defOffset,
      int typeOffset,
      int quotesOffset,
      int examplesOffset,
      int linksOffset,
      int synonymsOffset,
      int antonymsOffset) {
    builder.startObject(7);
    Definition.addAntonyms(builder, antonymsOffset);
    Definition.addSynonyms(builder, synonymsOffset);
    Definition.addLinks(builder, linksOffset);
    Definition.addExamples(builder, examplesOffset);
    Definition.addQuotes(builder, quotesOffset);
    Definition.addType(builder, typeOffset);
    Definition.addDef(builder, defOffset);
    return Definition.endDefinition(builder);
  }

  public static void startDefinition(FlatBufferBuilder builder) { builder.startObject(7); }
  public static void addDef(FlatBufferBuilder builder, int defOffset) { builder.addOffset(0, defOffset, 0); }
  public static int createDefVector(FlatBufferBuilder builder, short[] data) { builder.startVector(2, data.length, 2); for (int i = data.length - 1; i >= 0; i--) builder.addShort(data[i]); return builder.endVector(); }
  public static void startDefVector(FlatBufferBuilder builder, int numElems) { builder.startVector(2, numElems, 2); }
  public static void addType(FlatBufferBuilder builder, int typeOffset) { builder.addOffset(1, typeOffset, 0); }
  public static void addQuotes(FlatBufferBuilder builder, int quotesOffset) { builder.addOffset(2, quotesOffset, 0); }
  public static int createQuotesVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startQuotesVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addExamples(FlatBufferBuilder builder, int examplesOffset) { builder.addOffset(3, examplesOffset, 0); }
  public static int createExamplesVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startExamplesVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addLinks(FlatBufferBuilder builder, int linksOffset) { builder.addOffset(4, linksOffset, 0); }
  public static int createLinksVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startLinksVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addSynonyms(FlatBufferBuilder builder, int synonymsOffset) { builder.addOffset(5, synonymsOffset, 0); }
  public static int createSynonymsVector(FlatBufferBuilder builder, short[] data) { builder.startVector(2, data.length, 2); for (int i = data.length - 1; i >= 0; i--) builder.addShort(data[i]); return builder.endVector(); }
  public static void startSynonymsVector(FlatBufferBuilder builder, int numElems) { builder.startVector(2, numElems, 2); }
  public static void addAntonyms(FlatBufferBuilder builder, int antonymsOffset) { builder.addOffset(6, antonymsOffset, 0); }
  public static int createAntonymsVector(FlatBufferBuilder builder, short[] data) { builder.startVector(2, data.length, 2); for (int i = data.length - 1; i >= 0; i--) builder.addShort(data[i]); return builder.endVector(); }
  public static void startAntonymsVector(FlatBufferBuilder builder, int numElems) { builder.startVector(2, numElems, 2); }
  public static int endDefinition(FlatBufferBuilder builder) {
    int o = builder.endObject();
    builder.required(o, 4);  // def
    return o;
  }
}

