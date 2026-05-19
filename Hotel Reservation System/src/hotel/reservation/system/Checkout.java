package hotel.reservation.system;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.util.Date;
import java.awt.event.*;

public class Checkout extends JFrame implements ActionListener{
    JLabel l1, l2, l3, l31, l4, l41, l5, l51;
    Choice ch1;
    JButton b1, b2;
    
    Checkout(){
        setLayout(null);
        
        ImageIcon i1  = new ImageIcon(ClassLoader.getSystemResource("icons/tick.png"));
        Image i3 = i1.getImage().getScaledInstance(20, 20,Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(i3);
        JLabel l15 = new JLabel(i2);
        l15.setBounds(293,50,20,20);
        add(l15);
        
        l1 = new JLabel("CHECKOUT");
        l1.setBounds(100, 15, 100, 30);
        l1.setForeground(Color.BLUE);
        l1.setFont(new Font("Tahoma", Font.PLAIN, 20));
        add(l1);
        
        l2 = new JLabel("Customer ID:");
        l2.setBounds(30, 50, 100, 30);
        add(l2);
        
        l3 = new JLabel("Room No:");
        l3.setBounds(30, 80, 100, 30);
        add(l3);
        
        l31 = new JLabel();
        l31.setBounds(140, 80, 100, 30);
        add(l31);
        
        l4 = new JLabel("Checkin Time:");
        l4.setBounds(30, 110, 100, 30);
        add(l4);
        
        l41 = new JLabel();
        l41.setBounds(140, 110, 200, 30);
        add(l41);
        
        ch1 = new Choice();
        try{
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from customer");
            while(rs.next()){
                ch1.add(rs.getString("number"));
                l31.setText(rs.getString("room"));
                l41.setText(rs.getString("checkintime"));
            }
        }catch(Exception e){
            System.out.println(e);
        }
        ch1.setBounds(140 , 50, 150, 30);
        add(ch1);
        
        l5 = new JLabel("Checkout Time:");
        l5.setBounds(30, 140, 100, 30);
        add(l5);
        
        Date date = new Date();
        l51 = new JLabel("" + date);
        l51.setBounds(140, 140, 200, 30);
        add(l51);
        
        b1 = new JButton("CHECKOUT");
	b1.addActionListener(this);
	b1.setBounds(50, 200, 110, 30);
        b1.setBackground(Color.BLACK);
        b1.setForeground(Color.WHITE);
	add(b1);

	b2 = new JButton("BACK");
	b2.addActionListener(this);
	b2.setBounds(190, 200, 110, 30);
        b2.setBackground(Color.BLACK);
        b2.setForeground(Color.WHITE);
	add(b2);
        
        ImageIcon i4  = new ImageIcon(ClassLoader.getSystemResource("icons/sixth.jpg"));
        Image i5 = i4.getImage().getScaledInstance(325, 180,Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel l16 = new JLabel(i6);
        l16.setBounds(335,45,325,180);
        add(l16);
        
        getContentPane().setBackground(Color.WHITE);
        setSize(700, 300);
        setLocation(300, 200);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == b1){
            String q1 = "delete from customer where number = '"+ch1.getSelectedItem()+"'";
            String q2 = "update room set available = 'Available' where roomNo = '"+l31.getText()+"'";
            try{
                Conn c = new Conn();
                c.s.executeUpdate(q1);
                c.s.executeUpdate(q2);
                JOptionPane.showMessageDialog(null, "Checkout Done");
                
                setVisible(false);
                new Reception();
            }catch(Exception e){
                System.out.println(e);
            }
        }else{
            setVisible(false);
            new Reception();
        }
    }
    
    public static void main(String[] args){
        new Checkout();
    }
}