package service;

import java.text.SimpleDateFormat;
import java.util.List;

import dao.CartDao;

import java.util.Map;

import controller.Controller;
import util.ScanUtil;
import util.View;
import dao.OrderDao;
import dao.PayDao;

public class PayService {

private PayService(){};
   
   private static PayService instance;
   
   public static PayService getInstance(){
      if (instance == null) {
         instance = new PayService();
      }
      return instance;
   }
   public CartDao cartdao = CartDao.getInstance();
   public PayDao paydao = PayDao.getInstance();
  
//   //결제내역 출력
//   public int PayList(){
//      SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
//       while(true){
//        String userId = (String)Controller.loginUser.get("MEMBER_ID");
//      
//         List<Map<String, Object>> paylist = paydao.PayDetailList(userId);
//         if(paylist.size() > 0){
//         System.out.println("===================💰나의 결제내역💰===================");
//         System.out.println("주문번호\t\t결제일자\t\t결제금액");
//         
//         for(Map<String, Object> pay : paylist){
//            String time1 = format.format(pay.get("PAY_DATE")); 
//            System.out.println(pay.get("CART_NO")
//                 + "\t" + time1
//                  + "\t" +pay.get("PAY_PRICE"));
//         }
//         System.out.println("===============================================");
//         System.out.println("1.주문번호 상세조회 0.뒤로가기");
//         int input = ScanUtil.nextInt();switch(input){
//         
//         case 1: PayDetail();  //결제내역 상세 조회 view생성
//         
//         case 0:
//            return View.MYINFO;
//         }  
//         }else{
//            System.out.println("결제내역이 존재하지 않습니다.");
//            return View.MEMBERINFO; //리턴위치??
//         }
//            
//         
//       }
//   }
   //결제내역 상세조회
   public int PayDetail(){

      String userId = (String)Controller.loginUser.get("MEMBER_ID");
       
        List<Map<String, Object>> paydetail = paydao.PayDetailList(userId);
        
        if(paydetail == null){
            System.out.println("존재하지 않는 주문번호입니다.");
            return View.ORDER_LIST;  //주문번호 조회로 돌아가기??
   }else{
        System.out.println("======================💰결제내역💰======================");
        System.out.println("주문번호\t\t결제수단\t결제일자\t\t결제가격\t결제상태");
        System.out.println("------------------------------------------------------");
         String status = "";
        for(Map<String, Object> detail : paydetail){
           if(detail.get("PAY_STATUS").equals("Y")){
              status = "결제완료";
           }else{
              status ="결제대기";
           }
            System.out.println(detail.get("CART_NO")
                 + "\t" +detail.get("DOMAIN_NAME")    //설명?   
                  + "\t" +detail.get("PDATE")    //설명?
                  + "\t" +detail.get("PAY_PRICE")
                  + "\t" +status);
        }
        System.out.println("======================================================");
        System.out.println("1.주문내역으로 돌아가기 0.내 정보로 돌아가기");
        int detailInput = ScanUtil.nextInt();
        switch(detailInput){
        
        case 1: 
            return View.ORDER_LIST;
        case 0:
           return View.MYINFO;
           
        }
        
     }return View.MYINFO;
    
   }
}