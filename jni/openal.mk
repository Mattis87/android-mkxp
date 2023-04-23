LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

ifeq ($(TARGET_ARCH_ABI),armeabi)
  # ARMv5, used fixed point math
  LOCAL_CFLAGS += -marm -DOPENAL_FIXED_POINT -DOPENAL_FIXED_POINT_SHIFT=16
endif
ifeq ($(TARGET_ARCH_ABI), armeabi)
	LOCAL_CFLAGS += -DARCH_32BIT
else ifeq ($(TARGET_ARCH_ABI), armeabi-v7a)
	LOCAL_CFLAGS += -DARCH_32BIT
else ifeq ($(TARGET_ARCH_ABI), x86)
	LOCAL_CFLAGS += -DARCH_32BIT
else ifeq ($(TARGET_ARCH_ABI), mips)
	LOCAL_CFLAGS += -DARCH_32BIT
endif

LOCAL_MODULE:= openal
MAX_SOURCES_LOW ?= 4
MAX_SOURCES_START ?= 8
MAX_SOURCES_HIGH ?= 64
LOCAL_CPPFLAGS += -DMAX_SOURCES_LOW=$(MAX_SOURCES_LOW) \
				-DMAX_SOURCES_START=$(MAX_SOURCES_START) \
				-DMAX_SOURCES_HIGH=$(MAX_SOURCES_HIGH) \
				-DAL_ALEXT_PROTOTYPES -DANDROID \
				-ffunction-sections \
				-funwind-tables \
				-fstack-protector \
				-fno-short-enums \
				-DHAVE_GCC_VISIBLITY \
				-O3 \
				-DPOST_FROYO \
				-fexceptions \
				-DRESTRICT= \

LOCAL_C_INCLUDES := \
	$(LOCAL_PATH)/openal-soft \
	$(LOCAL_PATH)/openal-soft/include \
	$(LOCAL_PATH)/openal-soft/common

