package com.zx.POJO;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.jfree.chart.JFreeChart;
import org.jfree.experimental.chart.swt.ChartComposite;

public class Widget {
	public Text textTimes;
	public Text textXuanzhong;
	public Text textXuanzhe;
	public Text textGailv;
	public JFreeChart chart;
	public Label lblStatus;
	public ChartComposite frame;
	public Label labelTimesStatus;
	public Button btnOKGame;
	public Button btnAgainGame;
	public Label lblResultStatus;
	public Display display;
	public Shell shell;
	public Composite compositeLeft;
	public Composite compositeBegin;
	public Label lblnotice;
	public Button btnOK;
	public Label lblAgain;
	public Button radioNo;
	public Button radioOther;
	public Button radioSuiji;
	public Label lblXuanzhong;
	public Label lblXuanzhe;
	public Label labGailv;
	public FillLayout fl=new FillLayout(SWT.HORIZONTAL);
	public GridLayout gd=new GridLayout(2, false);
}
