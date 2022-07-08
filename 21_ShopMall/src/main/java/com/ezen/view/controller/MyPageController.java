package com.ezen.view.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ezen.biz.dto.CartVO;
import com.ezen.biz.dto.MemberVO;
import com.ezen.biz.dto.OrderVO;
import com.ezen.biz.mypage.CartService;
import com.ezen.biz.order.OrderService;

@Controller
public class MyPageController {

	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	
	/*
	 * 장바구니 담기 요청 처리
	 */
	@RequestMapping(value="/cart_insert", method=RequestMethod.POST)
	public String insertCart(CartVO vo, HttpSession session) {
		
		// 세션에 사용자 정보가 저장되어 있는지 확인(로그인 여부 확인)
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
		
		if(loginUser == null) {
			
			return "member/login";
		} else {
			
			vo.setId(loginUser.getId());
			
			cartService.insertCart(vo);
			
			// 장바구니 목록 확인
			return "redirect:cart_list";
		}
	}
	
	// 장바구니 목록 조회
	@RequestMapping(value="/cart_list", method=RequestMethod.GET)
	public String listCart(HttpSession session, Model model) {
		// 세션에 사용자 정보가 저장되어 있는지 확인(로그인 여부 확인)
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
				
		if(loginUser == null) {
					
			return "member/login";
		} else {
			List<CartVO> cartList = cartService.getListCart(loginUser.getId());
			
			// 장바구니 총액 계산
			int totalAmount = 0;
			for (CartVO vo : cartList) {
				totalAmount += (vo.getQuantity() * vo.getPrice2());
			}
			
			model.addAttribute("cartList", cartList);
			model.addAttribute("totalPrice", totalAmount);
			
			return "mypage/cartList";
		}
	}
	
	/*
	 * 장바구니 항목 삭제요청 처리
	 */
	@RequestMapping(value="/cart_delete", method=RequestMethod.POST)
	public String cartDelete(@RequestParam(value="cseq") int[] cseq) {
		
		for(int i=0; i<cseq.length; i++) {
			cartService.deleteCart(cseq[i]);
		}
		return "redirect:cart_list";
	}	
	
	/*
	 * 장바구니 내역의 주문 처리
	 */
	@RequestMapping(value="/order_insert", method=RequestMethod.POST)
	public String orderInsert(OrderVO vo, HttpSession session, Model model) {
		// 세션에 사용자 정보가 저장되어 있는지 확인(로그인 여부 확인)
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
				
		if(loginUser == null) {
					
			return "member/login";
		} else {
			vo.setId(loginUser.getId());
			
			int oseq = orderService.insertOrder(vo);
			
			model.addAttribute("oseq", oseq);
			
			return "redirect:order_list";
		}
	}
	
	// 진행중인 주문내역 조회
	@RequestMapping(value="/order_list")
	public String orderListAction(@RequestParam(value="oseq") int oseq,
									HttpSession session, OrderVO order, Model model) {
		// 세션에 사용자 정보가 저장되어 있는지 확인(로그인 여부 확인)
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
				
		if(loginUser == null) {
							
			return "member/login";
		} else {
			// 진행중인 주문 내역 조회
			order.setId(loginUser.getId());
			order.setOseq(oseq);
			order.setResult("1");
			List<OrderVO> orderList = orderService.getListOrderById(order);
			
			// 주문총액 계산
			int totalAmount = 0;
			for (OrderVO vo : orderList) {
				totalAmount += (vo.getQuantity() * vo.getPrice2());
			}
			model.addAttribute("orderList", orderList);
			model.addAttribute("totalPrice", totalAmount);	
			
			return "mypage/orderList";
		}
	}
	
