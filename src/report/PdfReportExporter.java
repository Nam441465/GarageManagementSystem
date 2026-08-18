package report;

import util.PdfUtil;

public class PdfReportExporter implements ReportExporter {
    @Override
    public boolean export(String outputPath, String title, String content) {
        return PdfUtil.generatePdf(outputPath, title, content);
    }
}
