#ifndef MOE_CATEGORIES_CONVERTER_H
#define MOE_CATEGORIES_CONVERTER_H

#include "converter.h"

#include <string>
#include <vector>

#include "json/json.hpp"
#include "flatbuffers/flatbuffers.h"
#include "moe_categories_generated.h"

namespace moe {

using std::vector;
using std::unique_ptr;

using nlohmann::json;
using flatbuffers::FlatBufferBuilder;
using flatbuffers::Vector;
using flatbuffers::Offset;

class CategoryConverter : public Converter {

public:
    virtual void convert(const string& input, const string& output) override;

private:

    Offset<Category> produce_category(const json& radical, const string& index,
                                    FlatBufferBuilder& builder);
};

}

#endif // MOE_CATEGORIES_CONVERTER_H
