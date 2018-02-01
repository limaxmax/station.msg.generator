package msggenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.junit.Test;

import util.ExcelUtil;

public class ReadActionByStamp {
	ExcelUtil eu = new ExcelUtil();

	public static HSSFWorkbook readReplay(String replayFile) {
		POIFSFileSystem fs = null;
		HSSFWorkbook hb = null;
		try {
			fs = new POIFSFileSystem(new FileInputStream(replayFile));
			hb = new HSSFWorkbook(fs);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hb;
	}

	public long readStamp(HSSFWorkbook hbk, int sheetIndex, CellAddress ca) {
		// ��ȡʱ�䲢תΪʱ���
		String timeStr = eu.readCellContent(sheetIndex, ca, hbk);
		if(timeStr.equals("")){
			return -1;
		}
		// System.out.println(timeStr);
		long timeStp = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		try {
			timeStp = sdf.parse(timeStr).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 8*3600+timeStp / 1000;
	}

	@Test
	public void readStampTest() {
		HSSFWorkbook hwk = readReplay("D:" + File.separator + "replay.xls");
		// System.out.println(readStamp(hwk, 0, new CellAddress("A3")));
	}

	public Map<Long, List<String>> readDevicesStatus(HSSFWorkbook hbk, int sheetIndex) {
		Map<Long, List<String>> devicesStatus = new HashMap<Long, List<String>>();

		Sheet sheet = hbk.getSheetAt(sheetIndex);
		int rowNum = sheet.getLastRowNum();
		for (int i = 2; i <= rowNum + 1; i++) {
			long stamp = readStamp(hbk, sheetIndex, new CellAddress("A" + i));
			if(stamp==-1){
				
				break;
			}
			Row row = sheet.getRow(i - 1);
			List<String> list = new ArrayList<String>();
			for (int j = 0; j < row.getLastCellNum() + 1; j++) {
				char colChar = (char) (j + 66);

				String cellStr = eu.readCellContent(sheetIndex, new CellAddress(String.valueOf(colChar) + i), hbk);
				if (!cellStr.equals("")) {
					list.add(cellStr);
				}
			}
			devicesStatus.put(stamp, list);
		}

		return devicesStatus;

	}

	@Test
	public void readDevicesStatusTest() {
		HSSFWorkbook hwk = readReplay("D:" + File.separator + "replay.xls");
		System.out.println(readDevicesStatus(hwk, 0));
		// for(String str:readDevicesStatus(hwk,0).get((long)1512666005)){
		// System.out.println(str);
		// }

	}

	public Map<Long, List<AtsTrainInfo>> readDevicesStatusFor2(HSSFWorkbook hbk, int sheetIndex) {
		Map<Long, List<AtsTrainInfo>> devicesStatus = new HashMap<Long, List<AtsTrainInfo>>();

		Sheet sheet = hbk.getSheetAt(sheetIndex);
		int rowNum = sheet.getLastRowNum();
		
		for (int i = 2; i <= rowNum + 1; i++) {
			List<AtsTrainInfo> list = new ArrayList<AtsTrainInfo>();
			long stamp = readStamp(hbk, sheetIndex, new CellAddress("A" + i));
			if(stamp==-1){
				break;
			};
			Row row = sheet.getRow(i - 1);
			// ��ȡ�����е����ݣ����Ϊһ��AtsTrainInfoʵ��
			AtsTrainInfo ati = new AtsTrainInfo();
			// �豸���
			ati.DeviceNo = eu.readCellContent(sheetIndex, new CellAddress("B" + i), hbk);
			// �����
			ati.serverNO = eu.readCellContent(sheetIndex, new CellAddress("C" + i), hbk).split("\\.")[0];
			// ˾����
			ati.driverNO = Integer
					.valueOf(eu.readCellContent(sheetIndex, new CellAddress("D" + i), hbk).split("\\.")[0]);
			// �г�����
			String property = eu.readCellContent(sheetIndex, new CellAddress("E" + i), hbk);
			if (property.equals("CBTC")) {
				ati.property = 0;
			} else if (property.equals("��")) {
				ati.property = 1;
			}
			;
			// ʱ���
			ati.Stamp = stamp;
			// �����
			ati.GroupId = eu.readCellContent(sheetIndex, new CellAddress("F" + i), hbk).split("\\.")[0];
			// ���κ�
			ati.TrainId = eu.readCellContent(sheetIndex, new CellAddress("G" + i), hbk).split("\\.")[0];
			// ���
			ati.TableNo = eu.readCellContent(sheetIndex, new CellAddress("H" + i), hbk).split("\\.")[0];
			// Ŀ�ĵ�
			ati.destination = eu.readCellContent(sheetIndex, new CellAddress("I" + i), hbk).split("\\.")[0];
			// �г�����
			String trainType=eu.readCellContent(sheetIndex, new CellAddress("J" + i), hbk);
			if(trainType.equals("�ƻ���")){
				ati.TrainType = "1";
			}else if (trainType.equals("ͷ�복")){
				ati.TrainType = "2";
			}else if(trainType.equals("�˹���")){
				ati.TrainType = "3";
			}else if(trainType.equals("�����˹���")){
				ati.TrainType = "3";
			}
			
			// ATP�г�
			String atpFlag = eu.readCellContent(sheetIndex, new CellAddress("K" + i), hbk);
			if (atpFlag.equals("��")) {
				ati.AtpFlag = 1;
			} else if (atpFlag.equals("��")) {
				ati.AtpFlag = 0;
			}
			;
			// �г������
			String onTime = eu.readCellContent(sheetIndex, new CellAddress("L" + i), hbk);
			if (onTime.equals("���")) {
				ati.OnTime = 1;
			} else if (onTime.equals("����")) {
				ati.OnTime = 2;
			} else if (onTime.equals("���")) {
				ati.OnTime = 3;
			} else if (onTime.equals("�������")) {
				ati.OnTime = 4;
			}
			;
			// �Ƿ���ͣ
			String skipStopFlag = eu.readCellContent(sheetIndex, new CellAddress("M" + i), hbk);
			if (skipStopFlag.equals("��")) {
				ati.SkipStopFlag = 0;
			} else if (skipStopFlag.equals("��")) {
				ati.SkipStopFlag = 1;
			}
			;
			// ������ʽ
			ati.BlockMode = Long
					.parseLong(eu.readCellContent(sheetIndex, new CellAddress("N" + i), hbk).replaceAll("0x", ""), 16);
			// ��ʻģʽ
			String drivingMode = eu.readCellContent(sheetIndex, new CellAddress("O" + i), hbk);
			if (drivingMode.equals("δ֪")) {
				ati.DrivingMode = 0;
			} else if (drivingMode.equals("AM")) {
				ati.DrivingMode = 1;
			} else if (drivingMode.equals("CM")) {
				ati.DrivingMode = 2;
			} else if (drivingMode.equals("RM")) {
				ati.DrivingMode = 3;
			} else if (drivingMode.equals("NRM")) {
				ati.DrivingMode = 4;
			}
			// ����
			String dir = eu.readCellContent(sheetIndex, new CellAddress("P" + i), hbk);
			if (dir.equals("δ֪")) {
				ati.Dir = 0;
			} else if (dir.equals("����")) {
				ati.Dir = 1;
			} else if (dir.equals("����")) {
				ati.Dir = 2;
			}
			;
			// �������α��
			ati.PhySection = eu.readCellContent(sheetIndex, new CellAddress("Q" + i), hbk);
			// �߼����α��
			ati.LogicSection = eu.readCellContent(sheetIndex, new CellAddress("R" + i), hbk);
			// �۳�
			String trainBuckle = eu.readCellContent(sheetIndex, new CellAddress("S" + i), hbk);
			if (trainBuckle.equals("�۳�")) {
				ati.TrainBuckle = 0;
			} else if (trainBuckle.equals("�޿۳�")) {
				ati.TrainBuckle = 1;
			}
			;
			// �г�ͣ��
			String trainStay = eu.readCellContent(sheetIndex, new CellAddress("T" + i), hbk);
			if (trainStay.equals("δͣ��")) {
				ati.TrainStay = 0;
			} else if (trainStay.equals("ͣ��")) {
				ati.TrainStay = 1;
			}
			;
			// ������Ϣ
			String trainDoor = eu.readCellContent(sheetIndex, new CellAddress("U" + i), hbk);
			if (trainDoor.equals("��")) {
				ati.TrainDoor = 0;
			} else if (trainDoor.equals("�ر�")) {
				ati.TrainDoor = 1;
			} else if (trainDoor.equals("δ֪")) {
				ati.TrainDoor = 2;
			}
			;
			// �г���ͻ
			String trainConflict = eu.readCellContent(sheetIndex, new CellAddress("V" + i), hbk);
			if (trainConflict.equals("����ͻ")) {
				ati.TrainConflict = 0;
			} else if (trainConflict.equals("��ͻ")) {
				ati.TrainConflict = 1;
			}
			;
			// �Ƿ��Ƴ�
			ati.Delete = Integer
					.parseInt(eu.readCellContent(sheetIndex, new CellAddress("W" + i), hbk).replaceAll("0x", ""), 16);
			// �г��ڳ��δ�λ�ñ��
			ati.TccWindowOffset = Integer
					.parseInt(eu.readCellContent(sheetIndex, new CellAddress("X" + i), hbk).replaceAll("0x", ""), 16);
			if(devicesStatus.containsKey(stamp)){
				list=devicesStatus.get(stamp);
				list.add(ati);
				devicesStatus.put(stamp, list);
			}else{
				list.add(ati);
				devicesStatus.put(stamp, list);
			}
			
			
		}
		return devicesStatus;
	}

	@Test
	public void readDevicesStatusFor2Test() {
		HSSFWorkbook hwk = readReplay("D:" + File.separator + "replay.xls");
		System.out.println(readDevicesStatusFor2(hwk, 2));
	}
	public Map<Long, List<AtsTrainInfo_type>> readDevicesStatusFor2_type(HSSFWorkbook hbk, int sheetIndex) {
		Map<Long, List<AtsTrainInfo_type>> devicesStatus = new HashMap<Long, List<AtsTrainInfo_type>>();

		Sheet sheet = hbk.getSheetAt(sheetIndex);
		int rowNum = sheet.getLastRowNum();
		
		
		for (int i = 2; i <= rowNum + 1; i++) {
			List<AtsTrainInfo_type> list = new ArrayList<AtsTrainInfo_type>();
			long stamp = readStamp(hbk, sheetIndex, new CellAddress("A" + i));
			if(stamp==-1){
				break;
			};
			Row row = sheet.getRow(i - 1);
			// ��ȡ�����е����ݣ����Ϊһ��AtsTrainInfoʵ��
			AtsTrainInfo_type ati = new AtsTrainInfo_type();
			// �豸���
			ati.DeviceNo = eu.readCellContent(sheetIndex, new CellAddress("B" + i), hbk);
			// �����
			ati.serverNO = eu.readCellContent(sheetIndex, new CellAddress("C" + i), hbk).split("\\.")[0];
			// ˾����
			ati.driverNO = eu.readCellContent(sheetIndex, new CellAddress("D" + i), hbk).split("\\.")[0];
			// �г�����
			String property = eu.readCellContent(sheetIndex, new CellAddress("E" + i), hbk);
			if (property.equals("CBTC")) {
				ati.property = "0";
			} else if (property.equals("��")) {
				ati.property = "1";
			}
			;
			// ʱ���
			ati.Stamp = stamp+"";
			// �����
			ati.GroupId = eu.readCellContent(sheetIndex, new CellAddress("F" + i), hbk).split("\\.")[0];
			// ���κ�
			ati.TrainId = eu.readCellContent(sheetIndex, new CellAddress("G" + i), hbk).split("\\.")[0];
			// ���
			ati.TableNo = eu.readCellContent(sheetIndex, new CellAddress("H" + i), hbk).split("\\.")[0];
			// Ŀ�ĵ�
			ati.destination = eu.readCellContent(sheetIndex, new CellAddress("I" + i), hbk).split("\\.")[0];
			// �г�����
			String trainType=eu.readCellContent(sheetIndex, new CellAddress("J" + i), hbk);
			if(trainType.equals("�ƻ���")){
				ati.TrainType = "1";
			}else if (trainType.equals("ͷ�복")){
				ati.TrainType = "2";
			}else if(trainType.equals("�˹���")){
				ati.TrainType = "3";
			}else if(trainType.equals("�����˹���")){
				ati.TrainType = "3";
			}
			
			// ATP�г�
			String atpFlag = eu.readCellContent(sheetIndex, new CellAddress("K" + i), hbk);
			if (atpFlag.equals("��")) {
				ati.AtpFlag = "1";
			} else if (atpFlag.equals("��")) {
				ati.AtpFlag = "0";
			}
			;
			// �г������
			String onTime = eu.readCellContent(sheetIndex, new CellAddress("L" + i), hbk);
			if (onTime.equals("���")) {
				ati.OnTime = "1";
			} else if (onTime.equals("����")) {
				ati.OnTime = "2";
			} else if (onTime.equals("���")) {
				ati.OnTime = "3";
			} else if (onTime.equals("�������")) {
				ati.OnTime = "4";
			}
			;
			// �Ƿ���ͣ
			String skipStopFlag = eu.readCellContent(sheetIndex, new CellAddress("M" + i), hbk);
			if (skipStopFlag.equals("��")) {
				ati.SkipStopFlag = "0";
			} else if (skipStopFlag.equals("��")) {
				ati.SkipStopFlag = "1";
			}
			;
			// ������ʽ
			ati.BlockMode = Long
					.parseLong(eu.readCellContent(sheetIndex, new CellAddress("N" + i), hbk).replaceAll("0x", ""), 16)+"";
			// ��ʻģʽ
			String drivingMode = eu.readCellContent(sheetIndex, new CellAddress("O" + i), hbk);
			if (drivingMode.equals("δ֪")) {
				ati.DrivingMode = "0";
			} else if (drivingMode.equals("AM")) {
				ati.DrivingMode = "1";
			} else if (drivingMode.equals("CM")) {
				ati.DrivingMode = "2";
			} else if (drivingMode.equals("RM")) {
				ati.DrivingMode = "3";
			} else if (drivingMode.equals("NRM")) {
				ati.DrivingMode = "4";
			}
			// ����
			String dir = eu.readCellContent(sheetIndex, new CellAddress("P" + i), hbk);
			if (dir.equals("δ֪")) {
				ati.Dir = "0";
			} else if (dir.equals("����")) {
				ati.Dir = "1";
			} else if (dir.equals("����")) {
				ati.Dir = "2";
			}
			;
			// �������α��
			ati.PhySection = eu.readCellContent(sheetIndex, new CellAddress("Q" + i), hbk);
			// �߼����α��
			ati.LogicSection = eu.readCellContent(sheetIndex, new CellAddress("R" + i), hbk);
			// �۳�
			String trainBuckle = eu.readCellContent(sheetIndex, new CellAddress("S" + i), hbk);
			if (trainBuckle.equals("�۳�")) {
				ati.TrainBuckle = "0";
			} else if (trainBuckle.equals("�޿۳�")) {
				ati.TrainBuckle = "1";
			}
			;
			// �г�ͣ��
			String trainStay = eu.readCellContent(sheetIndex, new CellAddress("T" + i), hbk);
			if (trainStay.equals("δͣ��")) {
				ati.TrainStay = "0";
			} else if (trainStay.equals("ͣ��")) {
				ati.TrainStay = "1";
			}
			;
			// ������Ϣ
			String trainDoor = eu.readCellContent(sheetIndex, new CellAddress("U" + i), hbk);
			if (trainDoor.equals("��")) {
				ati.TrainDoor = "0";
			} else if (trainDoor.equals("�ر�")) {
				ati.TrainDoor = "1";
			} else if (trainDoor.equals("δ֪")) {
				ati.TrainDoor = "2";
			}
			;
			// �г���ͻ
			String trainConflict = eu.readCellContent(sheetIndex, new CellAddress("V" + i), hbk);
			if (trainConflict.equals("����ͻ")) {
				ati.TrainConflict = "0";
			} else if (trainConflict.equals("��ͻ")) {
				ati.TrainConflict = "1";
			}
			;
			// �Ƿ��Ƴ�
			ati.Delete = Integer
					.parseInt(eu.readCellContent(sheetIndex, new CellAddress("W" + i), hbk).replaceAll("0x", ""), 16)+"";
			// �г��ڳ��δ�λ�ñ��
			ati.TccWindowOffset = Integer
					.parseInt(eu.readCellContent(sheetIndex, new CellAddress("X" + i), hbk).replaceAll("0x", ""), 16)+"";
			if(devicesStatus.containsKey(stamp)){
				list=devicesStatus.get(stamp);
				list.add(ati);
				devicesStatus.put(stamp, list);
			}else{
				list.add(ati);
				devicesStatus.put(stamp, list);
			}
			
			
		}
		return devicesStatus;
	}
	@Test
	public void readDevicesStatusFor2_typeTest() {
		HSSFWorkbook hwk = readReplay("D:" + File.separator + "replay.xls");
		System.out.println(readDevicesStatusFor2_type(hwk, 2));
	}
}
