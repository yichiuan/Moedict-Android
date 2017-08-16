#ifndef MOE_INDEX_CONVERTER_H
#define MOE_INDEX_CONVERTER_H

#include "converter.h"

#include <string>
#include <vector>

#include "json/json.hpp"
#include "flatbuffers/flatbuffers.h"
#include "index_generated.h"

namespace moe {

using std::vector;
using std::unique_ptr;

using nlohmann::json;
using flatbuffers::FlatBufferBuilder;
using flatbuffers::Vector;
using flatbuffers::Offset;

class IndexConverter : public Converter {

public:
    virtual void convert(const string& input, const string& output) override;
};

}

#endif // INDEX_CONVERTER_H
