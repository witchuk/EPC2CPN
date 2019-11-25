package main.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import main.contant.Constant;
import main.model.Arc;
import main.model.CPNObject;
import main.model.CommonField;
import main.model.Place;
import main.model.Trans;
import main.model.Variable;
import object.visualparadigm.Shape;

public class TranformUtils {
	
	public static String generateUniqueId () {
		
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		String currentTime = String.valueOf(System.currentTimeMillis());
		int rand3digit = new Random().nextInt(900) + 100;
		return currentTime.substring(currentTime.length()-7, currentTime.length()) + rand3digit;
	}
	
	public static void createOperXorSplit() {
		
	}
	
	public static List<Shape> findStartEvent() {
		return null;
	}
	
	private static Place generatePlaceObject(int x, int y, String text, String type, String initMark) {
		String uniqueId = generateUniqueId();
		
		Place place = new Place();
		place.setId(uniqueId + "1");
		place.setX(x);
		place.setY(y);
		place.setText(text);
		place.setType(new CommonField(uniqueId + "2", x + 36, y - 24, type));
		place.setInitMark(new CommonField(uniqueId + "3", x + 36, y + 23, initMark));
		
		return place;
	}
	
	private static Trans generateTransObject(int x, int y, String text) {
		String uniqueId = generateUniqueId();
		
		Trans trans = new Trans();
		trans.setId(uniqueId + "1");
		trans.setX(x);
		trans.setY(y);
		trans.setText(text);
		trans.setCond(new CommonField(uniqueId + "2", x - 38, y + 31, ""));
		trans.setTime(new CommonField(uniqueId + "3", x + 38, y + 31, ""));
		trans.setCode(new CommonField(uniqueId + "4", x + 38, y - 31, ""));
		trans.setPriority(new CommonField(uniqueId + "5", x - 38, y - 31, ""));
		
		return trans;
	}
	
	public static Arc generateArcObject(int x, int y, String arcType, String transId, String placeId, String text) {
		String uniqueId = generateUniqueId();
		
		Arc arc = new Arc();
		arc.setId(uniqueId + "1");
		arc.setArcType(arcType);
		arc.setTransId(transId);
		arc.setPlaceId(placeId);
		arc.setAnnot(new CommonField(uniqueId + "2", x, y, text));
		
		return arc;
	}
	
	public static Variable generateVariableObject(String type, String name) {
		String uniqueId = generateUniqueId();
		
		Variable var = new Variable();
		var.setId(uniqueId);
		var.setType(type);
		var.setName(name);
		
		return var;
	}
	
	public static CPNObject generateEventCPNObject(int initX, int initY, String text, String marking, String colset) {
		List<Place> placeList = new ArrayList<Place>();
		placeList.add(generatePlaceObject(initX, initY, text, colset, marking));
		
		CPNObject cpnObject = new CPNObject();
		cpnObject.setPlaceList(placeList);
		return cpnObject;
	}
	
	public static CPNObject generateEventDummyFunctionCPNObject(int initX, int initY, String text, String marking) {
		List<Place> placeList = new ArrayList<Place>();
		placeList.add(generatePlaceObject(initX, initY, text, Constant.CPN_COLSET_UNIT, marking));
		
		List<Trans> transList = new ArrayList<Trans>();
		transList.add(generateTransObject(initX, initY + 77, text + "_D1"));
		
		List<Arc> arcList = new ArrayList<Arc>();
		arcList.add(generateArcObject(initX + 18, initY + 38, Constant.CPN_ARC_TYPE_TTOP, transList.get(0).getId(), placeList.get(0).getId(), "1`()"));
		
		CPNObject cpnObject = new CPNObject();
		cpnObject.setPlaceList(placeList);
		cpnObject.setTransList(transList);
		cpnObject.setArcList(arcList);
		return cpnObject;
	}
	
	public static CPNObject generateFunctionCPNObject(int initX, int initY, String text) {
		List<Trans> transList = new ArrayList<Trans>();
		transList.add(generateTransObject(initX, initY, text));
		
		CPNObject cpnObject = new CPNObject();
		cpnObject.setTransList(transList);
		return cpnObject;
	}
	
