package main.model;

public class Trans extends CommonField {
	private CommonField cond;
	private CommonField time;
	private CommonField code;
	private CommonField priority;
	
	public CommonField getCond() {
		return cond;
	}
	public void setCond(CommonField cond) {
		this.cond = cond;
	}
	public CommonField getTime() {
		return time;
	}
	public void setTime(CommonField time) {
		this.time = time;
	}
	public CommonField getCode() {
		return code;
	}
	public void setCode(CommonField code) {
		this.code = code;
	}
	public CommonField getPriority() {
		return priority;
	}
	public void setPriority(CommonField priority) {
		this.priority = priority;
	}
	
}
