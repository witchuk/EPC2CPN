package main.model;

import java.util.List;

public class CPNObject {
	private List<Place> placeList;
	private List<Trans> transList;
	private List<Arc> arcList;
	
	public List<Place> getPlaceList() {
		return placeList;
	}
	public void setPlaceList(List<Place> placeList) {
		this.placeList = placeList;
	}
	public List<Trans> getTransList() {
		return transList;
	}
	public void setTransList(List<Trans> transList) {
		this.transList = transList;
	}
	public List<Arc> getArcList() {
		return arcList;
	}
	public void setArcList(List<Arc> arcList) {
		this.arcList = arcList;
	}
	
}
