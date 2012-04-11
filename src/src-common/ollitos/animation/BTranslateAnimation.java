package ollitos.animation;

import ollitos.geom.IBPoint;
import ollitos.geom.IBTransform;
import ollitos.platform.BFactory;

public class BTranslateAnimation extends BTransformAnimation{

	private IBPoint _dest;
	
	
	public BTranslateAnimation( IBPoint dest, int totalMillis, IBTransformAnimable ... a ){
		super(totalMillis,a);
		_dest = dest;
	}

	
	@Override
	public IBTransform getTransform(IBTransformAnimable a) {
		IBPoint origin = a.position();
		double tx = currentMillis()*(_dest.x() - origin.x())/totalMillis();
		double ty = currentMillis()*(_dest.y() - origin.y())/totalMillis();
		
		IBTransform ret = BFactory.instance().identityTransform();
		ret.translate(tx, ty);
		
		return ret;
	}


}