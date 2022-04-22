package dao;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import util.JDBCUtil;

public class UserDao {

   private UserDao(){}
   private static UserDao instance;
   public static UserDao getInstance(){
      if(instance == null){
         instance = new UserDao();
      }
      return instance;
   }
   
   private JDBCUtil jdbc = JDBCUtil.getInstance();
   
   public int insertUser(Map<String, Object>param){
      String sql = "INSERT INTO MEMBER VALUES(?,1,?,?,?,?,?,?,?,?,?,'N')";
      
      List<Object> p = new ArrayList<>();
      p.add(param.get("MEMBER_ID"));
      p.add(param.get("MEMBER_PASSWORD"));
      p.add(param.get("MEMBER_NAME"));
      p.add(param.get("MEMBER_HP"));
      p.add(param.get("MEMBER_EMAIL"));
      p.add(param.get("MEMBER_POSTCODE"));
      p.add(param.get("MEMBER_ADDRESS"));
      p.add(param.get("MEMBER_ADDRESS_DETAL"));
      p.add(param.get("MEMBER_BANK"));
      p.add(param.get("MEMBER_ACCOUNT"));
      
      
      return jdbc.update(sql, p);
      
   }
   
   public Map<String, Object> selectUser2(String userId){
      String sql1 = "SELECT MEMBER_ID, MEMBER_PASSWORD, MEMBER_NAME, MEMBER_HP, MEMBER_POSTCODE, MEMBER_ADDRESS, MEMBER_ADDRESS_DETAIL, MEMBER_DELETE "
            + "     FROM MEMBER "
            + "    WHERE MEMBER_ID = ?";
      
      List<Object> param = new ArrayList<>();
      param.add(userId);
      
      return jdbc.selectOne(sql1, param);
   }
   
   public Map<String, Object> checkId(String userId){
      String sql2 = "SELECT MEMBER_ID, MEMBER_PASSWORD, MEMBER_NAME, MEMBER_HP, MEMBER_POSTCODE, MEMBER_ADDRESS, MEMBER_ADDRESS_DETAIL, MEMBER_DELETE "
               + "     FROM MEMBER "
            + "    WHERE MEMBER_ID = ?";
      
      List<Object> param = new ArrayList<>();
      param.add(userId);
      
      return jdbc.selectOne(sql2, param);
   
   }
   
   //수강생 조회
      public Map<String, Object> selectUser(String userId, String password) {
         String sql = "SELECT MEMBER_ID, DOMAIN_NO, MEMBER_PASSWORD, MEMBER_NAME, MEMBER_HP, MEMBER_EMAIL, MEMBER_POSTCODE, MEMBER_ADDRESS,MEMBER_ADDRESS_DETAIL, MEMBER_BANK, MEMBER_ACCOUNT, MEMBER_DELETE"
               + "     FROM MEMBER"
               + "    WHERE MEMBER_ID = ?"
               + "      AND MEMBER_PASSWORD = ?"
               + "      AND DOMAIN_NO = 1";
                
         List<Object> param = new ArrayList<>();
         param.add(userId);
         param.add(password);
         Map<String,Object> map = jdbc.selectOne(sql, param);
         
      //지식공유자 조회   
         if(map==null){
            sql = "SELECT MEMBER_ID, DOMAIN_NO, MEMBER_PASSWORD, MEMBER_NAME, MEMBER_HP, MEMBER_EMAIL, MEMBER_POSTCODE, MEMBER_ADDRESS,MEMBER_ADDRESS_DETAIL, MEMBER_BANK, MEMBER_ACCOUNT, MEMBER_DELETE"
                  + "     FROM MEMBER"
                  + "    WHERE MEMBER_ID = ?"
                  + "      AND MEMBER_PASSWORD = ?"
                  + "      AND DOMAIN_NO = 2";
         
         }

         return jdbc.selectOne(sql, param);
      
         }

      
      //관리자 로그인
      public Map<String, Object> manager() {   
         String sql = "SELECT MANAGER_ID, MANAGER_PASSWORD, MANAGER_NAME"
               +     " FROM MANAGER";   
         return jdbc.selectOne(sql);
         
      }

      //주문번호 가져오기
      public String getCartNo() {
         String sql = "select fn_create_cartno as cartno from dual";
         Map<String, Object> cartNo = jdbc.selectOne(sql);
         
         return (String)cartNo.get("CARTNO");
      }

}