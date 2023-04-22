#include <jni.h>

static JavaVM* mVm;
static void* mReserved;
static jclass clazz;

extern jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    mVm = vm;
    mReserved = reserved;

    JNIEnv * mEnv;
    (*mVm)->GetEnv(mVm, (void**) &mEnv, JNI_VERSION_1_4);
    clazz = (*mEnv)->FindClass(mEnv, "org/libsdl/app/SDLActivity");
    clazz = (*mEnv)->NewGlobalRef(mEnv, clazz);

    return JNI_VERSION_1_4;
}

void sendMessageJNI(int typ, int obj) {
    JNIEnv * mEnv;
    (*mVm)->AttachCurrentThread(mVm, &mEnv, NULL);
    jmethodID messageMe = (*mEnv)->GetStaticMethodID(mEnv, clazz, "sendMessage", "(II)Z");
    (*mEnv)->CallStaticBooleanMethod(mEnv, clazz, messageMe, typ, obj);
}
