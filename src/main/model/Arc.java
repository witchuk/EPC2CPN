package main.model;

public class Arc extends CommonField {
	private String arcType;
	private String transId;
	private String placeId;
	private CommonField annot;
	
	public String getArcType() {
		return arcType;
	}
	public void setArcType(String arcType) {
		this.arcType = arcType;
	}
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	public CommonField getAnnot() {
		return annot;
	}
	public void setAnnot(CommonField annot) {
		this.annot = annot;
	}
	
}
