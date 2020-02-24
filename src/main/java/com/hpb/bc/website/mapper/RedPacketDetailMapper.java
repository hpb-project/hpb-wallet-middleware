package com.hpb.bc.website.mapper;

import com.hpb.bc.entity.RedPacketDetail;
import com.hpb.bc.example.RedPacketDetailExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RedPacketDetailMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_detail
     *
     * @mbg.generated
     */
    long countByExample(RedPacketDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_detail
     *
     * @mbg.generated
     */
    int deleteByExample(RedPacketDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_detail
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_detail
     *
     * @mbg.generated
     */
    int insert(RedPacketDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_detail
     *
     * @mbg.generated
     */
    int insertSelective(RedPacketDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_detail
     *
     * @mbg.generated
     */
    List<RedPacketDetail> selectByExample(RedPacketDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_detail
     *
     * @mbg.generated
     */
    RedPacketDetail selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_detail
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") RedPacketDetail record, @Param("example") RedPacketDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_detail
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") RedPacketDetail record, @Param("example") RedPacketDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_detail
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(RedPacketDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table red_packet_detail
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(RedPacketDetail record);
    RedPacketDetail selectByNoAndKeyAndNotErr(Map<String, String> param);
    int updateMaxFlagById(Long id);
    int updateStatusWithParam(Map<String, Object> param);
    RedPacketDetail selectByTokenIdAndStatus(Map<String, Object> param);
    RedPacketDetail selectByParam(Map<String, String> param);
    RedPacketDetail selectByToAddrAndStatus(Map<String, String> param);
    List<RedPacketDetail> selectByTokenIdWith1(Map<String, Object> param);
}