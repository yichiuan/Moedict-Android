#ifndef TEXT_UTILS_H
#define TEXT_UTILS_H

#include <string>

#include "flatbuffers/flatbuffers.h"
#include "u16string_generated.h"

using std::string;
using std::u16string;

using flatbuffers::FlatBufferBuilder;
using flatbuffers::Vector;
using flatbuffers::Offset;

using moe::UTF16String;

namespace moe { namespace utils {

string remove_tag(const string& source, const string& tag);

u16string convert_to_utf16(const string& source);

Offset<Vector<uint16_t>> create_u16(FlatBufferBuilder& builder, const string& source);

Offset<UTF16String> create_UTF16String(FlatBufferBuilder& builder, const string& source);

// convert ex."%3D%u7E23%u540D" to u8"人名"
const string convert_u16char_to_u8(const string& source);

}}
#endif // TEXT_UTILS_H
