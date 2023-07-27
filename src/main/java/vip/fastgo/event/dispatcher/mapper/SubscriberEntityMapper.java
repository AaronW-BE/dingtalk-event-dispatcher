package vip.fastgo.event.dispatcher.mapper;

import vip.fastgo.event.dispatcher.entity.SubscriberEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SubscriberEntityMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteBySn(String sn);

    int insert(SubscriberEntity record);

    SubscriberEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SubscriberEntity record);

    int updateByPrimaryKey(SubscriberEntity record);

    List<SubscriberEntity> findAll();
}
