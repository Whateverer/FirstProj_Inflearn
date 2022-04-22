package service;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.ScanUtil;
import util.View;
import dao.CommunityDao;
public class CommunityService {
   
   private CommunityService(){};
   private static CommunityService instance;
   public static CommunityService getInstance(){
      if (instance == null) {
         instance = new CommunityService();
      }
      return instance;
   }
   
   private CommunityDao communitydao = CommunityDao.getInstance();
   SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
   Date time = new Date();
   public static int ReadInput;
   String memberid = null;
   //커뮤니티 목록 조회
   public int CommunityList(){
      while(true){
         List<Map<String, Object>> communityList = communitydao.CommunityList();
         System.out.println("================커뮤니티 글 목록===============");
         System.out.println("번호\t제목\t\t작성자\t작성일");
         
         for(Map<String, Object> community : communityList){
            String time1 = format.format(community.get("BOARD_DATE")); 
            System.out.println(community.get("BOARD_NO")
                  + "\t" +community.get("BOARD_TITLE")
                  + "\t\t" +community.get("MEMBER_ID")
                  + "\t" + time1);  
         }
          System.out.println("===========================================");
         
          System.out.println("1.글 조회하기 2.글 등록하기 3.검색하기 0.뒤로가기");
          int input = ScanUtil.nextInt();
          
          switch(input){
             
              case 1 : return View.COMMUNITY_DETAIL;  
          
              case 2 : return View.COMMUNITY_INSERT; 
             
              case 3 : return View.COMMUNITY_SEARCH;   
                 
              case 0 :
                 return View.HOME;//리턴주소?
          }
      }
   }
   //커뮤니티 글 수정
   public int UpdateCommu(){
      System.out.println("1.새로운 제목을 입력해주세요.");
      String title = ScanUtil.nextLine();
      
      System.out.print("2.새로운 내용을 입력해주세요.");
      String content = ScanUtil.nextLine();
      
      int UpdateCommunity = communitydao.UpdateCommunityList(title,content,ReadInput);
      if(UpdateCommunity > 0){
      System.out.println("커뮤니티 글이 수정되었습니다.");
      
      return View.COMMUNITY_HOME;
     }else{
       System.out.println("커뮤니티 글 수정 실패");
       return View.COMMUNITY_HOME;
     }
   }  
   
   //커뮤니티 글 삭제
   public int DeleteCommu(){
      System.out.println("정말 삭제하시겠습니까?");
      System.out.println("1.삭제\t2.취소");
      int Delinput = ScanUtil.nextInt();
      
      if(Delinput==1){
         communitydao.DeleteCommunityList(ReadInput);
         System.out.println("작성하신 글이 삭제되었습니다.");
         return View.COMMUNITY_HOME;
         }
      if(Delinput==2){
         System.out.println("커뮤니티 글 삭제 취소");
         return View.COMMUNITY_HOME;
      }else{
         return View.COMMUNITY_HOME;
      }
   }
   //커뮤니티 글 조회                 
   public int ReadCommu(){

      System.out.println("조회할 글의 번호를 입력해주세요.");
      ReadInput = ScanUtil.nextInt();
      
      List<Map<String, Object>> ReadCommunity = communitydao.ReadCommunityList(ReadInput);
     
      if(ReadCommunity.size() < 1){
         System.out.println("존재하지 않는 게시물 번호입니다.");
         return View.COMMUNITY_HOME; 
      }
      else{
         System.out.println("=========================================조회한 커뮤니티 글=========================================");
          System.out.println("작성자\t제목\t내용");
         for(Map<String, Object> read : ReadCommunity){
            String time1 = format.format(read.get("BOARD_DATE")); 
            System.out.println(read.get("MEMBER_ID")
                  + "\t" +read.get("BOARD_TITLE")
                  + "\t" +read.get("BOARD_CONTENT"));
                  
            memberid = (String)read.get("MEMBER_ID");
          
         }
      }
      //댓글 출력
      System.out.println("---------------------------------------------댓글목록--------------------------------------------");
      System.out.println("댓글번호\t작성자\t내용");
      List<Map<String, Object>> comment = communitydao.comment(ReadInput);
      for (Map<String, Object> re : comment) {
         String time1 = format.format(re.get("RE_DATE"));
         System.out.print(re.get("RE_NO"));
         System.out.print("\t"+re.get("MEMBER_ID"));
         System.out.println("\t"+re.get("RE_CONTENT"));
//         System.out.println(time1);
      }
      
      System.out.println("===============================================================================================");
     
      
      
      Map<String, Object> member = communitydao.member(ReadInput);
      
      //비회원일 때 
      if(Controller.loginUser == null){
        
             while(true){
                    System.out.println("1.로그인  0.돌아가기");
                    int input = ScanUtil.nextInt();
                    if(input==1){
                       return View.LOGIN;
                    }if(input==0){
                       return View.COMMUNITY_HOME;
                    }else{
                       System.out.println("입력이 올바르지 않습니다.");
                    }
             }
          }
      
    //작성자거나  관리자일 때 수정 삭제 가능
      if(((String)Controller.loginUser.get("MEMBER_ID")).equals(member.get("MEMBER_ID"))){
         
         
         System.out.println("1.글 수정하기 2.글 삭제하기 3.댓글달기 4.댓글삭제 0.돌아가기");
         int input = ScanUtil.nextInt();
         switch(input){
           
         case 1:
            //커뮤니티 글 수정
            return View.COMMUNITY_UPDATE;
         case 2:
            //커뮤니티 글 삭제
            return View.COMMUNITY_DELETE;
         case 3:
            //커뮤니티 글 댓글등록
           return View.COMMUNITY_COMMENT; 
         case 4:   
           //커뮤니티 댓글 삭제
           return View.COMMUNITY_DEL_COMMENT;
           
         case 0:
           //홈으로
            return View.COMMUNITY_HOME; 
         }
      }

   return View.COMMUNUTY_CHOICE_COMMENT;
  }
   public int CommentChoice(){
      System.out.println("1.댓글달기 2.댓글삭제 3.커뮤니티 목록 0.홈으로");
      int input = ScanUtil.nextInt();
      switch(input){
      case 1: 
         return View.COMMUNITY_COMMENT;
      case 2:
         return View.COMMUNITY_MYCOMMENT_DEL;
      case 3:
         return View.COMMUNITY_HOME;
      case 0:
         return View.HOME; 
      }
      return View.HOME;
   }
   