LOCAL_SRC_FILES := \
	$(LOCAL_PATH)/openal-soft/al/extension.cpp \
	$(LOCAL_PATH)/openal-soft/al/auxeffectslot.cpp \
	$(LOCAL_PATH)/openal-soft/al/event.cpp \
	$(LOCAL_PATH)/openal-soft/al/effect.cpp \
	$(LOCAL_PATH)/openal-soft/al/buffer.cpp \
	$(LOCAL_PATH)/openal-soft/al/source.cpp \
	$(LOCAL_PATH)/openal-soft/al/state.cpp \
	$(LOCAL_PATH)/openal-soft/al/error.cpp \
	$(LOCAL_PATH)/openal-soft/al/listener.cpp \
	$(LOCAL_PATH)/openal-soft/al/filter.cpp \
	$(LOCAL_PATH)/openal-soft/al/effects/pshifter.cpp \
	$(LOCAL_PATH)/openal-soft/al/effects/echo.cpp \
	$(LOCAL_PATH)/openal-soft/al/effects/dedicated.cpp \
	$(LOCAL_PATH)/openal-soft/al/effects/fshifter.cpp \
	$(LOCAL_PATH)/openal-soft/al/effects/distortion.cpp \
	$(LOCAL_PATH)/openal-soft/al/effects/vmorpher.cpp \
	$(LOCAL_PATH)/openal-soft/al/effects/compressor.cpp \
	$(LOCAL_PATH)/openal-soft/al/effects/modulator.cpp \
	$(LOCAL_PATH)/openal-soft/al/effects/reverb.cpp \
	$(LOCAL_PATH)/openal-soft/al/effects/null.cpp \
	$(LOCAL_PATH)/openal-soft/al/effects/chorus.cpp \
	$(LOCAL_PATH)/openal-soft/al/effects/equalizer.cpp \
	$(LOCAL_PATH)/openal-soft/al/effects/convolution.cpp \
	$(LOCAL_PATH)/openal-soft/al/effects/effects.cpp \
	$(LOCAL_PATH)/openal-soft/al/effects/autowah.cpp \
	$(LOCAL_PATH)/openal-soft/alc/alconfig.cpp \
	$(LOCAL_PATH)/openal-soft/alc/device.cpp \
	$(LOCAL_PATH)/openal-soft/alc/panning.cpp \
	$(LOCAL_PATH)/openal-soft/alc/alu.cpp \
	$(LOCAL_PATH)/openal-soft/alc/alc.cpp \
	$(LOCAL_PATH)/openal-soft/alc/context.cpp \
	$(LOCAL_PATH)/openal-soft/alc/effects/pshifter.cpp \
	$(LOCAL_PATH)/openal-soft/alc/effects/echo.cpp \
	$(LOCAL_PATH)/openal-soft/alc/effects/dedicated.cpp \
	$(LOCAL_PATH)/openal-soft/alc/effects/fshifter.cpp \
	$(LOCAL_PATH)/openal-soft/alc/effects/distortion.cpp \
	$(LOCAL_PATH)/openal-soft/alc/effects/vmorpher.cpp \
	$(LOCAL_PATH)/openal-soft/alc/effects/compressor.cpp \
	$(LOCAL_PATH)/openal-soft/alc/effects/modulator.cpp \
	$(LOCAL_PATH)/openal-soft/alc/effects/reverb.cpp \
	$(LOCAL_PATH)/openal-soft/alc/effects/null.cpp \
	$(LOCAL_PATH)/openal-soft/alc/effects/chorus.cpp \
	$(LOCAL_PATH)/openal-soft/alc/effects/equalizer.cpp \
	$(LOCAL_PATH)/openal-soft/alc/effects/convolution.cpp \
	$(LOCAL_PATH)/openal-soft/alc/effects/autowah.cpp \
	$(LOCAL_PATH)/openal-soft/alc/backends/base.cpp \
	$(LOCAL_PATH)/openal-soft/alc/backends/loopback.cpp \
	$(LOCAL_PATH)/openal-soft/alc/backends/wave.cpp \
	$(LOCAL_PATH)/openal-soft/alc/backends/opensl.cpp \
	$(LOCAL_PATH)/openal-soft/alc/backends/null.cpp \
	$(LOCAL_PATH)/openal-soft/core/voice.cpp \
	$(LOCAL_PATH)/openal-soft/core/uhjfilter.cpp \
	$(LOCAL_PATH)/openal-soft/core/hrtf.cpp \
	$(LOCAL_PATH)/openal-soft/core/cubic_tables.cpp \
	$(LOCAL_PATH)/openal-soft/core/ambidefs.cpp \
	$(LOCAL_PATH)/openal-soft/core/bs2b.cpp \
	$(LOCAL_PATH)/openal-soft/core/effectslot.cpp \
	$(LOCAL_PATH)/openal-soft/core/mixer.cpp \
	$(LOCAL_PATH)/openal-soft/core/helpers.cpp \
	$(LOCAL_PATH)/openal-soft/core/cpu_caps.cpp \
	$(LOCAL_PATH)/openal-soft/core/fpu_ctrl.cpp \
	$(LOCAL_PATH)/openal-soft/core/buffer_storage.cpp \
	$(LOCAL_PATH)/openal-soft/core/logging.cpp \
	$(LOCAL_PATH)/openal-soft/core/bsinc_tables.cpp \
	$(LOCAL_PATH)/openal-soft/core/bformatdec.cpp \
	$(LOCAL_PATH)/openal-soft/core/except.cpp \
	$(LOCAL_PATH)/openal-soft/core/device.cpp \
	$(LOCAL_PATH)/openal-soft/core/ambdec.cpp \
	$(LOCAL_PATH)/openal-soft/core/devformat.cpp \
	$(LOCAL_PATH)/openal-soft/core/uiddefs.cpp \
	$(LOCAL_PATH)/openal-soft/core/fmt_traits.cpp \
	$(LOCAL_PATH)/openal-soft/core/context.cpp \
	$(LOCAL_PATH)/openal-soft/core/mastering.cpp \
	$(LOCAL_PATH)/openal-soft/core/converter.cpp \
	$(LOCAL_PATH)/openal-soft/core/filters/splitter.cpp \
	$(LOCAL_PATH)/openal-soft/core/filters/nfc.cpp \
	$(LOCAL_PATH)/openal-soft/core/filters/biquad.cpp \
	$(LOCAL_PATH)/openal-soft/core/mixer/mixer_c.cpp \
	$(LOCAL_PATH)/openal-soft/common/threads.cpp \
	$(LOCAL_PATH)/openal-soft/common/alcomplex.cpp \
	$(LOCAL_PATH)/openal-soft/common/almalloc.cpp \
	$(LOCAL_PATH)/openal-soft/common/alfstream.cpp \
	$(LOCAL_PATH)/openal-soft/common/ringbuffer.cpp \
	$(LOCAL_PATH)/openal-soft/common/polyphase_resampler.cpp \
	$(LOCAL_PATH)/openal-soft/common/dynload.cpp \
	$(LOCAL_PATH)/openal-soft/common/alstring.cpp \
	$(LOCAL_PATH)/openal-soft/common/strutils.cpp \

# $(LOCAL_PATH)/core/mixer/mixer_neon.cpp 

LOCAL_LDLIBS := -llog -lOpenSLES
include $(BUILD_SHARED_LIBRARY)

