package com.ezen.biz.qna;

import java.util.List;

import com.ezen.biz.dto.QnaVO;

public interface QnaService {

	List<QnaVO> getListQna(String id);

	QnaVO getQna(int qseq);
	
	void insertQna(QnaVO vo);
}