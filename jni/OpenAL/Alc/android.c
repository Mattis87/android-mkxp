#ifdef ANDROID
#include <jni.h>
#include "alMain.h"
#include "apportable_openal_funcs.h"

ApportableOpenALFuncs apportableOpenALFuncs;

static JavaVM* mJavaVM;
static jclass clazz;

JavaVM *alcGetJavaVM(void) {
	return mJavaVM;
}

extern jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    mJavaVM = vm;

	if (apportableOpenALFuncs.alc_android_set_java_vm) {
		apportableOpenALFuncs.alc_android_set_java_vm(mJavaVM);
	}

    return JNI_VERSION_1_4;
}

void alcUnloadAndroid()
{
	if (apportableOpenALFuncs.alc_android_set_java_vm) {
		apportableOpenALFuncs.alc_android_set_java_vm(NULL);
	}
}

ALC_API void ALC_APIENTRY alcSuspend(void) {
	if (apportableOpenALFuncs.alc_android_suspend) {
		apportableOpenALFuncs.alc_android_suspend();
	}
}

ALC_API void ALC_APIENTRY alcResume(void) {
	if (apportableOpenALFuncs.alc_android_resume) {
		apportableOpenALFuncs.alc_android_resume();
	}
}

#endif
