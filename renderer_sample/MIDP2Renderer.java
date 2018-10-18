


import javax.microedition.lcdui.*;
import com.syjay.egl.*;




class MIDP2Renderer extends Canvas implements Renderer
{
	
	Graphics gc		;		
	

	
	public void paint(Graphics g)
	{
		this.gc=g;
		
	}
	
	public void render(int[] buffer, int x, int y, int w, int h)
	{
		
		gc.drawRGB(buffer,0,w,x,y,w,h,false);
	}
}
