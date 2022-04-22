package service;

import java.util.List;
import java.util.Map;

import util.View;
import dao.MemberInfoDao;

public class MemberInfoService {

	private MemberInfoService() {}
	private static MemberInfoService instance;

	public static MemberInfoService getInstance() {
		if(instance == null) {
			instance = new MemberInfoService();
		}
		return instance;
	}
	
	MemberInfoService memberInfo = MemberInfoService.getInstance();
	
	//수강생 정보 목록 조회
	public int StudentList(){
		List<Map<String, Object>> StudentList = MemberInfoDao.selectStudentList();
		System.out.println("==============수강생 정보 조회==============");
		System.out.println("이름\t아이디\t이메일\tHP\t회원 탈퇴 여부");
		for(Map<String, Object> student : StudentList){
			System.out.println("-------------------------------------");
			System.out.println(student.get("MEMBER_NAME")
			+ "\t" + student.get("MEMBER_ID")
			+ "\t" + student.get("MEMBER_EMAIL")
			+ "\t" + student.get("MEMBER_HP")
			+ "\t" + student.get("MEMBER_DELETE"));
			
		}
		
		System.out.println("==========================================");
		return View.MEMBERSTUDENT;
		
	}
	
	//지식공유자 정보 조회
	public int MentoList(){
		List<Map<String, Object>> MentoList = MemberInfoDao.selectMentoList();
		System.out.println("============지식 공유자 정보 조회=============");
		System.out.println("이름\t아이디\t이메일\tHP\t회원 탈퇴 여부");
		for(Map<String, Object> mento : MentoList){
			System.out.println("-----------------------------------------");
			System.out.println(mento.get("MEMBER_NAME")
			+ "\t" + mento.get("MEMBER_ID")
			+ "\t" + mento.get("MEMBER_EMAIL")
			+ "\t" + mento.get("MEMBER_HP")
			+ "\t" + mento.get("MEMBER_DELETE"));
			
		}
		return View.MEMBERMENTO;
	}
	
	
}