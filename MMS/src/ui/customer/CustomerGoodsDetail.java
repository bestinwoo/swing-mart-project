package ui.customer;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.*;
import javax.swing.text.*;
import db.*;
/** 상품 상세 다이얼로그 */
public class CustomerGoodsDetail extends JDialog implements KeyListener {
	private int width = 800;
	private int height = 500;
	private String title = "상품 상세";
	private int payment, count = 1;
	private Font font;
	private GoodsVO gv;
	private JLabel lblPayment;
	private JSpinner spinner;
	private DecimalFormat formatter;
	private JLabel lblparentCnt;

	public CustomerGoodsDetail(JFrame frame, GoodsVO gv, JLabel lblCnt) {
		super(frame, true);
		this.lblparentCnt = lblCnt;
		DB.init();
		setTitle(title);
		setSize(width, height);
		setLocationRelativeTo(this);
		setLayout(null);
		getContentPane().setBackground(new Color(251, 249, 241));
		font = new Font("나눔바른고딕", Font.PLAIN, 15);
		this.gv = gv;
		payment = gv.getPrice();
		setComponents();
		setVisible(true);
	}
	/** 레이아웃 설정 */
	private void setComponents() {
		// 상품 이미지
		ImageIcon icon = new ImageIcon("images/" + gv.getId() + ".jpg");
		JLabel lbl = new JLabel(icon);
		lbl.setBounds(50, 40, 300, 300);
		lbl.setBorder(new EtchedBorder());
		add(lbl);
		// 줄바꿈시 가운데 정렬 기능을 위해 JTextPane 사용
		JTextPane tpName = new JTextPane();
		tpName.setBounds(400, 20, 350, 70);
		tpName.setEditable(false);
		tpName.setText(gv.getGoodsName());
		tpName.setFont(new Font("나눔바른고딕", Font.PLAIN, 20));
		tpName.setBackground(new Color(251, 249, 241));

		// tpName의 styleDocument를 가져와 가운데 정렬 설정
		StyledDocument doc = tpName.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);

		JSeparator jsp = new JSeparator();
		jsp.setBounds(400, 100, 350, 1);
		jsp.setBackground(new Color(0x7d7e80));
		add(jsp);
		// 가격, 수량 1000단위 , 표시
		formatter = new DecimalFormat("###,###");

		JLabel lblPrice = new JLabel(formatter.format(gv.getPrice()) + "원");
		lblPrice.setFont(new Font("나눔바른고딕 Bold", Font.PLAIN, 23));
		lblPrice.setForeground(new Color(0xFF8C00));
		lblPrice.setBounds(400, 110, 200, 30);

		JSeparator jsp1 = new JSeparator();
		jsp1.setBounds(400, 150, 350, 1);
		jsp1.setBackground(new Color(0x7d7e80));
		add(jsp1);

		JLabel[] lblSubjects = new JLabel[3]; // 가격, 재고, 상품분류, 구매수량, 총 결제 금액
		String[] strSubjects = { "재고 : ", "상품 분류 : ", "구매 수량 :" };

		for (int i = 0; i < lblSubjects.length; i++) {
			lblSubjects[i] = new JLabel(strSubjects[i]);
			lblSubjects[i].setFont(new Font("나눔바른고딕", Font.BOLD, 16));
			lblSubjects[i].setBounds(400, 160 + (i * 40), 110, 30);
			add(lblSubjects[i]);
		}

		JLabel lblCount = new JLabel(formatter.format(gv.getCount()) + "개");
		lblCount.setBounds(445, 160, 80, 30);
		lblCount.setFont(font);
		JLabel lblCategory = new JLabel(gv.getCategory());
		lblCategory.setBounds(480, 200, 80, 30);
		lblCategory.setFont(font);
		
