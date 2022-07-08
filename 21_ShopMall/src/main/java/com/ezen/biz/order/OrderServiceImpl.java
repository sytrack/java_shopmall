package com.ezen.biz.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.biz.dao.OrderDAO;
import com.ezen.biz.dto.CartVO;
import com.ezen.biz.dto.OrderVO;
import com.ezen.biz.mypage.CartService;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDAO oDao;
	@Autowired
	private CartService cartService;

	public int getMaxOseq() {
		return oDao.selectMaxOseq();
	}
	
	// 주문처리
	// - 주문 저장후 주문번호를 반환한다.
	public int insertOrder(OrderVO vo) {
		// (1) 주문번호를 할당받는다.
		int oseq = getMaxOseq();
		
		// (2) 위의 주문번호를 가지고 주문테이블에 저장
		vo.setOseq(oseq);	 // 주문번호 설정
		oDao.insertOrder(vo);	// 위의 설정한 내용으로 인해 주문번호와 ID가 할당된다.
		
		// (3) 장바구니 목록을 읽어서 주문상세내역 테이블에 저장
		//		- 장바구니 목록을 읽어온다.
		// 		- 주문상세 테이블 저장
		//		- 장바구니 테이블 업데이트(처리결과: '처리완료(2)'로 수정)
		List<CartVO> listCart = cartService.getListCart(vo.getId());
		
		for(CartVO cart : listCart) {
			OrderVO order = new OrderVO();
			
			order.setOseq(oseq);
			order.setPseq(cart.getPseq()); 	 // 장바구니의 상품번호를 저장
			order.setQuantity(cart.getQuantity()); 	// 장바구니 수량
			
			insertOrderDetail(order);
			
			// 장바구니 테이블 업데이트(처리결과를 '처리완료'로)
			cartService.updateCart(cart.getCseq());
		}
		return oseq;
	}
	
	// 주문 상세내역 저장
	public void insertOrderDetail(OrderVO vo) {
		
		oDao.insertOrderDetail(vo);
	}
	
	public List<OrderVO> getListOrderById(OrderVO vo) {
		return oDao.listOrderById(vo);
	}
	
	public List<Integer> getSeqOrdering(OrderVO vo) {
		return oDao.selectSeqOrdering(vo);
	}
}











