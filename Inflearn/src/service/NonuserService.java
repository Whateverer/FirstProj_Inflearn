package service;

public class NonuserService {

	//�̱�������
	private NonuserService() {}
	private static NonuserService instance;
	public static NonuserService getInstance() {
		if(instance == null) {
			instance = new NonuserService();
		}
		return instance;
	}
	
	//��ȸ���� �� ����
	public int mainView() {
		
		return 0;
	}
	
	
	//���Ǹ�� 
}
