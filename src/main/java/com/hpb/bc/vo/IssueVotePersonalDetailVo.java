package com.hpb.bc.vo;

import com.hpb.bc.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

public class IssueVotePersonalDetailVo extends BaseEntity {

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue_vote.issue_no
     *
     * @mbg.generated
     */
    private String issueNo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue_vote.vote_status
     *
     * @mbg.generated
     */
    private String voteStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue_vote.name_zh
     *
     * @mbg.generated
     */
    private String nameZh;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue_vote.name_en
     *
     * @mbg.generated
     */
    private String nameEn;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue_vote.desc_zh
     *
     * @mbg.generated
     */
    private String descZh;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue_vote.desc_en
     *
     * @mbg.generated
     */
    private String descEn;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue_vote.value1_zh
     *
     * @mbg.generated
     */
    private String value1Zh;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue_vote.value2_zh
     *
     * @mbg.generated
     */
    private String value2Zh;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue_vote.value1_en
     *
     * @mbg.generated
     */
    private String value1En;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue_vote.value2_en
     *
     * @mbg.generated
     */
    private String value2En;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue_vote.begin_time
     *
     * @mbg.generated
     */
    private Date beginTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue_vote.end_time
     *
     * @mbg.generated
     */
    private Date endTime;
    private long countDownTime;
    private String peragreeNum;
    private String perdisagreeNum;

    public String getPeragreeNum() {
        return peragreeNum;
    }

    public void setPeragreeNum(String peragreeNum) {
        this.peragreeNum = peragreeNum;
    }

    public String getPerdisagreeNum() {
        return perdisagreeNum;
    }

    public void setPerdisagreeNum(String perdisagreeNum) {
        this.perdisagreeNum = perdisagreeNum;
    }

    public long getCountDownTime() {
        return countDownTime;
    }

    public void setCountDownTime(long countDownTime) {
        this.countDownTime = countDownTime;
    }

    public String getIssueNo() {
        return issueNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue_vote.issue_no
     *
     * @param issueNo the value for issue_vote.issue_no
     *
     * @mbg.generated
     */
    public void setIssueNo(String issueNo) {
        this.issueNo = issueNo == null ? null : issueNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue_vote.vote_status
     *
     * @return the value of issue_vote.vote_status
     *
     * @mbg.generated
     */
    public String getVoteStatus() {
        return voteStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue_vote.vote_status
     *
     * @param voteStatus the value for issue_vote.vote_status
     *
     * @mbg.generated
     */
    public void setVoteStatus(String voteStatus) {
        this.voteStatus = voteStatus == null ? null : voteStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue_vote.name_zh
     *
     * @return the value of issue_vote.name_zh
     *
     * @mbg.generated
     */
    public String getNameZh() {
        return nameZh;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue_vote.name_zh
     *
     * @param nameZh the value for issue_vote.name_zh
     *
     * @mbg.generated
     */
    public void setNameZh(String nameZh) {
        this.nameZh = nameZh == null ? null : nameZh.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue_vote.name_en
     *
     * @return the value of issue_vote.name_en
     *
     * @mbg.generated
     */
    public String getNameEn() {
        return nameEn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue_vote.name_en
     *
     * @param nameEn the value for issue_vote.name_en
     *
     * @mbg.generated
     */
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn == null ? null : nameEn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue_vote.desc_zh
     *
     * @return the value of issue_vote.desc_zh
     *
     * @mbg.generated
     */
    public String getDescZh() {
        return descZh;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue_vote.desc_zh
     *
     * @param descZh the value for issue_vote.desc_zh
     *
     * @mbg.generated
     */
    public void setDescZh(String descZh) {
        this.descZh = descZh == null ? null : descZh.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue_vote.desc_en
     *
     * @return the value of issue_vote.desc_en
     *
     * @mbg.generated
     */
    public String getDescEn() {
        return descEn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue_vote.desc_en
     *
     * @param descEn the value for issue_vote.desc_en
     *
     * @mbg.generated
     */
    public void setDescEn(String descEn) {
        this.descEn = descEn == null ? null : descEn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue_vote.value1_zh
     *
     * @return the value of issue_vote.value1_zh
     *
     * @mbg.generated
     */
    public String getValue1Zh() {
        return value1Zh;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue_vote.value1_zh
     *
     * @param value1Zh the value for issue_vote.value1_zh
     *
     * @mbg.generated
     */
    public void setValue1Zh(String value1Zh) {
        this.value1Zh = value1Zh == null ? null : value1Zh.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue_vote.value2_zh
     *
     * @return the value of issue_vote.value2_zh
     *
     * @mbg.generated
     */
    public String getValue2Zh() {
        return value2Zh;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue_vote.value2_zh
     *
     * @param value2Zh the value for issue_vote.value2_zh
     *
     * @mbg.generated
     */
    public void setValue2Zh(String value2Zh) {
        this.value2Zh = value2Zh == null ? null : value2Zh.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue_vote.value1_en
     *
     * @return the value of issue_vote.value1_en
     *
     * @mbg.generated
     */
    public String getValue1En() {
        return value1En;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue_vote.value1_en
     *
     * @param value1En the value for issue_vote.value1_en
     *
     * @mbg.generated
     */
    public void setValue1En(String value1En) {
        this.value1En = value1En == null ? null : value1En.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue_vote.value2_en
     *
     * @return the value of issue_vote.value2_en
     *
     * @mbg.generated
     */
    public String getValue2En() {
        return value2En;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue_vote.value2_en
     *
     * @param value2En the value for issue_vote.value2_en
     *
     * @mbg.generated
     */
    public void setValue2En(String value2En) {
        this.value2En = value2En == null ? null : value2En.trim();
    }

    public Date getBeginTime() {
        return beginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue_vote.begin_time
     *
     * @param beginTime the value for issue_vote.begin_time
     *
     * @mbg.generated
     */
    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue_vote.end_time
     *
     * @return the value of issue_vote.end_time
     *
     * @mbg.generated
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue_vote.end_time
     *
     * @param endTime the value for issue_vote.end_time
     *
     * @mbg.generated
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}