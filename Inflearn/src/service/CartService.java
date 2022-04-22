package service;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.ScanUtil;
import util.View;
import controller.Controller;
import dao.CartDao;
import dao.PayDao;
import dao.StudyDao;

public class CartService {

private CartService(){};
   
   public static CartService instance;
   
   public static CartService getInstance(){
      if (instance == null) {
         instance = new CartService();
      }
      return instance;
   }
   public PayDao paydao = PayDao.getInstance();
   public CartDao cartdao = CartDao.getInstance();
   private int code;
   public String lectureNo;
   public static List<Map<String, Object>> lectures;
   StudyDao studyDao = StudyDao.getInstance();

   
   
   //장바구니 목록
   public int CartList(){
      SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
   while(true){
      String userId = (String)Controller.loginUser.get("MEMBER_ID");
    
      List<Map<String, Object>> cartlist = cartdao.cartlist(userId);
      if(cartlist.size() > 0){
      System.out.println("=================🛒장바구니 내역🛒=================");
      System.out.println("강의코드\t강의명\t\t강의가격\t담은 날짜");
      
      for(Map<String, Object> cart : cartlist){
         String time1 = format.format(cart.get("WISH_DATE")); 
         System.out.println(cart.get("LECTURE_NO")
               + "\t" +cart.get("LECTURE_NAME")
               + "\t\t" +cart.get("LECTURE_PRICE")
               + "\t" + time1);
      }
      System.out.println("================================================");
      }else{
         System.out.println("장바구니가 비어있습니다😢");
         return View.HOME; 
      }
      System.out.println("1.주문하기 2.장바구니에서 삭제 0.뒤로가기");
       int input = ScanUtil.nextInt();
       
       switch(input){
          
           case 1 : //CART_BUY주문하기 뷰
               return View.CART_BUY; 
               
           case 2 :    
               return View.CART_DELETE; //return View.CART_VIEW 
              
           case 0 :
              return View.MYINFO; //리턴주소?
          }
         
        }
    }
   //바구니에서 삭제
   public int CartDelete(){
      String userId = (String)Controller.loginUser.get("MEMBER_ID");
      
      System.out.println("바구니에서 삭제 할 강의코드를 입력해주세요."); //강의코드 입력???
      lectureNo = ScanUtil.nextLine();
      
      int cartdelete = cartdao.cartdelete(userId, lectureNo);
      
      if(cartdelete > 0){
         System.out.println("정상 삭제되었습니다.");
         return View.CART;//리턴 뷰??
      }else{
         System.out.println("삭제에 실패하였습니다.");
         return View.CART;//리턴 뷰??
      }
   }
   
   //강의코드로 장바구니에서 선택조회
   public int CartBuy(){
      String userId = (String)Controller.loginUser.get("MEMBER_ID");
      
      lectures = new ArrayList<>();
      while(true){
       Map<String, Object> lecture = new HashMap<>();   
       System.out.println("주문하실 강의코드를 입력해주세요."); //강의코드 입력???
       String lectureNo = ScanUtil.nextLine();
       
       //강의 리스트에 있는 강의번호 중에 있으면 select lecture_no from lecture 해서 그 값이 null일 때 다시
       Map<String, Object> check = cartdao.checkLectureNo(lectureNo);
       if(check == null){
          System.out.println("강의코드를 잘못 입력하셨습니다.");
          continue;
       }
       
       lecture.put("LECTURE_NO", lectureNo);
       
       lectures.add(lecture);
       System.out.print("더 주문하시겠습니까?(Y/N)");
       String answer = ScanUtil.nextLine();
       if(answer.equals("N")||answer.equals("n")) {
          break;
       }
      }
      
      //ArrayList에 있는 거 꺼내오기
       if(lectures != null){
          
          //주문테이블에 주문번호, 회원 아이디, 주문일자 넣는 메서드
          int result = cartdao.insertCart(userId);
          if(0 < result){
             System.out.println("주문번호가 입력되었습니다.");
          }else{
             System.out.println("주문번호 입력에 실패했습니다😢");
             
          }
          
          //주문강의에 주문번호, 강의코드를 넣는 메서드
          for(Map<String, Object> lecture : lectures){
             cartdao.InsertMyLecture(userId, lecture); 
          }
          //주문테이블에 금액을 넣는 메서드 => 그렇게 하면 주문번호가 같은 다른 회원의 주문이 같이 결제됨(주문번호 부여방식을 바꿔야 함 - 로그인 할 때마다 바뀌는 걸로)
             result = 0;
             result = cartdao.updateCartPrice();
             if(0 < result){
                System.out.println("주문금액이 계산되었습니다💰");
             }else{
                System.out.println("주문금액 계산에 실패했습니다😢");
                System.out.println("장바구니로 돌아갑니다🛒");
                return View.CART;
             }
          return View.PAYTYPE_CHOICE;
          
             
          }
       return View.CART_BUY;
   }
   //장바구니에서 선택한 강의 결제        payservice로 가야함
    public int Paychoice(){
       String userId = (String)Controller.loginUser.get("MEMBER_ID");
       List<Map<String, Object>> cartbuy = cartdao.cartbuy(userId);
       
       System.out.println("=================💻구매하실 강의💻=================");
       System.out.println();
       System.out.println("주문번호 : " + cartbuy.get(0).get("CART_NO"));
       System.out.println("------------------------------------------------");
       System.out.println("강의코드\t강의명\t\t강의가격\t주문일자");
       System.out.println("------------------------------------------------");
       for(Map<String, Object> map : cartbuy) {
           System.out.println(map.get("LECTURE_NO")
                    + "\t" +map.get("LECTURE_NAME")
                    + "\t\t" +map.get("LECTURE_PRICE")
                    + "\t" +map.get("CART_DATE"));
           
           System.out.println("------------------------------------------------");
       }
       System.out.println("총 주문금액 : " + cartbuy.get(0).get("CART_PRICE"));
       System.out.println("================================================");
       while(true){
         System.out.println("1.결제진행 2.장바구니 돌아가기 0.홈화면");
         int input = ScanUtil.nextInt();
         
         if(input < 3){
         switch(input){
         
           case 1: 
              return View.PAY;
            
           case 2: 
              return View.CART;
           
           case 0: 
              return View.HOME;
         }
      }else{
         System.out.println("잘못 입력하셨습니다😢");
         continue;
          }
       }      
    }
   
