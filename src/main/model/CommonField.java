package main.model;

public class CommonField {
	private String id;
	private int x;
	private int y;
	private String text;
	
	public CommonField() {
		super();
	}
	
	public CommonField(String id, int x, int y, String text) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.text = text;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
