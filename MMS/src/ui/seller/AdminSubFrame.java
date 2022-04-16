package ui.seller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import db.DB;

public class AdminSubFrame extends JPanel implements ActionListener, MouseListener{
	private DefaultTableModel model;
	private JPanel pan;
	private Font font, fontSecond;
	private String[] colNames = {"품목그림", "품목종류", "품목번호", "품목명", "수 량", "가 격" };	
	private String[] kinds = {"", "신선식품", "가공식품", "건강식품", "커피/음료"};	
	private JTable table;
	private JFileChooser chooser;
	private String file, searchWord;
	private BufferedImage img;
	private ImageIcon tableIcon, iconInsert, imgButton;
	private JButton btnReset, btnFirst, btnPlus, btnIn, btnTrash, btnSelect;
	private JTextField tfName, tfCount, tfPrice, tfNum, tfSelect;
	private JComboBox<String> cbKinds;
	private File imageFile;

	public AdminSubFrame() {
		font = new Font("나눔바른고딕", Font.BOLD, 18);
		fontSecond = new Font("나눔바른고딕", Font.PLAIN, 15);
		setLayout(null);
		
		DB.init();
		Table();
		Pan();
		Chooser();
		getData();
	}
	
	private void Table() {
		model = new DefaultTableModel(colNames, 0) {
			public boolean isCellEditable(int row, int column) { // 테이블 데이터값 수정 불가
				return false;
			}
			
			public Class<?> getColumnClass(int column) { // 테이블 컬럼 값의 자료형을 변경
				switch (column) {
				case 2:
					return Integer.class;
				case 4:	
					return Integer.class;
				default:
					return String.class;
				}
			}
		};
		
		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false); // 컬럼 이동 불가
		table.getTableHeader().setResizingAllowed(false); // 컬럼 크기 변경 불가
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 한개의 행만 선택
		table.getTableHeader().setFont(new Font("나눔바른고딕", Font.BOLD, 13));
		table.setShowVerticalLines(false); // 테이블 수직 라인 제거
		table.getTableHeader().setBackground(new Color(0XFFFFFF)); // 헤더 배경색 
		table.getTableHeader().setForeground(new Color(0X2A6049)); // 헤더 글자색
		table.setRowHeight(100); // 테이블 행 높이 설정
		table.getColumn("품목그림").setPreferredWidth(65);
		table.getColumn("품목종류").setPreferredWidth(50);
		table.getColumn("품목번호").setPreferredWidth(50);
		table.getColumn("수 량").setPreferredWidth(50);
		table.getColumn("가 격").setPreferredWidth(50);
		table.addMouseListener(this);
		
		// 필드 가운데 정렬
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer(); 
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
				
		TableColumnModel tcm = table.getColumnModel();
				
		for(int i = 1; i < tcm.getColumnCount(); i++) {
			tcm.getColumn(i).setCellRenderer(tcr);
		}

		table.getColumnModel().getColumn(0).setCellRenderer(new ImageRender()); // 테이블 첫 번째 컬럼에 이미지를 넣기 위한 렌더링
		MyRender mr = new MyRender();
		table.getColumnModel().getColumn(3).setCellRenderer(mr); // 테이블 네 번째 컬럼에 JTextPane을 넣기 위한 렌더링
	
		JScrollPane sp = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setBounds(380, 30, 700, 500);
		sp.setBackground(Color.WHITE);
		sp.setForeground(new Color(0X2A6049));
		sp.getViewport().setBackground(Color.WHITE);
		
