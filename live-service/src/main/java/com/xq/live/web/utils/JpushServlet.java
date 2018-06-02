package com.xq.live.web.utils;

/**
 * Created by ss on 2018/6/1.
 */

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



public class JpushServlet extends HttpServlet{

    private static final long serialVersionUID = 1L;

    /**
     * 享七
     */
    private static final String appKey ="dbef2534ff7ecc8956b6b451";
    private static final String masterSecret = "102c4486e66ead82d2ef8343";


    /**
     * 申通
     */
    private static final String appKey1 ="84cf5ee2099c654aa03a5d70";
    private static final String masterSecret1 = "7cf23f25a41806d5fd29e3c5";

    public JpushServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    public static void main(String[] args) throws Exception {
        JpushServlet jpushServlet= new JpushServlet();
        jpushServlet.push();
    }

    public void  push(){
        Jpush.testSendPush(appKey1, masterSecret1);
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
