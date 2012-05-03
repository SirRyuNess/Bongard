package ollitos.animation;

import ollitos.geom.IBPoint;
import ollitos.geom.IBTransform;

public interface IBTransformAnimable extends IBAnimable{
	IBTransform temporaryTransform();
	void setTemporaryTransform(IBTransform tt);
	void applyTemporaryTransform();
	IBPoint position();
}