		add(sp);
	}
	
	private void Pan() {
		JLabel lblImg = new JLabel("품목그림", JLabel.CENTER);
		lblImg.setBounds(10, 25, 100, 30);
		lblImg.setForeground(new Color(0X2A6049));
		lblImg.setFont(font);
		
		imgButton = new ImageIcon("images/plus.png");
		btnPlus = new JButton(imgButton);
		btnPlus.setBackground(Color.WHITE);
		btnPlus.setBounds(125, 30, 232, 130);
		btnPlus.addActionListener(this);
		
		JLabel lblKinds = new JLabel("품목종류", JLabel.CENTER);
		lblKinds.setBounds(10, 185, 100, 30);
		lblKinds.setForeground(new Color(0X2A6049));
		lblKinds.setFont(font);
		
		cbKinds = new JComboBox<String>(kinds);
		cbKinds.setBounds(125, 185, 232, 32);
		cbKinds.setFont(fontSecond);
		cbKinds.setBackground(Color.WHITE);
		((JLabel)cbKinds.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER); // 콤보박스 데이터값 가운데로 정렬
		
		JLabel lblnum = new JLabel("품목번호", JLabel.CENTER);
		lblnum.setBounds(10, 230, 100, 30);
		lblnum.setForeground(new Color(0X2A6049));
		lblnum.setFont(font);
		
		tfNum = new JTextField();
		tfNum.setBounds(125, 230, 232, 32);
		tfNum.setFont(fontSecond);
		
		JLabel lblname = new JLabel("품목명", JLabel.CENTER);
		lblname.setBounds(10, 275, 100, 30);
		lblname.setForeground(new Color(0X2A6049));
		lblname.setFont(font);
		
		tfName = new JTextField();
		tfName.setBounds(125, 275, 232, 32);
		tfName.setFont(fontSecond);
		
		JLabel lblcount = new JLabel("수 량", JLabel.CENTER);
		lblcount.setBounds(10, 320, 100, 30);
		lblcount.setForeground(new Color(0X2A6049));
		lblcount.setFont(font);
		
		tfCount = new JTextField();
		tfCount.setBounds(125, 320, 232, 32);
		tfCount.setFont(fontSecond);
		
		JLabel lblprice = new JLabel("가 격", JLabel.CENTER);
		lblprice.setBounds(10, 365, 100, 30);
		lblprice.setForeground(new Color(0X2A6049));
		lblprice.setFont(font);
		
		tfPrice = new JTextField();
		tfPrice.setBounds(125, 365, 232, 32);
		tfPrice.setFont(fontSecond);
		
		btnIn = new JButton("입 고");
		btnIn.setBounds(257, 410, 100, 40);
		btnIn.setBackground(new Color(0X2A6049));
		btnIn.setForeground(Color.WHITE);
		btnIn.setFont(new Font("나눔바른고딕", Font.BOLD, 20));
		btnIn.addActionListener(this);
		
		ImageIcon icontrash = new ImageIcon("images/trash.png");
		btnTrash = new JButton(icontrash);
		btnTrash.setBounds(-5, 520, 100, 100);
		btnTrash.setBorderPainted(false); // 버튼의 테두리 설정
		btnTrash.addActionListener(this);
		
		JLabel lblselect = new JLabel("물품검색", JLabel.CENTER);
		lblselect.setBounds(465, 560, 120, 40);
		lblselect.setFont(new Font("나눔바른고딕", Font.BOLD, 20));
		lblselect.setOpaque(true);
		lblselect.setBackground(new Color(0X2A6049));
		lblselect.setForeground(Color.WHITE);
		
		tfSelect = new JTextField();
		tfSelect.setBounds(585, 561, 350, 40);
		tfSelect.setFont(fontSecond);
		
		ImageIcon icon2 = new ImageIcon("images/select.png");
		btnSelect = new JButton(icon2);
		btnSelect.setBounds(935, 561, 50, 39);
		btnSelect.setBorderPainted(false);
		btnSelect.setBackground(new Color(0X2A6049));
		btnSelect.addActionListener(this);
		
		ImageIcon iconReset = new ImageIcon("images/reset.png");
		btnReset = new JButton(iconReset);
		btnReset.setBounds(187, 410, 50, 40);
		btnReset.setBorderPainted(false);
		btnReset.setBackground(Color.WHITE);
		btnReset.addActionListener(this);
		
		btnFirst = new JButton(iconReset);
		btnFirst.setBounds(1000, 561, 50, 39);
		btnFirst.setBorderPainted(false);
		btnFirst.setBackground(Color.WHITE);
		btnFirst.addActionListener(this);
		
		add(lblImg);
		add(btnPlus);
		add(lblKinds);
		add(cbKinds);
		add(lblnum);
		add(tfNum);
		add(lblname);
		add(tfName);
		add(lblcount);
		add(tfCount);
		add(lblprice);
		add(tfPrice);
		add(btnIn);
		add(btnTrash);
		add(lblselect);
		add(tfSelect);
		add(btnSelect);
		add(btnReset);
		add(btnFirst);
	}
	
	// 파일 열기
	private void Chooser() {
		chooser = new JFileChooser();
		chooser.setDialogTitle("파일");
		chooser.setAcceptAllFileFilterUsed(true); // Filter 모든 파일 적용
			
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG 및 PNG 이미지", "jpg", "png"); // Filter 확장자 추가
		chooser.setFileFilter(filter);
	}
	
	// 입력된 데이터값 초기화
	private void clear() {
		btnPlus.setBackground(Color.WHITE);
		btnPlus.setIcon(imgButton);
		cbKinds.setSelectedIndex(0);
		tfNum.setText("");
		tfName.setText("");
		tfCount.setText("");
		tfPrice.setText("");
		table.clearSelection();
	}
	
	// 이미지를 넣기 위한 렌더링 클래스
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
	
	// JTextPane을 넣기 위한 렌더링 클래스
	private class MyRender extends DefaultTableCellRenderer {
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			 
			String item = value.toString();
			JTextPane ta = new JTextPane();
			ta.setText(item);
			ta.setEditable(false); // 텍스트 수정 불가
			
			// ta의 StyleDocument를 가져와 가운데 정렬 선정
			StyledDocument doc = ta.getStyledDocument();
			SimpleAttributeSet center = new SimpleAttributeSet();
			StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
			doc.setParagraphAttributes(0, doc.getLength(), center, false);
	
			return ta;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if(obj == btnPlus) {
			chooser.setCurrentDirectory(new java.io.File("/images")); // 현재 사용 디렉토리를 지정
			int returnVal = chooser.showOpenDialog(this); // 열기용 창 오픈
			
			if(returnVal == JFileChooser.APPROVE_OPTION) { // 열기 클릭
				file = chooser.getSelectedFile().getPath(); // 선택된 파일의 경로명 가져오기
				ImageIcon ic = new ImageIcon(file); // 선택된 파일을 ImageIcon에 저장
				imageFile = new File(file);
				
				Image img = ic.getImage().getScaledInstance(232, 130, Image.SCALE_SMOOTH);
				ic.setImage(img);
				
				btnPlus.setIcon(ic); 
			}
		} else if(obj == btnTrash) {  // 테이블에 선택된 행 삭제
			if (JOptionPane.showConfirmDialog(null, "선택한 품목을 삭제하시겠습니까?", "품목 삭제",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                int row = table.getSelectedRow();
                String sql = "delete from goods where id = " + table.getValueAt(row, 2);
                try {
                    DB.executeUpdate(sql);
                    clear(); // 삭제한 후 입력된 값을 초기화
                    getData();
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, "삭제를 실패했습니다. 품목번호를 확인해주세요");
                    e1.printStackTrace();
                }
            }
		} else if(obj == btnSelect) { // 검색
			searchWord = tfSelect.getText();
			getData();
		} else if(obj == btnFirst) {
			searchWord = null;
			tfSelect.setText("");
			getData();
		}
		
		if(obj == btnIn) {
			if(btnPlus.getIcon().equals(imgButton)) {
				JOptionPane.showMessageDialog(null, "그림을 등록해 주세요", "MESSAGE", JOptionPane.ERROR_MESSAGE);
			} else if(cbKinds.getSelectedItem().equals("")) { 
				JOptionPane.showMessageDialog(null, "품목종류를 입력해 주세요", "MESSAGE", JOptionPane.ERROR_MESSAGE);
			} else if(tfNum.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "품목번호를 입력해 주세요", "MESSAGE", JOptionPane.ERROR_MESSAGE);
			} else if(tfName.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "품목명을 입력해 주세요", "MESSAGE", JOptionPane.ERROR_MESSAGE);
			} else if(tfCount.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "수량을 입력해 주세요", "MESSAGE", JOptionPane.ERROR_MESSAGE);
			} else if(tfPrice.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "가격을 입력해 주세요", "MESSAGE", JOptionPane.ERROR_MESSAGE);
			}
			else {
				if(file != null) {
			try {
				Image orginalImage = ImageIO.read(imageFile); // 파일에서 이미지 불러오기
				Image resizeImage = orginalImage.getScaledInstance(300, 300, Image.SCALE_SMOOTH); // 이미지 사이즈 수정
				BufferedImage newImage = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB); // 결과물을 옮길 이미지 생성
				
				// 생성한 이미지에 크기 수정된 이미지 그리기
				Graphics g = newImage.getGraphics(); 
				g.drawImage(resizeImage, 0, 0, null);
				g.dispose();
				
				// 새로 생성한 이미지를 파일로 저장하기
				ImageIO.write(newImage, "jpg", new File("./images/" + tfNum.getText() + ".jpg"));
				System.out.println("성공");
				file = null;
			} catch (IOException e1) {
				e1.printStackTrace();
				}
			} else if(file == null){
				try {
					Image orginalImage = ImageIO.read(imageFile);
					Image resizeImage = orginalImage.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
					BufferedImage newImage = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
					
					Graphics g = newImage.getGraphics();
					g.drawImage(resizeImage, 0, 0, null);
					g.dispose();
					
					ImageIO.write(newImage, "jpg", new File("./images/" + tfNum.getText() + ".jpg"));
					System.out.println("성공");
				} catch (IOException e1) {
					e1.printStackTrace();
					}
				}
				int row = table.getSelectedRow();
				int price = Integer.parseInt(tfPrice.getText());
				
				String sql = "select id from goods where id = "
						+ tfNum.getText();
				ResultSet rs = DB.executeQuery(sql);
				
				try {
					if(rs.next()) {
						if(JOptionPane.showConfirmDialog(null, "이미 존재하는 품목입니다. 품목 정보를 갱신하시겠습니까?",
								"품목 업데이트", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
							sql = "update goods set goodsName = '"
								+ tfName.getText() + "', category = '"
								+ cbKinds.getSelectedItem() + "', count = "
								+ Integer.parseInt(tfCount.getText()) + ", price = "
								+ price
								+ " where id = "
								+ Integer.parseInt(tfNum.getText());
							DB.executeUpdate(sql);
							clear(); // 입력된 데이터값 초기화
							getData();
						}
					} else {
						sql = "insert into goods values("
								+ Integer.parseInt(tfNum.getText()) + ", '"
								+ tfName.getText() + "', "
								+ price + ", "
								+ Integer.parseInt(tfCount.getText()) + ", '"
								+ cbKinds.getSelectedItem() + "')";
						DB.executeUpdate(sql);
						clear();
						getData();
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		if(obj == btnReset) {
			clear();
		} 
	}
	
	private void getData() {
		model.setNumRows(0);
		String sql = "select * from goods"; 
		if(searchWord != null && !searchWord.isEmpty()) sql += " where goodsName like '%"
				+ this.searchWord + "%'";
		System.out.println(sql);
		ResultSet rs = DB.executeQuery(sql);
		DecimalFormat formatter = new DecimalFormat("###,###"); // 숫자의 Format을 변경
		int cnt = 0;
		
		try {
			while(rs.next()) {
				tableIcon = new ImageIcon("images/" + rs.getString("id") + ".jpg");
				Image img = tableIcon.getImage().getScaledInstance(120, 100, Image.SCALE_SMOOTH);
				tableIcon.setImage(img);
				
				Vector<String> v = new Vector<String>();
				v.add("");
				v.add(rs.getString("category"));
				v.add(rs.getString("id"));
				v.add(rs.getString("goodsName"));
				v.add(formatter.format(rs.getInt("count")) + "개");
				v.add(formatter.format(rs.getInt("price")) + "원");
				
				model.addRow(v);
				model.setValueAt(tableIcon, cnt, 0); // 원하는 위치의 데이터 변경
				cnt++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = table.getSelectedRow();
		int col = table.getSelectedColumn();
		
		iconInsert = new ImageIcon("images/" + table.getValueAt(row, 2) + ".jpg"); 
		Image img = iconInsert.getImage().getScaledInstance(232, 130, Image.SCALE_SMOOTH);
		iconInsert.setImage(img);

		imageFile = new File(iconInsert.toString()); // 테이블의 선택된 행의 값 가져오기
		btnPlus.setIcon(iconInsert);
		cbKinds.setSelectedItem((String) table.getValueAt(row, 1));
		tfNum.setText((String) table.getValueAt(row, 2));
		tfName.setText((String) table.getValueAt(row, 3));
		
		String str1 = (String) table.getValueAt(row, 4);
		String substr1 = str1.substring(0, str1.length() - 1);
		String restr1 = substr1.replace(",", "");
		tfCount.setText(restr1);

		String str2 = (String) table.getValueAt(row, 5);
		String substr2 = str2.substring(0, str2.length() - 1);
		String restr2 = substr2.replace(",", "");
		tfPrice.setText(restr2);
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
