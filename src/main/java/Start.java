import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.snowtide.PDF;
import com.snowtide.pdf.Document;
import com.snowtide.pdf.OutputTarget;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.odf.OpenDocumentParser;
import org.apache.tika.sax.BodyContentHandler;
import org.fit.pdfdom.PDFDomTree;

import java.io.*;

public class Start {
    public static void main(String[] args) throws Exception {
        //pdfToTextUsingPDFBox();
        //pdfToTextUsingPDFBoxExtended();
        //pdfToHtmlUsingPDFBox();
        //pdfToTextUsingIText();
        //pdfToTextUsingPDFxStream();
        //pdfToTextUsingTika();
        //pdfToTextUsingTikaLatest();
        pdfToTextUsingITextAnother();
    }

    public static void pdfToTextUsingPDFBox() throws Exception {
        try {
            String sourceFilePath = "./files/RCLGI023EN20210315_Broker_9.pdf";
            String targetFilePath = "./files/cash.txt";

            File file = new File(sourceFilePath);
            String parsedText;

            if (file.exists()) {
                PDFParser parser = new PDFParser(new org.apache.pdfbox.io.RandomAccessFile(file, "r"));
                parser.parse();

                COSDocument cosDoc = parser.getDocument();
                PDFTextStripper pdfStripper = new PDFTextStripper();
                pdfStripper.setSortByPosition(true);
                PDDocument pdDoc = new PDDocument(cosDoc);
                parsedText = pdfStripper.getText(pdDoc);

                PrintWriter pw = new PrintWriter(targetFilePath);
                pw.printf(parsedText);
                pw.close();
            } else {
                throw new Exception("File not found");
            }
        } catch (Exception ex) {
            throw new Exception("File not found");
        }
    }

    public static void pdfToTextUsingPDFBoxExtended() throws Exception {
        try {
            String sourceFilePath = "./files/RCLGI023EN20210315_Broker_9.pdf";
            String targetFilePath = "./files/cash.txt";

            File file = new File(sourceFilePath);
            String parsedText;

            if (file.exists()) {
                PDFParser parser = new PDFParser(new org.apache.pdfbox.io.RandomAccessFile(file, "r"));
                parser.parse();

                COSDocument cosDoc = parser.getDocument();
                PDFTextStripper pdfStripper = new LayoutTextStripper();
                pdfStripper.setSortByPosition(true);
                PDDocument pdDoc = new PDDocument(cosDoc);
                parsedText = pdfStripper.getText(pdDoc);

                PrintWriter pw = new PrintWriter(targetFilePath);
                pw.printf(parsedText);
                pw.close();
            } else {
                throw new Exception("File not found");
            }
        } catch (Exception ex) {
            throw new Exception("File not found");
        }
    }

    public static void pdfToHtmlUsingPDFBox() throws Exception {
        try {
            String sourceFilePath = "./files/RCLGI023EN20210315_Broker_9.pdf";
            String targetFilePath = "./files/cash.html";

            File file = new File(sourceFilePath);
            String parsedText;

            if (file.exists()) {
                PDDocument pdf = PDDocument.load(new File(sourceFilePath));
                Writer output = new PrintWriter(targetFilePath, "utf-8");
                new PDFDomTree().writeText(pdf, output);

                output.close();
            } else {
                throw new Exception("File not found");
            }
        } catch (Exception ex) {
            throw new Exception("File not found");
        }
    }

    public static void pdfToTextUsingIText() {
        String sourceFilePath = "./files/RCLGI023EN20210315_Broker_9.pdf";
        String targetFilePath = "./files/cash.txt";
        StringBuffer text = new StringBuffer();
        PdfReader reader;
        try {
            reader = new PdfReader(sourceFilePath);
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);
            PrintWriter out = new PrintWriter(new FileOutputStream(targetFilePath));
            TextExtractionStrategy strategy;

            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
                text.append(strategy.getResultantText());
            }

