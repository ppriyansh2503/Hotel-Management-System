package hotel.reservation.system;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Reception extends JFrame implements ActionListener{
    JButton b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12;
    Reception(){
        setLayout(null);
        
        ImageIcon i1  = new ImageIcon(ClassLoader.getSystemResource("icons/fourth.jpg"));
        Image i3 = i1.getImage().getScaledInstance(500, 500,Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(i3);
        JLabel l1 = new JLabel(i2);
        l1.setBounds(250,30,500,470);
        add(l1);
                
        JLabel l2 = new JLabel("RECEPTION");
        l2.setFont(new Font("Tahoma", Font.BOLD, 18));
	l2.setBounds(200, 5, 120, 22);
	add(l2);
        
        b1 = new JButton("New Customer Form");
        b1.setBounds(10, 30, 200, 30);
        b1.setBackground(Color.BLACK);
        b1.setForeground(Color.WHITE);
        b1.addActionListener(this);
	add(b1);
        
        b2 = new JButton("Rooms");
        b2.setBounds(10, 70, 200, 30);
        b2.setBackground(Color.BLACK);
        b2.setForeground(Color.WHITE);
        b2.addActionListener(this);
	add(b2);
        
        b3 = new JButton("Department");
        b3.setBounds(10, 110, 200, 30);
        b3.setBackground(Color.BLACK);
        b3.setForeground(Color.WHITE);
        b3.addActionListener(this);
	add(b3);
        
        b4 = new JButton("All Employees");
        b4.setBounds(10, 150, 200, 30);
        b4.setBackground(Color.BLACK);
        b4.setForeground(Color.WHITE);
        b4.addActionListener(this);
	add(b4);
        
        b5 = new JButton("Customer Info");
        b5.setBounds(10, 190, 200, 30);
        b5.setBackground(Color.BLACK);
        b5.setForeground(Color.WHITE);
        b5.addActionListener(this);
	add(b5);
        
        b6 = new JButton("Manager Info");
        b6.setBounds(10, 230, 200, 30);
        b6.setBackground(Color.BLACK);
        b6.setForeground(Color.WHITE);
        b6.addActionListener(this);
	add(b6);
        
        b7 = new JButton("Checkout");
        b7.setBounds(10, 270, 200, 30);
        b7.setBackground(Color.BLACK);
        b7.setForeground(Color.WHITE);
        b7.addActionListener(this);
	add(b7);
        
        b8 = new JButton("Update Status");
        b8.setBounds(10, 310, 200, 30);
        b8.setBackground(Color.BLACK);
        b8.setForeground(Color.WHITE);
        b8.addActionListener(this);
	add(b8);
        
        b9 = new JButton("Update Room Status");
        b9.setBounds(10, 350, 200, 30);
        b9.setBackground(Color.BLACK);
        b9.setForeground(Color.WHITE);
        b9.addActionListener(this);
	add(b9);
        
        b10 = new JButton("Pickup Services");
        b10.setBounds(10, 390, 200, 30);
        b10.setBackground(Color.BLACK);
        b10.setForeground(Color.WHITE);
        b10.addActionListener(this);
	add(b10);
        
        b11 = new JButton("Search Room");
        b11.setBounds(10, 430, 200, 30);
        b11.setBackground(Color.BLACK);
        b11.setForeground(Color.WHITE);
        b11.addActionListener(this);
	add(b11);
        
        b12 = new JButton("Logout");
        b12.setBounds(10, 470, 200, 30);
        b12.setBackground(Color.BLACK);
        b12.setForeground(Color.WHITE);
        b12.addActionListener(this);
	add(b12);
        
        getContentPane().setBackground(Color.WHITE);
        setSize(850, 450);
        setLocation(280, 200);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == b1){
            setVisible(false);
            new AddCustomer();
        }else if(ae.getSource() == b2){
            setVisible(false);
            new Room();
        }else if(ae.getSource() == b3){
            setVisible(false);
            new Department();
        }else if(ae.getSource() == b4){
            setVisible(false);
            new EmployeeInfo();
        }else if(ae.getSource() == b5){
            setVisible(false);
            new CustomerInfo();
        }else if(ae.getSource() == b6){
            setVisible(false);
            new ManagerInfo();
        }else if(ae.getSource() == b7){
            setVisible(false);
            new Checkout();
        }else if(ae.getSource() == b8){
            setVisible(false);
            new UpdateCheck();
        }else if(ae.getSource() == b9){
            setVisible(false);
            new UpdateRoom();
        }else if(ae.getSource() == b10){
            setVisible(false);
            new PickUp();
        }else if(ae.getSource() == b11){
            setVisible(false);
            new SearchRoom();
        }else if(ae.getSource() == b12){
            setVisible(false);
            System.exit(0);
        }
    }
    
    public static void main(String[] args){
        new Reception();
    }
}
