package csteachingtips.csteachingtinder;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Created by olivia on 1/4/16.
 */
public class ExcelStuff {

    public ExcelStuff(){
    }

    public void test() throws IOException, WriteException
    {
        WorkbookSettings wbs = new WorkbookSettings();
        wbs.setLocale(new Locale("en", "EN" ) );
        WritableWorkbook workbook = Workbook.createWorkbook(new File("output.xls"), wbs);
        WritableSheet sheet = workbook.createSheet("First Sheet", 0);

        Label label = new Label(0, 2, "A label record");
        sheet.addCell(label);

        Number number = new Number(3, 4, 3.1459);
        sheet.addCell(number);


        workbook.write();
        workbook.close();


    }
}





