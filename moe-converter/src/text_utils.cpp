#include "text_utils.h"

#include <codecvt>
#include <locale>
#include <sstream>

namespace moe { namespace utils {

string remove_tag(const string& source, const string& tag) {

    string result;
    string::size_type pos = 0;

    while ((pos = source.find_first_not_of(tag, pos)) != string::npos) {
        result.push_back(source[pos]);
        ++pos;
    }
    return result;
}

u16string convert_to_utf16(const string& source) {
    return std::wstring_convert<
        std::codecvt_utf8_utf16<char16_t>, char16_t>{}.from_bytes(source);
}

Offset<Vector<uint16_t>> create_u16(FlatBufferBuilder& builder, const string& source) {
    u16string u16_str = utils::convert_to_utf16(source);
    return builder.CreateVector((uint16_t*)u16_str.data(), u16_str.length());
}

Offset<UTF16String>
create_UTF16String(FlatBufferBuilder& builder, const string& source) {
    u16string u16_string = utils::convert_to_utf16(source);
    Offset<Vector<uint16_t>> u16_string_offset
            = builder.CreateVector((uint16_t*)u16_string.data(), u16_string.length());
    return CreateUTF16String(builder, u16_string_offset);
}


const string convert_u16char_to_u8(const string& source) {

    std::vector<char16_t> uchars;
    string::size_type pos = 0;
    for (; pos != source.length(); ++pos) {
        if (source[pos] == 'u') {
            std::stringstream stream;
            stream << std::hex << source.substr(pos+1, 4);
            u_int16_t num = 0;
            stream >> num;
            uchars.push_back(num);
        }
    }

    std::u16string u16{uchars.data(), uchars.size()};

    return std::wstring_convert<
        std::codecvt_utf8_utf16<char16_t>, char16_t>{}.to_bytes(u16);
}

}}
