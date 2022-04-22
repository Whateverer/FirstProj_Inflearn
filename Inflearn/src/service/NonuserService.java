package service;

public class NonuserService {

	//싱글톤패턴
	private NonuserService() {}
	private static NonuserService instance;
	public static NonuserService getInstance() {
		if(instance == null) {
			instance = new NonuserService();
		}
		return instance;
	}
	
	//비회원일 때 고르기
	public int mainView() {
		
		return 0;
	}
	
	
	//강의목록 
}
