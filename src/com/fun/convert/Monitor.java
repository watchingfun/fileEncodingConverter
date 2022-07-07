package com.fun.convert;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;

public class Monitor implements ActionListener {
	static Monitor monitor;
	private static File currentf;
	static {
		monitor = new Monitor();		
		currentf = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath());
	}

	public static ActionListener getMonitor() {
		if (monitor != null)
			return monitor;
		else
			return new Monitor();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		switch (e.getActionCommand()) {
		case "selectFile":
			FileBean.setNowEncoding((String) Panel.comboBox1.getSelectedItem());
			FileBean.setToEncoding((String) Panel.comboBox2.getSelectedItem());
			
			JFileChooser fc = new JFileChooser(currentf);
			//fc.setCurrentDirectory(fc.getSelectedFile());
			fc.setMultiSelectionEnabled(true);
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int returnVal = fc.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				FileBean.setSelectFiles(fc.getSelectedFiles());
				if (FileBean.getSelectFiles() != null)
					Panel.b4.setEnabled(true);
				Panel.jlist.setListData(FileBean.getSelectFiles());
			} else
				Panel.textArea.append("\n取消打开");
			break;
		case "removeFile":
		    if(Panel.thread!=null&&Panel.thread.isAlive()){
		        Panel.thread.interrupt();
                Convert.isStop=true;
                Panel.resetGui();
            }
			List<File> selecedIndexs = Panel.jlist.getSelectedValuesList();
			// System.out.println(selecedIndexs);

			if (selecedIndexs.size() != 0) {
				Iterator<File> it = FileBean.getSelectArray().iterator();

				for (File f : selecedIndexs) {
					while (it.hasNext()) {
						// System.out.println(f.equals(it.next()));
						if (f.equals(it.next())) {
							it.remove();
							break;
						}
					}
				}
			}
			Panel.jlist.setListData(FileBean.getSelectFiles());
			if (FileBean.getSelectArray().size() == 0) {
				Panel.b2.setEnabled(false);
				Panel.b3.setEnabled(false);
			}
            Panel.resetGui();
			break;
		case "removeAll":
            if(Panel.thread!=null&&Panel.thread.isAlive()){
                Panel.thread.interrupt();
                Convert.isStop=true;
                Panel.resetGui();
            }
			FileBean.setSelectFiles(null);
			Panel.jlist.setListData(FileBean.getSelectFiles());
			Panel.b2.setEnabled(false);
			Panel.b3.setEnabled(false);
            Panel.resetGui();
			break;
		case "convert":
			int[] i = Convert.isRightEncoding((String) Panel.comboBox1.getSelectedItem(), FileBean.getSelectArray());
			if (i.length > 0) {
				Panel.jlist.setSelectedIndices(i);
                Panel.progressBar.setString("waiting...");
				Object[] options = { "确定", "取消" };
				int response = JOptionPane.showOptionDialog(Panel.frame,
						"注意！列表有文件不是当前编码（猜测），\n如果预览的是乱码请不要转换，转码有很大几率乱码，\n确认以当前编码转码吗？", "确认转码？", JOptionPane.YES_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (response == 0) {
					new Thread(() -> {
						start();

					}).start();
				} else if (response == 1) {
					Panel.textArea.setText("用户取消转码");
				}
			} else {
				Panel.jlist.clearSelection();
				Panel.progressBar.setString("waiting...");
				Object[] options = { "确定", "取消" };

				int response = JOptionPane.showOptionDialog(Panel.frame,
						"注意！软件猜测的文件编码不是100%准确，\n如果预览的是乱码请不要转换，转码有很大几率无法转码回来\n确认转码吗？", "确认转码？", JOptionPane.YES_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (response == 0) {
					System.out.println("您按下了第OK按钮 ");
					new Thread(() -> {
						start();
					}).start();
				} else if (response == 1) {
					Panel.textArea.setText("用户取消转码");
				}
			}
			break;
		}
	}


	private void start() {
		File[] f = FileBean.getSelectFiles();
		for (int i = 0; i < f.length; i++) {
			try {
				Convert.convert(f[i], FileBean.ToEncoding,
						Convert.getFileContentFromCharset(f[i], FileBean.nowEncoding, false));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
