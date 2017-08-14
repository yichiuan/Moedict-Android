#include "moe_categories_converter.h"
#include <fstream>
#include "text_utils.h"

#include <codecvt>

using namespace std;
using namespace nlohmann;
using namespace flatbuffers;

namespace moe {

void CategoryConverter::convert(const string& input, const string& output) {
    ifstream i(input);
    json j;
    i >> j;

    FlatBufferBuilder builder(BUFFER_SIZE);

    vector<Offset<Category>> categories;

    for (json::iterator it = j.begin(); it != j.end(); ++it) {
        const string key = it.key();
        const auto value = it.value();
        categories.emplace_back(produce_category(value, key, builder));
    }

    auto categories_vector = builder.CreateVectorOfSortedTables(&categories);
    builder.Finish(CreateCategories(builder, categories_vector));

    save(builder, output);
}

Offset<Category> CategoryConverter::produce_category(const json& category, const string& index,
                                                  FlatBufferBuilder& builder) {

    FlatString category_index = builder.CreateString(utils::convert_u16char_to_u8(index));
    vector<Offset<UTF16String>> terms;

    for (const auto item : category) {
        terms.push_back(utils::create_UTF16String(builder, item));
    }

    auto terms_offset = builder.CreateVector(terms);

    CategoryBuilder category_builder{builder};
    category_builder.add_index(category_index);
    category_builder.add_terms(terms_offset);

    return category_builder.Finish();
}

}
