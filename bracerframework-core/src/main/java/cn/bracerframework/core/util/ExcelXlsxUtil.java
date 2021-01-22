package cn.bracerframework.core.util;

import cn.bracerframework.core.annotation.excel.Excel;
import cn.bracerframework.core.exception.OperationException;
import cn.bracerframework.core.exception.SystemException;
import cn.hutool.core.convert.Convert;
import cn.hutool.poi.excel.RowUtil;
import cn.hutool.poi.excel.WorkbookUtil;
import cn.hutool.poi.excel.cell.CellUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Excel 操作工具<br>
 * 非线程安全<br>
 * 此工具依赖第三方 poi-ooxml 包
 *
 * @author Dracula
 */
public class ExcelXlsxUtil {

    /**
     * Excel 文档对象
     */
    private Workbook workbook;
    /**
     * 活动页下标
     */
    private int activeSheetIndex = 0;
    /**
     * 活动页
     */
    private Sheet activeSheet = null;
    /**
     * 总行数
     */
    private int total = 0;
    /**
     * 激活行
     */
    private int activeRowIndex = 0;
    /**
     * 表头样式
     */
    private CellStyle rowStyle = null;
    /**
     * 表头单元格样式
     */
    private CellStyle cellStyle = null;

    /**
     * 创建一个空 Excel 工具对象<br/>
     * 非线程安全<br>
     * 该类只支持 xlsx 文档
     */
    public ExcelXlsxUtil() {
        workbook = WorkbookUtil.createBook(true);
        switchSheet(0);
    }

    /**
     * 创建 Excel 工具对象<br/>
     * 非线程安全<br>
     * 该类只支持 xlsx 文档
     *
     * @param path Excel 文件路径
     */
    public ExcelXlsxUtil(String path) {
        this(new File(path));
    }

    /**
     * 创建 Excel 工具对象<br/>
     * 非线程安全<br>
     * 该类只支持 xlsx 文档
     *
     * @param file Excel 文件对象
     */
    public ExcelXlsxUtil(File file) {
        workbook = WorkbookUtil.createBook(file);
        switchSheet(0);
    }

    /**
     * 创建 Excel 工具对象<br/>
     * 非线程安全<br>
     * 该类只支持 xlsx 文档
     *
     * @param in Excel 文件流
     */
    public ExcelXlsxUtil(InputStream in) {
        workbook = WorkbookUtil.createBook(in);
        switchSheet(0);
    }

    /**
     * 切换 Sheet 页
     *
     * @param i Sheet 页下标，从0开始
     */
    public void switchSheet(int i) {
        resetLine();
        activeSheetIndex = i;
        activeSheet = WorkbookUtil.getOrCreateSheet(workbook, activeSheetIndex);
        //getLastRowNum获取的行数下表是从0开始
        total = activeSheet.getLastRowNum() + 1;
        if (total > 0) {
            rowStyle = RowUtil.getOrCreateRow(activeSheet, 0).getRowStyle();
            cellStyle = CellUtil.getOrCreateCell(RowUtil.getOrCreateRow(activeSheet, 0), 0).getCellStyle();
        }
    }

    /**
     * 切换 Sheet 页
     *
     * @param sheetName Sheet 名字
     */
    public void switchSheet(String sheetName) {
        resetLine();
        activeSheet = WorkbookUtil.getOrCreateSheet(workbook, sheetName);
        activeSheetIndex = workbook.getSheetIndex(activeSheet);
        //getLastRowNum获取的行数下表是从0开始
        total = activeSheet.getLastRowNum() + 1;
        if (total > 0) {
            rowStyle = RowUtil.getOrCreateRow(activeSheet, 0).getRowStyle();
            cellStyle = CellUtil.getOrCreateCell(RowUtil.getOrCreateRow(activeSheet, 0), 0).getCellStyle();
        }
    }

    /**
     * 下一个 Sheet 页
     *
     * @return 如果失败返回 false
     */
    public boolean nextSheet() {
        int i = activeSheetIndex + 1;
        if (i < workbook.getNumberOfSheets()) {
            switchSheet(i);
            return true;
        }
        return false;
    }

