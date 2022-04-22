package service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import util.ScanUtil;
import util.View;
import controller.Controller;
import dao.LectureDao;
import dao.WishListDao;

public class LectureService {

   //싱글톤패턴
   private LectureService() {}
   private static LectureService instance;

   public static LectureService getInstance() {
      if(instance == null) {
         instance = new LectureService();
      }
      return instance;
   }
   public static int lectureNo;
   
   LectureDao lectureDao = LectureDao.getInstance();
   WishListDao wishListDao = WishListDao.getInstance();

   //강의목록 조회
   public int lectureList() {
      List<Map<String, Object>> lectureList = lectureDao.selectLectureList();
      System.out.println("=================== 강의목록 =====================");
      System.out.println("강의번호\t강의명      \t강사명   \t가격\t별점평균");
      for(Map<String, Object> lecture : lectureList) {
         System.out.println("----------------------------------------------");
         System.out.println(lecture.get("LECTURE_NO")
               + "\t" + lecture.get("LECTURE_NAME")
               + "\t  " + lecture.get("MEMBER_NAME")
               + "\t " + lecture.get("LECTURE_PRICE")  +"원"
               + "\t" + lecture.get("SAVG"));
      }
      System.out.println("================================================");
      System.out.println("1.조회 2.홈으로");
      int input = ScanUtil.nextInt();
      switch(input) {
      case 1: return View.LECTURE_DETAIL;
      case 2: 
         //비회원시
         if(Controller.loginUser == null && Controller.loginAdmin == null) return View.LOGOUTHOME;
         //회원시
         if(Controller.loginUser != null && Controller.loginAdmin == null) return View.HOME;
         //관리자일 때      
         if(Controller.loginUser == null && Controller.loginAdmin != null) return View.ADMINHOME;      
      }
      return View.LECTURE_LIST;
   }

   //강의상세 조회(강의 상세페이지)
   public int lectureOne() {
     
    	  
      System.out.println("조회할 강의번호를 입력하세요.");
      lectureNo = ScanUtil.nextInt();
      Map<String, Object> lecture = lectureDao.selectLectureOne(lectureNo);
      Map<String, Object> lectureStar = lectureDao.selectLectureStar(lectureNo);
      List<Map<String,Object>> lectureReview = lectureDao.selectLectureReview(lectureNo);
      
      if(lecture == null){
    	  System.out.println("존재하지 않는 강의입니다.");
    	  return View.LECTURE_DETAIL;
      }
     
      
      while(true) {
      System.out.println("============== 강의조회 ===============");
      System.out.println("강의명 : " + lecture.get("LECTURE_NAME"));
      System.out.println("강의분류 : " + lecture.get("LECTURE_CODE_NAME"));
      System.out.println("강의가격 : " + lecture.get("LECTURE_PRICE") + "원");
      System.out.println("강의시간 : " + lecture.get("LECTURE_TIME") + "시간");
      System.out.println("평균별점 : " + lectureStar.get("SAVG"));
      System.out.println("강의소개 : " + lecture.get("LECTURE_INTRO"));
      System.out.println("커리큘럼 : " + lecture.get("CURRICULUM"));
      System.out.println("수강후기 : " );
      for(Map<String, Object> review : lectureReview) {
         System.out.println(review.get("AFTER_REVIEW"));
      }
      System.out.println("====================================");
      
      //관리자일 때
      if(Controller.loginAdmin != null) {
         System.out.println("1.수정 2.삭제 0.목록으로 돌아가기");
         int input = ScanUtil.nextInt();
         switch(input) {
         case 0: return View.LECTURE_ALLOW;
         }
         return View.LECTURE_DETAIL;
      }
      
      //일반회원들
      System.out.println("1.장바구니에 담기 2.목록으로");
      int input = ScanUtil.nextInt();
      switch(input) {
      case 1:
         //비회원
         if(Controller.loginUser == null && Controller.loginAdmin == null) {
            System.out.println("로그인한 회원만 장바구니에 담을 수 있습니다.");
            System.out.println("로그인 페이지로 이동합니다.");
            return View.LOGIN;
         }
         
         //회원 로그인상태
         if(Controller.loginUser != null && Controller.loginAdmin == null) {
            
            //수강중인 강의일 때 => 강의정보 페이지로
            if(lectureDao.checkLearn(lectureNo, (String)Controller.loginUser.get("MEMBER_ID")) != null) {
               System.out.println("이미 수강중인 강의입니다.");
               continue;
            }
           
            String userId = (String)Controller.loginUser.get("MEMBER_ID");
            
            //이미 장바구니에 있을 때 => 강의 세부정보 페이지로
//            Map<String,Object> map = wishListDao.checkWishList1(lectureNo);
//            int c = ((BigDecimal)map.get("CNT")).intValue();
//            
//            if(c!=0){
//            	System.out.println("이미 장바구니에 담긴 강의입니다.");
//            	continue; 
//            }
            
//            System.out.println("ppp"+wishListDao.checkWishList(lectureNo));
            if(wishListDao.checkWishList(lectureNo) != null){
                if(userId.equals(wishListDao.checkWishList(lectureNo).get("MEMBER_ID"))) {
                    System.out.println("이미 장바구니에 담긴 강의입니다.");
                    continue;
                 }            	
            }
            
            //정상적일 때 - 장바구니에 담기
            int result = lectureDao.insertWishList(userId, lectureNo);
            if(0 < result) {
               System.out.println("강의가 장바구니에 정상적으로 담겼습니다.");
               System.out.println("1.장바구니 가기 2.강의 목록으로");
               int input2 = ScanUtil.nextInt();
               switch(input2){
               case 1: return View.CART;
               case 2:   return View.LECTURE_LIST;
               
            }}else {
               System.out.println("장바구니 담기에 실패했습니다.");
               continue; 
            }
            
         }
      case 2:   return View.LECTURE_LIST;
      }
      return View.LECTURE_DETAIL;
      }
   }

   
   //지식공유자의 내가 올린 강의 조회 - 정산현황 없애기
   public int myLecture() {
      System.out.println("1.내가 올린 강의 목록 0.뒤로가기");
      int input = ScanUtil.nextInt();
      switch(input) {
      case 1: return View.MYLECTURE_LIST;
      case 0: return View.MYINFO;
      }
      
      return View.MYLECTURE;
   }