	public static CPNObject generateANDSplitCPNObject(int initX, int initY, String text) {
		List<Place> placeList = new ArrayList<Place>();
		placeList.add(generatePlaceObject(initX, initY + 84, text + "_D1", Constant.CPN_COLSET_UNIT, ""));
		placeList.add(generatePlaceObject(initX - 52, initY - 84, text + "_D2", Constant.CPN_COLSET_UNIT, ""));
		placeList.add(generatePlaceObject(initX + 52, initY - 84, text + "_D3", Constant.CPN_COLSET_UNIT, ""));
		
		List<Trans> transList = new ArrayList<Trans>();
		transList.add(generateTransObject(initX, initY, text));
		
		List<Arc> arcList = new ArrayList<Arc>();
		arcList.add(generateArcObject(initX + 18, initY + 42, Constant.CPN_ARC_TYPE_PTOT, transList.get(0).getId(), placeList.get(0).getId(), "1`()"));
		arcList.add(generateArcObject(initX - 10, initY - 53, Constant.CPN_ARC_TYPE_TTOP, transList.get(0).getId(), placeList.get(1).getId(), "1`()"));
		arcList.add(generateArcObject(initX + 43, initY - 32, Constant.CPN_ARC_TYPE_TTOP, transList.get(0).getId(), placeList.get(2).getId(), "1`()"));
		
		CPNObject cpnObject = new CPNObject();
		cpnObject.setPlaceList(placeList);
		cpnObject.setTransList(transList);
		cpnObject.setArcList(arcList);
		return cpnObject;
	}
	
	public static CPNObject generateANDJoinCPNObject(int initX, int initY, String text) {
		List<Place> placeList = new ArrayList<Place>();
		placeList.add(generatePlaceObject(initX - 52, initY + 84, text + "_D1", Constant.CPN_COLSET_UNIT, ""));
		placeList.add(generatePlaceObject(initX + 52, initY + 84, text + "_D2", Constant.CPN_COLSET_UNIT, ""));
		placeList.add(generatePlaceObject(initX, initY - 84, text + "_D3", Constant.CPN_COLSET_UNIT, ""));
		
		List<Trans> transList = new ArrayList<Trans>();
		transList.add(generateTransObject(initX, initY, text));
		
		List<Arc> arcList = new ArrayList<Arc>();
		arcList.add(generateArcObject(initX - 10, initY + 53, Constant.CPN_ARC_TYPE_PTOT, transList.get(0).getId(), placeList.get(0).getId(), "1`()"));
		arcList.add(generateArcObject(initX + 43, initY + 32, Constant.CPN_ARC_TYPE_PTOT, transList.get(0).getId(), placeList.get(1).getId(), "1`()"));
		arcList.add(generateArcObject(initX + 18, initY - 42, Constant.CPN_ARC_TYPE_TTOP, transList.get(0).getId(), placeList.get(2).getId(), "1`()"));
		
		CPNObject cpnObject = new CPNObject();
		cpnObject.setPlaceList(placeList);
		cpnObject.setTransList(transList);
		cpnObject.setArcList(arcList);
		return cpnObject;
	}
	
