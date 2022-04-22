package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class PayDao {

   private PayDao(){};
     
    private static PayDao instance;
    
     public static PayDao getInstance(){
      if (instance == null) {
         instance = new PayDao();
      }
      return instance;
   }
   JDBCUtil jdbc = JDBCUtil.getInstance();
   

   //결제내역 조회
   public List<Map<String, Object>> PayDetailList(Object userId){
      
      String sql = "SELECT A.CART_NO, C.DOMAIN_NAME, TO_CHAR(A.PAY_DATE,'yyyy-mm-dd') AS PDATE, A.PAY_PRICE, A.PAY_STATUS"
            + "     FROM PAY A, CART B, DOMAIN_TABLE C"
            + "    WHERE A.CART_NO = B.CART_NO"
            + "      AND A.DOMAIN_NO = C.DOMAIN_NO"
            + "      AND B.MEMBER_ID = ?";
           
   List<Object> param = new ArrayList<>();
   
   param.add(userId);
   return jdbc.selectList(sql, param);
}
}