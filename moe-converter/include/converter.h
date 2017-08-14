#ifndef MOE_CONVERTER_H
#define MOE_CONVERTER_H

#include <string>
#include "flatbuffers/flatbuffers.h"

namespace moe {

using std::string;
using flatbuffers::FlatBufferBuilder;
using flatbuffers::Offset;
using flatbuffers::String;
using flatbuffers::Vector;

using FlatString = Offset<String>;
using FlatU16String = Offset<Vector<uint16_t>>;

class Converter {

public:
    static const unsigned BUFFER_SIZE = 8192;

    virtual void convert(const string& input, const string& output) = 0;

    void save(FlatBufferBuilder& builder, const string& output);

    virtual ~Converter() {}
};

}
#endif // MOE_CONVERTER_H
