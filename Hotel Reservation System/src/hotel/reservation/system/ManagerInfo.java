package hotel.reservation.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import net.proteanit.sql.*;

public class ManagerInfo extends JFrame implements ActionListener{
    JTable t1;
    JLabel l1, l2, l3, l4, l5, l6, l7, l8;
    JButton b1;
    
    ManagerInfo(){
        setLayout(null);
        
        l1 = new JLabel("Name");
        l1.setBounds(0, 10, 60, 20);
        add(l1);
        
        l2 = new JLabel("Age");
        l2.setBounds(100, 10, 80, 20);
        add(l2);
        
        l3 = new JLabel("Gender");
        l3.setBounds(175, 10, 60, 20);
        add(l3);
        
        l4 = new JLabel("job");
        l4.setBounds(250, 10, 60, 20);
        add(l4);
        
        l5 = new JLabel("Salary");
        l5.setBounds(340, 10, 60, 20);
        add(l5);
        
        l6 = new JLabel("Phone");
        l6.setBounds(430, 10, 60, 20);
        add(l6);
        
        l7 = new JLabel("Email");
        l7.setBounds(515, 10, 110, 20);
        add(l7);
        
        l8 = new JLabel("Aadhar");
        l8.setBounds(600, 10, 110, 20);
        add(l8);
        
        t1 = new JTable();
        t1.setBounds(0, 40, 680, 350);
        add(t1);
        
        try{
            Conn c = new Conn();
            String str = "select * from employee where job = 'Manager'";
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
        new ManagerInfo();
    }
}
