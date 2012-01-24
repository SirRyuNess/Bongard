package purethought.gui.game;

import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.gui.basic.BSprite;
import purethought.gui.basic.IBCanvas;
import purethought.gui.container.BDrawableContainer;
import purethought.gui.container.BFlippableContainer;
import purethought.gui.container.IBFlippableDrawable;
import purethought.platform.BFactory;
import purethought.platform.BImageLocator;
import purethought.problem.BCardExtractor;
import purethought.problem.BProblem;

public class BBongardTestField extends BDrawableContainer implements IBFlippableDrawable{

	private BProblem _problem;
	private BSprite _sprite;
	private BFlippableContainer _container;
	
	public BBongardTestField(){
		this(null);
	}
	
	public BBongardTestField(BImageLocator l){
		setProblem(l);
	}

	
	/**
	 * 
	 * @param test
	 */
	public void setProblem( BImageLocator test ){
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
		super.draw_internal(c, t);
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
}
