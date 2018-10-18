/*

GL.java 2007 Jan 11.

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

#define INDEX_MODELVIEW 0
#define INDEX_PROJECTION 1

#define TEXTURE_OBJECT_WIDTH 	0
#define TEXTURE_OBJECT_HEIGHT 	1
#define TEXTURE_OBJECT_ALL 		2
/**
EGL is a pure java 3D Graphics API which was designed for J2ME mobile devices
and based on CLDC 1.0 only, which provided OpenGL-like interface and supports
basic 3D pipeline and texture.
*/
package com.syjay.egl;



/**
Implementaion of the whole 3D pipeline.
We suggest you read The OpenGL Programming Guide - The Redbook, Chapter 1, 2, 3, 4, 9.
http://www.opengl.org/documentation/red_book/
*/
public class GL 
{
	
	public static final int GL_CULL_FACE			= 0x0B44;
	public static final int GL_FRONT_AND_BACK			= 0x0408;
	public static final int GL_FRONT				= 0x0404;
	public static final int GL_BACK				= 0x0405;
	
	private  boolean cullFaceEnable=false;
	private  int cullFaceMode=GL_BACK;
	/**
	Treats each triplet of vertices as an
	independent triangle.  Vertices 3n-2,
	3n-1, and 3n define triangle n.	N/3
	triangles are drawn.
	*/
	public static final int GL_TRIANGLES=4;
	/**
	Modelview Matrix.
	*/
	public static final int GL_MODELVIEW=5888;
	/**
	Projection Matrix.
	*/
	public static final int GL_PROJECTION=5889;
	/**
	Color buffer bit.
	*/
	public static final int GL_COLOR_BUFFER_BIT=16384;
	/**
	Depth buffer bit.
	*/
	public static final int GL_DEPTH_BUFFER_BIT=256;
	/**
	Texture 2D property.
	*/
	public static final int GL_TEXTURE_2D=3553;
	
	/**
	Depth test property.
	*/
	public static final int GL_DEPTH_TEST=2929;
	/**
	Uses fixed point.
	*/
	public static final int GL_FIXED = 12345;//TODO
	/**
	Uses GL_BYTE.
	*/
	public static final int GL_BYTE  = 12312;
	
	//viewport properties
	private int vpx,vpy,vpw,vph;
	
	/**
	screen buffer
	*/
	private COLOR_TYPE [] colorBuffer;
	
	/**
	is depth test enable? 
	*/
	private boolean  depthTestEnable=false;
	/**
	is texture 2d enable?
	*/
	private boolean texture2DEnable=false;
	
	/**
	clear color
	*/
	// private COLOR_TYPE clearColor=0;
	
	private int [][] matrixs=new int[2][];
	private int matrixMode=INDEX_MODELVIEW;//current matrix mode
	
	private  int[] depthBuffer;
	
	/**
	points contians all vertex ;
	*/
	private int[] vertexPointer;
	
	/**
	points propertis
	*/
	private int []  texCoordPointer;
	
	private int[] colorPointer;
	/**
	elements going to be drawn
	*/
	private int [] elements;
	
	/**
	vertex buffer for rendering
	*/
	private int [][] tempVertexPointer=new int[3][4];
	private int [] [] tempTexCoorPointer=new int[3][2];

	
	/**
	Renderer
	*/
	private final Renderer render;
	
	/**
	temp matrix for translate
	*/
	private int tempMatrix[]=new int[16];
	
	
	private final int identityMatrix[]=	new int[]
	{
		FP_1,FP_0,FP_0,FP_0,
		FP_0,FP_1,FP_0,FP_0,
		FP_0,FP_0,FP_1,FP_0,
		FP_0,FP_0,FP_0,FP_1
	};
	
	private int triangleScanlineMinMaxArrays [][];
	private int triangleScanlineMinMaxArraysD [][];
	private int triangleScanlineMinMaxArraysT [][];
	private int triangleDeltaY;
	
	private int delX,delY,pxd2,pyd2;
	private int bufferSize;//depth and buffer size
	
	private int t;
	
	
	private int textureCount=1;//0 is retained
	
	private int currentTexture;//0 is default texture , do nothing
	private int currentTextureWidth;
	private int currentTextureHeight;
	private int fp_currentTextureWidth_1;
	private int fp_currentTextureHeight_1;
	
	private COLOR_TYPE[][] texelBuffer=new COLOR_TYPE[1][];//=new int[10][];//where stores texture content
	
	private int[] textureObject=new int[TEXTURE_OBJECT_ALL*1];
	/**
	type int
	*/
	private COLOR_TYPE currentColor;
	/** 
	Creates a new GL. 
	If glFlush() is called, The render() of the render will be called.
	@param render The renderer implementation which draw the entire 3D canvas.
	*/
	public GL(Renderer render ) 
	{
		matrixs[INDEX_MODELVIEW]=new int[16];
		matrixs[INDEX_PROJECTION]=new int[16];
		System.arraycopy(identityMatrix,0,matrixs[INDEX_MODELVIEW],0,16);
		System.arraycopy(identityMatrix,0,matrixs[INDEX_PROJECTION],0,16);
		/* 
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		 */
		this.render=render;
		
		
	}
	
	public void glCullFace(int var)
	{
		cullFaceMode=var;
	}
	
