cmake_minimum_required(VERSION 3.22)
project("virtu")

add_library(virtu SHARED vm_bridge.cpp)
target_compile_features(virtu PRIVATE cxx_std_17)
find_library(log-lib log)
target_link_libraries(virtu ${log-lib})
