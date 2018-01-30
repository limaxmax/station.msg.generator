package msggenerator;

import java.util.List;

import org.junit.Test;

public class StationMsgBody_type {
	public String _type;
	public StationData_type StationData;
	@Override
	public String toString() {
		String str="{\"__type\":\""+"MsgBodyDevice:#Bjjdsy.Mss.Data.Alarm"+"\",\"StationData\":"+StationData.toString()+"}";
		//String str="{\"StationData\":"+StationData.toString()+"}";
		return str;
	}
	
}
class StationData_type {
	public String _type;
	public List<AtsStationData_type> listASD;
	public List<SpeedData_type> listSD;
	public List<AtsTrainTraceStatus_type> atts;
	public String toString(){
		String str="{\"__type\":\""+"StationData:#Bjjdsy.Mss.Data.NoAlarm"+"\",\"listASD\":[";
		//String str="{\"listASD\":[";
		for(AtsStationData_type asd:listASD){
			str=str+asd.toString()+",";
		};
		if (listASD.size()==0){
			str=str+"],\"listSD\":[";
		}else{
			str=str.substring(0,str.length()-1)+"],\"listSD\":[";
		}
		for(SpeedData_type sd:listSD){
			str=str+sd.toString()+",";
		}
		if(listSD.size()==0){
			str=str+"],\"atts\":[";
		}else{
			str=str.substring(0,str.length()-1)+"],\"atts\":[";
		}
		
		for(AtsTrainTraceStatus_type attsOne:atts){
			str=str+attsOne.toString()+",";
		}
		if(atts.size()==0){
			str=str+"]}";
		}else{
			str=str.substring(0,str.length()-1)+"]}";
		}
	
		return str;
	}
}
class SpeedData_type{
	public String _type;
	public String TsrSpeed;
	public String SectionId;
	public String toString(){
		return "{\"__type\":\"" + "SpeedData:#Bjjdsy.Mss.Data.NoAlarm" + "\",\"TsrSpeed\":\"" + TsrSpeed + "\",\"SectionId\":\"" + SectionId + "\"}";
		//return "{\"TsrSpeed\":" + TsrSpeed + ",\"SectionId\":\"" + SectionId + "\"}";
	}
}
class AtsStationData_type{
	public String _type;
	public String Status;
	public String DeviceName;
	public String StatusType;
	public String DeviceType;
	@Override
	public String toString() {
		return "{\"__type\":\"" + "AtsStationData:#Bjjdsy.Mss.Data.NoAlarm" + "\",\"Status\":\"" + Status + "\",\"DeviceName\":\"" + DeviceName + "\",\"StatusType\":\""
				+ StatusType + "\",\"DeviceType\":\"" + DeviceType + "\"}";
//		return "{\"Status\":" + Status + ",\"DeviceName\":\"" + DeviceName + "\",\"StatusType\":"
//		+ StatusType + ",\"DeviceType\":" + DeviceType + "}";

	}
	

}
class AtsTrainTraceStatus_type{
	public String _type;
	public String StationId;
	public List<AtsTrainInfo_type> Train;
	public String DeviceNo;
	
	public String toString(){
		String str="{\"__type\":\"" + "AtsTrainTraceStatus:#Bjjdsy.Mss.Data.NoAlarm" +"\",\"StationId\":\""+StationId+"\",\"Train\":[";
		//String str="{\"StationId\":"+StationId+",\"Train\":[";
		for(AtsTrainInfo_type ati:Train){
			str=str+ati.toString()+",";
		};
		return str.substring(0, str.length()-1)+"],\"DeviceNo\":\""+DeviceNo+"\"}";
	}
}
class AtsTrainInfo_type{
	public String _type;
	public String DeviceNo;
	public String serverNO;
	public String driverNO;
	public String property;
	public String Stamp;
	public String GroupId;
	public String TrainId;
	public String TableNo;
	public String destination;
	public String TrainType;
	public String AtpFlag;
	public String OnTime;
	public String SkipStopFlag;
	public String BlockMode;
	public String DrivingMode;
	public String Dir;
	public String PhySection;
	public String LogicSection;
	public String TrainBuckle;
	public String TrainStay;
	public String TrainDoor;
	public String TrainConflict;
	public String Delete;
	public String TccWindowOffset;
	@Override
	public String toString() {
		return "{\"__type\":\"" + "AtsTrainInfo:#Bjjdsy.Mss.Data.NoAlarm" + "\",\"DeviceNo\":\"" + DeviceNo + "\",\"serverNO\":\"" + serverNO + "\",\"driverNO\":\""
				+ driverNO + "\",\"property\":\"" + property + "\",\"Stamp\":\"" + Stamp + "\",\"GroupId\":\"" + GroupId + "\",\"TrainId\":\""
				+ TrainId + "\",\"TableNo\":\"" + TableNo + "\",\"destination\":\"" + destination + "\",\"TrainType\":\"" + TrainType
				+ "\",\"AtpFlag\":\"" + AtpFlag + "\",\"OnTime\":\"" + OnTime + "\",\"SkipStopFlag\":\"" + SkipStopFlag + "\",\"BlockMode\":\""
				+ BlockMode + "\",\"DrivingMode\":\"" + DrivingMode + "\",\"Dir\":\"" + Dir + "\",\"PhySection\":\"" + PhySection
				+ "\",\"LogicSection\":\"" + LogicSection + "\",\"TrainBuckle\":\"" + TrainBuckle + "\",\"TrainStay\":\"" + TrainStay
				+ "\",\"TrainDoor\":\"" + TrainDoor + "\",\"TrainConflict\":\"" + TrainConflict + "\",\"Delete\":\"" + Delete
				+ "\",\"TccWindowOffset\":\"" + TccWindowOffset + "\"}";
//		return "{\"DeviceNo\":\"" + DeviceNo + "\",\"serverNO\":\"" + serverNO + "\",\"driverNO\":"
//		+ driverNO + ",\"property\":" + property + ",\"Stamp\":" + Stamp + ",\"GroupId\":\"" + GroupId + "\",\"TrainId\":\""
//		+ TrainId + "\",\"TableNo\":\"" + TableNo + "\",\"destination\":\"" + destination + "\",\"TrainType\":\"" + TrainType
//		+ "\",\"AtpFlag\":" + AtpFlag + ",\"OnTime\":" + OnTime + ",\"SkipStopFlag\":" + SkipStopFlag + ",\"BlockMode\":"
//		+ BlockMode + ",\"DrivingMode\":" + DrivingMode + ",\"Dir\":" + Dir + ",\"PhySection\":\"" + PhySection
//		+ "\",\"LogicSection\":\"" + LogicSection + "\",\"TrainBuckle\":" + TrainBuckle + ",\"TrainStay\":" + TrainStay
//		+ ",\"TrainDoor\":" + TrainDoor + ",\"TrainConflict\":" + TrainConflict + ",\"Delete\":" + Delete
//		+ ",\"TccWindowOffset\":" + TccWindowOffset + "}";
	}
	
	
}