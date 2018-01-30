package msggenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

public class MapToEntity {
	ReadActionByStamp rabs = new ReadActionByStamp();
	HSSFWorkbook hwb = null;

	public Map<Long, List<AtsStationData>> sheet1ToList() {
		Map<Long, List<String>> devStatusMap = rabs.readDevicesStatus(hwb, 0);
		Map<String, List<String>> statusMap = ReadStatusList.readStatusList();
		Map<Long, List<AtsStationData>> map = new HashMap<Long, List<AtsStationData>>();
		List<AtsStationData> list = new ArrayList<AtsStationData>();

		for (Map.Entry<Long, List<String>> entry : devStatusMap.entrySet()) {
			String stampStr = String.valueOf(entry.getKey());

			for (String cellStr : entry.getValue()) {
				for (String str : Arrays.asList(cellStr.split("\n"))) {
					AtsStationData asd = new AtsStationData();
					asd.DeviceName = str.trim().split("，")[0];
					List<String> statusStr = statusMap.get(str.split("，")[1]);
					for (String subStatusStr : statusStr) {
						// System.out.println(subStatusStr);
						String value = subStatusStr.split(":")[1];
						if (subStatusStr.contains("DeviceType")) {
							asd.DeviceType = Integer.valueOf(value);
						} else if (subStatusStr.contains("StatusType")) {
							asd.StatusType = Integer.valueOf(value);
						} else if (subStatusStr.contains("Status")) {
							asd.Status = Integer.valueOf(value);
						}

					}
					list.add(asd);
				}

			}
			map.put(Long.valueOf(stampStr), list);
		}
		return map;
	}

	@Test
	public void sheet1ToListTest() {
		System.out.println(sheet1ToList());
	}

	@Test
	public void strReplaceStr() {
		String stationStr = "{\"MsgHead\":{\"SystemLabel\":\"ATS\",\"SystemNo\":8,\"LocationNo\":2147483647,\"LineLabel\":\"CD3\",\"MsgId\":2147483647,\"Stamp\":1508909810},\"MsgBody\":{\"StationData\":{\"listASD\":[],\"listSD\":[],\"atts\":[{\"StationId\":32767,\"Train\":[{\"Stamp\":1508909810,\"GroupId\":\"307\",\"TrainId\":null,\"TableNo\":\"0\",\"serverNO\":\"0\",\"destination\":\"0\",\"TrainType\":\"3\",\"AtpFlag\":999,\"OnTime\":2,\"SkipStopFlag\":1,\"BlockMode\":4294967295,\"DrivingMode\":0,\"Dir\":2,\"PhySection\":\"null\",\"LogicSection\":\"CD3_ATS_000_011_00680\",\"TrainBuckle\":1,\"TrainStay\":0,\"TrainDoor\":1,\"TrainConflict\":999,\"Delete\":65535,\"TccWindowOffset\":65535,\"driverNO\":65535,\"property\":1,\"DeviceNo\":\"null\"}],\"DeviceNo\":\"null\"}]}}}";
		String aastr = "\"listSD\":["
				+ "{\" Status\":\"1\",\" DeviceName\":\"BJ7L_ATS_020_003_0226\",\" StatusType\":\"1\",\" DeviceType\":\"3\"}"
				+ "]";
		//System.out.println(stationStr.replaceAll("\"listSD\":\\[[a-zA-Z0-9:,]*\\]", aastr));
	}

