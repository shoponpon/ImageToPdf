import java.io.File;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.AWTException;
import java.util.Arrays;
import java.awt.PopupMenu;
import java.awt.MenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.exceptions.COSVisitorException;

public class ImageToPdf{
	public static final String inpath = "./imgfile"; 
	public static final String outpath = "./pdffile";
	public static final String DS_STORE = ".DS_Store";
	private TrayIcon icon;
	
	public static void main(String[] argv){
		ImageToPdf itp = new ImageToPdf();
		itp.run();
	}
	
	public ImageToPdf(){
		
	}
	
	void initTray(){
		Image image = null;
		try{
			File file = new File("trayicon.jpg");
			image = ImageIO.read(file);
		}catch(IOException e){
			System.err.println(e);
			System.err.println("trayicon.jpg is not found!");
			System.exit(1);
		}
		icon = new TrayIcon(image);
		PopupMenu menu = new PopupMenu();
		MenuItem item = new MenuItem("終了");
		item.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		try{
			SystemTray.getSystemTray().add(icon);
		}catch(AWTException e){
			System.err.println(e);
			System.exit(1);
		}
	}
	
	void printTray(String str){
		icon.displayMessage("ImageToPdf",str,MessageType.INFO);
	}
	
	public void run(){
		initTray();
		outputPDFs(getFolder());
		printTray("完了！");
		System.err.println("Complate!");
		try{
			Thread.sleep(5000);
		}catch(Exception e){
			System.err.println(e);
		}
		System.exit(0);
	}
	
	ArrayList<ImageFolder> getFolder(){
		printTray("読込中…");
		ArrayList<ImageFolder> list = new ArrayList<ImageFolder>();
		String[] folders = getfilesName(inpath);/*inpath内のフォルダ全て名前を取得*/
		String[] files;
		for(int i = 0;i<folders.length;i++){
			if(new File(inpath+"/"+folders[i]).isDirectory()){
				System.err.println("---file["+folders[i]+"]---");
				files = getfilesName(inpath+"/"+folders[i]);
				list.add(new ImageFolder(folders[i],files));
				for(int j = 0;j<files.length;j++){
					System.err.println(files[j]);
				}
			}
		}
		return list;
	}
	
	String[] getfilesName(String path){
		ArrayList<String> list = new ArrayList<String>();
		File dir = new File(path);
		File[] files = dir.listFiles();
		for(int i = 0;i<files.length;i++){
			list.add(files[i].getName());
		}
		return (String[])list.toArray(new String[0]);
	}
	
	void outputPDFs(ArrayList<ImageFolder> list){
		printTray("変換…");
		for(int i = 0;i<list.size();i++){
			try{
				ImageFolder imgf = list.get(i);
				PDDocument doc = makePDF(imgf);
				doc.save(outpath+"/"+imgf.getName()+".pdf");
				doc.close();
			}catch(IOException e){
				System.err.println(e);
			}catch(COSVisitorException e){
				System.err.println(e);
			}
		}
	}
	
	PDDocument makePDF(ImageFolder imgf){
		PDDocument doc = new PDDocument();
		String[] files = imgf.getFileList();
		System.err.println("---"+imgf.getName()+".pdf---");
		for(int  i = 0;i<files.length;i++){
			if(DS_STORE.equals(files[i]))continue;
			try{
			System.err.println("["+inpath+"/"+imgf.getName()+"/"+files[i]+"] add...");
			BufferedImage awtImage = ImageIO.read(new File(inpath+"/"+imgf.getName()+"/"+files[i]));
			PDXObjectImage ximage = new PDPixelMap(doc, awtImage);
			int y = ximage.getHeight();
			int x = ximage.getWidth();
			PDRectangle rec = new PDRectangle();
			rec.setUpperRightX(0);
			rec.setUpperRightY(0);
			rec.setLowerLeftX(x);
			rec.setLowerLeftY(y);
			PDPage page = new PDPage(rec);
			doc.addPage(page);
			PDPageContentStream contents = new PDPageContentStream(doc, page);
			contents.drawImage(ximage, 0, 0);
			contents.close();
			}catch(IOException e){
				System.err.println(e);
			}
		}
		return doc;
	}
}

class ImageFolder{
	private String name;
	private String[] file;
	
	public ImageFolder(String name,String[] file){
		this.name = name;
		Arrays.sort(file);
		this.file = file;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String[] getFileList(){
		return this.file;
	}
}