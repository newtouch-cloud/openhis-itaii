package com.core.generator.controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.core.common.annotation.Log;
import com.core.common.core.controller.BaseController;
import com.core.common.core.domain.AjaxResult;
import com.core.common.core.page.TableDataInfo;
import com.core.common.core.text.Convert;
import com.core.common.enums.BusinessType;
import com.core.common.utils.SecurityUtils;
import com.core.common.utils.StringUtils;
import com.core.common.utils.sql.SqlUtil;
import com.core.generator.domain.GenTable;
import com.core.generator.domain.GenTableColumn;
import com.core.generator.service.IGenTableColumnService;
import com.core.generator.service.IGenTableService;

/**
 * 代码生成 操作处理
 * 
 * @author system
 */
@RestController
@RequestMapping("/tool/gen")
public class GenController extends BaseController {
    @Autowired
    private IGenTableService genTableService;

    @Autowired
    private IGenTableColumnService genTableColumnService;

    /**
     * 查询代码生成列表
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:list')")
    @GetMapping("/list")
    public TableDataInfo genList(GenTable genTable) {
        startPage();
        List<GenTable> list = genTableService.selectGenTableList(genTable);
        return getDataTable(list);
    }

    /**
     * 修改代码生成业务
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:query')")
    @GetMapping(value = "/{tableId}")
    public AjaxResult getInfo(@PathVariable Long tableId) {
        GenTable table = genTableService.selectGenTableById(tableId);
        List<GenTable> tables = genTableService.selectGenTableAll();
        List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(tableId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("info", table);
        map.put("rows", list);
        map.put("tables", tables);
        return success(map);
    }

    /**
     * 查询数据库列表
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:list')")
    @GetMapping("/db/list")
    public TableDataInfo dataList(GenTable genTable) {
        startPage();
        List<GenTable> list = genTableService.selectDbTableList(genTable);
        return getDataTable(list);
    }

    /**
     * 查询数据表字段列表
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:list')")
    @GetMapping(value = "/column/{tableId}")
    public TableDataInfo columnList(Long tableId) {
        TableDataInfo dataInfo = new TableDataInfo();
        List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(tableId);
        dataInfo.setRows(list);
        dataInfo.setTotal(list.size());
        return dataInfo;
    }

    /**
     * 导入表结构（保存）
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:import')")
    @Log(title = "代码生成", businessType = BusinessType.IMPORT)
    @PostMapping("/importTable")
    public AjaxResult importTableSave(String tables) {
        String[] tableNames = Convert.toStrArray(tables);
        // 查询表信息
        List<GenTable> tableList = genTableService.selectDbTableListByNames(tableNames);
        genTableService.importGenTable(tableList, SecurityUtils.getUsername());
        return success();
    }

    /**
     * 创建表结构（保存）
     */
    @PreAuthorize("@ss.hasRole('admin')")
    @Log(title = "创建表", businessType = BusinessType.OTHER)
    @PostMapping("/createTable")
    public AjaxResult createTableSave(String sql) {
        try {
            SqlUtil.filterKeyword(sql);
            List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, DbType.mysql);
            List<String> tableNames = new ArrayList<>();
            for (SQLStatement sqlStatement : sqlStatements) {
                if (sqlStatement instanceof MySqlCreateTableStatement) {
                    MySqlCreateTableStatement createTableStatement = (MySqlCreateTableStatement)sqlStatement;
                    if (genTableService.createTable(createTableStatement.toString())) {
                        String tableName = createTableStatement.getTableName().replaceAll("`", "");
                        tableNames.add(tableName);
                    }
                }
            }
            List<GenTable> tableList =
                genTableService.selectDbTableListByNames(tableNames.toArray(new String[tableNames.size()]));
            String operName = SecurityUtils.getUsername();
            genTableService.importGenTable(tableList, operName);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AjaxResult.error("创建表结构异常");
        }
    }

    /**
     * 修改保存代码生成业务
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:edit')")
    @Log(title = "代码生成", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult editSave(@Validated @RequestBody GenTable genTable) {
        genTableService.validateEdit(genTable);
        genTableService.updateGenTable(genTable);
        return success();
    }

    /**
     * 删除代码生成
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:remove')")
    @Log(title = "代码生成", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tableIds}")
    public AjaxResult remove(@PathVariable Long[] tableIds) {
        genTableService.deleteGenTableByIds(tableIds);
        return success();
    }

    /**
     * 预览代码
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:preview')")
    @GetMapping("/preview/{tableId}")
    public AjaxResult preview(@PathVariable("tableId") Long tableId) throws IOException {
        Map<String, String> dataMap = genTableService.previewCode(tableId);
        return success(dataMap);
    }

    /**
     * 生成代码（下载方式）
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:code')")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/download/{tableName}")
    public void download(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException {
        byte[] data = genTableService.downloadCode(tableName);
        genCode(response, data);
    }

    /**
     * 生成代码（自定义路径）
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:code')")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/genCode/{tableName}")
    public AjaxResult genCode(@PathVariable("tableName") String tableName) {
        genTableService.generatorCode(tableName);
        return success();
    }

    /**
     * 同步数据库
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:edit')")
    @Log(title = "代码生成", businessType = BusinessType.UPDATE)
    @GetMapping("/synchDb/{tableName}")
    public AjaxResult synchDb(@PathVariable("tableName") String tableName) {
        genTableService.synchDb(tableName);
        return success();
    }

    /**
     * 批量生成代码
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:code')")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/batchGenCode")
    public void batchGenCode(HttpServletResponse response, String tables) throws IOException {
        String[] tableNames = Convert.toStrArray(tables);
        byte[] data = genTableService.downloadCode(tableNames);
        genCode(response, data);
    }

    /**
     * 生成zip文件
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"core.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }

    /**
     * 导入表设计
     * 
     * @param file
     * @return
     */
    @PostMapping("/importTableDesign")
    public AjaxResult importTableDesign(MultipartFile file) {
        try {
            // 读取文件
            InputStream is = file.getInputStream();
            Workbook wb = WorkbookFactory.create(is);
            StringBuilder sb = new StringBuilder();

            // 遍历每个sheet页（每个表）
            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                sb.append("-- ----------------------------------------------------------------------------------\n");
                Sheet st = wb.getSheetAt(i);

                // 从第一行读取表名表注释
                Row row0 = st.getRow(0);// 表名
                String tableName = row0.getCell(4).toString();// 表名
                String tableComment = row0.getCell(1).toString();// 表注释

                // 【开头】CREATE TABLE "public".表名 (
                StringBuilder createTableSqlSb = new StringBuilder();
                createTableSqlSb.append("CREATE TABLE \"public\".");
                createTableSqlSb.append(tableName);
                createTableSqlSb.append(" (");
                StringBuilder columnCommentSqlSb = new StringBuilder();

                // 遍历每行字段
                for (int j = 3; j < st.getLastRowNum(); j++) {
                    Row rowj = st.getRow(j);
                    Cell columnNo = rowj.getCell(0);// 序号
                    Cell columnComment = rowj.getCell(1);// 字段注释
                    Cell columnName = rowj.getCell(2);// 字段名
                    Cell columnType = rowj.getCell(3);// 类型
                    Cell columnLength = rowj.getCell(4);// 长度
                    Cell columnNullable = rowj.getCell(6);// NULL允许
                    Cell columnDefaultValue = rowj.getCell(8);// 默认值
                    if (columnNo == null || StringUtils.isEmpty(columnNo.toString())) {
                        // 没有序号说明遍历完毕，终止循环
                        break;
                    }
                    createTableSqlSb.append("\n \"");
                    createTableSqlSb.append(columnName.toString());
                    createTableSqlSb.append("\" ");

                    // 主键行
                    if (j == 3) {
                        // int8 NOT NULL DEFAULT nextval('表名_id_seq'::regclass),
                        createTableSqlSb.append("int8 NOT NULL DEFAULT nextval('");
                        createTableSqlSb.append(tableName);
                        createTableSqlSb.append("_id_seq'::regclass),");

                        // 类型【bigint】
                    } else if ("bigint".equals(columnType.toString())) {
                        if (columnNullable == null || StringUtils.isEmpty(columnNullable.toString())) {
                            createTableSqlSb.append("int8 NOT NULL ");
                            if (columnDefaultValue == null || StringUtils.isEmpty(columnDefaultValue.toString())) {
                                // 【不允许NULL】【没有默认值】int8 NOT NULL ,
                                createTableSqlSb.append(",");
                            } else {
                                // 【不允许NULL】【有默认值】int8 NOT NULL DEFAULT 0,
                                createTableSqlSb.append("DEFAULT 0,");
                            }
                        } else {
                            // 【允许NULL】int8 NULL,
                            createTableSqlSb.append("int8 NULL,");
                        }

                        // 类型【integer】
                    } else if ("integer".equals(columnType.toString())) {
                        if (columnNullable == null || StringUtils.isEmpty(columnNullable.toString())) {
                            createTableSqlSb.append("int4 NOT NULL ");
                            if (columnDefaultValue == null || StringUtils.isEmpty(columnDefaultValue.toString())) {
                                // 【不允许NULL】【没有默认值】int4 NOT NULL ,
                                createTableSqlSb.append(",");
                            } else {
                                // 【不允许NULL】【有默认值】int4 NOT NULL DEFAULT 0,
                                createTableSqlSb.append("DEFAULT ");
                                createTableSqlSb.append(columnDefaultValue);
                                createTableSqlSb.append(",");
                            }
                        } else {
                            // 【允许NULL】int4 NULL,
                            createTableSqlSb.append("int4 NULL,");
                        }

                        // 类型【varchar】
                    } else if ("varchar".equals(columnType.toString())) {
                        createTableSqlSb.append("varchar(");
                        createTableSqlSb.append(columnLength.toString().split("\\.")[0]);
                        if (columnNullable == null || StringUtils.isEmpty(columnNullable.toString())) {
                            createTableSqlSb.append(") NOT NULL ");
                            if (columnDefaultValue == null) {
                                // 【不允许NULL】【没有默认值】varchar(?) NOT NULL ,
                                createTableSqlSb.append(",");
                            } else {
                                if (columnName.toString().contains("json")) {
                                    // 【不允许NULL】【有默认值】【是JSON字段】varchar(?) NOT NULL DEFAULT '{}'::character varying,
                                    createTableSqlSb.append("DEFAULT '{}'::character varying,");
                                } else {
                                    // 【不允许NULL】【有默认值】【不是JSON字段】varchar(?) NOT NULL DEFAULT ''::character varying,
                                    createTableSqlSb.append("DEFAULT ''::character varying,");
                                }
                            }
                        } else {
                            // 【允许NULL】varchar(?) NULL,
                            createTableSqlSb.append(") NULL,");
                        }

                        // 类型【char】
                    } else if ("char".equals(columnType.toString())) {
                        createTableSqlSb.append("char(");
                        createTableSqlSb.append(columnLength.toString().split("\\.")[0]);
                        if (columnNullable == null || StringUtils.isEmpty(columnNullable.toString())) {
                            createTableSqlSb.append(") NOT NULL ");
                            if (columnDefaultValue == null) {
                                // 【不允许NULL】【没有默认值】char(?) NOT NULL ,
                                createTableSqlSb.append(",");
                            } else {
                                // 【不允许NULL】【有默认值】char(?) NOT NULL DEFAULT ''::character varying,,
                                createTableSqlSb.append("DEFAULT ''::character varying,");
                            }
                        } else {
                            // 【允许NULL】char(?) NULL,
                            createTableSqlSb.append(") NULL,");
                        }

                        // 类型【decimal】
                    } else if ("decimal".equals(columnType.toString())) {
                        if (columnNullable == null || StringUtils.isEmpty(columnNullable.toString())) {
                            createTableSqlSb.append("numeric(20,6) NOT NULL ");
                            if (columnDefaultValue == null) {
                                // 【不允许NULL】【没有默认值】numeric(20,6) NOT NULL ,
                                createTableSqlSb.append(",");
                            } else {
                                // 【不允许NULL】【有默认值】numeric(20,6) NOT NULL DEFAULT 0,
                                createTableSqlSb.append("DEFAULT 0,");
                            }
                        } else {
                            // 【允许NULL】numeric(20,6) NULL,
                            createTableSqlSb.append("numeric(20,6) NULL,");
                        }

                        // 类型【timestamptz】
                    } else if ("timestamptz".equals(columnType.toString())) {
                        if (columnNullable == null || StringUtils.isEmpty(columnNullable.toString())) {
                            // 【不允许NULL】timestamptz(6) NOT NULL,
                            createTableSqlSb.append("timestamptz(6) NOT NULL,");
                        } else {
                            // 【允许NULL】timestamptz(6) NULL,
                            createTableSqlSb.append("timestamptz(6) NULL,");
                        }
                    }

                    // 各字段注释：COMMENT ON COLUMN "public"."表名"."字段名" IS '注释';
                    columnCommentSqlSb.append("COMMENT ON COLUMN \"public\".\"");
                    columnCommentSqlSb.append(tableName);
                    columnCommentSqlSb.append("\".\"");
                    columnCommentSqlSb.append(columnName);
                    columnCommentSqlSb.append("\" IS '");
                    columnCommentSqlSb.append(columnComment.toString());
                    columnCommentSqlSb.append("';\n");
                }

                // 拼接Sequence文
                String createSequenceSql = "create sequence public." + tableName
                    + "_id_seq increment 1 start 200 minvalue 1 maxvalue 99999999 cache 1;\n";
                // 拼接drop文
                String dropTableSql = "DROP TABLE IF EXISTS \"public\".\"" + tableName + "\";\n";
                // 拼接create文
                createTableSqlSb.deleteCharAt(createTableSqlSb.length() - 1);
                createTableSqlSb.append("\n);\n");
                String createTableSql = createTableSqlSb.toString();
                // 拼接Comment文
                columnCommentSqlSb.append("COMMENT ON TABLE \"public\".\"");
                columnCommentSqlSb.append(tableName);
                columnCommentSqlSb.append("\" IS '");
                columnCommentSqlSb.append(tableComment).append("';\n");
                String columnCommentSql = columnCommentSqlSb.toString();
                // 拼接primaryKey文
                String primaryKeySql = "ALTER TABLE \"public\".\"" + tableName + "\" ADD CONSTRAINT \"" + tableName
                    + "_pkey\" PRIMARY KEY (\"" + st.getRow(3).getCell(2).toString() + "\");\n";
                // 拼接nextval文
                String nextvalSql = "alter table public." + tableName + " alter column id set default nextval('public."
                    + tableName + "_id_seq');\n";
                // 全部拼在一起
                sb.append(createSequenceSql).append("\n").append(dropTableSql).append("\n").append(createTableSql)
                    .append("\n").append(columnCommentSql).append("\n").append(primaryKeySql).append("\n")
                    .append(nextvalSql).append("\n");
            }

            // 将所有表的执行SQL打印到文件中
            String str = sb.toString();
            String filePath = "D:\\example.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(str);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                is.close();
            }
            return success();
        } catch (Exception e) {
            return error();
        }
    }
}