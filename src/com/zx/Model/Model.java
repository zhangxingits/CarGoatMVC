package com.zx.Model;

import java.awt.Font;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.experimental.chart.swt.ChartComposite;

import com.ibm.icu.math.BigDecimal;
import com.zx.View.View;

public class Model {

	View view;
	public void addView(View v){
		this.view=v;
	}
	//Ϊ��״ͼ׼�����ݼ�
    public static PieDataset createDataset(int curr, int cheng) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        double c=tongJi(curr, cheng);
        dataset.setValue("�ɹ���", c);
        dataset.setValue("ʧ����", 100-c);
        
        return dataset;  
    }
    //����ͼ��
    public static JFreeChart createChart(PieDataset dataset) {
        
        JFreeChart chart = ChartFactory.createPieChart(
            "��Ϸͳ��",  // chart title
            dataset,             // data
            true,               // include legend
            true,
            false
        );
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionOutlinesVisible(false);
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("��Ϸû��ʼ,��������ʾ��");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        return chart;
    }
    //�ж�������ַ����ǲ��ǺϷ�����
    public static boolean isNumeric(String str){ 
    	   Pattern pattern = Pattern.compile("[1-9][0-9]*"); 
    	   Matcher isNum = pattern.matcher(str);
    	   if( !isNum.matches() ){
    	       return false; 
    	   } 
    	   return true; 
    	}
    public static double tongJi(int c, int s) {
		BigDecimal b1=new BigDecimal(s);
		BigDecimal b2=new BigDecimal(c);
		BigDecimal b3=b1.divide(b2, 3 , BigDecimal.ROUND_HALF_UP);
		double d=b3.doubleValue()*100;
		BigDecimal b4=new BigDecimal(d);
		return b4.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
    
    public void indexChange() {
		if (view.widget.radioOther.getSelection()) {
			for (int i = 0; i < view.gameData.list.length; i++) {
				if (i!=view.gameData.userIndex && i!=view.gameData.openIndex) {
					view.gameData.userIndex=i;
					if (view.gameData.userIndex==view.gameData.sucIndex) {
						view.gameData.sucTimes++;
					}
				}
			}
		}else if (view.widget.radioNo.getSelection()) {
			if (view.gameData.userIndex==view.gameData.sucIndex) {
				view.gameData.sucTimes++;
			}
		}else if(view.widget.radioSuiji.getSelection()) {
			boolean f=true;
			while (f) {
				int user=view.gameData.random.nextInt();
				int z=(user>=0?user:(-user))%3;

				if (z!=view.gameData.openIndex) {
					view.gameData.userIndex=z;
					if (view.gameData.userIndex==view.gameData.sucIndex) {
						view.gameData.sucTimes++;
					}
					f=false;
				}
			}
		}
	}
	
	public void currentGame() {
		//��ʼ����Ϸ
		view.gameData.curTimes++; //������Ϸ������1
		for (int i = 0; i < view.gameData.list.length; i++) {
			view.gameData.list[i]=null;
		}
		int n=1;
		while (n<4) {
			int r=view.gameData.random.nextInt(3);
			if (null==view.gameData.list[r]) {
				if (n==1) {
					view.gameData.list[r]="Car";
					view.gameData.sucIndex=r; //��һ�β����������Ϊ����
				}else {
					view.gameData.list[r]="Goat";
				}
				n++;
			}
		}
		int user=view.gameData.random.nextInt();
		int z=(user>=0?user:(-user));
		view.gameData.userIndex=(z%3); //�����ߵ�ѡ��
		
		for (int i = 0; i < view.gameData.list.length; i++) {
			if (i!=view.gameData.userIndex && !"Car".equals(view.gameData.list[i])) {
				view.gameData.openIndex=i; //����һ��ɽ����
				break;
			}
		}
	}
	//ˢ�½��
	public void refreshResult() {
		int i=view.gameData.userIndex;
		int o=view.gameData.sucIndex;
		view.widget.chart = Model.createChart(Model.createDataset(view.gameData.curTimes, view.gameData.sucTimes));
		view.widget.frame.setChart(view.widget.chart);
		view.widget.frame.forceRedraw();
		view.widget.textXuanzhe.setText((++i)+"");
		view.widget.textXuanzhong.setText((++o)+"");
		view.widget.textGailv.setText(Model.tongJi(view.gameData.curTimes, view.gameData.sucTimes)+"%");
		view.widget.labelTimesStatus.setText("��Ϸ�ѽ���"+(view.gameData.curTimes)+"��,�ɹ�"+view.gameData.sucTimes+"��,��ʣ"+(view.gameData.totalTimes-view.gameData.curTimes)+"��");
	}
	
	public void refreshNotice() {
		int i=view.gameData.userIndex;
		int o=view.gameData.openIndex;
		view.widget.lblStatus.setText("�Ѿ�ѡ���"+(++i)+"���ţ���"+(++o)+"�����Ѵ�");
		view.widget.labelTimesStatus.setText("��Ϸ�ѽ���"+(view.gameData.curTimes)+"��,�ɹ�"+view.gameData.sucTimes+"��,��ʣ"+(view.gameData.totalTimes-view.gameData.curTimes)+"��");
	}
	
	//֪ͨView����
	public void notifyView() {
		view.widget.display = new Display();
		view.widget.shell = new Shell();
		view.widget.shell.setMinimumSize(new Point(144, 139));
		view.widget.shell.setToolTipText("");
		view.widget.shell.setModified(true);
		view.widget.shell.setSize(784, 500);
		view.widget.shell.setText("MyCarGoat");
		view.widget.shell.setLayout(view.widget.fl);
		
		view.widget.compositeLeft = new Composite(view.widget.shell, SWT.BORDER);
		view.widget.compositeLeft.setBounds(10, 10, 257, 136);
		
		view.widget.compositeBegin = new Composite(view.widget.shell, SWT.BORDER);
        view.widget.compositeBegin.setBounds(10, 152, 257, 299);
        view.widget.compositeBegin.setVisible(false);
        
        view.widget.lblStatus = new Label(view.widget.compositeBegin, SWT.NONE);
        view.widget.lblStatus.setBounds(10, 135, 207, 17);
        
		view.widget.lblnotice = new Label(view.widget.compositeLeft, SWT.NONE);
		view.widget.lblnotice.setBounds(10, 10, 108, 17);
		view.widget.lblnotice.setText("��������Ϸ������");
		
		view.widget.textTimes = new Text(view.widget.compositeLeft, SWT.BORDER);
		view.widget.textTimes.setBounds(39, 33, 179, 23);
		
		view.widget.btnOK = new Button(view.widget.compositeLeft, SWT.NONE);

		view.widget.btnOK.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				String textNum=view.widget.textTimes.getText();
				if (!textNum.isEmpty() && Model.isNumeric(textNum)) {
					view.gameData.totalTimes=Integer.parseInt(textNum); //������Ϸ����
//					compositeLeft.setVisible(false);
					view.widget.textTimes.setEnabled(false);
					view.widget.compositeBegin.setVisible(true);
					view.widget.btnOK.setEnabled(false);
					
					currentGame();
					refreshNotice();
					view.widget.shell.layout();
				}else {
					MessageDialog.openConfirm(view.widget.shell, "�� ʾ", "����ֵ���Ϸ������������룡");
					return;
				}
//				
			}
		});
		view.widget.btnOK.setBounds(138, 65, 80, 27);
		view.widget.btnOK.setText("ȷ��");

		view.widget.chart = Model.createChart(null);
