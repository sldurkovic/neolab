
package com.neolab.crm.server.reports;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.neolab.crm.shared.domain.Task;



public class Report {

    private static Font textFont = new Font (FontFactory.getFont("Verdana", 11));
    private static Font headerFont = new Font (FontFactory.getFont("Verdana", 13, Font.BOLD));

    
    
    public static void generate() {

		Report report = new Report();
		Rectangle pageSize = new Rectangle(PageSize.A4);

		Document document = new Document(pageSize);
		try {
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(new File("report.pdf")));
//			report.addMetaData(document, jTextField1.getText(),
//					jTextField2.getText());
			report.addHeader(writer, document);
			report.addFooter(writer, document);
			document.open();
			Paragraph title = new Paragraph("Test paragraph",
					new com.lowagie.text.Font(FontFactory.getFont("Verdana",
							18, Font.BOLD)));
			title.setAlignment(title.ALIGN_CENTER);
			document.add(title);
			document.add(new Chunk("\n"));
			document.add(new Paragraph("test paragraph"));
			document.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
    
    public static File parseTasks(ArrayList<Task> tasks, Date date){

		Report report = new Report();
		Rectangle pageSize = new Rectangle(PageSize.A4);
		Document document = new Document(pageSize);
		File file = new File("report.pdf");
		try {
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(file));
//			report.addMetaData(document, jTextField1.getText(),
//					jTextField2.getText());
			report.addHeader(writer, document);
			report.addFooter(writer, document);
			document.open();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat time = new SimpleDateFormat("H:mm");
			Paragraph title = new Paragraph(sdf.format(date),
					new com.lowagie.text.Font(FontFactory.getFont("Verdana",
							18, Font.BOLD)));
			title.setAlignment(title.ALIGN_CENTER);
			document.add(title);
			document.add(new Chunk("\n"));
			
			
			//add table
			PdfPTable table = new PdfPTable(3);

			table.setWidths(new float[]{70,15,15});
			for(Task t : tasks){
				PdfPCell cell = 
					new PdfPCell(new Paragraph(t.getTitle()));
				table.addCell(cell);
				PdfPCell start = 
					new PdfPCell(new Paragraph(time.format(t.getDateStart())));
//				table.addCell(start);
				PdfPCell end = 
					new PdfPCell(new Paragraph(time.format(t.getDateEnd())));
//				table.addCell(cell);
				table.addCell(start);
				table.addCell(end);
//				if(tasks.indexOf(t)%2!=0){
//					start.setBackgroundColor(Color.LIGHT_GRAY);
//					end.setBackgroundColor(Color.LIGHT_GRAY);
//					cell.setBackgroundColor(Color.LIGHT_GRAY);
//				}

			}
			
			
			document.add(table);
			document.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return file;
    }
    
    
    private String getDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void addMetaData(Document document, String title, String autor){
        document.addTitle(title);
	document.addSubject("Aktivnosti sa sastanka odrzanog "+getDateTime());
	document.addAuthor(autor);
    }


    private void addHeader(PdfWriter writer, Document document) throws Exception{
         com.lowagie.text.Image image = com.lowagie.text.Image.getInstance("../webapps/neolab/src/main/webapp/images/neologo.png");

         image.scaleAbsolute(200, 50);
        image.setAlignment(image.ALIGN_TOP);
        Table t = new Table(2, 1);//2 Columns and 1 row
        t.setWidth(100);

        t.setWidths(new int[]{10,10});
        t.setBorder(t.BOTTOM);

        //cell 1
        Cell c1 = new Cell(new Phrase(new Chunk("")));
        c1.addElement(image);
        c1.setVerticalAlignment(c1.ALIGN_CENTER);
        c1.setBorder(c1.NO_BORDER);
        t.addCell(c1);


        //cell 2
        String headerTxt = "Fakultet organizacionih nauka"+'\n'+"Beograd - Jove Ilica 154";

        Cell c2 = new Cell(new Phrase(headerTxt, headerFont));

        c2.setHorizontalAlignment(c2.ALIGN_RIGHT);
        c2.setBorder(c2.NO_BORDER);
        t.addCell(c2);

        Phrase p = new Phrase("");
        p.add(t);
        p.add("\n");
        HeaderFooter header = new HeaderFooter(p,false);
        
        header.setBorder(header.NO_BORDER);
        document.setHeader(header);
        
    }

    private void addFooter(PdfWriter writer, Document document) throws Exception{


        HeaderFooter footer = new HeaderFooter(new Phrase("www.neolab.com"), false);

        footer.setAlignment(Phrase.ALIGN_CENTER);
        footer.setBorder(1);
        document.setFooter(footer);

    }


}
