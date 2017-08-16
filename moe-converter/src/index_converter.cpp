#include "index_converter.h"
#include <fstream>
#include "text_utils.h"

using namespace std;
using namespace nlohmann;
using namespace flatbuffers;

namespace moe {

void IndexConverter::convert(const string& input, const string& output)
{
    ifstream i(input);
    json j;
    i >> j;

    FlatBufferBuilder builder(BUFFER_SIZE);

    vector<Offset<String>> words;

    for (const string word : j) {
        words.emplace_back(builder.CreateString(word));
    }

    auto indexes_vector = builder.CreateVector(words);
    builder.Finish(CreateIndex(builder, indexes_vector));

    save(builder, output);
}

}
