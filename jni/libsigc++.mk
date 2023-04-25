LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
 
LOCAL_MODULE     := libsigc++

LOCAL_CPP_EXTENSION := .cc

LOCAL_CFLAGS := -O3
LOCAL_SRC_FILES := \
    $(LOCAL_PATH)/libsigc++/sigc++/signal.cc \
    $(LOCAL_PATH)/libsigc++/sigc++/signal_base.cc \
    $(LOCAL_PATH)/libsigc++/sigc++/trackable.cc \
    $(LOCAL_PATH)/libsigc++/sigc++/functors/slot_base.cc \
    $(LOCAL_PATH)/libsigc++/sigc++/adaptors/lambda/lambda.cc \
    $(LOCAL_PATH)/libsigc++/sigc++/connection.cc \
    $(LOCAL_PATH)/libsigc++/sigc++/functors/slot.cc \

LOCAL_C_INCLUDES := $(LOCAL_PATH)/libsigc++

include $(BUILD_STATIC_LIBRARY)
