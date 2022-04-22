package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;



public class CommunityDao {
   
        private CommunityDao(){};
        private static CommunityDao instance;
       public static CommunityDao getInstance(){
          if (instance == null) {
             instance = new CommunityDao();
          }
          return instance;
       }
       JDBCUtil jdbc = JDBCUtil.getInstance();
       
    //커뮤니티 목록 조회   
    public List<Map<String, Object>> CommunityList(){
       String sql = "SELECT BOARD_NO, BOARD_TITLE,  MEMBER_ID, BOARD_DATE"
             + "     FROM COMMUNITY"
             + "    ORDER BY 1 DESC";
       
        return jdbc.selectList(sql);
    }
    //커뮤니티 글 조회
    public List<Map<String, Object>> ReadCommunityList(int ReadInput){
       String sql = "SELECT BOARD_NO, BOARD_TITLE, BOARD_CONTENT, MEMBER_ID, BOARD_DATE"
             + "     FROM COMMUNITY"
             + "    WHERE BOARD_NO = ?";
       List<Object> param = new ArrayList<>();
      param.add(ReadInput);
       
      return jdbc.selectList(sql, param);
    }
    
    //커뮤니티 글 등록
    public int InsertCommunityList(String BOARD_TITLE,String BOARD_CONTENT){
       String sql = "INSERT INTO COMMUNITY VALUES"
             + " (BOARD_NO_SEQ.NEXTVAL,?,?,?,SYSDATE)";
       List<Object> param = new ArrayList<>();
       param.add(BOARD_TITLE);
       param.add(BOARD_CONTENT);
       param.add(Controller.loginUser.get("MEMBER_ID"));
       
       return jdbc.update(sql, param);
    }
    
    //커뮤니티 글 수정
    public int UpdateCommunityList(String title,String content,int ReadInput){
       String sql = "UPDATE COMMUNITY"
             + "      SET BOARD_TITLE = ?, BOARD_CONTENT = ?"
             + "    WHERE BOARD_NO = ?";
       List<Object> param = new ArrayList<>();
       param.add(title);
       param.add(content);
       param.add(ReadInput);
       
       return jdbc.update(sql, param);
    }
    
    //커뮤니티 글 삭제
    public int DeleteCommunityList(int ReadInput){
       String sql = "DELETE COMMUNITY"
             + "    WHERE BOARD_NO = ?";
       List<Object> param = new ArrayList<>();
       param.add(ReadInput);
       
       return jdbc.update(sql, param);
    }
    
    //커뮤니티 태그 조회
    public List<Map<String, Object>> SearchCommunityList(String tag){
       String sql = "SELECT BOARD_NO, BOARD_TITLE, MEMBER_ID, BOARD_DATE"
             + "     FROM COMMUNITY"
             + "    WHERE BOARD_TITLE LIKE ? ";
            
       List<Object> param = new ArrayList<>();
       param.add("%"+tag+"%");
       
       return jdbc.selectList(sql, param);
                   
    }
    
    //커뮤니티 댓글등록  //댓글번호,글번호,회원아이디,내용,날짜
    public int InsertComment(int ReadInput, String com){
       String sql = " INSERT INTO BOARD_RE VALUES "
            + " (RE_NO.NEXTVAL,?,?,?,SYSDATE)";
       List<Object> param = new ArrayList<>();
      param.add(ReadInput);
      param.add(Controller.loginUser.get("MEMBER_ID"));   
      param.add(com);
      
      return jdbc.update(sql, param);
    }
    
    //커뮤니티 댓글삭제
    public int DeleteComment(int deleteinput){
       String sql = " DELETE BOARD_RE"
             +"      WHERE RE_NO = ?";
       List<Object> param = new ArrayList<>();
       param.add(deleteinput);
       
       return jdbc.update(sql, param);
    }
    //내가 작성한 댓글 삭제
    public int MyCommentDelete(int input, String userId){
       String sql = " DELETE BOARD_RE"
             +"      WHERE RE_NO = ?"
             +"        AND MEMBER_ID =?";
       
    List<Object>param = new ArrayList<>();
    param.add(input);
    param.add(userId);
    
    return jdbc.update(sql, param);
    }
    
    
    //커뮤니티 댓글 조회
    public List<Map<String, Object>> comment(int ReadInput) {
      String sql = "   SELECT A.RE_NO, A.RE_CONTENT, A.MEMBER_ID, A.RE_DATE "
               +  "     FROM ( SELECT B.RE_NO, B.RE_CONTENT, B.MEMBER_ID, B.RE_DATE " 
               +  "              FROM COMMUNITY A  "
                   +  "        INNER JOIN BOARD_RE B ON(A.BOARD_NO = B.BOARD_NO) "
                   +  "        WHERE A.BOARD_NO = ? "
               +  "        ORDER BY B.RE_NO)A       "; 

        List<Object> param = new ArrayList<>();
      param.add(ReadInput);
      
      return jdbc.selectList(sql, param);
   }
    
   //글 작성자 조회
    public Map<String, Object> member(int ReadInput) {
       String sql = "   SELECT MEMBER_ID"
             + "        FROM COMMUNITY"
             + "       WHERE BOARD_NO = ?";
       
       List<Object> param = new ArrayList<>();
       param.add(ReadInput);
       
       return jdbc.selectOne(sql, param);
    }
    
}