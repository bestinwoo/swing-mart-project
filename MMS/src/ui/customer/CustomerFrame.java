package ui.customer;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import db.*;
import ui.common.*;
import ui.seller.AdminFrame;

public class CustomerFrame extends JFrame implements ActionListener, MouseListener{
	private Font font;
	private Color baseColor;
	private String currentCategory = "신선식품";
	private ArrayList<GoodsVO> goodsList;
	private CustomerGoodsPanel[] arrPanGoods;
	private ArrayList<CustomButton> pageBtns;
	private int totalPageNum = 0, currentPage = 1;
	private String[] categoryNames = {"신선식품", "가공식품", "건강식품", "커피/음료"};
	private CustomButton[] categoryBtns;
	private CustomButton btnLogin, btnJoin;
	private boolean isLogin = false;
	private JTextField tfSearch;
	
	/** 로그인 여부를 설정하고 로그인 여부에 따라 버튼의 텍스트를 변경하는 메소드*/
	public void setLogin(boolean isLogin) {
		if(isLogin) {
			btnLogin.setText("로그아웃");
			if(UserInfo.getInstance().isAdmin) btnJoin.setText("판매자");
			else btnJoin.setText("구매내역");
		}
		else {
			btnLogin.setText("로그인");
			btnJoin.setText("회원가입");
			UserInfo.getInstance().clearUserInfo();
		}
		this.isLogin = isLogin;
	}

