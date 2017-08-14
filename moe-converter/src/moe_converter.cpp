#include "moe_converter.h"
#include <fstream>
#include "text_utils.h"

using namespace std;
using namespace nlohmann;
using namespace flatbuffers;

namespace moe {

void MoeConverter::convert(const string& input, const string& output) {
    std::ifstream i(input);
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

Offset<Word> MoeConverter::produce_word(const json& word, FlatBufferBuilder& builder) {

    FlatString word_index;
    FlatU16String word_title;

    FlatString word_radical;

    unique_ptr<vector<Offset<Heteronym>>> word_heteronyms;

    uint8_t word_stroke_count = 0;
    uint8_t word_non_radical_stroke_count = 0;

    Offset<Translation> word_translation;

    FlatString word_english;

    for (auto it = word.begin(); it != word.end(); ++it) {
        const string key = it.key();

        if (key == "t") {
            string title = it.value();

            word_index = builder.CreateString(utils::remove_tag(title, "`~"));
            word_title = utils::create_u16(builder, title);

        } else if (key == "r") {
            word_radical = builder.CreateString(utils::remove_tag(it.value(), "`~"));
        } else if (key == "c") {
            word_stroke_count = it.value();
        } else if (key == "n") {
            word_non_radical_stroke_count = it.value();
        } else if (key == "h") {
            word_heteronyms = produce_heteronyms(it.value(), builder);
        } else if (key == "translation") {
            const auto translation_json = it.value();

            vector<FlatString> englishs;
            vector<FlatString> deutschs;
            vector<FlatString> francaises;

            for (auto it = translation_json.begin(); it != translation_json.end(); ++it) {
                const string key = it.key();

                if (key == "English") {
                    for (auto english : it.value()) {
                        englishs.push_back(builder.CreateString(utils::remove_tag(english, "`~")));
                    }
                } else if (key == "Deutsch") {
                    for (const string deutsch : it.value()) {
                        deutschs.push_back(builder.CreateString(deutsch));
                    }
                } else if (key == "francais") {
                    for (const string francais : it.value()) {
                        francaises.push_back(builder.CreateString(francais));
                    }
                } else {
                    throw runtime_error(string{"translation field, key: " + key + " is not handled"});
                }
            }

            Offset<Vector<Offset<String>>> new_englishs;
            if (!englishs.empty()) new_englishs = builder.CreateVector(englishs);

            Offset<Vector<Offset<String>>> new_deutschs;
            if (!deutschs.empty()) new_deutschs = builder.CreateVector(deutschs);

            Offset<Vector<Offset<String>>> new_francaises;
            if (!francaises.empty()) new_francaises = builder.CreateVector(francaises);

            TranslationBuilder translation_builder{builder};
            translation_builder.add_English(new_englishs);
            translation_builder.add_Deutsch(new_deutschs);
            translation_builder.add_francais(new_francaises);

            word_translation = translation_builder.Finish();
        } else if (key == "english") {
            word_english = builder.CreateString(it.value().get<string>());
        } else if (key == "English" || key == "Deutsch" || key == "francais") {
            // pass
        } else {
            throw runtime_error(string{"word field, key: " + key + " is not handled"});
        }
    }

    if (!word_english.IsNull()) {
        if (word_translation.IsNull())
        {
            vector<FlatString> englishs {word_english};
            Offset<Vector<Offset<String>>> new_englishs;
            new_englishs = builder.CreateVector(englishs);

            Offset<Vector<Offset<String>>> empty;

            TranslationBuilder translation_builder{builder};
            translation_builder.add_English(new_englishs);
            translation_builder.add_Deutsch(empty);
            translation_builder.add_francais(empty);

            word_translation = translation_builder.Finish();

        } else {
            throw runtime_error(string{"keys include both translation and english"});
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

    if (word_non_radical_stroke_count) {
        word_builder.add_non_radical_stroke_count(word_non_radical_stroke_count);
    }

    word_builder.add_heteronyms(heteronyms);
    word_builder.add_translation(word_translation);

    return word_builder.Finish();
}

unique_ptr<vector<Offset<Heteronym>>>
MoeConverter::produce_heteronyms(const json& heteronyms, FlatBufferBuilder& builder) {

    auto word_heteronyms = make_unique<vector<Offset<Heteronym>>>();

    for (auto heteronym : heteronyms) {

        FlatString bopomofo;
        FlatString pinyin;
        FlatString audio_id;

        unique_ptr<vector<Offset<Definition>>> definitions;

        for (auto it = heteronym.begin(); it != heteronym.end(); ++it) {
            const string key = it.key();

            if (key == "b") {
                bopomofo = builder.CreateString(it.value().get<string>());
            } else if (key == "p") {
                pinyin = builder.CreateString(it.value().get<string>());
            } else if (key == "=") {
                audio_id = builder.CreateString(it.value().get<string>());
            } else if (key == "d") {
                definitions = produce_definitions(it.value(), builder);
            } else {
                throw runtime_error(string {"in heteronyms, key: " + key + " is not handled"});
            }
        }

        Offset<Vector<Offset<Definition>>> new_definitions;
        if (definitions)
            new_definitions = builder.CreateVector(*definitions);

        HeteronymBuilder heteronym_builder{builder};
        heteronym_builder.add_bopomofo(bopomofo);
        heteronym_builder.add_pinyin(pinyin);
        heteronym_builder.add_audio_id(audio_id);
        heteronym_builder.add_definitions(new_definitions);

        auto new_heteronym = heteronym_builder.Finish();
        word_heteronyms->push_back(new_heteronym);
    }

    return word_heteronyms;
}

unique_ptr<vector<Offset<Definition>>>
MoeConverter::produce_definitions(const json& definitions, FlatBufferBuilder& builder) {

    auto word_definitions = make_unique<vector<Offset<Definition>>>();

    for (auto definition : definitions) {

        FlatU16String def;

        FlatString type;
        FlatU16String synonyms;
        FlatU16String antonyms;

        vector<Offset<UTF16String>> quotes;
        vector<Offset<UTF16String>> examples;
        vector<Offset<UTF16String>> links;

        for (auto it = definition.begin(); it != definition.end(); ++it) {
            const string key = it.key();

            if (key == "f") {
                def = utils::create_u16(builder, it.value());
            } else if (key == "type") {
                type = builder.CreateString(utils::remove_tag(it.value(), "`~"));
            } else if (key == "s") {
                synonyms = utils::create_u16(builder, it.value());
            } else if (key == "a") {
                antonyms = utils::create_u16(builder, it.value());
            } else if (key == "q") {
                for (auto quote : it.value()) {
                    quotes.push_back(utils::create_UTF16String(builder, quote));
                }
            } else if (key == "e") {
                for (auto example : it.value()) {
                    examples.push_back(utils::create_UTF16String(builder, example));
                }
            } else if (key == "l") {
                for (auto link : it.value()) {
                    links.push_back(utils::create_UTF16String(builder, link));
                }
            } else {
                throw runtime_error(string{"in definition, key: " + key + " is not handled"});
            }
        }

        Offset<Vector<Offset<UTF16String>>> new_quotes;
        if (!quotes.empty()) new_quotes = builder.CreateVector(quotes);

        Offset<Vector<Offset<UTF16String>>> new_examples;
        if (!examples.empty()) new_examples = builder.CreateVector(examples);

        Offset<Vector<Offset<UTF16String>>> new_links;
        if (!links.empty()) new_links = builder.CreateVector(links);

        DefinitionBuilder definition_builder{builder};
        definition_builder.add_def(def);
        definition_builder.add_type(type);
        definition_builder.add_synonyms(synonyms);
        definition_builder.add_antonyms(antonyms);
        definition_builder.add_quotes(new_quotes);
        definition_builder.add_examples(new_examples);
        definition_builder.add_links(new_links);

        auto new_definition = definition_builder.Finish();
        word_definitions->push_back(new_definition);
    }

    return word_definitions;
}
}
