package com.fun.convert;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import com.alee.laf.WebLookAndFeel;

public class Panel {

	static JFrame frame;
	static JButton b1, b2, b3, b4;
	static JComboBox<String> comboBox1, comboBox2;
	static JProgressBar progressBar;
	static JList<File> jlist;
	static JTextArea textArea;

	private void initGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		// 创建及设置窗口
		frame = new JFrame("编码转换");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 700);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());
		frame.getContentPane().add(new topPanel(), BorderLayout.NORTH);
		frame.getContentPane().add(new bottomPanel(), BorderLayout.SOUTH);
		frame.getContentPane().add(new centerPanel(), BorderLayout.CENTER);
		// 显示窗口
		// frame.pack();
		frame.setVisible(true);
	}

	private class topPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		private topPanel() {
			b1 = new JButton("选择文件");
			b2 = new JButton("移除文件");
			b3 = new JButton("移除所有");
			b4 = new JButton("转换");
			String[] items = { "UTF-8", "UTF-16BE", "UTF-16LE", "windows-1252", "GBK", "GB2312", "GB18030",
					"US-ASCII" };
			String[] items2 = { "UTF-8", "GBK" };
			comboBox1 = new JComboBox<String>(items);
			comboBox1.setSelectedItem(items[4]);
			comboBox2 = new JComboBox<String>(items2);
			TitledBorder tb1 = BorderFactory.createTitledBorder("当前文件编码");
			TitledBorder tb2 = BorderFactory.createTitledBorder("转码后文件编码");
			tb1.setTitleJustification(TitledBorder.CENTER);
			tb2.setTitleJustification(TitledBorder.CENTER);
			comboBox1.setBorder(tb1);
			comboBox2.setBorder(tb2);
			JPanel jp1 = new JPanel();
			JPanel jp2 = new JPanel();
			jp1.setLayout(new GridLayout(1, 4, 30, 30));
			jp2.setLayout(new GridLayout(1, 2));
			jp1.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
			jp2.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
			jp1.add(b1);
			jp1.add(b2);
			jp1.add(b3);
			jp1.add(b4);
			jp2.add(comboBox1);
			jp2.add(comboBox2);
			setLayout(new GridLayout(2, 0));
			add(jp1);
			add(jp2);
			b2.setEnabled(false);
			b3.setEnabled(false);
			b4.setEnabled(false);
			b1.setActionCommand("selectFile");
			b2.setActionCommand("removeFile");
			b3.setActionCommand("removeAll");
			b4.setActionCommand("convert");
			ActionListener listener = Monitor.getMonitor();
			b1.addActionListener(listener);
			b2.addActionListener(listener);
			b3.addActionListener(listener);
			b4.addActionListener(listener);
			comboBox1.addActionListener((e) -> FileBean.setNowEncoding((String) Panel.comboBox1.getSelectedItem()));
			comboBox2.addActionListener((e) -> FileBean.setToEncoding((String) Panel.comboBox2.getSelectedItem()));

		}
	}

	private class centerPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private String str = null;

		void start() {
			textArea.setText("Loading");
			try {
				str = Convert.getFileContentFromCharset(jlist.getSelectedValue(), FileBean.getNowEncoding(),true);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			SwingUtilities.invokeLater(() -> {
				textArea.setText(str);
			});
		}

		private centerPanel() {
			setLayout(new GridLayout(1, 2));
			jlist = new JList<File>();
			textArea = new JTextArea();
			textArea.setFont(new FontUIResource("微软雅黑", 0, 14));
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);		
			JScrollPane sp1 = new JScrollPane(jlist);
			JScrollPane sp2 = new JScrollPane(textArea);
			sp1.setBorder(BorderFactory.createTitledBorder("文件列表"));
			sp2.setBorder(BorderFactory.createTitledBorder("以当前选择的编码预览"));
			add(sp1);
			add(sp2);

			// textArea.addMouseListener(new MouseListener() {
			// @Override
			// public void mouseReleased(MouseEvent e) {
			// // TODO Auto-generated method stub
			// }
			//
			// @Override
			// public void mousePressed(MouseEvent e) {
			// // TODO Auto-generated method stub
			// }
			//
			// @Override
			// public void mouseExited(MouseEvent e) {
			// // TODO Auto-generated method stub
			// }
			//
			// @Override
			// public void mouseEntered(MouseEvent e) {
			// // TODO Auto-generated method stub
			// }
			//
			// @Override
			// public void mouseClicked(MouseEvent e) {
			// // TODO Auto-generated method stub
			// jlist.clearSelection();
			//
			// }
			// });
			comboBox1.addActionListener((e) -> {
				jlist.clearSelection();
				progressBar.setString("wait excute");
			});

			jlist.addListSelectionListener((e) -> {
				Panel.b2.setEnabled(true);
				Panel.b3.setEnabled(true);
				new Thread(() -> {
					if (Check.ckCharSet(jlist.getSelectedValue()) == null) {
						textArea.setText("");
					} else if (!((String) comboBox1.getSelectedItem())
							.equals(Check.ckCharSet(jlist.getSelectedValue()))) {
						start();
						progressBar.setString("选中的文件不是当前选择的编码!  猜测编码为" + Check.ckCharSet(jlist.getSelectedValue()));
					} else {
						start();
					}
				}).start();

			});

		}
	}

	private class bottomPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		private bottomPanel() {
			setBorder(new EmptyBorder(5, 5, 5, 5));
			setLayout(new BorderLayout());
			progressBar = new JProgressBar();
			progressBar.setStringPainted(true);
			// new Thread() {
			// public void run() {
			// for (int i = 0; i <= 100; i++) {
			// try {
			// Thread.sleep(100);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			// progressBar.setValue(i);
			// }
			// progressBar.setString("升级完成！");
			// }
			// }.start();

			progressBar.setString("wait excute");
			add(progressBar, BorderLayout.CENTER);
		}
	}

	public static void main(String[] args) {
		// 显示应用 GUI
		new Thread(() -> {
			// do some things
			SwingUtilities.invokeLater(() -> {
				// update gui
				WebLookAndFeel.globalControlFont = new FontUIResource("微软雅黑", 0, 14);
				WebLookAndFeel.install();
				WebLookAndFeel.initializeManagers();
				Panel panel = new Panel();
				panel.initGUI();
			});
		}).start();
	}

}