	public static CPNObject generateXORSplitWithConditionCPNObject(int initX, int initY, String text, String colset, String condition0, String condition1, String condition2) {
		List<Place> placeList = new ArrayList<Place>();
		placeList.add(generatePlaceObject(initX, initY + 84, text + "_D1", colset, ""));
		placeList.add(generatePlaceObject(initX - 52, initY - 84, text + "_D2", Constant.CPN_COLSET_UNIT, ""));
		placeList.add(generatePlaceObject(initX + 52, initY - 84, text + "_D3", Constant.CPN_COLSET_UNIT, ""));
		
		List<Trans> transList = new ArrayList<Trans>();
		transList.add(generateTransObject(initX, initY, text));
		
		List<Arc> arcList = new ArrayList<Arc>();
		arcList.add(generateArcObject(initX + 18, initY + 42, Constant.CPN_ARC_TYPE_PTOT, transList.get(0).getId(), placeList.get(0).getId(), condition0));
		arcList.add(generateArcObject(initX - 10, initY - 53, Constant.CPN_ARC_TYPE_TTOP, transList.get(0).getId(), placeList.get(1).getId(), condition1));
		arcList.add(generateArcObject(initX + 43, initY - 32, Constant.CPN_ARC_TYPE_TTOP, transList.get(0).getId(), placeList.get(2).getId(), condition2));
		
		CPNObject cpnObject = new CPNObject();
		cpnObject.setPlaceList(placeList);
		cpnObject.setTransList(transList);
		cpnObject.setArcList(arcList);
		return cpnObject;
	}
	
	public static CPNObject generateXORSplitCPNObject(int initX, int initY, String text) {
		List<Place> placeList = new ArrayList<Place>();
		placeList.add(generatePlaceObject(initX, initY + 84, text + "_D1", Constant.CPN_COLSET_UNIT, ""));
		placeList.add(generatePlaceObject(initX - 52, initY - 84, text + "_D2", Constant.CPN_COLSET_UNIT, ""));
		placeList.add(generatePlaceObject(initX + 52, initY - 84, text + "_D3", Constant.CPN_COLSET_UNIT, ""));
		
		List<Trans> transList = new ArrayList<Trans>();
		transList.add(generateTransObject(initX - 52, initY, text + "_1"));
		transList.add(generateTransObject(initX + 52, initY, text + "_2"));
		
		List<Arc> arcList = new ArrayList<Arc>();
		arcList.add(generateArcObject(initX - 9, initY + 32, Constant.CPN_ARC_TYPE_PTOT, transList.get(0).getId(), placeList.get(0).getId(), "1`()"));
		arcList.add(generateArcObject(initX + 42, initY + 53, Constant.CPN_ARC_TYPE_PTOT, transList.get(1).getId(), placeList.get(0).getId(), "1`()"));
		arcList.add(generateArcObject(initX - 34, initY - 43, Constant.CPN_ARC_TYPE_TTOP, transList.get(0).getId(), placeList.get(1).getId(), "1`()"));
		arcList.add(generateArcObject(initX + 70, initY - 43, Constant.CPN_ARC_TYPE_TTOP, transList.get(1).getId(), placeList.get(2).getId(), "1`()"));
		
		CPNObject cpnObject = new CPNObject();
		cpnObject.setPlaceList(placeList);
		cpnObject.setTransList(transList);
		cpnObject.setArcList(arcList);
		return cpnObject;
	}
	
	public static CPNObject generateXORJoinCPNObject(int initX, int initY, String text) {
		List<Place> placeList = new ArrayList<Place>();
		placeList.add(generatePlaceObject(initX - 52, initY + 84, text + "_D1", Constant.CPN_COLSET_UNIT, ""));
		placeList.add(generatePlaceObject(initX + 52, initY + 84, text + "_D2", Constant.CPN_COLSET_UNIT, ""));
		placeList.add(generatePlaceObject(initX, initY - 84, text + "_D3", Constant.CPN_COLSET_UNIT, ""));
		
		List<Trans> transList = new ArrayList<Trans>();
		transList.add(generateTransObject(initX - 52, initY, text + "_1"));
		transList.add(generateTransObject(initX + 52, initY, text + "_2"));
		
		List<Arc> arcList = new ArrayList<Arc>();
		arcList.add(generateArcObject(initX - 34, initY + 42, Constant.CPN_ARC_TYPE_PTOT, transList.get(0).getId(), placeList.get(0).getId(), "1`()"));
		arcList.add(generateArcObject(initX + 70, initY + 42, Constant.CPN_ARC_TYPE_PTOT, transList.get(1).getId(), placeList.get(1).getId(), "1`()"));
		arcList.add(generateArcObject(initX - 9, initY - 32, Constant.CPN_ARC_TYPE_TTOP, transList.get(0).getId(), placeList.get(2).getId(), "1`()"));
		arcList.add(generateArcObject(initX + 42, initY - 53, Constant.CPN_ARC_TYPE_TTOP, transList.get(1).getId(), placeList.get(2).getId(), "1`()"));
		arcList.add(generateArcObject(initX + 9, initY + 58, Constant.CPN_ARC_TYPE_INHIBITOR, transList.get(0).getId(), placeList.get(1).getId(), "1`()"));
		arcList.add(generateArcObject(initX - 9, initY + 58, Constant.CPN_ARC_TYPE_INHIBITOR, transList.get(1).getId(), placeList.get(0).getId(), "1`()"));
		
		CPNObject cpnObject = new CPNObject();
		cpnObject.setPlaceList(placeList);
		cpnObject.setTransList(transList);
		cpnObject.setArcList(arcList);
		return cpnObject;
	}
	
