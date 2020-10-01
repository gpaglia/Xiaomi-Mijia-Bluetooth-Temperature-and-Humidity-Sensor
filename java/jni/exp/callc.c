#include "com_gpaglia_bluetooth_exp_test_JniExample1.h"
#include "com_gpaglia_bluetooth_exp_test_ThreadExample_Runner.h"

#include <unistd.h>

/*
 * Class:     com_gpaglia_bluetooth_exp_test_JniExample1
 * Method:    callc
 * Signature: (Lcom/gpaglia/bluetooth/exp/test/Another;)I
 */
JNIEXPORT jint JNICALL Java_com_gpaglia_bluetooth_exp_test_JniExample1_callc
  (JNIEnv *env, jobject this, jobject cb, jint val)
{
  printf("Entering call.c --- val=%d\n", val);
  jclass cbClass = (*env)->GetObjectClass(env, cb);
  jmethodID cbMethod = (*env)->GetMethodID(env, cbClass, "callback", "(I)V");

  printf("In call.c --- found method? %d\n", (cbMethod != 0));

  (*env)->CallVoidMethod(env, cb, cbMethod, 1000 + val);

  printf("In call.c --- returning %d\n", 10000 + val);

  return 10000 + val;
}

JNIEXPORT jint JNICALL Java_com_gpaglia_bluetooth_exp_test_ThreadExample_00024Runner_loop
  (JNIEnv *env, jobject this, jobject cb, jint cnt, jint st)
{
  printf("Entering call.c --- cnt=%d, st=%d\n", cnt, st);
  if (st < 0)
  {
    st = 1;
    printf("In call.c -- setting st to 1 by default\n");
  }
  jclass cbClass = (*env)->GetObjectClass(env, cb);
  jmethodID cbMethod = (*env)->GetMethodID(env, cbClass, "callback", "(I)V");

  printf("In call.c --- found method? %d\n", (cbMethod != 0));

  size_t act = 0;
  for (int i = 0; i < cnt; i++)
  {
    (*env)->CallVoidMethod(env, cb, cbMethod, 1000 + i);

    printf("In call.c --- sleeping for %d secs\n", st);

    act = i + 1;

    sleep(st);
  }

  printf("In call.c -- finished loop, act=%d, returning=%d\n", act, act + 10000);
  return 10000 + act;
}

/*
 * Class:     com_gpaglia_bluetooth_exp_test_JniExample2
 * Method:    callWithByteBuffer
 * Signature: (IS)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_com_gpaglia_bluetooth_exp_test_JniExample2_callWithByteBuffer
  (JNIEnv * env, jobject this, jint ival, jshort shval)
{
    static struct MyNativeStruct {
      int iv;
      short sv;
    } s;


    printf(
      "Entering callWithByteBuffer --- ival=%d (size: %d), shval=%d (size: %d) structSize=%d\n", 
      ival, 
      sizeof(ival),
      shval,
      sizeof(shval),
      sizeof(struct MyNativeStruct)
    );

    s.iv = ival + 1000;
    s.sv = shval + 2000; 

    jobject bb = (*env)->NewDirectByteBuffer(env, (void *) &s, sizeof(struct MyNativeStruct));

    return bb;
}