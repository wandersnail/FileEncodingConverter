import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

public class ConvertControl {
	
	private Charset targetCharset;
    private List<String> suffixs;
    private File srcFloder;
    private File dstFolder;

	public void setTargetCharset(Charset targetCharset) {
		this.targetCharset = targetCharset;
	}

	public void setSuffixs(List<String> suffixs) {
		this.suffixs = suffixs;
	}

	public void setSrcFloder(File srcFloder) {
		this.srcFloder = srcFloder;
	}

	public void setDstFolder(File dstFolder) {
		this.dstFolder = dstFolder;
	}

	public void runConvertCharset() {
		
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
        detector.add(new ParsingDetector(false));
        detector.add(JChardetFacade.getInstance());
        //ASCIIDetector用于ASCII编码测定  
        detector.add(ASCIIDetector.getInstance());
        //UnicodeDetector用于Unicode家族编码的测定  
        detector.add(UnicodeDetector.getInstance());
        try {
			convertToUtf8(srcFloder, detector);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void convertToUtf8(File file, CodepageDetectorProxy detector) throws Throwable {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    convertToUtf8(f, detector);
                }
            }
        } else {
            boolean accept = false;
            for (String suffix : suffixs) {
                if (file.getName().toLowerCase().endsWith(suffix.toLowerCase())) {
                    accept = true;
                    break;
                }
            }  
            if (!accept) {
                return;
            }
            Charset charset = null;
            try {
                charset = detector.detectCodepage(file.toURI().toURL());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (charset == null) {
                System.out.println("编码未知，无法转码。\t文件：" + file.getAbsolutePath());
            } else if (targetCharset.equals(charset)) {
                System.out.println(String.format(Locale.CHINA, "%s -> %s, 无需转码。\t文件：%s", charset.name(), targetCharset.name(), file.getAbsolutePath()));
            } else {//不是UTF-8的就转码
                System.out.println(String.format(Locale.CHINA, "%s -> %s, 开始转码。\t文件：%s", charset.name(), targetCharset.name(), file.getAbsolutePath()));
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
                    String relatePath = file.getPath().substring(srcFloder.getPath().length());
                    File dstFile = new File(dstFolder, relatePath);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dstFile), targetCharset));
                    String line;
                    boolean first = true;
                    while ((line = reader.readLine()) != null) {
                        if (!first) {
                            writer.newLine();
                        }
                        first = false;
                        writer.write(line);                        
                    }
                    reader.close();
                    writer.close();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }
}