	/**
	Specify the matrix mode.
	@param mode GL_MODELVIEW or GL_PROJECTION
	*/
	public void glMatrixMode(int mode)
	{
		switch(mode)
		{
		case GL_MODELVIEW:
			matrixMode=INDEX_MODELVIEW;
			break;
		case GL_PROJECTION:
			matrixMode=INDEX_PROJECTION;
			break;
		}
		
	}
	/**
	Specify the clear for glClear().
	@param r red part of color
	@param g green part of color
	@param b green part of color
	*/
/* 
	public void glClearColor3f(int r,int g, int b)
	{
#if COLOR_BUFFER_TYPE == 1
		r=TOINT(r*16);
		g=TOINT(g*16);
		b=TOINT(b*16);
		
		clearColor=(short)((r<<8) + (g<<4) +b);
#else
		 r=TOINT(r*255);
		 g=TOINT(g*255);
		 b=TOINT(b*255);
		
		
		clearColor=(r<<16) + (g<<8) +b;
#endif
	} 
*/
	/**
	Clear the buffers.
	@param buffer_bit can be GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or both.
	*/
	public void glClear(int buffer_bit)
	{
		depthBuffer=null;
		depthBuffer=new int[bufferSize];
		
		
		colorBuffer=null;
		colorBuffer=new COLOR_TYPE[bufferSize];
/* 		if((buffer_bit & GL_COLOR_BUFFER_BIT)!=0)
		{
			for(int loop0=0;loop0<colorBuffer.length;loop0++)
			{
				colorBuffer[loop0]=clearColor;
			}
			
			
		}
		
		if((buffer_bit & GL_DEPTH_BUFFER_BIT)!=0)
		{
			for(int loop0=0;loop0<depthBuffer.length;loop0++)
			{
				depthBuffer[loop0]=FP_MAX_VALUE;
			}
		} */
	}
	
	
	/**
	Disable GL_DEPTH_TEST or GL_TEXTURE_2D
	@param var GL_DEPTH_TEST or GL_TEXTURE_2D
	*/
	public void glDisable(int var)
	{
		switch(var)
		{
		case GL_DEPTH_TEST:
			depthTestEnable=false;
			break;
			
		case GL_TEXTURE_2D:
			texture2DEnable=false;
			break;
			
		case GL_CULL_FACE:
			cullFaceEnable=false;
		}
	}
	/**
	Enable GL_DEPTH_TEST or GL_TEXTURE_2D
	@param var GL_DEPTH_TEST or GL_TEXTURE_2D
	*/
	public void glEnable(int var)
	{
		switch(var)
		{
		case GL_DEPTH_TEST:
			depthTestEnable=true;
			break;
			
		case GL_TEXTURE_2D:
			texture2DEnable=true;
			break;
			
		case GL_CULL_FACE:
			cullFaceEnable=true;
			break;
		}
	}
	
 	private int[] increaseIntArray(int[] array, int num)
	{
		if(array==null)
		{
			return new int[num];
		}
		
		int[] temp=new int[array.length+num];
		System.arraycopy(array,0,temp,0,array.length);
		return temp;
	} 
	
	
	
	
	/**
	Set the current matrix to  ientity matrix
	*/
	public void glLoadIdentity()
	{
		System.arraycopy(identityMatrix,0,matrixs[matrixMode],0,16);
		// matrixs[matrixMode]=null;
		// matrixs[matrixMode]=
	}
	/**
	Multiply the current matrix by a general scaling matrix
	*/
	public void glScalef(int fp_sx,int fp_sy,int fp_sz)
	{
		//int[] temp=new int[16];
		System.arraycopy(identityMatrix,0,tempMatrix,0,16);
		tempMatrix[ 0]=fp_sx;
		tempMatrix[ 5]=fp_sy;
		tempMatrix[10]=fp_sz;
		
		//matrixMul(matrixs[matrixMode],tempMatrix);
		glMultMatrix(tempMatrix);
	}
	/**
	Multiply the current matrix by a general translating matrix
	*/
	public void glTranslatef(int fp_dx,int fp_dy,int fp_dz)
	{
		//int[] temp=new int[16];
		System.arraycopy(identityMatrix,0,tempMatrix,0,16);
		tempMatrix[ 3]+=fp_dx;
		tempMatrix[ 7]+=fp_dy;
		tempMatrix[11]+=fp_dz;
		
		//matrixMul(matrixs[matrixMode],tempMatrix);
		glMultMatrix(tempMatrix);
	}
	/**
	Multiply the current matrix by a general rotating matrix
	*/
	public void glRotatef(int angle,int x,int y,int z)
	{

		int mag, s, c;
		int xx, yy, zz, xy, yz, zx, xs, ys, zs, one_c;
		
		//final int PId180=FPDIV(FP_PI,TOFP(180));
		// System.out.println("FPDIV(FP_PI,TOFP(180))="+FPDIV(FP_PI,TOFP(180)));
		//TODO sin and cos can be further optimize
		s = FPSIN (FPMUL(angle , 1143));//FPDIV(FP_PI,TOFP(180))==1143
		c = FPCOS (FPMUL(angle , 1143));
		// System.out.println("c="+GETSTR(c));
		mag =  FPSQRT(FPMUL(x , x) + FPMUL(y , y) +FPMUL( z, z));
		// System.out.println("mag="+GETSTR(mag));
		
		if (mag == FP_0) { return ; }
		
		mag=MathFP.inverse(mag);
		x = FPMUL(x,mag); 
		y = FPMUL(y,mag);
		z = FPMUL(z,mag);
		// System.out.println("x="+GETSTR(x));
		xx = FPMUL(x , x); yy = FPMUL(y , y); zz = FPMUL(z , z);
		xy = FPMUL(x , y); yz = FPMUL(y , z); zx = FPMUL(z , x);
		xs = FPMUL(x , s); ys = FPMUL(y , s); zs = FPMUL(z , s);
		one_c = FP_1 - c;
		// System.out.println("xx="+GETSTR(xx));
		tempMatrix [ 0] = FPMUL(one_c , xx) + c;
		tempMatrix [ 4] = FPMUL(one_c , xy) + zs;
		tempMatrix [ 8] = FPMUL(one_c , zx) - ys;
		tempMatrix [12] = FP_0;
		tempMatrix [ 1] = FPMUL(one_c , xy) - zs;
		tempMatrix [ 5] = FPMUL(one_c , yy) + c;
		tempMatrix [ 9] = FPMUL(one_c , yz) + xs;
		tempMatrix [13] = FP_0;
		tempMatrix [ 2] = FPMUL(one_c , zx) + ys;
		// System.out.println("a [ 2]="+GETSTR(a [ 2]));
		tempMatrix [ 6] = FPMUL(one_c , yz) - xs;
		tempMatrix [10] = FPMUL(one_c , zz) + c;
		tempMatrix [14] = FP_0;
		tempMatrix [ 3] = FP_0;
		tempMatrix [ 7] = FP_0;
		tempMatrix [11] = FP_0;
		tempMatrix [15] = FP_1;

		glMultMatrix(tempMatrix);

	}
	

	/**
	Force the renderer to draw the current color buffer
	*/
	public void glFlush()
	{

		render.render(colorBuffer,vpx,vpy,vpw,vph);

	}
	


