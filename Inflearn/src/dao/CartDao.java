package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;

public class CartDao {

   private CartDao(){};
     
    private static CartDao instance;
    
     public static CartDao getInstance(){
      if (instance == null) {
         instance = new CartDao();
      }
      return instance;
   }
   JDBCUtil jdbc = JDBCUtil.getInstance();
   
   
   //장바구니조회 //모두 조회
      public List<Map<String, Object>> cartlist(String userId){
         
         String sql = "SELECT A.LECTURE_NO, B.LECTURE_NAME, B.LECTURE_PRICE, A.WISH_DATE"
               + "       FROM WISH_LIST A, LECTURE B"
               + "    WHERE A.LECTURE_NO = B.LECTURE_NO"
               + "      AND A.MEMBER_ID = ?";
                 
           
        List<Object> param = new ArrayList<>();
        param.add(userId);
        return jdbc.selectList(sql, param);
      }
      
      //장바구니에서 제거
      public int cartdelete(String userId, String lectureNo){
          
         String sql = "DELETE FROM WISH_LIST WHERE MEMBER_ID = ? AND LECTURE_NO = ?";
               
         List<Object> param = new ArrayList<>();
         param.add(userId);
         param.add(lectureNo);
         return jdbc.update(sql, param);
      }
      

      //장바구니에서 구매  //선택 조회
         public List<Map<String, Object>> cartbuy(String userId){
            
            String sql = "SELECT A.LECTURE_NO, B.LECTURE_NAME, B.LECTURE_PRICE, TO_CHAR(C.CART_DATE,'yyyy/MM/dd') AS CART_DATE, C.CART_NO, C.CART_PRICE "
            			+"	FROM MY_LECTURE A, LECTURE B, CART C "
            			+" WHERE A.LECTURE_NO = B.LECTURE_NO "
            			+"	 AND C.CART_NO = A.CART_NO "
            			+"	 AND C.MEMBER_ID = ? "
            			+"   AND C.CART_NO = ? "; 
              
           List<Object> param = new ArrayList<>();
           param.add(userId);
           param.add(Controller.cartNo);
           return jdbc.selectList(sql, param);
         }
      
      //주문하기 - 주문테이블에 주문번호, 회원아이디, 주문일자만 주문테이블에 넣기
         
      //주문강의에 주문번호, 강의코드 넣기 => 금액 합산해서 
      //주문금액 테이블에 넣기
      public int InsertMyLecture(String userId, Map<String, Object> lectures){
    	  String lectureNo = (String)lectures.get("LECTURE_NO");
         String sql = "INSERT INTO MY_LECTURE "
        		 	+"	VALUES(?,?)";
        		 			
         
      List<Object> param = new ArrayList<>();
      param.add(Controller.cartNo);
      param.add(lectureNo);
      return jdbc.update(sql, param);
      }
      
      //결제테이블 입력 
      public int InsertPay(int code){
         String sql = "INSERT INTO PAY VALUES"
               + " (?,?,SYSDATE,(SELECT CART_PRICE FROM CART WHERE CART_NO = ?),'Y',"
               + "(SELECT CART_PRICE*0.7 FROM CART WHERE CART_NO = ?))";
         
         List<Object> param = new ArrayList<>();
         param.add(Controller.cartNo);
         param.add(code);
         param.add(Controller.cartNo);
         param.add(Controller.cartNo);
         
         return jdbc.update(sql, param);
      }

      //강의번호가 강의테이블에 있는 자료인지 확인
	public Map<String, Object> checkLectureNo(String lectureNo) {
		String sql = "SELECT LECTURE_NO "
					  + " FROM LECTURE "
					  + " WHERE LECTURE_NO = ? ";
		ArrayList<Object> param = new ArrayList<>();
		param.add(lectureNo);
		return jdbc.selectOne(sql, param);
	}

	//주문테이블에 주문번호 입력
	public int insertCart(String userId) {
		 
		String sql = "INSERT INTO CART VALUES"
	               + " (?,?,SYSDATE,0)";
		ArrayList<Object> param = new ArrayList<>();
		param.add(Controller.cartNo);
		param.add(userId);
		
		return jdbc.update(sql, param);
	}

	//주문테이블에 주문금액 업데이트
	public int updateCartPrice() {
		String sql = "UPDATE CART C  "
					+"	 SET (C.CART_PRICE) = (SELECT B.CSUM  "
					+"			 				 FROM (SELECT A.CART_NO AS CNO, SUM(B.LECTURE_PRICE) AS CSUM  "
                    +"             					     FROM MY_LECTURE A, LECTURE B, CART C   "
                    +"              				     WHERE A.CART_NO=C.CART_NO  "
                    +"          				           AND A.LECTURE_NO=B.LECTURE_NO  "
                    +"             					     GROUP BY A.CART_NO) B  "
                    +"     						     	  WHERE B.CNO=C.CART_NO)  "
                    +"  WHERE C.CART_NO = ?";
		ArrayList<Object> param = new ArrayList<>();
		param.add(Controller.cartNo);
		return jdbc.update(sql, param);
	}
	
      
     
      
      
      
}