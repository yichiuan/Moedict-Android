#include "moe_radical_converter.h"
#include <fstream>
#include "text_utils.h"

using namespace std;
using namespace nlohmann;
using namespace flatbuffers;

namespace moe {

void read_radical(uint8_t*);

void RadicalConverter::convert(const string& input, const string& output) {
    ifstream i(input);
    json j;
    i >> j;

    FlatBufferBuilder builder(BUFFER_SIZE);

    vector<Offset<Radical>> radicals;

    for (json::iterator it = j.begin(); it != j.end(); ++it) {
        const string key = it.key();
        const auto value = it.value();
        radicals.emplace_back(produce_radical(value, key, builder));
    }

    auto radicals_vector = builder.CreateVectorOfSortedTables(&radicals);
    builder.Finish(CreateRadicalTable(builder, radicals_vector));

//    read_radical(builder.GetBufferPointer());

    save(builder, output);
}

Offset<Radical> RadicalConverter::produce_radical(const json& radical, const string& index,
                                                  FlatBufferBuilder& builder) {

    FlatString radical_index;

    vector<Offset<Words>> stroke_words;

    bool is_first_array_empty = radical[0].empty();

    if (is_first_array_empty && index == "@") {
        radical_index = builder.CreateString(index);
    } else {
        radical_index = builder.CreateString(radical[0][0].get<string>());
    }

    for (const auto item : radical) {

        if (item.empty()) {
            u_int16_t* null {};
            Offset<Vector<uint16_t>> null_offset = builder.CreateVector(null, 0);
            Offset<Words> words = CreateWords(builder, null_offset);
            stroke_words.emplace_back(words);

        } else if (item.is_array()) {

            vector<uint16_t> words;

            for (const auto word : item) {
                words.emplace_back((uint16_t(utils::convert_to_utf16(word)[0])));
            }
            stroke_words.emplace_back(CreateWordsDirect(builder, &words));

        } else {
            throw runtime_error(string{"word : " + index + " is not array."});
        }
    }

    auto stroke = builder.CreateVector(stroke_words);

    RadicalBuilder radical_builder{builder};
    radical_builder.add_index(radical_index);
    radical_builder.add_stroke(stroke);

    return radical_builder.Finish();
}

void read_radical(uint8_t* buf_point) {

    auto radical_table = GetRadicalTable(buf_point);
    auto radicals = radical_table->radicals();

    const auto radical = radicals->LookupByKey(u8"行");

    auto stroke = radical->stroke();
    size_t stroke_size = stroke->size();

    for (size_t i = 0; i < stroke_size; ++i) {
        auto words = stroke->Get(i);
        size_t words_size = words->word()->size();
    }
}

}
