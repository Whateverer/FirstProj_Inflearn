package service;

import java.util.List;
import java.util.Map;

import util.ScanUtil;
import util.View;
import controller.Controller;
import dao.OrderDao;

import java.text.SimpleDateFormat;

public class OrderService {
   
   
   private OrderService(){};
   
   private static OrderService instance;
   
   public static OrderService getInstance(){
      if (instance == null) {
         instance = new OrderService();
      }
      return instance;
   }
   
   private OrderDao orderDao = OrderDao.getInstance();
   
   //주문, 결제내역 홈
   public int orderAndPayList() {
	   System.out.println("1.주문내역 조회 2.결제내역 조회 0.뒤로가기");
	   int input = ScanUtil.nextInt();
	   switch(input){
	   case 1: return View.ORDER_LIST;
	   case 2: return View.PAY_LIST;
	   case 0: return View.MYINFO;
	   }
	   return View.ORDER_PAY;
   }
   
   //주문내역 조회
   public int OrderList(){
      SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
      while(true){
         String userId = (String)Controller.loginUser.get("MEMBER_ID");
         
         List<Map<String, Object>> orderlist = orderDao.OrderList(userId);
         
         if(orderlist.size() > 0){
         System.out.println("===================나의 주문내역===================");
         System.out.println("주문번호\t\t주문일자\t\t주문금액");
         
         for(Map<String, Object> order : orderlist){
            String time1 = format.format(order.get("CART_DATE")); 
            System.out.println(order.get("CART_NO")
                  + "\t" + time1
                  + "\t" +order.get("CART_PRICE"));
         }
         System.out.println("===============================================");
         System.out.println("1.주문번호 상세조회 0.뒤로가기");
         int input = ScanUtil.nextInt();
         
         switch(input){
         
         case 1: OrderDetail();  //주문내역 상세 조회 view생성
         
         case 0:
            return View.ORDER_PAY;
            
         }
         }else{
            System.out.println("주문내역이 존재하지 않습니다.");
            return View.MYINFO;
         }
        
      }
   }
   //주문내역 상세조회
         public int OrderDetail(){
            System.out.println("조회할 주문번호를 입력해주세요.");
            String userId = (String)Controller.loginUser.get("MEMBER_ID");
            String orderInput = ScanUtil.nextLine();
            
            List<Map<String, Object>> orderdetail = orderDao.OrderDetailList(userId, orderInput);
            
            if(orderdetail.size() < 1){
               System.out.println("존재하지 않는 주문번호입니다.");
               return View.ORDER_LIST;  //주문번호 조회로 돌아가기??
            }
            else{
               System.out.println("==============================================");
               System.out.println("주문번호\t\t강의코드\t강의명\t\t강의가격");
               for(Map<String, Object> detail : orderdetail){
                   System.out.println(detail.get("CART_NO")
                         + "\t" +detail.get("LECTURE_NO")    //설명?
                         + "\t" +detail.get("LECTURE_NAME")
                         + "\t\t" +detail.get("LECTURE_PRICE"));
               }
               System.out.println("==============================================");
               System.out.println("1.주문내역으로 돌아가기 2.내정보로 돌아가기");
               int detailInput = ScanUtil.nextInt();
               switch(detailInput){
               
               case 1: 
                   return View.ORDER_LIST;
               case 0:
                  return View.MYINFO;
                  
               }
               
            }return View.ORDER_LIST;
         }
        
}






