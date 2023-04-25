# To get a huge advantage on compilation time,
# copy all makefiles to their subfolders after
# cloning and rename each to Android.mk and
# then uncomment the following lines and 
# comment the rest.

#include $(call all-subdir-makefiles)

L_PATH := $(call my-dir)

include $(L_PATH)/mkxp.mk
include $(L_PATH)/physfs.mk
include $(L_PATH)/libogg.mk
include $(L_PATH)/pixman.mk
include $(L_PATH)/SDL/Android.mk
include $(L_PATH)/SDL_ttf/Android.mk
include $(L_PATH)/SDL_image/Android.mk
include $(L_PATH)/SDL_sound.mk

include $(L_PATH)/fluidsynth/Android.mk
include $(L_PATH)/glib/Android.mk
include $(L_PATH)/libsigc++.mk
include $(L_PATH)/openal.mk
include $(L_PATH)/ruby/Android.mk
include $(L_PATH)/libvorbis/Android.mk