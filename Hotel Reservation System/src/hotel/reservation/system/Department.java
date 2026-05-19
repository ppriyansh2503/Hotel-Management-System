package hotel.reservation.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import net.proteanit.sql.*;

public class Department extends JFrame implements ActionListener{
    JTable t1;
    JLabel l1, l2;
    JButton b1;
    
    Department(){
        setLayout(null);
        
        l1 = new JLabel("Department");
        l1.setBounds(150, 10, 90, 20);
        add(l1);
        
        l2 = new JLabel("Budget");
        l2.setBounds(400, 10, 80, 20);
        add(l2);
        
        t1 = new JTable();
        t1.setBounds(0, 40, 700, 300);
        add(t1);
        
        try{
            Conn c = new Conn();
            String str = "select * from department";
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
        setSize(700, 300);
        setLocation(300, 200);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae){
        setVisible(false);
        new Reception();
    }
    public static void main(String[] args){
        new Department();
    }
}
