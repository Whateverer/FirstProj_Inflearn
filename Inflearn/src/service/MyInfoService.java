package service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import util.ScanUtil;
import util.View;
import controller.Controller;
import dao.MyInfoDao;
import dao.StudyDao;

public class MyInfoService {
   private MyInfoService() {}
   private static MyInfoService instance;

   public static MyInfoService getInstance() {
      if(instance == null) {
         instance = new MyInfoService();
      }
      return instance;
   }
   
   static int learnNo = 0;
   
   MyInfoDao myInfoDao = MyInfoDao.getInstance();
   StudyDao studyDao = StudyDao.getInstance();

   
   //내 정보 메인 홈
   public int myInfoHome() {
      String userId = (String)Controller.loginUser.get("MEMBER_ID");
      Map<String, Object> user = myInfoDao.getMyInfo(userId);
      //수강생일 때   
      if("수강생".equals(user.get("DOMIAN_NO"))) {
      System.out.println("[수강생 내 정보 HOME]");   
      System.out.println("1.내 정보 2.내 학습 3.작성한 게시글 조회 ");
      System.out.println("4.내 장바구니 5.주문내역/결제내역 0.홈으로");
      int input = ScanUtil.nextInt();
      switch(input) {
      case 1: return View.MYINFO_DETAIL; 
      case 2: return View.MYSTUDY;   
      case 3: return View.MYBOARD;   
      case 4: return View.CART;   
      case 5: return View.ORDER_PAY;   
      case 0: return View.HOME;
      }
      return View.HOME;
      }
      
      //지식공유자일 때
      if("지식공유자".equals(user.get("DOMIAN_NO"))) {
    	  System.out.println("[지식공유자 내 정보 HOME]");
         System.out.println("1.내 정보 2.내 학습 3.내 강의 4.작성한 게시글 조회");
         System.out.println("5.내 장바구니 6.주문내역/결제내역 0.홈으로");
         int input = ScanUtil.nextInt();
         switch(input) {
         case 1: return View.MYINFO_DETAIL; 
         case 2: return View.MYSTUDY;   
         case 3: return View.MYLECTURE;
         case 4: return View.MYBOARD;   
         case 5: return View.CART;   
         case 6: return View.ORDER_PAY;   
         case 0: return View.HOME;
         }
         return View.HOME;
         
      }
      
      return View.HOME;
   }

   
   //내 정보 조회
   public int myInfoList() {
      System.out.println("내 정보를 조회합니다.");
      //로그인된 아이디 변수에 저장
      String userId = (String)Controller.loginUser.get("MEMBER_ID");
      Map<String, Object> myInfo = myInfoDao.getMyInfo(userId);
      
      //정보 가져오기
      System.out.println("=============== 내 정보 ================");
      System.out.println("1. 이름 : " + myInfo.get("MEMBER_NAME"));
      System.out.println("2. 회원타입 : " + myInfo.get("DOMIAN_NO"));
      System.out.println("3. 핸드폰번호 : " + myInfo.get("MEMBER_HP"));
      System.out.println("4. 이메일 : " + myInfo.get("MEMBER_EMAIL"));
      System.out.println("5. 주소 : " + myInfo.get("MEMBER_ADDRESS") + " " + myInfo.get("MEMBER_ADDRESS_DETAIL"));
      System.out.println("6. 은행계좌 : " + myInfo.get("MEMBER_BANK") +" "+ myInfo.get("MEMBER_ACCOUNT"));
      System.out.println("======================================");
      //수강생일 때
      if("수강생".equals(myInfo.get("DOMIAN_NO"))){
         System.out.println("1.내 정보 수정 2.지식공유 참여 3.회원탈퇴 0.목록으로");
         int input = ScanUtil.nextInt();
         switch(input) {
         //정보수정
         case 1: return View.MYINFO_UPDATE;
         //지식공유자 참여
         case 2: return View.MYINFO_UPGRADE;
         //회원탈퇴   
         case 3: return View.MYINFO_QUIT;
         case 0: return View.MYINFO;
         }
         return View.MYINFO_DETAIL;
         
      }
      
      if("지식공유자".equals(myInfo.get("DOMIAN_NO"))){
         System.out.println("1.내 정보 수정 2.회원탈퇴 0.목록으로");
         int input = ScanUtil.nextInt();
         switch(input) {
         //정보수정
         case 1: return View.MYINFO_UPDATE;
         //회원탈퇴 - 뷰만들기   
         case 2: return View.MYINFO_QUIT;
         case 0: return View.MYINFO;
         }
         return View.MYINFO_DETAIL;
      }
      return View.MYINFO_DETAIL;
   }

