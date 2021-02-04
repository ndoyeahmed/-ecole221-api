package com.ecole.school.services.utils;

import java.io.IOException;
import java.net.URL;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFEntetePied extends PdfPageEventHelper {
    private static final String FILE = "c:/util/FirstPdf.pdf";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static final Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    private PdfTemplate totalPages;
    protected PdfPTable table;
    protected PdfPTable tableFooter;
    protected float tableHeight;

    PdfPCell cellNum = new PdfPCell();

    public PDFEntetePied() throws BadElementException, IOException {
        float[] columnWidths = {3, 7, 3};
        table = new PdfPTable(columnWidths);
        table.setTotalWidth(523);
        table.setWidthPercentage(100);
        // table.setLockedWidth(true);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 8);
        PdfPCell cellOne = new PdfPCell(new Phrase("République du Sénégal", f));
        PdfPCell cellTwo = new PdfPCell(new Phrase("République du Sénégal\nMinistère de l'Economie des Finances et du Plan\nDirection Générale des Douanes\n Division de la Formation", f));
        cellOne.setBorder(Rectangle.BOTTOM);
        cellTwo.setBorder(Rectangle.BOTTOM);

//cellOne.setBackgroundColor(new Color(255,255,45));
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("img/logo_1.png");

        // String cheminLogo = url.getPath();
        Image i = Image.getInstance(url);
        //i.scaleAbsolute(10, 10);

        i.scalePercent(15);
        PdfPCell cellImg = new PdfPCell(i);
        cellImg.setBorder(Rectangle.BOTTOM);
        PdfPTable t1 = new PdfPTable(1);
        PdfPCell ct1 = new PdfPCell(i);
        ct1.setBorder(0);
        cellOne.setPaddingLeft(15);
        cellTwo.setPaddingLeft(50);
        cellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTwo.setVerticalAlignment(Element.ALIGN_BOTTOM);
        Font f1 = new Font(Font.FontFamily.TIMES_ROMAN, 7);
        PdfPCell slo = new PdfPCell(new Phrase("Un Peuble Un But Une Foi", f1));
        slo.setBorder(0);
        t1.addCell(ct1);
        t1.addCell(slo);
        cellOne.addElement(t1);
        table.addCell(cellOne);
        table.addCell(cellTwo);
        PdfPCell clvide = new PdfPCell();
        clvide.setBorder(Rectangle.BOTTOM);
        table.addCell(clvide);

        cellTwo.setColspan(2);
        tableHeight = table.getTotalHeight();
    }

    public float getTableHeight() {
        return tableHeight;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        table.writeSelectedRows(0, -1,
                document.left(),
                document.top() + ((document.topMargin() + tableHeight) / 2),
                writer.getDirectContent());
        footer(writer, document);
// tableFooter.writeSelectedRows(0, -1, 36, 55, writer.getDirectContent());
    }

    public void footer(PdfWriter writer, Document dc) {
        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 8);
        tableFooter = new PdfPTable(2);
        tableFooter.setTotalWidth(523);
        tableFooter.setLockedWidth(true);
        PdfPCell cellOne = new PdfPCell(new Phrase("Ecole des Douanes", f));

        cellOne.setBorder(Rectangle.BOTTOM);
        cellOne.setBorder(Rectangle.TOP);
        cellNum = new PdfPCell(new Phrase(String.valueOf(dc.getPageNumber())));
        cellNum.setBorder(Rectangle.TOP);
        cellNum.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tableFooter.addCell(cellOne);
        tableFooter.addCell(cellNum);

        tableFooter.writeSelectedRows(0, -1, 36, 36, writer.getDirectContent());

    }
}
