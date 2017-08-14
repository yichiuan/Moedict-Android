#include <iostream>
#include <fstream>

#include "moe_generated.h"
#include "moe_converter.h"
#include "taiwan_converter.h"
#include "hakka_converter.h"
#include "moe_radical_converter.h"
#include "moe_categories_converter.h"

using namespace std;
using namespace moe;
using namespace flatbuffers;

int main(int argc, char *argv[])
{
    if (argc != 4) {
        cerr << "Need an input file and an output path.";
        return EXIT_FAILURE;
    }

    const string type(argv[1]);
    const string input_file{argv[2]};
    const string output_path{argv[3]};

    try {
        if (type == "moe") {
            MoeConverter converter;
            converter.convert(input_file, output_path);
        } else if (type == "taiwan") {
            taiwan::TaiwanConverter converter;
            converter.convert(input_file, output_path);
        } else if (type == "hakka") {
            hakka::HakkaConverter converter;
            converter.convert(input_file, output_path);
        } else if (type == "radical") {
            RadicalConverter converter;
            converter.convert(input_file, output_path);
        } else if (type == "category") {
            CategoryConverter converter;
            converter.convert(input_file, output_path);
        } else {
            cerr << "Error type.";
            return EXIT_FAILURE;
        }

    } catch (const exception& e) {
        cerr << input_file << " : " << e.what();
        return EXIT_FAILURE;
    }

    return 0;
}


