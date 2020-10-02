
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

#
# exp and related artifacts
#
set(EXP_JAVA_PREFIX "com/gpaglia/bluetooth/exp")
set(EXP_JAVA_TEST_PREFIX "${EXP_JAVA_PREFIX}/test")
set(EXP_JAVA_TEST_RES_PREFIX "${EXP_JAVA_PREFIX}/test-resources")
set(EXP_JNI_HEADER_PATH "${CMAKE_CURRENT_BINARY_DIR}${CMAKE_FILES_DIRECTORY}/exp.dir/jni")
set(EXP_JNI_PREFIX "jni/exp")


#
# exp source files and resources
#
file(GLOB_RECURSE EXP_JAVA_TEST_SOURCES CONFIGURE_DEPENDS "src/${EXP_JAVA_TEST_PREFIX}/*.java")
file(GLOB_RECURSE EXP_TEST_RESOURCES CONFIGURE_DEPENDS "src/${EXP_JAVA_TEST_RES_PREFIX}/*")

#
# exp content
#
MESSAGE(STATUS "exp test sources: ${EXP_JAVA_TEST_SOURCES}")
MESSAGE(STATUS "exp test resources: ${EXP_JAVA_TEST_RESOURCES}")
MESSAGE(STATUS "exp jni prefix: ${EXP_JNI_PREFIX}")
MESSAGE(STATUS "exp jni headers dir: ${EXP_JNI_HEADER_PATH}")

# exp
set(CMAKE_JAR_CLASSES_PREFIX ${EXP_JAVA_TEST_PREFIX})
set(CMAKE_JNI_TARGET TRUE)
add_jar(lescan-exp 
                  SOURCES ${EXP_JAVA_TEST_SOURCES} ${EXP_JAVA_TEST_RESOURCES}
                  VERSION ${VERSION}
                  GENERATE_NATIVE_HEADERS exp-jniheaders DESTINATION ${EXP_JNI_HEADER_PATH} # ${CMAKE_CURRENT_SOURCE_DIR}/jni
                  OUTPUT_NAME lescan-exp
)

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

# install (FILES ${CMAKE_CURRENT_BINARY_DIR}/lescan.jar DESTINATION ${CMAKE_INSTALL_LIBDIR}/../lib/java)

add_subdirectory (jni/exp)