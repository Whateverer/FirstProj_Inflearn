package util;

public interface View {
   
   //로그인된 홈
   int HOME = 0;
   int LOGIN = 1;
   int JOIN = 2;
   
   //관리자 로그인 홈
   int ADMINHOME = 3;
   //비회원일 때
   int LOGOUTHOME = 4;
   
   //강의 목록
   int LECTURE_LIST = 5;
   int LECTURE_DETAIL = 6;
   int LECTURE_INSERT = 7;
   
   //관리자 강의
   int ADMIN_LECTURE = 10;
   //관리자 승인된 강의
   int LECTURE_ALLOW = 11;
   //관리자 승인 대기 강의목록
   int LECTURE_WAIT = 12;
   //업로드 대기 강의
   int LECTURE_UPLOAD_WAIT = 13;
   int LECTURE_UPLOAD_WAIT_DETAIL = 14;
   //수정승인 대기 강의
   int LECTURE_REVISE_WAIT = 16;
   int LECTURE_REVISE_WAIT_DETAIL = 17;
   //강의 수정 홈
   int LECTURE_REVISE = 18;
   //강의명 수정
   int LECTURE_NAMEUPDATE = 190;
   int LECTURE_TYPEUPDATE = 200;
   int LECTURE_PRICEUPDATE = 210;
   //강의 삭제
   int LECTURE_DELETE = 211;
   
   //커뮤니티
   int COMMUNITY_HOME = 20;
   int COMMUNITY_DETAIL = 21;
   int COMMUNITY_INSERT = 22;
   int COMMUNITY_UPDATE = 23;
   int COMMUNITY_DELETE = 24;
   //글 검색
   int COMMUNITY_SEARCH = 25;
   //커뮤니티 댓글
   int COMMUNITY_COMMENT = 26;
   int COMMUNITY_DEL_COMMENT = 27;
   int COMMUNUTY_CHOICE_COMMENT = 28;
   int COMMUNITY_MYCOMMENT_DEL = 29;
   //내 정보에 있을 뷰(수강생, 지식공유자 모두)
   //내 정보 홈
   int MYINFO = 30;
   int MYINFO_DETAIL = 31;
   //내 정보 수정
   int MYINFO_UPDATE = 32;
   int MYINFO_UPGRADE = 33;
   int MYINFO_QUIT = 34;
   //내 학습
   int MYSTUDY = 40;
   int MYSTUDY_DETAIL = 41;
   //학습하기(강의보기)
   int MYSTUDY_STUDY = 42;
   
   int MYBOARD = 50;
   //지식공유자만 조회하는 내 강의
   int MYLECTURE = 60;
   int MYLECTURE_LIST = 61;
   //지식공유자의 내 강의 상세
   int MYLECTURE_DETAIL = 62;
   int MYLECTURE_UPLOAD = 63;
   //지식공유자 정산
   int MYCOMMISSION = 65;
   
   
   //장바구니, 주문
   int CART = 70;
   //주문하기
   int CART_BUY = 73;
   //장바구니에서 삭제
   int CART_DELETE = 74;
   //결제수단 선택
   int PAYTYPE_CHOICE = 75;
   
   //결제
   int PAY = 76;
   //카드결제
   int PAY_CARD = 77;
   int CARD_NUMBER = 78;
   int CARD_CVC = 79;
   
   //계좌이체 결제
   int PAY_ACCOUNT = 80;
   //핸드폰 결제
   int PAY_PHONE = 85;
   int MOBILE_COMPANY = 86;
   int CERTIFICATION = 87;
   
   //최종 결제
   int PAY_END = 90;
   
   //주문내역, 결제내역
   int ORDER_PAY = 101;
   int ORDER_LIST = 100;
   int PAY_LIST = 115;
   
   
   //관리자가 강의조회
   int ADMINLECTURE = 150;
   //관리자가 회원조회시
   int MEMBERINFO = 160;
   //수강생 조회
   int MEMBERSTUDENT = 170;
   //수강생 상세조회
   int MEMBER_STUDENT_DETAIL = 120;
   //지식공유자 조회
   int MEMBERMENTO = 130;
   //지식공유자 상세조회
   int MEMBER_MENTO_DETAIL = 140;
   
   
   
}
