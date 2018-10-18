echo off
del egl_?_?_?.zip
del classes\*.* /Q
del bin\bank\*.*  /Q
del cv_src\*.* /Q
del cv_src\com\syjay\egl\*.* /Q
del res\*.* /Q
del tmpclasses\*.* /Q
del src\*.java~ /Q
del src\GL\*.java~ /Q
del *.txt~
del *.mf~
del *.pl~
del *.bat~
del makefile~
del proguard.log

cd ..
zip -uJ9qr egl_0_0_3.zip egl\*
move egl_0_0_3.zip egl
cd egl

echo make release ok
echo on