    //결제 진행
    public int PayStart(){
       while(true){
          System.out.println("===================================");
          System.out.println("\t결제수단 선택");
          System.out.println("\t1.신용카드");
          System.out.println("\t2.계좌이체");
          System.out.println("\t3.휴대폰결제");
          System.out.println("\t0.결제취소");
          System.out.println("*결제 미완료시 14일 후 주문내역은 삭제됩니다.*");
          System.out.println("===================================");
         
       int paytype = ScanUtil.nextInt();
       switch(paytype){
          //신용카드
          case 1:
             code = 3;
             return View.PAY_CARD;
          //계좌이체   
          case 2:
             code = 4;
             return View.PAY_ACCOUNT;
          //휴대폰결제   
          case 3:
             code = 5;
             return View.PAY_PHONE;
          //결제취소   
          case 0:
             System.out.println("장바구니로 돌아갑니다");
             return View.CART;
       }
          
    }
}
    //카드결제 시작
    //카드선택
    public int CreditCard(){
         while(true){
         System.out.println("==================================");
         System.out.println("\t 카드사 선택");
         System.out.println("1.삼성카드 2.하나카드 3.신한카드 4.농협카드");
         System.out.println("5.롯데카드 6.우리카드 7.MG카드 0.결제취소");
         System.out.println("==================================");
         int cardInput = ScanUtil.nextInt();
         if(cardInput == 0){
             System.out.println("카드결제를 취소합니다");
             return View.PAY;
          }if(cardInput < 8){
            return View.CARD_NUMBER;
         }else{
            System.out.println("입력이 올바르지 않습니다.");
         }
      }
    }         
     //카드번호확인
    public int CardNumber(){
          while(true){
          System.out.println("진행중인 결제를 취소하시려면 '0'입력");   
         System.out.println("신용카드 번호 16자리를 입력해주세요.(****-****-****-****)");
         String cardnumber = ScanUtil.nextLine();
            if(cardnumber.length() == 16){ //정규표현식 수정해야함(숫자만)
            return View.CARD_CVC;
            }if(cardnumber.equals("0")){
               System.out.println("카드결제를 취소합니다");
               return View.PAY;
            }else{
               System.out.println("입력이 올바르지 않습니다.");
         }
      }
   }
    //카드결제 최종
    //CVC확인
    public int CardCVC(){
       while(true){
       System.out.println("진행중인 결제를 취소하시려면 '0'입력");
       System.out.println("카드 CVC번호 3자리를 입력해주세요.");
       String cardcvc = ScanUtil.nextLine();
          if(cardcvc.length() == 3){ //정규표현식 수정해야함(숫자만)
             return View.PAY_END;
          }if(cardcvc.equals("0")){
             System.out.println("카드결제를 취소합니다.");
             return View.PAY;
          }else{
             System.out.println("입력이 올바르지 않습니다.");
          }
       }
    }
    //계좌이체 시작
    public int AccountPay(){
       while(true){
         System.out.println("==================================");
        System.out.println("\t은행선택");
        System.out.println("1.하나은행 2.신한은행 3.국민은행 4.우리은행");
        System.out.println("5.새마을금고 6.농협 7.기업은행 0.결제취소");
        System.out.println("==================================");
        int accountInput = ScanUtil.nextInt();
        if(accountInput == 0){
            System.out.println("계좌이체 결제를 취소합니다");
            return View.PAY;
         }if(accountInput < 8){
           System.out.println("=============주문완료=============");
           System.out.println("\t 입금하실 계좌번호");
           System.out.println("\t065-00943-797757");
           System.out.println("=================================");
           return View.PAY_END;
            
         }else{
            System.out.println("입력이 올바르지 않습니다.");
         }
      }
 }
   //휴대폰결제 시작
       public int Phonepay(){
          while(true){
             System.out.println("결제를 취소하시려면 '0'을 입력해주세요.");
             System.out.println("==========================================================");
             System.out.println("휴대폰 번호입력 > *에 들어갈 0자리 숫자만 입력해주세요. ex)010-****-****");
             System.out.println("==========================================================");
             String phonenumber = ScanUtil.nextLine();
             System.out.println(phonenumber);
             if(phonenumber.length()==8){
                return View.MOBILE_COMPANY;
             }if(phonenumber.equals("0")){
                System.out.println("휴대폰 결제를 취소합니다.");
                return View.PAY;
             }else{
                System.out.println("입력이 올바르지 않습니다.");
          }
       }
   } 
    //통신사 선택
       public int MoblieCompany(){
          while(true){
             System.out.println("통신사를 선택해주세요.");
             System.out.println("1.SKT 2.KT 3.LGU+ 4.알뜰폰 0.결제취소");
             int input = ScanUtil.nextInt();
             if(input == 0){
                 System.out.println("휴대폰 결제를 취소합니다.");
                 return View.PAY;
              }if(input < 5){
                return View.CERTIFICATION;
             }else{
                System.out.println("입력이 올바르지 않습니다.");
             }
          }
       }
    
