
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
# lescan and related artifacts
#
set(LESCAN_JAVA_PREFIX "com/gpaglia/bluetooth/lescan")
set(LESCAN_JAVA_API_PREFIX "${LESCAN_JAVA_PREFIX}/api")
set(LESCAN_JAVA_HCI_PREFIX "${LESCAN_JAVA_PREFIX}/hci")
set(LESCAN_JAVA_HCI_RES_PREFIX "${LESCAN_JAVA_PREFIX}/hci-resources")
set(LESCAN_JNI_HEADER_PATH "${CMAKE_CURRENT_BINARY_DIR}${CMAKE_FILES_DIRECTORY}/lescan.dir/jni")
set(LESCAN_JNI_PREFIX "${LESCAN_JAVA_PREFIX}/jni")

#
# exp and related artifacts
#
set(EXP_JAVA_PREFIX "com/gpaglia/bluetooth/exp")
set(EXP_JAVA_TEST_PREFIX "${EXP_JAVA_PREFIX}/test")
set(EXP_JAVA_TEST_RES_PREFIX "${EXP_JAVA_PREFIX}/test-resources")
set(EXP_JNI_HEADER_PATH "${CMAKE_CURRENT_BINARY_DIR}${CMAKE_FILES_DIRECTORY}/exp.dir/jni")
set(EXP_JNI_PREFIX "${EXP_JAVA_PREFIX}/jni")

#
# lescan source files and resources
#
file(GLOB_RECURSE LESCAN_JAVA_API_SOURCES CONFIGURE_DEPENDS "${LESCAN_JAVA_API_PREFIX}/*.java")
file(GLOB_RECURSE LESCAN_JAVA_HCI_SOURCES CONFIGURE_DEPENDS "${LESCAN_JAVA_HCI_PREFIX}/*.java")
#file(GLOB_RECURSE JAVA_HCI_RESOURCES CONFIGURE_DEPENDS "${LESCAN_JAVA_HCI_RES_PREFIX}/*")
set(LESCAN_JAVA_HCI_RESOURCES "${LESCAN_JAVA_HCI_RES_PREFIX}/META-INF/services/com.gpaglia.bluetooth.lescan.api.ScannerManager")

#
# exp source files and resources
#
file(GLOB_RECURSE EXP_JAVA_TEST_SOURCES CONFIGURE_DEPENDS "${EXP_JAVA_TEST_PREFIX}/*.java")
file(GLOB_RECURSE EXP_TEST_RESOURCES CONFIGURE_DEPENDS "${EXP_JAVA_TEST_RES_PREFIX}/*")

#
# lescan content
#
MESSAGE(STATUS "lescan api sources: ${LESCAN_JAVA_API_SOURCES}")
MESSAGE(STATUS "lescan hci sources: ${LESCAN_JAVA_HCI_SOURCES}")
MESSAGE(STATUS "lescan hci resources: ${LESCAN_JAVA_HCI_RESOURCES}")
MESSAGE(STATUS "lescan  jni prefix: ${LESCAN_JNI_PREFIX}")
MESSAGE(STATUS "lescan hci jni headers dir: ${LESCAN_JNI_HEADER_PATH}")

#
# exp content
#
MESSAGE(STATUS "exp test sources: ${EXP_JAVA_TEST_SOURCES}")
MESSAGE(STATUS "exp test resources: ${EXP_JAVA_TEST_RESOURCES}")
MESSAGE(STATUS "exp jni prefix: ${EXP_JNI_PREFIX}")
MESSAGE(STATUS "exp jni headers dir: ${EXP_JNI_HEADER_PATH}")

#
# lescan jars
#

# lescan-api
set(CMAKE_JAR_CLASSES_PREFIX ${LESCAN_JAVA_API_PREFIX})
add_jar(lescan-api ${LESCAN_JAVA_API_SOURCES}
                  VERSION ${VERSION}
                  OUTPUT_NAME lescan-api
)

# lescan-hci
set(CMAKE_JAVA_INCLUDE_PATH ${LESCAN_JAVA_API_PREFIX})
set(CMAKE_JAR_CLASSES_PREFIX ${LESCAN_JAVA_HCI_PREFIX})
add_jar(lescan-hci 
                  SOURCES ${LESCAN_JAVA_HCI_SOURCES} ${LESCAN_JAVA_HCI_RESOURCES}
                  VERSION ${VERSION}
                  GENERATE_NATIVE_HEADERS lescan-jniheaders DESTINATION ${LESCAN_JNI_HEADER_PATH} # ${CMAKE_CURRENT_SOURCE_DIR}/jni
                  OUTPUT_NAME lescan-hci
)

# exp
set(CMAKE_JAR_CLASSES_PREFIX ${EXP_JAVA_TEST_PREFIX})
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

add_subdirectory (com/gpaglia/bluetooth/exp/jni)