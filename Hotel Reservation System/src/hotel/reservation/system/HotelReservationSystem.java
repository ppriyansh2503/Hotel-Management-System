package hotel.reservation.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HotelReservationSystem extends JFrame implements ActionListener{
    JLabel l1;
    JButton b1;
    
    HotelReservationSystem(){
        
        setSize(1300, 565);
        setLocation(50, 100);
        setLayout(null);
        
        ImageIcon i1  = new ImageIcon(ClassLoader.getSystemResource("icons/first.jpg"));
        Image i3 = i1.getImage().getScaledInstance(1300, 565, Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(i3);
        l1 = new JLabel(i2);
        l1.setBounds(0, 0, 1300, 565);
        add(l1);
        
        JLabel text=new JLabel("HOTEL MANAGEMENT SYSTEM");
        text.setBounds(30,450,1000,100);
        text.setFont(new Font("serif",Font.PLAIN,50));
        text.setForeground(Color.red);
        l1.add(text);
            
        b1 = new JButton("Next");
        b1.setBackground(Color.WHITE);
        b1.setForeground(Color.BLACK);
        b1.setBounds(1100,480,100,35);
        b1.addActionListener(this);
        l1.add(b1);
        
        setVisible(true);
        while(true){
            text.setVisible(false);
            try{
                Thread.sleep(500);
            }catch(Exception e){} 
                text.setVisible(true);
            try{
                Thread.sleep(500);
            }catch(Exception e){}
        }
    }

    public void actionPerformed(ActionEvent ae){
        new Login().setVisible(true);
        this.setVisible(false);
    }
    
    public static void main(String[] args) {
       new HotelReservationSystem();
    }
    
}
