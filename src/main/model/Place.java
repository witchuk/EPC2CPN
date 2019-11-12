package main.model;

public class Place extends CommonField {
	private CommonField type;
	private CommonField initMark;
	
	public CommonField getType() {
		return type;
	}
	public void setType(CommonField type) {
		this.type = type;
	}
	public CommonField getInitMark() {
		return initMark;
	}
	public void setInitMark(CommonField initMark) {
		this.initMark = initMark;
	}
	
}
