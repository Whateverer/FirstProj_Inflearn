package dao;


import java.util.ArrayList;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;

public class StudyDao {
	//싱글톤패턴
		private StudyDao() {}
		private static StudyDao instance;

		public static StudyDao getInstance() {
			if(instance == null) {
				instance = new StudyDao();
			}
			return instance;
		}
		
		JDBCUtil jdbc = JDBCUtil.getInstance();

		//내 강의 내용 불러오기
		public Map<String, Object> getStudyContent(int lectureNo) {
			String sql = "SELECT LECTURE_CONTENT\r\n"
					+ "     FROM LECTURE\r\n"
					+ "    WHERE LECTURE_NO = ? ";
			ArrayList<Object> param = new ArrayList<>();
			param.add(lectureNo);
			
				
			return jdbc.selectOne(sql, param);
		}

		//수강테이블에 자료입력
		public int insertLecture(String lectureNo) {
			String sql = "INSERT INTO LEARN "
						 +" VALUES(SEQ_LEA.NEXTVAL,sysdate,0,?,?)";
			ArrayList<Object> param = new ArrayList<>();
			
			param.add(Controller.cartNo);
			param.add(lectureNo);
			return jdbc.update(sql, param);
		}

		//진도율 증가시키기 
		public int updateProgress(String userId, int lectureNo) {
			String sql = "UPDATE LEARN "
						+"	  SET LEARN_PROGRESS = LEARN_PROGRESS + 10 "
						+"	WHERE LECTURE_NO = (SELECT B.LECTURE_NO "
						+"						  FROM (SELECT A.CART_NO AS CNO "
						+"								  FROM CART A  "
						+"								 WHERE MEMBER_ID = ?)A, MY_LECTURE B, PAY C "
						+"						  WHERE A.CNO=B.CART_NO "
						+"						    AND C.CART_NO=B.CART_NO "
						+"						    AND B.LECTURE_NO = ?)";
			ArrayList<Object> param = new ArrayList<>();
			
			param.add(userId);
			param.add(lectureNo);
			return jdbc.update(sql, param);
		}

		public Map<String, Object> getProgress(String userId, int lectureNo) {
			String sql = "SELECT A.LEARN_PROGRESS, A.LEARN_NO  "
						+"    FROM LEARN A, CART B, PAY C, LEARN D "
						+"   WHERE C.CART_NO=A.CART_NO "
						+"     AND B.CART_NO=C.CART_NO "
						+"     AND A.CART_NO = D.CART_NO "
						+"     AND B.MEMBER_ID = ? "
						+"    		 AND D.LECTURE_NO = ?";
			ArrayList<Object> param = new ArrayList<>();
			param.add(userId);
			param.add(lectureNo);
			return jdbc.selectOne(sql, param);
		}

		public int insertReview(int learnNo, String review, int star) {
			String sql = "INSERT INTO AFTER_LECTURE "
						+"   VALUES (?,?,?)";
			ArrayList<Object> param = new ArrayList<>();
			
			param.add(learnNo);
			param.add(review);
			param.add(star);
			
			return jdbc.update(sql, param);
		}

		

	
		
		
		
}