	public static CPNObject generateORSplitCPNObject(int initX, int initY, String text) {
		List<Place> placeList = new ArrayList<Place>();
		placeList.add(generatePlaceObject(initX, initY + 84, text + "_D1", Constant.CPN_COLSET_UNIT, ""));
		placeList.add(generatePlaceObject(initX - 52, initY - 84, text + "_D2", Constant.CPN_COLSET_UNIT, ""));
		placeList.add(generatePlaceObject(initX + 52, initY - 84, text + "_D3", Constant.CPN_COLSET_UNIT, ""));
		
		List<Trans> transList = new ArrayList<Trans>();
		transList.add(generateTransObject(initX - 104, initY, text + "_1"));
		transList.add(generateTransObject(initX, initY, text + "_2"));
		transList.add(generateTransObject(initX + 104, initY, text + "_3"));
		
		List<Arc> arcList = new ArrayList<Arc>();
		arcList.add(generateArcObject(initX - 61, initY + 58, Constant.CPN_ARC_TYPE_PTOT, transList.get(0).getId(), placeList.get(0).getId(), "1`()"));
		arcList.add(generateArcObject(initX + 18, initY + 42, Constant.CPN_ARC_TYPE_PTOT, transList.get(1).getId(), placeList.get(0).getId(), "1`()"));
		arcList.add(generateArcObject(initX + 61, initY + 58, Constant.CPN_ARC_TYPE_PTOT, transList.get(2).getId(), placeList.get(0).getId(), "1`()"));
		arcList.add(generateArcObject(initX - 61, initY - 32, Constant.CPN_ARC_TYPE_TTOP, transList.get(0).getId(), placeList.get(1).getId(), "1`()"));
		arcList.add(generateArcObject(initX - 10, initY - 53, Constant.CPN_ARC_TYPE_TTOP, transList.get(1).getId(), placeList.get(1).getId(), "1`()"));
		arcList.add(generateArcObject(initX + 43, initY - 32, Constant.CPN_ARC_TYPE_TTOP, transList.get(1).getId(), placeList.get(2).getId(), "1`()"));
		arcList.add(generateArcObject(initX + 94, initY - 53, Constant.CPN_ARC_TYPE_TTOP, transList.get(2).getId(), placeList.get(2).getId(), "1`()"));
		
		CPNObject cpnObject = new CPNObject();
		cpnObject.setPlaceList(placeList);
		cpnObject.setTransList(transList);
		cpnObject.setArcList(arcList);
		return cpnObject;
	}
	
