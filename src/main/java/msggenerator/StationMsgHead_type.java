package msggenerator;

public class StationMsgHead_type {
	public String _type;
	public String SystemLabel;
	public String SystemNo;
	public String LocationNo;
	public String MsgId;
	public String Stamp;
	public String LineLable;

	public String toString(){
		return "{\"__type\":\""+"MsgHeadDefault:#Bjjdsy.Mss.Data"+"\",\"SystemLabel\":\""+SystemLabel+"\",\"SystemNo\":\""+SystemNo+"\",\"LocationNo\":\""+LocationNo+"\",\"MsgId\":\""+MsgId+"\",\"Stamp\":\""+Stamp+"\",\"LineLabel\":\""+LineLable+"\"}";
		//return "{\"SystemLabel\":\""+SystemLabel+"\",\"SystemNo\":"+SystemNo+",\"LocationNo\":"+LocationNo+",\"MsgId\":"+MsgId+",\"Stamp\":"+Stamp+",\"LineLabel\":\""+LineLable+"\"}";
		
	}
}
