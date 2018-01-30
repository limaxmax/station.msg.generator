package msggenerator;

public class StationMsgHead {
	//public String _type;
	public String SystemLabel;
	public int SystemNo;
	public int LocationNo;
	public int MsgId;
	public long Stamp;
	public String LineLable;

	public String toString(){
		//return "{\"__type\":\""+_type+","+"\"SystemLabel\":\""+SystemLabel+",\"SystemNo\":\""+SystemNo+",\"LocationNo\":\""+LocationNo+",\"MsgId\":\""+MsgId+",\"Stamp\":\""+Stamp+",\"LineLabel\":\""+LineLable+"}";
		return "{\"SystemLabel\":\""+SystemLabel+"\",\"SystemNo\":"+SystemNo+",\"LocationNo\":"+LocationNo+",\"MsgId\":"+MsgId+",\"Stamp\":"+Stamp+",\"LineLabel\":\""+LineLable+"\"}";
		
	}
}