            out.println(text.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public static void pdfToTextUsingIText1() { // https://www.roseindia.net/tutorial/java/itext/convertpdfToTextFile.html
        try {
            String sourceFilePath = "./files/RCLGI023EN20210315_Broker_9.pdf";
            String targetFilePath = "./files/cash.txt";
            Document document = new Document();
            document.open();
            PdfReader reader = new PdfReader(sourceFilePath);
            PdfDictionary dictionary = reader.getPageN(1);
            PRIndirectReference reference = (PRIndirectReference)
                    dictionary.get(PdfName.CONTENTS);
            PRStream stream = (PRStream) PdfReader.getPdfObject(reference);
            byte[] bytes = PdfReader.getStreamBytes(stream);
            PRTokeniser tokenizer = new PRTokeniser(bytes);
            FileOutputStream fos=new FileOutputStream(targetFilePath);
            StringBuffer buffer = new StringBuffer();
            while (tokenizer.nextToken()) {
                if (tokenizer.getTokenType() == PRTokeniser.TokenType.STRING) {
                    buffer.append(tokenizer.getStringValue());
                }
            }
            String test=buffer.toString();
            StringReader stReader = new StringReader(test);
            int t;
            while((t=stReader.read())>0)
                fos.write(t);
            document.add(new Paragraph(".."));
            document.close();
        }
        catch (Exception e) {}
    }*/

    public static void pdfToTextUsingPDFxStream() throws IOException {
        String sourceFilePath = "./files/RCLGI023EN20210315_Broker_9.pdf";
        String targetFilePath = "./files/cash.txt";

        Document pdf = PDF.open(sourceFilePath);
        StringBuilder text = new StringBuilder(1024);
        pdf.pipe(new OutputTarget(text));
        pdf.close();

        PrintWriter pw = new PrintWriter(targetFilePath);
        pw.printf(text.toString());
        pw.close();
    }

    public static void pdfToTextUsingTika() throws Exception {
        try {
            String sourceFilePath = "./files/RCLGI023EN20210315_Broker_9.pdf";
            String targetFilePath = "./files/cash.txt";

            File file = new File(sourceFilePath);
            FileInputStream inputstream = new FileInputStream(file);

            Tika tika = new Tika();
            String fileContent = tika.parseToString(inputstream);

            PrintWriter pw = new PrintWriter(targetFilePath);
            pw.printf(fileContent);
            pw.close();
        } catch (Exception ex) {
            throw new Exception("File not found");
        }
    }

    public static void pdfToTextUsingTikaLatest() throws Exception {
        try {
            String sourceFilePath = "./files/RCLGI023EN20210315_Broker_9.pdf";
            String targetFilePath = "./files/cash.txt";

            BodyContentHandler handler = new BodyContentHandler(-1);
            Metadata metadata = new Metadata();
            InputStream inputStream = new FileInputStream(new File(sourceFilePath));
            ParseContext context = new ParseContext();

            OpenDocumentParser pdfParser = new OpenDocumentParser();
            pdfParser.parse(inputStream, handler, metadata, context);

            PrintWriter pw = new PrintWriter(targetFilePath);
            pw.printf(handler.toString());
            pw.close();
        } catch (Exception ex) {
            throw new Exception("File not found");
        }
    }

    public static void pdfToTextUsingITextAnother() throws Exception {
        try {
            String cashSourceFilePath = "./files/RCLGI023EN20210315_Broker_9.pdf";
            String holdingSourceFilePath = "./files/RCLGI020EN20211213984944.pdf";
            String targetFilePath = "./files/cash.txt";

            PdfReader reader = new PdfReader(cashSourceFilePath);
            String page = "";
            PrintWriter pw = new PrintWriter(targetFilePath);

            System.out.println("This PDF has " + reader.getNumberOfPages() + " pages.");
            System.out.println("This document tampered: " + reader.isTampered());
            System.out.println("This document encrypted: " + reader.isEncrypted());

            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                page = PdfTextExtractor.getTextFromPage(reader, i);
                pw.printf(page);
                pw.printf("\n");
            }

            pw.close();
        } catch (Exception ex) {
            throw new Exception("File not found");
        }
    }
}
