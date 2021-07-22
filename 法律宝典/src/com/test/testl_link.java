package com.test;

import java.sql.*;

import com.mysql.cj.jdbc.*;

public class testl_link {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String drivername="com.mysql.cj.jdbc.Driver"; 
		String dbname = "layer";
		String username = "root";
		String userpassword = "root";
		String url="jdbc:mysql://localhost:3306/"+dbname+"?user="+username+"&password="+userpassword+"&useUnicode=true&characterEncoding=UTF-8&&serverTimezone=GMT";
		
		try {
			Class.forName(drivername).newInstance();
			Connection connection=DriverManager.getConnection(url); 
			Statement statement = connection.createStatement();
			String sql="SELECT * FROM `layer_civil code`";
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String checktext = rs.getString("ÄÚÈÝ");
				if(!checktext.isEmpty()) {
					System.out.println(checktext);
				}else {
					System.out.println("¹þ¹þ");
				}

				System.out.println(rs.getString("Ìõ"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
