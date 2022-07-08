package com.ezen.view.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.ezen.biz.admin.AdminService;
import com.ezen.biz.dto.AdminVO;
import com.ezen.biz.dto.ProductVO;
import com.ezen.biz.dto.SalesQuantity;
import com.ezen.biz.product.ProductService;

import utils.Criteria;
import utils.PageMaker;

@Controller
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/admin_login_form", method = RequestMethod.GET)
	public String adminLoginView() {

		return "admin/main";
	}

	@RequestMapping(value = "/admin_login", method = RequestMethod.POST)
	public String adminLogin(AdminVO vo, Model model) {

		// (1) 관리자 ID 인증
		int result = adminService.adminCheck(vo);

		// (2) 정상 관리자이면
		// - 관리자 정보 조회
		// - 상품목록 화면으로 이동 (redirect:admin_product_list)
		if (result == 1) {
			adminService.getAdmin(vo.getId());
			return "redirect:admin_product_list";
		} else {

			// (3) 비정상 관리자이면
			// - 메시지를 설정하고 로그인 페이지로 이동
			if (result == 0) {
				model.addAttribute("message", "비밀번호를 확인해 주세요.");
			} else {
				model.addAttribute("message", "아이디를 확인해 주세요.");
			}
			return "admin/main";
		}
	}

	@RequestMapping(value = "/admin_logout", method = RequestMethod.GET)
	public String adminLogout(SessionStatus status) {

		status.setComplete();

		return "admin/main";
	}

	/*
	 * 전체 상품 목록 조회 처리

	@RequestMapping(value = "/admin_product_list", method = RequestMethod.GET)
	public String adminProductList(Model model) {

		List<ProductVO> prodList = productService.getListProduct("");
		
		model.addAttribute("productList", prodList);
		model.addAttribute("productListSize", prodList.size());
		
		return "admin/product/productList";
	}
	*/
	
	/*
	 * 페이징 기능을 추가한 전체상품 목록 조회 처리
	 */
	@RequestMapping(value="/admin_product_list", method=RequestMethod.GET)
	public String adminProductList(@RequestParam(value="key", defaultValue="") String name,
								   @RequestParam(value="pageNum", defaultValue="1") int pageNum,
								   @RequestParam(value="rowsPerPage", defaultValue="10") int rowsPerPage,
								   Model model) {
		Criteria criteria = new Criteria();
		criteria.setPageNum(pageNum);
		criteria.setRowsPerPage(rowsPerPage);
		
		System.out.println("[adminProductList()] criteria="+criteria);
		// prodList - 각 페이지에 해당하는 게시물이 저장되어 있음.
		List<ProductVO> prodList = productService.getListProductWithPaging(criteria, name);
		
		// 화면에 표시할 페이지 버튼의 정보 설정(PageMaker 객체 이용)
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCriteria(criteria); // 현재 페이지와 페이지 당 항목 수 저장
		pageMaker.setTotalCount(productService.getCountProduct(name));	// 전체 항목 수를 조회하여 저장
		System.out.println("[adminProductList] pageMaker="+pageMaker);
		
		model.addAttribute("productList", prodList);
		model.addAttribute("productListSize", prodList.size());
		model.addAttribute("pageMaker", pageMaker);
		
		return "admin/product/productList";
	}
	
	/*
	 * 상품 등록페이지 표시
	 */
	@RequestMapping(value="/admin_product_write_form", method=RequestMethod.POST)
	public String adminProductWriteView(Model model) {
		String kindList[] = {"힐", "부츠", "샌들", "슬리퍼", "스니커즈"};
		
		model.addAttribute("kindList", kindList);
		
		return "admin/product/productWrite";
	}

	/*
	 * 상품 등록 처리
	 */
	@RequestMapping(value="/admin_product_write", method=RequestMethod.POST)
	public String adminProductWrite(@RequestParam(value="product_image") MultipartFile uploadFile,
									ProductVO vo, HttpSession session) {
		
		if(!uploadFile.isEmpty()) {
			String fileName = uploadFile.getOriginalFilename();
			vo.setImage(fileName);
			
			// 이미지 파일을 이동할 실제경로 구하기
			String image_path = session.getServletContext()
								.getRealPath("WEB-INF/resources/product_images/");
			try {
				uploadFile.transferTo(new File(image_path+fileName));
			} catch (IllegalStateException | IOException e) {
				
				e.printStackTrace();
			}
		}
		productService.insertProduct(vo);
		
		return "redirect:admin_product_list";
	}
	
	/*
	 * 상품 상세정보 출력
	 */
	@RequestMapping(value="/admin_product_detail")
	public String adminProductDetailAction(ProductVO vo, Criteria criteria, Model model) {
		String[] kindList = {"", "힐", "부츠", "샌들", "슬리퍼", "스니커즈", "세일"};
		
		ProductVO product = productService.getProduct(vo);
		
		model.addAttribute("productVO", product);

		int index = Integer.parseInt(product.getKind());
		model.addAttribute("kind", kindList[index]);
		model.addAttribute("criteria", criteria);
		
		return "admin/product/productDetail";
	}
	
	/*
	 * 상품 수정화면 출력
	 */
	@RequestMapping(value="/admin_product_update_form")
	public String adminProductUpdateView(ProductVO vo, Model model) {
		String[] kindList = {"힐", "부츠", "샌들", "슬리퍼", "스니커즈", "세일"};
		
		ProductVO product = productService.getProduct(vo);
		
		model.addAttribute("productVO", product);
		model.addAttribute("kindList", kindList);
		
		return "admin/product/productUpdate";
	}
	
	@RequestMapping(value="/admin_product_update", method=RequestMethod.POST)
	public String adminProductUpdateAction(@RequestParam(value="product_image") MultipartFile uploadFile,
										   @RequestParam(value="nonmakeImg") String org_image,
											ProductVO vo, HttpSession session) {
		
		if(!uploadFile.isEmpty()) {		// 상품 이미지를 수정한 경우
			String fileName = uploadFile.getOriginalFilename();
			vo.setImage(fileName);
			
			// 이미지 파일을 이동할 실제경로 구하기
			String image_path = session.getServletContext()
								.getRealPath("WEB-INF/resources/product_images/");
			try {
				uploadFile.transferTo(new File(image_path+fileName));
			} catch (IllegalStateException | IOException e) {
				
				e.printStackTrace();
			}
		} else {	// 상품 이미지를 수정하지 않은 경우
			vo.setImage(org_image);
		}
		
		if(vo.getUseyn() == null) {
			vo.setUseyn("n");
		} else {
			vo.setUseyn("y");
		}
		
		if(vo.getBestyn() == null) {
			vo.setBestyn("n");
		} else {
			vo.setBestyn("y");
		}
		
		productService.updateProduct(vo);
		
		return "redirect:admin_product_list";
	}
	
	/*
	 * 상품별 판매 실적 화면 출력
	 */
	@RequestMapping(value="admin_sales_record_form")
	public String adminProductSalesChart() {
		
		return "admin/order/salesRecords";
	}
	
	/*
	 * 상품별 판매 실적 조회 및 데이터 전송(JSON 포맷)
	 */
	@RequestMapping(value="sales_record_chart", produces="application/json; charset=UTF-8")
	@ResponseBody
	public List<SalesQuantity> salesRecordChart() {
		List<SalesQuantity> listSales = productService.getProductSales();
		
		return listSales;
	}
}













