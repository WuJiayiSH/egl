/*

GLU.java 2007 Jan 11.

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
#include "Setting.java"
#include "FPMethod.java"

package com.syjay.egl;

/**
GL util of the GL.
We suggest you read The OpenGL Programming Guide - The Redbook, Chapter 1, 2, 3, 4, 9.
http://www.opengl.org/documentation/red_book/
*/
public class GLU
{
	private final GL gl;
	
	/**
	Basic constructor for GLU.
	*/
	public GLU(GL gl)
	{
		this.gl=gl;
	}
	
	/**
	gluPerspective specifies a viewing frustum into the world coordinate system.
	*/
	public void gluPerspective (int fovy, int aspect, int zNear, int zFar) 
	{
		// sout("uPerspective fovy: "+GETSTR(fovy));
		// sout("uPerspective aspect: "+GETSTR(aspect));
		// sout("uPerspective zNear: "+GETSTR(zNear));
		// sout("uPerspective zFar: "+GETSTR(zFar));
		int xmin, xmax, ymin, ymax;
		// sout ("uPerspective FPMUL(fovy , FPDIV(FP_PI ,  TOFP(360))): "+GETSTR(FPMUL(fovy , FPDIV(FP_PI ,  TOFP(360)))));
		ymax = FPMUL(zNear , FPTAN(FPMUL(fovy , FPDIV(FP_PI ,  TOFP(360))) ));
		ymin = -ymax;
		
		xmin = FPMUL(ymin , aspect);
		xmax =  FPMUL(ymax ,aspect);
		
		// sout("uPerspective ymax: "+GETSTR(ymax));
		// sout("uPerspective ymin: "+GETSTR(ymin));
		// sout("uPerspective xmax: "+GETSTR(xmax));
		// sout("uPerspective xmin: "+GETSTR(xmin));
		
		gl.glFrustum(xmin, xmax, ymin,ymax, zNear, zFar);
		
		//printMatrix(INDEX_PROJECTION);
	}
}