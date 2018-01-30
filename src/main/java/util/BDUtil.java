package util;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;



public class BDUtil {
	//ThreadLocal�ڲ���һ��Map
	//key��ĳ���̣߳�value�Ǳ����ֵ
	//�������Խ�ĳ��ֵ�󶨵�һ���߳��ϣ���ʱͨ�����̻߳�ȡ���ֵ
	private static ThreadLocal<Connection> tl;
//  ���¼���static�������ڴ������ӳ�ʱ��ɣ����ڿ���ע��
//	private static String driver;
//	private static String url;
//	private static String user;
//	private static String pass;
	//���ݿ����ӳ�
	private static BasicDataSource ds;
	static{
		tl=new ThreadLocal<Connection>();
		//���������ļ�
		Properties prop=new Properties();
		try {
			prop.load(new FileInputStream("config.properties"));
			String driver=prop.getProperty("driverName");
			String url=prop.getProperty("url");
			String user=prop.getProperty("user");
			String pass=prop.getProperty("pass");
			//���ӳ����������
			int maxActive=Integer.parseInt(prop.getProperty("maxactive"));
			//��û�п�������ʱ�����ȴ�ʱ��
			int maxWait=Integer.parseInt(prop.getProperty("maxwait"));
			
			
			//���ӳس�ʼ��
			ds=new BasicDataSource();
			//Class.forName() ��������
			ds.setDriverClassName(driver);
			ds.setUrl(url);
			ds.setUsername(user);
			ds.setPassword(pass);
//			ds.setMaxTotal(maxActive);
//			ds.setMaxWaitMillis(maxWait);
			ds.setMaxActive(maxActive);
			ds.setMaxWait(maxWait);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//��ȡһ�����ݿ�����
	public static Connection getConnection() throws Exception{
//		Class.forName(driver);
//		Connection conn;
//		
//			conn = DriverManager.getConnection(url,user,pass);
		//�����ӳ�Ҫ���ݿ����ӡ��������ӿ��ã��÷������������״̬��
		//����ʱ����maxWait������������ʱ�䣬�÷����׳���ʱ�쳣
		Connection conn=ds.getConnection();
		//���������õ�ThreadLocal�У��߳�Ϊkey��set��������Ĳ���conn����Ϊvalue
		tl.set(conn);
				
		return conn;
	}
	//�ر����ݿ����ӣ�dbcp��ʽ�ر����ǹرմ���ʵ������ݿ�����δ�رգ�ֻ�ǽ��仹�����ӳ�
	public static void closeConnection() {
		Connection conn=tl.get();
		if (conn!=null){
		try {
			conn.close();
			tl.remove();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	@Test
	public void  test() {
		// TODO Auto-generated method stub
		try {
			Connection conn=BDUtil.getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("��ȡ�ɹ�");
		
	}

}
