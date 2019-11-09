package main.model;

import object.visualparadigm.Shape;

public class ExtendedShape {
	private Shape shape;
	private boolean isStartNode;
	
	public Shape getShape() {
		return shape;
	}
	public void setShape(Shape shape) {
		this.shape = shape;
	}
	public boolean isStartNode() {
		return isStartNode;
	}
	public void setStartNode(boolean isStartNode) {
		this.isStartNode = isStartNode;
	}
	
}