//		view.chart = Model.createChart(Model.createDataset(view.gameData.curTimes+1, view.gameData.sucTimes));
		
		view.widget.frame = new ChartComposite(view.widget.shell, SWT.BORDER, view.widget.chart, true);
		view.widget.frame.setBounds(288, 10, 452, 441);
        
		
        view.widget.lblAgain = new Label(view.widget.compositeBegin, SWT.NONE);
        view.widget.lblAgain.setBounds(10, 10, 61, 17);
        view.widget.lblAgain.setText("�Ƿ���ѡ��");
        
        view.widget.radioNo = new Button(view.widget.compositeBegin, SWT.RADIO);
        view.widget.radioNo.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		refreshNotice();
        	}
        });
        view.widget.radioNo.setSelection(true);
        view.widget.radioNo.setBounds(31, 33, 97, 17);
        view.widget.radioNo.setText("������ѡ");
        
        view.widget.radioOther = new Button(view.widget.compositeBegin, SWT.RADIO);
        view.widget.radioOther.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		for (int i = 0; i < view.gameData.list.length; i++) {
    				if (i!=view.gameData.userIndex && i!=view.gameData.openIndex) {
    					view.gameData.userIndex=i;
    					refreshNotice();
    					break;
    				}
    			}
        	}
        });
        view.widget.radioOther.setBounds(31, 56, 97, 17);
        view.widget.radioOther.setText("ѡ����һ��");
        
        view.widget.radioSuiji = new Button(view.widget.compositeBegin, SWT.RADIO);
        view.widget.radioSuiji.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
    			boolean f=true;
    			while (f) {
    				int user=view.gameData.random.nextInt();
    				int z=(user>=0?user:(-user))%3;
    				if (z!=view.gameData.openIndex) {
    					view.gameData.userIndex=z;
    					f=false;
    				}
    			}
        		int i=view.gameData.userIndex;
				int o=view.gameData.openIndex;
				view.widget.lblStatus.setText("�Ѿ�ѡ���"+(++i)+"���ţ���"+(++o)+"�����Ѵ�");
        	}
        });
        view.widget.radioSuiji.setBounds(31, 79, 97, 17);
        view.widget.radioSuiji.setText("���ѡ��");
        
        
        view.widget.btnAgainGame = new Button(view.widget.compositeBegin, SWT.NONE);
        view.widget.btnAgainGame.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseDown(MouseEvent e) {
        		if ((view.gameData.totalTimes-view.gameData.curTimes)==0) {
					MessageDialog.openConfirm(view.widget.shell, "�� ʾ", "��Ϸ��������,лл!");
					view.widget.compositeBegin.setVisible(false);
					view.widget.textTimes.setEnabled(true);
					view.widget.textTimes.setText("");
					view.widget.btnOK.setEnabled(true);
					return;
				}
        		currentGame();
        		refreshNotice();
        		view.widget.textXuanzhe.setText("");
        		view.widget.textXuanzhong.setText("");
        		view.widget.radioNo.setSelection(true);
        		view.widget.radioOther.setSelection(false);
        		view.widget.radioSuiji.setSelection(false);
        		view.widget.btnOKGame.setEnabled(true);
        	}
        });
        view.widget.btnAgainGame.setBounds(10, 102, 80, 27);
        view.widget.btnAgainGame.setText("������Ϸ");
        
        view.widget.btnOKGame = new Button(view.widget.compositeBegin, SWT.NONE);
        view.widget.btnOKGame.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseDown(MouseEvent e) {
        		indexChange();
        		refreshResult();
        		view.widget.btnOKGame.setEnabled(false);
        	}
        });
        view.widget.btnOKGame.setBounds(137, 102, 80, 27);
        view.widget.btnOKGame.setText("ȷ��");
        
        
        view.widget.lblXuanzhong = new Label(view.widget.compositeBegin, SWT.NONE);
        view.widget.lblXuanzhong.setBounds(10, 161, 61, 17);
        view.widget.lblXuanzhong.setText("ѡ�н��Ϊ��");
        
        view.widget.textXuanzhong = new Text(view.widget.compositeBegin, SWT.BORDER);
        view.widget.textXuanzhong.setBounds(77, 158, 140, 23);
        
        view.widget.lblXuanzhe = new Label(view.widget.compositeBegin, SWT.NONE);
        view.widget.lblXuanzhe.setBounds(10, 190, 61, 17);
        view.widget.lblXuanzhe.setText("ѡ����Ϊ");
        
        view.widget.textXuanzhe = new Text(view.widget.compositeBegin, SWT.BORDER);
        view.widget.textXuanzhe.setBounds(76, 187, 141, 23);
        
        view.widget.labelTimesStatus = new Label(view.widget.compositeBegin, SWT.NONE);
        view.widget.labelTimesStatus.setBounds(10, 213, 207, 17);
        view.widget.labelTimesStatus.setText("��Ϸ�ѽ���0�Σ���ʣ30��");
        
        view.widget.labGailv = new Label(view.widget.compositeBegin, SWT.NONE);
        view.widget.labGailv.setBounds(10, 236, 61, 17);
        view.widget.labGailv.setText("ѡ�и��ʣ�");
        
        view.widget.textGailv = new Text(view.widget.compositeBegin, SWT.BORDER);
        view.widget.textGailv.setBounds(77, 236, 140, 23);
        
        view.widget.lblResultStatus = new Label(view.widget.compositeBegin, SWT.NONE);
       view.widget.lblResultStatus.setBounds(10, 268, 207, 17);
        view.widget.shell.pack();
		view.widget.shell.open();
		view.widget.shell.layout();
		while (!view.widget.shell.isDisposed()) {
			if (!view.widget.display.readAndDispatch()) {
				view.widget.display.sleep();
			}
		}
	}
	
}
