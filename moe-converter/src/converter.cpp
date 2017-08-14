#include "converter.h"
#include <fstream>

namespace moe {

void Converter::save(FlatBufferBuilder& builder, const string& output) {
    uint8_t* buffer_point = builder.GetBufferPointer();
    auto size = builder.GetSize();

    std::ofstream of(output, std::ios::binary);
    of.write(reinterpret_cast<const char*>(buffer_point), size);
    of.close();
}

}
