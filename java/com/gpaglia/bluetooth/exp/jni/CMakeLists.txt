find_package(JNI REQUIRED)

if (JNI_FOUND)
    message (STATUS "JNI_INCLUDE_DIRS=${JNI_INCLUDE_DIRS}")
    message (STATUS "JNI_LIBRARIES=${JNI_LIBRARIES}")
endif (JNI_FOUND)

set (EXP_LIB_INCLUDE_DIRS
    ${PROJECT_SOURCE_DIR}/jniexp
)

include_directories(
    ${JNI_INCLUDE_DIRS}
    ${EXP_LIB_INCLUDE_DIRS}
    ${EXP_JNI_HEADER_PATH}
)

file(GLOB JNI_SOURCES "*.c")

set (CMAKE_SHARED_LINKER_FLAGS "-Wl,--as-needed")

add_library (jniexp SHARED ${JNI_SOURCES})
target_link_libraries(jniexp ${JNI_LIBRARIES} )

message (STATUS "lescan_VERSION_MAJOR=${lescan_VERSION_MAJOR}")
message (STATUS "lescan_VERSION_STRING=${lescan_VERSION_STRING}")
message (STATUS "CMAKE_INSTALL_LIBDIR=${CMAKE_INSTALL_LIBDIR}")

set_target_properties(
    jniexp
    PROPERTIES
    SOVERSION ${lescan_VERSION_MAJOR}
    VERSION ${lescan_VERSION_STRING}
    CXX_STANDARD 11
    COMPILE_FLAGS "-Wall -Wextra -fPIC"
)

#install(TARGETS jniexp LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR})