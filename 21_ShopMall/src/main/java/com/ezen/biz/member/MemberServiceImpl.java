package com.ezen.biz.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.biz.dao.MemberDAO;
import com.ezen.biz.dto.AddressVO;
import com.ezen.biz.dto.MemberVO;

@Service("memberService")
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDAO mDao;
	
	@Override
	public MemberVO getMember(String id) {
		
		return mDao.getMember(id);
	}

	@Override
	public int confirmID(String id) {
		
		return mDao.confirmID(id);
	}

	@Override
	public void insertMember(MemberVO vo) {
		
		mDao.insertMember(vo);
	}

	@Override
	public int loginID(MemberVO vo) {
		
		return mDao.loginID(vo);
	}

	@Override
	public List<AddressVO> selectAddressByDong(String dong) {
		
		return mDao.selectAddressByDong(dong);
	}

	@Override
	public String selectIdByNameEmail(MemberVO vo) {
		
		return mDao.selectIdByNameEmail(vo);
	}

	@Override
	public String selectPwdByIdNameEmail(MemberVO vo) {
		
		return mDao.selectPwdByIdNameEmail(vo);
	}

	@Override
	public void changePassword(MemberVO vo) {
		
		mDao.changePassword(vo);
	}

}
