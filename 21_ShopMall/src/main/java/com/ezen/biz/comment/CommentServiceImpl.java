package com.ezen.biz.comment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.biz.dao.CommentDAO;
import com.ezen.biz.dto.ProductCommentVO;

import utils.Criteria;

@Service("commentService")
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentDAO cDao;
	
	@Override
	public int saveComment(ProductCommentVO vo) {

		return cDao.saveComment(vo);
	}

	@Override
	public List<ProductCommentVO> getCommentList(int pseq) {
		
		return cDao.commentList(pseq);
	}

	@Override
	public List<ProductCommentVO> getCommentListWithPaging(Criteria criteria, int pseq) {
		
		return cDao.commentListWithPaging(criteria, pseq);
	}

	@Override
	public int countCommentList(int pseq) {
		
		return cDao.countCommentList(pseq);
	}

}
