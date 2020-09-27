add_executable(scanMijia scanMijia.c ${BLUEZ_FILES} ${CMAKE_BINARY_DIR}/misc/version.c)
target_include_directories(scanMijia PUBLIC ${BLUEZ_DIR} ${MAIN_INCLUDE_DIR})