	private void calculateLine(/*int x1,int y1,int x2,int y2,int z1,int z2,*/int idx0,int idx1)
	{
		
		int x1=TOINT(tempVertexPointer[idx0][0]);
		int x2=TOINT(tempVertexPointer[idx1][0]);
		
		int y1=TOINT(tempVertexPointer[idx0][1]);
		int y2=TOINT(tempVertexPointer[idx1][1]);
		
		int z1=tempVertexPointer[idx0][2];
		int z2=tempVertexPointer[idx1][2];
		
		int u1=tempTexCoorPointer[idx0][0];
		int v1=tempTexCoorPointer[idx0][1];
		
		int u2=tempTexCoorPointer[idx1][0];
		int v2=tempTexCoorPointer[idx1][1];
		
		int iTag;
		int dx,dy,tx,ty,inc1,inc2,d,curx,cury;
		//System.out.println(x1+","+y1);
		
		int dz=0;//z1;
		int du=0;//tempTexCoorPointer[idx0][0];
		int dv=0;//tempTexCoorPointer[idx0][1];
		int oy=y1;
		
		
		//keep x cooridnate inside the screen buffer
		if(x1<0)x1=0;
		// if(y1<0)y1=0;
		if(x2<0)x2=0;
		// if(y2<0)y2=0;
		
		if(x1>=vpw)x1=vpw-1;
		// if(y1>=vph)y1=vph-1;
		if(x2>=vpw)x2=vpw-1;
		// if(y2>=vph)y2=vph-1;
		
		z1=z1/2-(FP_MAX_VALUE>>1);
		z2=z2/2-(FP_MAX_VALUE>>1);
		
		
		if(y2!=y1)
		{
			dz=(z2-z1)/(y2-y1);
			du=(u2-u1)/(y2-y1);;
			dv=(v2-v1)/(y2-y1);;
		}
		
		// System.out.println("calculateLine "+x1+" "+y1+" "+x2+" "+y2+","+du+" "+dv);
		
		setPexel(x1,y1,z1,u1,v1);
		setPexel(x2,y2,z2,u2,v2);
		
		if(x1==x2 && y1==y2)
		{
			return ;
		}
		
		iTag=0;
		dx=Math.abs(x2-x1);
		dy=Math.abs(y2-y1);
		if(dx<dy)
		{
			iTag=1;
			
			int tmp;
			
			tmp=x1;
			x1=y1;
			y1=tmp;
			
			tmp=x2;
			x2=y2;
			y2=tmp;
			
			tmp=dx;
			dx=dy;
			dy=tmp;
			
		}
		
		tx=(x2-x1)>0?1:-1;
		ty=(y2-y1)>0?1:-1;
		
		curx=x1;
		cury=y1;
		
		inc1=2*dy;
		inc2=2*(dy-dx);
		
		d=inc1-dx;
		
		while(curx!=x2)
		{
			if(d<0)
			{
				d+=inc1;
			}else
			{
				cury+=ty;
				d+=inc2;
			}
			
			if(iTag>0)
			{

				setPexel(cury,curx,(curx-oy)*dz+z1,
					(curx-oy)*du+u1,
					(curx-oy)*dv+v1
					);
			}
			else
			{

				setPexel(curx,cury,(cury-oy)*dz+z1,
					(cury-oy)*du+u1,
					(cury-oy)*dv+v1
					);
			}
			
			curx+=tx;
		}
		

		// return 0;

	}
	

	

	
	/**
	set RGB value to the screen buffer at x, y
	
	private void setPexelColor(int x, int y , COLOR_TYPE rgb)
	{
		colorBuffer[ x+ y*vpw ] =rgb;
	}
	*/
	/**
	set the depth value to the depth beffer at x, y
	
	private void setPexelDepth(int x, int y, int depth)
	{
		depthBuffer[ x+ y*vpw ] =depth;
	}
	*/
	
	
	private void setPexel(int x,int y,int z,int u,int v)
	{
		
		// System.out.println("setpixel: x="+x+" y="+y+" u="+u+" v="+v);
		y-=triangleDeltaY;
		if(triangleScanlineMinMaxArrays[y]==null)
		{
			triangleScanlineMinMaxArrays[y]=new int[2];
			triangleScanlineMinMaxArrays[y][0]=x;
			triangleScanlineMinMaxArrays[y][1]=x;
			triangleScanlineMinMaxArraysD[y]=new int[2];
			triangleScanlineMinMaxArraysD[y][0]=z;
			triangleScanlineMinMaxArraysD[y][1]=z;
			
			triangleScanlineMinMaxArraysT[y]=new int[4];
			triangleScanlineMinMaxArraysT[y][0]=u;
			triangleScanlineMinMaxArraysT[y][1]=v;
			triangleScanlineMinMaxArraysT[y][2]=u;
			triangleScanlineMinMaxArraysT[y][3]=v;
		}else
		{
			
			if(x<triangleScanlineMinMaxArrays[y][0])//
			{
				triangleScanlineMinMaxArrays[y][0]=x;
				triangleScanlineMinMaxArraysD[y][0]=z;
				
				triangleScanlineMinMaxArraysT[y][0]=u;
				triangleScanlineMinMaxArraysT[y][1]=v;
				
			}else if(x>triangleScanlineMinMaxArrays[y][1])
			{//
				triangleScanlineMinMaxArrays[y][1]=x;
				triangleScanlineMinMaxArraysD[y][1]=z;
				
				triangleScanlineMinMaxArraysT[y][2]=u;
				triangleScanlineMinMaxArraysT[y][3]=v;
			}
		}
	}
	
	

	/**
	Set the view port .
	@param x the src x coordinate.
	@param y the src y coordinate.
	@param w the viewport width.
	@param h the viewport height.
	*/
	public void glViewport(int x,int y,int w,int h)
	{
		vpx=x;
		vpy=y;
		vpw=w;
		vph=h;
		
		bufferSize=vpw*vph;

		
		glClear(GL_DEPTH_BUFFER_BIT|GL_COLOR_BUFFER_BIT);
		
		pxd2=TOFP(vpw-1);
		pyd2=TOFP(vph-1);
	}
	

