package com.accurascience.util;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * pdf导出相关工具类
 * @author zhuchaojie
 */
@Component
public class PdfUtil {
    /**
     * 生成pdf文件
     * @param json 解析结果
     * @param out 输出流
     */
    public void createPDF(JSONObject json, OutputStream out) {
        //字体设置，中文支持
        BaseFont bf;
        Font font2 = null;
        try {

            bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
                    BaseFont.NOT_EMBEDDED);//创建字体
            font2 = new Font(bf, 12, Font.BOLD);//标题
        } catch (Exception e) {
            e.printStackTrace();
        }
        Document document = new Document(PageSize.A4);
        try {
            //PdfWriter writer = PdfWriter.getInstance(document, out);
            document.addTitle("vcf report pdf");
            document.open();

            //遍历json
            for (String key: json.keySet()) {
                //标题
                document.add(new Paragraph(key, font2));
                //生成表格
                PdfPTable table = createTable(key, (JSONObject) json.get(key));
                document.add(table);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    /**
     * 生成表格
     * @param key 类型
     * @param data 数据
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    public static PdfPTable createTable(String key, JSONObject data) throws DocumentException, IOException{
        //字体设置
        BaseFont bf;
        Font font = null;
        Font font2 = null;
        try {

            bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
                    BaseFont.NOT_EMBEDDED);//创建字体
            font = new Font(bf, 10);//普通
            font2 = new Font(bf, 10, Font.BOLD);//普通
        } catch (Exception e) {
            e.printStackTrace();
        }
        PdfPTable table = null;//生成表格
        PdfPCell cell;
        int size = 15;
        //以行为单位
        int index = 0;
        for (String rowTitle: data.keySet()) {
            if(index==0){
                table = new PdfPTable(((JSONObject)data.get(rowTitle)).size());
                //标题
                for(String title: ((JSONObject) data.get(rowTitle)).keySet()){
                    cell = new PdfPCell(new Paragraph(title,font2));
                    cell.setFixedHeight(size);//设置高度
                    table.addCell(cell);
                }
            }
            for (String title: ((JSONObject)data.get(rowTitle)).keySet()) {
                cell = new PdfPCell(new Paragraph(((JSONObject)data.get(rowTitle)).get(title)+"",font));
                cell.setFixedHeight(size);//设置高度
                table.addCell(cell);
            }
            index++;

        }
        return table;
    }

    /**
     * test
     * @param args
     */
    public static void main(String[] args) {
        PdfUtil pu = new PdfUtil();
        //pu.createPDF("/Users/zhuchaojie/Downloads/test.pdf");
    }
}
