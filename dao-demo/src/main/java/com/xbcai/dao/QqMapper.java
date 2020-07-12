package com.xbcai.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xbcai.model.Qq;
import com.xbcai.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QqMapper extends BaseMapper<Qq> {
     /**
      *
      * @param qqGroup qq群
      * @param sex 性别
      * @return
      */
     List<Qq> findAllQqs(@Param("qqGroup")String qqGroup,@Param("sex")String sex);

     /**
      *
      * @param qq qq号码
      * @param sex 性别
      * @param qqGroup 来自于哪个qq群
      * @param qqGroupName qq群号码
      * @return
      */
     int saveQq(@Param("qq") String qq,@Param("sex") String sex,@Param("qqGroup")String qqGroup,@Param("qqGroupName")String qqGroupName);

     void updateSendInfo(@Param("id") Integer id);
}
