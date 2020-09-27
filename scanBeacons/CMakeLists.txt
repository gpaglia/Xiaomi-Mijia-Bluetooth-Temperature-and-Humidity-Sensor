add_executable(scanBeacons scanBeacons.c ${BLUEZ_FILES} ${CMAKE_BINARY_DIR}/misc/version.c)
target_include_directories(scanBeacons PUBLIC ${BLUEZ_DIR} ${MAIN_INCLUDE_DIR})