   //내 정보 수정하기
   public int myInfoUpdate() {//시간 남으면 정규표현식 보충하기
      //아이디 저장
      String userId = (String)Controller.loginUser.get("MEMBER_ID");
      
      System.out.println("내 정보 수정을 위해 비밀번호를 입력하세요.");
      String password = ScanUtil.nextLine();
      //비밀번호가 일치할 때
      if(password.equals(Controller.loginUser.get("MEMBER_PASSWORD"))) {
         while(true) {
            
         System.out.println("어떤 정보를 수정할까요? 번호를 입력해주세요.");
         System.out.println("1.이름 2.핸드폰번호 3.이메일 4.주소 5.은행 계좌 0.목록으로");
         int input = ScanUtil.nextInt();
         switch(input) {
         //이름 수정
         case 1: System.out.println("수정할 이름을 입력해주세요.");
            String name = ScanUtil.nextLine();
            //수정메서드
            int upname = myInfoDao.updateName(userId ,name);
            if(0 < upname) {
               System.out.println("이름이 정상적으로 수정되었습니다.");
               return View.MYINFO_DETAIL;
            }else {
               System.out.println("이름 수정에 실패했습니다.");
               return View.MYINFO_DETAIL;
            }
         //핸드폰번호 수정
         case 2: System.out.println("수정할 핸드폰번호를 입력하세요.");
            String hp = ScanUtil.nextLine();
            //수정메서드
            int uphp = myInfoDao.updateHp(userId ,hp);
            if(0 < uphp) {
               System.out.println("핸드폰번호가 정상적으로 수정되었습니다.");
               return View.MYINFO_DETAIL;
            }else {
               System.out.println("핸드폰번호 수정에 실패했습니다.");
               return View.MYINFO_DETAIL;
            }
         //이메일 수정
         case 3: System.out.println("수정할 이메일을 입력하세요.");
            String email = ScanUtil.nextLine();
            //정규표현식 확인 => 정리
            int upemail = myInfoDao.updateEmail(userId ,email);
            if(0 < upemail) {
               System.out.println("이메일이 정상적으로 수정되었습니다.");
               return View.MYINFO_DETAIL;
            }else {
               System.out.println("이메일 수정에 실패했습니다.");
               return View.MYINFO_DETAIL;
            }
         
         //주소 수정   
         case 4: System.out.println("수정할 주소를 입력하세요.");
               System.out.print("기본주소 : ");
               String address1 = ScanUtil.nextLine();
               System.out.print("상세주소: ");
               String address2 = ScanUtil.nextLine();
               //수정메서드
               int upadd = myInfoDao.updateAddress(userId, address1, address2);
               if(0 < upadd) {
                  System.out.println("주소가 정상적으로 수정되었습니다.");
                  return View.MYINFO_DETAIL;
               }else {
                  System.out.println("주소 수정에 실패했습니다.");
                  return View.MYINFO_DETAIL;
               }
         //은행, 계좌 수정   
         case 5: 
            System.out.println("수정할 은행을 입력하세요.");
            String bank = ScanUtil.nextLine();
            System.out.println("수정할 계좌번호를 입력하세요.");
            String account = ScanUtil.nextLine();
            //수정메서드
            int upbank = myInfoDao.updateBank(userId, bank, account);
            if(0 < upbank) {
               System.out.println("은행 계좌가 정상적으로 수정되었습니다.");
               return View.MYINFO_DETAIL;
            }else {
               System.out.println("은행 계좌 수정에 실패했습니다.");
               return View.MYINFO_DETAIL;
            }
         case 0: return View.MYINFO_DETAIL;
      
         }
         }
      }else {//비밀번호 틀렸을 때 
         System.out.println("비밀번호가 틀렸습니다.");
         return View.MYINFO_UPDATE;
      }
   }

   //지식공유자 업그레이드
   public int myInfoUpgrade() {//시간 남으면 관리자가 승인하는 방식으로 바꾸기
      String userId = (String)Controller.loginUser.get("MEMBER_ID");
      
      System.out.println("지식공유 회원이 되면 강의를 올리고 수익을 정산받을 수 있습니다.");
      System.out.println("지식공유에 참여하시겠습니까?");
      System.out.println("1.예 2.아니오");
      int input = ScanUtil.nextInt();
      
       switch(input) {
       case 1: int result = myInfoDao.upgrade(userId);
          if(0 < result) {
             System.out.println("지식공유자가 되었습니다. 이제부터 강의를 업로드 할 수 있습니다.");
          }else {
             System.out.println("지식공유 회원 변경에 실패했습니다.");
          }
       case 2:   return View.MYINFO_DETAIL;
       }
      return View.MYINFO_DETAIL;
   }

