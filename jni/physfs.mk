LOCAL_PATH:= $(call my-dir)/physfs

include $(CLEAR_VARS)

LOCAL_MODULE:= physfs 
LOCAL_SRC_FILES:= $(LOCAL_PATH)/src/physfs.c \
	$(LOCAL_PATH)/src/physfs_archiver_7z.c \
	$(LOCAL_PATH)/src/physfs_archiver_dir.c \
	$(LOCAL_PATH)/src/physfs_archiver_grp.c \
	$(LOCAL_PATH)/src/physfs_archiver_hog.c \
	$(LOCAL_PATH)/src/physfs_archiver_iso9660.c \
	$(LOCAL_PATH)/src/physfs_archiver_mvl.c \
	$(LOCAL_PATH)/src/physfs_archiver_qpak.c \
	$(LOCAL_PATH)/src/physfs_archiver_slb.c \
	$(LOCAL_PATH)/src/physfs_archiver_unpacked.c \
	$(LOCAL_PATH)/src/physfs_archiver_vdf.c \
	$(LOCAL_PATH)/src/physfs_archiver_wad.c \
	$(LOCAL_PATH)/src/physfs_archiver_zip.c \
	$(LOCAL_PATH)/src/physfs_byteorder.c \
	$(LOCAL_PATH)/src/physfs_platform_android.c \
	$(LOCAL_PATH)/src/physfs_platform_haiku.cpp \
	$(LOCAL_PATH)/src/physfs_platform_os2.c \
	$(LOCAL_PATH)/src/physfs_platform_posix.c \
	$(LOCAL_PATH)/src/physfs_platform_qnx.c \
	$(LOCAL_PATH)/src/physfs_platform_unix.c \
	$(LOCAL_PATH)/src/physfs_platform_windows.c \
	$(LOCAL_PATH)/src/physfs_platform_winrt.cpp \
	$(LOCAL_PATH)/src/physfs_unicode.c

LOCAL_C_INCLUDES := $(LOCAL_PATH)
LOCAL_CFLAGS:=-O3

include $(BUILD_STATIC_LIBRARY)