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

public class Main {
    private static Charset targetCharset;
    private static List<String> suffixs = new ArrayList<String>();
    
    public static void main(String[] args) throws Throwable {
        System.out.println("请输入目标编码：");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        while (true) {
            try {
                targetCharset = Charset.forName(line);
                break;
            } catch (Exception e) {
                System.out.println("无法识别编码，请重新输入：");
                line = reader.readLine();
            }            
        }
        System.out.println("请输入需要转码文件后缀，多个以空格隔开，不过滤则直接回车：");
        line = reader.readLine();
        if (line != null && !line.isEmpty()) {
            String[] split = line.split(" ");
            for (String s : split) {
                if (!s.trim().isEmpty()) {
                    suffixs.add(s.trim());
                }
            }
        }
        System.out.println("请输入需要转码的文件所在路径：");
        line = reader.readLine();
        File file;
        while (true) {
            file = new File(line);
            if (file.exists()) {
                break;
            } else {
                System.out.println("路径不存在，请重新输入：");
                line = reader.readLine();
            }
        }
        reader.close();
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
        detector.add(new ParsingDetector(false));
        detector.add(JChardetFacade.getInstance());
        //ASCIIDetector用于ASCII编码测定  
        detector.add(ASCIIDetector.getInstance());
        //UnicodeDetector用于Unicode家族编码的测定  
        detector.add(UnicodeDetector.getInstance());
        convertToUtf8(file, detector);
        System.out.println("转码完成!");
    }
    
    private static void convertToUtf8(File file, CodepageDetectorProxy detector) throws Throwable {
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
                    File outFileTemp = new File(file.getParent(), file.getName() + "tmp");
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFileTemp), targetCharset));
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
                    file.delete();
                    outFileTemp.renameTo(file);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }
}
