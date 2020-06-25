
#set (_JAVA_HOME $ENV{JAVA_HOME})
#message(STATUS "=== _JAVA_HOME=${_JAVA_HOME} JAVA_HOME=" $ENV{JAVA_HOME})

find_package(Java 11 REQUIRED)
include(UseJava)

if (NOT DEFINED $ENV{JAVA_HOME_NATIVE})
  set (JAVA_HOME_NATIVE $ENV{JAVA_HOME})
  set (JAVAC $ENV{JAVA_HOME}/bin/javac)
  set (JAR $ENV{JAVA_HOME}/bin/jar)
else ()
  set (JAVAC $ENV{JAVA_HOME_NATIVE}/bin/javac)
  set (JAR $ENV{JAVA_HOME_NATIVE}/bin/jar)
endif ()


# Check that we can use javah
if(NOT Java_JAVAH_EXECUTABLE)
    message(FATAL_ERROR "Cannot locate javah executable.")
endif(NOT Java_JAVAH_EXECUTABLE)

set(LESCAN_JAVA_PREFIX "com/gpaglia/bluetooth/lescan")
set(LESCAN_JAVA_API_PREFIX "${LESCAN_JAVA_PREFIX}/api")
set(LESCAN_JAVA_IMPL_PREFIX "${LESCAN_JAVA_PREFIX}/impl")

file(GLOB_RECURSE JAVA_API_SOURCES CONFIGURE_DEPENDS "${LESCAN_JAVA_API_PREFIX}/*.java")
file(GLOB_RECURSE JAVA_IMPL_SOURCES CONFIGURE_DEPENDS "${LESCAN_JAVA_IMPL_PREFIX}/*.java")

add_jar(lescan-api ${JAVA_API_SOURCES}
                  VERSION ${VERSION}
                  OUTPUT_NAME lescan-api
)

set(LESCAN_API_JAVA_PREFIX ${LESCAN_JAVA_PREFIX}/api)
# install (FILES ${CMAKE_CURRENT_BINARY_DIR}/lescan.jar DESTINATION ${CMAKE_INSTALL_LIBDIR}/../lib/java)


#[[
set(CMAKE_JNI_TARGET TRUE)
file(GLOB JAVA_SOURCES "*.java")
add_jar(lescan ${JAVA_SOURCES}
                  MANIFEST ${CMAKE_CURRENT_BINARY_DIR}/manifest.txt
                  OUTPUT_NAME lescan
)
#]]

# set(JAVA_CLASSES XXX)
#[[
add_custom_command (TARGET lescan
  POST_BUILD
  COMMAND ${CMAKE_COMMAND} -E echo "Generating JNI headers.."
  WORKING_DIRECTORY "${CMAKE_CURRENT_BINARY_DIR}/${CMAKE_FILES_DIRECTORY}/tinybjar.dir"
  COMMAND ${Java_JAVAH_EXECUTABLE} -d jni/ -jni ${JAVA_CLASSES}
)
#]]

set(JNI_HEADER_PATH "${CMAKE_CURRENT_BINARY_DIR}/${CMAKE_FILES_DIRECTORY}/lescan.dir/jni")
# install (FILES ${CMAKE_CURRENT_BINARY_DIR}/lescan.jar DESTINATION ${CMAKE_INSTALL_LIBDIR}/../lib/java)

# add_subdirectory (jni)