/*

Setting.java 2007 Jan 11.

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
// #define DEBUG_ON 1

#ifdef DEBUG_ON
	#define sout(str) System.out.println(str)
	#define sout2(str) System.out.print(str)
	#define soutV(str, vertex) System.out.println(str+" "+MathFP.toString(vertex[0])+" "+MathFP.toString(vertex[1]) +" "+MathFP.toString(vertex[2])+" "+MathFP.toString(vertex[3]))
#else
	#define sout(str) 
	#define sout2(str)
	#define soutV(str, vertex) 
#endif

#ifdef GC_BUG
	#define GC() System.gc();
#else
	#define GC()
#endif





// #define COLOR_BUFFER_TYPE 1	/*TYPE_USHORT_444_RGB for nokia 40 devices*/

// #define COLOR_BUFFER_TYPE 0	/*TYPE_INT_888_RGB*/


#if COLOR_BUFFER_TYPE == 1

#define COLOR_TYPE short

#else

#define COLOR_TYPE int

#endif