    /**
     * 重置 Sheet 页下标回到第一页
     */
    public void resetSheet() {
        switchSheet(0);
    }

    /**
     * 验证当前行是否为指定格式行
     *
     * @param columnFirst 第一列值
     * @return 是否为指定格式行
     */
    public boolean isSpecsRow(String columnFirst) {
        return StrUtil.equals(getStringValue(0), columnFirst);
    }

    /**
     * 验证当前行是否为指定标题行</br>
     * 应先调用 isSpecsRow 方法判断当前行是否为标题行
     *
     * @param columnsNames 标题行各列标题
     * @return 是否为指定标题行
     */
    public boolean checkSpecsRow(String[] columnsNames) {
        for (int i = 0; i < columnsNames.length; i++) {
            if (!StrUtil.equals(columnsNames[i], StrUtil.removeAllNonPrintable(getStringValue(i)))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return 是否还有数据
     */
    public boolean hasNext() {
        return total > activeRowIndex;
    }

    /**
     * @return 下一行数据
     */
    public List<String> nextLine() {
        Row row = activeSheet.getRow(activeRowIndex);

        List<String> rowData = new LinkedList<>();
        for (int i = 0, cellNum = row.getLastCellNum(); i < cellNum; i++) {
            rowData.add(getStringValue(i));
        }

        activeRowIndex++;
        return rowData;
    }

    /**
     * 循环数组元素作为 key，将对应下标列以 key -> value 的形式存储到 {@link Map} 中
     *
     * @param columnsKey 列对应变量名
     * @return 下一行数据
     */
    public Map<String, String> nextLine(String[] columnsKey) {
        if (columnsKey.length != activeSheet.getRow(activeRowIndex).getLastCellNum()) {
            throw new SystemException("获取指定行为 Map 对象失败，Key 长度与列数不匹配！");
        }
        Map<String, String> rowData = new HashMap<>(columnsKey.length);
        for (int i = 0; i < columnsKey.length; i++) {
            rowData.put(columnsKey[i], getStringValue(i));
        }

        activeRowIndex++;
        return rowData;
    }

    /**
     * 重置行数为第一行
     */
    public void resetLine() {
        activeRowIndex = 0;
    }

    /**
     * 读取 excel 数据为二维列表
     *
     * @param sheet 读取数据所在 Sheet
     * @return
     */
    public List<List<String>> read(int sheet) {
        this.switchSheet(sheet);

        List<List<String>> ls = new ArrayList<>();
        List<String> line;
        for (activeRowIndex = 0; activeRowIndex < total; activeRowIndex++) {
            line = new ArrayList<>();
            for (int i = 0, size = activeSheet.getRow(activeRowIndex).getLastCellNum(); i < size; i++) {
                String val = getStringValue(i);
                line.add(StrUtil.isEmpty(val) ? null : val);
            }
            ls.add(line);
        }
        return ls;
    }

    /**
     * 读取 excel 数据为指定类型对象列表<br/>
     * 适合第一行为表头的场景
     *
     * @param sheet      读取数据所在 Sheet
     * @param type       映射对象类型
     * @param haveHeader 是否存在表头，存在的话会进行校验
     * @return
     */
    public <T> List<T> read(int sheet, Class<T> type, boolean haveHeader) {
        this.switchSheet(sheet);
        List<Field> fields = ReflectUtil.getFields(type, Excel.class, Comparator.comparingInt(field -> field.getAnnotation(Excel.class).index()));
        String[] headerNames = getHeaderNames(type, fields);

        List<T> ls = new ArrayList<>();
        for (activeRowIndex = 0; activeRowIndex < total; activeRowIndex++) {
            if (haveHeader && activeRowIndex == 0) {
                if (!checkSpecsRow(headerNames)) {
                    throw new OperationException("Excel 文档表头不规范！");
                }
                continue;
            }

            ls.add(getActiveRowObj(type, fields));
        }

        return ls;
    }

    /**
     * 以表头分组读取 excel 数据为指定类型对象列表<br/>
     * 适合存在多行相同表头和不存在表头的场景
     *
     * @param sheet 读取数据所在 Sheet
     * @param type  映射对象类型
     * @return
     */
    public <T> List<List<T>> readHeaderGroup(int sheet, Class<T> type) {
        this.switchSheet(sheet);
        List<Field> fields = ReflectUtil.getFields(type, Excel.class, Comparator.comparingInt(field -> field.getAnnotation(Excel.class).index()));
        String[] headerNames = getHeaderNames(type, fields);

        List<List<T>> ls = new ArrayList<>();
        for (activeRowIndex = 0; activeRowIndex < total; activeRowIndex++) {
            if (activeRowIndex == 0 || isSpecsRow(headerNames[0])) {
                ls.add(new ArrayList<>());
                if (checkSpecsRow(headerNames)) {
                    continue;
                } else {
                    throw new OperationException("Excel 文档第" + (activeRowIndex + 1) + "行表头不规范！");
                }
            } else {
                ls.get(ls.size() - 1).add(getActiveRowObj(type, fields));
            }
        }
        return ls;
    }

    /**
     * 获取表头文本数组
     *
     * @param type   映射对象类型
     * @param fields 映射列字段
     * @param <T>
     * @return
     */
    private <T> String[] getHeaderNames(Class<T> type, List<Field> fields) {
        Set<Integer> indexs = new HashSet<>();
        for (Field field : fields) {
            indexs.add(field.getAnnotation(Excel.class).index());
        }

        if (indexs.size() != fields.size()) {
            throw new SystemException("Excel 与 " + type.getName() + " 映射关系 【index】 配置有误！");
        }
        CollectionUtil.clear(indexs);

        int size = fields.size();
        String[] headerNames = new String[size];
        for (int i = 0; i < size; i++) {
            headerNames[i] = fields.get(i).getAnnotation(Excel.class).headerName();
        }
        return headerNames;
    }

    /**
     * 获取激活行对象
     *
     * @param type   映射对象类型
     * @param fields 映射列字段
     * @param <T>
     * @return
     */
    private <T> T getActiveRowObj(Class<T> type, List<Field> fields) {
        T t = ReflectUtil.newInstance(type);
        Excel annoExcel;
        for (int i = 0, size = fields.size(); i < size; i++) {
            annoExcel = fields.get(i).getAnnotation(Excel.class);
            String val = getStringValue(i);
            if (annoExcel.required() && StrUtil.isEmpty(val)) {
                throw new OperationException("第" + (activeRowIndex + 1) + "行" + annoExcel.headerName() + "不能为空！");
            }
            if (val != null && annoExcel.trim()) {
                val = StrUtil.trim(val);
            }
            ReflectUtil.setFieldValue(t, fields.get(i), val);
        }
        return t;
    }

    /**
     * 以字符串形式获取当前激活行指定列数据
     *
     * @param index 指定列
     * @return 列数据
     */
    private String getStringValue(int index) {
        if (activeRowIndex < 0) {
            return null;
        }
        Row row = RowUtil.getOrCreateRow(activeSheet, activeRowIndex);
        if (row.getLastCellNum() < index) {
            return null;
        }
        Cell cell = CellUtil.getOrCreateCell(row, index);

        String result;
        switch (cell.getCellType()) {
            case STRING:
                result = cell.getStringCellValue();
                break;
            case NUMERIC:
                result = new DecimalFormat("#.##").format(cell.getNumericCellValue());
                break;
            case BOOLEAN:
                result = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                result = cell.getCellFormula();
                break;
            default:
                result = null;
                break;
        }
        if (StrUtil.isEmpty(result)) {
            result = null;
        }
        return result;
    }

    /**
     * 添加一行记录
     *
     * @param columns 一行数据
     */
    public void writeNext(List<String> columns) {
        Row row = RowUtil.getOrCreateRow(activeSheet, activeRowIndex);
        for (int i = 0, size = columns.size(); i < size; i++) {
            CellUtil.getOrCreateCell(row, i).setCellValue(columns.get(i));
        }
        activeRowIndex++;
    }

    /**
     * 写入二维列表到 excel 文档
     *
     * @param sheet 读取数据所在 Sheet
     * @param lses  二维列表
     */
    public void write(int sheet, List<List<String>> lses) {
        this.switchSheet(sheet);
        this.write(lses);
    }

    /**
     * 写入二维列表到 excel 文档
     *
     * @param sheetName 通过 Sheet 名字读取数据所在 Sheet
     * @param lses      二维列表
     */
    public void write(String sheetName, List<List<String>> lses) {
        this.switchSheet(sheetName);
        this.write(lses);
    }

    /**
     * 写入二维列表到 excel 文档
     *
     * @param lses 二维列表
     */
    private void write(List<List<String>> lses) {
        Row row;

        for (List<String> ls : lses) {
            row = RowUtil.getOrCreateRow(activeSheet, activeRowIndex);
            for (int i = 0, size = ls.size(); i < size; i++) {
                CellUtil.getOrCreateCell(row, i).setCellValue(ls.get(i));
            }
            ++activeRowIndex;
        }
    }

    /**
     * 将指定类型对象列表写入 excel<br/>
     * 适合第一行为表头的场景
     *
     * @param sheet      读取数据所在 Sheet
     * @param type       映射对象类型
     * @param objs       数据对象列表
     * @param haveHeader 是否存在表头，存在的话会进行校验
     * @param <T>        映射对象类型
     */
    public <T> void write(int sheet, Class<T> type, List<T> objs, boolean haveHeader) {
        this.switchSheet(sheet);
        this.write(type, objs, haveHeader);
    }

    /**
     * 将指定类型对象列表写入 excel<br/>
     * 适合第一行为表头的场景
     *
     * @param sheetName  通过 Sheet 名字读取数据所在 Sheet
     * @param type       映射对象类型
     * @param objs       数据对象列表
     * @param haveHeader 是否存在表头，存在的话会进行校验
     * @param <T>        映射对象类型
     */
    public <T> void write(String sheetName, Class<T> type, List<T> objs, boolean haveHeader) {
        this.switchSheet(sheetName);
        this.write(type, objs, haveHeader);
    }

    /**
     * 将指定类型对象列表写入 excel
     *
     * @param type       映射对象类型
     * @param objs       数据对象列表
     * @param haveHeader 是否存在表头，存在的话会进行校验
     * @param <T>        映射对象类型
     */
    private <T> void write(Class<T> type, List<T> objs, boolean haveHeader) {
        List<Field> fields = ReflectUtil.getFields(type, Excel.class, Comparator.comparingInt(field -> field.getAnnotation(Excel.class).index()));
        String[] headerNames = this.getHeaderNames(type, fields);

        if (haveHeader) {
            if (StrUtil.isEmpty(getStringValue(0))) {
                this.writeHeaderWithActiveRow(headerNames);
            } else {
                if (!checkSpecsRow((headerNames))) {
                    throw new SystemException("通过 Excel 模板写入数据失败，模板列头与配置不符！");
                }
            }
            ++activeRowIndex;
        }

        for (T obj : objs) {
            this.writeDataWithActiveRow(fields, obj);
            ++activeRowIndex;
        }
    }

    /**
     * 将指定类型对象列表以表头分组的形式写入 excel<br/>
     * 适合存在多行相同表头的场景
     *
     * @param sheet 读取数据所在 Sheet
     * @param type  映射对象类型
     * @param objs  数据对象列表
     * @param <T>   映射对象类型
     */
    public <T> void writeHeaderGroup(int sheet, Class<T> type, List<List<T>> objs) {
        this.switchSheet(sheet);
        this.writeHeaderGroup(type, objs);
    }

    /**
     * 将指定类型对象列表以表头分组的形式写入 excel<br/>
     * 适合存在多行相同表头的场景
     *
     * @param sheetName 通过 Sheet 名字读取数据所在 Sheet
     * @param type      映射对象类型
     * @param objs      数据对象列表
     * @param <T>       映射对象类型
     */
    public <T> void writeHeaderGroup(String sheetName, Class<T> type, List<List<T>> objs) {
        this.switchSheet(sheetName);
        this.writeHeaderGroup(type, objs);
    }

    /**
     * 将指定类型对象列表以表头分组的形式写入 excel
     *
     * @param type 映射对象类型
     * @param objs 数据对象列表
     * @param <T>  映射对象类型
     */
    private <T> void writeHeaderGroup(Class<T> type, List<List<T>> objs) {
        List<Field> fields = ReflectUtil.getFields(type, Excel.class, Comparator.comparingInt(field -> field.getAnnotation(Excel.class).index()));
        String[] headerNames = this.getHeaderNames(type, fields);

        if (!isEmpty() && !checkSpecsRow((headerNames))) {
            throw new SystemException("通过模板写入数据失败，模板列头与配置不符！");
        } else {
            this.writeHeaderWithActiveRow(headerNames);
        }

        for (int i = 0, sizeI = objs.size(); i < sizeI; i++) {
            for (int j = 0, sizeJ = objs.get(i).size(); j < sizeJ; j++) {
                ++activeRowIndex;
                this.writeDataWithActiveRow(fields, objs.get(i).get(j));
            }
            ++activeRowIndex;
            this.writeHeaderWithActiveRow(headerNames);
        }
    }

    /**
     * 为当前激活行写入表头
     *
     * @param headerNames 表头数组
     */
    private void writeHeaderWithActiveRow(String[] headerNames) {
        Row row = RowUtil.getOrCreateRow(activeSheet, activeRowIndex);
        row.setRowStyle(rowStyle);
        Cell cell;
        for (int i = 0; i < headerNames.length; i++) {
            cell = CellUtil.getOrCreateCell(row, i);
            cell.setCellValue(headerNames[i]);
            cell.setCellStyle(cellStyle);
        }
    }

    /**
     * 为当前激活行写入表头
     *
     * @param fields 对象字段列表
     * @param data   数据对象
     */
    private <T> void writeDataWithActiveRow(List<Field> fields, T data) {
        Row row = RowUtil.getOrCreateRow(activeSheet, activeRowIndex);
        for (int i = 0, size = fields.size(); i < size; i++) {
            CellUtil.getOrCreateCell(row, i).setCellValue(Convert.toStr(ReflectUtil.getFieldValue(data, fields.get(i))));
        }
    }

    /**
     * 输出文件流
     *
     * @param stream 输出流
     */
    public void out(OutputStream stream) {
        try {
            workbook.write(stream);
        } catch (IOException e) {
            throw new SystemException("输出文档数据流失败！", e);
        }
    }

    /**
     * 当前页是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return this.total <= 0;
    }

    /**
     * 更新当前 Sheet 的名字
     *
     * @param sheetName
     */
    public void setSheetName(String sheetName) {
        workbook.setSheetName(activeSheetIndex, sheetName);
    }

    /**
     * 删除指定 Sheet<br/>
     * 如果删除的页下标小于当前指向的页下标，则指向页下标向前移，例：Sheet1 -> Sheet0
     *
     * @param i Sheet 下标
     */
    public void removeSheet(int i) {
        workbook.removeSheetAt(i);
        if (activeSheetIndex > i) {
            activeSheetIndex--;
            switchSheet(activeSheetIndex);
        }
    }

    /**
     * 删除当前激活的 Sheet<br/>
     * 当前 Sheet 指向页面下标向前移，例：Sheet1 -> Sheet0
     */
    public void removeActiveSheet() {
        workbook.removeSheetAt(activeSheetIndex);
        if (activeSheetIndex > 0) {
            activeSheetIndex--;
        } else {
            activeSheetIndex = 0;
        }
        switchSheet(activeSheetIndex);
    }

}