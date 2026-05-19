package hotel.reservation.system;
import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.util.Date;
import java.awt.event.*; 

public class AddCustomer extends JFrame implements ActionListener{
    JLabel l1, l2, l3, l4, l5, l6, l7, l8, l9, l10;
    JTextField t1, t2, t3, t4;
    JComboBox c1;
    JButton b1, b2;
    JRadioButton r1, r2;
    ButtonGroup bg;
    Choice ch1;
    
    AddCustomer(){
        setLayout(null);
        
        ImageIcon i1  = new ImageIcon(ClassLoader.getSystemResource("icons/fifth.jpg"));
        Image i3 = i1.getImage().getScaledInstance(350, 350,Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(i3);
        JLabel l15 = new JLabel(i2);
        l15.setBounds(420,45,350,350);
        add(l15);
        
        l1 = new JLabel("NEW CUSTOMER FORM");
        l1.setFont(new Font("Raleway", Font.PLAIN, 15));
	l1.setBounds(150, 10, 300, 22);
	add(l1);
        
        l2 = new JLabel("ID :");
        l2.setFont(new Font("Raleway", Font.PLAIN, 15));
	l2.setBounds(50, 50, 200, 20);
	add(l2);
                
        c1 = new JComboBox(new String[] {"Passport", "Aadhar Card", "Voter Id", "Driving license"});
	c1.setBounds(240, 50, 150, 20);
	add(c1);
                
        l3= new JLabel("Number :");
        l3.setFont(new Font("Raleway", Font.PLAIN, 15));
	l3.setBounds(50, 90, 100, 20);
	add(l3);
                
        t1 = new JTextField();
	t1.setBounds(240, 90, 150, 20);
	add(t1);
		
	l4= new JLabel("Name :");
        l4.setFont(new Font("Raleway", Font.PLAIN, 15));
	l4.setBounds(50, 130, 100, 20);
	add(l4);
		
	t2 = new JTextField();
	t2.setBounds(240, 130, 150, 20);
	add(t2);

        l5= new JLabel("Gender :");
        l5.setFont(new Font("Raleway", Font.PLAIN, 15));
	l5.setBounds(50, 170, 100, 20);
	add(l5);
                
        r1 = new JRadioButton("Male");
        r1.setFont(new Font("Raleway", Font.BOLD, 14));
        r1.setBackground(Color.WHITE);
        r1.setBounds(240, 170, 80, 20);
        add(r1);
                
        r2 = new JRadioButton("Female");
        r2.setFont(new Font("Raleway", Font.BOLD, 14));
        r2.setBackground(Color.WHITE);
        r2.setBounds(320, 170, 100, 20);
	add(r2);
        
        bg = new ButtonGroup();
        bg.add(r1);
        bg.add(r2);
                
	l6 = new JLabel("Country :");
        l6.setFont(new Font("Raleway", Font.PLAIN, 15));
	l6.setBounds(50, 210, 100, 20);
	add(l6);
        
        t3 = new JTextField();
	t3.setBounds(240, 210, 150, 20);
	add(t3);
		
	l7= new JLabel("Allocated Room Number :");
        l7.setFont(new Font("Raleway", Font.PLAIN, 15));
	l7.setBounds(50, 250, 190, 20);
	add(l7);
        
        ch1 = new Choice();
        try{
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from room where available = 'Available'");
            while(rs.next()){
                ch1.add(rs.getString("roomNo"));
            }
        }catch(Exception e){
            System.out.println(e);
        }
        ch1.setBounds(240, 250, 150, 20);
        add(ch1);
        
        l8 = new JLabel("Checked-In :");
        l8.setFont(new Font("Raleway", Font.PLAIN, 15));
	l8.setBounds(50, 290, 200, 15);
	add(l8);
        
        Date date = new Date();
        l9 = new JLabel("" + date);
        l9.setFont(new Font("Raleway", Font.PLAIN, 15));
	l9.setBounds(190, 290, 250, 20);
	add(l9);
		
	l10= new JLabel("Deposit :");
        l10.setFont(new Font("Raleway", Font.PLAIN, 15));
	l10.setBounds(50, 330, 200, 15);
	add(l10);
        
	t4 = new JTextField();
	t4.setBounds(240, 330, 150, 20);
	add(t4);
        
        b1 = new JButton("Add Customer");
	b1.addActionListener(this);
	b1.setBounds(50, 365, 125, 33);
        b1.setBackground(Color.BLACK);
        b1.setForeground(Color.WHITE);
	add(b1);

	b2 = new JButton("Back");
	b2.addActionListener(this);
	b2.setBounds(240, 365, 150, 33);
        b2.setBackground(Color.BLACK);
        b2.setForeground(Color.WHITE);
	add(b2);
                
        getContentPane().setBackground(Color.WHITE);
        setSize(850, 450);
        setLocation(300, 200);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == b1){
            String id = (String) c1.getSelectedItem();
            String number =  t1.getText();
            String name =  t2.getText();
            
            String gender =  null;
            if(r1.isSelected()){
                gender = "Male";
            }else if(r2.isSelected()){
                gender = "Female";
            }
            
            String country =  t3.getText();
            String room =  ch1.getSelectedItem();
            String time =  l9.getText();
            String deposit =  t4.getText();
            
            try{
                Conn c = new Conn();
                String q1 = "insert into customer values('"+id+"','"+number+"','"+name+"','"+gender+"','"+country+"','"+room+"','"+time+"','"+deposit+"')";
                String q2 = "update room set available = 'Occupied' where roomNo = '"+room+"'";
                c.s.executeUpdate(q1);
                c.s.executeUpdate(q2);
	    			
	    	JOptionPane.showMessageDialog(null, "New Customer Added Successfully");
                new Reception().setVisible(true);
                setVisible(false);
            }catch (Exception e){
                System.out.println(e);
            }
            
        }else if(ae.getSource() == b2){
            new Reception().setVisible(true);
            setVisible(false);
        }
    }
    
    public static void main(String[] args){
        new AddCustomer();
    }
}
