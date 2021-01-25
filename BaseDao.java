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
 * ���ݿ⹤����
 * @author EDZ
 *
 */
public class BaseDao{
	
	Connection conn=null;
	PreparedStatement pstmt=null;
	//���µ��ĸ�������������database.properties�ļ��е��ĸ��ַ���
	static String driver=null;
	static String url=null;
	static String userName=null;
	static String password=null;
	//��̬�����
	static {
		init();
	}
	
	public static void init() {
		//����Properties����,��������.properties�ļ�
		Properties properties=new Properties();
		//����һ���ַ���,�����ļ���
		String configFile="database.properties";
		//����һ��InputStream������
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
	
	
	
   
	//��ȡConnection����
	public Connection getConnection() {
		try {
			//1.��������
			Class.forName(driver);
			//2.��ȡConnection����
			conn=DriverManager.getConnection(url,userName,password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	//��ɾ��ͨ�÷���
	public Integer executeUpdate(String sql,Object[] params) {
		Integer result=0;
		conn=this.getConnection();
		try {
			pstmt=conn.prepareStatement(sql);
			//params����null˵��sql�������?
			if(params!=null) {
				for (int i = 0; i < params.length; i++) {
					pstmt.setObject(i+1, params[i]);
				}
			}
			//ִ��sql���
		    result=pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			this.closeAll(null, pstmt, conn);
		}
		return result;
	} 
	
	
	
	//�ͷ���Դ(�ر�����)
	public void closeAll(ResultSet rs,PreparedStatement pstmt,Connection conn) {
		if (rs!=null) {
			try {
				//3.�ر�Connection����
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
		if (pstmt!=null) {
			try {
				//3.�ر�Connection����
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		if (conn!=null) {
			try {
				//3.�ر�Connection����
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} 
	}
	
	

}