	private void matrixMulCoord2(int [] m1, int [] m2, int begin)
	{

		int f0=  FPMUL(m1[begin],m2[0])+
                FPMUL(m1[begin+1],m2[1])+
                FPMUL(m1[begin+2],m2[2])+
                FPMUL(m1[begin+3],m2[3]);
		
		int f1=  FPMUL(m1[begin+0],m2[4])+
                FPMUL(m1[begin+1],m2[5])+
                FPMUL(m1[begin+2],m2[6])+
                FPMUL(m1[begin+3],m2[7]);
		
		int f2=  FPMUL(m1[begin+0],m2[8])+
                FPMUL(m1[begin+1],m2[9])+
                FPMUL(m1[begin+2],m2[10])+
                FPMUL(m1[begin+3],m2[11]);
		
		int f3=  FPMUL(m1[begin+0],m2[12])+
                FPMUL(m1[begin+1],m2[13])+
                FPMUL(m1[begin+2],m2[14])+
                FPMUL(m1[begin+3],m2[15]);
		
		m1[begin+0]=f0;
		m1[begin+1]=f1;
		m1[begin+2]=f2;
		m1[begin+3]=f3;

	}
	
	/**
	Multiply the current matrix by a general  matrix
	*/
	public void glMultMatrix(/*int [] m1, */int [] m2)
	{

		
		int [] m1=matrixs[matrixMode];
		
		tempMatrix[0]=  FPMUL(m1[0],m2[0])+
                FPMUL(m1[1],m2[4])+
                FPMUL(m1[2],m2[8])+
                FPMUL(m1[3],m2[12]);
		
		tempMatrix[1]=  FPMUL(m1[0],m2[1])+
                FPMUL(m1[1],m2[5])+
                FPMUL(m1[2],m2[9])+
                FPMUL(m1[3],m2[13]);
		
		tempMatrix[2]=  FPMUL(m1[0],m2[2])+
                FPMUL(m1[1],m2[6])+
                FPMUL(m1[2],m2[10])+
                FPMUL(m1[3],m2[14]);
		
		tempMatrix[3]=  FPMUL(m1[0],m2[3])+
                FPMUL(m1[1],m2[7])+
                FPMUL(m1[2],m2[11])+
                FPMUL(m1[3],m2[15]);
		
		tempMatrix[4]=  FPMUL(m1[4],m2[0])+
                FPMUL(m1[5],m2[4])+
                FPMUL(m1[6],m2[8])+
                FPMUL(m1[7],m2[12]);
		
		tempMatrix[5]=  FPMUL(m1[4],m2[1])+
                FPMUL(m1[5],m2[5])+
                FPMUL(m1[6],m2[9])+
                FPMUL(m1[7],m2[13]);
		
		tempMatrix[6]=  FPMUL(m1[4],m2[2])+
                FPMUL(m1[5],m2[6])+
                FPMUL(m1[6],m2[10])+
                FPMUL(m1[7],m2[14]);
		
		tempMatrix[7]=  FPMUL(m1[4],m2[3])+
                FPMUL(m1[5],m2[7])+
                FPMUL(m1[6],m2[11])+
                FPMUL(m1[7],m2[15]);
		
		tempMatrix[8]=  FPMUL(m1[8],m2[0])+
                FPMUL(m1[9],m2[4])+
                FPMUL(m1[10],m2[8])+
                FPMUL(m1[11],m2[12]);
		
		tempMatrix[9]=  FPMUL(m1[8],m2[1])+
                FPMUL(m1[9],m2[5])+
                FPMUL(m1[10],m2[9])+
                FPMUL(m1[11],m2[13]);
		
		tempMatrix[10]= FPMUL(m1[8],m2[2])+
                FPMUL(m1[9],m2[6])+
                FPMUL(m1[10],m2[10])+
                FPMUL(m1[11],m2[14]);
		
		tempMatrix[11]= FPMUL(m1[8],m2[3])+
                FPMUL(m1[9],m2[7])+
                FPMUL(m1[10],m2[11])+
                FPMUL(m1[11],m2[15]);       
		
		tempMatrix[12]= FPMUL(m1[12],m2[0])+
                FPMUL(m1[13],m2[4])+
                FPMUL(m1[14],m2[8])+
                FPMUL(m1[15],m2[12]);
		
		tempMatrix[13]= FPMUL(m1[12],m2[1])+
                FPMUL(m1[13],m2[5])+
                FPMUL(m1[14],m2[9])+
                FPMUL(m1[15],m2[13]);
		
		tempMatrix[14]= FPMUL(m1[12],m2[2])+
                FPMUL(m1[13],m2[6])+
                FPMUL(m1[14],m2[10])+
                FPMUL(m1[15],m2[14]);
		
		tempMatrix[15]= FPMUL(m1[12],m2[3])+
                FPMUL(m1[13],m2[7])+
                FPMUL(m1[14],m2[11])+
                FPMUL(m1[15],m2[15]);    
		
		System.arraycopy(tempMatrix,0,m1,0,16);
	}
	

	/**
	glFrustum describes a perspective matrix that produces a perspective projection. (left,bottom,-near) and (right,top,-near) specify the points on the near clipping plane that are mapped to the lower left and upper right corners of the window, respectively, assuming that the eye is located at (0, 0, 0). -far specifies the location of the far clipping plane. Both near and far must be positive. 
	*/
	public void glFrustum(int l, int r, int b, int t, int n, int f){
		// int [] matrix=new int[16];
		
		
		for(int lo=0;lo<16;lo++)
		{
			tempMatrix[lo]=FP_0;
		}
		
		// int fp_1=TOFP(1);
		// int fp_2=TOFP(2);
		tempMatrix[0] = FPDIV(FPMUL(TOFP(2) , n) , r-l );
		tempMatrix[2] = FPDIV(r+l , r-l );
		tempMatrix[5] = FPDIV(FPMUL(TOFP(2) , n) , t-b );
		tempMatrix[6] = FPDIV( t+b , t-b );
		tempMatrix[10]= FPDIV( -f-n , f-n );
		tempMatrix[11]= FPDIV(FPMUL(FPMUL(-TOFP(2) , f),n) , f-n );
		tempMatrix[14]= FP_0-FP_1;
		
		glMultMatrix(tempMatrix);

	}
	

	

	
	protected int[] inter_tex(int[] v1, int[] v2) {
		// point v1 is out, point v2 is in....
		int tempTexCoord [] = new int [2];
		
		tempTexCoord[0]=v2[0]+FPMUL(t,(v1[0]-v2[0]));
		tempTexCoord[1]=v2[1]+FPMUL(t,(v1[1]-v2[1]));
		// tempTexCoord[2]=v2.TexCoord[2]+t*(v1.TexCoord[2]-v2.TexCoord[2]);
		return tempTexCoord;
	}
	