   //지식공유자의 내가 올린 강의 리스트
   public int myLectureList() {
      String userId = (String)Controller.loginUser.get("MEMBER_ID");
      List<Map<String, Object>> lectures = lectureDao.getMyLecture(userId);
      System.out.println("================= 내가 올린 강의 =================");
      System.out.println("강의번호\t강의명\t\t누적학생수\t누적정산금액");
      for(Map<String, Object> lecture : lectures) {
         System.out.println(lecture.get("LECTURE_NO")
               +"\t" + lecture.get("LECTURE_NAME")
               +"\t\t" + lecture.get("LCNT") + "명"
               +"\t" + lecture.get("PSUM") + "원");
      }
      System.out.println("==============================================");
      
      System.out.println();
      System.out.println("1.강의 상세 조회 2.강의 업로드 0.뒤로가기");
      int input = ScanUtil.nextInt();
      switch(input) {
      case 1: return View.MYLECTURE_DETAIL;
      case 2: return View.MYLECTURE_UPLOAD;
      case 0: return View.MYINFO;
      }
      return View.MYLECTURE_LIST;
   }

   //지식공유자 자신의 강의 & 관리자 강의 상세조회
   public int myLectureDetail() {
      System.out.println("조회할 강의 번호를 입력하세요.");
      lectureNo = ScanUtil.nextInt();
      //강의 조회
      Map<String, Object> lecture = lectureDao.myLectureDetail(lectureNo);
      Map<String,Object> selectLectureStar = lectureDao.selectLectureStar(lectureNo);
      
      System.out.println("================= 강의 조회 ====================");
      System.out.println("강의 번호 : " + lecture.get("LECTURE_NO"));
      System.out.println("강의분류명 : " + lecture.get("LECTURE_CODE_NAME"));
      System.out.println("강의명 : " + lecture.get("LECTURE_NAME"));
      System.out.println("강의 소개 : " + lecture.get("LECTURE_INTRO"));
      System.out.println("커리큘럼 : " + lecture.get("CURRICULUM"));
      System.out.println("강의 시간 : " + lecture.get("LECTURE_TIME"));
      System.out.println("강의 가격 : " + lecture.get("LECTURE_PRICE") + "원");
      System.out.println("강의 평점 : " + selectLectureStar.get("SAVG"));
      System.out.println("==============================================");
      System.out.println();
      
      //회원일 때
      if(Controller.loginAdmin == null && Controller.loginUser != null) {
         
      System.out.println("1.수정 요청하기 0.목록으로");
      int input = ScanUtil.nextInt();
      switch(input) {
      case 1: 
         System.out.println("수정 요청할까요?");
         System.out.println("1.예 2.아니오");
         int input1 = ScanUtil.nextInt();
         switch(input1) {
         case 1: int result = lectureDao.askUpdate(lectureNo);
            if(0 < result) {
               System.out.println("수정 요청이 정상적으로 완료되었습니다.");
            }else {
               System.out.println("수정 요청에 실패했습니다.");
            }
            return View.MYLECTURE_LIST;
            
         case 2: break;
         }
      return View.MYLECTURE_LIST;
      case 0: return View.MYLECTURE_LIST;
      }
      return View.MYLECTURE_LIST;
   }
      //관리자 일 때
      if(Controller.loginUser == null && Controller.loginAdmin != null) {
         System.out.println("1.수정 2.삭제 0.목록으로");
         int input1 = ScanUtil.nextInt();
         switch(input1) {
         case 1: return View.LECTURE_REVISE;
         case 2: return View.LECTURE_DELETE;
         case 0:   return View.LECTURE_ALLOW;
         }
      }
      return View.MYLECTURE_DETAIL;
   }

