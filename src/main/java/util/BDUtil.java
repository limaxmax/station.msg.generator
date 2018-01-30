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
	//ThreadLocal内部是一个Map
	//key是某个线程，value是保存的值
	//这样可以将某个值绑定到一个线程上，随时通过该线程获取这个值
	private static ThreadLocal<Connection> tl;
//  以下几个static变量将在创建连接池时完成，现在可以注掉
//	private static String driver;
//	private static String url;
//	private static String user;
//	private static String pass;
	//数据库连接池
	private static BasicDataSource ds;
	static{
		tl=new ThreadLocal<Connection>();
		//加载配置文件
		Properties prop=new Properties();
		try {
			prop.load(new FileInputStream("config.properties"));
			String driver=prop.getProperty("driverName");
			String url=prop.getProperty("url");
			String user=prop.getProperty("user");
			String pass=prop.getProperty("pass");
			//连接池最大连接数
			int maxActive=Integer.parseInt(prop.getProperty("maxactive"));
			//当没有可用连接时，最大等待时间
			int maxWait=Integer.parseInt(prop.getProperty("maxwait"));
			
			
			//连接池初始化
			ds=new BasicDataSource();
			//Class.forName() 设置驱动
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
	//获取一个数据库连接
	public static Connection getConnection() throws Exception{
//		Class.forName(driver);
//		Connection conn;
//		
//			conn = DriverManager.getConnection(url,user,pass);
		//向连接池要数据库连接。若无连接可用，该方法会进入阻塞状态，
		//阻塞时间由maxWait决定，超过该时间，该方法抛出超时异常
		Connection conn=ds.getConnection();
		//将连接设置到ThreadLocal中，线程为key，set方法传入的参数conn对象为value
		tl.set(conn);
				
		return conn;
	}
	//关闭数据库连接，dbcp方式关闭则是关闭代理，实则的数据库连接未关闭，只是将其还给连接池
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
		System.out.println("获取成功");
		
	}

}
