cmake . \
    -DCMAKE_TOOLCHAIN_FILE=~/Android/Sdk/ndk/25.2.9519653/build/cmake/android.toolchain.cmake \
    -DANDROID_ABI=arm64-v8a \
    -DANDROID_PLATFORM=android-27

cmake . \
    -DCMAKE_TOOLCHAIN_FILE=~/Android/Sdk/ndk/25.2.9519653/build/cmake/android.toolchain.cmake \
    -DANDROID_ABI=x86_64 \
    -DANDROID_PLATFORM=android-27