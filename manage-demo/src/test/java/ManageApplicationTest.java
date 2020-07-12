import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xbcai.ManageApplication;
import com.xbcai.dao.QqMapper;
import com.xbcai.date.JdkDate;
import com.xbcai.file.FileUtils;
import com.xbcai.mail.TaoBao;
import com.xbcai.mail.TaoBaoProductUtils;
import com.xbcai.model.Qq;
import com.xbcai.qq.GainQQ;
import com.xbcai.service.mail.MailService;
import com.xbcai.service.user.UserService;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by summer on 2017/5/4.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ManageApplication.class)
public class ManageApplicationTest {

    @Autowired
    private MailService mailService;
    @Autowired
    private UserService userService;
    @Autowired
    private QqMapper qqMapper;

    @Test
    public void deleteQqTest() throws Exception{
        String path="D:\\副业\\淘宝\\邮件推送\\qq.txt";
        List<String> deleteList = FileUtils.readFile(path);
        int qqCounts = qqMapper.delete(new QueryWrapper<Qq>().in("qq_mail", deleteList));
        System.out.println("删除了"+qqCounts+"条邮箱");
    }

    @Test
    public void saveQqTest(){
        String s = "821517356;807088006;819271303;836413982;821750661;827356395;819283560;836424694;821924758;827545656;819437396;448019618;806919033;827660829;809286130;448143157;448313680;442115949;442781524;448365051;442308471;443173664;448381014;442333846;443679054;444391164;449124787;442648006;443800738;450844453;451565872;451888654;476654930;465420676;451649050;437651370;465486032;451841151;438529469;465220427;466606939;459672852;460533262;460533262;461368320;459787263;460496541;461747381;459829175;461141360;461909770;460114091;461150912;470195947;458492637;470195947;457020060;457806042;458156161;458492637";
        String[] array = s.split(";");
        for (String ss: array) {
            qqMapper.saveQq(ss+"@qq.com","男",null,null);
        }
        System.out.println("保存成功，size:"+array.length);
    }

    @Test
    public void updateQqTest(){
        qqMapper.updateSendInfo(406);
    }

    @Test
    public void queryTest(){
        List<Qq> allQqs = qqMapper.findAllQqs("196665125",null);
        allQqs.forEach(item-> System.out.println(item));

    }

    /**
     * 发送之前测试是否能发邮件，给自己发
     */
    @Test
    public void sendTest(){
        TaoBao tb = TaoBaoProductUtils.tingshen();
        try {
            mailService.sendInlineResourceMail("1285589395@qq.com", tb.getSubject(),tb.getContent(), tb.getImgPath(),tb.getH(),tb.getW());
        }catch (Exception e){
            System.out.println("发送失败:"+e.getMessage());
        }
    }

    /**
     * 将qq群成员信息入库
     */
    @Test
    public void saveQqQunTest(){
        String qqGroup = "655386402";
        String qqGroupName = "广州交友相亲群";
        int size = 1769;
        String cookie = "pgv_pvid=7081042122; tvfe_boss_uuid=288b475d417cee27; pgv_pvi=1503060992; RK=RID4BrRNHX; ptcz=6a18b8a7e23ec82cb34f652c3cc8bd48a262981b111f2e76bb20c8cb429af3f7; o_cookie=1285589395; pac_uid=1_1285589395; XWINDEXGREY=0; _ga=GA1.2.1900990938.1589288442; pgv_si=s8752216064; _qpsvr_localtk=0.8691136113957243; uin=o1285589395; skey=@16sQNXwnw; p_uin=o1285589395; pt4_token=DSwSYVwTMTpo-Pgln8zf1HyMElkliXV0swKps3QYrVk_; p_skey=Q5T3P492nqGt1IOngiRPyQx7cRkbkSlVXsZz23BNjXY_; traceid=7361e199e5";
        List<Map<String, String>> qq = GainQQ.getQq(qqGroup, "823629650", cookie, size);
        qq.forEach(item->qqMapper.saveQq(item.get("qq")+"@qq.com",item.get("sex"),qqGroup,qqGroupName));
    }



    @Test
    public void sendInlineResourceMail() {
        TaoBao tb = TaoBaoProductUtils.tingshen();
        List<Qq> allQqs = qqMapper.findAllQqs("194425825","男");
        int size = 20;
        StringBuilder sb = new StringBuilder();
        int j = 0;
        int sendCount = 0;
        List<Integer> ids = new ArrayList<>();
        for(int i=0;i<allQqs.size();i++){
            Qq qq = allQqs.get(i);
            //今天已经发过的不再发送
            if(qq.getLastSendTime()!=null&&StringUtils.equals(JdkDate.format(qq.getLastSendTime(),"yyyyMMdd"),
            JdkDate.format(new Date(),"yyyyMMdd"))){continue;}
            sendCount++;
            if(sendCount>200) break;
            try{
                if(j==size||(j<size&&i==allQqs.size()-1)){
                    mailService.sendInlineResourceMail(sb.toString(), tb.getSubject(), tb.getContent(), tb.getImgPath(),tb.getH(),tb.getH());
                    Thread.sleep(3000);
                    System.out.println("成功发送："+sb.toString());
                    sb.setLength(0);
                    //sb.append("461435481@qq.com;");
                    j=0;
                    ids.forEach(item->qqMapper.updateSendInfo(item));
                    ids.clear();
                }
                sb.append(qq.getQqMail()).append(";");
                ids.add(qq.getId());
                j++;
            }catch (Exception e){
                System.out.println("发送失败，邮箱："+sb.toString()+"\n,error:"+e.getMessage());
                System.exit(500);
            }finally {
                ids.forEach(item->qqMapper.updateSendInfo(item));
                System.out.println("更新已发送记录："+ids);
                ids.clear();
            }
        }
        System.out.println("=====================总共发送邮件数：================================"+sendCount);
    }

}
