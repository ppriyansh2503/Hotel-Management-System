package hotel.reservation.system;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class UpdateCheck extends JFrame implements ActionListener{
    JLabel l1, l2, l3, l4, l5, l6;
    Choice ch1;
    JTextField t1, t2, t3, t4, t5;
    JButton b1, b2, b3;
    
    UpdateCheck(){
        setLayout(null);
        
        ImageIcon i1  = new ImageIcon(ClassLoader.getSystemResource("icons/nine.jpg"));
        Image i3 = i1.getImage().getScaledInstance(300, 200,Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(i3);
        JLabel l15 = new JLabel(i2);
        l15.setBounds(350,30,300,200);
        add(l15);
        
        l1 = new JLabel("Update Status");
        l1.setFont(new Font("Tahoma", Font.PLAIN, 20));
        l1.setBounds(80, 5, 150, 20);
        l1.setForeground(Color.BLUE);
        add(l1);
        
        l2 = new JLabel("Customer Id: ");
        l2.setBounds(10, 35, 100, 20);
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
        ch1.setBounds(150 , 35, 150, 20);
        add(ch1);
        
        l3 = new JLabel("Name: ");
        l3.setBounds(10, 65, 100, 20);
        add(l3);
        
        t1 = new JTextField();
	t1.setBounds(150, 65, 150, 20);
	add(t1);
        
        l4 = new JLabel("Room No: ");
        l4.setBounds(10, 95, 100, 20);
        add(l4);
        
        t2 = new JTextField();
	t2.setBounds(150, 95, 150, 20);
	add(t2);
        
        l5 = new JLabel("Checkintime: ");
        l5.setBounds(10, 125, 100, 20);
        add(l5);
        
        t3 = new JTextField();
	t3.setBounds(150, 125, 150, 20);
	add(t3);
        
        l6 = new JLabel("Amount Paid: ");
        l6.setBounds(10, 155, 100, 20);
        add(l6);
        
        t4 = new JTextField();
	t4.setBounds(150, 155, 150, 20);
	add(t4);
        
        l6 = new JLabel("Amount Pending: ");
        l6.setBounds(10, 185, 130, 20);
        add(l6);
        
        t5 = new JTextField();
	t5.setBounds(150, 185, 150, 20);
	add(t5);
        
        b1 = new JButton("CHECK");
	b1.addActionListener(this);
	b1.setBounds(20, 225, 100, 33);
        b1.setBackground(Color.BLACK);
        b1.setForeground(Color.WHITE);
	add(b1);
        
        b2 = new JButton("UPDATE");
	b2.addActionListener(this);
	b2.setBounds(150, 225, 100, 33);
        b2.setBackground(Color.BLACK);
        b2.setForeground(Color.WHITE);
	add(b2);
        
        b3 = new JButton("Back");
	b3.addActionListener(this);
	b3.setBounds(90, 270, 100, 33);
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
                    t1.setText(rs.getString("name"));
                    t2.setText(rs.getString("room"));
                    t3.setText(rs.getString("checkintime"));
                    t4.setText(rs.getString("deposit"));
                }
                
                ResultSet rs2 = c.s.executeQuery("select * from room where roomNo = '"+t2.getText()+"'");
                while(rs2.next()){
                    String price = rs2.getString("price");
                    int amountPaid = Integer.parseInt(price) - Integer.parseInt(t4.getText());
                    t5.setText("" + amountPaid);
                }
            }catch(Exception e){
                System.out.println(e);
            }
				
        }else if(ae.getSource() == b2){
            String number = ch1.getSelectedItem();
            String name = t1.getText();
            String roomNo = t2.getText(); 
            String checkin = t3.getText(); 
            String deposit = t4.getText(); 
            try{
                Conn c = new Conn();
                c.s.executeUpdate("update customer set name = '"+name+"', room = '"+roomNo+"', checkintime = '"+checkin+"', deposit = '"+deposit+"' where number = '"+number+"'");
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
        new UpdateCheck();
    }
}
