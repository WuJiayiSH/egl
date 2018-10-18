


import com.nttdocomo.ui.*;
import com.syjay.egl.*;




class DOJA3Renderer extends Canvas implements Renderer
{
	
	Graphics gc	=getGraphics() 	;		
	

	

	
	public void render(int[] buffer, int x, int y, int w, int h)
	{
		gc.setRGBPixels( x,     y,   w,     h,buffer,0)
		
		// gc.setColor(0x00ff00);
		// gc.drawString("dummy rendering...",dx,dy,0); 
	}
}
