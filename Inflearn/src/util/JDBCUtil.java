package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUtil {
	
	//싱글톤 패턴 : 인스턴스의 생성을 제한하여 하나의 인스턴스만 사용하는 디자인 패턴
	//객체가 여러 개일 필요가 없는 경우 - 구조적으로 객체를 하나만 만들도록 설계함
	
	private JDBCUtil(){//생성자에 private을 붙이면 다른 클래스에서 객체생성 불가
		
	}
	
	private static JDBCUtil instance;
	
	public static JDBCUtil getInstance() {//다른 클래스에서 불러올 때
		if(instance == null) {//변수가 null일 때만 객체를 생성
			instance = new JDBCUtil();//객체를 생성해서 instance에 보관
		}
		return instance;//return 받아서 사용가능
	}//이런 형태가 싱클톤 패턴(객체를 하나만 가지기 때문)
	
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String user = "inflearn";
	private String password = "java";
	
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	/*
	 * Map<String, Object> selectOne(String sql)//한 줄을 select해주는 메서드(쿼리 안에 ?가 없다는 것)
	 * Map<String, Object> selectOne(String sql, List<Object> param) 쿼리와 다른 무언가를 넘기는 것 - ?가 있다는 것
	 * List<Map<String, Object>> selectList(String sql)//조회되는 결과가 여러 줄
	 * List<Map<String, Object>> selectList(String sql, List<Object> param)
	 * int update(String sql)//insert, update, delete를 하고싶을 때
	 * int update(String sql, List<Object> param)
	 */
	
	//1. 한 줄 불러오는 메서드
	public Map<String, Object> selectOne(String sql){
		Map<String, Object> map = null;
		
		try {
			con = DriverManager.getConnection(url, user, password);
			
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			if(rs.next()) {
				map = new HashMap<>();
				for(int i = 1; i <= columnCount; i++) {
					map.put(metaData.getColumnName(i), rs.getObject(i));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try {rs.close();} catch(Exception e) {}
			if (ps != null) try {ps.close();} catch(Exception e) {}
			if (con != null) try {con.close();} catch(Exception e) {}
		}
		return map;
	}
	
	
	//2. 한 줄 불러오는 메서드 with ?
	public Map<String, Object> selectOne(String sql, List<Object> param){
		Map<String, Object> map = null;
		
		try {
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			
			for(int i = 0; i <param.size(); i++) {
				ps.setObject(i + 1, param.get(i));
			}
			rs = ps.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			if(rs.next()){
				map = new HashMap<>();
				for(int i = 1; i <= columnCount; i++) {
					map.put(metaData.getColumnName(i), rs.getObject(i));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try {rs.close();} catch(Exception e) {}
			if (ps != null) try {ps.close();} catch(Exception e) {}
			if (con != null) try {con.close();} catch(Exception e) {}
		}return map;
	}
	
	//3. 리스트를 불러오는 메서드
	public List<Map<String, Object>> selectList(String sql){
		List<Map<String, Object>> list = new ArrayList<>();
		
		try {
			con = DriverManager.getConnection(url, user, password);
			
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while(rs.next()) {
				HashMap<String, Object> map = new HashMap<>();
				for(int i = 1; i <= columnCount; i++) {
					map.put(metaData.getColumnName(i), rs.getObject(i));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try {rs.close();} catch(Exception e) {}
			if (ps != null) try {ps.close();} catch(Exception e) {}
			if (con != null) try {con.close();} catch(Exception e) {}
		}
		return list;
	}
	
	//4. 리스트 불러오는 메서드 with ?
	public List<Map<String, Object>> selectList(String sql, List<Object> param){
		List<Map<String, Object>> list= new ArrayList<>();
		
		try {
			con = DriverManager.getConnection(url, user, password);
			
			ps = con.prepareStatement(sql);
			for(int i = 0; i < param.size(); i++) {
				ps.setObject(i + 1, param.get(i)); // ?는 인덱스가 1부터 시작하기 때문에 i + 1을 넣음
			}
			
			rs = ps.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while(rs.next()) {
				//hashMap을 만들어서 데이터를 만들고 list에 넣기
				HashMap<String, Object> map = new HashMap<>();//한 줄의 데이터를 담기 위해 hashMap 만듦
				for(int i = 1; i <= columnCount; i++) { //columnCount는 반드시 크거나 같음!!!!
					map.put(metaData.getColumnName(i), rs.getObject(i));//모든 컬럼에 접근, 모든 컬럼의 데이터를 map에 저장(키(컬럼명), 값)
				}//metaData.getColumnName() : 컬럼명을 가져오는 메서드
				list.add(map);//한 줄 가져왔으면 ArrayList에 저장
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try {rs.close();} catch(Exception e) {}
			if (ps != null) try {ps.close();} catch(Exception e) {}
			if (con != null) try {con.close();} catch(Exception e) {}
		}
		
		return list; //모든 데이터가 들어간 것을 리턴
	}
	
	//5. update 메서드 
	public int update(String sql) {
		int result = 0;
		try {
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if (ps != null) try {ps.close();} catch(Exception e) {}
			if (con != null) try {con.close();} catch(Exception e) {}
		}
		return result;
	}
	
	//6. update 메서드 with ?
	public int update(String sql, List<Object> param) {
		int result = 0;
		try {
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			for(int i = 0; i < param.size(); i++) {
				ps.setObject(i + 1, param.get(i));
			}
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if (ps != null) try {ps.close();} catch(Exception e) {}
			if (con != null) try {con.close();} catch(Exception e) {}
		}
		return result;
	}
	
	
	//컬럼명 출력하는 메서드
	


}








