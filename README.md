EGL 3D Graphics API

Copyright (C) 2007 Jiayi Wu 

INTRODUCTION
============

EGL is a pure java 3D Graphics API which was designed for J2ME mobile devices
and based on CLDC 1.0 only, which provided OpenGL-like interface and supports
basic 3D pipeline and texture.

FEATURES
========

EGL is small, after obfuscated, EGL context class' size is only 11k. 
EGL is based on only CLDC1.0, so it can be easily ported to desktop or DOJA
platform.
EGL uses FIXED POINT so it can be used on devices which does not support float.
EGL supports texture.
EGL does not support light.
As tested, EGL can run on  old devices such as Nokia S40.

LICENSE
=======

EGL is free software; you can redistribute it and/or modify it under the terms
of the GNU GENERAL PUBLIC LICENSE as published by the Free Software Foundation.
Please refer to the LICENSE file (./License.txt) for licensing details.

AUTHOR
======

Jiayi Wu 
Raffles City 17F, 268 Xizang Zhong Lu, Shanghai, China.
KONAMI Shanghai.

wujiyish@msn.com

PROJECT STRUCTURE
=================

./bin 				//sample binary jar and jad
./compiled 			//EGL classes which can be used in your project
./src				//the EGL source code and sample source code
./cv_src 			//the source code after preprocessed
./renderer_sample	//renderer sample for port
./res				//resource for the sample
./doc 				//api documentation
./obj2mds			//some tools which can convert blizzard's mdx model to mds

HOW TO COMPILE THE SOURCE CODES
===============================

We dont have a clear makefile so it is not easy for a beginner to compile this
project. If you only want to use it, just copy classes int the ./compiled to
your project.

We advice you to compile the project using BCB's make. 

The sample has been ported to following midp devices, after modify the env of
the make file, you can build them by following command:

#for Nokia 40 devices:
make -e N40II=1

#for devices such as Sony Ericsson K300/J300, Moto C650/V180 which has an 128
canvas and supports MIDP2:
make -e MS=1

#for Sony Ericsson K500 which has an 128*160 canvas and supports MIDP2:
make -e GS=1

#for devices such as Sony Ericsson K700, Moto V300, Samsung D508 which has an
176*2xx canvas and supports MIDP2:
make -e GM=1

#for Nokia 60 devices (176*208):
make -e N60C=1

#for devices such as Sony Ericsson S700, Moto E680 which has an 240*320 canvas
and supports MIDP2:
make -e GL=1