package msggenerator;

import java.io.File;

public class StationToDB {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MapToEntity_type mt= new MapToEntity_type();
		//ReadActionByStamp rabs = new ReadActionByStamp();
		//mt.hwb = rabs.readReplay(args[0]);
		mt.msg_typeToDB(args[0]);
	}

}
