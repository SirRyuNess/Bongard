package ollitos.animation;



public interface IBAnimation {
	void stepAnimation(long millis);
	IBAnimable[] animables();
	void setAnimables(IBAnimable ... a);
	boolean endReached();
	boolean needsUpdate();
}
