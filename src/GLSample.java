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
#include "Setting.java"

#define KEY_FIRE 	0
#define KEY_UP 	1
#define KEY_DOWN	2
#define KEY_LEFT	3
#define KEY_RIGHT	4
#define KEY_EXIT	5

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.io.*;

#ifdef FULL_CANVAS
import com.nokia.mid.ui.*;
#endif

import com.syjay.egl.*;

public class GLSample extends MIDlet implements Runnable{
	
	private GameCanvas cvs;
	public static GLSample midlet;
	public static final int sleepTime=100;
	public static boolean liveFlag=true;
	
	private long lastRepaint=0;
	
	public GLSample()
	{
		midlet=this;
	}
	
	public void startApp() 
	{
		//System.out.println("free memory="+Runtime.getRuntime().freeMemory());
		cvs=new GameCanvas();
		Display.getDisplay(this).setCurrent(cvs);
		//        new Thread(this).start();
		new Thread(this).start();
	}
	
	public void pauseApp() 
	{
	}
	
	public void destroyApp(boolean unconditional) 
	{
	}
	
	public void run()
	{
		while(liveFlag)
		{
			lastRepaint=System.currentTimeMillis();
			cvs.repaint();
			cvs.serviceRepaints();
			cvs.setOutput(String.valueOf(System.currentTimeMillis()-lastRepaint));
			/* try 
			{
				long costTime=System.currentTimeMillis()-lastRepaint;
				if(sleepTime-costTime>30)
				{
					Thread.sleep(sleepTime-costTime);
				}else
				{
					Thread.sleep(30);
				}
			} catch (InterruptedException ex) 
			{
				ex.printStackTrace();
			}
			lastRepaint=System.currentTimeMillis(); */
		}
		
		destroyApp(true);
		notifyDestroyed();
	}
}


