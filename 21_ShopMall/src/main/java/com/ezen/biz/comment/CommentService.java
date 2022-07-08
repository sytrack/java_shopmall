package com.ezen.biz.comment;

import java.util.HashMap;
import java.util.List;

import com.ezen.biz.dto.ProductCommentVO;

import utils.Criteria;

public interface CommentService {

	int saveComment(ProductCommentVO vo);
		
	List<ProductCommentVO> getCommentList (int pseq);
	
	List<ProductCommentVO> getCommentListWithPaging(Criteria criteria, int pseq);
	
	int countCommentList(int pseq);
}
