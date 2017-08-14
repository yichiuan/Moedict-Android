#ifndef MOE_TAIWAN_CONVERTER_H
#define MOE_TAIWAN_CONVERTER_H

#include "converter.h"
#include "json/json.hpp"
#include "taiwan_generated.h"

namespace moe { namespace taiwan {


using std::vector;
using std::unique_ptr;

using nlohmann::json;
using flatbuffers::Offset;

using moe::taiwan::Word;
using moe::taiwan::Heteronym;
using moe::taiwan::Definition;

class TaiwanConverter : public Converter
{
public:
    virtual void convert(const string& input, const string& output) override;

private:
    Offset<Word> produce_word(const json& word, FlatBufferBuilder& builder);

    unique_ptr<vector<Offset<Heteronym>>>
    produce_heteronyms(const json& heteronyms, FlatBufferBuilder& builder);

    unique_ptr<vector<Offset<Definition>>>
    produce_definitions(const json& definitions, FlatBufferBuilder& builder);
};

}}
#endif // MOE_TAIWAN_CONVERTER_H