   //강의 업로드하기 - 시간있으면 정규식 추가
   public int lectureUpload() {
      
      //강의명
      System.out.println("강의명을 입력해주세요.(30글자 이내)");
      String lectureName = ScanUtil.nextLine();

      System.out.println("강의분류를 선택해주세요.");
      System.out.println("1.웹 개발 2.데이터베이스 3.모바일 앱 개발 4.알고리즘");
      int lectureType = ScanUtil.nextInt();
      System.out.println("강의 가격을 설정해주세요.");
      int lecturePrice = ScanUtil.nextInt();
      //아이디
      String userId = (String)Controller.loginUser.get("MEMBER_ID");
      
      System.out.println("강의 커리큘럼을 작성해주세요.(1000자 이내)");
      String curriclum = ScanUtil.nextLine();
      
      System.out.println("강의 시간을 입력해주세요.");
      int lectureTime = ScanUtil.nextInt();
      
      System.out.println("강의 소개를 입력해주세요.(300자 이내)");
      String lectureIntro = ScanUtil.nextLine();
      
      System.out.println("학습 강의를 올려주세요.");
      String lectureContent = ScanUtil.nextLine();
      
      int result = lectureDao.insertLecture(lectureName,lectureType,lecturePrice,lectureTime,userId,curriclum,lectureIntro,lectureContent);
      
      if(0 < result) {
         System.out.println("강의 업로드 승인 신청이 완료되었습니다.");
         System.out.println("관리자 승인을 받으면 강의가 업로드됩니다.");
         return View.MYLECTURE_LIST;
      }else {
         System.out.println("강의 업로드에 실패했습니다.");
         return View.MYLECTURE_LIST;
      }

   }
   
   //지식공유자 강의 정산현황 - 확인
   public int myCommission() {
      String userId = (String)Controller.loginUser.get("MEMBER_ID");
      List<Map<String, Object>> lectures = lectureDao.getMyLecture(userId);
      System.out.println("================= 내가 올린 강의 =================");
      System.out.println("강의번호\t강의명\t누적학생수\t누적정산금액");
      for(Map<String, Object> lecture : lectures) {
         System.out.println(lecture.get("LECTURE_NO")
               +"\t" + lecture.get("LECTURE_NAME")
               +"\t" + lecture.get("LCNT") + "명"
               +"\t" + lecture.get("PSUM") + "원");
      }
      System.out.println("==============================================");
      
      System.out.println();
      System.out.println("1.강의 상세 조회 2.강의 업로드 0.뒤로가기");
      int input = ScanUtil.nextInt();
      switch(input) {
      case 1: return View.MYLECTURE_DETAIL;
      case 2: return View.MYLECTURE_UPLOAD;
      case 3: return View.ADMINHOME;
      }
      return 0;
   }

   //관리자 강의목록
   public int adminLecture() {
      System.out.println("1.승인된 강의 2.승인 대기 강의 0.뒤로가기");
      int input = ScanUtil.nextInt();
      switch(input) {
      case 1: return View.LECTURE_ALLOW;
      case 2: return View.LECTURE_WAIT;
      case 0: return View.ADMINHOME;
      }
      return View.ADMIN_LECTURE;
   }

