package com.ezen.biz.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezen.biz.dto.ProductCommentVO;

import utils.Criteria;

@Repository
public class CommentDAO {
	@Autowired
	private SqlSessionTemplate mybatis;
	
	public int saveComment(ProductCommentVO vo) {
		
		return mybatis.insert("CommentMapper.saveComment", vo);
		
	}
	public List<ProductCommentVO> commentList (int pseq) {
		
		return mybatis.selectList("CommentMapper.commentList", pseq);
	}
	
	public List<ProductCommentVO> commentListWithPaging(Criteria criteria, int pseq) {
		HashMap<String, Object> map = new HashMap<>();
		
		map.put("criteria", criteria);
		map.put("pseq", pseq);
		
		return mybatis.selectList("CommentMapper.commentListWithPaging", map);
	}
	
	public int countCommentList(int pseq) {
		
		return mybatis.selectOne("CommentMapper.countCommentList", pseq);
	}
}






