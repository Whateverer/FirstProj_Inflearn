package controller;

import java.util.Map;

import dao.UserDao;
import service.AdminService;
import service.CartService;
import service.CommunityService;
import service.LectureService;
import service.MyBoard;
import service.MyInfoService;
import service.NonuserService;
import service.OrderService;
import service.PayService;
import service.StudyService;
import service.UserService;
import util.ScanUtil;
import util.View;

public class Controller {
   
   public static void main(String[] args) {
   
      new Controller().start();
   }
   
   public static Map<String, Object> loginUser = null;
   public static Map<String, Object> loginAdmin = null;
   
   NonuserService nonuserService = NonuserService.getInstance();
   UserService userService = UserService.getInstance();
   LectureService lectureService = LectureService.getInstance();
   MyInfoService myInfoService = MyInfoService.getInstance();
   StudyService studyService = StudyService.getInstance();
   AdminService adminService = AdminService.getInstance();
   CartService cartService = CartService.getInstance();
   MyBoard myBoard = MyBoard.getInstance();
   CommunityService communityService = CommunityService.getInstance();
   OrderService orderService = OrderService.getInstance();
   PayService payService = PayService.getInstance();
   
   static UserDao userDao = UserDao.getInstance();
   
   //주문번호
   public static String cartNo = null;
         
   private void start() {
      int view = View.HOME;
      
      while(true) {
         switch(view) {
         //로그인 된 HOME
         case View.HOME: view = home(); break;
         //로그인
         case View.LOGIN: view = userService.login(); break;
         //회원가입
         case View.JOIN: view = userService.join(); break;
         //비회원 HOME
         case View.LOGOUTHOME: view = mainHome(); break;
         //강의목록조회
         case View.LECTURE_LIST: view = lectureService.lectureList(); break;
         //강의상세조회
         case View.LECTURE_DETAIL: view = lectureService.lectureOne(); break;
         //강의 장바구니에 담기 - 메서드로 해결
//         case View.LECTURE_INSERT: break;
         
         //관리자 홈
         case View.ADMINHOME: view = adminHome(); break;
         
         //관리자 강의목록 조회
         case View.ADMIN_LECTURE: view = lectureService.adminLecture(); break;
         //관리자 승인된 강의목록
         case View.LECTURE_ALLOW: view = lectureService.adminAllow(); break;
         //관리자 강의 수정 홈
         case View.LECTURE_REVISE: view = lectureService.lectureRevise(); break;
         //관리자가 강의명 수정
         case View.LECTURE_NAMEUPDATE: view = lectureService.nameUpdate(); break;
         //관리자가 강의 분류 수정      
         case View.LECTURE_TYPEUPDATE: view = lectureService.typeUpdate(); break;
         //관리자가 강의 가격 수정
         case View.LECTURE_PRICEUPDATE: view = lectureService.priceUpdate(); break;
               
         
         //관리자 강의 삭제
         case View.LECTURE_DELETE: view = lectureService.lectureDelete(); break;
         //관리자 승인 대기 강의목록
         case View.LECTURE_WAIT: view = lectureService.lectureWait(); break;
         //관리자 업로드 승인 대기 강의 목록
         case View.LECTURE_UPLOAD_WAIT: view = lectureService.uploadWaitList(); break;
         //관리자 업로드 승인 대기 강의 상세
         case View.LECTURE_UPLOAD_WAIT_DETAIL: view = lectureService.uploadWaitDetail(); break;
         //관리자 수정 승인 대기 강의 목록
         case View.LECTURE_REVISE_WAIT: view = lectureService.reviseWaitList(); break;
         //관리자 수정 승인 대기 강의 상세
         case View.LECTURE_REVISE_WAIT_DETAIL:view = lectureService.reviseWaitDetail(); break;
         //
         
         //커뮤니티조회
         case View.COMMUNITY_HOME: view = communityService.CommunityList(); break;
         //커뮤니티상세
         case View.COMMUNITY_DETAIL: view = communityService.ReadCommu(); break;
         //커뮤니티 글 작성
         case View.COMMUNITY_INSERT: view = communityService.InsertCommu();break;
         //커뮤니티 글 수정
         case View.COMMUNITY_UPDATE: view = communityService.UpdateCommu(); break;
         //커뮤니티 글 삭제
         case View.COMMUNITY_DELETE: view = communityService.DeleteCommu(); break;
         //커뮤니티 글 검색
         case View.COMMUNITY_SEARCH: view = communityService.SearchCommu(); break;
         //커뮤니티 댓글 작성
         case View.COMMUNITY_COMMENT: view = communityService.InsertComment(); break;
         //커뮤니티 댓글 삭제
         case View.COMMUNITY_DEL_COMMENT: view = communityService.DeleteComment(); break;
         //커뮤니티 내 댓글 삭제
         case View.COMMUNITY_MYCOMMENT_DEL: view = communityService.MyCommentDel(); break;
         //커뮤니티 댓글 선택
         case View.COMMUNUTY_CHOICE_COMMENT: view = communityService.CommentChoice(); break;
         
         //내 정보 HOME
         case View.MYINFO: view = myInfoService.myInfoHome(); break;
         //내 정보 상세
         case View.MYINFO_DETAIL: view = myInfoService.myInfoList(); break;
         //내 정보 수정
         case View.MYINFO_UPDATE: view = myInfoService.myInfoUpdate(); break;
         //수강생일 때 지식공유자로 업그레이드
         case View.MYINFO_UPGRADE: view = myInfoService.myInfoUpgrade(); break;
         //회원 탈퇴 
         case View.MYINFO_QUIT: view = myInfoService.quit(); break;
         //내 학습
         case View.MYSTUDY: view = myInfoService.myStudy(); break;
         //내 학습 조회
         case View.MYSTUDY_DETAIL: view = myInfoService.myStudyDetail(); break;
         //내가 작성한 게시글 - 수정삭제가능
         case View.MYBOARD: view = myBoard.MyCommunityList(); break;
         
         //지식공유자일 때 내 강의 홈
         case View.MYLECTURE: view = lectureService.myLecture(); break;
         //지식공유자 내가 올린 강의 목록
         case View.MYLECTURE_LIST: view = lectureService.myLectureList(); break;
         //지식공유자 내 강의 상세조회
         case View.MYLECTURE_DETAIL: view = lectureService.myLectureDetail(); break;
         //지식공유자 강의업로드
         case View.MYLECTURE_UPLOAD: view = lectureService.lectureUpload(); break;
         //지식공유자 정산
         //case View.MYCOMMISSION: view = 
         
         //장바구니
         case View.CART: view = cartService.CartList(); break;
         //주문하기
         case View.CART_BUY: view = cartService.CartBuy(); break;
         //장바구니에서 삭제
         case View.CART_DELETE: view = cartService.CartDelete(); break;
         //결제수단 선택
         case View.PAYTYPE_CHOICE: view = cartService.Paychoice(); break;
            
         //결제진행
         case View.PAY: view = cartService.PayStart(); break;
         //카드
         case View.PAY_CARD: view = cartService.CreditCard(); break;
         //카드번호확인
         case View.CARD_NUMBER: view = cartService.CardNumber(); break;
         //카드 CVC
         case View.CARD_CVC: view = cartService.CardCVC(); break;
         
           //계좌 
         case View.PAY_ACCOUNT: view = cartService.AccountPay(); break;
         
         //핸드폰결제
         case View.PAY_PHONE: view = cartService.Phonepay(); break;
         //통신사선택
         case View.MOBILE_COMPANY: view = cartService.MoblieCompany(); break;
         //핸드폰 인증
         case View.CERTIFICATION: view = cartService.Certification(); break;
         //최종결제
         case View.PAY_END: view = cartService.PayEnd(); break;
            
            
         //주문,결제내역 홈
         case View.ORDER_PAY: view = orderService.orderAndPayList(); break;
         //주문내역
         case View.ORDER_LIST: view = orderService.OrderList(); break;
         //결제내역
         case View.PAY_LIST: view = payService.PayDetail(); break;
         
         
         
         //관리자가 강의내역 조회
         
          //관리자가 회원조회시
            case View.MEMBERINFO: view = adminService.memberInfo(); break;
            //관리자가 수강생 조회
            case View.MEMBERSTUDENT: view = adminService.studentList(); break;
            //관리자가 수강생 상세조회
            case View.MEMBER_STUDENT_DETAIL: view = adminService.studentDetail(); break;
            //관리자가 지식공유자 조회
            case View.MEMBERMENTO: view = adminService.mentoList();break;
            //관리자가 지식공유자 상세조회
            case View.MEMBER_MENTO_DETAIL: view = adminService.mentoDetail(); break;
         
         }
         
         
      }
      
   }


