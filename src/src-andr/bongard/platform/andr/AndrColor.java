package bongard.platform.andr;

import bongard.gui.basic.IBColor;

public class AndrColor implements IBColor {

	private int _color;
	
	public AndrColor( int c ){
		_color = c;
	}
	
	public int color(){
		return _color;
	}
}