#! /bin/bash

mkdir androidsdk
cd androidsdk
curl https://dl.google.com/android/repository/sdk-tools-linux-3859397.zip -o sdk.zip
unzip -q sdk.zip
rm sdk.zip
yes | ./tools/bin/sdkmanager --licenses
yes | ./tools/bin/sdkmanager \
    "platform-tools" \
    "platforms;android-29" \
    "build-tools;29.0.2" \
    "ndk;21.4.7075529"
echo sdk.dir=`pwd` >> ../local.properties
cd ..