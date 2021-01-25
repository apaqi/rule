/*
package com.bstek.urule.console.servlet.respackage;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

;

public class RecursionObject {
    public static void main(String[] args){
        //制造数据
        SubAddressBO subAddressBO0 = new SubAddressBO("subaddress0",10);
        SubAddressBO subAddressBO1 = new SubAddressBO("subaddress1",20);
        SubAddressBO subAddressBO2 = new SubAddressBO("subaddress2",30);
        SubAddressBO subAddressBO3 = new SubAddressBO("subaddress3",40);
        SubAddressBO subAddressBO4 = new SubAddressBO("subaddress4",50);
        AddressBO address0 = new AddressBO();
        address0.setSubAddressBO(subAddressBO0);

        AddressBO address1 = new AddressBO();
        address1.setSubAddressBO(subAddressBO1);

        AddressBO address2 = new AddressBO();
        address2.setSubAddressBO(subAddressBO2);

        AddressBO address3 = new AddressBO();
        address3.setSubAddressBO(subAddressBO3);

        AddressBO address4 = new AddressBO();
        address4.setSubAddressBO(subAddressBO4);



        HashMap<String,AddressBO> map = new HashMap<>();
        map.put("addr1",address1);
        HashMap<String,AddressBO> map2 = new HashMap<>();
        map2.put("addr2",address2);

        ArrayList<AddressBO> list = new ArrayList<>();
        list.add(address3);
        list.add(address4);

        UserBO userBO = new UserBO();
        userBO.setId("u111");
        userBO.setUserName("lsl");
        userBO.setAge(11);
        //测试目标属性存在于对象属性是自定义类中
        userBO.setAddress(address0);
        //测试目标属性存在于对象属性是list的元素中
        //userBO.setList(list);
        userBO.setMap(map);

        UserBO userBO2 = new UserBO();
        userBO2.setId("u222");
        userBO2.setUserName("ls2");
        userBO2.setAge(22);
        //测试目标属性存在于对象属性是自定义类中
        //userBO2.setAddress(address0);
        //测试目标属性存在于对象属性是list的元素中
        //userBO2.setList(list);
        userBO2.setMap(map2);

        List<UserBO> userBOList = new ArrayList<>();
        userBOList.add(userBO);
        userBOList.add(userBO2);
        //利用方法一
        String matchKey = "addrId";
        Map<String,Object> resultMap = recursionLoopThroughObj(userBOList,matchKey);
        System.out.println("result:" + resultMap.get("proValue"));

        //利用方法二
        //String businessNoFromArg = getBusinessNoFromArg(userBO, "test");
        //System.out.println("businessNoFromArg=" + businessNoFromArg);

    }

    */
