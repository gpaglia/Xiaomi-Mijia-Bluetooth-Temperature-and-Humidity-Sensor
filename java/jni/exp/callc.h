#include <jni.h>

// struct for thread start routine
struct tparams {
  JavaVM * jvm;
  jobject cb;
  int val;
  int count;
  int tslp;
};