package report;

/** Exports a report to a concrete output format. */
public interface ReportExporter {
    boolean export(String outputPath, String title, String content);
}
