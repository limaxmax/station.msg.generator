package msggenerator;

public class StationMsg {
	public StationMsgBody smb;
	public StationMsgHead smh;
	
	public StationMsg(String SystemLable,int SystemNo,int LocationNo,int MsgId,Long Stamp,String LineLabel,StationData sd) {
		StationMsgBody smb=new StationMsgBody();
		StationMsgHead smh=new StationMsgHead();
		smh.LineLable=LineLabel;
		smh.LocationNo=LocationNo;
		smh.SystemLabel=SystemLable;
		smh.SystemNo=SystemNo;
		smh.MsgId=MsgId;
		smh.Stamp=Stamp;
		smb.StationData=sd;
		this.smb=smb;
		this.smh=smh;
	}
	

	public StationMsg() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String toString() {
		return "{\"MsgHead\":"+smh.toString()+",\"MsgBody\":"+smb.toString()+"}";
	}
	
}
