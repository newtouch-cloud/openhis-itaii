package com.openhis.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.*;


public class SignUtil {
    private static List<String> ignoreSign = new ArrayList();

    public static String getSignText(JSONObject jsonObject, String appSecret) {
        Map<String, String> signMap = new TreeMap();
        Set<Map.Entry<String, Object>> entrys = jsonObject.entrySet();
        Iterator var4 = entrys.iterator();

        while (var4.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry) var4.next();
            if (!StringUtil.isEmpty(entry.getValue()) && !ignoreSign.contains(entry.getKey())) {
                signMap.put(entry.getKey(), getValue(entry.getValue()));
            }
        }

        ArrayList<String> list = new ArrayList();
        Iterator var10 = signMap.entrySet().iterator();

        while (var10.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) var10.next();
            if (StringUtil.isNotEmpty(getObjString(entry.getValue()))) {
                list.add((String) entry.getKey() + "=" + (String) entry.getValue() + "&");
            }
        }

        int size = list.size();
        String[] arrayToSort = (String[]) list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < size; ++i) {
            sb.append(arrayToSort[i]);
        }

        String signText = sb.append("key=").append(appSecret).toString();
        return signText;
    }

    public static String getObjString(Object object) {
        return object == null ? "" : (String) object;
    }

    public static String getValue(Object value) {
        return value instanceof String ? getObjString(value) : treeJsonParam(value);
    }

    private static String treeJsonParam(Object value) {
        String jsonParam = null;
        if (value instanceof Map) {
            Map<String, Object> treeNestedMap = new TreeMap();
            Map<?, ?> nestedMap = (Map) value;
            Iterator var4 = nestedMap.entrySet().iterator();

            while (var4.hasNext()) {
                Map.Entry<?, ?> nestedEntry = (Map.Entry) var4.next();
                treeNestedMap.put((String) nestedEntry.getKey(), nestedEntry.getValue());
            }

            jsonParam = JSONObject.toJSONString(treeParams(treeNestedMap));
        } else if (value instanceof ArrayList) {
            ArrayList<?> ar = (ArrayList) value;
            jsonParam = JSONObject.toJSONString(treeList(ar));
        } else if (value instanceof JSONArray) {
            JSONArray jarr = (JSONArray) value;
            jsonParam = JSONObject.toJSONString(treeJsonArray(jarr));
        } else {
            jsonParam = value.toString();
        }

        return jsonParam;
    }

    private static Map<String, Object> treeParams(Map<String, Object> params) {
        if (params == null) {
            return new TreeMap();
        } else {
            Map<String, Object> treeParams = new TreeMap();
            Iterator var2 = params.entrySet().iterator();

            while (true) {
                while (var2.hasNext()) {
                    Map.Entry<String, Object> entry = (Map.Entry) var2.next();
                    String key = (String) entry.getKey();
                    Object value = entry.getValue();
                    if (value instanceof Map) {
                        Map<String, Object> treeNestedMap = new TreeMap();
                        Map<?, ?> nestedMap = (Map) value;
                        Iterator var8 = nestedMap.entrySet().iterator();

                        while (var8.hasNext()) {
                            Map.Entry<?, ?> nestedEntry = (Map.Entry) var8.next();
                            treeNestedMap.put((String) nestedEntry.getKey(), nestedEntry.getValue());
                        }

                        treeParams.put(key, treeParams(treeNestedMap));
                    } else if (value instanceof ArrayList) {
                        ArrayList<?> ar = (ArrayList) value;
                        treeParams.put(key, treeList(ar));
                    } else if (value instanceof JSONArray) {
                        JSONArray ar = (JSONArray) value;
                        treeParams.put(key, treeJsonArray(ar));
                    } else if (!"".equals(value) && value != null) {
                        treeParams.put(key, value.toString());
                    }
                }

                return treeParams;
            }
        }
    }

    private static JSONArray treeList(ArrayList<?> list) {
        if (list != null && list.size() != 0) {
            JSONArray jsonArray = new JSONArray();
            int size = list.size();

            for (int i = 0; i < size; ++i) {
                jsonArray.add(i, list.get(i));
            }

            return treeJsonArray(jsonArray);
        } else {
            return null;
        }
    }

    private static JSONArray treeJsonArray(JSONArray jarr) {
        if (jarr != null && jarr.size() != 0) {
            JSONArray jsonArray = new JSONArray();
            int size = jarr.size();

            for (int i = 0; i < size; ++i) {
                Object value = jarr.get(i);
                if (!(value instanceof Map)) {
                    if (value instanceof ArrayList) {
                        ArrayList<?> ar = (ArrayList) value;
                        jsonArray.add(i, treeList(ar));
                    } else if (value instanceof JSONArray) {
                        JSONArray ar = (JSONArray) value;
                        jsonArray.add(i, treeJsonArray(ar));
                    } else if (!"".equals(value)) {
                        jsonArray.add(i, value.toString());
                    }
                } else {
                    Map<String, Object> treeNestedMap = new TreeMap();
                    Map<?, ?> nestedMap = (Map) value;
                    Iterator var7 = nestedMap.entrySet().iterator();

                    while (var7.hasNext()) {
                        Map.Entry<?, ?> nestedEntry = (Map.Entry) var7.next();
                        treeNestedMap.put((String) nestedEntry.getKey(), nestedEntry.getValue());
                    }

                    jsonArray.add(i, treeParams(treeNestedMap));
                }
            }

            return jsonArray;
        } else {
            return null;
        }
    }

    static {
        ignoreSign.add("signData");
        ignoreSign.add("encData");
        ignoreSign.add("extra");
    }
}
