package main.model;

import java.util.List;

import object.visualparadigm.Shape;

public class EPCElement {
	private Shape shape;
	private boolean isStartNode;
	private boolean reqDummyTrans;
	private String operatorType;		//Split, Join
	private List<String> fromShapeId;
	private List<String> toShapeId;

	private CPNObject cpnObject;
	private List<String> incomeCPNId;
	private List<String> outcomeCPNId;
	
	private String initMark = "";
	
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
	public String getOperatorType() {
		return operatorType;
	}
	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}
	public CPNObject getCpnObject() {
		return cpnObject;
	}
	public void setCpnObject(CPNObject cpnObject) {
		this.cpnObject = cpnObject;
	}
	public boolean isReqDummyTrans() {
		return reqDummyTrans;
	}
	public void setReqDummyTrans(boolean reqDummyTrans) {
		this.reqDummyTrans = reqDummyTrans;
	}
	public List<String> getFromShapeId() {
		return fromShapeId;
	}
	public void setFromShapeId(List<String> fromShapeId) {
		this.fromShapeId = fromShapeId;
	}
	public List<String> getToShapeId() {
		return toShapeId;
	}
	public void setToShapeId(List<String> toShapeId) {
		this.toShapeId = toShapeId;
	}
	public List<String> getIncomeCPNId() {
		return incomeCPNId;
	}
	public void setIncomeCPNId(List<String> incomeCPNId) {
		this.incomeCPNId = incomeCPNId;
	}
	public List<String> getOutcomeCPNId() {
		return outcomeCPNId;
	}
	public void setOutcomeCPNId(List<String> outcomeCPNId) {
		this.outcomeCPNId = outcomeCPNId;
	}
	public String getInitMark() {
		return initMark;
	}
	public void setInitMark(String initMark) {
		this.initMark = initMark;
	}
	
}
