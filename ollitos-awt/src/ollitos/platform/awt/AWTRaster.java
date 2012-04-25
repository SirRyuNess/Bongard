package ollitos.platform.awt;

import java.awt.Image;

import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.platform.IBRaster;


public class AWTRaster implements IBRaster{
	private Image _image;


	public AWTRaster( Image i ){
		_image = i;
	}
	
	/**
	 * 
	 * @return
	 */
	public Image image(){
		return _image;
	}


	@Override
	public void setUp() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void dispose() {
		_image = null;
	}

	@Override
	public boolean disposed() {
		return _image == null;
	}

	@Override
	public int w() {
		return _image.getWidth(null);
	}

	@Override
	public int h() {
		return _image.getHeight(null);
	}

}