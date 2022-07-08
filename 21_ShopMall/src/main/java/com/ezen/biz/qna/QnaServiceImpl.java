package com.ezen.biz.qna;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.biz.dao.QnaDAO;
import com.ezen.biz.dto.QnaVO;

@Service("qnaService")
public class QnaServiceImpl implements QnaService {

	@Autowired
	public QnaDAO qDao;
	
	public List<QnaVO> getListQna(String id) {
		return qDao.listQna(id);
	}
	
	public QnaVO getQna(int qseq) {
		return qDao.getQna(qseq);
	}

	public void insertQna(QnaVO vo) {
		qDao.insertQna(vo);
	}
}
