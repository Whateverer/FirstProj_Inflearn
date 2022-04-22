package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class LectureDao {
    //싱글톤
	private LectureDao() {}
	private static LectureDao instance;
	public static LectureDao getInstance() {
		if(instance == null) {
			instance = new LectureDao();
		}
		return instance;
	}
	
	JDBCUtil jdbc = JDBCUtil.getInstance();
	
	//강의목록 불러오기 - 지식공유회원의 이름까지 불러오기(승인된 강의만)
	public List<Map<String, Object>> selectLectureList() {
		String sql = "SELECT A.LECTURE_NO,A.LECTURE_NAME,B.MEMBER_NAME,A.LECTURE_PRICE,NVL(C.SAVG,0) AS SAVG ,A.LECTURE_TIME"
				+ "  FROM LECTURE A\r\n"
				+ "  LEFT OUTER JOIN MEMBER B ON A.MEMBER_ID=B.MEMBER_ID\r\n"
				+ "  LEFT OUTER JOIN (SELECT A.LECTURE_NO AS LNO,ROUND(AVG(C.AFTER_STAR)) AS SAVG\r\n"
				+ "                               FROM LECTURE A, LEARN B, AFTER_LECTURE C \r\n"
				+ "                              WHERE A.LECTURE_NO=B.LECTURE_NO\r\n"
				+ "                                AND B.LEARN_NO=C.LEARN_NO\r\n"
				+ "                              GROUP BY A.LECTURE_NO) C ON C.LNO = A.LECTURE_NO"
				+ "  WHERE LECTURE_ALLOW = 'Y'";
		
		ArrayList<Object> param = new ArrayList<>();
		return jdbc.selectList(sql, param);
	}
	
	//강의 상세조회
	public Map<String, Object> selectLectureOne(int lectureNo){
		//강의 상세조회 
		String sql = "SELECT A.LECTURE_NAME, B.LECTURE_CODE_NAME, A.LECTURE_PRICE, A.LECTURE_TIME, A.LECTURE_INTRO, A.CURRICULUM \r\n"
				+ "     FROM LECTURE A, LECTURE_TYPE B\r\n"
				+ "    WHERE A.LECTURE_CODE=B.LECTURE_CODE"
				+ "      AND A.LECTURE_NO = ?";
		
		ArrayList<Object> param = new ArrayList<>();
		param.add(lectureNo);
		return jdbc.selectOne(sql, param);
	}
	
	//강의 별점 조회
	public Map<String, Object> selectLectureStar(int lectureNo){
		
		String sql = " SELECT A.LECTURE_NO,NVL(ROUND(AVG(C.AFTER_STAR)),0) AS SAVG "
				    +"    FROM LECTURE A "
                    +" LEFT OUTER JOIN LEARN B ON A.LECTURE_NO=B.LECTURE_NO "
                    +" LEFT OUTER JOIN AFTER_LECTURE C ON B.LEARN_NO=C.LEARN_NO "
				    +" WHERE A.LECTURE_NO = ? "
				    +" GROUP BY A.LECTURE_NO ";
				   
				    
		ArrayList<Object> param = new ArrayList<>();
		
		param.add(lectureNo);
		return jdbc.selectOne(sql, param);
	}
	
	//강의 후기 조회
	public List<Map<String, Object>> selectLectureReview(int lectureNo){
		
		String sql = "SELECT A.LECTURE_NO,C.AFTER_REVIEW\r\n"
				+ "     FROM LECTURE A, LEARN B, AFTER_LECTURE C \r\n"
				+ "    WHERE A.LECTURE_NO=B.LECTURE_NO\r\n"
				+ "      AND B.LEARN_NO=C.LEARN_NO\r\n"
				+ "      AND A.LECTURE_NO = ?";
		ArrayList<Object> param = new ArrayList<>();
		
		param.add(lectureNo);
		return jdbc.selectList(sql, param);
	}
	
	//수강중인 강의인지 확인
	public Map<String, Object> checkLearn(int lectureNo, String userId){
		String sql = "SELECT B.MEMBER_ID  "
				+"		FROM LEARN A, CART B, PAY C "
				+"	   WHERE A.CART_NO=B.CART_NO  "
				+"	   	 AND B.CART_NO=C.CART_NO "
				+"	   	 AND LECTURE_NO = ?     "
				+"	     AND B.MEMBER_ID = ?";
		ArrayList<Object> param = new ArrayList<>();
		param.add(lectureNo);
		param.add(userId);
		return jdbc.selectOne(sql, param);
	}

	//내가 올린 강의 목록
	public List<Map<String, Object>> getMyLecture(String userId) {
		String sql = "SELECT A.LECTURE_NO, A.LECTURE_NAME, COUNT(B.LECTURE_NO) AS LCNT, NVL(SUM(C.PAY_CHARGE),0) AS PSUM "
				 + " FROM LECTURE A "
                 + " LEFT OUTER JOIN MY_LECTURE B ON A.LECTURE_NO=B.LECTURE_NO "
                 + " LEFT OUTER JOIN CART D ON D.CART_NO=B.CART_NO "
                 + " LEFT OUTER JOIN PAY C ON C.CART_NO=D.CART_NO "
                 + " LEFT OUTER JOIN LEARN E ON E.CART_NO=D.CART_NO "
				 + " WHERE A.MEMBER_ID = ? "
				 + " GROUP BY A.LECTURE_NO,A.LECTURE_NAME ";
		ArrayList<Object> param = new ArrayList<>();
		param.add(userId);
		
		return jdbc.selectList(sql, param);
	}	
	
	//지식공유자가 자기 강의 상세조회
	public Map<String, Object> myLectureDetail(int lectureNo){
		//강의 상세조회 
		String sql = "SELECT LECTURE_NO, B.LECTURE_CODE_NAME, LECTURE_NAME, LECTURE_INTRO, CURRICULUM, LECTURE_TIME, LECTURE_PRICE\r\n"
				+ "     FROM LECTURE A, LECTURE_TYPE B\r\n"
				+ "    WHERE A.LECTURE_CODE=B.LECTURE_CODE\r\n"
				+ "      AND LECTURE_NO = ?";
		
		ArrayList<Object> param = new ArrayList<>();
		param.add(lectureNo);
		return jdbc.selectOne(sql, param);
	}

	//수정요청
	public int askUpdate(int lectureNo) {
		String sql = "UPDATE LECTURE\r\n"
				+ "      SET LECTURE_REVISE_ALLOW ='N'\r\n"
				+ "    WHERE LECTURE_NO = ?  ";
		ArrayList<Object> param = new ArrayList<>();
		param.add(lectureNo);
		return jdbc.update(sql, param);
		
	}

	//강의 업로드
	public int insertLecture(String lectureName, int lectureType, int lecturePrice, int lectureTime, String userId, String curriclum, String lectureIntro, String lectureContent) {
		String sql = "INSERT INTO LECTURE\r\n"
				+ "   VALUES ((SELECT NVL(MAX(LECTURE_NO), 0)+1 FROM LECTURE),?,?,?,?,?,'N',?,?,?,'')";
		ArrayList<Object> param = new ArrayList<>();
		param.add(lectureType);
		param.add(lectureName);
		param.add(lecturePrice);
		param.add(lectureTime);
		param.add(userId);
		param.add(curriclum);
		param.add(lectureIntro);
		param.add(lectureContent);
		
		return jdbc.update(sql, param);
	}

	//강의 수정 - 강의명, 강의분류, 강의가격만 수정 가능하게
	//강의명 수정
	public int nameUpdate(int lectureNo, String name) {
		String sql = "UPDATE LECTURE"
				+ "      SET LECTURE_NAME = ?"
				+ "    WHERE LECTURE_NO = ?";
		ArrayList<Object> param = new ArrayList<>();
		param.add(name);
		param.add(lectureNo);
		return jdbc.update(sql, param);
	}

	//강의분류 수정
	public int typeUpdate(int lectureNo, String type) {
		String sql = "UPDATE LECTURE"
				+ "      SET LECTURE_TYPE = ?"
				+ "    WHERE LECTURE_NO = ?";
		ArrayList<Object> param = new ArrayList<>();
		param.add(type);
		param.add(lectureNo);
		return jdbc.update(sql, param);
	}

	//강의가격 수정
	public int priceUpdate(int lectureNo, int price) {
		String sql = "UPDATE LECTURE"
				+ "      SET LECTURE_PRICE = ?"
				+ "    WHERE LECTURE_NO = ?";
		ArrayList<Object> param = new ArrayList<>();
		param.add(price);
		param.add(lectureNo);
		return jdbc.update(sql, param);
	}
	
	//강의 삭제
	public int deleteLecture(int lectureNo) {
		String sql = "DELETE LECTURE"
				+ "   WHERE LECTURE_NO = ?";
		ArrayList<Object> param = new ArrayList<>();
		param.add(lectureNo);
		return jdbc.update(sql, param);
	}

	//장바구니에 담기
	public int insertWishList(String userId, int lectureNo) {
		String sql = "INSERT INTO WISH_LIST\r\n"
				+ "   VALUES (?,?,SYSDATE)";
		ArrayList<Object> param = new ArrayList<>();
		param.add(userId);
		param.add(lectureNo);
		return jdbc.update(sql, param);
	}

	//업로드 대기강의 가져오기 - 쿼리 작성
	public List<Map<String, Object>> getUploadWaitList() {
		String sql = "SELECT A.LECTURE_CODE_NAME, B.LECTURE_NO, B.LECTURE_NAME, B.LECTURE_PRICE, B.LECTURE_TIME "
                      + " FROM LECTURE_TYPE A, LECTURE B"
                      + " WHERE B.LECTURE_ALLOW = 'N' "
                      +   " AND A.LECTURE_CODE=B.LECTURE_CODE ";
		ArrayList<Object> param = new ArrayList<>();
		return jdbc.selectList(sql, param);
	}

	//수정 대기강의 목록 가져오기 - 쿼리 작성
	public List<Map<String, Object>> getReviseWaitList() {
		String sql = "SELECT A.LECTURE_CODE_NAME, B.LECTURE_NO, B.LECTURE_NAME, B.LECTURE_PRICE, B.LECTURE_TIME "
                + " FROM LECTURE_TYPE A, LECTURE B"
                + " WHERE B.LECTURE_REVISE_ALLOW = 'N'  "
                +   " AND A.LECTURE_CODE=B.LECTURE_CODE ";
		ArrayList<Object> param = new ArrayList<>();
		return jdbc.selectList(sql, param);
		
	}

	//업로드 대기강의 상세
	public Map<String, Object> uploadDetail(int lectureNo) {
		String sql = "SELECT A.LECTURE_NO, B.LECTURE_CODE_NAME, A.LECTURE_NAME, A.LECTURE_INTRO, A.CURRICULUM, A.LECTURE_TIME, A.LECTURE_PRICE"
					+"  FROM LECTURE A, LECTURE_TYPE B"
					+"  WHERE A.LECTURE_NO = ? ";
							
		ArrayList<Object> param = new ArrayList<>();
		param.add(lectureNo);
		return jdbc.selectOne(sql, param);
	}

	//업로드 승인하기
	public int uploadApprove(int lectureNo) {
		String sql = "UPDATE LECTURE "
					+"	 SET LECTURE_ALLOW = 'Y'"
					+" WHERE LECTURE_NO = ?  ";
		ArrayList<Object> param = new ArrayList<>();
		param.add(lectureNo);
		return jdbc.update(sql, param);
	}

	//수정 승인하기
	public int reviseApprove(int lectureNo) {
		String sql = "UPDATE LECTURE "
				+"	     SET LECTURE_REVISE_ALLOW = null"
				+"     WHERE LECTURE_NO = ?  ";
		ArrayList<Object> param = new ArrayList<>();
		
		param.add(lectureNo);
		return jdbc.update(sql, param);
	}
	
	

//	//수정 대기강의 상세
//	public Map<String, Object> reviseDetail(int lectureNo) {
//		String sql = "";
//		ArrayList<Object> param = new ArrayList<>();
//		
//		return jdbc.selectOne(sql, param);
//	}

	
}