   private int home() {
      
      //비회원일 때
      if(loginUser == null) {
         return mainHome();
      }
      
      //관리자일 때 - 관리자의 아이디 비번
      if(loginAdmin != null) {
         return adminHome();
      }
      
      //회원일 때
      System.out.println("1.강의목록 조회 2.커뮤니티 접속 3.내 정보 4.로그아웃 0.종료");
      int input = ScanUtil.nextInt();
      switch(input) {
      case 1: return View.LECTURE_LIST;
      case 2: return View.COMMUNITY_HOME;
      case 3: return View.MYINFO;
      case 4: System.out.println("로그아웃"); loginUser = null; return View.LOGOUTHOME;
      case 0:
         System.out.println("프로그램이 종료됩니다.");
         System.exit(0);
      }
      
      return View.HOME;
   }
   
   //비회원(로그아웃 된) 메인 홈페이지
   private int mainHome(){
	  System.out.println("╔═════════╗");
      System.out.println("🌱INFEARN🌱");
      System.out.println("╚═════════╝");
      System.out.println("1.강의목록 조회 2.커뮤니티 접속 3.로그인 4.회원가입 0.종료");
      int input = ScanUtil.nextInt();
      switch(input) {
      case 1://강의목록 조회
         return View.LECTURE_LIST; 
      case 2://커뮤니티
         return View.COMMUNITY_HOME;
      case 3://로그인
         return View.LOGIN;
      case 4://회원가입
         return View.JOIN;
      case 0:
         System.out.println("프로그램이 종료됩니다.");
         System.exit(0);
      }
      return View.LOGOUTHOME;
   }
   
   //관리자로 로그인 시
   private int adminHome(){
      
      System.out.println("1.강의목록 조회 2.커뮤니티 접속 3.회원정보 조회 4.로그아웃 0.종료");
      int input = ScanUtil.nextInt();
      switch(input) {
      //강의목록
      case 1: return View.ADMIN_LECTURE; 
      //커뮤니티
      case 2: return View.COMMUNITY_HOME;
      //회원정보 조회
      case 3: return View.MEMBERINFO;
      //로그아웃 하고 비회원 홈으로
      case 4: loginAdmin = null; return View.LOGOUTHOME;
      case 0: 
         System.out.println("프로그램이 종료됩니다.");
         System.exit(0);
      }
      return 0;
   }
   
}