package hotel.reservation.system;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class AddRooms extends JFrame implements ActionListener{
    JLabel l1, l2, l3, l4, l5;
    JTextField t1, t2;
    JComboBox c1, c2, c3;
    JButton b1, b2;
    
    AddRooms(){
        setLayout(null);
        
        ImageIcon i1  = new ImageIcon(ClassLoader.getSystemResource("icons/twelve.jpg"));
        Image i3 = i1.getImage().getScaledInstance(400, 300,Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(i3);
        JLabel l15 = new JLabel(i2);
        l15.setBounds(400,30,400,300);
        add(l15);
        
        JLabel l10 = new JLabel("ADD ROOMS");
        l10.setFont(new Font("Tahoma", Font.BOLD, 18));
	l10.setBounds(150, 20, 120, 22);
	add(l10);
        
	l1 = new JLabel("Room Number:");
	l1.setFont(new Font("Tahoma", Font.PLAIN, 14));
	l1.setBounds(60, 70, 150, 22);
	add(l1);
        
        t1 = new JTextField();
	t1.setBounds(200, 70, 156, 20);
	add(t1);
        
	l2 = new JLabel("Availability:");
	l2.setFont(new Font("Tahoma", Font.PLAIN, 14));
	l2.setBounds(60, 110, 102, 22);
	add(l2);
        
        c1 = new JComboBox<>(new String[] { "Available", "Occupied" });
	c1.setBounds(200, 110, 154, 20);
	add(c1);
        
	l3 = new JLabel("Cleaning Status:");
	l3.setFont(new Font("Tahoma", Font.PLAIN, 14));
	l3.setBounds(60, 150, 130, 22);
	add(l3);
        
        c2 = new JComboBox<>(new String[] { "Cleaned", "Dirty" });
	c2.setBounds(200, 150, 154, 20);
	add(c2);

	l4 = new JLabel("Price:");
	l4.setFont(new Font("Tahoma", Font.PLAIN, 14));
	l4.setBounds(60, 190, 102, 22);
	add(l4);
        
        t2 = new JTextField();
	t2.setBounds(200, 190, 156, 20);
	add(t2);

        l5 = new JLabel("Bed Type:");
	l5.setFont(new Font("Tahoma", Font.PLAIN, 14));
	l5.setBounds(60, 230, 102, 22);
	add(l5);
        
        c3 = new JComboBox<>(new String[] { "Single Bed", "Double Bed"});
	c3.setBounds(200, 230, 154, 20);
	add(c3);

	b1 = new JButton("Add Room");
	b1.addActionListener(this);
	b1.setBounds(60, 290, 125, 33);
        b1.setBackground(Color.BLACK);
        b1.setForeground(Color.WHITE);
	add(b1);

	b2 = new JButton("Cancle");
	b2.addActionListener(this);
	b2.setBounds(200, 290, 150, 33);
        b2.setBackground(Color.BLACK);
        b2.setForeground(Color.WHITE);
	add(b2);

        getContentPane().setBackground(Color.WHITE);
        setSize(850, 400);
        setLocation(300, 200);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae){
        try{
            if(ae.getSource() == b1){
                try{
                Conn c = new Conn();
                String roomNo = t1.getText();
                String available = (String)c1.getSelectedItem();
                String status = (String)c2.getSelectedItem();
                String price  = t2.getText();
                String type = (String)c3.getSelectedItem();
                
                String str = "INSERT INTO room values( '"+roomNo+"', '"+available+"', '"+status+"','"+price+"', '"+type+"')";
                c.s.executeUpdate(str);
		JOptionPane.showMessageDialog(null, "Room Successfully Added");
                this.setVisible(false);
               
                }catch(Exception e){
                    System.out.println(e);
                }
            }
            else if(ae.getSource() == b2){
                this.setVisible(false);
            }
        }catch(Exception ee){
            System.out.println(ee);
        }
    }
     
    public static void main(String[] args){
        new AddRooms();
    }
}
