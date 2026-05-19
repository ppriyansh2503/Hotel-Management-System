package hotel.reservation.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener{
    JLabel l1, l2;
    JTextField t1;
    JPasswordField p1;
    JButton b1, b2;
    
    Login(){
        
        setLayout(null);
        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/second.jpg"));
        Image i2 = i1.getImage().getScaledInstance(200,200,Image.SCALE_DEFAULT);
        ImageIcon i3 =  new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(350,10,150,150);
        add(l3);
        
        l1 = new JLabel("UserName:");
        l1.setBounds(40,20,100,30);
        add(l1);
        
        l2 = new JLabel("Password:");
        l2.setBounds(40,70,100,30);
        add(l2);
        
        t1 = new JTextField();
        t1.setBounds(140,20,150,30);
        add(t1);
        
        p1 = new JPasswordField();
        p1.setBounds(140,70,150,30);
        add(p1);
        
        b1 = new JButton("Login");
        b1.setBounds(40,150,100,30);
        b1.setFont(new Font("serif",Font.BOLD,15));
        b1.setForeground(Color.WHITE);
        b1.setBackground(Color.BLACK);
        b1.addActionListener(this);
        add(b1);
        
        b2 = new JButton("Cancel");
        b2.setBounds(190,150,100,30);
        b2.setFont(new Font("serif",Font.BOLD,15));
        b2.setForeground(Color.WHITE);
        b2.setBackground(Color.BLACK);
        b2.addActionListener(this);
        add(b2);
        
        getContentPane().setBackground(Color.WHITE);

        setVisible(true);
        setSize(600,300);
        setLocation(400,250);
    }
    
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource()==b1){
        try{
            Conn c1 = new Conn();
            String u = t1.getText();
            String v = p1.getText();
            
            String q = "select * from login where username='"+u+"' and password='"+v+"'";
            
            ResultSet rs = c1.s.executeQuery(q); 
            if(rs.next()){ 
                new Dashboard().setVisible(true);
                setVisible(false);
            }else{
                JOptionPane.showMessageDialog(null, "Invalid login");
                setVisible(false);
            }
        }catch(Exception e){
            System.out.println(e);
        }
        }else if(ae.getSource()==b2){
            System.exit(0);
        }
    }
    
    public static void main(String[] args){
        new Login();
    }
}
