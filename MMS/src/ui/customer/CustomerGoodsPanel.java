package ui.customer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import db.GoodsVO;

public class CustomerGoodsPanel extends JPanel {
	private GoodsVO goods;
	private JLabel lblCount;
	
	public JLabel getLblCount() {
		return lblCount;
	}

	public CustomerGoodsPanel() {
		setLayout(null);
		this.setBorder(new EtchedBorder());
		setBackground(Color.white);
	}
	
	public CustomerGoodsPanel(GoodsVO goods) {
		this.goods = goods;
		setLayout(null);
		setBackground(Color.white);
		this.setBorder(new EtchedBorder());
		updateGoodsPanel(goods);
	}
	/** 패널 레이아웃 업데이트 */
	public void updateGoodsPanel(GoodsVO goods) {
		removeAll();
		this.goods = goods;
		ImageIcon icon = new ImageIcon("images/" + goods.getId() + ".jpg");
		// ImageIcon에서 image를 꺼내와서 label 크기에 맞게 사이즈 조절
		Image img = icon.getImage().getScaledInstance(232, 130, Image.SCALE_SMOOTH);
		icon.setImage(img);
		
		JLabel lblImg = new JLabel(icon);
		lblImg.setBounds(0, 0, 232, 130);
		//label에 테두리 추가
		lblImg.setBorder(new EtchedBorder());
		
		//줄바꿈시 가운데 정렬 기능을 위해 JTextPane 사용
		JTextPane tpName = new JTextPane();
		tpName.setBounds(5, 135, 220, 40);
		tpName.setEditable(false);
		tpName.setText(goods.getGoodsName());
		tpName.setFont(new Font("나눔바른고딕 Bold", Font.PLAIN, 15));
		
		//tpName.setForeground(Color.black);
	
		
		//tpName의 styleDocument를 가져와 가운데 정렬 설정
		StyledDocument doc = tpName.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		StyleConstants.setBold(center, true);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		DecimalFormat formatter = new DecimalFormat("###,###");
		
		Font font = new Font("나눔바른고딕", Font.PLAIN, 15);
		lblCount = new JLabel("재고 : " + formatter.format(goods.getCount()) + "개");
		lblCount.setBounds(5, 180, 220, 20);
		lblCount.setHorizontalAlignment(JLabel.CENTER);
		lblCount.setFont(font);
		
		JLabel lblPrice = new JLabel("가격 : " + formatter.format(goods.getPrice()) + "원");
		lblPrice.setBounds(5, 200, 220, 20);
		lblPrice.setHorizontalAlignment(JLabel.CENTER);
		lblPrice.setFont(font);
		
		add(lblPrice);
		add(lblCount);
		add(tpName);
		add(lblImg);
	}

	public GoodsVO getGoods() {
		return goods;
	}
}
