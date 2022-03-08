package loanAssistant;
import javax.swing.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
public class LoanAssistant {
	private boolean isValid(String str) {
		return str != null && str.matches("[-+]?\\d*\\.?\\d+");
	}
	LoanAssistant(){
		JFrame f=new JFrame("Loan Assistant");		
		JLabel bal=new JLabel("Loan Balance");
		JLabel rate=new JLabel("Interest Rate");
		JLabel no=new JLabel("Number of Payments");
		JLabel monthly=new JLabel("Monthly Payments");
		JLabel analysis=new JLabel("Loan Analysis:");		
		JButton exit=new JButton("Exit");
		JButton compute=new JButton();
		JButton next=new JButton("New Loan Analysis");
		JButton byMonth=new JButton("X");
		JButton byNo=new JButton("X");		
		JTextField loan=new JTextField();
		JTextField interest=new JTextField();
		JTextField payments=new JTextField();
		JTextField months=new JTextField();		
		JTextArea result=new JTextArea();		
		f.add(bal);f.add(rate);f.add(no);f.add(monthly);f.add(analysis);
		f.add(exit);f.add(compute);f.add(next);f.add(byMonth);f.add(byNo);
		f.add(loan);f.add(interest);f.add(payments);f.add(months);f.add(result);		
		f.setSize(650,250);
		bal.setBounds(30,20,170,20);
		rate.setBounds(30,50,170,20);
		no.setBounds(30,80,170,20);
		monthly.setBounds(30,110,170,20);
		analysis.setBounds(380,20,170,20);
		compute.setBounds(50,140,195,20);
		next.setBounds(70,170,150,20);
		exit.setBounds(450,180,56,20);
		byNo.setBounds(327,80,45,20);
		byMonth.setBounds(327,110,45,20);
		loan.setBounds(220,20,100,20);
		interest.setBounds(220,50,100,20);
		payments.setBounds(220,80,100,20);
		months.setBounds(220,110,100,20);
		result.setBounds(380,50,200,120);
		f.setLayout(null);
		f.setVisible(true);
		loan.setHorizontalAlignment(SwingConstants.RIGHT);
		interest.setHorizontalAlignment(SwingConstants.RIGHT);
		payments.setHorizontalAlignment(SwingConstants.RIGHT);
		months.setHorizontalAlignment(SwingConstants.RIGHT);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		compute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double balance,rat,month,mutiplier,last;
				int payment;
				if(!isValid(loan.getText())) {
					JOptionPane.showConfirmDialog(null,"Invalid or empty Loan Balance field \nPlease correct","Balance Input Error",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
					loan.setText("");
					loan.requestFocus();
					return;
				}
				if(!isValid(interest.getText())) {
					JOptionPane.showConfirmDialog(null,"Invalid or empty Interest Rate field \nPlease correct","Interest Rate Input Error",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
					interest.setText("");
					interest.requestFocus();
					return;
				}
				if(byMonth.isVisible()&&!isValid(payments.getText())) {
					JOptionPane.showConfirmDialog(null,"Invalid or empty Payment No. field \nPlease correct","Payment Number Input Error",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
					payments.setText("");
					payments.requestFocus();
					return;
				}
				if(byNo.isVisible()&&!isValid(months.getText())) {
					JOptionPane.showConfirmDialog(null,"Invalid or empty Monthly Payment field \nPlease correct","Monthly Payment Input Error",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
					months.setText("");
					months.requestFocus();
					return;
				}
				balance=Double.valueOf(loan.getText()).doubleValue();
				rat=Double.valueOf(interest.getText()).doubleValue();
				rat/=1200;
				last=balance;
				if(byMonth.isVisible()) {
					payment=Integer.valueOf(payments.getText()).intValue();
					if(payment>balance||payment==0) {
						JOptionPane.showConfirmDialog(null,"Invalid No. of Payment field \nPlease provide proper value","Number of Payment Input Error",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
						payments.setText("");
						payments.requestFocus();
						return;
					}
					if(rat==0) {
						month=balance/payment;
						months.setText(new DecimalFormat("0.00").format(month));
					}
					else {
					mutiplier=Math.pow(1+rat,payment);
					month=balance*rat*mutiplier/(mutiplier-1);
					months.setText(new DecimalFormat("0.00").format(month));
					}
				}
				else {
					month=Double.valueOf(months.getText()).doubleValue();
					if(month>balance||month==0){
						JOptionPane.showConfirmDialog(null,"Invalid Monthly Payment field \nPlease provide proper value","Monthly Payment Input Error",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
						months.setText("");
						months.requestFocus();
						return;
					}
					if(month<=balance*rat+1.0) {
						if(JOptionPane.showConfirmDialog(null,"Minimum payment must be Rs. "+new DecimalFormat("0.00").format((int)(balance*rat+1.0))+"\nDo you want to use minimum payment?","Input Error",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION) {
							months.setText(new DecimalFormat("0.00").format((int)(balance*rat+1.0)));
							month=(int)(balance*rat+1.0);
						}
						else {
							JOptionPane.showConfirmDialog(null,"Enter Monthly Payment field again","Monthly Payment Input Error",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
							months.setText("");
							months.requestFocus();
							return;
						}
					}
					if(rat==0) payment=(int)(balance/month);
					else  payment=(int)((Math.log(month)-Math.log(month-balance*rat))/Math.log(1+rat));
					for(int i=1;i<=payment-1;i++) last+=last*rat-month;
					if(last>month) {
						last+=last*rat-month;
						payment++;
					}
					payments.setText(String.valueOf(payment));
				}
				
				result.setText("Loan Balance : Rs. "+new DecimalFormat("0.00").format(balance));
				result.append("\n"+"Interest Rate : "+new DecimalFormat("0.00").format(rat*1200)+"%");
				result.append("\n\n"+String.valueOf(payment-1)+" Payments of Rs. "+new DecimalFormat("0.00").format(month));
				result.append("\n"+"Final Payment of Rs. "+new DecimalFormat("0.00").format(last));
				result.append("\n"+"Total Payment of Rs. "+new DecimalFormat("0.00").format((payment-1)*month+last));
				result.append("\n"+"Interest Paid : Rs. "+new DecimalFormat("0.00").format((payment-1)*month+last-balance));
				compute.setEnabled(false);
				next.setEnabled(true);
				next.requestFocus();
			}
		});
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(byMonth.isVisible()) months.setText("");
				else payments.setText("");
				result.setText("");
				next.setEnabled(false);
				compute.setEnabled(true);
				loan.requestFocus();
			}
		});
		byNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				byMonth.setVisible(true);
				byNo.setVisible(false);
				compute.setText("Compute Monthly Payments");
				months.setText("");
				payments.setEditable(true);
				months.setEditable(false);
				months.setBackground(Color.YELLOW);
				payments.setBackground(Color.WHITE);
				months.setFocusable(false);
				payments.setFocusable(true);
				if(loan.getText().equals("")) loan.requestFocus();
				else payments.requestFocus();
			}
		});
		byMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				byMonth.setVisible(false);
				byNo.setVisible(true);
				compute.setText("Compute No. of Payments");
				payments.setText("");
				payments.setEditable(false);
				months.setEditable(true);
				months.setBackground(Color.WHITE);
				payments.setBackground(Color.YELLOW);
				months.setFocusable(true);
				payments.setFocusable(false);
				if(loan.getText().equals("")) loan.requestFocus();
				else months.requestFocus();
			}
		});
		result.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		result.setFocusable(false);
		byNo.setFocusable(false);
		byMonth.setFocusable(false);
		compute.setFocusable(false);
		next.setFocusable(false);
		exit.setFocusable(false);
		byNo.doClick();
	}
	public static void main(String[] args) {
		new LoanAssistant();
	}
}



