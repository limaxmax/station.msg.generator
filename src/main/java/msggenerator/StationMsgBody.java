package msggenerator;

import java.util.List;

import org.junit.Test;

public class StationMsgBody {
	//public String _type;
	public StationData StationData;
	@Override
	public String toString() {
		//String str="{\"_type\":\""+_type+"\",\"StationData\":"+StationData.toString()+"}";
		String str="{\"StationData\":"+StationData.toString()+"}";
		return str;
	}
	
}
class StationData {
	//public String _type;
	public List<AtsStationData> listASD;
	public List<SpeedData> listSD;
	public List<AtsTrainTraceStatus> atts;
	public String toString(){
		//String str="{\"type\":\""+_type+"\",\"listASD\":[";
		String str="{\"listASD\":[";
		for(AtsStationData asd:listASD){
			str=str+asd.toString()+",";
		};
		if (listASD.size()==0){
			str=str+"],\"listSD\":[";
		}else{
			str=str.substring(0,str.length()-1)+"],\"listSD\":[";
		}
		for(SpeedData sd:listSD){
			str=str+sd.toString()+",";
		}
		if(listSD.size()==0){
			str=str+"],\"atts\":[";
		}else{
			str=str.substring(0,str.length()-1)+"],\"atts\":[";
		}
		
		for(AtsTrainTraceStatus attsOne:atts){
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
class SpeedData{
	//public String _type;
	public int TsrSpeed;
	public String SectionId;
	public String toString(){
//		return "{\"_type\":\"" + _type + "\",\"TsrSpeed\":\"" + TsrSpeed + "\",\"SectionId\":\"" + SectionId + "\",\"StatusType\":\""
//				+ "\"}";
		return "{\"TsrSpeed\":" + TsrSpeed + ",\"SectionId\":\"" + SectionId + "\"}";
	}
}
class AtsStationData{
	//public String _type;
	public int Status;
	public String DeviceName;
	public int StatusType;
	public int DeviceType;
	@Override
	public String toString() {
//		return "{\"_type\":\"" + _type + "\",\"Status\":\"" + Status + "\",\"DeviceName\":\"" + DeviceName + "\",\"StatusType\":\""
//				+ StatusType + "\",\"DeviceType\":\"" + DeviceType + "\"}";
		return "{\"Status\":" + Status + ",\"DeviceName\":\"" + DeviceName + "\",\"StatusType\":"
		+ StatusType + ",\"DeviceType\":" + DeviceType + "}";

	}
	

}
class AtsTrainTraceStatus{
	//public String _type;
	public int StationId;
	public List<AtsTrainInfo> Train;
	public String DeviceNo;
	
	public String toString(){
		//String str="{\"_type\":\"" + _type +"\",\"StationId\":\""+StationId+",\"Train\":[";
		String str="{\"StationId\":"+StationId+",\"Train\":[";
		for(AtsTrainInfo ati:Train){
			str=str+ati.toString()+",";
		};
		return str.substring(0, str.length()-1)+"],\"DeviceNo\":\""+DeviceNo+"\"}";
	}
}
class AtsTrainInfo{
	//public String _type;
	public String DeviceNo;
	public String serverNO;
	public int driverNO;
	public int property;
	public long Stamp;
	public String GroupId;
	public String TrainId;
	public String TableNo;
	public String destination;
	public String TrainType;
	public int AtpFlag;
	public int OnTime;
	public int SkipStopFlag;
	public long BlockMode;
	public int DrivingMode;
	public int Dir;
	public String PhySection;
	public String LogicSection;
	public int TrainBuckle;
	public int TrainStay;
	public int TrainDoor;
	public int TrainConflict;
	public int Delete;
	public int TccWindowOffset;
	@Override
	public String toString() {
//		return "{\"_type\":\"" + _type + "\", \"DeviceNo\":\"" + DeviceNo + "\",\"serverNO\":\"" + serverNO + "\",\"driverNO\":\""
//				+ driverNO + "\",\"property\":\"" + property + "\",\"Stamp\":\"" + Stamp + "\",\"GroupId\":\"" + GroupId + "\",\"TrainId\":\""
//				+ TrainId + "\",\"TableNo\":\"" + TableNo + "\",\"destination\":\"" + destination + "\",\"TrainType\":\"" + TrainType
//				+ "\",\"AtpFlag\":\"" + AtpFlag + "\",\"OnTime\":\"" + OnTime + "\",\"SkipStopFlag\":\"" + SkipStopFlag + "\",\"BlockMode\":\""
//				+ BlockMode + "\",\"DrivingMode\":\"" + DrivingMode + "\",\"Dir\":\"" + Dir + "\",\"PhySection\":\"" + PhySection
//				+ "\",\"LogicSection\":\"" + LogicSection + "\",\"TrainBuckle\":\"" + TrainBuckle + "\",\"TrainStay\":\"" + TrainStay
//				+ "\",\"TrainDoor\":\"" + TrainDoor + "\",\"TrainConflict\":\"" + TrainConflict + "\",\"Delete\":\"" + Delete
//				+ "\",\"TccWindowOffset\":\"" + TccWindowOffset + "\"}";
		return "{\"DeviceNo\":\"" + DeviceNo + "\",\"serverNO\":\"" + serverNO + "\",\"driverNO\":"
		+ driverNO + ",\"property\":" + property + ",\"Stamp\":" + Stamp + ",\"GroupId\":\"" + GroupId + "\",\"TrainId\":\""
		+ TrainId + "\",\"TableNo\":\"" + TableNo + "\",\"destination\":\"" + destination + "\",\"TrainType\":\"" + TrainType
		+ "\",\"AtpFlag\":" + AtpFlag + ",\"OnTime\":" + OnTime + ",\"SkipStopFlag\":" + SkipStopFlag + ",\"BlockMode\":"
		+ BlockMode + ",\"DrivingMode\":" + DrivingMode + ",\"Dir\":" + Dir + ",\"PhySection\":\"" + PhySection
		+ "\",\"LogicSection\":\"" + LogicSection + "\",\"TrainBuckle\":" + TrainBuckle + ",\"TrainStay\":" + TrainStay
		+ ",\"TrainDoor\":" + TrainDoor + ",\"TrainConflict\":" + TrainConflict + ",\"Delete\":" + Delete
		+ ",\"TccWindowOffset\":" + TccWindowOffset + "}";
	}
	
	
}