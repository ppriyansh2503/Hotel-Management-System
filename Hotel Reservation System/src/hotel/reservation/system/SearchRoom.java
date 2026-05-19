package hotel.reservation.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import net.proteanit.sql.*;

public class SearchRoom extends JFrame implements ActionListener{
    JTable t1;
    JLabel l1, l2, l3, l4, l5, l6, l7;
    JButton b1, b2;
    JComboBox c1;
    JCheckBox ch1;
    
    SearchRoom(){
        setLayout(null);
        
        l1 = new JLabel("Search For Room");
        l1.setFont(new Font("Tahoma", Font.PLAIN, 20));
        l1.setBounds(150, 20, 200, 20);
        add(l1);
        
        l2 = new JLabel("Bed Type: ");
        l2.setBounds(50, 60, 80, 20);
        add(l2);
        
        c1 = new JComboBox(new String[] {"Single Bed", "Double Bed"});
	c1.setBounds(120, 60, 150, 20);
        c1.setBackground(Color.WHITE);
	add(c1);
        
        ch1 = new JCheckBox("Only Display Available");
        ch1.setBounds(500, 60, 200, 20);
        ch1.setBackground(Color.WHITE);
	add(ch1);
        
        l3 = new JLabel("Room No");
        l3.setBounds(0, 90, 60, 20);
        add(l3);
        
        l4 = new JLabel("Availability");
        l4.setBounds(90, 90, 80, 20);
        add(l4);

        l5 = new JLabel("Status");
        l5.setBounds(180, 90, 60, 20);
        add(l5);
        
        l6 = new JLabel("Price");
        l6.setBounds(270, 90, 60, 20);
        add(l6);
        
        l7 = new JLabel("Bed Type");
        l7.setBounds(360, 90, 60, 20);
        add(l7);
        
        t1 = new JTable();
        t1.setBounds(0, 110, 450, 250);
        add(t1);
        
        try{
            Conn c = new Conn();
            String str = "select * from room";
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
                String q1 = "select * from Room where type = '"+c1.getSelectedItem()+"'";
		String q2 = "select * from Room where available = 'Available' AND type = '"+c1.getSelectedItem()+"'";
			
                Conn c = new Conn();
		ResultSet rs;
                
		if(ch1.isSelected()){
                    rs = c.s.executeQuery(q2);
		}else{
                    rs = c.s.executeQuery(q1);
                }
                
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
        new SearchRoom();
    }
}