//OR


package com.suven.consultancy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Consumer_Loan_Assistant extends JFrame implements ActionListener {
    JLabel l1,l2,l3,l4,la;
    JTextField tf1,tf2,tf3,tf4;
    JButton b1,b2,x1,x2,exit;
    JTextArea ta;
    Font flabel,fbutton;
    Boolean tf3enabled=false,tf4enabled=true;

    Consumer_Loan_Assistant(){
        super("                                                                          Consumer Loan Assistant");

        ta=new JTextArea("");
        ta.setBounds(400,40,300,150);
        ta.setFont(new Font("Segoe Script",Font.PLAIN,14));
//        ta.setBackground(Color.YELLOW);
        ta.setForeground(Color.BLACK);
        ta.setEditable(false);
        ta.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(ta);

        flabel =new Font("Arial", Font.PLAIN,16);
        fbutton=new Font("SANS_SERIF",Font.BOLD,13);

        la = new JLabel("Loan Analysis: ");
        l1=new JLabel("Loan Balance");
        l2=new JLabel("Interest Rate");
        l3=new JLabel("Number of Payments");
        l4=new JLabel("Monthly Payment");

        tf1=new JTextField();
        tf2=new JTextField();
        tf3=new JTextField();
        tf4=new JTextField();

        b1=new JButton("Compute Monthly Payment");
        b2=new JButton("New Loan Analysis");

        setLayout(null);
        setSize(800,300);
//        getContentPane().setBackground(Color.RED);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        la.setBounds(400,0,150,50);
        la.setFont(flabel);
        add(la);

        l1.setBounds(20,0,100,50);
        l1.setFont(flabel);
        add(l1);

        l2.setBounds(20,30,150,50);
        l2.setFont(flabel);
        add(l2);

        l3.setBounds(20,60,150,50);
        l3.setFont(flabel);
        add(l3);

        l4.setBounds(20,90,150,50);
        l4.setFont(flabel);
        add(l4);

        b1.setBounds(50,140,250,30);
        b1.setFont(fbutton);
        add(b1);

        b2.setBounds(75,190,200,30);
        b2.setFont(fbutton);
        b2.setEnabled(false);
        add(b2);

        b1.addActionListener(this);
        b2.addActionListener(this);

        tf1.setBounds(170,15,100,20);
        tf1.setFont(flabel);
        tf1.setHorizontalAlignment(JTextField.RIGHT);
        add(tf1);

        tf2.setBounds(170,45,100,20);
        tf2.setHorizontalAlignment(JTextField.RIGHT);
        tf2.setFont(flabel);
        add(tf2);

        tf3.setBounds(170,80,100,20);
        tf3.setHorizontalAlignment(JTextField.RIGHT);
        tf3.setFont(flabel);
        add(tf3);

        tf4.setBounds(170,110,100,20);
        tf4.setHorizontalAlignment(JTextField.RIGHT);
        tf4.setFont(flabel);
        tf4.setEditable(false);
        tf4.setForeground(Color.BLACK);
        tf4.setBackground(Color.YELLOW);
        add(tf4);

        x1=new JButton("X");
        x1.setBounds(300,70,50,25);
        x1.setFont(fbutton);
        add(x1);

        x2=new JButton("X");
        x2.setBounds(300,110,50,25);
        x2.setFont(fbutton);
        add(x2);
        x2.setVisible(false);

        x1.addActionListener(this);
        x2.addActionListener(this);

        exit= new JButton("Exit");
        exit.setBounds(500,220,100,30);
        exit.setFont(fbutton);
        add(exit);
        exit.addActionListener(this);


    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==b1) {
            try {
                if(tf2.getText().equals("") || tf2.getText().equals("0")) {
                JOptionPane.showMessageDialog(null,"Interest Rate cannot be 0%");
                }
                if ((tf1.getText().equals("") || tf2.getText().equals("") || tf3.getText().equals("")) && (tf1.getText().equals("") || tf2.getText().equals("") || tf4.getText().equals(""))) {
                    JOptionPane.showMessageDialog(null, "Fill All The Required Details");
                }
                if (tf4.getText().equals("")) {
                    float A = Float.parseFloat(tf1.getText());
                    float i = Float.parseFloat(tf2.getText());
                    float n = Float.parseFloat(tf3.getText());
                    float I = i / 1200;
                    float P = (float) (I * A / (1 - Math.pow((1 + I), -n)));
                    tf4.setText(P + "");
                    String str="Loan Amount : $"+A+"\n";
                    str+="Interest Rate : "+I*1200+"%\n\n";
                    str+=n+" Payments of : $"+P;
                    ta.setText(str);
                }
                if (tf3.getText().equals("")) {
                    float A = Float.parseFloat(tf1.getText());
                    float i = Float.parseFloat(tf2.getText());
                    float P = Float.parseFloat(tf4.getText());
                    float I = i / 1200;
                    float n = (float) (-(Math.log10(1 - I * A / P)) / Math.log10(1 + I));
                    tf3.setText(n + "");
                    String str="Loan Amount : $"+A+"\n";
                    str+="Interest Rate : "+I*1200+"%\n\n";
                    str+=n+" Payments of : $"+P;
                    ta.setText(str);
                }
//                String As = ""+A;
                b1.setEnabled(false);
                b2.setEnabled(true);
            }catch (Exception ex){
                System.out.println(ex);
            }
        }
        if(e.getSource()==b2) {
            if (tf4enabled) {
                tf4.setText(null);
//            tf3.setText(null);
            }
            if(tf3enabled){
                tf3.setText(null);
            }
            b1.setEnabled(true);
            b2.setEnabled(false);
        }
        if(e.getSource()==x1){
            x1.setVisible(false);
            x2.setVisible(true);
            tf4.setEditable(true);
            tf3.setEditable(false);
            tf3.setBackground(Color.YELLOW);
            tf4.setBackground(Color.white);
            tf3enabled=true;
            tf4enabled=false;
            tf3.setText(null);

            b1.setEnabled(true);
            b2.setEnabled(false);
        }
        if(e.getSource()==x2){
            x1.setVisible(true);
            x2.setVisible(false);
            tf4.setEditable(false);
            tf3.setEditable(true);
            tf3.setBackground(Color.white);
            tf4.setBackground(Color.yellow);
            tf3enabled=false;
            tf4enabled=true;
            tf4.setText(null);

            b1.setEnabled(true);
            b2.setEnabled(false);
        }
        if(e.getSource()==exit){
            System.exit(0);
        }
    }
    public static void main(String[] args) {
         new Consumer_Loan_Assistant();
    }

}


