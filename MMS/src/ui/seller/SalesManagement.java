package ui.seller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;


import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;


import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import db.DB;


public class SalesManagement extends JPanel implements MouseListener {
	private JTable table;
	private DefaultTableModel model;
	private JLabel image, kinds, num, name, sold, price, lresult;
	private JTextField tfimage, tfkinds, tfnum, tfname, tfsold, tfprice, tfresult;
	private JFileChooser chooser;
	private ImageIcon imgbutton;
	private JButton btnplus;
	private JButton btnPicture;
	private ImageIcon iconinsert;
	private ImageIcon tableIcon;
	
	public SalesManagement() {
		setLayout(null);
		Pan();
		DB.init();
	}
	
	private void Pan() {
	
		JPanel SMpan = new JPanel(null);
		SMpan.setBounds(0, 0, 1100, 800);
		SMpan.setBackground(Color.WHITE);
		add(SMpan);
		
		// 테이블
		String[] colNames = {"품목그림", "품목종류", "품목번호", "품목명", "판매량", "매출액" };
		
		model = new DefaultTableModel(colNames, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
			
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 2:
					return Integer.class;
				default:
					return String.class;
				}
			}
		};
		
		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false); // 컬럼 이동 불가
		table.getTableHeader().setResizingAllowed(false); // 컬럼 크기 변경 불가
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 행 하나만 선택
		table.setShowVerticalLines(false); // 수직 라인 안보이게 처리
		table.getTableHeader().setFont(new Font("나눔바른고딕", Font.BOLD, 13));
		table.getTableHeader().setBackground(new Color(0XFFFFFF)); // 헤더 배경색 
		table.getTableHeader().setForeground(new Color(0X2A6049)); //헤더 글자색
		//테이블 크기 지정
		table.setRowHeight(100);
		table.getColumn("품목그림").setPreferredWidth(65);
		table.getColumn("품목종류").setPreferredWidth(50);
		table.getColumn("품목번호").setPreferredWidth(50);
		table.getColumn("판매량").setPreferredWidth(50);
		table.getColumn("매출액").setPreferredWidth(50);
		table.addMouseListener(this);
		
		//테이블 내용 가운데 정렬
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcm = table.getColumnModel();
						
		for(int i = 0; i < 6; i++) {
			tcm.getColumn(i).setCellRenderer(dtcr);
		}
		
		table.getColumnModel().getColumn(0).setCellRenderer(new ImageRender());
		table.getColumnModel().getColumn(3).setCellRenderer(new MyRender());
				
		// ScrollPane
		JScrollPane sp = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setBounds(380, 30, 700, 500);
		sp.setBackground(Color.WHITE);
		add(sp);
		sp.setForeground(new Color(0X2A6049));
		sp.getViewport().setBackground(Color.WHITE);//테이블 백그라운드 배경색
		sp.setBackground(Color.WHITE);
		SMpan.add(sp);
		
		
		//label
		image = new JLabel("품목그림", JLabel.CENTER);
		image.setBounds(10, 30, 100, 30);
		image.setFont(new Font("나눔바른고딕", Font.BOLD, 18));
		image.setForeground(new Color(0X2A6049));
		SMpan.add(image);
		
		btnPicture = new JButton();
		btnPicture.setBackground(Color.WHITE);
		btnPicture.setBounds(125, 30, 232, 130);
		
		SMpan.add(btnPicture);
		
		kinds = new JLabel("품목종류", JLabel.CENTER); 
		kinds.setBounds(10, 200, 100, 30);// 크기 지정
		kinds.setFont(new Font("나눔바른고딕", Font.BOLD, 18)); //글씨
		kinds.setForeground(new Color(0X2A6049)); // 글씨 색
		
		tfkinds = new JTextField();
		tfkinds.setBounds(125, 200, 232, 32); // 크기 지정
		tfkinds.setFont(new Font("나눔바른고딕", Font.PLAIN, 15)); //글씨
		tfkinds.setBackground(Color.WHITE); //텍스트필트 색 지정
		tfkinds.setEditable(false); //텍스트필드 변경 불가
		SMpan.add(kinds);
		SMpan.add(tfkinds);
		
		num = new JLabel("품목번호", JLabel.CENTER);
		num.setBounds(10, 270, 100, 30);
		num.setFont(new Font("나눔바른고딕", Font.BOLD, 18));
		num.setForeground(new Color(0X2A6049));
		
		tfnum = new JTextField();
		tfnum.setBounds(125, 270, 232, 32);
		tfnum.setFont(new Font("나눔바른고딕", Font.PLAIN, 15));
		tfnum.setBackground(Color.WHITE);
		tfnum.setEditable(false);
		SMpan.add(num);
		SMpan.add(tfnum);
		
		name = new JLabel("품목명", JLabel.CENTER);
		name.setBounds(10, 340, 100, 30);
		name.setFont(new Font("나눔바른고딕", Font.BOLD, 18));
		name.setForeground(new Color(0X2A6049));
		
		tfname = new JTextField();
		tfname.setBounds(125, 340, 232, 32);
		tfname.setFont(new Font("나눔바른고딕", Font.PLAIN, 15));
		tfname.setBackground(Color.WHITE);
		tfname.setEditable(false);
		SMpan.add(name);
		SMpan.add(tfname);
		
		sold = new JLabel("판매량", JLabel.CENTER);
		sold.setBounds(10, 410, 100, 30);
		sold.setFont(new Font("나눔바른고딕", Font.BOLD, 18));
		sold.setForeground(new Color(0X2A6049));
		
		tfsold = new JTextField();
		tfsold.setBounds(125, 410, 232, 32);
		tfsold.setFont(new Font("나눔바른고딕", Font.PLAIN, 15));
		tfsold.setBackground(Color.WHITE);
		tfsold.setEditable(false);
		SMpan.add(sold);
		SMpan.add(tfsold);
		
		price = new JLabel("매출액", JLabel.CENTER);
		price.setBounds(10, 480, 100, 30);
		price.setFont(new Font("나눔바른고딕", Font.BOLD, 18));
		price.setForeground(new Color(0X2A6049));
		
		tfprice = new JTextField();
		tfprice.setBounds(125, 480, 232, 32);
		tfprice.setFont(new Font("나눔바른고딕", Font.PLAIN, 15));
		tfprice.setBackground(Color.WHITE);
		tfprice.setEditable(false);
		SMpan.add(price);
		SMpan.add(tfprice);
		
		//총 매출
		lresult = new JLabel("총 매출");
		lresult.setFont(new Font("나눔바른고딕", Font.BOLD, 20));
		lresult.setOpaque(true);
		lresult.setForeground(Color.WHITE);
		lresult.setBackground(new Color(0X2A6049));
		lresult.setBounds(480, 560, 120, 40);
		

		tfresult = new JTextField(JTextField.CENTER);
		tfresult.setBounds(600, 560, 350, 40);
		tfresult.setFont(new Font("나눔바른고딕", Font.BOLD, 15));
		tfresult.setEditable(false);
		tfresult.setBackground(Color.WHITE);

		
		SMpan.add(lresult);
		SMpan.add(tfresult);
		
		
	    getData();
	    
		setVisible(true);
	}
	
	//ImageRender class
	private class ImageRender extends DefaultTableCellRenderer {
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			 
			String photoname = value.toString();
			ImageIcon imageicon = new ImageIcon(new ImageIcon(photoname).getImage().getScaledInstance(120, 100, Image.SCALE_DEFAULT));
			JLabel lbl = new JLabel(imageicon);
			
			return lbl;
		}
	}
	
	//MyRender class
	private class MyRender extends DefaultTableCellRenderer {
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			 
			String item = value.toString();
			JTextPane ta = new JTextPane();
			ta.setText(item);
			
			ta.setEditable(false);
			
			// ta의 styleDocument를 가져와 가운데 정렬 선정
			StyledDocument doc = ta.getStyledDocument();
			SimpleAttributeSet center = new SimpleAttributeSet();
			StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
			doc.setParagraphAttributes(0, doc.getLength(), center, false);
	
			return ta;
		}

	}
	
	private void getData() {
		String sql = "select goods.*, sum(orders.count) saleCnt from goods, orders "
				+ "where goods.id = orders.GoodsID "
				+ "group by goods.id";
		ResultSet rs = DB.executeQuery(sql);
		DecimalFormat formatter = new DecimalFormat("###,###");

		
		int cnt = 0;
		try {
			while (rs.next()) {
				tableIcon = new ImageIcon("./images/" + rs.getString("id") + ".jpg");
				Image img = tableIcon.getImage().getScaledInstance(120, 100, Image.SCALE_SMOOTH);
				tableIcon.setImage(img);
				

				Vector<String> v = new Vector<>();
				int saleCnt = rs.getInt("saleCnt");
				v.add("그림넣을자리");
				v.add(rs.getString("category"));
				v.add(rs.getString("id"));
				v.add(rs.getString("goodsName"));
				v.add(formatter.format(saleCnt) + "개");
				v.add(formatter.format(rs.getInt("price") * saleCnt) + "원");
				
				model.addRow(v);
				model.setValueAt(tableIcon, cnt, 0);

				cnt++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		sql = "select sum(amount) total from orders";
		rs = DB.executeQuery(sql);
		
		try {
			if(rs.next()) {
				tfresult.setText(formatter.format(rs.getInt("total")) + "원");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		//테이블 클릭하면 사진과 함께 텍스트 필드로 값 이동
			int row = table.getSelectedRow();
			int col = table.getSelectedColumn();
			
			iconinsert = (ImageIcon)table.getValueAt(row, 0);
			Image img = iconinsert.getImage().getScaledInstance(232, 130, Image.SCALE_SMOOTH);
			iconinsert.setImage(img);
			
			btnPicture.setIcon(iconinsert);
	    	tfkinds.setText((String)table.getValueAt(row, 1));
	    	tfnum.setText((String)table.getValueAt(row, 2));
	    	tfname.setText((String)table.getValueAt(row, 3));
	    	tfsold.setText((String)table.getValueAt(row, 4));
	    	tfprice.setText((String)table.getValueAt(row, 5));
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}

