package co.stag.proj.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.Iterator;

public class JSONUtils {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }

    }

    public static JSONObject getJsonArrayObjAt(JSONObject jsonObj, String objName, int index) {
        try {
            return jsonObj.getJSONArray(objName).getJSONObject(index);
        } catch (Exception e) {
            return null;
        }
    }

    public static int getNoOfArrays(JSONObject jsonObj, String objName) {
        try {
            if (jsonObj.getJSONArray(objName) != null) {
                return jsonObj.getJSONArray(objName).length();
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getJsonString(JSONObject jsonObj, String arrayName) {
        try {
            return jsonObj.getJSONArray(arrayName).getString(0);
        } catch (Exception e) {
            try {
                return jsonObj.getString(arrayName);
            } catch (Exception e1) {
                return "Could not get JSON string for : " + arrayName + " from JSON object : " + jsonObj;
            }
        }
    }

    public static JSONObject getJsonObj(JSONObject jsonObj, String objName) {
        try {
            if (jsonObj.getJSONArray(objName) == null) {
                return jsonObj.getJSONObject(objName);
            }
            return jsonObj.getJSONArray(objName).getJSONObject(0);
        } catch (Exception e) {
            try {
                return jsonObj.getJSONObject(objName);
            } catch (Exception xs) {

            }
            return null;
        }
    }

    public String getValueFromJson(JSONObject jsonObj, String arrayName) {
        try {
            return jsonObj.getJSONArray(arrayName).getString(0);
        } catch (Exception e) {
            try {
                return jsonObj.getString(arrayName);
            } catch (Exception e1) {
                return null;
            }
        }
    }

    public JSONObject getSpecificJsonObj(JSONObject jsonObj, String arrayName, String attributeName, String attributeValue) {
        try {

            JSONArray jsonArray = jsonObj.getJSONArray(arrayName);
            if (jsonArray == null) {
                return jsonObj.getJSONObject(arrayName);
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String act_attr_value = jsonObject.getString(attributeName);
                if (act_attr_value.equalsIgnoreCase(attributeValue)) {
                    return jsonObject;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean readJSONAttributes(Object expJsonFormat, Hashtable<String, Integer> attributeList, String pref) {
        try {
            //Get Attributes from Expected JSON
            JSONObject jsonObjExp = (JSONObject) expJsonFormat;
            Iterator<String> exp_attributes = jsonObjExp.keys();
            while (exp_attributes.hasNext()) {
                String key = exp_attributes.next();
                Object exp_attr_obj = jsonObjExp.get(key);
                if (exp_attr_obj instanceof JSONArray) {
                    readJsonArrayAttribute((JSONArray) exp_attr_obj, attributeList, key);
                } else if (exp_attr_obj instanceof JSONObject) {
                    readJSONAttributes(exp_attr_obj, attributeList, key);
                } else {
                    addToAttributeList(pref + ":" + key, attributeList);
                }
            }
            return true;
        } catch (Exception e) {
            Debugger.println("Exception while reading attribute values from JSON. Exception: " + e);
            return false;
        }
    }

    private static void addToAttributeList(String key, Hashtable<String, Integer> attributeList) {
        if (!attributeList.containsKey(key)) {
            attributeList.put(key, 1);
        }

    }

    private static void readJsonArrayAttribute(JSONArray jsonArray, Hashtable<String, Integer> attributeList, String pref) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Object jsonObj = jsonArray.getJSONObject(i);
                if (jsonObj instanceof JSONObject) {
                    Iterator<String> exp_attributes = ((JSONObject) jsonObj).keys();
                    while (exp_attributes.hasNext()) {
                        String key = exp_attributes.next();
                        addToAttributeList(pref + ":" + key, attributeList);
                        Object exp_attr_obj = ((JSONObject) jsonObj).get(key);
                        if (exp_attr_obj instanceof JSONObject) {
                            Iterator<String> exp_attributes_inner = ((JSONObject) exp_attr_obj).keys();
                            while (exp_attributes_inner.hasNext()) {
                                String innerKey = exp_attributes_inner.next();
                                addToAttributeList(pref + ":" + key + ":" + innerKey, attributeList);
                            }
                        } else if (exp_attr_obj instanceof JSONArray) {
                            readJsonArrayAttribute((JSONArray) exp_attr_obj, attributeList, pref + ":" + key);
                        }
                    }
                    //break;
                }
            }
        } catch (Exception e) {
            Debugger.println("Exception while reading JSON array attribute. Exception: " + e);
        }
    }
}//end
