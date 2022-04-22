package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class MyBoardDao {
	private MyBoardDao(){}
	private static MyBoardDao instance;       
	public static MyBoardDao getInstance(){
		if(instance == null){
			instance = new MyBoardDao();
		}
		return instance;
	}
	JDBCUtil jdbc = JDBCUtil.getInstance();
	
	public List<Map<String, Object>> MyCommunityList(String userId){
    	String sql = "SELECT BOARD_NO, BOARD_TITLE,  MEMBER_ID, BOARD_DATE"
    			+ "     FROM COMMUNITY"
    			+ "    WHERE MEMBER_ID = ? "
    			+ "    ORDER BY 1 DESC";
    	
    	List<Object> param = new ArrayList<>();
    	param.add(userId);
    	
    	 return jdbc.selectList(sql, param);
    }
	
}
