package purethought.animation;

import java.util.ArrayList;

public abstract class BAnimator {

	private ArrayList<IBAnimation> _animations = new ArrayList<IBAnimation>();

	public void addAnimation(IBAnimation a){
		_animations.add(a);
	}
	
	/**
	 * 
	 * @param millis
	 */
	public void stepAnimations(long millis){
		IBAnimation[] an = _animations.toArray( new IBAnimation[0] );
		for (IBAnimation a : an) {
			if( a.endReached() ){
				_animations.remove(a);
			}
			else{
				a.stepAnimation(millis);
			}
		}
	}
	
	public boolean needsUpdate(){
		for (IBAnimation a : _animations ) {
			if( a.needsUpdate() ){
				return true;
			}
		}
		return false;
	}
}
