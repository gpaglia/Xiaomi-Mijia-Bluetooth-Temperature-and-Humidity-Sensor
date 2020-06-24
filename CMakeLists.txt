cmake_minimum_required(VERSION 3.1)

if(${CMAKE_VERSION} VERSION_LESS 3.15)
    cmake_policy(VERSION ${CMAKE_MAJOR_VERSION}.${CMAKE_MINOR_VERSION})
else()
    cmake_policy(VERSION 3.15)
endif()

set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED ON)

project(ScanBeacons VERSION 1.0
                  DESCRIPTION "Scan BLE beacons in passive mode"
                  LANGUAGES C)

set(BLUEZ_FILES bluez/bluetooth.c bluez/hci.c bluez/oui.c)

add_executable(scanMijia src/scanMijia.c ${BLUEZ_FILES})
add_executable(scanBeacons src/scanBeacons.c ${BLUEZ_FILES})
target_include_directories(scanMijia PUBLIC bluez)
target_include_directories(scanBeacons PUBLIC bluez)
