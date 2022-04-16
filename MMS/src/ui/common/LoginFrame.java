package ui.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import db.DB;
import db.UserInfo;
import ui.customer.CustomerFrame;
import ui.seller.AdminFrame;

public class LoginFrame extends JFrame implements FocusListener, ActionListener  {

	private JPanel pan, panTop, panCenter;
	private JTextField tfId;
	private Font font;
	private JButton btnLogin;
	private JButton btnPass;
	private JPasswordField tfPw;
	private CustomerFrame mainFrame;
	public LoginFrame(String title, int width, int height, CustomerFrame mainFrame) {
		setTitle(title);
		setSize(width, height);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(this);
		this.mainFrame = mainFrame;
		frame();
		frametop();
		framebottom();
		
		setVisible(true);
	}

	private void framebottom() {
		panCenter = new JPanel();
		panCenter.setLayout(new GridLayout(4, 1, 15, 15));
		panCenter.setBorder(BorderFactory.createEmptyBorder(20, 70, 40, 70));
		panCenter.setBackground(Color.WHITE);
		
		font = new Font("나눔바른고딕", Font.PLAIN, 18);
	
		
		tfId = new JTextField("아이디");
		tfId.setFont(font);
		tfId.setForeground(new Color(0xD3D3D3));
		tfId.addFocusListener(this);
		
		tfPw = new JPasswordField("비밀번호");
		tfPw.setEchoChar((char)0);
		tfPw.setFont(font);
		tfPw.setForeground(new Color(0xD3D3D3));
		tfPw.addFocusListener(this);
		
		
		btnLogin = new JButton("로그인");
		btnLogin.addActionListener(this);
		btnLogin.setFont(font);
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setBackground(new Color(0X2A6049));
		
		
		btnPass = new JButton("회원가입");
		btnPass.addActionListener(this);
		btnPass.setFont(font);
		btnPass.setForeground(new Color(0X2A6049));
		btnPass.setBackground(Color.WHITE);
		
	
		panCenter.add(tfId);
		panCenter.add(tfPw);
		panCenter.add(btnLogin);
		panCenter.add(btnPass);
		
		pan.add(panCenter);
	}

		private void frametop() {
		panTop = new JPanel();
		panTop.setBackground(Color.WHITE);
		panTop.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
		
		ImageIcon img = new ImageIcon("images/mart.png");
		JLabel lblImage = new JLabel(img);
		
		JLabel lblTitle = new JLabel("골라JAVA");
		lblTitle.setFont(new Font("나눔바른고딕", Font.BOLD, 35));		
		lblTitle.setForeground(new Color(0X2A6049));
		
		panTop.add(lblImage);
		panTop.add(lblTitle);
		
		pan.add(panTop, BorderLayout.NORTH);
	}

	private void frame() {
		pan = new JPanel();
		pan.setLayout(new BorderLayout());
		
		add(pan);
	}

	public static void main(String[] args) {
		new LoginFrame("마음껏 골라JAVA", 500, 450, null);
	}

	@Override
	public void focusGained(FocusEvent e) {
		Object obj = e.getSource();
		
		if(obj == tfId) {
			if(tfId.getText().equals("아이디")) {
			tfId.setText("");
			tfId.setForeground(Color.BLACK);
			tfId.setBorder(BorderFactory.createLineBorder(new Color(0X2A6049), 3));
			}
		} else if (obj == tfPw) {
			if(tfPw.getText().equals("비밀번호")) {
			tfPw.setText("");
			tfPw.setEchoChar('*');
			tfPw.setForeground(Color.BLACK);
			tfPw.setBorder(BorderFactory.createLineBorder(new Color(0X2A6049), 3));
		
			}
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		Object obj = e.getSource();
		
		if(obj == tfId) {
			if(tfId.getText().equals("") || tfId.getText().length() == 0) {
				tfId.setText("아이디");
				tfId.setForeground(new Color(0xD3D3D3));
				tfId.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				} 
			}
		else if(obj == tfPw){
				if(tfPw.getText().equals("") || tfPw.getText().length() == 0) {
					tfPw.setEchoChar((char)0);
					tfPw.setText("비밀번호");
					tfPw.setForeground(new Color(0xD3D3D3));
					tfPw.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				}
				
			}
		}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
			
		if(obj == btnLogin) {
			if(tfId.getText().equals("") || tfId.getText().equals("아이디")) {
				JOptionPane.showMessageDialog(null, "아이디를 다시 입력해 주세요", "ERROR", JOptionPane.ERROR_MESSAGE);
			} else if(tfPw.getText().equals("") || tfPw.getText().equals("비밀번호")) {
				JOptionPane.showMessageDialog(null, "비밀번호를 다시 입력해 주세요", "ERROR", JOptionPane.ERROR_MESSAGE);
			} else {
				String sql = "select * from member where memberID = '" + tfId.getText() + "'and password = '" +
						tfPw.getText() + "'";
				ResultSet rs = DB.executeQuery(sql);
				try {
					if(rs.next()) {
						UserInfo.getInstance().userID = rs.getString("memberID");
						UserInfo.getInstance().userName = rs.getString("memberName");
						UserInfo.getInstance().address = rs.getString("address");
						UserInfo.getInstance().isAdmin = rs.getBoolean("isAdmin");
						mainFrame.setLogin(true);
						System.out.println("로그인 성공");
						if(UserInfo.getInstance().isAdmin) new AdminFrame("회원관리", 1100, 700);
						this.dispose();
					} else {
						JOptionPane.showMessageDialog(null, "아이디 또는 비밀번호가 틀렸습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "아이디와 비밀번호를 다시 입력해주세요.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		} else if(obj.equals(btnPass)) {
			new JoinFrame("회원가입", 550, 700);
		}
	}
		
}

	

	
	