		// 구매 수량 JSpinner 설정
		SpinnerNumberModel model;
		if (gv.getCount() > 0)
			model = new SpinnerNumberModel(1, 1, gv.getCount(), 1);
		else
			model = new SpinnerNumberModel(0, 0, 0, 0);
		spinner = new JSpinner(model);
		// 재고 수량에 알맞는 유효한 값만 입력 가능
		JFormattedTextField txt = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();
		((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);
		spinner.setBounds(480, 243, 60, 25);
		spinner.addChangeListener(e -> {
			count = (int) spinner.getValue();
			calculatorPayment();
		});
		JTextField tfSpinner = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
		tfSpinner.addKeyListener(this);

		JSeparator jsp2 = new JSeparator();
		jsp2.setBounds(400, 290, 350, 1);
		jsp2.setBackground(new Color(0x7d7e80));
		add(jsp2);

		// 총 금액
		JLabel lblSub = new JLabel("총 금액 : ");
		lblSub.setBounds(400, 320, 80, 30);
		lblSub.setFont(lblSubjects[0].getFont());
		lblPayment = new JLabel();
		lblPayment.setText(formatter.format((int) spinner.getValue() * gv.getPrice()) + "원");
		lblPayment.setFont(lblPrice.getFont());
		lblPayment.setForeground(lblPrice.getForeground());
		lblPayment.setBounds(650, 320, 180, 30);

		// 구매 버튼
		CustomButton btnBuy = new CustomButton("구매");
		btnBuy.setBounds(410, 375, 300, 50);
		btnBuy.setContentAreaFilled(true);
		btnBuy.setForeground(Color.white);
		btnBuy.addActionListener(e -> insertOrder());

		add(btnBuy);
		add(lblSub);
		add(lblPayment);
		add(spinner);
		add(lblCategory);
		add(lblCount);
		add(lblPrice);
		add(tpName);
	}

	/**
	 * 구매 수량에 따라 가격 계산
	 */
	private void calculatorPayment() {
		int price = gv.getPrice();
		payment = count * price;
		lblPayment.setText(formatter.format(payment) + "원");
	}
	/** 구매시 주문 테이블에 Insert */
	private void insertOrder() {
		if (gv.getCount() > 0) { // 재고가 0이 아니고, 고객 계정으로 로그인 했을 경우만 DB Insert
			if (UserInfo.getInstance().validateUser()) {
				if (!UserInfo.getInstance().isAdmin) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					String today = format.format(new Date());

					String sql = "insert into orders(customerID, goodsID, amount, orderDate, count) values('"
							+ UserInfo.getInstance().userID + "', " + gv.getId() + ", " + payment + ", '" + today
							+ "', " + count + ")";
					try {
						DB.executeUpdate(sql);
						updateGoodsCount();
						DecimalFormat formatter = new DecimalFormat("###,###");
						lblparentCnt.setText("재고 : " + formatter.format(gv.getCount() - count) + "개");
						JOptionPane.showMessageDialog(null, "구매가 완료되었습니다.");
						this.dispose();
					} catch (SQLException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, "구매에 실패했습니다.");
					}
				} else
					JOptionPane.showMessageDialog(null, "고객 계정에서만 구매가 가능합니다.");
			} else
				JOptionPane.showMessageDialog(null, "로그인 후에 구매가 가능합니다.");
		} else
			JOptionPane.showMessageDialog(null, "품절된 상품입니다.");
	}

	private void updateGoodsCount() throws SQLException { // 구매 후 구매수량만큼 재고량에서 삭감
		String sql = "update goods set count = count - " + count + " where id = " + gv.getId();
		DB.executeUpdate(sql);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) { // 총 가격 자동 계산을 위한 텍스트필드 KeyAction
		int keyCode = e.getKeyCode();
		if ((keyCode > 47 && keyCode < 58) || (keyCode > 95 && keyCode < 106)) {
			// spinner의 value로 계산하면 ketReleased 호출될 때는 spinner 값이 업데이트가 안된 상태라서 textField를
			// 가져와서 계산함
			JTextField tfSpinner = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
			count = Integer.parseInt(tfSpinner.getText());
			calculatorPayment();
		}

	}

}
