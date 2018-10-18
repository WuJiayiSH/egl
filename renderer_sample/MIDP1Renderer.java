


import javax.microedition.lcdui.*;
import com.syjay.egl.*;




class MIDP1Renderer extends Canvas implements Renderer
{
	
	Graphics gc		;		
	

	
	public void paint(Graphics g)
	{
		this.gc=g;
		
	}
	
	public void render(int[] buffer, int x, int y, int w, int h)
	{
		int tx,ty;
		for (ty=0;ty<h;ty++)
		{
			for (tx=0;tx<w;tx++)
			{
				gc.setColor(  buffer[  tx+ty*w  ]);
				gc.drawLine(x+tx,y+ty,x+tx,y+ty);
				
			}
		}
		
		// gc.setColor(0x00ff00);
		// gc.drawString("dummy rendering...",dx,dy,0); 
	}
}
