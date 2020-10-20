#define _POSIX_C_SOURCE 200112L

#include "com_gpaglia_bluetooth_exp_test_JniExample1.h"
#include "com_gpaglia_bluetooth_exp_test_JniExample2.h"
#include "com_gpaglia_bluetooth_exp_test_JniExample3.h"
#include "com_gpaglia_bluetooth_exp_test_ThreadExample_Runner.h"

#include <signal.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <errno.h>

#include "callc.h"

#define handle_error_en(en, msg) \
        do { errno = en; perror(msg); exit(EXIT_FAILURE); } while (0)

#define handle_error(msg) \
        do { perror(msg); exit(EXIT_FAILURE); } while (0)
        
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

// support for example 3

static void * thread_start(void * arg);

/*
 * Class:     com_gpaglia_bluetooth_exp_test_JniExample3
 * Method:    startThread
 * Signature: (IILcom/gpaglia/bluetooth/exp/test/MyCallback;)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_com_gpaglia_bluetooth_exp_test_JniExample3_startThread
  (JNIEnv * env, jobject this, jint count, jint tslp, jint val, jobject cb) {
  
  pthread_attr_t attr;
  pthread_t *thread;
  JavaVM * jvm;
  struct tparams * params;
  int s;

  printf("In startThread, self=%x\n", (unsigned int) pthread_self());
  
  s = (*env) -> GetJavaVM(env, &jvm);
  if (s != 0) {
    handle_error_en(errno, "GetJavaVM");
  }

  params = (struct tparams *) calloc(1, sizeof(struct tparams));
  if (params == NULL) {
    handle_error_en(errno, "calloc");
  }

  params->val = val;
  params->count = count;
  params->tslp = tslp;
  params->jvm = jvm;
  params->cb = (*env) -> NewGlobalRef(env, cb);

  s = pthread_attr_init(&attr);
  if (s != 0) {
    handle_error_en(s, "pthread_attr_init");
  }

  printf("In startThread: creating thread with val=%d, count=%d, tslp=%d\n", params->val, params->count, params->tslp);

  thread = calloc(1, sizeof(pthread_t));

  if (thread == NULL) {
    handle_error_en(errno, "calloc thread");
  }

  s = pthread_create(thread, &attr, &thread_start, (void *) params);
  if (s != 0) {
    handle_error_en(s, "pthread_create");
  }

  printf("In startThread: pthread is %x\n", (unsigned int) *thread);


  jobject bb = (*env)->NewDirectByteBuffer(env, (void *) thread, sizeof(pthread_t));
  return bb;
}

/*
 * Class:     com_gpaglia_bluetooth_exp_test_JniExample3
 * Method:    isThreadAlive
 * Signature: (Ljava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_gpaglia_bluetooth_exp_test_JniExample3_isThreadAlive
  (JNIEnv * env, jobject this, jobject ctx) {

  pthread_t * thread = (*env) ->GetDirectBufferAddress(env, ctx);

  printf("In isThreadAlive: pthread=%x, self=%x\n", (unsigned int) * thread, (unsigned int) pthread_self());

  int s = pthread_kill(*thread, 0);

  printf("In isThreadAlive: pthread_kill returned %d\n", s);

  return (jboolean) (s == 0);
}

/*
 * Class:     com_gpaglia_bluetooth_exp_test_JniExample3
 * Method:    stopThread
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_com_gpaglia_bluetooth_exp_test_JniExample3_stopThread
  (JNIEnv * env, jobject this, jobject ctx) {

  pthread_t * thread = (*env) ->GetDirectBufferAddress(env, ctx);

  printf("In stopThread: pthread=%x, self=%x\n", (unsigned int) * thread, (unsigned int) pthread_self());

  int s = pthread_kill(*thread, SIGUSR1);

  if (s != 0) {
    handle_error_en(errno, "pthread_kill SIGUSR1");
  }
  return;
  }

static void handler(int signum) {
  printf("Thread %x got signal %d\n", (unsigned int) pthread_self(), signum);
}

static void * thread_start(void * arg) {
  struct tparams * params = (struct tparams *) arg;
  struct sigaction oldact;
  int val = params->val;
  int count = params->count;
  int tslp = params->tslp;
  JavaVM * jvm = params->jvm;
  jobject cb = params->cb;

  JNIEnv * env;
  int s;

  printf("In thread_start, val=%d, count=%d, tslp=%d, self=%x\n", val, count, tslp, (unsigned int) pthread_self());

  printf("In thread_start, setting signal handler for SIGUSR1\n");
  struct sigaction sa;
  sa.sa_handler = handler;
  sigemptyset(&sa.sa_mask);
  sa.sa_flags = 0;

  s = sigaction(SIGUSR1, &sa, &oldact);
  if (s != 0) {
    handle_error_en(errno, "sigaction");
  }

  printf("In thread_start, attaching thread");
  s = (*jvm) -> AttachCurrentThread(jvm, (void **) & env, NULL);
  if (s != 0) {
    handle_error_en(errno, "AttachCurrentThread");
  }

  printf("In thread_start, getting class");
  jclass cbClass = (*env)->GetObjectClass(env, cb);
  if (cbClass == NULL) {
    handle_error("cbClass is NULL");
  }

  printf("In thread_start, getting method");
  jmethodID cbMethod = (*env)->GetMethodID(env, cbClass, "accept", "(II)V");
  if (cbMethod == NULL) {
    handle_error("cbMethod is NULL");
  }

  printf("In thread_start: val=%d, count=%d, tslp=%d\n", val, count, tslp);

  for (int i = 0; i < count; i++)
  {
    printf("In thread_start loop: iter=%d, calling cb ...\n", i);

    (*env)->CallVoidMethod(env, cb, cbMethod, val, i);
    s = sleep(tslp);
    printf("In thread loop: sleep returned %d\n", s);
    if (s != 0) {
      break;
    }

  }
  
  /*
  for (int i = 0; i < 10; i++) {
    printf("Thread loop: %d\n", i);
    sleep(1);
  }
  */

  printf("in thread: exiting...\n");

  return (void *) NULL;

}