package ui.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import db.DB;

public class JoinFrame extends JFrame implements FocusListener, ActionListener, KeyListener{
	
	private JPanel pan;
	private Font font;
	private JButton btnJoin;
	private JPasswordField tfPas;
	private JPasswordField tfPass;
	private JTextField tfId;
	private JTextField tfName;
	private JTextField tfAddress;
	private JButton btnCheck;
	private boolean dupliCheck = false;
	
	private String today;
	private JLabel lblPa;
	
	public JoinFrame(String title, int width, int height) {
		setTitle(title);
		setSize(width, height);
		setLocationRelativeTo(this);
		font = new Font("나눔바른고딕", Font.PLAIN, 18);
		
		frame();
		subframe();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        today = format.format(new Date());
        
		setVisible(true);
		requestFocus();
	}


	private void frame() {
		pan = new JPanel();
		//pan.setBackground(Color.WHITE);
		pan.setLayout(new GridLayout(8, 1));
		add(pan);
	}

	private void subframe() {
		JPanel panTop = new JPanel();
		panTop.setBackground(Color.WHITE);
		panTop.setBorder(new LineBorder(new Color(0X2A6049), 2));
		
		ImageIcon img = new ImageIcon("images/mart.png");
		JLabel lblImage = new JLabel(img);
		
		JLabel lblTitle = new JLabel("골라JAVA");
		lblTitle.setFont(new Font("나눔바른고딕", Font.BOLD, 35));		
		lblTitle.setForeground(new Color(0X2A6049));
		
		JPanel panTitle = new JPanel();
		panTitle.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
		panTitle.setBackground(Color.WHITE);
		JLabel title = new JLabel("회원정보 입력");
		title.setFont(new Font("나눔바른고딕", Font.BOLD, 25));
		
		JPanel panId = new JPanel();
		panId.setBackground(Color.WHITE);
		panId.setLayout(null);
		
		JLabel lblId = new JLabel("아이디");
		lblId.setBounds(70, 0, 70, 30);
		lblId.setFont(font);
		
		tfId = new JTextField("아이디를 입력해 주세요");
		tfId.addFocusListener(this);
		tfId.setFont(font);
		tfId.setForeground(new Color(0xD3D3D3));
		tfId.setBounds(70, 30, 300, 45);
		
		btnCheck = new JButton("중복체크");
		btnCheck.setFont(new Font("나눔바른고딕", Font.PLAIN, 15));
		btnCheck.setForeground(Color.WHITE);
		btnCheck.setBackground(new Color(0X2A6049));
		btnCheck.setBounds(370, 30, 100, 44);
		btnCheck.addActionListener(this);
		
		JPanel panPass = new JPanel();
		panPass.setBackground(Color.WHITE);
		panPass.setLayout(null);
		
		JLabel lblPass = new JLabel("비밀번호");
		lblPass.setBounds(70, 0, 70, 30);
		lblPass.setFont(font);
		
		tfPass = new JPasswordField("비밀번호를 입력해 주세요");
		tfPass.addFocusListener(this);
		tfPass.addKeyListener(this);
		tfPass.setEchoChar((char)0);
		tfPass.setFont(font);
		tfPass.setForeground(new Color(0xD3D3D3));
		tfPass.setBounds(70, 30, 400, 45);
		
		JPanel panPas = new JPanel();
		panPas.setBackground(Color.WHITE);
		panPas.setLayout(null);
		
		JLabel lblPas = new JLabel("비밀번호 재확인");
		lblPas.setBounds(70, 0, 200, 30);
		lblPas.setFont(font);
		
		lblPa = new JLabel();
		lblPa.setBounds(310, 10, 200, 20);
		lblPa.setFont(new Font("나눔바른고딕", Font.PLAIN, 13));
		lblPa.setForeground(Color.RED);
		
		
		tfPas = new JPasswordField("비밀번호를 다시한번 입력해 주세요");
		tfPas.addFocusListener(this);
		tfPas.setEchoChar((char)0);
		tfPas.setFont(font);
		tfPas.setForeground(new Color(0xD3D3D3));
		tfPas.setBounds(70, 30, 400, 45);
		tfPas.addKeyListener(this);
		
		JPanel panName = new JPanel();
		panName.setBackground(Color.WHITE);
		panName.setLayout(null);
		
		JLabel lblName = new JLabel("이름");
		lblName.setBounds(70, 0, 70, 30);
		lblName.setFont(font);
		
		tfName = new JTextField("이름을 입력해주세요");
		tfName.addFocusListener(this);
		tfName.setFont(font);
		tfName.setForeground(new Color(0xD3D3D3));
		tfName.setBounds(70, 30, 400, 45);
		
		JPanel panAddress = new JPanel();
		panAddress.setBackground(Color.WHITE);
		panAddress.setLayout(null);
		
		JLabel lblAddress = new JLabel("주소");
		lblAddress.setBounds(70, 0, 70, 30);
		lblAddress.setFont(font);
		
		tfAddress = new JTextField("주소를 입력해주세요");
		tfAddress.addFocusListener(this);
		tfAddress.setFont(font);
		tfAddress.setForeground(new Color(0xD3D3D3));
		tfAddress.setBounds(70, 30, 400, 45);
		
		JPanel panJoin = new JPanel();
		panJoin.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		panJoin.setBackground(Color.WHITE);
		
		btnJoin = new JButton("가입 완료하기");
		btnJoin.setFont(font);
		btnJoin.addActionListener(this);
		btnJoin.setBackground(new Color(0X2A6049));
		btnJoin.setForeground(Color.WHITE);
		btnJoin.setPreferredSize(new Dimension(400, 60));
		
		panTop.add(lblImage);
		panTop.add(lblTitle);
		panTitle.add(title);
		panId.add(lblId);
		panId.add(tfId);
		panId.add(btnCheck);
		panPass.add(lblPass);
		panPass.add(tfPass);
		panPas.add(lblPas);
		panPas.add(lblPa);
		panPas.add(tfPas);
		panName.add(lblName);
		panName.add(tfName);
		panAddress.add(lblAddress);
		panAddress.add(tfAddress);
		panJoin.add(btnJoin);
		
		
		pan.add(panTop);
		pan.add(panTitle);
		pan.add(panId);
		pan.add(panPass);
		pan.add(panPas);
		pan.add(panName);
		pan.add(panAddress);
		pan.add(panJoin);
		
	}
	public static void main(String[] args) {
		new JoinFrame("마음껏 골라JAVA", 550, 700);
	}
	
