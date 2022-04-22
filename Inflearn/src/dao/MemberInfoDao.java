package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class MemberInfoDao {

	private MemberInfoDao() {}
	private static MemberInfoDao instance;
	public static MemberInfoDao getInstance() {
		if(instance == null) {
			instance = new MemberInfoDao();
		}
		return instance;
	}
	
	private static JDBCUtil jdbc = JDBCUtil.getInstance();
	
	//수강생 정보 불러오기
	public static List<Map<String, Object>>selectStudentList(){
		String sql = "SELECT A.MEMBER_NAME, A.MEMBER_ID, A.MEMBER_EMAIL, A.MEMBER_HP, A.MEMBER_DELETE"
                      +"FROM MEMBER A, DOMAIN_TABLE B"
                      +"WHERE B.DOMAIN_NO = 1";
		return jdbc.selectList(sql);
	}
	
	
	
	//지식공유자 정보 불러오기
	public static List<Map<String, Object>>selectMentoList(){
		String sql = "SELECT A.MEMBER_NAME, A.MEMBER_ID, A.MEMBER_EMAIL, A.MEMBER_HP, A.MEMBER_DELETE"
                      +"FROM MEMBER A, DOMAIN_TABLE B"
                      +"WHERE B.DOMAIN_NO = 2";
		return jdbc.selectList(sql);
	}
	
}
