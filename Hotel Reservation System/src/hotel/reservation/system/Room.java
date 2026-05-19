package hotel.reservation.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import net.proteanit.sql.*;

public class Room extends JFrame implements ActionListener{
    JTable t1;
    JLabel l1, l2, l3, l4, l5;
    JButton b1;
    
    Room(){
        setLayout(null);
        
        ImageIcon i1  = new ImageIcon(ClassLoader.getSystemResource("icons/eight.jpg"));
        Image i3 = i1.getImage().getScaledInstance(400, 400,Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(i3);
        JLabel l15 = new JLabel(i2);
        l15.setBounds(400,40,400,400);
        add(l15);
        
        l1 = new JLabel("Room No.");
        l1.setBounds(0, 10, 60, 20);
        add(l1);
        
        l2 = new JLabel("Availability");
        l2.setBounds(70, 10, 80, 20);
        add(l2);
        
        l3 = new JLabel("Status");
        l3.setBounds(150, 10, 60, 20);
        add(l3);
        
        l4 = new JLabel("Price");
        l4.setBounds(215, 10, 60, 20);
        add(l4);
        
        l5 = new JLabel("Bed Type");
        l5.setBounds(280, 10, 60, 20);
        add(l5);
        
        t1 = new JTable();
        t1.setBounds(0, 40, 350, 350);
        add(t1);
        
        try{
            Conn c = new Conn();
            String str = "select * from room";
            ResultSet rs = c.s.executeQuery(str);
            t1.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e1){
            System.out.println(e1);
	}
        
        b1 = new JButton("Back");
	b1.addActionListener(this);
	b1.setBounds(150, 400, 150, 33);
        b1.setBackground(Color.BLACK);
        b1.setForeground(Color.WHITE);
	add(b1);
        
        getContentPane().setBackground(Color.WHITE);
        setSize(850, 650);
        setLocation(300, 200);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae){
        setVisible(false);
        new Reception();
    }
    public static void main(String[] args){
        new Room();
    }
}
