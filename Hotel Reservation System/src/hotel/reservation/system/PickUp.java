package hotel.reservation.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import net.proteanit.sql.*;

public class PickUp extends JFrame implements ActionListener{
    JTable t1;
    JLabel l1, l2, l3, l4, l5, l6, l7, l8, l9;
    JButton b1, b2;
    Choice c1;
    JCheckBox ch1;
    
    PickUp(){
        setLayout(null);
        
        l1 = new JLabel("Pickup Service");
        l1.setFont(new Font("Tahoma", Font.PLAIN, 20));
        l1.setBounds(150, 20, 200, 20);
        add(l1);
        
        l2 = new JLabel("Type of Car: ");
        l2.setBounds(50, 60, 80, 20);
        add(l2);
        
        c1 = new Choice();
        try{
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from driver");
            while(rs.next()){
                c1.add(rs.getString("brand"));
            }
        }catch(Exception e){
            System.out.println(e);
        }
        c1.setBounds(150 , 60, 150, 20);
        add(c1);
        
        l3 = new JLabel("Name");
        l3.setBounds(0, 90, 60, 20);
        add(l3);
        
        l4 = new JLabel("Age");
        l4.setBounds(70, 90, 30, 20);
        add(l4);

        l5 = new JLabel("Gender");
        l5.setBounds(130, 90, 60, 20);
        add(l5);
        
        l6 = new JLabel("Company");
        l6.setBounds(190, 90, 60, 20);
        add(l6);
        
        l7 = new JLabel("Brand");
        l7.setBounds(260, 90, 60, 20);
        add(l7);
        
        l8 = new JLabel("Availability");
        l8.setBounds(320, 90, 70, 20);
        add(l8);
        
        l9 = new JLabel("Location");
        l9.setBounds(390, 90, 60, 20);
        add(l9);
        
        t1 = new JTable();
        t1.setBounds(0, 110, 450, 250);
        add(t1);
        
        try{
            Conn c = new Conn();
            String str = "select * from driver";
            ResultSet rs = c.s.executeQuery(str);
            t1.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e1){
            System.out.println(e1);
	}
        
        b1 = new JButton("SUBMIT");
	b1.addActionListener(this);
	b1.setBounds(50, 400, 150, 33);
        b1.setBackground(Color.BLACK);
        b1.setForeground(Color.WHITE);
	add(b1);
        
        b2 = new JButton("Back");
	b2.addActionListener(this);
	b2.setBounds(250, 400, 150, 33);
        b2.setBackground(Color.BLACK);
        b2.setForeground(Color.WHITE);
	add(b2);
        
        getContentPane().setBackground(Color.WHITE);
        setSize(850, 650);
        setLocation(300, 200);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == b1){
            try{
                String q = "select * from driver where brand = '"+c1.getSelectedItem()+"'";
			
                Conn c = new Conn();
		ResultSet rs = c.s.executeQuery(q);
                t1.setModel(DbUtils.resultSetToTableModel(rs));
            }catch (SQLException ss){
                System.out.println(ss);
            }
			
        }else{
            setVisible(false);
            new Reception();
        }
    }
    public static void main(String[] args){
        new PickUp();
    }
}