	public CustomerFrame(String title, int width, int height) {
		DB.init();
		setTitle(title);
		setSize(width, height);
		setLocationRelativeTo(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(null);
		setBackground(Color.white);
		font = new Font("나눔바른고딕", Font.PLAIN, 15);
		goodsList = new ArrayList<>();
		baseColor = new Color(0X2A6049);
		getContentPane().setBackground(new Color(251,249,241));
		setCenterGoodsPanel();
		setTopPanel();
		setBottomPanel();
		//카테고리와 페이지의 초기값인 
		getGoodsList(currentPage, currentCategory, null);
		
		setVisible(true);
	}
	
	/** 중앙 상품리스트 패널 초기 설정*/
	private void setCenterGoodsPanel() {
		JPanel panCenter = new JPanel(new GridLayout(3, 5, 10, 10));
		panCenter.setBounds(10, 200, 1200, 700);
		panCenter.setBackground(new Color(251,249,241));
		
		arrPanGoods = new CustomerGoodsPanel[15];
		for(int i = 0; i < 15; i++) {
			arrPanGoods[i] = new CustomerGoodsPanel();
			arrPanGoods[i].addMouseListener(this);
			panCenter.add(arrPanGoods[i]);
		}
		
		add(panCenter);
	}
	
	/** 상단 패널 레이아웃 설정 */
	private void setTopPanel() {
		JPanel panTop = new JPanel(null);
		panTop.setBounds(0, 0, 1230, 200);
		panTop.setBackground(new Color(251,249,241));
		
		ImageIcon martImg = new ImageIcon("images/mart2.png");
		
		JLabel lblImage = new JLabel(martImg);
		lblImage.setBounds(15, 10, 70, 76);
		
		JLabel lblTitle = new JLabel("골라JAVA");
		lblTitle.setBounds(100, 3, 200, 100);
		lblTitle.setFont(new Font("나눔바른고딕", Font.BOLD, 45));		
		lblTitle.setForeground(baseColor);
		
		tfSearch = new JTextField();
		tfSearch.setBounds(340, 85, 400, 30);
		
		CustomButton btnSearch = new CustomButton("검색");
		btnSearch.setBounds(740, 85, 70, 30);
		btnSearch.setContentAreaFilled(true);
		btnSearch.setForeground(Color.white);
		//검색 버튼 클릭시 검색어로 상품리스트 불러오고 카테고리 선택해제
		btnSearch.addActionListener(e -> {
			if(!tfSearch.getText().isEmpty()) {
				getGoodsList(1, null, tfSearch.getText());
				for(CustomButton btn : categoryBtns) {
					if(btn != null) btn.setContentAreaFilled(false);
				}
			}
		});
		
		btnLogin = new CustomButton("로그인");
		btnLogin.setBounds(800, 30, 90, 30);
		btnLogin.setContentAreaFilled(true);
		btnLogin.setForeground(Color.white);
		btnLogin.addActionListener(this);
		
		
		btnJoin = new CustomButton("회원가입");
		btnJoin.setBounds(900, 30, 90, 30);
		btnJoin.setContentAreaFilled(true);
		btnJoin.setForeground(Color.white);
		btnJoin.addActionListener(this);
		
		JPanel categoryLayer = new JPanel(new FlowLayout(FlowLayout.CENTER, 80, 0));
		categoryLayer.setBounds(0,150,1230,50);
		categoryLayer.setBackground(new Color(51,51,51));
		// 카테고리 버튼 생성 및 클릭시 해당 카테고리 상품 리스트 표시
		categoryBtns = new CustomButton[5];
		for(int i = 0; i < categoryNames.length; i++) {
			categoryBtns[i] = new CustomButton(categoryNames[i]);
			categoryBtns[i].setForeground(Color.white);
			categoryBtns[i].setFont(new Font("나눔바른고딕 Bold", Font.PLAIN, 15));
			categoryBtns[i].setPreferredSize(new Dimension(150, 50));
			categoryBtns[i].addActionListener(e -> {
				CustomButton btn = (CustomButton) e.getSource();
				tfSearch.setText("");
				btn.setContentAreaFilled(true);
				currentCategory = btn.getText();
				this.getGoodsList(1, currentCategory, null);
				
				for(int j = 0; j < categoryNames.length; j++) {
					if(categoryBtns[j].equals(btn)) categoryBtns[j].setContentAreaFilled(true);
					else categoryBtns[j].setContentAreaFilled(false);
				}
			});
			categoryLayer.add(categoryBtns[i]);
		}
		categoryBtns[0].setContentAreaFilled(true);
		
		panTop.add(btnSearch);
		panTop.add(tfSearch);
		panTop.add(categoryLayer);
		panTop.add(btnJoin);
		panTop.add(btnLogin);
		panTop.add(lblTitle);
		panTop.add(lblImage);
		
		add(panTop);
	}
	/** 하단 페이지 패널 레이아웃 */
	private void setBottomPanel() {
		JPanel panBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
		panBottom.setBounds(0, 900, 1235, 100);
		panBottom.setBackground(new Color(251,249,241));
		// 전체 페이지 개수
		totalPageNum = countPages("where category like '" + currentCategory + "'");
		System.out.println(totalPageNum);
		// 이전 버튼 선택 시 액션
		CustomButton btnPre = new CustomButton("이전");
		btnPre.addActionListener(e -> {
			if(currentPage > 5) {
				currentPage -= 5;
				getGoodsList(currentPage, currentCategory, tfSearch.getText());
			}
		});
		panBottom.add(btnPre);
		// 남은 페이지와 표시할 페이지 수 계산
		int remainPage = totalPageNum - currentPage;
		int showPageCnt = remainPage >= 5 ? 5 : remainPage;
		//계산된 페이지 수만큼 페이지 버튼 add
		pageBtns = new ArrayList<>();
		int index = 0;
		for(int i = currentPage; i < currentPage + showPageCnt; i++) {
			pageBtns.add(new CustomButton(Integer.toString(i)));
			pageBtns.get(index).addActionListener(this);
			panBottom.add(pageBtns.get(index));
			index++;
		}
		//초기 1페이지 선택
		fillPageBtn(0);
		//다음 버튼 선택시 액션
		CustomButton nextBtn = new CustomButton("다음");
		nextBtn.addActionListener(e -> {
			if(Integer.parseInt(pageBtns.get(0).getText()) + 5 < totalPageNum) {
				currentPage += 5;
				if(currentPage > totalPageNum) currentPage = totalPageNum;
				getGoodsList(currentPage, currentCategory, tfSearch.getText());
			}
		});
		panBottom.add(nextBtn);
		
		add(panBottom);
	}
	
	/**
	 * @param conditional SQL 조건절
	 * @return 전체 페이지 수 계산
	 */
	private int countPages(String conditional) {
		String sql = "select count(*) from goods " + conditional;
		ResultSet rs = DB.executeQuery(sql);
		try {
			if(rs.next()) {
				return (int) Math.ceil(rs.getDouble(1) / 15.0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	/** 표시할 페이지 리스트 계산 */
	private void loadPageList(String conditional) {
		// 우선 전체 페이지 수 계산
		totalPageNum = countPages(conditional);
		// 표시할 페이지만 visible 하기 위해 처음에 모두 invisible
		for(CustomButton b : pageBtns) b.setVisible(false);
		int lastPageNum = 0; // 마지막 페이지 Num
		int firstPageNum = 0; // 첫번 째 페이지 Num
		int pagesPerBlock = 5; // 한번에 표시할 페이지 개수
		
		// 전체 페이지 수에서 한번에 표시할 페이지를 나눈 나머지를 저장
		int mod = totalPageNum % pagesPerBlock;
		// 전체 페이지에서 mod 를 뺀 값이 현재 페이지보다 크거나 같을 경우에 페이지 계산
		if(totalPageNum - mod >= currentPage) {
			 lastPageNum = (int) (Math.ceil((float)currentPage / pagesPerBlock) * pagesPerBlock); // 마지막 페이지 계산
			 firstPageNum = lastPageNum - (pagesPerBlock - 1); // 첫번째 페이지 계산
         } else { // 현재 페이지가 전체 페이지에서 mod를 뺀 값보다 클 경우 마지막 페이지를 전체 페이지의 끝으로 설정
        	 lastPageNum = totalPageNum;
        	 firstPageNum = (int) (Math.ceil((float)currentPage / pagesPerBlock) * pagesPerBlock)
                     - (pagesPerBlock - 1);
         }
		
		int index = 0;
		// 첫번째 페이지부터 마지막 페이지까지 pageBtns에 설정
		for(int i = firstPageNum; i <= lastPageNum; i++) {
			pageBtns.get(index).setText(Integer.toString(i));
			pageBtns.get(index).setVisible(true);
			if(i == currentPage) fillPageBtn(index); // 현재 선택된 페이지 색칠
			index++;
		}
	}
	/** 상품 리스트 DB에서 가져오기*/
	public void getGoodsList(int page, String categoryName, String searchName) {
		currentCategory = categoryName;
		currentPage = page;
		goodsList.clear();
		// 상품 리스트는 한번에 15개씩만 가져옴 (한페이지에 15개 표시)
		int limit = (page - 1) != 0 ? (page - 1) * 15 : 0;
		String sql = "select * from goods";
		String conditional = ""; // 카테고리나 검색이 설정된 경우 조건절 추가
		if(searchName == null || searchName.isEmpty()) conditional += " where category like '" + categoryName + "' ";
		else conditional += " where goodsName like '%" + searchName + "%' ";
		sql += conditional + "limit " + limit + ", 15";
		
		ResultSet rs = DB.executeQuery(sql);
		try {
			while(rs.next()) {
				int id = rs.getInt("id");
				int price = rs.getInt("price");
				int count = rs.getInt("count");
				String name = rs.getString("goodsName");
				String category = rs.getString("category");
				// 상품리스트에 상품 정보 add
				goodsList.add(new GoodsVO(id,name,price,count,category));
			}
			
			for(int i = 0; i < 15; i++) {
				if(goodsList.size() > i) { // 상품 목록 리스트 레이아웃 업데이트
					arrPanGoods[i].updateGoodsPanel(goodsList.get(i));
					//상품 정보 업데이트 후 밑에 두개의 메소드를 호출해줘야 패널이 다시 그려짐
					arrPanGoods[i].revalidate();
					arrPanGoods[i].repaint();
					arrPanGoods[i].setVisible(true);
				}
				else arrPanGoods[i].setVisible(false);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		loadPageList(conditional);
	}
	/** 선택된 페이지 색칠 */
	private void fillPageBtn(int index) {
		for(CustomButton btn : pageBtns) {
			btn.setContentAreaFilled(false);
		}
		pageBtns.get(index).setContentAreaFilled(true);
		
	}
	
	public static void main(String[] args) {
		new CustomerFrame("고객페이지",1235,1000);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		//페이지 버튼 선택시 페이지 이동
		if(pageBtns.contains(obj)) {
			CustomButton btn = (CustomButton) obj;
			int movePage = Integer.parseInt(btn.getText());
			getGoodsList(movePage, currentCategory, tfSearch.getText());
		} else if(obj.equals(btnLogin)) { // 로그인, 로그아웃 액션
			// 로그인 정보 있을 때 (로그아웃 처리)
			if(UserInfo.getInstance().validateUser()) {
				setLogin(false);
				btnLogin.setText("로그인");
			} else {
				//로그인 정보 없을 때 (로그인)
				new LoginFrame("로그인", 500, 450, this);
			}
		} else if(obj.equals(btnJoin)) { // 회원가입, 구매내역, 판매자 페이지 설정
			if(btnJoin.getText().equals("회원가입")) new JoinFrame("회원가입", 550, 700);
			else if(btnJoin.getText().equals("판매자")) new AdminFrame("판매자 페이지", 1100, 700);
			else if(btnJoin.getText().equals("구매내역")) new CustomerCheck("구매내역", 1100, 650);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Object obj = e.getSource();
		// 상품 선택시 상품 상세 페이지 띄우기
		if(obj.getClass().equals(CustomerGoodsPanel.class)) {
			CustomerGoodsPanel pan = (CustomerGoodsPanel) obj;
			new CustomerGoodsDetail(this, pan.getGoods(), pan.getLblCount());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}

//테두리 없고 깔끔한 버튼을 CustomButton class로 따로 정의
class CustomButton extends JButton {
	public CustomButton(String text) {
		setText(text);
		setBackground(new Color(0X2A6049));
		setContentAreaFilled(false);
		setFocusPainted(false);
		setBorderPainted(false);
		setFont(new Font("나눔바른고딕", Font.PLAIN, 15));
	}
}
