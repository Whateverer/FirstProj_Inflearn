package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class AdminDao {

   private AdminDao(){}
   private static AdminDao instance;
   public static AdminDao getInstance(){
      if(instance == null){
         instance = new AdminDao();
      }
      return instance;
   }
   
   JDBCUtil jdbc = JDBCUtil.getInstance();
  
   
   //회원 목록 불러오기
   public List<Map<String, Object>> selectStudentList(int type) {
      String sql =  " SELECT MEMBER_NAME, MEMBER_ID, MEMBER_EMAIL, MEMBER_HP, "
                  +" CASE WHEN MEMBER_DELETE = 'Y' THEN '탈퇴회원' "
                  +"  WHEN MEMBER_DELETE = 'N' THEN '정상회원' "
                  +"  END AS MEMBER_DELETE "
                  +"  FROM MEMBER "
                  +"   WHERE DOMAIN_NO = ?"; 
        
      ArrayList<Object> param = new ArrayList<>();
      param.add(type);
      return jdbc.selectList(sql, param);
      
   }
  
   
   //수강생 상세 조회
   public Map<String, Object> studentList(String userId){
      String sql =  "SELECT B.MEMBER_ID,A.MEMBER_NAME,COUNT(B.LECTURE_NO) AS LCNT,A.MEMBER_EMAIL,A.MEMBER_HP,A.MEMBER_DELETE"
         +         " FROM MEMBER A, LECTURE B"
         +          " WHERE A.MEMBER_ID = ?"
         +            " AND A.DOMAIN_NO = 1"   
         +            " AND A.MEMBER_DELETE = 'N'"
         +          " GROUP BY B.MEMBER_ID,A.MEMBER_NAME,A.MEMBER_EMAIL, A.MEMBER_HP, A.MEMBER_DELETE";
             
      ArrayList<Object> param = new ArrayList<>();
      param.add(userId);
      return jdbc.selectOne(sql, param);
   }
   
   
  
   
   //지식공유자 상세 조회
   public Map<String, Object> selectmentoList(String userId){
      System.out.println(userId);
      String sql =  "SELECT A.MEMBER_ID,A.MEMBER_NAME,COUNT(B.LECTURE_NO) AS LCNT,A.MEMBER_EMAIL,A.MEMBER_HP,A.MEMBER_DELETE"
                +"    FROM MEMBER A, LECTURE B"
                +"   WHERE ? = B.MEMBER_ID"
                +"     AND A.DOMAIN_NO = 2"
                + "       AND A.MEMBER_DELETE = 'N'"  // 도메인 넘버가 없어서 임시로 2로 해놓음
                +" GROUP BY A.MEMBER_ID,A.MEMBER_NAME,A.MEMBER_EMAIL,A.MEMBER_HP,A.MEMBER_DELETE";
      ArrayList<Object> param = new ArrayList<>();
      param.add(userId);
      return jdbc.selectOne(sql, param);
      
   }
   
   //강의코드, 강의 분류, 강의명, 진도율을 리스트 - 지식공유자
   public List<Map<String, Object>> mentoDetailList(String userId){
      String sql = "SELECT A.LECTURE_CODE, A.LECTURE_NO, A.LECTURE_NAME, B.LEARN_PROGRESS"
            +        " FROM LECTURE A, LEARN B"
            +       " WHERE A.LECTURE_NO = B.LECTURE_NO"
            +         " AND A.MEMBER_ID = ?";
      ArrayList<Object> param = new ArrayList<>();
      param.add(userId);
      
      return jdbc.selectList(sql, param);

   }
   
   //누적누적
   public List<Map<String, Object>> lectsellist(){
      String sql =    "SELECT LECTURE_CODE, LECTURE_NAME, COUNT(A.MEMBER_ID) AS SSUM, COUNT(A.LECTURE_PRICE) AS SPAY"
            +          " FROM LECTURE A, LEARN B"
            +         " WHERE A.LECTURE_NO = B.LECTURE_NO"
            +         " GROUP BY LECTURE_CODE, LECTURE_NAME";
      
    ArrayList<Object> param = new ArrayList<>();
    return jdbc.selectList(sql);
   }

}