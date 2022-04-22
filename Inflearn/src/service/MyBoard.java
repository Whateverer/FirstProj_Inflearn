package service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import util.ScanUtil;
import util.View;
import controller.Controller;
import dao.MyBoardDao;

public class MyBoard {
   private MyBoard(){};
   private static MyBoard instance;
   public static MyBoard getInstance(){
      if (instance == null) {
         instance = new MyBoard();
      }
      return instance;
   }
   public MyBoardDao myinboarddao = MyBoardDao.getInstance();
   SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
 //내 커뮤니티 글 조회
   public int MyCommunityList(){
         while(true){
            String userId = (String)Controller.loginUser.get("MEMBER_ID");
            
            List<Map<String, Object>> mycommunityList = myinboarddao.MyCommunityList(userId);
            
            if(mycommunityList.size()>0){
            System.out.println("==============나의 커뮤니티 글 목록==============");
            System.out.println("번호\t제목\t\t작성자\t작성일");
            
            for(Map<String, Object> mycommunity : mycommunityList){
               String time1 = format.format(mycommunity.get("BOARD_DATE"));
               System.out.println(mycommunity.get("BOARD_NO")
                    + "\t" +mycommunity.get("BOARD_TITLE")   
                     + "\t\t" +mycommunity.get("MEMBER_ID")
                     + "\t" + time1); 
            }
             System.out.println("===========================================");
            }else{
               System.out.println("등록된 커뮤니티 글이 존재하지않습니다.");
               return View.MYINFO;
            }
             System.out.println("1.글 조회하기  0.뒤로가기");
             int input = ScanUtil.nextInt();
             
             switch(input){
                
           case 1 : return View.COMMUNITY_DETAIL;
       
           case 0 :
              return View.MYINFO; 
       }

         }
      }
   
   
   }
   