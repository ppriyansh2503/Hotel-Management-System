package hotel.reservation.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddEmployee extends JFrame implements ActionListener{
    JTextField t1, t2, t3, t4, t5, t6;
    JLabel l1, l2, l3, l4, l5, l6, l7, l8;
    JRadioButton r1, r2;
    JComboBox c1;
    JButton submit;
    ButtonGroup bg;
    
    AddEmployee(){
        setLayout(null);
        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/tenth.jpg"));
        Image i3 = i1.getImage().getScaledInstance(500, 500,Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(i3);
        JLabel image = new JLabel(i2);
        image.setBounds(380,60,480,410);
        add(image);

        setTitle("ADD EMPLOYEE DETAILS");
        
        l1 = new JLabel("NAME:");
        l1.setFont(new Font("Tahoma", Font.PLAIN, 17));
        l1.setBounds(60, 30, 150, 27);
        add(l1);
        
        t1 = new JTextField();
        t1.setBounds(150, 30, 150, 27);
        add(t1);
        
        l2 = new JLabel("AGE:");
        l2.setFont(new Font("Tahoma", Font.PLAIN, 17));
        l2.setBounds(60, 80, 150, 27);
        add(l2);
        
        t2 = new JTextField();
        t2.setBounds(150, 80, 150, 27);
        add(t2);
        
        l3 = new JLabel("GENDER:");
        l3.setFont(new Font("Tahoma", Font.PLAIN, 17));
        l3.setBounds(60, 130, 150, 27);
        add(l3);
        
        r1 = new JRadioButton("MALE");
        r1.setBackground(Color.WHITE);
        r1.setBounds(145, 130, 80, 27);
        add(r1);
	
        r2 = new JRadioButton("FEMALE");
        r2.setBackground(Color.WHITE);
        r2.setBounds(230, 130, 100, 27);
        add(r2);
        
        bg = new ButtonGroup();
        bg.add(r1);
        bg.add(r2);
            
        l4 = new JLabel("JOB:");
        l4.setFont(new Font("Tahoma", Font.PLAIN, 17));
        l4.setBounds(60, 180, 150, 27);
        add(l4);
        
        String course[] = {"Front Desk Clerks","Porters","Housekeeping","Kitchen Staff","Room Service","Waiter/Waitress","Manager","Accountant","Chef"};
        c1 = new JComboBox(course);
        c1.setBackground(Color.WHITE);
        c1.setBounds(150,180,150,30);
        add(c1);
        
        l5 = new JLabel("SALARY:");
        l5.setFont(new Font("Tahoma", Font.PLAIN, 17));
        l5.setBounds(60, 230, 150, 27);
        add(l5);
        
        t3 = new JTextField();
        t3.setBounds(150, 230, 150, 27);
        add(t3);
        
        l6 = new JLabel("PHONE:");
        l6.setFont(new Font("Tahoma", Font.PLAIN, 17));
        l6.setBounds(60, 280, 150, 27);
        add(l6);
        
        t4 = new JTextField();
        t4.setBounds(150, 280, 150, 27);
        add(t4);
        
        l7 = new JLabel("EMAIL:");
        l7.setFont(new Font("Tahoma", Font.PLAIN, 17));
        l7.setBounds(60, 330, 150, 27);
        add(l7);
        
        t5 = new JTextField();
        t5.setBounds(150, 330, 150, 27);
        add(t5);
        
        l8 = new JLabel("AADHAR:");
        l8.setFont(new Font("Tahoma", Font.PLAIN, 17));
        l8.setBounds(60, 380, 150, 27);
        add(l8);
        
        t6 = new JTextField();
        t6.setBounds(150, 380, 150, 27);
        add(t6);
        
        submit = new JButton("SUBMIT");
        submit.setBounds(150, 420, 150, 30);
        submit.setBackground(Color.BLACK);
        submit.setForeground(Color.WHITE);
        submit.addActionListener(this);
        add(submit);
        
        getContentPane().setForeground(Color.BLUE);
        getContentPane().setBackground(Color.WHITE);
        setSize(850, 550);
        setLocation(300, 190);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae){
        String name = t1.getText();
        String age = t2.getText();
        String salary = t3.getText();
        String phone = t4.getText();
        String email = t5.getText();
        String aadhar = t6.getText();
        
        if(aadhar.equals("")){
            JOptionPane.showMessageDialog(null,"fill all required records");
            return;
        }
        
        String gender = null;
        if(r1.isSelected()){
            gender = "male";
        }else if(r2.isSelected()){
            gender = "female";
        }
        
        String job = (String)c1.getSelectedItem();
        try {
            Conn c = new Conn();
            String str = "INSERT INTO employee values( '"+name+"', '"+age+"', '"+gender+"','"+job+"', '"+salary+"', '"+phone+"','"+email+"', '"+aadhar+"')";
            c.s.executeUpdate(str);
            JOptionPane.showMessageDialog(null,"Employee Added Successfully");
            setVisible(false);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static void main(String[] args){
        new AddEmployee();
    }
}
