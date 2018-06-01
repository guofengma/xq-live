package com.xq.live.web.utils;

/**
 * Created by ss on 2018/6/1.
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.org.apache.xml.internal.serializer.utils.Utils;



public class JpushServlet extends HttpServlet{

    private static final long serialVersionUID = 1L;

    private static final String appKey ="dbef2534ff7ecc8956b6b451";
    private static final String masterSecret = "102c4486e66ead82d2ef8343";

    public JpushServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    public static void main(String[] args) throws Exception {
        JpushServlet jpushServlet= new JpushServlet();
        jpushServlet.push();
    }

    public void  push(){
        Jpush.testSendPush(appKey, masterSecret);
        System.out.println("sucess");
    }
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Jpush.testSendPush(appKey, masterSecret);
        System.out.println("sucess");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }
}
