package service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.AdminDao;
import dao.MyInfoDao;
import util.ScanUtil;
import util.View;


public class AdminService {
   
   MyInfoDao myinfodao = MyInfoDao.getInstance();
   
   public int type = 0;

   public AdminDao admindao = AdminDao.getInstance();
   private AdminService(){};
   private static AdminService instance;
   public static AdminService getInstance(){
      if(instance == null){
         instance = new AdminService();
      }
      return instance;
   }
   
   

   public int memberInfo(){
   System.out.println("1.수강생 정보 조회 2.지식공유자 정보 조회");
   int input1 = ScanUtil.nextInt();
   switch(input1){
   case 1: return View.MEMBERSTUDENT;
   case 2: return View.MEMBERMENTO;   
     }
   return View.MEMBERINFO;
   }
   
   //수강생 목록 조회
   public int studentList(){
     type = 1;
      List<Map<String, Object>> studentList = admindao.selectStudentList(type);
      System.out.println("===================수강생 list===================");
      System.out.println("이름\t수강생ID\t이메일\tHP\t회원탈퇴 여부");
      for(Map<String, Object> student : studentList){
         System.out.println("----------------------------------------------");
         System.out.println(student.get("MEMBER_NAME")
         + "\t"+ student.get("MEMBER_ID")
         + "\t"+ student.get("MEMBER_EMAIL")
         + "\t"+ student.get("MEMBER_HP")
         + "\t"+ student.get("MEMBER_DELETE"));
         
      }
      
      System.out.println("===============================================");
      System.out.println("1.수강생 상세 조회 0.뒤로가기");
      int input = ScanUtil.nextInt();
      switch(input){
      case 1: return View.MEMBER_STUDENT_DETAIL;
      case 0: return View.HOME;
      }
      return View.ADMINHOME;
      
   }
   
   //지식공유자 목록 조회
      public int mentoList(){
         type = 2;
         List<Map<String, Object>> studentList = admindao.selectStudentList(type);
         System.out.println("===================지식공유자 list===================");
         System.out.println("이름\t지식공유자ID\t이메일\tHP\t회원탈퇴 여부");
         for(Map<String, Object> student : studentList){
            System.out.println("----------------------------------------------");
            System.out.println(student.get("MEMBER_NAME")
            + "\t"+ student.get("MEMBER_ID")
            + "\t"+ student.get("MEMBER_EMAIL")
            + "\t"+ student.get("MEMBER_HP")
            + "\t"+ student.get("MEMBER_DELETE"));
            
         }
         System.out.println("===============================================");
         System.out.println("1.지식 공유자 상세 조회 0.뒤로가기");
         int input = ScanUtil.nextInt();
         switch(input){
         case 1: return View.MEMBER_MENTO_DETAIL;   
         case 0: return View.HOME;
         }
         return View.ADMINHOME;
      }
      
   
   //수강생 상세 조회
   public int studentDetail(){
      System.out.println("조회할 수강생 ID를 입력하세요.>");
      String userId = ScanUtil.nextLine();
      Map<String, Object> student = admindao.studentList(userId);
      
      System.out.println("====================================");
      System.out.println("수강생 ID: "+ student.get("MEMBER_ID"));
      System.out.println("------------------------------------");
      System.out.println("이름: "+ student.get("MEMBER_NAME"));
      System.out.println("------------------------------------");
      System.out.println("현재 듣고 있는 강의 수: "+ student.get("LCNT"));
      System.out.println("------------------------------------");
      System.out.println("이메일: "+ student.get("MEMBER_EMAIL"));
      System.out.println("------------------------------------");
      System.out.println("핸드폰 번호: "+ student.get("MEMBER_HP"));
      System.out.println("------------------------------------");
      System.out.println("회원탈퇴 여부: "+ student.get("MEMBER_DELETE"));
      System.out.println("====================================");
      
      //강의코드, 강의 분류, 강의명, 진도율을 리스트로 만들기..
      List<Map<String, Object>> DetailList = myinfodao.getStudyList(userId);
      System.out.println("강의코드\t강의분류\t강의명\t진도율");
      for(Map<String, Object> lcnt : DetailList){
         System.out.println("----------------------------------");
         System.out.println (lcnt.get("LECTURE_NO")
         + "\t" + lcnt.get("LECTURE_CODE_NAME")
         + "\t" + lcnt.get("LECTURE_NAME")
         + "\t" + lcnt.get("LEARN_PROGRESS"));
      
      }
      return View.ADMINHOME;
   }
         
      
      //지식공유자 상세 조회
      public int mentoDetail(){
         System.out.println("조회할 수강생 ID를 입력하세요.>");
         String userId = ScanUtil.nextLine();
         Map<String, Object> mento = admindao.selectmentoList(userId);
         
        
         System.out.println("====================================");
         System.out.println("수강생 ID: "+ mento.get("MEMBER_ID"));
         System.out.println("------------------------------------");
         System.out.println("이름: "+ mento.get("MEMBER_NAME"));
         System.out.println("------------------------------------");
         System.out.println("현재 듣고 있는 강의 수: "+ mento.get("LCNT"));
         System.out.println("------------------------------------");
         System.out.println("이메일: "+ mento.get("MEMBER_EMAIL"));
         System.out.println("------------------------------------");
         System.out.println("핸드폰 번호: "+ mento.get("MEMBER_HP"));
         System.out.println("------------------------------------");
         System.out.println("회원탈퇴 여부: "+ mento.get("MEMBER_DELETE"));
         System.out.println("====================================");
         
         //강의코드, 강의 분류, 강의명, 진도율을 리스트로 만들기..
         List<Map<String, Object>> studentDetailList = admindao.mentoDetailList(userId);
         System.out.println("강의코드\t강의분류\t강의명\t진도율");
         for(Map<String, Object> lcnt : studentDetailList){
            System.out.println("----------------------------------");
            System.out.println (lcnt.get("LECTURE_CODE")
            + "\t" + lcnt.get("LECTURE_NO")
            + "\t" + lcnt.get("LECTURE_NAME")
            + "\t" + lcnt.get("LEARN_PROGRESS"));
         }
         
         
         List<Map<String, Object>> mentolecsellist = admindao.lectsellist();
         System.out.println("====================================");
         System.out.println("강의코드\t강의명\t누적 학생수\t누적 금액");
         for(Map<String, Object> sell : mentolecsellist){
         System.out.println("----------------------------------");
         System.out.print(sell.get("LECTURE_CODE")+"\t");
         System.out.print(sell.get("LECTURE_NAME")+"\t");
         System.out.print(sell.get("LECTURE_CODE")+"\t");
         System.out.println(sell.get("LECTURE_CODE")+"\t");
         }
         
         return View.ADMINHOME;
         }   
       
      
   }
   
   
      
   
   