cmake_minimum_required(VERSION 3.7)

set(CMAKE_C_COMPILER clang-5.0)
set(CMAKE_CXX_COMPILER clang++-5.0)

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

project(moe-converter)

# compile flatbuffers schema
set(FLATBUFFERS_FLATC flatc)

set(FBS_DIR ${CMAKE_CURRENT_SOURCE_DIR}/fbs)

file(GLOB FBS_FILES ${FBS_DIR}/*fbs)

set(FBS_GENERATED_DIR ${CMAKE_CURRENT_BINARY_DIR}/generated)

set(GENERATED_FILES "")

function(compile_flatbuffers_schema_to_cpp FBS_FILE)

  get_filename_component(FBS_FILE_NEME ${FBS_FILE} NAME)
  string(REGEX REPLACE "\\.fbs$" "_generated.h" GEN_HEADER_FILE ${FBS_FILE_NEME})

  set(FBS_GENERATED_FILE ${FBS_GENERATED_DIR}/${GEN_HEADER_FILE})

  add_custom_command(
    OUTPUT ${FBS_GENERATED_FILE}
    COMMAND ${FLATBUFFERS_FLATC} -c -j -o ${FBS_GENERATED_DIR} ${FBS_FILE}
    COMMENT "Generate flatbuffers files")

    SET(GENERATED_FILES ${GENERATED_FILES} ${FBS_GENERATED_FILE} PARENT_SCOPE)
endfunction()

foreach(item ${FBS_FILES})
  compile_flatbuffers_schema_to_cpp(${item})
endforeach()

message("GENERATED_FILES: ${GENERATED_FILES}")

# include
set(INCLUDE_DIRS include)

# third-party
set(INCLUDE_DIRS ${INCLUDE_DIRS} third-party/flatbuffers-1.8.0 third-party/json-2.1.1)

set(INCLUDE_DIRS ${INCLUDE_DIRS} ${FBS_GENERATED_DIR})

include_directories(${INCLUDE_DIRS})

add_executable(${PROJECT_NAME} ${GENERATED_FILES} ${FBS_FILES}
    src/main.cpp src/converter.cpp src/taiwan_converter.cpp src/moe_converter.cpp
    src/hakka_converter.cpp src/moe_radical_converter.cpp src/moe_categories_converter.cpp
    src/index_converter.cpp src/text_utils.cpp)
target_link_libraries(${PROJECT_NAME} -static-libgcc -static-libstdc++)

# install(TARGETS ${PROJECT_NAME} DESTINATION ${CMAKE_CURRENT_SOURCE_DIR}/bin/${CMAKE_LIBRARY_ARCHITECTURE})
install(TARGETS ${PROJECT_NAME} DESTINATION ${CMAKE_CURRENT_SOURCE_DIR}/bin/)