	// 아이디 중복체크
		private boolean idDuplicateCheck(String id) {
			String sql = "select memberID from member where memberID = '" + id + "'";
			ResultSet rs = DB.executeQuery(sql);
			try {
				if (rs.next())
					return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dupliCheck = true; 
			return false;
		}


	@Override
	public void focusGained(FocusEvent e) {
		Object obj = e.getSource();
		
		if(obj == tfId) {
			if(tfId.getText().equals("아이디를 입력해 주세요")) {
				tfId.setText("");
				tfId.setForeground(Color.BLACK);
			}
		} else if(obj == tfPass) {
			if(tfPass.getText().equals("비밀번호를 입력해 주세요")) {
				tfPass.setText("");
				tfPass.setForeground(Color.BLACK);
				tfPass.setEchoChar('*');
			}
		} else if(obj == tfPas) {
			if(tfPas.getText().equals("비밀번호를 다시한번 입력해 주세요")) {
				tfPas.setText("");
				tfPas.setForeground(Color.BLACK);
				tfPas.setEchoChar('*');
			}
		} else if(obj == tfName) {
			if(tfName.getText().equals("이름을 입력해주세요")) {
				tfName.setText("");
				tfName.setForeground(Color.BLACK);
			}
		} else {
			if(tfAddress.getText().equals("주소를 입력해주세요")) {
				tfAddress.setText("");
				tfAddress.setForeground(Color.BLACK);
			}
		}
	} 


	@Override
	public void focusLost(FocusEvent e) {
		Object obj = e.getSource();
		
		if(obj == tfId) {
			if(tfId.getText().equals("") || tfId.getText().length() == 0) {
				tfId.setText("아이디를 입력해 주세요");
				tfId.setForeground(new Color(0xD3D3D3));
			}
		} else if(obj == tfPass) {
			if(tfPass.getText().equals("") || tfPass.getText().length() == 0) {
				tfPass.setEchoChar((char)0);
				tfPass.setForeground(new Color(0xD3D3D3));
				tfPass.setText("비밀번호를 입력해 주세요");
			}
		} else if(obj == tfPas) {
			if(tfPas.getText().equals("") || tfPas.getText().length() == 0) {
				tfPas.setEchoChar((char)0);
				tfPas.setForeground(new Color(0xD3D3D3));
				tfPas.setText("비밀번호를 다시한번 입력해 주세요");
			} 
		} else if(obj == tfName) {
			if(tfName.getText().equals("") || tfName.getText().length() == 0) {
				tfName.setForeground(new Color(0xD3D3D3));
				tfName.setText("이름을 입력해주세요");
				
			}
		} else {
			if(tfAddress.getText().equals("") || tfAddress.getText().length() == 0) {
				tfAddress.setForeground(new Color(0xD3D3D3));
				tfAddress.setText("주소를 입력해주세요");
			}
		}
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if(obj == btnJoin) {
			if(tfId.getText().equals("") || tfId.getText().equals("아이디를 입력해 주세요")) {
				JOptionPane.showMessageDialog(null, "아이디를 입력해 주세요", "ERROR", JOptionPane.ERROR_MESSAGE);
			} else if(tfPass.getText().equals("") || tfPass.getText().equals("비밀번호를 입력해 주세요")) {
				JOptionPane.showMessageDialog(null, "비밀번호를 입력해 주세요", "ERROR", JOptionPane.ERROR_MESSAGE);
			} else if(tfPas.getText().equals("") || tfPas.getText().equals("비밀번호를 다시한번 입력해 주세요")) {
				JOptionPane.showMessageDialog(null, "비밀번호를 다시한번 입력해 주세요", "ERROR", JOptionPane.ERROR_MESSAGE);
			} else if(tfName.getText().equals("") || tfName.getText().equals("이름을 입력해주세요")) {
				JOptionPane.showMessageDialog(null, "이름을 입력헤주세요", "ERROR", JOptionPane.ERROR_MESSAGE);
			} else if(tfAddress.getText().equals("") || tfAddress.getText().equals("주소를 입력해주세요")) {
				JOptionPane.showMessageDialog(null, "주소를 입력헤주세요", "ERROR", JOptionPane.ERROR_MESSAGE);
			} else if(!tfPas.getText().equals(tfPass.getText())) {
				JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
			} else if(!dupliCheck) JOptionPane.showMessageDialog(null, "아이디 중복체크를 해주세요.", "ERROR", JOptionPane.ERROR_MESSAGE);
			
			else {
				String sql = "insert into member(memberID, memberName, password, registerDate, address) values('" +
						tfId.getText() + "', '" +
						tfName.getText() + "', '" +
						tfPass.getText() + "', '" +
						today + "', '" +
						tfAddress.getText() + "')";
				System.out.println(sql);
				try {
					DB.executeUpdate(sql);
					JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다.");
					this.dispose();
				} catch (SQLException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "ERROR", "회원가입에 실패했습니다. 정보를 다시 확인해주세요.", JOptionPane.ERROR_MESSAGE);
				}
			}
		} else if(obj == btnCheck && !tfId.getText().isEmpty() && !tfId.getText().equals("아이디를 입력해 주세요")) {
			if (!dupliCheck) {
				if (idDuplicateCheck(tfId.getText())) {
					JOptionPane.showMessageDialog(null, "중복된 아이디입니다.");
				} else {
					JOptionPane.showMessageDialog(null, "사용 가능한 아이디입니다.");
					btnCheck.setText("다시 입력");
					tfId.setEnabled(false);
					tfPass.requestFocus();
				}
			} else {
				tfId.setText("");
				tfId.setEnabled(true);
				tfId.requestFocus();
				btnCheck.setText("중복 체크");
				dupliCheck = false;
			}
		}
	}


	@Override
	public void keyTyped(KeyEvent e) {
		
	}


	@Override
	public void keyPressed(KeyEvent e) {
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		Object obj = e.getSource();
		
		
	
		if(obj == tfPas) {
			if(tfPas.getText().equals(tfPass.getText())) {
				lblPa.setText("비밀번호가 일치합니다.");
			} else {
				lblPa.setText("비밀번호가 일치하지 않습니다.");
			}
		} else if(obj == tfPass) {
			if(tfPas.getText().equals(tfPass.getText())) {
				lblPa.setText("비밀번호가 일치합니다.");
			} else if(tfPas.getText().equals("비밀번호를 다시한번 입력해 주세요")) {
				lblPa.setText("필수 입력 정보입니다");
			} else {
				lblPa.setText("비밀번호가 일치하지 않습니다.");
			}
		}
	}
}
