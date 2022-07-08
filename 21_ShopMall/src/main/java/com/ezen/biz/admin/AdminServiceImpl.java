package com.ezen.biz.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.biz.dao.AdminDAO;
import com.ezen.biz.dto.AdminVO;

@Service("adminService")
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminDAO adminDao;
	
	/*
	 * 입력: AdminVO (화면 입력 값)
	 * 
	 * 리턴값: 
	 * 		-1: id가 존재하지 않음
	 * 		 0: 비밀번호가 맞지 않음
	 * 		 1: 정상 로그인
	 */
	@Override
	public int adminCheck(AdminVO vo) {
		int result = -1;
		
		// Admin 테이블에서 pwd조회
		String pwd_in_db = adminDao.adminCheck(vo.getId());
		if (pwd_in_db == null) {
			result = -1;
		} else if (pwd_in_db.equals(vo.getPwd())) {
			result = 1;
		} else {
			result = 0;
		}
		return result;
	}

	@Override
	public AdminVO getAdmin(String id) {
		return adminDao.getAdmin(id);
	}

}







