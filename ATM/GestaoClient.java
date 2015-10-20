import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;

import javax.swing.JOptionPane;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.event.EventListenerList;


public class GestaoClient {
	public static Gestao gestao; //objecto da interface
	public static String index; //indentificador unico do cliente
	public static void main(String args[]) {
		
        try {
				
				String name = "rmi://localhost:9013/GestaoService";  //nome do servico GestaoService
				gestao = (Gestao) Naming.lookup(name); //lookup ao servico
			

			} catch (Exception e) {
				System.err.println("FTP exception:");
				e.printStackTrace();
			}
		          JFrame frame = new JFrame("JFrame Example");
			        JPanel panel = new JPanel();
			        panel.setLayout(new FlowLayout());
			 
			        JButton novaConta = new JButton();
			        novaConta.setText("Cria nova conta");
			 
			        JButton apagaConta = new JButton();
			        apagaConta.setText("apaga a tua conta");
			 
			        JButton listaConta = new JButton();
			        listaConta.setText("Listar contas");
			 
			        JButton depositar = new JButton();
			        depositar.setText("deposita dinheiro");
			 
			        JButton levantar = new JButton();
			        levantar.setText("levanta dinheiro");
			 
			        panel.add(novaConta);
			        panel.add(apagaConta);
			        panel.add(listaConta);
			        panel.add(depositar);
			        panel.add(levantar);
			        
			        frame.add(panel);
			        frame.setSize(300, 300);
			        frame.setLocationRelativeTo(null);
			        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			        frame.setVisible(true);
			        novaConta.addActionListener(new ActionListener()
			        {
			        	  

						@Override
						public void actionPerformed(ActionEvent e) {
							final JFrame f = new JFrame("Nova Conta");

					        // Make a panel to hold the demo "form", then
					        // add it to the top of the frame's content pane
					        JPanel form = new JPanel();
							f.getContentPane().setLayout(new BorderLayout());
					        f.getContentPane().add(form, BorderLayout.NORTH);

					        // Set the form panel's layout to GridBagLayout
					        // and create a FormUtility to add things to it.
					        form.setLayout(new GridBagLayout());
					        FormUtility formUtility = new FormUtility();

					        // Add some sample fields
					        formUtility.addLabel("Name: ", form);
					        final JTextField titular=new JTextField();
					        formUtility.addLastField(titular, form);

					        formUtility.addLabel("Pin: ", form);
					        final JTextField pin=new JTextField();
					        formUtility.addLastField(pin, form);
					        
					        formUtility.addLabel("Quantia: ", form);
					        final JTextField qt=new JTextField();
					        formUtility.addLastField(qt, form);
					        
					        JButton ok = new JButton("Submit");
					        form.setBorder(new EmptyBorder(2, 2, 2, 2));
					        form.add(ok);

					        // Note that we don't use pack() here, since that
					        // may shrink the "last" column more than we want.
					        f.setSize(200, 200);
					        f.setVisible(true);
					        
					      
					        ok.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									Actor a=new Actor("abrir",pin.getText(),titular.getText(),Integer.parseInt(qt.getText()));
                                    
									//print response
                                    String result=a.getResult();
                                    if(!result.equals("Conta ja existente!")){
                                    	new JOptionPane().showMessageDialog(f,result);
                                    	GestaoClient.index=result.split(" ")[4];
                                    }
								}
					        });
						}
			        	});
			        apagaConta.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							final JFrame f = new JFrame("Apagar Conta");

					        // Make a panel to hold the demo "form", then
					        // add it to the top of the frame's content pane
					        JPanel form = new JPanel();
							f.getContentPane().setLayout(new BorderLayout());
					        f.getContentPane().add(form, BorderLayout.NORTH);

					        // Set the form panel's layout to GridBagLayout
					        // and create a FormUtility to add things to it.
					        form.setLayout(new GridBagLayout());
					        FormUtility formUtility = new FormUtility();

					        // Add some sample fields
					        formUtility.addLabel("Name: ", form);
					        final JTextField titular=new JTextField();
					        formUtility.addLastField(titular, form);

					        formUtility.addLabel("Pin: ", form);
					        final JTextField pin=new JTextField();
					        formUtility.addLastField(pin, form);
					        
					        
					        
					        JButton ok = new JButton("Submit");
					        form.setBorder(new EmptyBorder(2, 2, 2, 2));
					        form.add(ok);

					        // Note that we don't use pack() here, since that
					        // may shrink the "last" column more than we want.
					        f.setSize(200, 200);
					        f.setVisible(true);
					        ok.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									 Actor a=new Actor(pin.getText(),titular.getText());
			                            
			                            String result=a.getResult();
			                           
			                            new JOptionPane().showMessageDialog(f,result);
										
									
								}
					        });
						}
					});
			        listaConta.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							Actor a=new Actor();
                            
                            byte[] result=a.getList();
                            final JFrame frame1 = new JFrame("Lista de contas");
							JPanel panel = new JPanel();
					        try {
					        	String r[] = new String(result,"UTF-8").split("\n");
					        	panel.setLayout(new GridLayout(r.length,1));
					        	System.out.println(r.length);
					        	int j=0;
					        	for(String i : r){
					        		Label yolo= new Label(i);
									panel.add(yolo,j);
									j++;
					        	}
							} catch (HeadlessException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (UnsupportedEncodingException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
					        
					       
					        
					        frame1.add(panel);
					        frame1.setSize(300, 300);
					        frame1.setLocationRelativeTo(null);
					        frame1.setVisible(true);
                           
							
						}
					});
			        depositar.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							final JFrame f = new JFrame("levantar uma quantia da conta");

					        // Make a panel to hold the demo "form", then
					        // add it to the top of the frame's content pane
					        JPanel form = new JPanel();
							f.getContentPane().setLayout(new BorderLayout());
					        f.getContentPane().add(form, BorderLayout.NORTH);

					        // Set the form panel's layout to GridBagLayout
					        // and create a FormUtility to add things to it.
					        form.setLayout(new GridBagLayout());
					        FormUtility formUtility = new FormUtility();

					        // Add some sample fields
					        formUtility.addLabel("Name: ", form);
					        final JTextField titular=new JTextField();
					        formUtility.addLastField(titular, form);

					        formUtility.addLabel("Pin: ", form);
					        final JTextField pin=new JTextField();
					        formUtility.addLastField(pin, form);
					        
					        formUtility.addLabel("Quantia: ", form);
					        final JTextField qt=new JTextField();
					        formUtility.addLastField(qt, form);
					        
					        JButton ok = new JButton("Submit");
					        form.setBorder(new EmptyBorder(2, 2, 2, 2));
					        form.add(ok);

					        // Note that we don't use pack() here, since that
					        // may shrink the "last" column more than we want.
					        f.setSize(200, 200);
					        f.setVisible(true);
					        ok.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									Actor a=new Actor("depositar",pin.getText(),titular.getText(),Integer.parseInt(qt.getText()));
                                    
									//print response
                                    String result=a.getResult();
                                    new JOptionPane().showMessageDialog(f,result);
								}
					        });
							
						}
					});
			        levantar.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							final JFrame f = new JFrame("Nova Conta");

					        // Make a panel to hold the demo "form", then
					        // add it to the top of the frame's content pane
					        JPanel form = new JPanel();
							f.getContentPane().setLayout(new BorderLayout());
					        f.getContentPane().add(form, BorderLayout.NORTH);

					        // Set the form panel's layout to GridBagLayout
					        // and create a FormUtility to add things to it.
					        form.setLayout(new GridBagLayout());
					        FormUtility formUtility = new FormUtility();

					        // Add some sample fields
					        formUtility.addLabel("Name: ", form);
					        final JTextField titular=new JTextField();
					        formUtility.addLastField(titular, form);

					        formUtility.addLabel("Pin: ", form);
					        final JTextField pin=new JTextField();
					        formUtility.addLastField(pin, form);
					        
					        formUtility.addLabel("Quantia: ", form);
					        final JTextField qt=new JTextField();
					        formUtility.addLastField(qt, form);
					        
					        JButton ok = new JButton("Submit");
					        form.setBorder(new EmptyBorder(2, 2, 2, 2));
					        form.add(ok);

					        // Note that we don't use pack() here, since that
					        // may shrink the "last" column more than we want.
					        f.setSize(200, 200);
					        f.setVisible(true);
					        ok.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									Actor a=new Actor("levantar",pin.getText(),titular.getText(),Integer.parseInt(qt.getText()));
                                    
                                    String result=a.getResult();
                                   
                                    new JOptionPane().showMessageDialog(f,result);
                                    
									
								}
					        });
							
						}
					});

	}

	
}
/**
 * Necessario pois as interfaces graficas sao um conjunto de funcoes anonimas
 * @author 
 *
 */
class Actor{
	private String result;
	private byte[] list;
	public Actor(String op,String p,String t, int s){
		
		try {
			if(op.equals("abrir"))
				result =GestaoClient.gestao.abrirConta(GestaoClient.index, p, t, s);
			else if(op.equals("levantar"))
				result =GestaoClient.gestao.levantar(GestaoClient.index, p, t, s);
			else if(op.equals("depositar"))
				result =GestaoClient.gestao.depositar(GestaoClient.index, p, t, s);
			setResult(result);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public Actor(String p,String t){
		try {
			result=GestaoClient.gestao.fecharConta(GestaoClient.index, p, t);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setResult(result);
	}
	public Actor(){
			
			try {
				byte [] list=GestaoClient.gestao.listar();
				setList(list);
			}catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	private void setResult(String result) {
		this.result=result;
		
	}
	public String getResult(){
		return result;
	}
	/**
	 * @return the list
	 */
	public byte[] getList() {
		return list;
	}
	/**
	 * @param list the list to set
	 */
	public void setList(byte[] list) {
		this.list = list;
	}
	
}
