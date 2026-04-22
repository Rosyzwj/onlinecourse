package com.utils;

import java.util.*;

import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class CommonUtil {
	/**
     * 获取随机字符串
     *
     * @param num
     * @return
     */
    public static String getRandomString(Integer num) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < num; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 检查map参数并添加缺失参数
     * @param params
     * @return
     */
    public static void checkMap(Map<String, Object> params) {
        boolean page=true,limit=true,sort=true,order=true;
        Iterator<Map.Entry<String, Object>> iter = params.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Object> info = iter.next();
            Object key = info.getKey();
            Object value = info.getValue();
            if(value==null || "".equals(value) || "null".equals(value)){
                iter.remove();
            }else if("page".equals(key)){page=false;
            }else if("limit".equals(key)){limit=false;
            }else if("sort".equals(key)){sort=false;
            }else if("order".equals(key)){order=false;
            }
        }
        if(page){params.put("page","1");}
        if(limit){params.put("limit","10");}
        if(sort){params.put("sort","id");}
        if(order){params.put("order","desc");}
    }

	/**
	 * 获取随机验证码
	 *
	 * @param num
	 * @return
	 */
	public static String getRandomNumber(Integer num) {
	    String base = "0123456789";
	    Random random = new Random();
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < num; i++) {
	        int number = random.nextInt(base.length());
	        sb.append(base.charAt(number));
	    }
	    return sb.toString();
	}

    public static String getCellValue(Cell cell) {
        String resultValue = "";
        // 判空
        if (Objects.isNull(cell)) {
            return resultValue;
        }

        // 拿到单元格类型
        int cellType = cell.getCellType();
        switch (cellType) {
            // 字符串类型
            case Cell.CELL_TYPE_STRING:
                resultValue = StringUtils.isEmpty(cell.getStringCellValue()) ? "" : cell.getStringCellValue().trim();
                break;
            // 布尔类型
            case Cell.CELL_TYPE_BOOLEAN:
                resultValue = String.valueOf(cell.getBooleanCellValue());
                break;
            // 数值类型
            case Cell.CELL_TYPE_NUMERIC:
                /**
                 * format 的值可能为以下这些 yyyyMMddHHmmss
                 * yyyy-MM-dd----- 14
                 * yyyy年m月d日----- 31
                 * yyyy年m月--------57
                 * m月d日  -----------58
                 * HH:mm-----------20
                 * h时mm分  --------- 32
                 */
                Object val = cell.getNumericCellValue();
                // POI Excel 日期格式转换
                String formatDate = "";
                switch (cell.getCellStyle().getDataFormat()){
                    case 14:
                        formatDate = "yyyy-MM-dd";
                        break;
                    case 20:
                        formatDate = "HH:mm";
                        break;
                    case 21:
                        formatDate = "HH:mm:ss";
                        break;
                    case 31:
                        formatDate = "yyyy年MM月dd日";
                        break;
                    case 32:
                        formatDate = "HH时mm分";
                        break;
                    case 33:
                        formatDate = "HH时mm分mm秒";
                        break;
                    case 57:
                        formatDate = "yyyy年MM月";
                        break;
                    case 58:
                        formatDate = "MM月dd日";
                        break;
                    case 176:
                        formatDate = "yyyy-MM-dd HH:mm:ss";
                        break;
                }
                if(!"".equals(formatDate)){
                    resultValue = new SimpleDateFormat(formatDate).format(DateUtil.getJavaDate((Double) val));
                }else{
                    resultValue = new DecimalFormat("#.######").format(cell.getNumericCellValue());
                }
                break;
            // 取空串
            default:
                break;
        }
        return resultValue;
    }





}