   //커뮤니티 댓글 입력
   public int InsertComment(){
   System.out.print  ("댓글입력 : ");
   String com = ScanUtil.nextLine();
   int insertComment = communitydao.InsertComment(ReadInput, com);
   if(insertComment > 0){
      System.out.println("댓글이 등록되었습니다");
      return View.COMMUNITY_HOME;
   }else{
      System.out.println("댓글 등록에 실패했습니다.");
      return View.COMMUNITY_HOME;
   }
  }
   
   //커뮤니티 댓글 삭제
   public int DeleteComment(){
      System.out.println("삭제할 댓글 번호를 입력해주세요.");
       int deleteinput = ScanUtil.nextInt();
       int deleteComment = communitydao.DeleteComment(deleteinput);
       if(deleteComment > 0){
          System.out.println("댓글이 삭제되었습니다.");
             return View.COMMUNITY_HOME;
       }else{
          System.out.println("댓글 삭제에 실패했습니다.");
             return View.COMMUNITY_HOME;
       }
   }
   
   //댓글작성자 댓글삭제
   public int MyCommentDel(){
      String userId = (String)Controller.loginUser.get("MEMBER_ID");
      System.out.println("삭제할 댓글 번호를 입력해주세요.");
      int input = ScanUtil.nextInt();
      int mycommentdel = communitydao.MyCommentDelete(input, userId);
      if(mycommentdel > 0){
         System.out.println("댓글이 삭제되었습니다.");
         return View.COMMUNITY_HOME;
      }else{
         System.out.println("댓글 삭제에 실패했습니다.");
         return View.COMMUNITY_HOME;
      }
   }
   
   
   //커뮤니티 글 등록
   public int InsertCommu(){
      if(Controller.loginUser == null){
         System.out.println("로그인이 필요합니다.");
         return View.LOGIN;
      }else{
         System.out.println("=============커뮤니티 글 작성=============");
         System.out.print("제목을 작성해주세요. : ");
         String title = ScanUtil.nextLine();
         System.out.print("내용을 작성해주세요. : ");
         String content = ScanUtil.nextLine();
          
         System.out.println("1.등록\t2.취소");
         int input = ScanUtil.nextInt();
         if(input==1){
             communitydao.InsertCommunityList(title, content);
            System.out.println("커뮤니티 글 등록 완료.");
         }if(input==2){
             System.out.println("커뮤니티 글 등록 취소.");
          return View.COMMUNITY_HOME;
         }else{
          return View.COMMUNITY_HOME;
         }
      }
   }
   //커뮤니티 글 제목 태그로 검색
   public int SearchCommu(){
      
      System.out.println("검색할 *제목*의 태그를 입력해주세요.");
      String tag = ScanUtil.nextLine();
      List<Map<String, Object>> list = communitydao.SearchCommunityList(tag);
      if(list.size() < 1){
       System.out.println("***********검색조건의 결과가 없습니다.***********");
       return View.COMMUNITY_HOME;
      }
       while(list.size() > 0){
          System.out.println("===============커뮤니티 글 목록===============");
            System.out.println("번호\t제목\t\t작성자\t작성일");
            
            for(Map<String, Object> tagcom :list){
               String time1 = format.format(tagcom.get("BOARD_DATE")); //?
               System.out.println(tagcom.get("BOARD_NO")
                     + "\t"+tagcom.get("BOARD_TITLE")
                     + "\t\t" +tagcom.get("MEMBER_ID")
                     + "\t" + time1);  
            }
             System.out.println("==========================================");
            
             System.out.println("1.글 조회하기 2.글 등록하기 3.검색하기 0.뒤로가기");
             int input = ScanUtil.nextInt();
             
             switch(input){
                
                 case 1 : return View.COMMUNITY_DETAIL; 
             
                 case 2 : return View.COMMUNITY_INSERT;
                
                 case 3 : return View.COMMUNITY_SEARCH;   
                    
                 case 0 :
                    return View.COMMUNITY_HOME; //리턴주소?
             }
         }
     
    return View.COMMUNITY_HOME;
   }
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   

}