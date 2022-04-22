package service;

import java.util.Map;

import controller.Controller;
import dao.StudyDao;

public class StudyService {
	
		private StudyService() {}
		private static StudyService instance;

		public static StudyService getInstance() {
			if(instance == null) {
				instance = new StudyService();
			}
			return instance;
		}
		
	StudyDao studyDao = StudyDao.getInstance();


	
	
	
}
