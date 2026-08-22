package service.export;

import model.Invoice;

public interface InvoiceExporter {
    String export(Invoice invoice, String targetDirectory) throws Exception;
    String getFormatName();
}
