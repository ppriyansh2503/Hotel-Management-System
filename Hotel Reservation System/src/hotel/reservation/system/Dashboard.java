package hotel.reservation.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JFrame implements ActionListener{
    JLabel l1;
    Dashboard(){
        setLayout(null);
        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/third.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1450, 650,Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2); 
	JLabel l3 = new JLabel(i3);
	l3.setBounds(0, 0, 1450, 650); 
        add(l3);
        
        l1 = new JLabel("THE TAJ GROUP WELCOMES YOU");
	l1.setForeground(Color.WHITE);
        l1.setFont(new Font("Tahoma", Font.PLAIN, 46));
	l1.setBounds(340, 60, 1000, 85);
	l3.add(l1);
        
        JMenuBar mb = new JMenuBar();
        mb.setBounds(0, 0, 1450, 30);
        l3.add(mb);
        
        JMenu hotel = new JMenu("HOTEL MANAGEMENT");
        hotel.setForeground(Color.RED);
	mb.add(hotel);
        
        JMenuItem reception = new JMenuItem("RECEPTION");
        reception.addActionListener(this);
	hotel.add(reception);
        
        JMenu admin = new JMenu("ADMIN");
        admin.setForeground(Color.BLUE);
	mb.add(admin);
        
        JMenuItem addEmployee = new JMenuItem("ADD EMPLOYEE");
        addEmployee.addActionListener(this);
	admin.add(addEmployee);
        
        JMenuItem addRooms = new JMenuItem("ADD ROOMS");
        addRooms.addActionListener(this);
	admin.add(addRooms);
        
        JMenuItem addDrivers = new JMenuItem("ADD DRIVERS");
        addDrivers.addActionListener(this);
	admin.add(addDrivers);
        
        setSize(1450,650);
	setVisible(true);
        getContentPane().setBackground(Color.WHITE);
    }
    
    public void actionPerformed(ActionEvent ae){
        if(ae.getActionCommand().equals("ADD EMPLOYEE")){
            new AddEmployee().setVisible(true);
        }else if(ae.getActionCommand().equals("ADD ROOMS")){
            new AddRooms().setVisible(true);
        }else if(ae.getActionCommand().equals("ADD DRIVERS")){
            new AddDriver().setVisible(true);
        }else if(ae.getActionCommand().equals("RECEPTION")){
            new Reception().setVisible(true);
        }
    }
    
    public static void main(String[] args){
        new Dashboard();
    }
}
