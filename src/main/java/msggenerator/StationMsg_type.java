package msggenerator;

public class StationMsg_type {
	public StationMsgBody_type smb;
	public StationMsgHead_type smh;
	
	public StationMsg_type(String SystemLable,String SystemNo,String LocationNo,String MsgId,String Stamp,String LineLabel,StationData_type sd) {
		StationMsgBody_type smb=new StationMsgBody_type();
		StationMsgHead_type smh=new StationMsgHead_type();
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
	

	public StationMsg_type() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String toString() {
		return smh.toString()+"&"+smb.toString();
	}
	
}