   //관리자 승인 강의목록
   public int adminAllow() {
      List<Map<String, Object>> lectureList = lectureDao.selectLectureList();
      System.out.println("================ 승인된 강의 목록 ==================");
      System.out.println("강의번호\t강의명\t올린사람\t가격\t별점평균");
      for(Map<String, Object> lecture : lectureList) {
         System.out.println("----------------------------------------------");
         System.out.println(lecture.get("LECTURE_NO")
               + "\t" + lecture.get("LECTURE_NAME")
               + "\t" + lecture.get("MEMBER_NAME")
               + "\t" + lecture.get("LECTURE_PRICE") + "원"
               + "\t" + lecture.get("SAVG"));
      }
      System.out.println("===============================================");
      System.out.println("1.강의 상세 조회 0.뒤로가기");
      int input = ScanUtil.nextInt();
      switch(input) {
      case 1: return View.MYLECTURE_DETAIL;
      case 0: return View.ADMIN_LECTURE;
         
      }
      return View.LECTURE_ALLOW;
   }

   //강의 수정 홈 
   public int lectureRevise() {
      System.out.println("1.강의명 수정 2.강의분류 수정 3.강의가격 수정 0.뒤로");
      int input = ScanUtil.nextInt();
      switch(input) {
      case 1: return View.LECTURE_NAMEUPDATE;
      case 2: return View.LECTURE_TYPEUPDATE;
      case 3:   return View.LECTURE_PRICEUPDATE;
      case 0: return View.MYLECTURE_DETAIL;   
      }
      
      return View.MYLECTURE_LIST;
   }
   
   //강의명 수정
   public int nameUpdate() {
      System.out.println("수정할 강의명을 입력해주세요.");
       String name = ScanUtil.nextLine();
      int result = lectureDao.nameUpdate(lectureNo, name);
      if(0 < result) {
         System.out.println("강의명이 정상적으로 수정되었습니다.");
      }else {
         System.out.println("강의명 수정에 실패하였습니다.");
      }
      return View.LECTURE_REVISE;
   }
   
   //강의분류 수정
   public int typeUpdate() {
      System.out.println("수정할 강의타입을 입력해주세요.");
      String type = ScanUtil.nextLine();
      int result = lectureDao.typeUpdate(lectureNo, type);
      if(0 < result) {
         System.out.println("정상적으로 수정되었습니다.");
      }else {
         System.out.println("강의 수정에 실패하였습니다.");
      }
      return View.LECTURE_REVISE;
   }
   
   //강의가격 수정
   public int priceUpdate() {
      System.out.println("수정할 강의가격을 입력해주세요.");
      int price = ScanUtil.nextInt();
      int result = lectureDao.priceUpdate(lectureNo, price);
      if(0 < result) {
         System.out.println("정상적으로 수정되었습니다.");
      }else {
         System.out.println("강의 수정에 실패하였습니다.");
      }
      return View.LECTURE_REVISE;
   }

   //강의 삭제
   public int lectureDelete() {
      System.out.println("정말 강의를 삭제하시겠습니까?");
      System.out.println("1.예 2.아니오");
      int input = ScanUtil.nextInt();
      switch(input) {
      case 1: 
         int result = lectureDao.deleteLecture(lectureNo);
         if(0 < result) {
            System.out.println("강의가 정상적으로 삭제되었습니다.");
            return View.MYLECTURE_DETAIL;
         }else {
            System.out.println("강의 삭제에 실패했습니다.");
            return View.MYLECTURE_DETAIL;
         }
      case 2: return View.MYLECTURE_DETAIL;
      }
      
      return View.MYLECTURE_DETAIL;
   }

   //관리자 승인 대기 강의 목록
   public int lectureWait() {
      System.out.println("1.업로드승인 대기 강의 2.수정승인 대기 강의 0.뒤로가기");
      int input = ScanUtil.nextInt();
      switch(input) {
      //업로드 대기
      case 1: return View.LECTURE_UPLOAD_WAIT;
      //수정 대기
      case 2: return View.LECTURE_REVISE_WAIT;
      case 0: return View.ADMIN_LECTURE;
      }
      return View.LECTURE_WAIT;
   }

   //업로드 대기 강의 목록
   public int uploadWaitList() {
      List<Map<String, Object>> lectures = lectureDao.getUploadWaitList();
      System.out.println("==================== 업로드 승인 대기 강의 =====================");
      System.out.println("강의분류\t\t강의번호\t강의명\t\t강의가격\t강의시간");
      for(Map<String, Object> lecture : lectures){
         System.out.println(lecture.get("LECTURE_CODE_NAME") 
               + "\t\t" + lecture.get("LECTURE_NO")
               + "\t" + lecture.get("LECTURE_NAME")
               + "\t\t" + lecture.get("LECTURE_PRICE") + "원"
               + "\t" + lecture.get("LECTURE_TIME") + "시간");
      }
      System.out.println("==========================================================");
      System.out.println("1.조회 0.뒤로가기");
      int input = ScanUtil.nextInt();
      switch(input){
      case 1: return View.LECTURE_UPLOAD_WAIT_DETAIL;
      case 0:   return View.LECTURE_WAIT;
      }
      return View.LECTURE_UPLOAD_WAIT;
   }

