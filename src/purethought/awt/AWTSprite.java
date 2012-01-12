package purethought.awt;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import purethought.geom.IBTransform;
import purethought.gui.BSprite;
import purethought.gui.IBCanvas;
import purethought.gui.IBRaster;

public class AWTSprite extends BSprite{

	
	public AWTSprite(IBRaster raster) {
		super(raster);
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		AWTCanvas canvas = (AWTCanvas) c;
		
		
		Graphics2D g2d = canvas.getGraphics();
		g2d.transform((AffineTransform) t);
		
		Image img = raster().getImpl(Image.class);
		
		int x = -img.getWidth(null)/2;
		int y = -img.getHeight(null)/2;
		
		g2d.drawImage(img, x, y, null);
		g2d.dispose();
	}
}
