package chatproject;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Register extends JFrame {

	private JPanel contentPane;
	private JTextField txtname;
	private JTextField txtpassword;

	
	public Register() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		setResizable(false);
		setTitle("Register");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 400);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblName = new JLabel("Name :");
		lblName.setBounds(39, 55, 56, 16);
		contentPane.add(lblName);
		
		txtname = new JTextField();
		txtname.setBounds(94, 52, 174, 22);
		contentPane.add(txtname);
		txtname.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password : ");
		lblPassword.setBounds(15, 133, 80, 16);
		contentPane.add(lblPassword);
		
		txtpassword = new JTextField();
		txtpassword.setBounds(94, 130, 174, 22);
		contentPane.add(txtpassword);
		txtpassword.setColumns(10);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = txtname.getText();
				String password = txtpassword.getText();
				register(name, password);
			}
		});
		btnRegister.setBounds(94, 220, 97, 25);
		contentPane.add(btnRegister);
		
		
		setVisible(true);
	}
	
	private void register(String name, String password){
		String n = name;
		String p = password;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/prac","root","HiragiAkira");
			Statement st = conn.createStatement();
			st.executeUpdate("insert into register values('"+n+"','"+p+"')");
		}catch(Exception e){
			e.printStackTrace();
		}
		dispose();
		new Login();
	}
}
