


import com.nokia.mid.ui.*;
import com.syjay.egl.*;




class Nokia60Renderer extends FullCanvas implements Renderer
{
	
	Graphics gc		;		
	

	
	public void paint(Graphics g)
	{
		this.gc=g;
		
	}
	
	public void render(int[] buffer, int x, int y, int w, int h)
	{
		
		com.nokia.mid.ui.DirectGraphics g=com.nokia.mid.ui.DirectUtils.getDirectGraphics(gc);
		try
		{
			g.drawPixels(buffer,false,0,w,x,y,w,h,0,888);
		}catch(Exception ex)
		{
			
		} 
	}
}
