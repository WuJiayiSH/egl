


import com.nokia.mid.ui.*;
import com.syjay.egl.*;




class Nokia40Renderer extends FullCanvas implements Renderer
{
	
	Graphics gc		;		
	

	
	public void paint(Graphics g)
	{
		this.gc=g;
		
	}
	
	public void render(int[] buffer, int x, int y, int w, int h)
	{
		
		short tmp[]=new short[buffer.length];
		int cr,cg,cb;
		for(int lo=0;lo<buffer.length;lo++)
		{
			cr=((buffer[lo]&0xf00000)>>12);
			cg=((buffer[lo]&0x00f000)>>8);
			cb=((buffer[lo]&0x0000f0)>>4);
			tmp[lo]=(short)(cr+cg+cb);
			
		}
		
		com.nokia.mid.ui.DirectGraphics g=com.nokia.mid.ui.DirectUtils.getDirectGraphics(gc);
		try
		{
			g.drawPixels(tmp,false,0,w,x,y,w,h,0,444);
		}catch(Exception ex)
		{
			
		} 
		
	}
}
