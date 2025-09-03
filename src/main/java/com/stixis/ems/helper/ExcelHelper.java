package com.stixis.ems.helper;

import com.stixis.ems.exceptions.InvalidFileFormatException;
import com.stixis.ems.model.Employee;
import com.stixis.ems.model.Role;
import com.stixis.ems.repository.DepartmentRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {


   public static String[] OUTPUTHEADERS={"employeeId","firstName","lastName","email","mobileNumber","dateOfbirth","dateOfJoining","department","role"};
   public static String[] INPUTHEADERS={"firstName","lastName","email","mobileNumber","dateOfbirth","dateOfJoining","department",};

   public static String SHEET_NAME="employee_data";

    //checking file type
    public static boolean checkExcelFormat(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            return true;
        } else {
            return false;
        }
    }

    //Converting Excel file to list
    public static List<Employee> convertExcelToList(InputStream is,DepartmentRepository departmentRepository) {
        List<Employee> list = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheet("data");
            int rowNumber = 0;
            Iterator<Row> iterator = sheet.iterator();
            while(iterator.hasNext()){
                Row row = iterator.next();
                if(rowNumber==0){
                    Iterator<Cell> headerCells= row.iterator();
                    int hid =0;
                    boolean validHeaders = true;
                    while (headerCells.hasNext() && hid < INPUTHEADERS.length) {
                        Cell cell = headerCells.next();

                        // Check if the header matches the expected header
                        if (!cell.getStringCellValue().equals(INPUTHEADERS[hid])) {
                            validHeaders = false;
                            break;
                        }

                        hid++;
                    }

                    if (!validHeaders || hid < INPUTHEADERS.length) {
                        // Handle the case where headers are invalid or missing
                        throw new InvalidFileFormatException("Invalid or missing headers in the file.");
                    }
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cells = row.iterator();
                int cid = 0;
                Employee employee=new Employee();
                while(cells.hasNext()){
                    Cell cell = cells.next();
                    switch(cid){

                        case 0:
                            employee.setFirstName(cell.getStringCellValue());
                            break;
                        case 1:
                            employee.setLastName(cell.getStringCellValue());
                            break;
                        case 2:
                            employee.setEmail(cell.getStringCellValue());
                            break;
                        case 3:
                            employee.setMobileNumber((long)cell.getNumericCellValue());
                            break;
                        case 4:
                            employee.setDateOfBirth(LocalDate.from(cell.getLocalDateTimeCellValue()));
                            break;
                        case 5:
                            employee.setDateOfJoining(LocalDate.from(cell.getLocalDateTimeCellValue()));
                            break;
                        case 6:
                            employee.setDepartment(departmentRepository.findById((long)cell.getNumericCellValue()).get());
                        default:break;
                    }
                    cid++;
                }
                employee.setRole(Role.USER);
                list.add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //converting list into Excel file
    public static ByteArrayInputStream convertListToExcel(List<Employee> list) throws IOException {
        //CREATE FILE
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try{
            Sheet sheet = workbook.createSheet(SHEET_NAME);
            //CREATE HEADER
            Row row = sheet.createRow(0);
            for(int i=0;i<OUTPUTHEADERS.length;i++){
                Cell cell = row.createCell(i);
                cell.setCellValue(OUTPUTHEADERS[i]);
            }
            //VALUE ROWS
            int rowNumber=1;
            for(Employee e:list){
                Row dataRow=sheet.createRow(rowNumber);
                rowNumber++;
                dataRow.createCell(0).setCellValue(e.getEmployeeId());
                dataRow.createCell(1).setCellValue(e.getFirstName());
                dataRow.createCell(2).setCellValue(e.getLastName());
                dataRow.createCell(3).setCellValue(e.getEmail());
                dataRow.createCell(4).setCellValue(e.getMobileNumber());
                dataRow.createCell(5).setCellValue(e.getDateOfBirth());
                dataRow.createCell(6).setCellValue(e.getDateOfJoining());
                if(e.getDepartment()!=null&&e.getDepartment().getDepartmentId()!=null){
                    dataRow.createCell(7).setCellValue(e.getDepartment().getDepartmentId());

                }else{
                    dataRow.createCell(7).setCellValue("");

                }
                dataRow.createCell(8).setCellValue(e.getRole().name());
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("couldn't import data into EXCEL");
            return null;
        }
        finally {
            workbook.close();
            out.close();
        }
    }
}
