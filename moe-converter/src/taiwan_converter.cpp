#include "taiwan_converter.h"
#include <fstream>
#include "text_utils.h"

namespace moe { namespace taiwan {

using namespace std;
using namespace nlohmann;
using namespace flatbuffers;

void TaiwanConverter::convert(const string& input, const string& output) {
    ifstream i(input);
    json j;
    i >> j;

    FlatBufferBuilder builder(BUFFER_SIZE);

    vector<Offset<Word>> words;

    for (json::iterator it = j.begin(); it != j.end(); ++it) {
        string key = it.key();
        auto value = it.value();
        words.emplace_back(produce_word(value, builder));
    }

    auto words_vector = builder.CreateVectorOfSortedTables(&words);

    auto dictionary = CreateDictionary(builder, words_vector);
    builder.Finish(dictionary);

    save(builder, output);
}

Offset<Word> TaiwanConverter::produce_word(const json& word, FlatBufferBuilder& builder) {
    FlatString word_index;
    Offset<Vector<uint16_t>> word_title;

    FlatString word_radical;

    unique_ptr<vector<Offset<Heteronym>>> word_heteronyms;

    uint8_t word_stroke_count = 0;
    uint8_t word_non_radical_stroke_count = 0;

    for (auto it = word.begin(); it != word.end(); ++it) {
        const string key = it.key();

        if (key == "t") {
            string title = it.value();
            word_index = builder.CreateString(utils::remove_tag(title, "`~"));
            word_title = utils::create_u16(builder, title);

        } else if (key == "r") {
            word_radical = builder.CreateString(utils::remove_tag(it.value(), "`~"));
        } else if (key == "c") {
            if (!it.value().is_null()) word_stroke_count = it.value();
        } else if (key == "n") {
            if (!it.value().is_null()) word_non_radical_stroke_count = it.value();
        } else if (key == "h") {
            word_heteronyms = produce_heteronyms(it.value(), builder);
        } else {
            throw runtime_error(string{"word field, key: " + key + " is not handled"});
        }
    }

    Offset<Vector<Offset<Heteronym>>> heteronyms;
    if (word_heteronyms)
        heteronyms = builder.CreateVector(*word_heteronyms);

    WordBuilder word_builder{builder};
    word_builder.add_index(word_index);
    word_builder.add_title(word_title);
    word_builder.add_radical(word_radical);

    if (word_stroke_count) word_builder.add_stroke_count(word_stroke_count);

    if (word_non_radical_stroke_count)
        word_builder.add_non_radical_stroke_count(word_non_radical_stroke_count);

    word_builder.add_heteronyms(heteronyms);

    return word_builder.Finish();
}

unique_ptr<vector<Offset<Heteronym>>>
TaiwanConverter::produce_heteronyms(const json& heteronyms, FlatBufferBuilder& builder) {

    auto word_heteronyms = make_unique<vector<Offset<Heteronym>>>();

    for (auto heteronym : heteronyms) {

        FlatString id;
        FlatString audio_id;
        FlatString reading;
        FlatString trs;

        FlatU16String synonyms;
        FlatU16String antonyms;

        unique_ptr<vector<Offset<Definition>>> definitions;

        for (auto it = heteronym.begin(); it != heteronym.end(); ++it) {
            const string key = it.key();

            if (key == "_") {
                id = builder.CreateString(it.value().get<string>());
            } else if (key == "=") {
                audio_id = builder.CreateString(it.value().get<string>());
            } else if (key == "reading") {
                reading = builder.CreateString(utils::remove_tag(it.value(), "`~"));
            } else if (key == "T") {
                trs = builder.CreateString(it.value().get<string>());
            } else if (key == "s") {
                synonyms = utils::create_u16(builder, it.value());
            } else if (key == "a") {
                antonyms = utils::create_u16(builder, it.value());
            } else if (key == "d") {
                definitions = produce_definitions(it.value(), builder);
            } else if (key == "D") {
                // Now, don't handle
            } else {
                throw runtime_error(string{"in heteronyms, key: " + key + " is not handled"});
            }
        }

        Offset<Vector<Offset<Definition>>> new_definitions;
        if (definitions)
            new_definitions = builder.CreateVector(*definitions);

        HeteronymBuilder heteronym_builder{builder};
        heteronym_builder.add_id(id);
        heteronym_builder.add_audio_id(audio_id);
        heteronym_builder.add_reading(reading);
        heteronym_builder.add_trs(trs);
        heteronym_builder.add_synonyms(synonyms);
        heteronym_builder.add_antonyms(antonyms);
        heteronym_builder.add_definitions(new_definitions);

        auto new_heteronym = heteronym_builder.Finish();
        word_heteronyms->push_back(new_heteronym);
    }

    return word_heteronyms;
}

unique_ptr<vector<Offset<Definition>>>
TaiwanConverter::produce_definitions(const json& definitions, FlatBufferBuilder& builder) {

    auto word_definitions = make_unique<vector<Offset<Definition>>>();

    for (auto definition : definitions) {

        FlatU16String def;
        FlatString type;
        vector<Offset<UTF16String>> examples;

        for (auto it = definition.begin(); it != definition.end(); ++it) {
            const string key = it.key();

            if (key == "f") {
                def = utils::create_u16(builder, it.value());
            } else if (key == "type") {
                type = builder.CreateString(utils::remove_tag(it.value(), "`~"));
            } else if (key == "e") {
                for (auto example : it.value()) {
                    examples.push_back(utils::create_UTF16String(builder, example));
                }
            } else {
                throw runtime_error(string{"in definition, key: " + key + " is not handled"});
            }
        }

        Offset<Vector<Offset<UTF16String>>> new_examples;
        if (!examples.empty()) new_examples = builder.CreateVector(examples);

        DefinitionBuilder definition_builder{builder};
        definition_builder.add_def(def);
        definition_builder.add_type(type);
        definition_builder.add_examples(new_examples);

        auto new_definition = definition_builder.Finish();
        word_definitions->push_back(new_definition);
    }

    return word_definitions;
}
} }
