package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;

public class MyInfoDao {
	private MyInfoDao() {}
	private static MyInfoDao instance;

	public static MyInfoDao getInstance() {
		if(instance == null) {
			instance = new MyInfoDao();
		}
		return instance;
	}
	
	JDBCUtil jdbc = JDBCUtil.getInstance();

	//수강생 OR 지식공유자 정보 가져오기
	public Map<String, Object> getMyInfo(String userId) {
		String sql = "SELECT MEMBER_NAME,\r\n"
				+ "       CASE WHEN DOMAIN_NO=1 THEN '수강생'\r\n"
				+ "       ELSE '지식공유자'\r\n"
				+ "       END AS DOMIAN_NO,\r\n"
				+ "       MEMBER_HP,MEMBER_EMAIL,MEMBER_ADDRESS,MEMBER_ADDRESS_DETAIL,MEMBER_BANK,MEMBER_ACCOUNT\r\n"
				+ "  FROM MEMBER\r\n"
				+ " WHERE MEMBER_ID =? ";
		ArrayList<Object> param = new ArrayList<>();
		param.add(userId);
		return jdbc.selectOne(sql, param);
	}

	//이름수정
	public int updateName(String userId, String name) {
		String sql = "UPDATE MEMBER\r\n"
				+ "      SET MEMBER_NAME = ?\r\n"
				+ "    WHERE MEMBER_ID = ? ";
		ArrayList<Object> param = new ArrayList<>();
		param.add(name);
		param.add(userId);
		
		return jdbc.update(sql, param);
	}
	
	//핸드폰번호 수정
	public int updateHp(String userId, String hp) {
		String sql = "UPDATE MEMBER\r\n"
				+ "      SET MEMBER_HP = ?\r\n"
				+ "    WHERE MEMBER_ID = ? ";
		ArrayList<Object> param = new ArrayList<>();
		param.add(hp);
		param.add(userId);
		
		return jdbc.update(sql, param);
	}
	
	//이메일 수정
	public int updateEmail(String userId, String email) {
		String sql = "UPDATE MEMBER\r\n"
				+ "      SET MEMBER_EMAIL = ?\r\n"
				+ "    WHERE MEMBER_ID = ? ";
		ArrayList<Object> param = new ArrayList<>();
		param.add(email);
		param.add(userId);
		
		return jdbc.update(sql, param);
	}
	
	//주소 수정
	public int updateAddress(String userId, String address1, String address2) {
		String sql = "UPDATE MEMBER\r\n"
				+ "      SET MEMBER_ADDRESS = ?"
				+ "         ,MEMBER_ADDRESS_DETAIL =?"
				+ "    WHERE MEMBER_ID = ? ";
		ArrayList<Object> param = new ArrayList<>();
		param.add(address1);
		param.add(address2);
		param.add(userId);
		
		return jdbc.update(sql, param);
	}
	
	//은행, 계좌 수정
	public int updateBank(String userId, String bank, String account) {
		String sql = "UPDATE MEMBER\r\n"
				+ "      SET MEMBER_BANK = ?"
				+ "         ,MEMBER_ACCOUNT =?"
				+ "    WHERE MEMBER_ID = ? ";
		ArrayList<Object> param = new ArrayList<>();
		param.add(bank);
		param.add(account);
		param.add(userId);
		
		return jdbc.update(sql, param);
	}

	//지식공유자로 업그레이드
	public int upgrade(String userId) {
		String sql = "UPDATE MEMBER\r\n"
				+ "      SET DOMAIN_NO = 2\r\n"
				+ "    WHERE MEMBER_ID = ?  ";
		ArrayList<Object> param = new ArrayList<>();
		param.add(userId);
		return jdbc.update(sql, param);
	}

	//회원탈퇴
	public int memberQuit(String userId) {
		String sql = "UPDATE MEMBER"
				+ "      SET MEMBER_DELETE = 'Y'"
				+ "    WHERE MEMBER_ID = ?";
		ArrayList<Object> param = new ArrayList<>();
		param.add(userId);	
		return jdbc.update(sql, param);
	}

	
	//내 학습 목록(수강 중인 강의) 가져오기
	public List<Map<String, Object>> getStudyList(String userId) {
		String sql = "SELECT DISTINCT C.LECTURE_NO, B.LECTURE_CODE_NAME, A.LECTURE_NAME, A.LECTURE_PRICE, C.LEARN_PROGRESS "
				+" FROM LECTURE A, LECTURE_TYPE B, LEARN C, MY_LECTURE D, CART E "
				+" WHERE D.LECTURE_NO=C.LECTURE_NO "
				+" AND D.LECTURE_NO=A.LECTURE_NO "
				+" AND D.CART_NO=E.CART_NO "
				+" AND A.LECTURE_CODE=B.LECTURE_CODE "
				+" AND C.CART_NO=E.CART_NO "
				+" AND E.MEMBER_ID = ? "
				+" ORDER BY 1";
		ArrayList<Object> param = new ArrayList<>();
		param.add(userId);
		
		return jdbc.selectList(sql, param);
	}

	//강의 상세 조회(내가 수강중인 강의)
	public Map<String, Object> getLectureInfo(int lectureNo) {
		String sql = "SELECT DISTINCT C.LECTURE_NO,B.LECTURE_CODE_NAME, A.LECTURE_NAME, A.LECTURE_INTRO, A.LECTURE_TIME, A.CURRICULUM, C.LEARN_PROGRESS "
				 +"    FROM LECTURE A, LECTURE_TYPE B, LEARN C, MY_LECTURE D, CART E "
				 +"   WHERE A.LECTURE_NO=C.LECTURE_NO "
                 +"     AND A.LECTURE_CODE=B.LECTURE_CODE "
                 +"     AND E.CART_NO=C.CART_NO "
                 +"     AND C.LECTURE_NO = ? "
                 +"     AND E.MEMBER_ID = ?";
		ArrayList<Object> param = new ArrayList<>();
		param.add(lectureNo);
		param.add(Controller.loginUser.get("MEMBER_ID"));
		
		return jdbc.selectOne(sql, param);
	}


	
	
}












