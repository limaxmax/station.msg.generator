package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.junit.Test;

public class ExcelUtil{
	public static HSSFWorkbook excelFile;
	/**
	 * 创建一个给定路径的excel文件，默认创建3个sheet（不创建会报错）
	 * @param filePath 文件存放路径
	 * @return 返回创建好的excel文件
	 * @throws FileNotFoundException 
	 */
	public HSSFWorkbook creatExcelFile(String filePath) throws FileNotFoundException{
		HSSFWorkbook wb = new HSSFWorkbook();
		File exampleFile=new File(filePath);
		if(!exampleFile.exists()){
			try {
				exampleFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileOutputStream os=new FileOutputStream(exampleFile);
		Sheet sheet1=wb.createSheet("sheet1");
		Sheet sheet2=wb.createSheet("sheet2");
		Sheet sheet3=wb.createSheet("sheet3");
		try {
			
			wb.write(os);
			os.flush();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wb;
		
	}
	@Test
	public void createExcelFielTest(){
		try {
			creatExcelFile("D:"+File.separator+"text.xls");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param sheetIndex 表单索引号
	 * @param colNum 单元格列数
	 * @param rowNum 单元格行数
	 * @param excelFilee 被读取的文件
	 * @return 已String类型返回单元格中的内容
	 */
	public String readCellContent(int sheetIndex,CellAddress ca,HSSFWorkbook excelFile){
		Sheet sourceSheet=null;
		Row newRow=null;
		int colNum=ca.getColumn();
		int rowNum=ca.getRow();
		Cell newCell=null;
		String cellValue=null;
		int sheetSum=excelFile.getNumberOfSheets();
		//判断是否存在对应的sheet
		if(sheetIndex<sheetSum){
			sourceSheet=excelFile.getSheetAt(sheetIndex);
		}else{
			return "ErrorMsg:sheet index out of bounds!";
		};
		newRow=sourceSheet.getRow(rowNum);
		if(newRow==null){
			return "";
		};
		newCell=newRow.getCell(colNum);
		if(newCell==null){
			return "";
		};
		switch (newCell.getCellType()){
		case 0:	//返回0表示数字，日期格式包含在数字格式中
			if(HSSFDateUtil.isCellDateFormatted(newCell)){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				cellValue=sdf.format(HSSFDateUtil.getJavaDate(newCell.
			             getNumericCellValue())).toString();
				break;
			}else{
				cellValue=String.valueOf(newCell.getNumericCellValue());
			}
			break;
		case 1: //返回1表示字符串
			cellValue=newCell.getStringCellValue();
			break;
		case 2: //返回2表示公式
			cellValue=newCell.getCellFormula()+"";
			break;
		case 3: //返回3表示空格
			cellValue="";
			break;
		case 4: //返回3表示boolean
			cellValue=newCell.getBooleanCellValue()+"";
			break;
		case 5: //返回5表示error
			cellValue="cell error";
			break;
		default:
			cellValue=""; //默认类型设置为空""
			break;
		}
		return cellValue;
	}
	@Test
	public void readCellContentTest(){
		try {
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("D:"+File.separator+"replay.xls"));
			System.out.println(readCellContent(4,new CellAddress("B2"),new HSSFWorkbook(fs)).replaceAll("\\.[0-9]*", ""));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param colNum 单元格列数
	 * @param rowNum 单元格行数
	 * @param content 写入单元格的内容
	 * @param excleFile 被写入的文件
	 */
	public void writeCellContent(int colNum,int rowNum,String content,HSSFWorkbook excelFile){
		
	}
}