/**
     * 方法一：利用递归遍历
     * 用途：从复杂对象中递归遍历，获取string类型的目标属性名的值
     * 适用条件：该复杂对象中如果存在多个目标属性targetProName，遍历到第一个atargetProName则退出遍历
     *           targetProName属性必须是string
     *           targetProName可以存在自定义对象中、list、map、数组中
     *           如果复杂对象不包含目标属性则返回空字符串
     *           复杂对象可以是复杂嵌套的BO/List<BO>/Map<Object,BO>,目标属性存在于BO中
     *           对于复杂对象是list或map嵌套的不做支持。比如List<List<BO></BO>> /List<Map<object,BO>> / Map<object,List<BO>>
     * @param object 复杂对象
     * @param targetProName  目标属性名
     * @return
     *//*

    public static  Map<String,Object> recursionLoopThroughObj(Object object,String targetProName){
        Map<String,Object> resultMap = new HashMap<>();
        Class clazz = null;
        String proValue = "";
        boolean loopFlag = true;
        resultMap.put("loopFlag",loopFlag);
        resultMap.put("proValue",proValue);
        try {
            if (object==null || checkObjectIsSysType(object)){
                //如果object是null/基本数据类型/包装类/日期类型，则不需要在递归调用
                resultMap.put("loopFlag",false);
                resultMap.put("proValue","");
                return resultMap;
            }
            if (object instanceof Map){
                Map map = (Map)object;
                Map<String,Object> objMap = new HashMap<>();
                if (map!=null && map.size()>0){
                    Iterator iterator = map.values().iterator();
                    while (iterator.hasNext()){
                        objMap = recursionLoopThroughObj(iterator.next(),targetProName);
                        if (!(boolean)objMap.get("loopFlag")){
                            return objMap;
                        }
                    }
                }
            }

            clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String proType = field.getGenericType().toString();
                String proName = field.getName();
                System.out.println("proName:" + proName + ",proType:" + proType );
                if ("class java.lang.String".equals(proType) && targetProName.equals(proName)){
                    field.setAccessible(true);
                    proValue = (String)field.get(object);
                    resultMap.put("loopFlag",false);
                    resultMap.put("proValue",proValue);
                    return resultMap;
                }else if ("byte".equals(proType) || "short".equals(proType) || "int".equals(proType)|| "long".equals(proType)|| "double".equals(proType) || "float".equals(proType) || "boolean".equals(proType) ){
                    //属性是基本类型跳过
                    continue;
                }else if ("class java.lang.Byte".equals(proType) || "class java.lang.Short".equals(proType) || "class java.lang.Integer".equals(proType) || "class java.lang.Long".equals(proType) || "class java.lang.Double".equals(proType) || "class java.lang.Float".equals(proType) || "class java.lang.Boolean".equals(proType) || ("class java.lang.String".equals(proType) && !targetProName.equals(proName))){
                    //属性是包装类跳过
                    continue;
                }else if (proType.startsWith("java.util")){
                    //属性是集合类型则遍历
                    if (proType.startsWith("java.util.List")){
                        //对List类型的属性遍历
                        PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), clazz);
                        Method method = descriptor.getReadMethod();
                        List list = (List)method.invoke(object);
                        Map<String,Object> objMap = new HashMap<>();
                        if (list!=null && list.size()>0){
                            int len = list.size();
                            for (int i= 0;i<len;i++){
                                objMap = recursionLoopThroughObj(list.get(i),targetProName);
                                if (!(boolean)objMap.get("loopFlag")){
                                    return objMap;
                                }
                            }
                        }

                    }else if (proType.startsWith("java.util.Map")){
                        //对Map类型的属性遍历
                        PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), clazz);
                        Method method = descriptor.getReadMethod();
                        Map map = (Map)method.invoke(object);
                        Map<String,Object> objMap = new HashMap<>();
                        if (map!=null && map.size()>0){
                            for (Object obj : map.values()){
                                objMap = recursionLoopThroughObj(obj,targetProName);
                                if (!(boolean)objMap.get("loopFlag")){
                                    return objMap;
                                }
                            }
                        }

                    }

                }else if(field.getType().isArray()){
                    //属性是数组类型则遍历
                    field.setAccessible(true);
                    Object[] objArr = (Object[]) field.get(object);
                    Map<String,Object> objMap = new HashMap<>();
                    if (objArr!=null && objArr.length>0){
                        for (Object arr : objArr){
                            objMap = recursionLoopThroughObj(arr,targetProName);
                            if (!(boolean)objMap.get("loopFlag")){
                                return objMap;
                            }
                        }
                    }

                }else  {
                    //class类型的遍历
                    PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), clazz);
                    Method method = descriptor.getReadMethod();
                    Object obj = method.invoke(object);
                    Map<String,Object> objMap = new HashMap<>();
                    if (obj!= null){
                        objMap = recursionLoopThroughObj(obj,targetProName);
                        if (!(boolean)objMap.get("loopFlag")){
                            return objMap;
                        }
                    }else {
                        continue;
                    }

                }
            }
        } catch (Exception e) {
            System.err.println("err:" + e);
        }
        return resultMap;
    }

    */
/**
     * 检查object是否为java的基本数据类型/包装类/java.util.Date/java.sql.Date
     * @param object
     * @return
     *//*

    public static boolean checkObjectIsSysType(Object object){
        String objType = object.getClass().toString();
        if ("byte".equals(objType) || "short".equals(objType) || "int".equals(objType)|| "long".equals(objType)|| "double".equals(objType) || "float".equals(objType) || "boolean".equals(objType)){
            return true;
        }else if ("class java.lang.Byte".equals(objType) || "class java.lang.Short".equals(objType) || "class java.lang.Integer".equals(objType) || "class java.lang.Long".equals(objType) || "class java.lang.Double".equals(objType) || "class java.lang.Float".equals(objType) || "class java.lang.Boolean".equals(objType) || "class java.lang.String".equals(objType)){
            return true;
        }else {
            return  false;
        }

    }

    */
/**
     * 方法二：从复杂对象中获取string类型的目标属性targetProName的值
     * 把对象转换成json字符串，然后截取第一次出现的targetProName的值
     * 适用条件：同方法一
     * @param object 复杂对象
     * @param targetProName 目标属性
     * @return
     *//*

    public static String getBusinessNoFromArg(Object object,String targetProName){
        String jsonString = JSON.toJSONString(object);
        System.err.println("jsonString=" + jsonString);
        jsonString = StringUtils.substringAfter(jsonString,"\""+targetProName + "\":\"");
        jsonString = StringUtils.substringBefore(jsonString,"\"");
        return jsonString;
    }

}
*/
