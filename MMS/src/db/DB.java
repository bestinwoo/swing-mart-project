package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DB {
	private static Connection dbConn = null;
	private static Statement stmt;
	

	public static void init() {
		if (dbConn == null) {
			try {
				String url = "jdbc:mysql://localhost:3306/bestinwoo?&serverTimezone=Asia/Seoul";
				String user = "usersh";
				String password = "1126";
				
				Class.forName("com.mysql.cj.jdbc.Driver");
				dbConn = DriverManager.getConnection(url, user, password);
				stmt = dbConn.createStatement();
				System.out.println("DB 연결 성공");
			} catch (ClassNotFoundException e) {
				System.out.println("JDBC 드라이버 에러");
				e.printStackTrace();
			} catch (SQLException e) {
				System.out.println("DB 연결 오류");
				e.printStackTrace();
			}
		}
	}
	
	public static Connection getDbConn() {
		return dbConn;
	}

	/**
	 * select문의 쿼리를 수행하여 결과를 ResultSet으로 반환
	 */
	public static ResultSet executeQuery(String sql) {
		try {
			stmt = dbConn.createStatement();
			return stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * select 문을 제외한 데이터를 추가, 삭제, 수정하는 SQL문 수행
	 * @throws SQLException 
	 */
	public static void executeUpdate(String sql) throws SQLException {
			stmt.executeUpdate(sql);
	}
	
	/**
	 *  크롤링한 데이터 DB에 넣기
	 */
	public static void insertGoods(ArrayList<GoodsVO> goods) {
		if(dbConn != null) {
			String sql = "insert into goods(id, goodsName, price, count, category) values(?, ?, ?, ?, ?)";
			try {
				PreparedStatement pst = dbConn.prepareStatement(sql);
				for(int i = 0; i < goods.size(); i++) {
					GoodsVO obj = goods.get(i);
					pst.setInt(1, obj.getId());
					pst.setString(2, obj.goodsName);
					pst.setInt(3, obj.price);
					pst.setInt(4, obj.count);
					pst.setString(5, obj.category);
					pst.executeUpdate();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
		}
	}
	
	public static ResultSet getGoods(PreparedStatement pst) throws SQLException {
		if(dbConn != null) {
				return pst.executeQuery();
		} else return null;
	}
	
	
	

}
