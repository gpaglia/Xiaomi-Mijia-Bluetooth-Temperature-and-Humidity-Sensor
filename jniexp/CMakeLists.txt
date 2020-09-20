add_executable(jniexp jniexp.c ${BLUEZ_FILES} ${CMAKE_BINARY_DIR}/misc/version.c)
target_include_directories(jniexp PUBLIC ${BLUEZ_DIR} ${MAIN_INCLUDE_DIR})
