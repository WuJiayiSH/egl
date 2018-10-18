

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

import com.syjay.egl.*;




class DesktopRenderer extends JPanel implements Renderer
{
	
	Graphics gc		;		
	


	public void paint(Graphics g)
	{
		this.gc=g;

	}
	
	public void render(int [] buffer, int x, int y, int w, int h)
	{
		int tx;
		int ty;
		if(gc!=null)
		{
			for (ty=0;ty<h;ty++)
			{
				for (tx=0;tx<w;tx++)
				{
					
					gc.setColor( new Color( buffer[  tx+ty*w  ]));
					gc.drawLine(x+tx,y+ty,x+tx,y+ty);
				}
			}
		}
	}
}
