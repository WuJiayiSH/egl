


import com.nttdocomo.ui.*;
import com.syjay.egl.*;




class DOJA3Renderer extends Canvas implements Renderer
{
	
	Graphics gc	=getGraphics() 	;		
	

	

	
	public void render(int[] buffer, int x, int y, int w, int h)
	{
		int tx,ty,color,cr,cg,cb;
		for (ty=0;ty<h;ty++)
		{
			for (tx=0;tx<w;tx++)
			{
				color= buffer[  tx+ty*w  ];
				cr=(color&0xff0000)>>16;
				cg=(color&0x00ff00)>>8;
				cb=(color&0x0000ff)>>0;
				color=gc.getColorOfRGB(cr,cg,cb);
				gc.setColor(  color);
				gc.drawLine(x+tx,y+ty,x+tx,y+ty);
				
			}
		}
	}
}
