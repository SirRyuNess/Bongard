package purethought.gui;

import purethought.geom.IBPoint;
import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.problem.BCardExtractor;
import purethought.problem.BProblem;
import purethought.problem.BProblemLocator;
import purethought.util.BFactory;

public class BBongardTestField extends BTopDrawable implements IBFlippableDrawable{

	private BProblem _problem;
	private BSprite _sprite;
	private BFlippableContainer _container;
	
	private IBCanvasListener _listener = new IBCanvasListener(){

		@Override
		public void pointerClick(IBPoint p) {
		}

		@Override
		public void pointerDown(IBPoint p) {
		}

		@Override
		public void pointerDrag(IBPoint p) {
		}

		@Override
		public void pointerUp(IBPoint p) {
		}

		@Override
		public void resized() {
		}

		@Override
		public void zoomIn(IBPoint p) {
		}

		@Override
		public void zoomOut(IBPoint p) {
			_container.flipUp();
		}
	};
	
	/**
	 * 
	 * @param test
	 */
	public void setProblem( BProblemLocator test ){
		BFactory f = BFactory.instance();
		_problem = BCardExtractor.extract(test);
		
		_sprite = f.sprite(_problem.testImage());
		_sprite.translate( originalSize().w()/2, originalSize().h()/2 );
	}

	@Override
	public IBRectangle originalSize() {
		return _sprite.raster().originalSize();
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		_sprite.draw(c, t);
	}

	@Override
	public BFlippableContainer flippableContainer() {
		return _container;
	}

	@Override
	public void hided() {
	}

	@Override
	public void setFlippableContainer(BFlippableContainer c) {
		_container = c;
		
	}

	@Override
	public void showed() {
	}
	
	@Override
	public void addedTo(IBCanvas c) {
		if( canvas() != null ){
			canvas().removeListener(_listener);
		}
		super.addedTo(c);
		if( canvas() != null ){
			canvas().addListener(_listener);
		}
	}
	

}
