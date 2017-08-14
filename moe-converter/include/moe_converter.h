#ifndef MOE_MOECONVERTER_H
#define MOE_MOECONVERTER_H

#include "converter.h"

#include <string>
#include <vector>

#include "json/json.hpp"
#include "flatbuffers/flatbuffers.h"
#include "moe_generated.h"

namespace moe {

using std::vector;
using std::unique_ptr;

using nlohmann::json;
using flatbuffers::FlatBufferBuilder;
using flatbuffers::Vector;
using flatbuffers::Offset;

class MoeConverter : public Converter {

public:
    virtual void convert(const string& input, const string& output) override;

private:

    Offset<Word> produce_word(const json& word, FlatBufferBuilder& builder);

    unique_ptr<vector<Offset<Heteronym>>>
    produce_heteronyms(const json& heteronyms, FlatBufferBuilder& builder);

    unique_ptr<vector<Offset<Definition>>>
    produce_definitions(const json& definitions, FlatBufferBuilder& builder);
};

}
#endif // MOE_MOECONVERTER_H
