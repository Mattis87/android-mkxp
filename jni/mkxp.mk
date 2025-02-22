LOCAL_PATH := $(call my-dir)/mkxp
include $(CLEAR_VARS)

LOCAL_MODULE:= mkxp
LOCAL_CPPFLAGS:= \
	-DSHARED_FLUID \
	-DGLES2_HEADER \
	-DMKXP_EMULATE_TOUCH_MOUSE \
	-O3 \
	-fexceptions

ifeq ($(TARGET_ARCH_ABI), armeabi)
	LOCAL_CPPFLAGS += -DARCH_32BIT
else ifeq ($(TARGET_ARCH_ABI), armeabi-v7a)
	LOCAL_CPPFLAGS += -DARCH_32BIT
else ifeq ($(TARGET_ARCH_ABI), x86)
	LOCAL_CPPFLAGS += -DARCH_32BIT
else ifeq ($(TARGET_ARCH_ABI), mips)
	LOCAL_CPPFLAGS += -DARCH_32BIT
endif

LOCAL_C_INCLUDES := \
	$(LOCAL_PATH) \
	$(LOCAL_PATH)/../openal-soft/include \
	$(LOCAL_PATH)/../SDL_sound \
	$(LOCAL_PATH)/../libsigc++ \
	$(LOCAL_PATH)/../boost_headers \
	$(LOCAL_PATH)/../pixman-extra \
	$(LOCAL_PATH)/../physfs/src \
	$(LOCAL_PATH)/../vorbis-include \
	$(LOCAL_PATH)/../ruby/include \
	$(LOCAL_PATH)/../fluidsynth/include \
	$(LOCAL_PATH)/src \
	$(LOCAL_PATH)/shader \
	$(LOCAL_PATH)/assets

LOCAL_SRC_FILES := \
	$(LOCAL_PATH)/src/android.cpp \
	$(LOCAL_PATH)/src/main.cpp \
	$(LOCAL_PATH)/src/audio.cpp \
	$(LOCAL_PATH)/src/bitmap.cpp \
	$(LOCAL_PATH)/src/eventthread.cpp \
	$(LOCAL_PATH)/src/filesystem.cpp \
	$(LOCAL_PATH)/src/font.cpp \
	$(LOCAL_PATH)/src/input.cpp \
	$(LOCAL_PATH)/src/plane.cpp \
	$(LOCAL_PATH)/src/scene.cpp \
	$(LOCAL_PATH)/src/sprite.cpp \
	$(LOCAL_PATH)/src/table.cpp \
	$(LOCAL_PATH)/src/tilequad.cpp \
	$(LOCAL_PATH)/src/viewport.cpp \
	$(LOCAL_PATH)/src/window.cpp \
	$(LOCAL_PATH)/src/texpool.cpp \
	$(LOCAL_PATH)/src/shader.cpp \
	$(LOCAL_PATH)/src/glstate.cpp \
	$(LOCAL_PATH)/src/tilemap.cpp \
	$(LOCAL_PATH)/src/autotiles.cpp \
	$(LOCAL_PATH)/src/graphics.cpp \
	$(LOCAL_PATH)/src/gl-debug.cpp \
	$(LOCAL_PATH)/src/iniconfig.cpp \
	$(LOCAL_PATH)/src/etc.cpp \
	$(LOCAL_PATH)/src/config.cpp \
	$(LOCAL_PATH)/src/settingsmenu.cpp \
	$(LOCAL_PATH)/src/keybindings.cpp \
	$(LOCAL_PATH)/src/tileatlas.cpp \
	$(LOCAL_PATH)/src/sharedstate.cpp \
	$(LOCAL_PATH)/src/gl-fun.cpp \
	$(LOCAL_PATH)/src/gl-meta.cpp \
	$(LOCAL_PATH)/src/vertex.cpp \
	$(LOCAL_PATH)/src/soundemitter.cpp \
	$(LOCAL_PATH)/src/sdlsoundsource.cpp \
	$(LOCAL_PATH)/src/alstream.cpp \
	$(LOCAL_PATH)/src/audiostream.cpp \
	$(LOCAL_PATH)/src/rgssad.cpp \
	$(LOCAL_PATH)/src/bundledfont.cpp \
	$(LOCAL_PATH)/src/vorbissource.cpp \
	$(LOCAL_PATH)/src/windowvx.cpp \
	$(LOCAL_PATH)/src/tilemapvx.cpp \
	$(LOCAL_PATH)/src/tileatlasvx.cpp \
	$(LOCAL_PATH)/src/autotilesvx.cpp \
	$(LOCAL_PATH)/src/midisource.cpp \
	$(LOCAL_PATH)/src/fluid-fun.cpp \
	$(LOCAL_PATH)/binding-mri/binding-mri.cpp \
	$(LOCAL_PATH)/binding-mri/binding-util.cpp \
	$(LOCAL_PATH)/binding-mri/bitmap-binding.cpp \
	$(LOCAL_PATH)/binding-mri/table-binding.cpp \
	$(LOCAL_PATH)/binding-mri/etc-binding.cpp \
	$(LOCAL_PATH)/binding-mri/font-binding.cpp \
	$(LOCAL_PATH)/binding-mri/graphics-binding.cpp \
	$(LOCAL_PATH)/binding-mri/input-binding.cpp \
	$(LOCAL_PATH)/binding-mri/sprite-binding.cpp \
	$(LOCAL_PATH)/binding-mri/viewport-binding.cpp \
	$(LOCAL_PATH)/binding-mri/plane-binding.cpp \
	$(LOCAL_PATH)/binding-mri/window-binding.cpp \
	$(LOCAL_PATH)/binding-mri/tilemap-binding.cpp \
	$(LOCAL_PATH)/binding-mri/audio-binding.cpp \
	$(LOCAL_PATH)/binding-mri/module_rpg.cpp \
	$(LOCAL_PATH)/binding-mri/filesystem-binding.cpp \
	$(LOCAL_PATH)/binding-mri/windowvx-binding.cpp \
	$(LOCAL_PATH)/binding-mri/tilemapvx-binding.cpp \

LOCAL_STATIC_LIBRARIES:=vorbis physfs sigc++ pixman ruby SDL2_static SDL2_ttf_static SDL2_sound_static SDL2_image_static ogg
LOCAL_SHARED_LIBRARIES:=openal fluidsynth
LOCAL_LDLIBS:=-lz -llog -ldl -lm -lOpenSLES
include $(BUILD_SHARED_LIBRARY)
