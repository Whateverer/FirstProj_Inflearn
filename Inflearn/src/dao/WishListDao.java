package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class WishListDao {
	//싱글톤
	private WishListDao() {}
	private static WishListDao instance;
	public static WishListDao getInstance() {
		if(instance == null) {
			instance = new WishListDao();
		}
		return instance;
	}

	JDBCUtil jdbc = JDBCUtil.getInstance();
	
	//강의바구니에 강의가 이미 존재하는지 확인
		public Map<String, Object> checkWishList(int lectureNo){
			
			String sql = "SELECT MEMBER_ID\r\n"
					+ "     FROM WISH_LIST\r\n"
					+ "    WHERE LECTURE_NO = ? ";
			
			
			ArrayList<Object> param = new ArrayList<>();
			param.add(lectureNo);
			return jdbc.selectOne(sql, param);
		}
		
		public Map<String, Object> checkWishList1(int lectureNo){
			String sql = "SELECT COUNT(MEMBER_ID) AS CNT FROM WISH_LIST WHERE LECTURE_NO = ?";
			ArrayList<Object> param = new ArrayList<>();
			param.add(lectureNo);
			
			return jdbc.selectOne(sql,param);
		}
			
		
	
		
}
