cmake_minimum_required(VERSION 3.1)

if(${CMAKE_VERSION} VERSION_LESS 3.15)
    cmake_policy(VERSION ${CMAKE_MAJOR_VERSION}.${CMAKE_MINOR_VERSION})
else()
    cmake_policy(VERSION 3.15)
endif()

set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED ON)

project(liblescan VERSION 1.0
                  DESCRIPTION "Java native library to Scan BLE beacons in passive mode"
                  LANGUAGES C)

# Appends the cmake/modules path to MAKE_MODULE_PATH variable.
set (CMAKE_MODULE_PATH ${CMAKE_SOURCE_DIR}/cmake/modules ${CMAKE_MODULE_PATH})

# Make a version file containing the current version from git.
include (GetGitRevisionDescription)
git_describe (VERSION "--tags")
MESSAGE(STATUS "Git version: ${VERSION}")

if ("x_${VERSION}" STREQUAL "x_GIT-NOTFOUND" OR "x_${VERSION}" STREQUAL "x_HEAD-HASH-NOTFOUND" OR "x_${VERSION}" STREQUAL "x_-128-NOTFOUND")
  message (WARNING " - Install git to compile a production liblescan!")
  set (VERSION "0.1.0-SNAPSHOT")
endif ()

message (INFO " - liblescan Version ${VERSION}")

#parse the version information into pieces.
string (REGEX REPLACE "^([0-9]+)\\..*" "\\1" VERSION_MAJOR "${VERSION}")
string (REGEX REPLACE "^[0-9]+\\.([0-9]+).*" "\\1" VERSION_MINOR "${VERSION}")
string (REGEX REPLACE "^[0-9]+\\.[0-9]+\\.([0-9]+).*" "\\1" VERSION_PATCH "${VERSION}")
string (REGEX REPLACE "^[0-9]+\\.[0-9]+\\.[0-9]+\\-([0-9]+).*" "\\1" VERSION_COMMIT "${VERSION}")
string (REGEX REPLACE "^[0-9]+\\.[0-9]+\\.[0-9]+-[0-9]+\\-(.*)" "\\1" VERSION_SHA1 "${VERSION}")
set (VERSION_SHORT "${VERSION_MAJOR}.${VERSION_MINOR}.${VERSION_PATCH}")
set (VERSION_API "${VERSION_MAJOR}.${VERSION_MINOR}")
string(TIMESTAMP BUILD_TSTAMP "%Y-%m-%d %H:%M:%S")

if ("${VERSION_COMMIT}" EQUAL ${VERSION})
  set (VERSION_COMMIT "")
endif()

configure_file (${CMAKE_SOURCE_DIR}/cmake/modules/version.c.in
                ${CMAKE_BINARY_DIR}/misc/version.c)
# ---

set(MAIN_INCLUDE_DIR ${CMAKE_SOURCE_DIR}/include)
set(BLUEZ_DIR "${CMAKE_SOURCE_DIR}/bluez")

file(GLOB BLUEZ_FILES CONFIGURE_DEPENDS "${BLUEZ_DIR}/*.c")

add_subdirectory(scanMijia)
add_subdirectory(scanBeacons)
add_subdirectory(lescan)
add_subdirectory(java)
add_subdirectory(jniexp)