class GameCanvas extends 
#ifdef FULL_CANVAS
FullCanvas 
#else
Canvas
#endif
implements Renderer
{
	public static final byte stateLogo		=0;
	public static final byte stateTitle		=1;
	public static final byte statePyramid	=2;
	public static final byte stateTexPyramid	=3;
	public static final byte stateW3Model	=4;
	public static byte mainState		=stateLogo;
	public static int subState		=0;
	
	// public static final byte stateTitle=0;
	
	public Graphics gc;
	
	private Image img[]=new Image[1];
	
	private int dx,dy;
	
	private boolean keys[]=new boolean[6];
	
	private byte choice=0;
	
	private GL gl;
	private GLU glu;
	private boolean GLInitFlag=false;
	
	private int vp[];
	private int uv[];
	private int elements[];
	private int colors[];
	private int tex[]=new int[1];
	
	private int xrot=0;
	private Class _class;
	String output="";
	public GameCanvas()
	{
		dx=(getWidth()-CSIZE)/2;
		dx=dx>0?dx:0;
		dy=(getHeight()-CSIZE)/2;
		dy=dy>0?dy:0;
		_class=getClass();
		
		#ifdef FULL2
		setFullScreenMode(true);
		#endif
	}
	
	
	
	public void setOutput(String output)
	{
		this.output=output;
	}
	
	public void paint(Graphics g)
	{
		gc=g;
		
		// SOUT(mainState);
		
		switch(mainState)
		{
			case stateLogo:
				logo();
				break;
			case stateTitle:
				title();
				break;
			case statePyramid:
				pyramid();
				break;
			case stateTexPyramid:
				texPyramid();
				break;
			case stateW3Model:
				w3Model();
				break;
		}
		
		for(int lo=0;lo<keys.length;lo++)
		{
			keys[lo]=false;
		}
		
		// gc.setColor(0xff0000);
		// gc.drawString(output,0,30,0);
	}
	
	private void logo()
	{
		if(subState==0)
		{
			img[0]=createImage("/0.png");
		}
		
		gc.setColor(0xffffff);
		gc.fillRect(0,0,320,320);
		gc.drawImage(img[0],dx+CSIZE/2, dy+CSIZE/2,gc.VCENTER|gc.HCENTER);
		
		subState++;
		
		if(subState==1)
		{
			mainState=stateTitle;
			img[0]=null;
			subState=0;
			GC();
		}
	}
	
	private void title()
	{
		if(subState==0)
		{
			img[0]=createImage("/1.png");
		}
		
		gc.setColor(0x000000);
		gc.fillRect(0,0,320,320);
		gc.drawImage(img[0],dx+SCALE(10), dy+SCALE(10),0);
		gc.setColor(0x00ffff);
		gc.drawString(">",dx+SCALE(10), dy+SCALE(70)+choice*SCALE(16),0);
		gc.setColor(0xffffff);
		gc.drawString("pyramid",dx+SCALE(20), dy+SCALE(70),0);
		gc.drawString("textured pyramid",dx+SCALE(20), dy+SCALE(70)+1*SCALE(16),0);
		gc.drawString("warcraft model",dx+SCALE(20), dy+SCALE(70)+2*SCALE(16),0);
		// gc.drawString("exit",dx+SCALE(20), dy+SCALE(60)+3*SCALE(16),0);
		if(keys[KEY_DOWN])
		{
			choice++;
			choice=choice==3?0:choice;
		}
		
		if(keys[KEY_UP])
		{
			choice--;
			choice=choice==-1?2:choice;
		}
		
		if(keys[KEY_EXIT])
		{
			GLSample.liveFlag=false;
		}
		
		if(keys[KEY_FIRE])
		{
			switch(choice)
			{
				case 0:
				mainState=statePyramid;
				img[0]=null;
				subState=0;
				GC();
				break;
				
				case 1:
				mainState=stateTexPyramid;
				img[0]=null;
				subState=0;
				GC();
				break;
				
				case 2:
				mainState=stateW3Model;
				img[0]=null;
				subState=0;
				GC();
				break;
				
				// case 3:
				// GLSample.liveFlag=false;
				// break;
			}
			
		}
		subState++;
	}
	
	private void pyramid()
	{
		switch(subState)
		
		{
			case 0:
			drawLoading(0);break;
			
			case 1:
			GLInit();
			drawLoading(50);break;
		
			case 2:
			initPyramid();
			gl.glDisable(gl.GL_TEXTURE_2D);
			drawLoading(100);break;
			
			default:
			gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);	
			gl.glLoadIdentity();	
			gl.glTranslatef(0,0,MathFP.toFP(-10));	
			gl.glRotatef(xrot,0,MathFP.toFP(1),0); 
			gl.glVertexPointer(3,gl.GL_FIXED,0,vp);
			//gl.glTexCoordPointer(2,gl.GL_FIXED,0,uv);
			gl.glColorPointer(3,gl.GL_FIXED,0,colors);
			gl.glDrawElements(gl.GL_TRIANGLES,12,0,elements);
			gl.glFlush();
			
			xrot-=MathFP.toFP(3);
			
			if(keys[KEY_FIRE])
			{
				mainState=stateTitle;
				// img[0]=null;
				subState=0;
				GC();
				return;
			}
			
			break;
		}
		subState++;
		
		
	}
	
	private void texPyramid()
	{
		switch(subState)
		
		{
			case 0:
			drawLoading(0);break;
			
			case 1:
			GLInit();
			drawLoading(33);break;
		
			case 2:
			initPyramid();
			drawLoading(66);break;
			
			case 3:
			createTexture("/wall.bmp");
			drawLoading(100);break;
			
			default:
			gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);	
			gl.glLoadIdentity();	
			gl.glTranslatef(0,0,MathFP.toFP(-8));	
			gl.glRotatef(xrot,0,MathFP.toFP(1),0); 
			gl.glVertexPointer(3,gl.GL_FIXED,0,vp);
			gl.glTexCoordPointer(2,gl.GL_FIXED,0,uv);
			// gl.glColorPointer(3,gl.GL_FIXED,0,colors);
			gl.glDrawElements(gl.GL_TRIANGLES,12,0,elements);
			gl.glFlush();
			
			xrot-=MathFP.toFP(3);
			
			if(keys[KEY_FIRE])
			{
				mainState=stateTitle;
				//img[0]=null;
				subState=0;
				gl.glDeleteTextures(1,tex);
				GC();
				return;
			}	
			break;
		}
		subState++;
		
		
	}
	
	private void w3Model()
	{
		switch(subState)
		
		{
			case 0:
			drawLoading(0);break;
			
			case 1:
			GLInit();
			drawLoading(25);break;
		
			case 2:
			//initPyramid();
			drawLoading(50);break;
			
			case 3:
			createTexture("/bull.bmp");
			drawLoading(75);break;
			
			case 4:
			loadMds("/bull.mds");
			drawLoading(100);break;
			
			default:
			gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);	
			gl.glLoadIdentity();	
			//ren ma
			gl.glTranslatef(0,-16000,MathFP.toFP(-3));
			//gl.glRotatef(MathFP.toFP(-90),60000,0,0); 
			gl.glRotatef(xrot,0,MathFP.toFP(1),0); 
			// gl.glRotatef(MathFP.toFP(-90),MathFP.toFP(1),0,0);
			/* shizhi
			gl.glTranslatef(0,MathFP.toFP(-1),MathFP.toFP(-6));
			//gl.glRotatef(MathFP.toFP(-90),60000,0,0); 
			
			gl.glRotatef(xrot,0,MathFP.toFP(1),0);  */
			// gl.glScalef(111,-111,111);
			gl.glVertexPointer(3,gl.GL_FIXED,0,vp);
			gl.glTexCoordPointer(2,gl.GL_FIXED,0,uv);
			gl.glDrawElements(gl.GL_TRIANGLES,elements.length,0,elements);
			gl.glFlush();
			
			xrot-=MathFP.toFP(3);
			
			if(keys[KEY_FIRE])
			{
				mainState=stateTitle;
				//img[0]=null;
				subState=0;
				gl.glDeleteTextures(1,tex);
				GC();
				return;
			}	
			break;
		}
		subState++;
		
		
	}
	
	private void GLInit()
	{
		if(!GLInitFlag)
		{
			gl=new GL(this);
			glu=new GLU(gl);
			
			// gl.glClearColor3f(0,0,0);	
			gl.glEnable(gl.GL_DEPTH_TEST);
			gl.glEnable(GL.GL_CULL_FACE);
			gl.glCullFace(GL.GL_BACK);
			resizeScene();
			GLInitFlag=true;
		}
	}
	
	public void resizeScene()
	{
		gl.glViewport(dx, dy, CSIZE, CSIZE);
		
		gl.glMatrixMode(gl.GL_PROJECTION);	
		gl.glLoadIdentity();	
		// System.out.println(w+", "+h);
		
		
		glu.gluPerspective(MathFP.toFP(45),
		MathFP.div(MathFP.toFP(CSIZE),MathFP.toFP(CSIZE))  ,
		8192 ,//0.125FP
		MathFP.toFP(1000)
		); 
		
		/* 	gl.frustum(MathFP.toFP(-10), MathFP.toFP(10),
		MathFP.toFP(-6), MathFP.toFP(6),
		MathFP.toFP(1), MathFP.toFP(100) 
		);*/
		gl.glMatrixMode(gl.GL_MODELVIEW);	
		gl.glLoadIdentity();	
	}
	
	private void initPyramid()
	{
		vp=null;
		uv=null;
		elements=null;
		colors=null;
		GC();
		vp	=new int []
		{
			MathFP.toFP(1),		MathFP.toFP(-1),	MathFP.toFP(1),
			MathFP.toFP(-1),	MathFP.toFP(-1),	MathFP.toFP(1),
			MathFP.toFP(-1),	MathFP.toFP(-1),	MathFP.toFP(-1),
			MathFP.toFP(1),		MathFP.toFP(-1),	MathFP.toFP(-1),
			0,			MathFP.toFP(1),		0
		};
		
		uv	=new int[]
		{	0,	0,
			4096*16,0,
			0,	0,
			4096*16,0,
			4096*8,	4096*8
		};
		elements	=new int[]{0,4,1,1,4,2,2,4,3,3,4,0};
		//int el[]	=new int[]{1,3,2};
		colors	=new int[]
		{	
			2048*16,	0,		0,
			0,		2048*16,	0,
			0,		0,		2048*16,   
			2048*8,		0,		2048*8,
			2048*8,		2048*8,		2048*8
		};
	}
	
	private void drawLoading(int step)
	{
		gc.setColor(0x000000);
		gc.fillRect(0,0,320,320);
		
		gc.setColor(0xffffff);
		gc.fillRect(dx+SCALE(12),dy+CSIZE/2-SCALE(12),CSIZE-SCALE(12)*2,SCALE(12)*2);
		
		gc.setColor(0x000000);
		gc.fillRect(dx+SCALE(14),dy+CSIZE/2-SCALE(10),CSIZE-SCALE(14)*2,SCALE(10)*2);
		
		gc.setColor(0x00ffff);
		gc.fillRect(dx+SCALE(16),dy+CSIZE/2-SCALE(8),(CSIZE-SCALE(16)*2)*step/100,SCALE(8)*2);
	}
	
	
	#ifdef N40II
	public void render(short[] buffer, int x, int y, int w, int h)
	{
		/* 		short tmp[]=new short[buffer.length];
		int cr,cg,cb;
		for(int lo=0;lo<buffer.length;lo++)
			{
		cr=((buffer[lo]&0xf00000)>>12);
		cg=((buffer[lo]&0x00f000)>>8);
		cb=((buffer[lo]&0x0000f0)>>4);
		tmp[lo]=(short)(cr+cg+cb);
		
		}
		*/
		com.nokia.mid.ui.DirectGraphics g=com.nokia.mid.ui.DirectUtils.getDirectGraphics(gc);
		try
		{
			g.drawPixels(buffer,false,0,w,x,y,w,h,0,444);
		}catch(Exception ex)
		{
			
		} 
	}
	#elif defined(FULL_CANVAS)
	public void render(int[] buffer, int x, int y, int w, int h)
	{
		com.nokia.mid.ui.DirectGraphics g=com.nokia.mid.ui.DirectUtils.getDirectGraphics(gc);
		
		g.drawPixels(buffer,false,0,w,x,y,w,h,0,888);
		
	}
	#else
	public void render(int[] buffer, int x, int y, int w, int h)
	{
		gc.drawRGB(buffer,0,w,x,y,w,h,false);
	}
	#endif

	
	private boolean createTexture(String src)
	{
		gl.glEnable(gl.GL_TEXTURE_2D);
		try
		{
			int width;
			int height;
			DataInputStream file = new DataInputStream(_class.getResourceAsStream(src));
			file.skip(18);
			width=(file.read()<<0)+(file.read()<<8)+(file.read()<<16)+(file.read()<<24);
			height=(file.read()<<0)+(file.read()<<8)+(file.read()<<16)+(file.read()<<24);
			System.out.println("createTexture "+src+" width="+(width)+" hieght="+(height));
			int plane=(file.read()<<0)+(file.read()<<8);//+(file.read()<<16)+(file.read()<<24);
			System.out.println("createTexture plane="+plane);
			int bpp=(file.read()<<0)+(file.read()<<8);
			System.out.println("createTexture bpp="+bpp);
			file.skip(24);
			int size=3*width*height;
			byte[] data=new byte[3*width*height];
			file.read(data);
			file.close();
			
			for (int i=0;i<size;i+=3) 
			{ // reverse all of the colors. (bgr -> rgb)
				byte temp = data[i];
				data[i] = data[i+2];
				data[i+2] = temp;
			}
			
			gl.glGenTextures(1, tex);
			gl.glBindTexture(gl.GL_TEXTURE_2D, tex[0]);   
			gl.glTexImage2D(gl.GL_TEXTURE_2D, 0, 3, width, height, 0,0,0, data);
		}
		catch(Exception x)
		{
			x.printStackTrace();
			return false;
		};
		return true;
	}
	
	private Image createImage(String src)
	{
		Image img=null;
		try
		{
			img=Image.createImage(src);
		}catch(Exception x)
		{
			
		}
		
		return img;
	}
	
	private boolean loadMds(String src)
	{
		try{
			DataInputStream mds = new DataInputStream(_class.getResourceAsStream(src));
			
			byte[] header=new byte[4];
			mds.read(header);
			if(header[0]!=0x6D || header[1]!=0x64 || header[2]!=0x73 || header[3]!=0)
			{
				System.out.println("loadMds invalid mds header !!!");
				return false;
			}
			
			int vplength=mds.readInt();
			vp=new int[vplength];
			for(int lo=0;lo<vplength;lo++)
			{
				vp[lo]=mds.readInt();
			//	vp[lo]=MathFP.div(vp[lo],MathFP.toFP(100));
			}
			
			int uvlength=mds.readInt();
			uv=new int[uvlength];
			for(int lo=0;lo<uvlength;lo++)
			{
				uv[lo]=mds.readInt();
			}
			
			int ellength=mds.readInt();
			int elbit=mds.read();
			elements=new int[ellength];
			if(elbit!=32)
			{
				System.out.println("loadMds invalid elements bit !!! "+elbit);
				return false;
			}
			for(int lo=0;lo<ellength;lo++)
			{
				elements[lo]=mds.readInt();
			}
			System.out.println("loadMds elements.length="+elements.length);
			mds.close();
			
			
		}
		catch(Exception x)
		{
			x.printStackTrace();
			return false;
		};
		return true;
	}
	
	public void keyPressed(int keyCode)
	{
		processKeys(keyCode,true);
		
	}
	
 	public void keyReleased(int keyCode)
	{
		processKeys(keyCode,false);
		
	}
	
	private void processKeys(int keyCode, boolean ifClick)
	{
		
		switch(keyCode)
		{
			case KEY_NUM2:
			keys[KEY_UP]=ifClick;
			break;
			
			case KEY_NUM8:
			keys[KEY_DOWN]=ifClick;
			break;
			
			case KEY_NUM4:
			keys[KEY_LEFT]=ifClick;
			break;
			
			case KEY_NUM6:
			keys[KEY_RIGHT]=ifClick;
			break;
			
			case KEY_NUM5:
			keys[KEY_FIRE]=ifClick;
			break;
			
			case KEY_STAR:
			keys[KEY_EXIT]=ifClick;
			break;
		}
		
		switch(getGameAction(keyCode))
		{
			case UP:
			keys[KEY_UP]=ifClick;
			break;
			
			case DOWN:
			keys[KEY_DOWN]=ifClick;
			break;
			
			case LEFT:
			keys[KEY_LEFT]=ifClick;
			break;
			
			case RIGHT:
			keys[KEY_RIGHT]=ifClick;
			break;
			
			case FIRE:
			keys[KEY_FIRE]=ifClick;
			break;
		
		}
	}
}
