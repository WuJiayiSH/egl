/*

GLSample.java 2007 Jan 11.

EGL is a pure java 3D Graphics API which was designed for J2ME mobile devices
and based on CLDC 1.0 only, which provided OpenGL-like interface and supports
basic 3D pipeline and texture.

Copyright (C) 2007 Jiayi Wu (wujiyish@msn.com).

This program is free software; you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program; if not, write to the Free Software Foundation, Inc., 51 Franklin
Street, Fifth Floor, Boston, MA 02110-1301 USA.

*/


//#define SERVLET_URL "http://ssj0.java.j.dev.konami.net/ssj0/servlet/"

	//#define USE_MACRO_TO_DRAW
#define DEBUG_ON 1

/* #if N40I == 1
	#define FULL_CANVAS

	#define C120
	#define NOKIA
	#define TONE_SOUND


#endif */

#if N40II == 1
	#define FULL_CANVAS

	//#define SOUND_MIDI

	//#define SOUND_SX2
	//#define SOUND_NOKIA
	#define C120
	#define NOKIA



	//#define SIS_M65
	//#define SOUND_TEST
	//#define TONE_SOUND
#endif

#if N60C == 1
	#define FULL_CANVAS

	#define C176
	#define NOKIA
	//#define SOUND_NOKIA
	#define SOUND_NOKIA
	//#define SOUND_MIDI

#endif


/* #if QD == 1
	#define FULL_CANVAS

	#define C176
	#define NOKIA
	//#define SOUND_NOKIA
	#define SOUND_NOKIA

#endif */






#if GM == 1
	#define USE_COMMAND

	#define SOUND_MIDI
	#define C176


	//#define SOUND_TEST
#endif

#if GL == 1
	//#define SOUND_NOKIA
	#define USE_COMMAND

	#define SOUND_MIDI

	//#define LOAD_ALL_IMAGE
	#define C240
	#undef GL
#endif



#if GS == 1
	#define USE_COMMAND

	#define SOUND_MIDI

	#define C120

#endif





#if MS == 1
	#define USE_COMMAND

	#define SOUND_MIDI
	#define C120
	#define FULL2

#endif



/* #if NL == 1
	#define USE_COMMAND

	#define C240
	#define SOUND_NEC


#endif
 */



#ifdef DEBUG_ON
	#define SOUT(str) System.out.println(str)
	//#define sout2(str) System.out.print(str)
	//#define soutV(str, vertex) System.out.println(str+" "+MathFP.toString(vertex[0])+" "+MathFP.toString(vertex[1]) +" "+MathFP.toString(vertex[2])+" "+MathFP.toString(vertex[3]))
#else
	#define SOUT(str) 
	// #define sout2(str)
	// #define soutV(str, vertex) 
#endif

#ifdef GC_BUG
	#define GC() System.gc();
#else
	#define GC()
#endif

#ifdef C120
#define CSIZE 120
#define SCALE(x) (x)
#endif

#ifdef C176
#define CSIZE 176
#define SCALE(x) (x*3/2)
#endif

#ifdef C240
#define CSIZE 240
#define SCALE(x) (x*2)
#endif