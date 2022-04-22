package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class OrderDao {
   
     private OrderDao(){};
     
      private static OrderDao instance;
      
       public static OrderDao getInstance(){
        if (instance == null) {
           instance = new OrderDao();
        }
        return instance;
     }
     JDBCUtil jdbc = JDBCUtil.getInstance();
     
     //내 주문내역 조회
     public List<Map<String, Object>> OrderList(String userId){
        String sql = "SELECT CART_NO, CART_DATE, CART_PRICE"
              + "     FROM CART"
              + "    WHERE MEMBER_ID = ?"
              + "    ORDER BY 1";
        
     List<Object> param = new ArrayList<>();
     param.add(userId);
     return jdbc.selectList(sql, param);
     }
   
     //주문내역 상세조회
     public List<Map<String, Object>> OrderDetailList(String userId, String orderInput){
        String sql = "SELECT B.CART_NO, A.LECTURE_NO, C.LECTURE_NAME, C.LECTURE_PRICE"
             + "     FROM WISH_LIST A, CART B, LECTURE C"
              + "    WHERE A.MEMBER_ID = B.MEMBER_ID"
             + "      AND A.LECTURE_NO = C.LECTURE_NO"
              + "      AND B.MEMBER_ID = ?"
              + "      AND B.CART_NO = ?";
        
        List<Object> param = new ArrayList<>();
        param.add(userId);
        param.add(orderInput);
        
        return jdbc.selectList(sql, param);
     }
     
     
     
     
     
     
     
     
     
     
     
     
     
     
}