	private int[] inter_point(int[] v1, int[] v2, int i, int j)
	{
		return j==0?inter_point_neg(v1, v2, i):inter_point_pos(v1, v2, i);
/* 		if (j == 0) { return inter_point_neg(v1, v2, i); }
		return inter_point_pos(v1, v2, i);	// j == 1 */
	}
	
	private int[] inter_point_pos(int[] v1, int[] v2, int xy) {
		//sout("// point v1 is out, point v2 is in....");
		int dvertex [] = new int [4];
		int temp[] = new int [4];
		temp[0]=FP_0;
		temp[1]=FP_0;
		temp[2]=FP_0;
		temp[3]=FP_0;
		//	int yx = 1 - xy;
		
		dvertex[0]=v1[0]-v2[0];
		dvertex[1]=v1[1]-v2[1];
		dvertex[2]=v1[2]-v2[2];
		dvertex[3]=v1[3]-v2[3];
		t =FPDIV( (v2[xy]-v2[ 3]),(dvertex[3]-dvertex[xy]));
		temp[3 ]=v2[3 ]+FPMUL(t,dvertex[3 ]);
		if (xy != 2) { temp[2 ]=v2[2 ]+FPMUL(t,dvertex[2 ]); }
		if (xy != 1) { temp[1 ]=v2[1 ]+FPMUL(t,dvertex[1 ]); }
		if (xy != 0) { temp[0 ]=v2[0 ]+FPMUL(t,dvertex[0 ]); }
		//	temp.Vertex[yx]=v2.Vertex[yx]+t*dvertex[yx];
		temp[xy]=temp[3 ];
		
		return temp;
	}
	
	private int[] inter_point_neg(int[] v1, int[] v2, int xy) {
		sout("// point v1 is out, point v2 is in....");
		int dvertex [] = new int [4];
		int temp []= new int [4];
		temp[0]=FP_0;
		temp[1]=FP_0;
		temp[2]=FP_0;
		temp[3]=FP_0;
		//	int yx = 1 - xy;
		int t;
		
		dvertex[0]=v1[0]-v2[0];
		dvertex[1]=v1[1]-v2[1];
		dvertex[2]=v1[2]-v2[2];
		dvertex[3]=v1[3]-v2[3];
		t = FP_0-FPDIV((v2[xy]+v2[ 3]),(dvertex[3]+dvertex[xy]));
		temp[3 ]=v2[3 ]+FPMUL(t,dvertex[3 ]);
		//	temp.Vertex[2 ]=v2.Vertex[2 ]+t*dvertex[2 ];
		if (xy != 2) { temp[2 ]=v2[2 ]+FPMUL(t,dvertex[2 ]); }
		if (xy != 1) { temp[1 ]=v2[1 ]+FPMUL(t,dvertex[1 ]); }
		if (xy != 0) { temp[0 ]=v2[0 ]+FPMUL(t,dvertex[0 ]); }
		//	temp.Vertex[yx]=v2.Vertex[yx]+t*dvertex[yx];
		temp[xy]=FP_0-temp[3 ];
		
		return temp;
	}
	
	/* 
	private boolean IsInside_pos(int p [], int xy)
	{
		if (p [xy] > p [3]) { return false; } else { return true; }
	}
	
	private boolean IsInside_neg(int p [], int xy)
	{
		if (p [xy] < -p [3]) { return false; } else { return true; }
	}
	 */
	private boolean IsInside(int p [], int i, int j) 
	{
		
		return j==0?(p [i] >= -p [3]):(p [i] <= p [3]);
		/* boolean res;
		
		if(j==0) 
		{
			res=(p [i] >= -p [3]); 	//IsInside_neg(p, i); 
		}else
		{
			res=(p [i] <= p [3]); //  IsInside_pos(p, i);//j==1
		}
		

		return res;
		*/
	}
	

	/**
	glGenTextures returns n texture names in textures.
	@param n number of tex.
	@param textureNames array stores the tex names.
	*/
	public void glGenTextures(int n, int[] textureNames)
	{
		int cnt=0;
		int lo=1;
		int moreObject=n;
		
		for(;lo<texelBuffer.length;lo++)
		{
			if(texelBuffer[lo]==null)
			{
				moreObject--;
			}
		}
		
		if(moreObject>0)
		{
			COLOR_TYPE[][] temp = new COLOR_TYPE[texelBuffer.length+moreObject][];
			System.arraycopy(texelBuffer,0,temp,0,texelBuffer.length);
			texelBuffer=temp;
			textureObject=increaseIntArray(textureObject , TEXTURE_OBJECT_ALL*moreObject);
		}
		
		lo=1;
		
		while(cnt<n)
		{
			if(texelBuffer[lo]==null)
			{
				textureNames[cnt++]=lo++;
				
			}
		}
/* 		for(int loop0=0;loop0<n;loop0++)
		{
			textureNames[loop0]=textureCount++;
		} */
	}
	
