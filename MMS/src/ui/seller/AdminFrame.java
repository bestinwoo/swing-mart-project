package ui.seller;

import java.awt.*;
import java.awt.event.*;
import java.net.http.WebSocket.Listener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.*;
import db.DB;

public class AdminFrame extends JFrame implements ActionListener, MouseListener {
	private DefaultTableModel model;
	private JTable table;

	private JButton wd, bsearch;
	private JTextField tfname, tfid, tfday, tfadress, tfsearch;
	private JLabel lblImg, lblTitle, name, id, day, adress, lsearch;
	private String searchWord;
	public AdminSubFrame AdminFrame = null;
	public SalesManagement SalesManagement = null;

	public AdminFrame(String title, int width, int height) {
		setTitle(title);
		setSize(width, height);
		setLocationRelativeTo(this);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// 두 번째 탭
		AdminFrame = new AdminSubFrame();
		AdminFrame.setBackground(Color.WHITE);
		
		//세 번쨰 탭
		SalesManagement = new SalesManagement();
		SalesManagement.setBackground(Color.WHITE);
		
		// 테이블
		String[] colNames = {"아이디", "회원 이름", "가입일", "주소", "선택" };

		model = new DefaultTableModel(colNames, 0) {
			// 체크박스 column을 제외한 나머지 cell 수정 불가
			public boolean isCellEditable(int row, int column) {
				if (column == 4)
					return true;
				else
					return false;
			}
			
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 4:
					return Boolean.class;
					
				default:
					return String.class;
				}
			}

		};
		
		setLayout(null);
		

		JPanel TabPan = new JPanel(null);
		TabPan.setBounds(0, 0, 1100, 800);
		TabPan.setBackground(Color.WHITE);
		add(TabPan);
		
		
		JPanel Apan = new JPanel(null);
		Apan.setBounds(0, 0, 1100, 800);
		Apan.setBackground(Color.WHITE);
		add(Apan);

		
		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false); // 컬럼 이동 불가
		TableRowSorter<TableModel> tablesorter = new TableRowSorter<TableModel>(table.getModel()); // 정렬기능
		table.setRowSorter(tablesorter);
		table.setShowVerticalLines(false); // 수직 라인 안보이게 처리
		table.getTableHeader().setFont(new Font("나눔바른고딕", Font.BOLD, 13));
		table.getTableHeader().setBackground(new Color(0XFFFFFF)); // 헤더 배경색 
		table.getTableHeader().setForeground(new Color(0X2A6049)); //헤더 글자색
		
		//테이블 이벤트
		table.addMouseListener(this);
		
		//테이블 내용 가운데 정렬
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcm = table.getColumnModel();
				
		for(int i = 0; i < 4; i++) {
			tcm.getColumn(i).setCellRenderer(dtcr);
		}
		
		table.getColumn("주소").setPreferredWidth(200);
		table.getColumn("선택").setPreferredWidth(5);
		

		// ScrollPane
		JScrollPane sp = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setBounds(380, 30, 700, 500);
		sp.setBackground(Color.WHITE);
		add(sp);
		sp.setForeground(new Color(0X2A6049));
		sp.getViewport().setBackground(Color.WHITE);//테이블 백그라운드 배경색

		Apan.add(sp);
		
		//label
		
		ImageIcon icon = new ImageIcon("images/mart.png");
		lblImg = new JLabel(icon);
		lblImg.setBounds(70, 30, 70, 70);
		Apan.add(lblImg);
		
		lblTitle = new JLabel("골라JAVA");
		lblTitle.setBounds(150, 40, 200, 50);
		lblTitle.setFont(new Font("나눔바른고딕", Font.BOLD, 35));
		lblTitle.setForeground(new Color(0X2A6049));
		Apan.add(lblTitle);
		
		name = new JLabel(" 이름 ", JLabel.CENTER);
		name.setFont(new Font("나눔바른고딕", Font.BOLD, 18)); //글씨
		name.setForeground(new Color(0X2A6049)); //글씨 색
		name.setBounds(15, 120, 100, 30); //크기 지정
		
		tfname = new JTextField();
		tfname.setBounds(125, 120, 232, 32); //크기 지정
		tfname.setFont(new Font("나눔바른고딕", Font.PLAIN, 15)); //글씨
		tfname.setBackground(Color.WHITE); //텍스트필드 색 지정
		tfname.setEditable(false); //텍스트필드 변경 불가
		Apan.add(name);
		Apan.add(tfname);
		
		id = new JLabel(" 아이디 ", JLabel.CENTER);
		id.setFont(new Font("나눔바른고딕", Font.BOLD, 18));
		id.setForeground(new Color(0X2A6049));
		id.setBounds(15, 210, 100, 30);
		
		tfid = new JTextField();
		tfid.setBounds(125, 210, 232, 32);
		tfid.setFont(new Font("나눔바른고딕", Font.PLAIN, 15));
		tfid.setBackground(Color.WHITE);
		tfid.setEditable(false);
		Apan.add(id);
		Apan.add(tfid);
		
		day = new JLabel(" 가입일 ", JLabel.CENTER);
		day.setFont(new Font("나눔바른고딕", Font.BOLD, 18));
		day.setForeground(new Color(0X2A6049));
		day.setBounds(15, 300, 100, 30);
		
		tfday = new JTextField();
		tfday.setBounds(125, 300, 232, 32);
		tfday.setFont(new Font("나눔바른고딕", Font.PLAIN, 15));
		tfday.setBackground(Color.WHITE);
		tfday.setEditable(false);
		Apan.add(day);
		Apan.add(tfday);
		
		adress = new JLabel(" 주소 ", JLabel.CENTER);
		adress.setFont(new Font("나눔바른고딕", Font.BOLD, 18));
		adress.setForeground(new Color(0X2A6049));
		adress.setBounds(15, 390, 100, 30);
		
		tfadress = new JTextField();
		tfadress.setBounds(125, 390, 232, 32);
		tfadress.setFont(new Font("나눔바른고딕", Font.PLAIN, 15));
		tfadress.setBackground(Color.WHITE);
		tfadress.setEditable(false);
		Apan.add(adress);
		Apan.add(tfadress);
		
		//탈퇴
		wd = new JButton("탈  퇴");
		wd.setFont(new Font("나눔바른고딕", Font.BOLD, 20));
		wd.setForeground(new Color(0XFFFFFF));
		wd.setBackground(new Color(0X2A6049));
		wd.setBounds(145, 480, 100, 40);
		Apan.add(wd);
		wd.addActionListener(this);
		wd.addMouseListener(this);
		
		//검색
		lsearch = new JLabel(" 이름검색 ", JLabel.CENTER);
		lsearch.setFont(new Font("나눔바른고딕", Font.BOLD, 20));
		lsearch.setOpaque(true);
		lsearch.setForeground(Color.WHITE);
		lsearch.setBackground(new Color(0X2A6049));
		lsearch.setBounds(465, 560, 120, 40);
		
		tfsearch = new JTextField();
		tfsearch.setBounds(585, 561, 350, 40);
		tfsearch.setFont(new Font("나눔바른고딕", Font.BOLD, 15));
		
		ImageIcon icon2 = new ImageIcon("images/select.png");
		bsearch = new JButton(icon2);
		bsearch.setBounds(935, 561, 50, 39);
		bsearch.setBackground(new Color(0X2A6049));
		
		bsearch.setBorderPainted(false); // 선 없애줌
		bsearch.setFocusPainted(false);
		bsearch.addActionListener(this);
		Apan.add(lsearch);
		Apan.add(tfsearch);
		Apan.add(bsearch);
		
		//Tab
        JTabbedPane pane = new JTabbedPane();
        
        pane.setBounds(0,0,1100,700);
		pane.setBackground(Color.WHITE);
		pane.setFont(new Font("나눔바른고딕", Font.BOLD, 20));

		pane.addTab("회원 관리", Apan);
		pane.addTab("재고 관리", AdminFrame);
		pane.addTab("매출 관리", SalesManagement);
		
	    TabPan.add(pane);
		
		getData();
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new AdminFrame("판매자 페이지", 1100, 700);
		DB.init();
	}
	


	private void getData() {
		model.setNumRows(0);
		String sql = "select * from member where isAdmin = false";
		if(searchWord != null && !searchWord.isEmpty()) sql += " and memberName like '%" + searchWord + "%'";
		ResultSet rs = DB.executeQuery(sql);
		int cnt = 0;
		try {
			while (rs.next()) {
				Vector<String> v = new Vector<>();
				v.add(rs.getString("memberID"));
				v.add(rs.getString("memberName"));
				v.add(rs.getString("registerDate"));
				v.add(rs.getString("address"));
				model.addRow(v);
				model.setValueAt(false, cnt, 4);
				cnt++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == wd) {
			if (JOptionPane.showConfirmDialog(null, "선택한 회원을 탈퇴 처리하시겠습니까?", "회원 탈퇴",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				String msg = "";
				for (int i = 0; i < table.getRowCount(); i++) {
					Boolean checked = Boolean.valueOf(table.getValueAt(i, 4).toString());
					String col = table.getValueAt(i, 0).toString();

					if (checked) {
						System.out.println(col);
						String sql = "delete from member where memberID = '" + col + "'";
						try {
							DB.executeUpdate(sql);
							msg = "탈퇴처리 완료";
						} catch (SQLException e1) {
							msg = "탈퇴처리에 오류가 발생했습니다.";
							e1.printStackTrace();
						}
					}
				}
				if (msg.isEmpty()) {
					JOptionPane.showMessageDialog(null, "탈퇴할 회원을 선택해주세요.");
				} else {
					JOptionPane.showMessageDialog(null, msg);
					model.setNumRows(0); // table 데이터 초기화
					getData();
				}
			}
		} else if(obj == bsearch) {
			searchWord = tfsearch.getText();
			getData();
			System.out.println(searchWord);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//테이블 셀 클릭하면 텍스트 필트에 값 넣어지도록
		if(e.getSource() == table) {
			int row = table.getSelectedRow();
			int col = table.getSelectedColumn();
			tfname.setText((String)table.getValueAt(row, 1));
	    	tfid.setText((String)table.getValueAt(row, 0));
	    	tfday.setText((String)table.getValueAt(row, 2));
	    	tfadress.setText((String)table.getValueAt(row, 3));
		}
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
		//탈퇴 버튼 커서 들어가면 색 변하도록
		if(e.getSource() == wd) {
		wd = (JButton)e.getSource();
		wd.setBackground(new Color(0X2A4549));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//탈퇴 버튼 커서 나가면 색 변하도록
		if(e.getSource() == wd) {
		wd = (JButton)e.getSource();
		wd.setBackground(new Color(0X2A6049) );
		}
	}
		
}
