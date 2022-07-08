package com.ezen.biz.member;

import java.util.List;

import com.ezen.biz.dto.AddressVO;
import com.ezen.biz.dto.MemberVO;

public interface MemberService {

	// 회원 상세정보 조회
	MemberVO getMember(String id);

	// 회원ID 존재 확인
	int confirmID(String id);

	// 회원 인증
	int loginID(MemberVO vo);
	
	// 회원 추가
	void insertMember(MemberVO vo);

	// 동이름으로 우편번호 찾기
	List<AddressVO> selectAddressByDong(String dong);
	
	// 이름과 이메일로 아이디 찾기
	String selectIdByNameEmail(MemberVO vo);
	
	// 아이디, 이름, 이메일로 비밀번호 찾기
	String selectPwdByIdNameEmail(MemberVO vo);
	
	void changePassword(MemberVO vo);
	
}