	/**
	Active the specfied texture.
	@param target GL_TEXTURE_2D
	@param textureName the specfied texture name.
	*/
	public void glBindTexture(int target, int textureName)
	{
		if(target==GL_TEXTURE_2D)
		{
			currentTexture=textureName;
			// currentTextureWidth  = textureObject[currentTexture * TEXTURE_OBJECT_ALL +TEXTURE_OBJECT_WIDTH];
			// currentTextureHeight = textureObject[currentTexture * TEXTURE_OBJECT_ALL +TEXTURE_OBJECT_HEIGHT];
		}
	}
	
	
	/**
	Build the  texture.
	@param target GL_TEXTURE_2D
	@param level no use
	@param component no use
	@param width texture width
	@param height texture height
	@param border no use
	@param format no use
	@param type GL_FIXED
	@param texels the texture data
	*/
	public void glTexImage2D(int target, int level, int component, int width, int height
	, int border, int format, int type, byte[]texels)
	{
		sout("glTexImage2D width="+width+" height="+height);
		COLOR_TYPE [] data=new COLOR_TYPE[ width * height];
		//TODO goto byte array
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				int r,g,b;           
				r=texels[i*width*3 + j*3 + 0];
				if(r<0)r+=256;
					
				g=texels[i*width*3 + j*3 + 1];
				if(g<0)g+=256;
					
				b=texels[i*width*3 + j*3 + 2];
				if(b<0)b+=256;
#if COLOR_BUFFER_TYPE == 1
				data[ j + width*i ]=(short)(((r/16)<<8) + ((g/16)<<4) + ((b/16))+0xf000);
#else
				data[ j + width*i ]=(r<<16) + (g<<8) + (b<<0)+0xff000000;
#endif
			}
		}
		sout("glTexImage2D 0");
		texelBuffer[currentTexture]=data;
		// sout("texImage2D temp.length="+temp.length+" texels.length="+ texels.length);
		sout("glTexImage2D 1");
		// int lastIndex=textureObject.length;
		// textureObject=increaseIntArray(textureObject , TEXTURE_OBJECT_ALL);
		textureObject[currentTexture*TEXTURE_OBJECT_ALL + TEXTURE_OBJECT_WIDTH]=width;
		textureObject[currentTexture*TEXTURE_OBJECT_ALL + TEXTURE_OBJECT_HEIGHT]=height;
		currentTextureWidth=width;
		currentTextureHeight=height;
		fp_currentTextureWidth_1=TOFP(currentTextureWidth-1);
		fp_currentTextureHeight_1=TOFP(currentTextureHeight-1);
		sout("glTexImage2D 2");
	}
	
	/**
	Delete named textures.
	@param n number of textures.
	@param textures texture names to be deleted
	*/
	public void glDeleteTextures(int n, int [] textures)
	{
		for(int lo=0;lo<n;lo++)
		{
			texelBuffer[textures[lo]]=null;
		}
		
		if(texelBuffer[currentTexture]==null)
		{
			currentTexture=0;
		}
	}

	private  int textureTestBetween0to1(int fp_u)
	{
		while(fp_u<FP_0)
		{
			fp_u+=FP_1;
		}
		
		while(fp_u>FP_1)
		{
			fp_u-=FP_1;
		}
		

		
		return fp_u;
	}
	
	/**
	Specify the vertex pointer.
	@param size 3
	@param type GL_FIX
	@param stride 0
	@param pointer coordinates
	*/
	
	public void glVertexPointer(int size, int type, int stride, int[] pointer)
	{
		vertexPointer=pointer;
	}
	
	/**
	Specify the ColorPointer
	@param size 3
	@param type GL_FIX
	@param stride 0
	@param pointer ColorPointer
	*/
	public void glColorPointer(int size, int type, int stride, int[] pointer)
	{
		colorPointer=pointer;
	}
	
	/**
	Specify the TexCoordPointer.
	@param size 2
	@param type GL_FIX
	@param stride 0
	@param pointer TexCoordPointer.
	*/
	public void glTexCoordPointer(int size, int type, int stride, int[] pointer)
	{
		texCoordPointer=pointer;
	}
	
	
	/**
	specifies multiple geometric primitives with very few subroutine calls.
	@param mode GL_TRIANGLES
	@param count number of points to be drawn
	@param type no use
	@param indices index array to be drawn
	*/
	public void glDrawElements(int mode, int count, int type, int[] indices)
	{
		elements=indices;
		
		// if(mode==GL_TRIANGLES)
		// 
		for(int lo=0;lo<count;lo+=3)//3point a triangle,
		{
			sout("glDrawElements lo="+lo);
			if(colorPointer!=null)
			{
				#if COLOR_BUFFER_TYPE == 1
				int r=TOINT(colorPointer[elements[lo+2]*3+0]*16);
				int g=TOINT(colorPointer[elements[lo+2]*3+1]*16);
				int b=TOINT(colorPointer[elements[lo+2]*3+2]*16);
				
				
				currentColor=(short)((r<<8) + (g<<4) +b);
				#else
				int r=TOINT(colorPointer[elements[lo+2]*3+0]*255);
				int g=TOINT(colorPointer[elements[lo+2]*3+1]*255);
				int b=TOINT(colorPointer[elements[lo+2]*3+2]*255);
				
				
				currentColor=(r<<16) + (g<<8) +b;
				#endif
			}
			for (int _lo=0;_lo<3;_lo++)//
			{//
				System.arraycopy(vertexPointer,elements[lo+_lo]*3,tempVertexPointer[_lo],0,3);
				/* tempVertexPointer[_lo][0]=vertexPointer[elements[lo+_lo]*3];
				tempVertexPointer[_lo][1]=vertexPointer[elements[lo+_lo]*3+1];
				tempVertexPointer[_lo][2]=vertexPointer[elements[lo+_lo]*3+2]; */
				tempVertexPointer[_lo][3]=FP_1;//vertexPointer[elements[lo+_lo]*3+3];
				
				matrixMulCoord2(tempVertexPointer[_lo],matrixs[INDEX_MODELVIEW],0);
				matrixMulCoord2(tempVertexPointer[_lo],matrixs[INDEX_PROJECTION],0);
				
				if(texture2DEnable)
				{
					System.arraycopy(texCoordPointer,elements[lo+_lo]*2,
						tempTexCoorPointer[_lo],0,2);
					// tempTexCoorPointer[_lo][0]=texCoordPointer[elements[lo+_lo]*2];
					// tempTexCoorPointer[_lo][1]=texCoordPointer[elements[lo+_lo]*2+1];
				}
			}
			
			
			
			
			sout("glDrawElements 2");
			
			int outlistCnt=clipTriangle();
			
			
			for(int _lo=0;_lo<outlistCnt;_lo++)
			{
				int inversedW=MathFP.inverse(tempVertexPointer[_lo][3]);
				tempVertexPointer[_lo][0] = 
				((FPMUL(FPMUL(tempVertexPointer[_lo][0],inversedW),pxd2)+pxd2)>>1)+TOFP(vpx);
				tempVertexPointer[_lo][1] = 
				((FPMUL(-FPMUL(tempVertexPointer[_lo][1],inversedW),pyd2)+pyd2)>>1)+TOFP(vpy);
				tempVertexPointer[_lo][2] = FPMUL(tempVertexPointer[_lo][2],inversedW);
			}
			sout("glDrawElements 3");
			
			fillPolygon(outlistCnt);
			sout("glDrawElements 4");
			
		}
		
	}
	
	/*
	*code about clipping begins here
	*/
	private int clipTriangle() 
	{
		//int inlist_n=3;
		int outlist_n=3;
		// sout("======      Clipping debug begins here:      ======");
		for (int i = 0; i < 3; i++) 
		{
			for (int j = 0; j < 2; j++)
			{
				// sout("current Clip Plane: "+i);
				
				int temp [][];
				
				int inlist_n=outlist_n;
				
				int[][] inlist_Polygon=tempVertexPointer;
				// sout("input polygon corners counter="+inlist_n);
				
				int[][] outlist_Polygon = new int [inlist_n][4];
				int[][] outputTexCoord = new int [inlist_n][2];
				outlist_n = 0;
				
				int size = inlist_n;
				int prev = inlist_n - 1;
				
				if (inlist_n == 0) { continue; }
				
				for (int curr = 0; curr < inlist_n; curr++) //对每个点
				{
					// sout("processing vertex "+curr);
					if (IsInside(inlist_Polygon[curr],i,j)) 
					{
						if (IsInside(inlist_Polygon[prev],i,j)) 
						{
							// both in....just copy....
							// sout("// both in....just copy....");
							if (outlist_n == size) 
							{
								temp = new int [size + 5][4];
								sout(temp.length);
								System.arraycopy(outlist_Polygon,0,temp,0,size);
								outlist_Polygon = temp;
								
								temp=new int[size+5][2];
								System.arraycopy(outputTexCoord,0,temp,0,size);
								outputTexCoord = temp;
								
								size += 5;
							}
							//                        sout("size: "+size+", outlist_n: "+outlist_n);
							outlist_Polygon[outlist_n++] = inlist_Polygon[curr];
							
							outputTexCoord[outlist_n-1]=tempTexCoorPointer[curr];//
						} else 
						{
							// current is in, but previous is out....
							// sout("// current is in, but previous is out...."+outlist_n+" "+(size - 2));
							if (outlist_n > size - 2) 
							{
								temp = new int [size + 5][4];
								System.arraycopy(outlist_Polygon,0,temp,0,size);
								outlist_Polygon = temp;
								
								temp=new int[size+5][2];
								System.arraycopy(outputTexCoord,0,temp,0,size);
								outputTexCoord = temp;
								
								size += 5;
								
								
							}
							outlist_Polygon[outlist_n++]=inter_point(inlist_Polygon[prev],
							inlist_Polygon[curr],
							i,j);
							outlist_Polygon[outlist_n++]=inlist_Polygon[curr];
							
							outputTexCoord[outlist_n-2]=inter_tex(tempTexCoorPointer[prev],
							tempTexCoorPointer[curr]);//
							outputTexCoord[outlist_n-1]=tempTexCoorPointer[curr];
						}
					} else
					{
						if (IsInside(inlist_Polygon[prev],i,j)) 
						{
							// current is out, but previous is in....
							// sout("// current is out, but previous is in...."+outlist_n+" "+size);
							if (outlist_n == size)
							{
								temp = new int [size + 5][4];
								System.arraycopy(outlist_Polygon,0,temp,0,size);
								outlist_Polygon = temp;
								
								temp=new int[size+5][2];
								System.arraycopy(outputTexCoord,0,temp,0,size);
								outputTexCoord = temp;
								
								size += 5;
							}
							outlist_Polygon[outlist_n++]=inter_point(inlist_Polygon[curr],
							inlist_Polygon[prev],
							i,j);
							
							outputTexCoord[outlist_n-1]=inter_tex(tempTexCoorPointer[curr],
							tempTexCoorPointer[prev]);//
						}else
						{
							// sout("// else, both out, do nothing....");// else, both out, do nothing....
						}
					}
					prev = curr;
				}
				// sout("clippolygon  outlist_Polygon.length="+outlist_Polygon.length+" outlist_n="+outlist_n);
		
				tempVertexPointer=outlist_Polygon;
				tempTexCoorPointer=outputTexCoord;
			}
		}
		
		return outlist_n;
	}
	
	
	private void fillPolygon(int totalPoint)
	{

		boolean facing;
		int Top, Mid, Down;
		int area;
		int ymax,ymin;
		// Test the position of the three points....
		if (tempVertexPointer [0][1] < tempVertexPointer [1][1])
		{
			if (tempVertexPointer [1][1] < tempVertexPointer [2][1]) 
			{
				Top = 0; Mid = 1; Down = 2;
				facing = false;
			} else
			{
				Down = 1;
				if (tempVertexPointer [0][1] < tempVertexPointer [2][1])
				{
					Top = 0; Mid = 2; 
					facing = true;
				} else 
				{
					Top = 2; Mid = 0;
					facing = false;
				}
			}
		} else 
		{
			if (tempVertexPointer [0][1] < tempVertexPointer [2][1]) 
			{
				Top = 1; Mid = 0; Down = 2;
				facing = true;
			} else 
			{
				Down = 0;
				if (tempVertexPointer [1][1] < tempVertexPointer [2][1]) 
				{
					Top = 1; Mid = 2;
					facing = false;
				} else 
				{
					Top = 2; Mid = 1;
					facing = true;
				}
			}
		}
		
		
		area = FPMUL(tempVertexPointer [Down][0] - tempVertexPointer [Top][0] ,
			tempVertexPointer [Mid][1] - tempVertexPointer [Top][1]) - 
		FPMUL(tempVertexPointer [Mid][0] - tempVertexPointer [Top][0], 
			tempVertexPointer [Down][1] - tempVertexPointer [Top][1]);
		
		if (area == FP_0)return;
		//FrontFace default is GL_CCW
		facing = facing ^ (area > 0) ^ false;//(CC.Raster.FrontFace == GL.GL_CW);
		// if(!facing)continue;
			
		if (cullFaceEnable) 
		{
			// if (cullFaceMode == GL_FRONT_AND_BACK) return;
			if (!facing & (cullFaceMode ==GL_BACK)) return;
			if (facing & (cullFaceMode == GL_FRONT)) return;
			
		}  //*/
			
			
			
		ymin=tempVertexPointer[Top][1];
		ymax=tempVertexPointer[Down][1];
		
		for (int lo=3;lo<totalPoint;lo++)
		{
			if(tempVertexPointer[lo][1]>ymax)
			{
				ymax=(tempVertexPointer[lo][1]);
			}else
			if(tempVertexPointer[lo][1]<ymin)
			{
				ymin=(tempVertexPointer[lo][1]);
			}
		}
		
		triangleDeltaY=TOINT(ymin);
		ymax=TOINT(ymax)-triangleDeltaY+1;
		
		// sout("maxY="+ymax+" minY="+ymin);
		triangleScanlineMinMaxArrays=new int[ymax][];
		triangleScanlineMinMaxArraysD=new int[ymax][];
		triangleScanlineMinMaxArraysT=new int[ymax][];
		// triangleDeltaY=ymin;
		sout("fillTriangle step1");
		
		//bresenham differs between p1p2 and p2p1, force it draw from p1 to p2
		for (int lo=0;lo<totalPoint;lo++)
		{
			//int tx1,tx2,ty1,ty2,tz1,tz2;
			int /*idx0,*/idx1;
			/* tx1=tempVertexPointer[lo][0];
			ty1=tempVertexPointer[lo][1];
			tz1=tempVertexPointer[lo][2];
			idx0=lo;
			if(lo+1<totalPoint)
			{
				tx2=tempVertexPointer[lo+1][0];
				ty2=tempVertexPointer[lo+1][1];
				tz2=tempVertexPointer[lo+1][2];
				idx1=lo+1;
			}else
			{
				tx2=tempVertexPointer[0][0];
				ty2=tempVertexPointer[0][1];
				tz2=tempVertexPointer[0][2];
				idx1=0;
			}
			 */
			idx1=(lo+1<totalPoint)?(lo+1):0;
			
			if(tempVertexPointer[lo][0]<tempVertexPointer[idx1][0])
			{
				calculateLine(lo,idx1);
			}else
			{
				calculateLine(idx1,lo);
			}
		}
		
		// sout("fillTriangle step2 aChu="+aChu+" bChu="+bChu);
		
		for (int l=0;l<ymax/*triangleScanlineMinMaxArrays.length*/;l++)
		{
			if(triangleScanlineMinMaxArrays[l]!=null)
			{
				sout("fillPolygon 0");
				int y=triangleDeltaY+l;
				
				if(y<0 || y>=vph)continue;//keep y cooridnate inside the screen buffer
				
				int startx,endx;
				int startz,endz;
				int startu, startv,endu,endv;
				
				if(triangleScanlineMinMaxArrays[l][0]<triangleScanlineMinMaxArrays[l][1])
				{
					startx=	triangleScanlineMinMaxArrays[l][0];
					endx = triangleScanlineMinMaxArrays[l][1];
					startz= triangleScanlineMinMaxArraysD[l][0];
					endz = triangleScanlineMinMaxArraysD[l][1];
					
					startu = triangleScanlineMinMaxArraysT[l][0];
					startv = triangleScanlineMinMaxArraysT[l][1];
					endu = triangleScanlineMinMaxArraysT[l][2];
					endv = triangleScanlineMinMaxArraysT[l][3];
				}else
				{
					startx=	triangleScanlineMinMaxArrays[l][1];
					endx = triangleScanlineMinMaxArrays[l][0];
					startz= triangleScanlineMinMaxArraysD[l][1];
					endz = triangleScanlineMinMaxArraysD[l][0];
					
					startu = triangleScanlineMinMaxArraysT[l][2];
					startv = triangleScanlineMinMaxArraysT[l][3];
					endu = triangleScanlineMinMaxArraysT[l][0];
					endv = triangleScanlineMinMaxArraysT[l][1];
				}
				
				int dz=0;
				int du=0;
				int dv=0;
				
				if(endx!=startx)
				{
					dz=(endz-startz)/(endx-startx);
					du=(endu-startu)/(endx-startx);
					dv=(endv-startv)/(endx-startx);
				}
				
				for(int m=startx;m<=endx;m++)
				{
					int z;
					
					sout("fillPolygon 0.9");
					// z=((m-x1)*deltaZperX) + ((y-y1)*deltaZperY) + z1;//depth test
					
					// z=startz;
					
					z=(m-startx)*dz+startz;
					
					sout("fillPolygon 1.0 m="+m+" y="+y);
	/* 				if(y>=120 || m>=120)
					{
						continue;//TODO
					} */
					if(z<depthBuffer[m+y*vpw] || !depthTestEnable)
					{
						sout("fillPolygon 1.1");
						
						depthBuffer[ m+ y*vpw ] =z;
						// setPexelDepth(m,y,z);
						sout("fillPolygon 1.2");
						if(texture2DEnable)
						{
							
							//m is x
							// int ui,vi;
							COLOR_TYPE color;
							int tempu=(m-startx)*du+startu;
							int tempv=(m-startx)*dv+startv;
							/*
							int fp_u,fp_v;

							if(endx!=startx)
							{
								fp_u=(m-startx)*(endu-startu)/(endx-startx)+startu;
								fp_v=(m-startx)*(endv-startv)/(endx-startx)+startv;
							}else
							{
								fp_u=startu;
								fp_v=startv;
							} 
							*/
							
							// sout2(" fp_v2_v1="+fp_v2_v1+" fp_v3_v1="+fp_v3_v1+" fp_v1="+fp_v1);
							tempu=textureTestBetween0to1(tempu);
							tempv=textureTestBetween0to1(tempv);
							// sout2(" fp_u="+fp_u+" fp_v="+fp_v);
							tempu=TOINT(FPMUL(fp_currentTextureWidth_1,tempu));
							tempv=TOINT(FPMUL(fp_currentTextureHeight_1,tempv));
							// sout2(" bBeiChu="+bBeiChu+" aBeiChu="+aBeiChu+" achu="+aChu);
							// sout(" ui="+ui+" vi="+vi);
							sout("fillPolygon 2");
							color=texelBuffer[currentTexture][tempu+tempv*currentTextureWidth];
#if COLOR_BUFFER_TYPE == 1

							// if((color&0xf000)==0xf000)
							// {
							colorBuffer[ m+ y*vpw ] =(short)(color&0x0fff);
								// setPexelColor(m,y,(short)(color&0x0fff));
							// }
#else

							//if((color&0xff000000)==0xff000000)
							//{
							colorBuffer[ m+ y*vpw ] =(color&0x00ffffff);
								// setPexelColor(m,y,(color&0x00ffffff));
							//}
#endif
						}else
						{
							colorBuffer[ m+ y*vpw ] = currentColor;
							// setPexelColor(m,y,currentColor);
						}
					}
				}
			}
		}
	}
}
