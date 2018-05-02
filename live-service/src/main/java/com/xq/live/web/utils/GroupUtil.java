package com.xq.live.web.utils;

import java.util.*;

/**
 * Created by ss on 2018/5/2.
 */
public class GroupUtil {
    static int indexList=0;
    static List<String> listShop=new ArrayList<String>();
    static List<String> listUser = new ArrayList<String>();

    public static void main(String[] args) throws Exception {
        /***循环添加
         * listShop.add(0,"A");
         listUser.add(0,"A");
         */
        addList("咖喱","1");
        System.out.println(grouptwo(listShop,listUser));


    }

    public static void  addList(String shop,String user){
        System.out.print(indexList);
        listShop.add(indexList,shop);
        listUser.add(indexList,user);
        indexList++;
    }

    public static Map<String,String> grouptwo(List<String> shop,List<String> user){
        Map<String,String> maplist = new HashMap<String,String>();
        Random random = new Random();
        List<String> value = new ArrayList<String>();
        //Integer[] value = new Integer[user.size()];
        for (int u=0;u<user.size();u++){
            value.add(u,user.get(u));
        }
        //System.out.println(user.length);
        for (int i=user.size()-1;i>=0;i--){
            String temp = value.get(random.nextInt(i+1));
            value.set(random.nextInt(i+1),value.get(i));
            value.set(i,temp);

        }
        for (int k=0;k<user.size();k++){
            user.set(k,value.get(k));
        }
        //System.out.println(user.toString());
        for (int j=0;j<user.size();j++){
            maplist.put(shop.get(j), user.get(j));
            // System.out.print(maplist);
        }

        return maplist;
    }


}
