package com.itextpdf.samples.book.chapter03;

import com.itextpdf.basics.font.FontConstants;
import com.itextpdf.basics.font.Type1Font;
import com.itextpdf.canvas.PdfCanvas;
import com.itextpdf.core.font.PdfType1Font;
import com.itextpdf.basics.geom.Rectangle;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.model.Document;
import com.itextpdf.model.Property;
import com.itextpdf.model.element.Paragraph;
import com.itextpdf.model.layout.LayoutArea;
import com.itextpdf.model.renderer.DocumentRenderer;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

import java.io.FileOutputStream;
import java.util.List;

public class Listing_03_19_MovieColumns4 extends Listing_03_16_MovieColumns1 {

    public static final String DEST = "./target/test/resources/Listing_03_19_MovieColumns4.pdf";

    public static final Rectangle[] COLUMNS = {
            new Rectangle(36, 666, 260, 136), new Rectangle(110, 580, 190, 90),
            new Rectangle(36, 480, 260, 100) , new Rectangle(36, 390, 190, 90), new Rectangle(36, 36, 260, 350),
            new Rectangle(299, 480, 260, 322), new Rectangle(373, 390, 190, 90),
            new Rectangle(299, 250, 260, 136), new Rectangle(299, 150, 190, 100), new Rectangle(299, 36, 260, 110)
    };

    public static void main(String[] args) throws Exception {
        new Listing_03_19_MovieColumns4().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        //Initialize writer
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);

        //Initialize document
        final PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        normal = new PdfType1Font(pdfDoc, new Type1Font(FontConstants.HELVETICA, ""));
        bold = new PdfType1Font(pdfDoc, new Type1Font(FontConstants.HELVETICA_BOLD, ""));
        italic = new PdfType1Font(pdfDoc, new Type1Font(FontConstants.HELVETICA_OBLIQUE, ""));
        boldItalic = new PdfType1Font(pdfDoc, new Type1Font(FontConstants.HELVETICA_BOLDOBLIQUE, ""));

        doc.setProperty(Property.FONT, normal);

        doc.setRenderer(new DocumentRenderer(doc) {
            int nextAreaNumber = 0;
            int currentPageNumber;

            @Override
            public LayoutArea getNextArea() {
                if (nextAreaNumber % COLUMNS.length == 0) {
                    currentPageNumber = super.getNextArea().getPageNumber();
                    drawRectangles(new PdfCanvas(document.getPdfDocument().getLastPage()));
                }
                return (currentArea = new LayoutArea(currentPageNumber, COLUMNS[nextAreaNumber++ % COLUMNS.length].clone()));
            }
        });

        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        List<Movie> movies = PojoFactory.getMovies(connection);
        for (Movie movie : movies) {
            doc.add(createMovieInformation(movie));
        }

        doc.close();
    }

    public void drawRectangles(PdfCanvas canvas) {
        canvas.saveState();
        canvas.setFillColorGray(0.9f);
        canvas.rectangle(33, 592, 72, 72);
        canvas.rectangle(263, 406, 72, 72);
        canvas.rectangle(491, 168, 72, 72);
        canvas.fillStroke();
        canvas.restoreState();
    }

    @Override
    public Paragraph createMovieInformation(Movie movie) {
        return super.createMovieInformation(movie).
                setPaddingLeft(0).
                setFirstLineIndent(0);
    }
}