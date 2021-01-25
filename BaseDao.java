package cn.kgc.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库工具类
 * @author EDZ
 *
 */
public class BaseDao{
	
	Connection conn=null;
	PreparedStatement pstmt=null;
	//以下的四个变量用来接收database.properties文件中的四个字符串
	static String driver=null;
	static String url=null;
	static String userName=null;
	static String password=null;
	//静态代码块
	static {
		init();
	}
	
	public static void init() {
		//创建Properties对象,用来解析.properties文件
		Properties properties=new Properties();
		//定义一个字符串,保存文件名
		String configFile="database.properties";
		//构建一个InputStream流对象
		try {
			//InputStream is=new FileInputStream("database.properties");
			InputStream is=BaseDao.class.getClassLoader().getResourceAsStream(configFile);
			properties.load(is);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			e.printStackTrace();
		}
		driver=properties.getProperty("jdbc.driver");
		url=properties.getProperty("jdbc.url");
		userName=properties.getProperty("jdbc.username");
		password=properties.getProperty("jdbc.password");
		
		System.out.println(driver);
	}
	
	
	
   
	//获取Connection对象
	public Connection getConnection() {
		try {
			//1.加载驱动
			Class.forName(driver);
			//2.获取Connection对象
			conn=DriverManager.getConnection(url,userName,password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	//增删改通用方法
	public Integer executeUpdate(String sql,Object[] params) {
		Integer result=0;
		conn=this.getConnection();
		try {
			pstmt=conn.prepareStatement(sql);
			//params不是null说明sql语句中有?
			if(params!=null) {
				for (int i = 0; i < params.length; i++) {
					pstmt.setObject(i+1, params[i]);
				}
			}
			//执行sql语句
		    result=pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			this.closeAll(null, pstmt, conn);
		}
		return result;
	} 
	
	
	
	//释放资源(关闭连接)
	public void closeAll(ResultSet rs,PreparedStatement pstmt,Connection conn) {
		if (rs!=null) {
			try {
				//3.关闭Connection对象
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
		if (pstmt!=null) {
			try {
				//3.关闭Connection对象
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		if (conn!=null) {
			try {
				//3.关闭Connection对象
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} 
	}
	
	

}
