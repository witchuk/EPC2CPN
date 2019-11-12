package main.model;

import object.visualparadigm.Shape;

public class EPCElement {
	private Shape shape;
	private boolean isStartNode;
	private String operatorType;		//Split, Join
	
	private String incomeId;
	private String outcomeId;
	private CPNObject cpnObject;
	
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
	public String getIncomeId() {
		return incomeId;
	}
	public void setIncomeId(String incomeId) {
		this.incomeId = incomeId;
	}
	public String getOutcomeId() {
		return outcomeId;
	}
	public void setOutcomeId(String outcomeId) {
		this.outcomeId = outcomeId;
	}
	public CPNObject getCpnObject() {
		return cpnObject;
	}
	public void setCpnObject(CPNObject cpnObject) {
		this.cpnObject = cpnObject;
	}
	
}