	public static CPNObject generateORJoinCPNObject(int initX, int initY, String text) {
		List<Place> placeList = new ArrayList<Place>();
		placeList.add(generatePlaceObject(initX - 52, initY + 84, text + "_D1", Constant.CPN_COLSET_UNIT, ""));
		placeList.add(generatePlaceObject(initX + 52, initY + 84, text + "_D2", Constant.CPN_COLSET_UNIT, ""));
		placeList.add(generatePlaceObject(initX, initY - 84, text + "_D3", Constant.CPN_COLSET_UNIT, ""));
		
		List<Trans> transList = new ArrayList<Trans>();
		transList.add(generateTransObject(initX - 104, initY, text + "_1"));
		transList.add(generateTransObject(initX, initY, text + "_2"));
		transList.add(generateTransObject(initX + 104, initY, text + "_3"));
		
		List<Arc> arcList = new ArrayList<Arc>();
		arcList.add(generateArcObject(initX - 61, initY + 32, Constant.CPN_ARC_TYPE_PTOT, transList.get(0).getId(), placeList.get(0).getId(), "1`()"));
		arcList.add(generateArcObject(initX - 10, initY + 53, Constant.CPN_ARC_TYPE_PTOT, transList.get(1).getId(), placeList.get(0).getId(), "1`()"));
		arcList.add(generateArcObject(initX + 43, initY + 32, Constant.CPN_ARC_TYPE_PTOT, transList.get(1).getId(), placeList.get(1).getId(), "1`()"));
		arcList.add(generateArcObject(initX + 94, initY + 53, Constant.CPN_ARC_TYPE_PTOT, transList.get(2).getId(), placeList.get(1).getId(), "1`()"));
		arcList.add(generateArcObject(initX - 38, initY - 30, Constant.CPN_ARC_TYPE_TTOP, transList.get(0).getId(), placeList.get(2).getId(), "1`()"));
		arcList.add(generateArcObject(initX + 18, initY - 42, Constant.CPN_ARC_TYPE_TTOP, transList.get(1).getId(), placeList.get(2).getId(), "1`()"));
		arcList.add(generateArcObject(initX + 38, initY - 30, Constant.CPN_ARC_TYPE_TTOP, transList.get(2).getId(), placeList.get(2).getId(), "1`()"));
		arcList.add(generateArcObject(initX + 31, initY + 59, Constant.CPN_ARC_TYPE_INHIBITOR, transList.get(2).getId(), placeList.get(0).getId(), "1`()"));
		arcList.add(generateArcObject(initX - 31, initY + 59, Constant.CPN_ARC_TYPE_INHIBITOR, transList.get(0).getId(), placeList.get(1).getId(), "1`()"));
		
		CPNObject cpnObject = new CPNObject();
		cpnObject.setPlaceList(placeList);
		cpnObject.setTransList(transList);
		cpnObject.setArcList(arcList);
		return cpnObject;
	}
	