	public Map<Long, List<SpeedData>> sheet2ToList() {
		Map<Long, List<String>> devStatusMap = rabs.readDevicesStatus(hwb, 1);
		Map<Long, List<SpeedData>> map = new HashMap<Long, List<SpeedData>>();
		List<SpeedData> list=new ArrayList<SpeedData>();
		for(Map.Entry<Long, List<String>> entry : devStatusMap.entrySet()){
			String stampStr = String.valueOf(entry.getKey());
			String[] speeds=entry.getValue().get(0).split("\n");
			String[] sections=entry.getValue().get(1).split("\n");
			for(int i=0;i<speeds.length;i++){
				SpeedData sd=new SpeedData();
				sd.TsrSpeed=Integer.valueOf(speeds[i]);
				sd.SectionId=sections[i];
				list.add(sd);
			}
			map.put(Long.valueOf(stampStr), list);
		}
	
		return map;

	}
	@Test
	public void sheet2ToListTest(){
		System.out.println(sheet2ToList());
	}
	public Map<Long, List<AtsTrainTraceStatus>> sheet3ToList() {
		Map<Long, List<AtsTrainTraceStatus>> attsMap=new HashMap<Long, List<AtsTrainTraceStatus>>();
		Map<Long, List<AtsTrainInfo>> map=rabs.readDevicesStatusFor2(hwb,2);
		for(Map.Entry<Long, List<AtsTrainInfo>> entry : map.entrySet()){
			AtsTrainTraceStatus atts=new AtsTrainTraceStatus();
			//预留，"null"
			atts.DeviceNo="null";
			//预留，0x7FFF
			atts.StationId=Integer.parseInt("7FFF", 16);
			atts.Train=entry.getValue();
			if(attsMap.containsKey(entry.getKey())){
				attsMap.get(entry.getValue()).add(atts);
			}else{
				List<AtsTrainTraceStatus> list=new ArrayList<AtsTrainTraceStatus>();
				list.add(atts);
				attsMap.put(entry.getKey(),list);
			};
		}
		return attsMap;
	}
	@Test
	public void sheet3ToListTest(){
		System.out.println(sheet3ToList());
	}
	public Map<Long ,StationMsg> sheetsToList(){
		Map<Long, List<AtsStationData>> map1=sheet1ToList();
		Map<Long, List<SpeedData>>  map2=sheet2ToList();
		Map<Long, List<AtsTrainTraceStatus>> map3=sheet3ToList();
		Map<Long, StationMsg> stationMap=new HashMap<Long ,StationMsg>();
		for(Map.Entry<Long, List<AtsStationData>> entry : map1.entrySet()){
			if(stationMap.containsKey(entry.getKey())){
				stationMap.get(entry.getKey()).smb.StationData.listASD=entry.getValue();
			}else{
				//没有记录的话创建一个msg，haed部分的信息写死。
				StationData sd = new StationData();
				sd.listASD=entry.getValue();
				sd.atts=new ArrayList<AtsTrainTraceStatus>();
				sd.listSD=new ArrayList<SpeedData>();
				StationMsg sm=new StationMsg("ATS",8,2147483647,2147483647,entry.getKey(),"SJZ3",sd);
				stationMap.put(entry.getKey(), sm);
			}
		};
		for(Map.Entry<Long, List<SpeedData>> entry : map2.entrySet()){
			if(stationMap.containsKey(entry.getKey())){
				stationMap.get(entry.getKey()).smb.StationData.listSD=entry.getValue();
			}else{
				//没有记录的话创建一个msg，haed部分的信息写死。
				StationData sd = new StationData();
				sd.listSD=entry.getValue();
				sd.atts=new ArrayList<AtsTrainTraceStatus>();
				sd.listASD=new ArrayList<AtsStationData>();
				StationMsg sm=new StationMsg("ATS",8,2147483647,2147483647,entry.getKey(),"SJZ3",sd);
				stationMap.put(entry.getKey(), sm);
			}
		};
		for(Map.Entry<Long, List<AtsTrainTraceStatus>> entry : map3.entrySet()){
			if(stationMap.containsKey(entry.getKey())){
				stationMap.get(entry.getKey()).smb.StationData.atts=entry.getValue();
			}else{
				//没有记录的话创建一个msg，haed部分的信息写死。
				StationData sd = new StationData();
				sd.atts=entry.getValue();
				sd.listASD=new ArrayList<AtsStationData>();
				sd.listSD = new ArrayList<SpeedData>();
				StationMsg sm=new StationMsg("ATS",8,2147483647,2147483647,entry.getKey(),"SJZ3",sd);
				stationMap.put(entry.getKey(), sm);
			}
		};
		return stationMap;
	}
	@Test
	public void sheetsToListTest(){
		System.out.println(sheetsToList());
	}
}
