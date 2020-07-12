package com.xbcai.qq;





import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GainQQ {

    /**
     * POST
     **/
    public static String sendPostRequest(String requestUrl, String payload,String Cookie) {
        StringBuffer jsonString = new StringBuffer();
        HttpURLConnection connection=null;
        BufferedReader br=null;
        try {
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            connection.setRequestProperty("user-agent", "Mozilla/5.0 (compatible; MSIE 11.0; Windows NT 6.1; Trident/5.0)");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestProperty("Cookie",Cookie);
            connection.setRequestProperty("origin","https://qun.qq.com");
            connection.setRequestProperty("referer","https://qun.qq.com/member.html");

            connection.setReadTimeout(300000);
            connection.setConnectTimeout(300000);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            writer.write(payload);
            writer.close();
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            connection.disconnect();

        }catch(Exception e){
            e.printStackTrace();
            br = new BufferedReader(new InputStreamReader(connection.getErrorStream()) );
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    jsonString.append(line);
                }
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }finally{
            connection.disconnect();
            try {
                br.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return jsonString.toString();
    }

    /**
     * 将Unix时间戳转换成指定格式日期
     * @param timestampString    Unix时间戳
     * @param formats            格式（"yyyy-MM-dd HH:mm:ss"）
     * @return
     */
    public static String TimeStamp2Date(String timestampString, String formats){
        Long timestamp = Long.parseLong(timestampString)*1000;
        String date = new java.text.SimpleDateFormat(formats).format(new java.util.Date(timestamp));
        return date;
    }

    /**
     * 获取数据并处理数据
     * @param size    qq群总人数
     * @param gc    qq群号
     * @param bkn    我也不知道是啥
     * @param Cookie    进入https://qun.qq.com/member.html#gid=224392232，按F12 点击网络，点开请求"http://qun.qq.com/cgi-bin/qun_mgr/search_group_members" 获取里面的参数和Cookie
     * @return  QQ数据
     * @throws Exception
     */
    @SuppressWarnings("all")
    public static List<String> processing(int size,String gc,String bkn,String Cookie){
        //存放每个QQ详细数据
        List<String> olist = new ArrayList<String>();
        olist.add("uin"+"\t"+"role"+"\t"+"flag"+"\t"+"g"+"\t"+"join_time"+"\t"+"last_speak_time"+"\t"+"lv"+"\t"+"nick"+"\t"+"card"+"\t"+"qage"+"\t"+"tags"+"\t"+"rm"+"\t"+"mailbox");
        //控制偏移量
        int i = 0;
        //控制循环次数
        int j = 0;
        //控制是否跳出循环的boolean
        boolean judge = true;
        while(judge){
            //from(开始)
            int st = i;
            //end(结束)
            int end = i+20;
            System.out.println(st+"---"+end+"---"+j+"---"+size/20);
            String requestUrl = "http://qun.qq.com/cgi-bin/qun_mgr/search_group_members?gc="+gc+"&st="+st+"&end="+end+"&sort=0&bkn="+bkn;
            //把获取的参数转换成JSON字符串并清洗成可使用数据
            JSONObject json_all = JSONObject.fromObject(sendPostRequest(requestUrl, "",Cookie));
            //获取JSON串中的mems数组
            if(json_all.get("mems")!=null){
                String qq_all =  json_all.get("mems").toString();
                //把数组导入到JSONArray中方便数据处理
                JSONArray json_qq = JSONArray.fromObject(qq_all);
                for(int le = 0 ; le <json_qq.length();le++){
                    JSONObject qq = JSONObject.fromObject(json_qq.get(le));
                    //把每个QQ转换成一条数据并存入List集合
                    String all = qq.getString("uin")+"\t"+
                            qq.getString("role")+"\t"+
                            qq.getString("flag")+"\t"+
                            // 1:女；0：男
                            qq.getString("g")+"\t"+
                            TimeStamp2Date(qq.getString("join_time"), "yyyy-MM-dd")+"\t"+
                            TimeStamp2Date(qq.getString("last_speak_time"), "yyyy-MM-dd")+"\t"+
                            qq.getString("lv")+"\t"+
                            qq.getString("nick")+"\t"+
                            qq.getString("card")+"\t"+
                            qq.getString("qage")+"\t"+
                            qq.getString("tags")+"\t"+
                            qq.getString("rm")+"\t"+
                            qq.getString("uin")+"@qq.com";
                    olist.add(all);
                }
            }
            j++;    //控制循环次数
            if(j==size/21){
                //循环完毕后跳出循环
                judge = false;
            }else{
                i+=21;
            }
            System.out.println("已扫描人数："+i);
        }
        return olist;
    }

    /**
     /**
     * 获取QQ号码等数据并处理数据
     * @param size    qq群总人数
     * @param gc    qq群号
     * @param bkn    我也不知道是啥
     * @param Cookie    进入https://qun.qq.com/member.html#gid=224392232，按F12 点击网络，点开请求"http://qun.qq.com/cgi-bin/qun_mgr/search_group_members" 获取里面的参数和Cookie
     * @return  QQ数据
     * @throws Exception
     */
    @SuppressWarnings("all")
    public static List<Map<String,String>> getQq(String gc, String bkn, String cookie, int size){
        List<Map<String,String>> olist = new ArrayList<>();
        //控制偏移量
        int i = 0;
        //控制循环次数
        int j = 0;
        //控制是否跳出循环的boolean
        boolean judge = true;
        while(judge){
            //from(开始)
            int st = i;
            //end(结束)
            int end = i+10;
            System.out.println(st+"---"+end+"---"+j+"---"+size/10);
            String requestUrl = "http://qun.qq.com/cgi-bin/qun_mgr/search_group_members?gc="+gc+"&st="+st+"&end="+end+"&sort=0&bkn="+bkn;
            //把获取的参数转换成JSON字符串并清洗成可使用数据
            JSONObject json_all = JSONObject.fromObject(sendPostRequest(requestUrl, "",cookie));
            //获取JSON串中的mems数组
            if(json_all.get("mems")!=null&&j!=12){
                String qq_all =  json_all.get("mems").toString();
                //把数组导入到JSONArray中方便数据处理
                JSONArray json_qq = JSONArray.fromObject(qq_all);
                for(int le = 0 ; le <json_qq.length();le++){
                    JSONObject qq = JSONObject.fromObject(json_qq.get(le));
                    //把每个QQ转换成一条数据并存入List集合
                    Map<String,String> maps = new HashMap<>();
                    maps.put("qq",qq.getString("uin"));
                    maps.put("sex","1".equals(qq.getString("g"))?"女":"男");
                    maps.put("nick",qq.getString("nick"));
                    olist.add(maps);
                }
            }
            j++;    //控制循环次数
            if(j==size/11){
                //循环完毕后跳出循环
                judge = false;
            }else{
                i+=11;
            }
            System.out.println("已扫描人数："+i);
        }
        return olist;

    }

    public static void main(String[] args) {
        //qq群总人数
        int size = 358;
        //qq群号
        String gc = "1039650374";
        //我也不知道是啥，反正一定要填
        String bkn = "1330149222";
        //网址Cookie
        String cookie = "pgv_pvid=7081042122; tvfe_boss_uuid=288b475d417cee27; pgv_pvi=1503060992; RK=RID4BrRNHX; ptcz=6a18b8a7e23ec82cb34f652c3cc8bd48a262981b111f2e76bb20c8cb429af3f7; o_cookie=1285589395; pac_uid=1_1285589395; XWINDEXGREY=0; _ga=GA1.2.1900990938.1589288442; pgv_si=s9667376128; _qpsvr_localtk=0.40790315411979416; uin=o1285589395; skey=@G3u96UtRH; p_uin=o1285589395; pt4_token=NEVTWm1Kbar2viSYtLwdJPDfdvZL37o-F2ceADY5w10_; p_skey=4L0kyokPcZe1x6sjmlLzM7*4JlK3SX*yOSu7lBtJxs0_; traceid=c181e5848d";
        //输出qq详细信息
       processing(size, gc, bkn, cookie).forEach(item-> System.out.println(item));
        // 输出qq信息
        //getQq(gc,bkn,cookie,size).forEach(item-> System.out.println(item));
    }
}