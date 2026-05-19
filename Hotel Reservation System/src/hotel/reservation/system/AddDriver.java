package hotel.reservation.system;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class AddDriver extends JFrame implements ActionListener{
    JLabel l1, l2, l3, l4, l5, l6, l7;
    JTextField t1, t2, t3, t4, t5;
    JComboBox c1, c2;
    JButton b1, b2;
    
    AddDriver(){
        setLayout(null);
        
        ImageIcon i1  = new ImageIcon(ClassLoader.getSystemResource("icons/eleven.jpg"));
        Image i3 = i1.getImage().getScaledInstance(400, 300,Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(i3);
        JLabel l15 = new JLabel(i2);
        l15.setBounds(400,30,400,300);
        add(l15);
        
        JLabel l10 = new JLabel("ADD DRIVERS");
        l10.setFont(new Font("Tahoma", Font.BOLD, 18));
	l10.setBounds(150, 20, 120, 22);
	add(l10);
        
	l1 = new JLabel("Name:");
	l1.setFont(new Font("Tahoma", Font.PLAIN, 14));
	l1.setBounds(60, 70, 150, 22);
	add(l1);
        
        t1 = new JTextField();
	t1.setBounds(200, 70, 156, 20);
	add(t1);
        
	l2 = new JLabel("Age:");
	l2.setFont(new Font("Tahoma", Font.PLAIN, 14));
	l2.setBounds(60, 110, 102, 22);
	add(l2);
        
        t2 = new JTextField();
	t2.setBounds(200, 110, 156, 20);
	add(t2);
        
	l3 = new JLabel("Gender:");
	l3.setFont(new Font("Tahoma", Font.PLAIN, 14));
	l3.setBounds(60, 150, 130, 22);
	add(l3);
        
        c1 = new JComboBox<>(new String[] { "Male", "Female" });
	c1.setBounds(200, 150, 154, 20);
	add(c1);

	l4 = new JLabel("Car Company:");
	l4.setFont(new Font("Tahoma", Font.PLAIN, 14));
	l4.setBounds(60, 190, 102, 22);
	add(l4);
        
        t3 = new JTextField();
	t3.setBounds(200, 190, 156, 20);
	add(t3);

        l5 = new JLabel("Car Model:");
	l5.setFont(new Font("Tahoma", Font.PLAIN, 14));
	l5.setBounds(60, 230, 102, 22);
	add(l5);
        
        t4 = new JTextField();
	t4.setBounds(200, 230, 156, 20);
	add(t4);
        
        l6 = new JLabel("Available:");
	l6.setFont(new Font("Tahoma", Font.PLAIN, 14));
	l6.setBounds(60, 270, 102, 22);
	add(l6);
        
        c2 = new JComboBox<>(new String[] { "Available", "Busy" });
	c2.setBounds(200, 270, 154, 20);
	add(c2);
        
        l7 = new JLabel("Location:");
	l7.setFont(new Font("Tahoma", Font.PLAIN, 14));
	l7.setBounds(60, 310, 102, 22);
	add(l7);
        
        t5 = new JTextField();
	t5.setBounds(200, 310, 156, 20);
	add(t5);

	b1 = new JButton("Add Driver");
	b1.addActionListener(this);
	b1.setBounds(60, 350, 125, 33);
        b1.setBackground(Color.BLACK);
        b1.setForeground(Color.WHITE);
	add(b1);

	b2 = new JButton("Cancle");
	b2.addActionListener(this);
	b2.setBounds(200, 350, 150, 33);
        b2.setBackground(Color.BLACK);
        b2.setForeground(Color.WHITE);
	add(b2);

        getContentPane().setBackground(Color.WHITE);
        setSize(850, 450);
        setLocation(300, 200);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae){
        try{
            if(ae.getSource() == b1){
                try{
                Conn c = new Conn();
                String name = t1.getText();
                String age = t2.getText();
                String gender = (String)c1.getSelectedItem();
                String company  = t3.getText();
                String brand = t4.getText();
                String available = (String)c2.getSelectedItem();
                String location = t5.getText();
                
                String str = "INSERT INTO driver values( '"+name+"', '"+age+"', '"+gender+"','"+company+"', '"+brand+"', '"+available+"','"+location+"')";
                c.s.executeUpdate(str);
		JOptionPane.showMessageDialog(null, "Driver Successfully Added");
                this.setVisible(false);
               
                }catch(Exception ee){
                    System.out.println(ee);
                }
            }
            else if(ae.getSource() == b2){
                this.setVisible(false);
            }
        }catch(Exception eee){
            System.out.println(eee);
        }
    }
    
    public static void main(String[] args){
        new AddDriver();
    }
}
