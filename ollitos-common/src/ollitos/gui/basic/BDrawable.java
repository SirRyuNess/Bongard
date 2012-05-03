package ollitos.gui.basic;

import ollitos.animation.IBTransformAnimable;
import ollitos.geom.IBPoint;
import ollitos.geom.IBTransform;
import ollitos.platform.BCanvasContext;
import ollitos.platform.BPlatform;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBCanvas.CanvasContext;



public abstract class BDrawable implements IBDrawable, IBTransformAnimable, IBCanvas.CanvasContextProvider{

	protected IBTransform _t = BPlatform.instance().identityTransform();
	protected IBTransform _tt;
	private boolean _visible = true;
	private BCanvasContext _canvasContext = (BCanvasContext) BPlatform.instance().canvasContext();
	
	@Override
	public BCanvasContext canvasContext() {
		return (BCanvasContext) _canvasContext;
	}
	
	public boolean visible(){
		return _visible;
	}
	
	public void setVisible(boolean b){
		_visible = b;
	}
	
	@Override
	public IBTransform temporaryTransform(){
		return _tt;
	}
	
	@Override
	public void setTemporaryTransform(IBTransform tt){
		_tt = tt;
	}

	
	@Override
	public IBTransform transform() {
		return _t;
	}

	@Override
	public void setTransform(IBTransform t) {
		_t = t.copy();
	}

	/**
	 * 
	 */
	@Override
	public void scale(double x, double y){
		_t.scale(x, y);
	}
	
	/**
	 * 
	 */
	@Override
	public void rotate(double a){
		_t.rotate(a);
	}
	
	/**
	 * 
	 */
	@Override
	public void translate(double x, double y){
		_t.translate(x, y);
	}
	
	/**
	 * 
	 */
	@Override
	public void concatenate( IBTransform t ){
		_t.concatenate(t);
	}
	
	@Override
	public void preConcatenate( IBTransform t ){
		_t.preConcatenate(t);
	}
	

	@Override
	public void applyTemporaryTransform(){
		IBTransform tt = temporaryTransform();
		if( tt != null ){
			_t.concatenate(tt);
		}
		setTemporaryTransform(null);
	}
	
	public IBTransform transformWithTemporary(){
		IBTransform t = transform();
		
		IBTransform tt = temporaryTransform();
		if( tt != null ){
			IBTransform temp = BPlatform.instance().identityTransform();
			temp.concatenate(t);
			temp.concatenate(tt);
			t = temp;
		}
		return t;
	}

	@Override
	public void draw(IBCanvas c, IBTransform aditionalTransform ){
		if( !visible() ){
			return;
		}
		
		IBTransform t = transformWithTemporary();
		
		if( aditionalTransform != null ){
			IBTransform temp = BPlatform.instance().identityTransform();
			temp.concatenate(aditionalTransform);
			temp.concatenate(t);
			t = temp;
		}
		
		_canvasContext.setTransform(t);
		draw_internal(c);
	}


	@Override
	public IBPoint position(){
		IBPoint ret = BPlatform.instance().point(0, 0);
		ret = transform().transform(ret);
		return ret;
	}

	public IBPoint temporaryPosition(){
		IBPoint ret = BPlatform.instance().point(0, 0);
		ret = transformWithTemporary().transform(ret);
		return ret;
	}

	/**
	 * Draw ignoring the internal transform, only the given transform 
	 * @param c
	 */
	protected abstract void draw_internal(IBCanvas c);
	
}