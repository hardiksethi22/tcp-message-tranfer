package chatproject;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtAddress;
	private JLabel lblAddress;
	private JLabel lblPort;
	private JTextField txtPort;
	private JLabel lblAddressDesc;
	private JLabel lblPortDesc;

	
	
	public Login() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		setResizable(false);
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 400);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtName = new JTextField();
		txtName.setBounds(64, 50, 165, 28);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(119, 24, 49, 23);
		contentPane.add(lblName);
		
		txtAddress = new JTextField();
		txtAddress.setBounds(64, 113, 165, 28);
		contentPane.add(txtAddress);
		txtAddress.setColumns(10);
		
		lblAddress = new JLabel("IP Address:");
		lblAddress.setBounds(110, 91, 74, 16);
		contentPane.add(lblAddress);
		
		lblPort = new JLabel("Port:");
		lblPort.setBounds(127, 173, 39, 16);
		contentPane.add(lblPort);
		
		txtPort = new JTextField();
		txtPort.setColumns(10);
		txtPort.setBounds(64, 195, 165, 28);
		contentPane.add(txtPort);
		
		lblAddressDesc = new JLabel("eg. 127.0.0.1");
		lblAddressDesc.setBounds(98, 143, 98, 23);
		contentPane.add(lblAddressDesc);
		
		lblPortDesc = new JLabel("eg. 8192");
		lblPortDesc.setBounds(114, 228, 65, 23);
		contentPane.add(lblPortDesc);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = txtName.getText();
				String address = txtAddress.getText();
				int port = Integer.parseInt(txtPort.getText());
				login(name, address, port);
			}
		});
		btnLogin.setBounds(31, 296, 97, 25);
		contentPane.add(btnLogin);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Register();
			}
		});
		btnRegister.setBounds(170, 296, 97, 25);
		contentPane.add(btnRegister);
		
	}
	
	private void login(String name, String address, int port){
		dispose();
		new ClientWindow(name, address, port);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
