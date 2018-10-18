/*

FPMethod.java 2007 Jan 11.

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

#define FPMUL(x, y) 		MathFP.mul(x,y)
#define FPDIV(x,y) 		MathFP.div(x,y)

#define FPSIN(s)			MathFP.sin(s)
#define FPCOS(s)			MathFP.cos(s)
#define FPTAN(s)			MathFP.tan(s)

#define TOFP(i) 			((i) << 16)
#define TOINT(i) 			((i) >> 16) 
#define TOSTR(i)			MathFP.toString(i)

#define FPSQRT(x)			MathFP.sqrt (x)

#define FP_1 				(1 << 16)
#define FP_0 				0

#define FP_PI				(411774>>1)

#define FP_MAX_VALUE		2147483647
#define FP_MIN_VALUE		-2147483647

#define FP 					int