   //회원탈퇴하기
   public int quit() {
      
      String userId = (String)Controller.loginUser.get("MEMBER_ID");

      System.out.println("정말 회원을 탈퇴하시겠습니까?");
      System.out.println("1.예 2.아니오");
      int input = ScanUtil.nextInt();
      
      switch(input) {
      case 1:
         System.out.println("회원을 탈퇴합니다.");
         int result = myInfoDao.memberQuit(userId);
         if(0 < result) {
            System.out.println("회원탈퇴가 정상적으로 완료되었습니다.");
            Controller.loginUser = null;
            System.out.println("로그아웃");
            return View.LOGOUTHOME; 
         }else {
            System.out.println("회원탈퇴에 실패하였습니다.");
            return View.MYINFO;
         }
         
      case 2: return View.MYINFO_DETAIL;   
         
      }
      return View.MYINFO_DETAIL;
   }

   //내 학습 조회
   public int myStudy() {
      
      String userId = (String)Controller.loginUser.get("MEMBER_ID");
      
      List<Map<String, Object>> studyList = myInfoDao.getStudyList(userId);
      
      System.out.println("=============== 수강 중인 강의 목록 ===============");
      System.out.println("강의번호\t강의분류명\t강의명\t강의가격\t진도율");
      for(Map<String, Object> study : studyList) {
    	  System.out.println("---------------------------------------------");
         System.out.println(study.get("LECTURE_NO")
               + "\t" + study.get("LECTURE_CODE_NAME")
               + "\t" + study.get("LECTURE_NAME")
               + "\t" + study.get("LECTURE_PRICE")
               + "\t" + study.get("LEARN_PROGRESS") + " / 100");
               
      }
      System.out.println("=============================================");
      
      System.out.println("1.조회 0.뒤로");
      int input = ScanUtil.nextInt();
      switch(input) {
      case 1: return View.MYSTUDY_DETAIL;
      case 0:   
         return View.MYINFO;
      }
      return View.MYSTUDY;
   }

   //내 학습 상세
   public int myStudyDetail() {
      System.out.println("조회할 강의 번호를 입력하세요.");
      int lectureNo = ScanUtil.nextInt();
      Map<String, Object> lectureInfo = myInfoDao.getLectureInfo(lectureNo);
      System.out.println("=====================================");
      System.out.println("강의번호 : " + lectureInfo.get("LECTURE_NO"));
      System.out.println("강의분류명 : " + lectureInfo.get("LECTURE_CODE_NAME"));
      System.out.println("강의명 : " + lectureInfo.get("LECTURE_NAME"));
      System.out.println("강의소개 : " + lectureInfo.get("LECTURE_INTRO"));
      System.out.println("강의시간 : " + lectureInfo.get("LECTURE_TIME"));
      System.out.println("커리큘럼 : " + lectureInfo.get("CURRICULUM"));
      System.out.println("진도율 : " + lectureInfo.get("LEARN_PROGRESS"));
      System.out.println("=====================================");
      
      System.out.println();
      System.out.println("1.학습하기 2.강의 목록으로");
      int input = ScanUtil.nextInt();
      
      switch(input) {
      case 1: //학습하기
         String userId = (String)Controller.loginUser.get("MEMBER_ID");
         
         
         System.out.println("강의를 시작합니다.");
         Map<String, Object> study = studyDao.getStudyContent(lectureNo);
         System.out.println("==================== 강의 내용 ====================");
         System.out.println(study.get("LECTURE_CONTENT")); 
         System.out.println("================================================");
         System.out.println();
         
         int result = studyDao.updateProgress(userId, lectureNo);
         Map<String, Object> progress = studyDao.getProgress(userId,lectureNo);
         if(0 < result){
        	 System.out.println("진도율 : " + progress.get("LEARN_PROGRESS"));
         }
         learnNo = ((BigDecimal)progress.get("LEARN_NO")).intValue();
        
        	 if(((BigDecimal)progress.get("LEARN_PROGRESS")).intValue() >= 100){
        		 System.out.println("강의를 완강했습니다. 수강후기를 작성해주세요.");
        		 String review = ScanUtil.nextLine();
        		 System.out.println("별점을 입력해주세요. 1~5까지");
        		 int star = ScanUtil.nextInt();
        		 
        		 if(star > 5){
        			 System.out.println("잘 못 입력하셨습니다. 다시 입력해주세요.");
        			 
        		 }else{ result = 0;
        				result = studyDao.insertReview(learnNo, review, star);
        				if(0 < result){
        					System.out.println("리뷰작성, 별점 입력 성공");
        				}
        			 break;
        		 }
        	 
        	 
         }
         System.out.println();
         System.out.println("0.내 수강 강의 목록으로");
         int input2 = ScanUtil.nextInt();
         switch(input2) {
         case 0:   return View.MYSTUDY;//내 수강 강의 목록
         }
         return View.MYSTUDY_DETAIL;
      case 2: return View.MYSTUDY;   
      }
      return View.MYSTUDY;
   }
   
   
   
}











