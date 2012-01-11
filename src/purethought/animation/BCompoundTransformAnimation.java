package purethought.animation;

import purethought.gui.IBTransform;
import purethought.util.BFactory;

public class BCompoundTransformAnimation implements IBAnimation {

	private IBTransformAnimable[] _a;
	private BTransformAnimation[] _animations;
	private boolean _aborted;

	public BCompoundTransformAnimation( IBTransformAnimable[] a, BTransformAnimation ... animations ){
		_a = a;
		_animations = animations;
	}
	
	@Override
	public void abortAnimation() {
		_aborted = true;
		for (BTransformAnimation a : _animations) {
			a.abortAnimation();
		}
	}

	@Override
	public boolean aborted() {
		return _aborted;
	}

	@Override
	public IBAnimable[] animables() {
		return _a;
	}

	@Override
	public void applyAnimation() {
		for( IBAnimable a: animables() ){
			IBTransformAnimable ta = (IBTransformAnimable) a;
			ta.applyAnimation(this);
		}
	}

	@Override
	public boolean endReached() {
		for (BTransformAnimation a : _animations) {
			if( !a.endReached() ){
				return false;
			}
		}
		return true;
	}

	@Override
	public void stepAnimation(long millis) {
		IBTransform t = BFactory.instance().identityTransform();
		for (BTransformAnimation a : _animations) {
			if( !a.endReached() ){
				IBTransform st = a.stepTransform(millis);
				t.concatenate(st);
			}
		}
		for( IBAnimable a: animables() ){
			IBTransformAnimable ta = (IBTransformAnimable) a;
			ta.setTemporaryTransform(t);
		}
	}
}
