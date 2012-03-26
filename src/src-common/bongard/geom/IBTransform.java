package bongard.geom;

public interface IBTransform extends IBTransformable{
	public IBPoint transform(IBPoint p);
	public IBTransform toIdentity();
	public IBTransform inverse();
	public IBTransform copy();
}