   //휴대폰 인증
       public int Certification(){
          while(true){
             System.out.println("결제를 취소하시려면 '0'을 입력해주세요.");
             System.out.println("\t인증번호를 입력해주세요.");
             int output = (int)(Math.random() * 99999) + 10000;
             System.out.println("인증번호 : "+ output);
             int input = ScanUtil.nextInt();
             if(input == 0){
                 System.out.println("휴대폰 결제를 취소합니다.");
                 return View.PAY;
              }if(input == output){
                System.out.println("=======🙂인증완료🙂=======");
                return View.PAY_END;
             }else{
                System.out.println("입력이 올바르지 않습니다.");
             }
          }
       }
    
    //최종결제 (만들어야함) 결제테이블에 입력해야하는데 아직 못만들었음
    public int PayEnd(){
       while(true){
          String userId = (String)Controller.loginUser.get("MEMBER_ID");
          List<Map<String, Object>> payend = cartdao.cartbuy(userId);


          //결제
          if(code == 4) {
             System.out.println(payend.get(0).get("CART_PRICE") + " 원을 이체하시겠습니까?");
             System.out.println("1.이체 0.취소");
             
          }else {
             System.out.println(payend.get(0).get("CART_PRICE") + " 원을 결제하시겠습니까?");
             System.out.println("1.결제 0.취소");
             
          }
          //input = 1 결제, 0 취소
          int input = ScanUtil.nextInt();
          //결제하기
          if(input==1){
            //결제테이블에 자료 올리기
             int result = cartdao.InsertPay(code); ///리턴타입 무엇으로
             int study = 0;
             if(0 < result){
                System.out.println("결제가 완료되었습니다.");
             
                //장바구니 없애기  & 수강테이블에 자료 올리기
                for(Map<String, Object> lecture : lectures) {
                   //장바구니 없애기
                   cartdao.cartdelete(userId, (String)lecture.get("LECTURE_NO"));
                   //수강 테이블에 자료 올리기 - 메서드 만들기
                   study = studyDao.insertLecture((String)lecture.get("LECTURE_NO"));
                }
                if(0 < study){
                   System.out.println("이제부터 강의를 수강할 수 있습니다.");
                }
                
                 return View.CART;
                 
               }else{
                      System.out.println("결제에 실패했습니다."); 
                  return View.CART;
               }
          }if(input==0){
             System.out.println("결제를 취소하셨습니다.");
             return View.PAY;
          }else{
             System.out.println("입력이 올바르지 않습니다.");
          }
       }
       
    }
    
            
       


   }