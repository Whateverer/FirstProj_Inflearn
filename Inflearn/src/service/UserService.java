package service;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.Controller;
import util.ScanUtil;
import util.View;
import dao.UserDao;

public class UserService {

   private UserService() {
   }

   private static UserService instance;

   public static UserService getInstance() {
      if (instance == null) {
         instance = new UserService();
      }
      return instance;
   }

   private UserDao userDao = UserDao.getInstance();
   SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
   Date time = new Date(0);

   public int join() {
      String userId;
      String password;
      String userName;
      String phonecall;
      String email;
      String bank;
      String account;
      String postNumber;
      String address;
      String addressDetail;

      System.out.println("============회원가입=============");

      // 아이디

      while (true) {
         while (true) {
            System.out.println("아이디(4자 이상)>");
            userId = ScanUtil.nextLine();
            String IdRegrex = "^[a-zA-Z0-9_-]{4,100}$";
            Matcher matcher = Pattern.compile(IdRegrex).matcher(userId);
            // 아이디를 가져와서 정규표현식과 매치를 시킨다
            if (matcher.matches() == false) {// 매치한게 맞지 않을때
               System.out.println("4~100자의 영문, 숫자와 특수기호(_)(-)만 사용가능합니다");
            } else {// 매치한게 맞을때
               break;
            }
         }

         Map<String, Object> user = userDao.selectUser2(userId);
         Map<String, Object> check = userDao.checkId(userId);
         if (check != null && user.get("MEMBER_DELETE").equals("N")) {
            // 삭제되면 1 안되면 0 : USER_DELETE가 0이면 삭제가 안된것
            System.out.println("중복되는 아이디입니다.");
         } else if (check != null && user.get("MEMBER_DELETE").equals("Y")) {
            break;
         } else {
            break;
         }
      }

      // 비밀번호
      while(true){
      while (true) {
         System.out.println("비밀번호>");
         password = ScanUtil.nextLine();
         String passwordRegrex = "^[a-zA-Z0-9_-]{4,50}$";
         // 영문자(소문자, 대문자), 숫자, 4부터 50

         Matcher matcher1 = Pattern.compile(passwordRegrex)
               .matcher(password);

         if (matcher1.matches() == false) {
            System.out.println("4-50자 영문, 숫자, 특수기호(-),(_)를 사용 가능합니다.");
         } else {
            break;
         }
         
      }
         System.out.println("비밀번호확인>");
         String passwordcheck = ScanUtil.nextLine();
         
         if (!password.equals(passwordcheck)) {
            System.out.println("비밀번호가 일치하지 않습니다.");
         } else {
            break;
         }
      }
      
      // 이름
      while (true) {
         System.out.println("이름>");
         userName = ScanUtil.nextLine();
         String nameRegrex = "^[가-힣]*$";
         Matcher matcher2 = Pattern.compile(nameRegrex).matcher(userName);

         if (matcher2.matches() == false) {
            System.out.println("한글 문자만 사용가능합니다");
         } else {
            break;
         }
      }

      // 핸드폰번호
      while (true) {
         System.out.print("핸드폰번호>");
         phonecall = ScanUtil.nextLine();
         String phoneRegrex = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$";
         Matcher matcher3 = Pattern.compile(phoneRegrex).matcher(phonecall);
         if (matcher3.matches() == false) {
            System.out.println("전화번호 형식은 010-1234-5678 입니다.");
         } else {
            break;
         }
      }
      
      //이메일 추가
      while(true){
         System.out.print("이메일>");
         email = ScanUtil.nextLine();
         String emailRegrex = "^\\w+@\\w+\\.\\w+(\\.\\w+)?$";
          Matcher matcher4 = Pattern.compile(emailRegrex).matcher(email);
         if(matcher4.matches() == false){
            System.out.println("올바른 이메일 형식이 아닙니다.");
         }else{
            break;
         }
      }
      
      //은행 추가
      while(true){
         System.out.print("은행>");
         bank = ScanUtil.nextLine();
         if (bank.equals("")) {
            System.out.println("필수항목입니다. 은행을 입력해주세요.");
         } else {
            break;
         }
      }
      
      
      //계좌번호 추가
      while(true){
         System.out.print("계좌번호>");
         account = ScanUtil.nextLine();
         if (account.equals("")) {
            System.out.println("필수항목입니다. 계좌번호를 입력해주세요.");
         } else {
            break;
         }
      }

      // 우편번호
      while (true) {
         System.out.print("우편번호>");
         postNumber = ScanUtil.nextLine();
         String postRegrex = "[0-9]{5}";
         Matcher matcher5 = Pattern.compile(postRegrex).matcher(postNumber);
         if (matcher5.matches() == false) {
            System.out.println("우편번호 5자리를 정확히 입력해주세요");
         } else {
            break;
         }
      }

      // 주소
      while (true) {
         System.out.print("주소>");
         address = ScanUtil.nextLine();
         if (address.equals("")) {
            System.out.println("필수항목입니다. 주소를 입력해주세요.");
         } else {
            break;
         }
      }

      // 상세주소
      System.out.print("상세주소>");
      addressDetail = ScanUtil.nextLine();

      // 해시맵에 입력데이터 넣기
      Map<String, Object> param = new HashMap<>();
      param.put("MEMBER_ID", userId);
      param.put("MEMBER_PASSWORD", password);
      param.put("MEMBER_NAME", userName);
      param.put("MEMBER_HP", phonecall);
      param.put("MEMBER_EMAIL", email);
      param.put("MEMBER_POSTCODE", postNumber);
      param.put("MEMBER_ADDRESS", address);
      param.put("MEMBER_ADDRESS_DETAL", addressDetail);
      param.put("MEMBER_BANK", bank);
      param.put("MEMBER_ACCOUNT", account);

      int result = userDao.insertUser(param);

      if (0 < result) {
         System.out.println("🎉회원가입 성공🎉");
      } else {
         System.out.println("회원가입 실패😢");
      }
      return View.HOME;

      
   }
   
   
   //로그인
   public int login(){
      while(true){
      System.out.println("🌱LOGIN🌱");
      System.out.print("아이디 입력>");
      String userId = ScanUtil.nextLine();
      System.out.println("비밀번호 입력>");
      String password = ScanUtil.nextLine();
      
      Map<String, Object> user = userDao.selectUser(userId, password); ////UserDao의 selectUser 메서드에서 유저를 조회
      
      if(user == null && userId.equals("MANAGER")&&password.equals("0000")){
         Map<String, Object> manager = userDao.manager();
         System.out.println("관리자 계정 로그인 ");
         Controller.loginAdmin = manager;
         return View.ADMINHOME; //리턴위치?
      
         //관리자 계정로그인
      }if(user == null){  
         System.out.println("아이디 혹은 비밀번호를 잘못 입력하셨습니다.");
      
         //((BigDecimal)map.get("ORDER_PRICE"))
      }else if(user.get("MEMBER_DELETE").equals("Y")){
         System.out.println("탈퇴된 회원입니다.");
         return View.LOGIN;
      }else if(((BigDecimal)user.get("DOMAIN_NO")).intValue() == 1){
         System.out.println(user.get("MEMBER_NAME")+" 수강자님 환영합니다.");
         Controller.loginUser = user;
         Controller.cartNo = userDao.getCartNo();
         return View.HOME;  //리턴위치?
      }else{
         System.out.println(user.get("MEMBER_NAME")+" 지식공유자님 환영합니다.");    
         Controller.loginUser = user;
         Controller.cartNo = userDao.getCartNo();
         return View.HOME;  //리턴위치?
      }
   }
   }   
}