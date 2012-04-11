package ollitos.gui.basic;

import ollitos.geom.BRectangle;
import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.platform.BFactory;

public abstract class BRectangularDrawable extends BDrawable implements IBRectangularDrawable{
	
	private IBRectangle _originalSize;

	public BRectangularDrawable(IBRectangle r) {
		setOriginalSize(r);
	}

	public void setOriginalSize(IBRectangle r) {
		_originalSize = r;
	}

	@Override
	public final boolean inside(IBPoint p, IBTransform aditionalTransform) {
		IBTransform t = transform();
		if( aditionalTransform != null ){
			IBTransform tt = BFactory.instance().identityTransform();
			tt.concatenate(t);
			tt.concatenate(aditionalTransform);
			t = tt;
		}
		
		IBTransform inverseT = t.inverse();
		
		IBPoint inverseP = inverseT.transform(p);		
		
		return BRectangle.inside( originalSize(), inverseP);
	}

	public IBRectangle originalSize(){
		return _originalSize;
	}


}