	/*
	 * 진행중인 모든 주문 내역의 요약정보 조회
	 */
	@RequestMapping(value="/mypage")
	public String myPageView(HttpSession session, OrderVO vo, Model model) {
		// 세션에 사용자 정보가 저장되어 있는지 확인(로그인 여부 확인)
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
				
		if(loginUser == null) {
							
			return "member/login";
		} else {
			// (1) 사용자가 주문한 모든 주문번호 조회
			vo.setId(loginUser.getId());
			vo.setResult("1");
			List<Integer> oseqList = orderService.getSeqOrdering(vo);
			
			// (2) 각 주문번호에 대해 주문정보를 조회하고 요약정보 생성
			List<OrderVO> summaryList = new ArrayList<OrderVO>(); 	// 주문 요약정보 목록 저장
			for(int oseq : oseqList) {
				OrderVO order = new OrderVO();
				
				// 각 주문에 대한 주문내역 조회
				order.setId(loginUser.getId());
				order.setOseq(oseq);
				order.setResult("1");
				
				List<OrderVO> listByOseq = orderService.getListOrderById(order);
				
				// 각 주문 요약정보 생성
				OrderVO summary = new OrderVO();
				summary.setOseq(listByOseq.get(0).getOseq());
				summary.setIndate(listByOseq.get(0).getIndate());
				
				if(listByOseq.size() > 1) {
					summary.setPname(listByOseq.get(0).getPname() + " 외" + 
							(listByOseq.size() - 1)+"건");
				} else {
					summary.setPname(listByOseq.get(0).getPname());
				}
				
				int amount = 0;
				for(int i=0; i<listByOseq.size(); i++) {
					amount += listByOseq.get(i).getQuantity() * 
							  listByOseq.get(i).getPrice2();
				}
				summary.setPrice2(amount);
				
				// 요약정보를 리스트에 추가
				summaryList.add(summary);
			}
			
			model.addAttribute("title", "진행중인 주문 내역");
			model.addAttribute("orderList", summaryList);
		}
		return "mypage/mypage";
	}
	
	/*
	 * 주문 상세 정보조회 처리
	 */
	@RequestMapping(value="/order_detail", method=RequestMethod.GET)
	public String orderDetail(HttpSession session, OrderVO vo, Model model) {
		// 세션에 사용자 정보가 저장되어 있는지 확인(로그인 여부 확인)
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
				
		if(loginUser == null) {
									
			return "member/login";
		} else {
			vo.setId(loginUser.getId());
			vo.setResult("");
			
			List<OrderVO> orderList = orderService.getListOrderById(vo);
			
			// 화면에 출력할 정보 생성
			// (1) 주문자 정보 생성
			OrderVO orderDetail = new OrderVO();
			orderDetail.setOseq(orderList.get(0).getOseq());	 // 주문번호 저장
			orderDetail.setIndate(orderList.get(0).getIndate()); // 주문일자 저장
			orderDetail.setMname(orderList.get(0).getMname());	 // 주문자명 저장
			// 주문 총 금액 계산
			int totalAmount=0;
			for (int i=0; i<orderList.size(); i++) {
				totalAmount += (orderList.get(i).getQuantity() * orderList.get(i).getPrice2());
			}
			
			// (2) 화면에 출력할 정보 저장
			model.addAttribute("title", "Mypage 주문 상세 정보");
			model.addAttribute("orderDetail", orderDetail);
			model.addAttribute("totalPrice", totalAmount);
			model.addAttribute("orderList", orderList);
			
			return "mypage/orderDetail";
		}
	}
	
	// 총 주문내역
	@RequestMapping(value="order_all", method=RequestMethod.GET)
	public String orderAllView(HttpSession session, OrderVO vo, Model model) {
		// 세션에 사용자 정보가 저장되어 있는지 확인(로그인 여부 확인)
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
				
		if(loginUser == null) {
									
			return "member/login";
		} else {
			// (1) 사용자가 주문한 모든 주문번호 조회
			vo.setId(loginUser.getId());
			vo.setResult(""); 	// 처리결과 지정 안함
			List<Integer> oseqList = orderService.getSeqOrdering(vo);
			
			// (2) 각 주문번호에 대해 주문정보를 조회하고 요약정보 생성
			List<OrderVO> summaryList = new ArrayList<OrderVO>(); 	// 주문 요약정보 목록 저장
			for(int oseq : oseqList) {
				OrderVO order = new OrderVO();
				order.setId(loginUser.getId());
				order.setOseq(oseq);
				order.setResult("");
						
				List<OrderVO> listByOseq = orderService.getListOrderById(order);
				
				// 각 주문 요약정보 생성
				OrderVO summary = new OrderVO();
				summary = listByOseq.get(0);	// 우선 첛번째 상품내역 정보를 복사
				
				if(listByOseq.size() > 1) {
					summary.setPname(listByOseq.get(0).getPname() + " 외" + 
							(listByOseq.size() - 1)+"건");
				} else {
					summary.setPname(listByOseq.get(0).getPname());
				}
				
				int amount = 0;
				for(int i=0; i<listByOseq.size(); i++) {
					amount += listByOseq.get(i).getQuantity() * 
							  listByOseq.get(i).getPrice2();
				}
				summary.setPrice2(amount);
				
				// 요약정보를 리스트에 추가
				summaryList.add(summary);
		}
			model.addAttribute("title", "총 주문내역");
			model.addAttribute("orderList", summaryList);
	}
		return "mypage/mypage";
	}
}



