   //업로드 대기 강의 상세
   public int uploadWaitDetail() {
      System.out.println("조회할 강의 번호를 입력하세요.");
      lectureNo = ScanUtil.nextInt();
      //강의 조회
      Map<String, Object> lecture = lectureDao.uploadDetail(lectureNo);
      
      System.out.println("==============================================");
      System.out.println("강의 번호 : " + lecture.get("LECTURE_NO"));
      System.out.println("강의분류명 : " + lecture.get("LECTURE_CODE_NAME"));
      System.out.println("강의명 : " + lecture.get("LECTURE_NAME"));
      System.out.println("강의 소개 : " + lecture.get("LECTURE_INTRO"));
      System.out.println("커리큘럼 : " + lecture.get("CURRICULUM"));
      System.out.println("강의 시간 : " + lecture.get("LECTURE_TIME") + "시간");
      System.out.println("강의 가격 : " + lecture.get("LECTURE_PRICE") + "원");
      System.out.println("==============================================");
      System.out.println();
      System.out.println("1.승인하기 0.뒤로가기");
      
      int input = ScanUtil.nextInt();
      switch(input){
      case 1: 
         int result = lectureDao.uploadApprove(lectureNo);
            if(0 <result){
               System.out.println("업로드 승인되었습니다.");
               return View.LECTURE_UPLOAD_WAIT;
            }else{
               System.out.println("업로드 승인에 실패했습니다.");
               return View.LECTURE_UPLOAD_WAIT;
            }
         
      case 0:   return View.LECTURE_UPLOAD_WAIT;
      }
      return View.LECTURE_UPLOAD_WAIT_DETAIL;
   }
   

   //수정 대기 강의 목록
   public int reviseWaitList() {
      List<Map<String, Object>> lectures = lectureDao.getReviseWaitList();
      System.out.println("============== 수정 승인 대기 강의 ===============");
      System.out.println("강의분류\t강의번호\t강의명\t강의가격\t강의시간");
      for(Map<String, Object> lecture : lectures){
         System.out.println(lecture.get("LECTURE_CODE_NAME") 
               + "\t" + lecture.get("LECTURE_NO")
               + "\t" + lecture.get("LECTURE_NAME")
               + "\t" + lecture.get("LECTURE_PRICE")
               + "\t" + lecture.get("LECTURE_TIME"));
      }
      System.out.println("============================================");
      System.out.println("1.조회 0.뒤로가기");
      
      int input = ScanUtil.nextInt();
      switch(input){
      case 1: return View.LECTURE_REVISE_WAIT_DETAIL;
      case 0: return View.LECTURE_WAIT;
      }
      return View.LECTURE_REVISE_WAIT;
   }

   //수정 대기 강의 상세
   public int reviseWaitDetail() {
      System.out.println("조회할 강의 번호를 입력하세요.");
      lectureNo = ScanUtil.nextInt();
      //강의 조회
      Map<String, Object> lecture = lectureDao.uploadDetail(lectureNo);
      
      System.out.println("==============================================");
      System.out.println("강의 번호 : " + lecture.get("LECTURE_NO"));
      System.out.println("강의분류명 : " + lecture.get("LECTURE_CODE_NAME"));
      System.out.println("강의명 : " + lecture.get("LECTURE_NAME"));
      System.out.println("강의 소개 : " + lecture.get("LECTURE_INTRO"));
      System.out.println("커리큘럼 : " + lecture.get("CURRICULUM"));
      System.out.println("강의 시간 : " + lecture.get("LECTURE_TIME") + "시간");
      System.out.println("강의 가격 : " + lecture.get("LECTURE_PRICE") + "원");
      System.out.println("==============================================");
      System.out.println();
      
      System.out.println("1.수정 승인하기 0.뒤로가기");
      int input = ScanUtil.nextInt();
      switch(input){
      case 1: 
         int result = lectureDao.reviseApprove(lectureNo);
         if(0 <result){
            System.out.println("수정 승인되었습니다.");
            return View.LECTURE_REVISE_WAIT;
         }else{
            System.out.println("수정 승인에 실패했습니다.");
            return View.LECTURE_REVISE_WAIT;
         }
      case 0:   return View.LECTURE_REVISE_WAIT;
      }
      
      return View.LECTURE_REVISE_WAIT_DETAIL;
      
      
   }


   

   

}