package ollitos.platform.awt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import ollitos.geom.IBTransform;
import ollitos.gui.basic.BLabel;
import ollitos.gui.basic.IBCanvas;



public class AWTLabel extends BLabel {

	public AWTLabel(String text) {
		super(text);
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		AWTCanvas canvas = (AWTCanvas) c;
		
		Graphics2D g2d = canvas.getGraphics();
		g2d.transform((AffineTransform) t);
		
		g2d.setColor( Color.white );

		g2d.drawString( text(), 0, 0);
		
		g2d.dispose();
	}

}