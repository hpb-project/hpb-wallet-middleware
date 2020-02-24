package com.hpb.bc.website.mapper;

import com.hpb.bc.entity.RedPacketKey;
import com.hpb.bc.example.RedPacketKeyExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RedPacketKeyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_key
     *
     * @mbg.generated
     */
    long countByExample(RedPacketKeyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_key
     *
     * @mbg.generated
     */
    int deleteByExample(RedPacketKeyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_key
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_key
     *
     * @mbg.generated
     */
    int insert(RedPacketKey record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_key
     *
     * @mbg.generated
     */
    int insertSelective(RedPacketKey record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_key
     *
     * @mbg.generated
     */
    List<RedPacketKey> selectByExample(RedPacketKeyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_key
     *
     * @mbg.generated
     */
    RedPacketKey selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_key
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") RedPacketKey record, @Param("example") RedPacketKeyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_key
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") RedPacketKey record, @Param("example") RedPacketKeyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_key
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(RedPacketKey record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_key
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(RedPacketKey record);
    int insertBatch(List<RedPacketKey> records);
    Long selectUsedCount(Map<String, String> param);
    RedPacketKey selectRandomKey(Map<String, String> param);
    RedPacketKey selectUniqueKey(String packetNo);
    int updateKeyUsed(Map<String, String> param);
    RedPacketKey selectKeyIsVaild(Map<String, String> param);
    RedPacketKey selectByParam(Map<String, String> param);
}