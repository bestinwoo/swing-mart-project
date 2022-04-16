package ui.customer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import db.*;

public class CustomerCheck extends JFrame implements MouseListener{
	private JTable table;
	private DefaultTableModel model;
	private String[] colNames = {"주문날짜", "품목그림", "품목종류", "품목명", "수량", "결제금액"};
	private JPanel pan;
	private Font font;
	private ImageIcon imgbutton;
	private JButton btnPicture;
	private JTextField tfDate;
	private Font font1;
	private JTextField tfKinds;
	private JTextField tfname;
	private JTextField tfCount;
	private JTextField tfPrice;
	private JButton btnin;
	private JButton btnTrash;
	private JButton btnreset;
	private JButton btnFirst;
	private ImageIcon tableIcon;
	private ImageIcon iconinsert;
	
	public CustomerCheck(String title, int width, int height) {
		setTitle(title);
		setSize(width, height);
		setLocationRelativeTo(this);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		DB.init();
		Pan();
		SubPan();
		Table();
		getData();
		setVisible(true);
	}

	private void getData() {
		String sql = "select orders.*, goods.goodsName, goods.category from orders, goods"
				+ " where orders.goodsID = goods.id and"
				+ " customerID = '" + UserInfo.getInstance().userID + "'";
		ResultSet rs = DB.executeQuery(sql);
		DecimalFormat formatter = new DecimalFormat("###,###");
		
	
		
		int cnt = 0;
		try {
			while(rs.next()) {
				tableIcon = new ImageIcon("images/" + rs.getString("goodsID") + ".jpg");
				Image img = tableIcon.getImage().getScaledInstance(120, 100, Image.SCALE_SMOOTH);
				tableIcon.setImage(img);
				
				Vector<String> v = new Vector<>();
				v.add(rs.getString("orderDate"));
				v.add(rs.getString("goodsID"));
				v.add(rs.getString("category"));
				v.add(rs.getString("goodsName"));
				v.add(formatter.format(rs.getInt("count")) + "개");
				v.add(formatter.format(rs.getInt("amount")) + "원");
				
				model.addRow(v);
				model.setValueAt(tableIcon, cnt, 1);
				cnt++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void Table() {
		model = new DefaultTableModel(colNames, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false); // 컬럼 이동 불가
		table.getTableHeader().setResizingAllowed(false); // 컬럼 크기 변경 불가
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 한개의 행만 선택
		table.getTableHeader().setFont(new Font("나눔바른고딕", Font.BOLD, 13));
		table.setShowVerticalLines(false);
		table.getTableHeader().setBackground(new Color(0XFFFFFF)); // 헤더 배경색 
		table.getTableHeader().setForeground(new Color(0X2A6049)); //헤더 글자색
		table.setRowHeight(100);
		table.getColumn("품목그림").setPreferredWidth(65);
		table.getColumn("품목종류").setPreferredWidth(50);
		table.getColumn("주문날짜").setPreferredWidth(50);
		table.getColumn("수량").setPreferredWidth(50);
		table.getColumn("결제금액").setPreferredWidth(50);
		table.addMouseListener(this);
		
		// 필드 가운데 정렬
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer(); 
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		
		TableColumnModel tcm = table.getColumnModel();
		
		for(int i = 0; i < tcm.getColumnCount(); i++) {
			tcm.getColumn(i).setCellRenderer(tcr);
		}
		table.getColumnModel().getColumn(1).setCellRenderer(new ImageRender());
		table.getColumnModel().getColumn(3).setCellRenderer(new MyRender());
		// 데이터값을 오름차순, 내림차순 정렬
				table.setAutoCreateRowSorter(true);
				TableRowSorter<TableModel> tablesorter = new TableRowSorter<TableModel>(table.getModel());
				tablesorter.setSortable(0, false);
				tablesorter.setSortable(1, false);
				tablesorter.setSortable(4, false);
				tablesorter.setSortable(5, false);
				table.setRowSorter(tablesorter);
		
		JScrollPane sp = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setBounds(380, 30, 700, 500);
		sp.setBackground(Color.WHITE);
		sp.setForeground(new Color(0X2A6049));
		sp.getViewport().setBackground(Color.WHITE);
		
		pan.add(sp);
	}
	
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

	private void Pan() {
		pan = new JPanel();
		
		pan.setBackground(Color.WHITE);
		pan.setLayout(null);
		add(pan);
	}

	private void SubPan() {
		font = new Font("나눔바른고딕", Font.BOLD, 18);
		font1 = new Font("나눔바른고딕", Font.PLAIN, 15);
		
		JLabel lblDate = new JLabel("주문날짜", JLabel.CENTER);
		lblDate.setBounds(10, 30, 100, 30);
		lblDate.setForeground(new Color(0X2A6049));
		lblDate.setFont(font);
		
		pan.add(lblDate);
		
		tfDate = new JTextField();
		tfDate.setBounds(125, 30, 232, 32);
		tfDate.setFont(font1);
		tfDate.setEditable(false);
		tfDate.setBackground(Color.WHITE);
		
		pan.add(tfDate);
		
		JLabel lblPicture = new JLabel("품목그림", JLabel.CENTER);
		lblPicture.setBounds(10, 75, 100, 30);
		lblPicture.setForeground(new Color(0X2A6049));
		lblPicture.setFont(font);
		
		pan.add(lblPicture);
		
		btnPicture = new JButton();
		btnPicture.setBackground(Color.WHITE);
		btnPicture.setBounds(125, 80, 232, 130);
		
		pan.add(btnPicture);
		
		
		JLabel lblKinds = new JLabel("품목종류", JLabel.CENTER);
		lblKinds.setBounds(10, 230, 100, 30);
		lblKinds.setForeground(new Color(0X2A6049));
		lblKinds.setFont(font);
		
		pan.add(lblKinds);
		
		tfKinds = new JTextField();
		tfKinds.setBounds(125, 230, 232, 32);
		tfKinds.setFont(font1);
		tfKinds.setEditable(false);
		tfKinds.setBackground(Color.WHITE);
		
		pan.add(tfKinds);
		
		JLabel lblname = new JLabel("품목명", JLabel.CENTER);
		lblname.setBounds(10, 275, 100, 30);
		lblname.setForeground(new Color(0X2A6049));
		lblname.setFont(font);
		
		pan.add(lblname);
		
		tfname = new JTextField();
		tfname.setBounds(125, 275, 232, 32);
		tfname.setFont(font1);
		tfname.setEditable(false);
		tfname.setBackground(Color.WHITE);
		
		pan.add(tfname);
		
		JLabel lblCount = new JLabel("수 량", JLabel.CENTER);
		lblCount.setBounds(10, 320, 100, 30);
		lblCount.setForeground(new Color(0X2A6049));
		lblCount.setFont(font);
		
		pan.add(lblCount);
		
		tfCount = new JTextField();
		tfCount.setBounds(125, 320, 232, 32);
		tfCount.setFont(font1);
		tfCount.setEditable(false);
		tfCount.setBackground(Color.WHITE);
		
		pan.add(tfCount);
		
		JLabel lblPrice = new JLabel("결제금액", JLabel.CENTER);
		lblPrice.setBounds(10, 365, 100, 30);
		lblPrice.setForeground(new Color(0X2A6049));
		lblPrice.setFont(font);
		
		pan.add(lblPrice);
		
		tfPrice = new JTextField();
		tfPrice.setBounds(125, 365, 232, 32);
		tfPrice.setFont(font1);
		tfPrice.setEditable(false);
		tfPrice.setBackground(Color.WHITE);
		
		pan.add(tfPrice);
		
		ImageIcon icontrash = new ImageIcon("images/trash.png");
		btnTrash = new JButton(icontrash);
		btnTrash.setBounds(-5, 505, 100, 100);
		btnTrash.setBorderPainted(false);
		
		pan.add(btnTrash);
	}

	public static void main(String[] args) {
		new CustomerCheck("구매내역", 1100, 650);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = table.getSelectedRow();
		int col = table.getSelectedColumn();
		
		iconinsert = (ImageIcon)table.getValueAt(row, 1);
		Image img = iconinsert.getImage().getScaledInstance(232, 130, Image.SCALE_SMOOTH);
		iconinsert.setImage(img);
		
		btnPicture.setIcon(iconinsert);
		tfKinds.setText((String) table.getValueAt(row, 2));
		tfDate.setText((String) (table.getValueAt(row, 0)));
		tfname.setText((String) table.getValueAt(row, 3));
		tfCount.setText((String) (table.getValueAt(row, 4)));
		tfPrice.setText((String) table.getValueAt(row, 5));
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
