add_executable(lescan lescan.c ${BLUEZ_FILES} ${CMAKE_BINARY_DIR}/misc/version.c)
target_include_directories(lescan PUBLIC ${BLUEZ_DIR} ${MAIN_INCLUDE_DIR})
