#include "com_gpaglia_bluetooth_exp_test_JniExample1.h"

/*
 * Class:     com_gpaglia_bluetooth_exp_test_JniExample1
 * Method:    callc
 * Signature: (Lcom/gpaglia/bluetooth/exp/test/Another;)I
 */
JNIEXPORT jint JNICALL Java_com_gpaglia_bluetooth_exp_test_JniExample1_callc
  (JNIEnv * env, jobject this, jobject cb) {
    return 911;
  }