package hotel.reservation.system;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class UpdateRoom extends JFrame implements ActionListener{
    JLabel l1, l2, l3, l4, l5;
    Choice ch1;
    JTextField t1, t2, t3;
    JButton b1, b2, b3;
    
    UpdateRoom(){
        setLayout(null);
        
        ImageIcon i1  = new ImageIcon(ClassLoader.getSystemResource("icons/seventh.jpg"));
        Image i3 = i1.getImage().getScaledInstance(350, 250,Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(i3);
        JLabel l15 = new JLabel(i2);
        l15.setBounds(320,30,350,250);
        add(l15);
        
        l1 = new JLabel("Update Room Status");
        l1.setFont(new Font("Tahoma", Font.PLAIN, 25));
        l1.setBounds(30, 5, 250, 25);
        l1.setForeground(Color.BLUE);
        add(l1);
        
        l2 = new JLabel("Customer Id: ");
        l2.setBounds(10, 45, 100, 20);
        add(l2);
        
        ch1 = new Choice();
        try{
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from customer");
            while(rs.next()){
                ch1.add(rs.getString("number"));
            }
        }catch(Exception e){
            System.out.println(e);
        }
        ch1.setBounds(150 , 45, 150, 20);
        add(ch1);
        
        l3 = new JLabel("Room No: ");
        l3.setBounds(10, 75, 100, 20);
        add(l3);
        
        t1 = new JTextField();
	t1.setBounds(150, 75, 150, 20);
	add(t1);
        
        l4 = new JLabel("Availability: ");
        l4.setBounds(10, 105, 100, 20);
        add(l4);
        
        t2 = new JTextField();
	t2.setBounds(150, 105, 150, 20);
	add(t2);
        
        l5 = new JLabel("Cleaning Status: ");
        l5.setBounds(10, 135, 100, 20);
        add(l5);
        
        t3 = new JTextField();
	t3.setBounds(150, 135, 150, 20);
	add(t3);
        
        b1 = new JButton("CHECK");
	b1.addActionListener(this);
	b1.setBounds(20, 190, 100, 33);
        b1.setBackground(Color.BLACK);
        b1.setForeground(Color.WHITE);
	add(b1);
        
        b2 = new JButton("UPDATE");
	b2.addActionListener(this);
	b2.setBounds(150, 190, 100, 33);
        b2.setBackground(Color.BLACK);
        b2.setForeground(Color.WHITE);
	add(b2);
        
        b3 = new JButton("Back");
	b3.addActionListener(this);
	b3.setBounds(90, 230, 100, 33);
        b3.setBackground(Color.BLACK);
        b3.setForeground(Color.WHITE);
	add(b3);
        
        getContentPane().setBackground(Color.WHITE);
        setSize(700, 400);
        setLocation(300, 200);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == b1){
            String document = ch1.getSelectedItem();
            String q1 = "select * from customer where number = '"+document+"'";
            try{
                Conn c = new Conn();
                ResultSet rs = c.s.executeQuery(q1);
                while(rs.next()){
                    t1.setText(rs.getString("room"));
                }
                
                ResultSet rs2 = c.s.executeQuery("select * from room where roomNo = '"+t1.getText()+"'");
                while(rs2.next()){
                    t2.setText(rs2.getString("available"));  
                    t3.setText(rs2.getString("status"));
                }
            }catch(Exception e){
                System.out.println(e);
            }
				
        }else if(ae.getSource() == b2){
            String roomNo = t1.getText(); 
            String available = t2.getText();
            String status = t3.getText(); 
            try{
                Conn c = new Conn();
                c.s.executeUpdate("update room set available = '"+available+"', status = '"+status+"' where roomNo = '"+roomNo+"'");
                JOptionPane.showMessageDialog(null, "Data Updated Successfully");
                
                setVisible(false);
                new Reception();
                
            }catch(Exception ee){
                System.out.println(ee);
            }
        }else if(ae.getSource() == b3){
            setVisible(false);
            new Reception();
        }
    }
    
    public static void main(String[] args){
        new UpdateRoom();
    }
}
