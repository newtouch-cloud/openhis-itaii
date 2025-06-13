package com.openhis.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import com.openhis.utils.RedisUtil;
import com.openhis.web.ybmanage.dto.PDFInputDto;
import org.springframework.beans.factory.annotation.Autowired;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * PDF模板替换类
 *
 * @author liuhr
 * @date 2025/4/30
 */
public final class PDFTemplateFillerUtil {

    // "--"分隔处方结束
    public static final String DASH_LINE =
        "-------------------------------------------------------------------------------------------------------";

    /**
     * 根据模板pdf文件，替换生成新的pdf
     *
     * @param templatePath 模板pdf文件
     * @param outputPath 替换后的pdf文件
     * @param hospitalSealPath 医院盖章路径
     * @param dto 替换内容
     */
    public static void fillPdfTemplate(String templatePath, String outputPath, String hospitalSealPath, PDFInputDto dto)
        throws IOException, DocumentException {
        PdfReader reader = new PdfReader(templatePath);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputPath));

        // 设置字体类型和大小（需要提前注册字体）
        // 设置字体为宋体（SimSun）
        BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        stamper.getAcroFields().setFieldProperty("$HOS_NAME", "textfont", baseFont, null);
        stamper.getAcroFields().setFieldProperty("$HOS_NAME", "textsize", 14f, null);
        stamper.getAcroFields().setFieldProperty("$QR_NUMBER", "textfont", baseFont, null);
        stamper.getAcroFields().setFieldProperty("$QR_NUMBER", "textsize", 10f, null);
        stamper.getAcroFields().setFieldProperty("$BUS_NO", "textfont", baseFont, null);
        stamper.getAcroFields().setFieldProperty("$BUS_NO", "textsize", 10f, null);
        stamper.getAcroFields().setFieldProperty("$P_NO", "textfont", baseFont, null);
        stamper.getAcroFields().setFieldProperty("$P_NO", "textsize", 10f, null);
        stamper.getAcroFields().setFieldProperty("$LOCATION_NAME", "textfont", baseFont, null);
        stamper.getAcroFields().setFieldProperty("$LOCATION_NAME", "textsize", 7f, null);
        stamper.getAcroFields().setFieldProperty("$GENDER", "textfont", baseFont, null);
        stamper.getAcroFields().setFieldProperty("$GENDER", "textsize", 10f, null);
        stamper.getAcroFields().setFieldProperty("$AGE", "textfont", baseFont, null);
        stamper.getAcroFields().setFieldProperty("$AGE", "textsize", 10f, null);
        stamper.getAcroFields().setFieldProperty("$CONTRACT", "textfont", baseFont, null);
        stamper.getAcroFields().setFieldProperty("$CONTRACT", "textsize", 8f, null);
        stamper.getAcroFields().setFieldProperty("$DATE", "textfont", baseFont, null);
        stamper.getAcroFields().setFieldProperty("$DATE", "textsize", 10f, null);
        stamper.getAcroFields().setFieldProperty("$DAYS", "textfont", baseFont, null);
        stamper.getAcroFields().setFieldProperty("$DAYS", "textsize", 10f, null);

        // 替换文本内容
        stamper.getAcroFields().setField("$QR_NUMBER", dto.getRxTraceCode());
        stamper.getAcroFields().setField("$HOS_NAME", dto.getOrgName());
        stamper.getAcroFields().setField("$BUS_NO", dto.getIptOtpNo());
        stamper.getAcroFields().setField("$P_NO", dto.getPrescriptionNo());
        stamper.getAcroFields().setField("$LOCATION_NAME", dto.getLocationName());
        stamper.getAcroFields().setField("$PATIENT_NAME", dto.getPatnName());
        stamper.getAcroFields().setField("$GENDER", dto.getGender());
        stamper.getAcroFields().setField("$AGE", dto.getPatnAge().toString());
        stamper.getAcroFields().setField("$CONTRACT", dto.getHiFeesetlType());
        // 设置时间格式为 "yyyy-MM-dd HH:mm:ss"
        // 设置时间格式为中文格式 "xx年xx月xx日"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        stamper.getAcroFields().setField("$DATE", sdf.format(dto.getPrscTime()));
        stamper.getAcroFields().setField("$DIAG_NAME", dto.getDiagName());
        stamper.getAcroFields().setField("$DAYS", dto.getValiDays().toString() + "天");
        stamper.getAcroFields().setField("$REASON", dto.getReason());
        stamper.getAcroFields().setField("$P1_NAME", dto.getPrscDrName());
        stamper.getAcroFields().setField("$P2_NAME", dto.getPharName());
        stamper.getAcroFields().setField("$P3_NAME", dto.getDisRevPharName());
        stamper.getAcroFields().setField("$P4_NAME", dto.getCheckPharName());

        // 处理药品明细信息
        if (dto.getMedDetailList() != null && !dto.getMedDetailList().isEmpty()) {
            List<PDFInputDto.MedDetail> medDetails = dto.getMedDetailList();
            medDetails.forEach(medDetail -> {
                String strName = medDetail.getMedName() + "   " + medDetail.getDrugSpec();
                String strDose = "用法： ";
                // 单次用量及单位
                if (medDetail.getSinDoscnt() != null && medDetail.getSinDosunt() != null) {
                    strDose = strDose + medDetail.getSinDoscnt() + medDetail.getSinDosunt();
                }
                strDose = strDose + "   " + medDetail.getUsedFrquCodg() + "   " + medDetail.getMedWay();

                int index = medDetails.indexOf(medDetail) + 1;

                try {
                    stamper.getAcroFields().setFieldProperty("$NAME_" + index, "textfont", baseFont, null);
                    stamper.getAcroFields().setFieldProperty("$NAME_" + index, "textsize", 10f, null);
                    stamper.getAcroFields().setFieldProperty("$DOSE_" + index, "textfont", baseFont, null);
                    stamper.getAcroFields().setFieldProperty("$DOSE_" + index, "textsize", 10f, null);

                    stamper.getAcroFields().setField("$NAME_" + index, strName);
                    stamper.getAcroFields().setField("$DOSE_" + index, strDose);
                    boolean isLastItem = medDetails.indexOf(medDetail) == medDetails.size() - 1;
                    // 最后一张打印“---”
                    if (isLastItem) {
                        stamper.getAcroFields().setFieldProperty("$LINE_" + index, "textfont", baseFont, null);
                        stamper.getAcroFields().setFieldProperty("$LINE_" + index, "textsize", 13f, null);
                        stamper.getAcroFields().setFieldProperty("$EMPTY_" + index, "textfont", baseFont, null);
                        stamper.getAcroFields().setFieldProperty("$EMPTY_" + index, "textsize", 7f, null);

                        stamper.getAcroFields().setField("$LINE_" + index, DASH_LINE);
                        stamper.getAcroFields().setField("$EMPTY_" + index, "(以下是空白)");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            });
        }

        // 删除空白的元素
        for (int i = 1; i < 6; i++) {
            // 检查字段是否存在并且内容为空
            if (stamper.getAcroFields().getField("$EMPTY_" + i) == null || stamper.getAcroFields().getField("$EMPTY_" + i).isEmpty()) {
                stamper.getAcroFields().removeField("$LINE_" + i);
                stamper.getAcroFields().removeField("$EMPTY_" + i);
            }
        }

        // 生成二维码
        PdfContentByte cb = stamper.getOverContent(1); // 获取第一页的内容画布
        int qrX = 80; // 二维码 X 坐标（根据模板调整）
        int qrY = 680; // 二维码 Y 坐标（根据模板调整）
        int qrSize = 100; // 二维码大小

        BarcodeQRCode qrCode = new BarcodeQRCode(dto.getRxTraceCode(), qrSize, qrSize, null);
        Image qrImage = qrCode.getImage();
        qrImage.setAbsolutePosition(qrX, qrY);
        cb.addImage(qrImage);

        // 添加医院红章图片
        if (hospitalSealPath != null && !hospitalSealPath.isEmpty()) {
            try {
                Image hospitalSeal = Image.getInstance(hospitalSealPath);
                // 设置图片的高度和宽度（按照实际需求设置）
                hospitalSeal.scaleToFit(120, 100); // 设置图片大小为宽100，高100
                // 获取PDF页面的实际宽度和高度
                float pageSizeWidth = reader.getPageSize(1).getWidth();
                float pageSizeHeight = reader.getPageSize(1).getHeight();
                // 计算右下角的位置（留出一些边距）
                int sealX = (int)(pageSizeWidth - hospitalSeal.getScaledWidth() - 80); // X坐标（右下角）
                int sealY = (int)(pageSizeHeight - hospitalSeal.getScaledHeight() - 550); // Y坐标（右下角）
                hospitalSeal.setAbsolutePosition(sealX, sealY);
                cb.addImage(hospitalSeal);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("添加医院红章图片时发生错误：" + e.getMessage());
            }
        }

        stamper.close();
        reader.close();
    }

    /**
     * 处方文件获取
     *
     * @param fileName pdf文件名路径
     */
    public static String getPDFFile(String fileName) {

        File file = new File(fileName);

        // 校验文件是否存在
        if (!file.exists() || !file.isFile() || !file.getName().toLowerCase().endsWith(".pdf")) {
            return null;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            // 读取文件字节数组
            byte[] fileBytes = new byte[(int)file.length()];
            int bytesRead = fis.read(fileBytes);
            if (bytesRead != fileBytes.length) {
                return null;
            }
            // Base64编码（标准格式，无换行符）
            return Base64.getEncoder().encodeToString(fileBytes);

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 做成PDF
     *
     * @param rxFileString pdf信息
     * @param fileName pdf文件名路径
     */
    public static Boolean makePDF(String rxFileString,String fileName) {
        try {
            // 验证Base64字符串格式
            if (rxFileString == null || rxFileString.isEmpty()) {
                return false;
            }
            // 解码Base64字符串
            byte[] pdfBytes = Base64.getDecoder().decode(rxFileString);
            // 创建目标目录（如果不存在）
            String directoryPath;
            int lastSlash = fileName.lastIndexOf('/');
            if (lastSlash == -1) {
                // 没有目录部分，可能是当前目录下的文件（如 "file.pdf"）
                directoryPath = "";  // 或者 "." 表示当前目录
            } else {
                directoryPath = fileName.substring(0, lastSlash);
            }
            Files.createDirectories(Paths.get(directoryPath));

            // 写入文件
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                fos.write(pdfBytes);
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }
}