	public static String performCPNElemetXMLString(CPNObject cpnObject, Properties properties) {
		String result = "";
		if (cpnObject != null) {
			
			String placeTemplate = "";
			String transTemplate = "";
			String arcTemplate = "";
			try {
				placeTemplate = FileUtils.readFileToString(new File(properties.getProperty("TEMPLATE_PLACE")), Charset.defaultCharset());
				transTemplate = FileUtils.readFileToString(new File(properties.getProperty("TEMPLATE_TRANS")), Charset.defaultCharset());
				arcTemplate = FileUtils.readFileToString(new File(properties.getProperty("TEMPLATE_ARC")), Charset.defaultCharset());
			} catch (IOException io) {
				io.printStackTrace();
			}

			// perform Place
			if (cpnObject.getPlaceList() != null && cpnObject.getPlaceList().size() > 0) {
				for (Place place : cpnObject.getPlaceList()) {
					result += placeTemplate.replaceAll("#PLACE_ID", place.getId())
							.replaceAll("#PLACE_X", String.valueOf(place.getX()))
							.replaceAll("#PLACE_Y", String.valueOf(place.getY()))
							.replaceAll("#PLACE_TEXT", place.getText())
							.replaceAll("#PLACE_TYPE_ID", place.getType().getId())
							.replaceAll("#PLACE_TYPE_X", String.valueOf(place.getType().getX()))
							.replaceAll("#PLACE_TYPE_Y", String.valueOf(place.getType().getY()))
							.replaceAll("#PLACE_TYPE_TEXT", place.getType().getText())
							.replaceAll("#PLACE_MARK_ID", place.getInitMark().getId())
							.replaceAll("#PLACE_MARK_X", String.valueOf(place.getInitMark().getX()))
							.replaceAll("#PLACE_MARK_Y", String.valueOf(place.getInitMark().getY()))
							.replaceAll("#PLACE_MARK_TEXT", place.getInitMark().getText());
				}
			}
			
			// perform Transition
			if (cpnObject.getTransList() != null && cpnObject.getTransList().size() > 0) {
				for (Trans trans : cpnObject.getTransList()) {
					result += transTemplate.replaceAll("#TRANS_ID", trans.getId())
							.replaceAll("#TRANS_X", String.valueOf(trans.getX()))
							.replaceAll("#TRANS_Y", String.valueOf(trans.getY()))
							.replaceAll("#TRANS_TEXT", trans.getText())
							.replaceAll("#TRANS_COND_ID", trans.getCond().getId())
							.replaceAll("#TRANS_COND_X", String.valueOf(trans.getCond().getX()))
							.replaceAll("#TRANS_COND_Y", String.valueOf(trans.getCond().getY()))
							.replaceAll("#TRANS_COND_TEXT", trans.getCond().getText())
							.replaceAll("#TRANS_TIME_ID", trans.getTime().getId())
							.replaceAll("#TRANS_TIME_X", String.valueOf(trans.getTime().getX()))
							.replaceAll("#TRANS_TIME_Y", String.valueOf(trans.getTime().getY()))
							.replaceAll("#TRANS_TIME_TEXT", trans.getTime().getText())
							.replaceAll("#TRANS_CODE_ID", trans.getCode().getId())
							.replaceAll("#TRANS_CODE_X", String.valueOf(trans.getCode().getX()))
							.replaceAll("#TRANS_CODE_Y", String.valueOf(trans.getCode().getY()))
							.replaceAll("#TRANS_CODE_TEXT", trans.getCode().getText())
							.replaceAll("#TRANS_PRIO_ID", trans.getPriority().getId())
							.replaceAll("#TRANS_PRIO_X", String.valueOf(trans.getPriority().getX()))
							.replaceAll("#TRANS_PRIO_Y", String.valueOf(trans.getPriority().getY()))
							.replaceAll("#TRANS_PRIO_TEXT", trans.getPriority().getText());
				}
			}
			
			// perform Transition
			if (cpnObject.getArcList() != null && cpnObject.getArcList().size() > 0) {
				for (Arc arc : cpnObject.getArcList()) {
					result += arcTemplate.replaceAll("#ARC_ID", arc.getId())
							.replaceAll("#ARC_TYPE", arc.getArcType())
							.replaceAll("#TRANS_ID", arc.getTransId())
							.replaceAll("#PLACE_ID", arc.getPlaceId())
							.replaceAll("#ARC_ANNO_X", String.valueOf(arc.getAnnot().getX()))
							.replaceAll("#ARC_ANNO_Y", String.valueOf(arc.getAnnot().getY()))
							.replaceAll("#ARC_ANNO_TEXT", arc.getAnnot().getText());
				}
			}
			
		}
		return result;
	}
	
	public static String performCPNVariableXMLString(Variable variable, Properties properties) {
		String result = "";
		if (variable != null) {
			
			String variableTemplate = "";
			try {
				variableTemplate = FileUtils.readFileToString(new File(properties.getProperty("TEMPLATE_VARIABLE")), Charset.defaultCharset());
			} catch (IOException io) {
				io.printStackTrace();
			}
			
			// perform Variable
			result += variableTemplate.replaceAll("#VAR_ID", variable.getId())
					.replaceAll("#VAR_TYPE", variable.getType())
					.replaceAll("#VAR_NAME", variable.getName());
			
		}
		return result;
	}
	
	public static String performCPNMLConditionString(String condition) {
		if (condition.contains("=="))
			condition = condition.replaceAll("==", "=");
		else if (condition.contains("!="))
			condition = condition.replaceAll("!=", "&lt;&gt;");
		else 
			condition = condition.replaceAll("<", "&lt;").replaceAll(">", "&gt;");condition = condition.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		return "if " + condition + " then 1`() else empty";
	}
	
}
