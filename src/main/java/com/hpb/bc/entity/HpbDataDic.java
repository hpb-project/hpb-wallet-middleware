package com.hpb.bc.entity;

public class HpbDataDic extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2561668814243985943L;

	/**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hpb_data_dic.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hpb_data_dic.data_type_no
     *
     * @mbg.generated
     */
    private Integer dataTypeNo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hpb_data_dic.data_type_name
     *
     * @mbg.generated
     */
    private String dataTypeName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hpb_data_dic.data_no
     *
     * @mbg.generated
     */
    private String dataNo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hpb_data_dic.data_name
     *
     * @mbg.generated
     */
    private String dataName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hpb_data_dic.state
     *
     * @mbg.generated
     */
    private String state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hpb_data_dic.data_no_len
     *
     * @mbg.generated
     */
    private Integer dataNoLen;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hpb_data_dic.limit_flag
     *
     * @mbg.generated
     */
    private String limitFlag;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hpb_data_dic.high_limit
     *
     * @mbg.generated
     */
    private String highLimit;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hpb_data_dic.low_limit
     *
     * @mbg.generated
     */
    private String lowLimit;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hpb_data_dic.effect_date
     *
     * @mbg.generated
     */
    private Long effectDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hpb_data_dic.expire_date
     *
     * @mbg.generated
     */
    private Long expireDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hpb_data_dic.create_time
     *
     * @mbg.generated
     */
    private Long createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hpb_data_dic.show_seq
     *
     * @mbg.generated
     */
    private Integer showSeq;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hpb_data_dic.update_time
     *
     * @mbg.generated
     */
    private Long updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column hpb_data_dic.user_id
     *
     * @mbg.generated
     */
    private Integer userId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hpb_data_dic.id
     *
     * @return the value of hpb_data_dic.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hpb_data_dic.id
     *
     * @param id the value for hpb_data_dic.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hpb_data_dic.data_type_no
     *
     * @return the value of hpb_data_dic.data_type_no
     *
     * @mbg.generated
     */
    public Integer getDataTypeNo() {
        return dataTypeNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hpb_data_dic.data_type_no
     *
     * @param dataTypeNo the value for hpb_data_dic.data_type_no
     *
     * @mbg.generated
     */
    public void setDataTypeNo(Integer dataTypeNo) {
        this.dataTypeNo = dataTypeNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hpb_data_dic.data_type_name
     *
     * @return the value of hpb_data_dic.data_type_name
     *
     * @mbg.generated
     */
    public String getDataTypeName() {
        return dataTypeName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hpb_data_dic.data_type_name
     *
     * @param dataTypeName the value for hpb_data_dic.data_type_name
     *
     * @mbg.generated
     */
    public void setDataTypeName(String dataTypeName) {
        this.dataTypeName = dataTypeName == null ? null : dataTypeName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hpb_data_dic.data_no
     *
     * @return the value of hpb_data_dic.data_no
     *
     * @mbg.generated
     */
    public String getDataNo() {
        return dataNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hpb_data_dic.data_no
     *
     * @param dataNo the value for hpb_data_dic.data_no
     *
     * @mbg.generated
     */
    public void setDataNo(String dataNo) {
        this.dataNo = dataNo == null ? null : dataNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hpb_data_dic.data_name
     *
     * @return the value of hpb_data_dic.data_name
     *
     * @mbg.generated
     */
    public String getDataName() {
        return dataName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hpb_data_dic.data_name
     *
     * @param dataName the value for hpb_data_dic.data_name
     *
     * @mbg.generated
     */
    public void setDataName(String dataName) {
        this.dataName = dataName == null ? null : dataName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hpb_data_dic.state
     *
     * @return the value of hpb_data_dic.state
     *
     * @mbg.generated
     */
    public String getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hpb_data_dic.state
     *
     * @param state the value for hpb_data_dic.state
     *
     * @mbg.generated
     */
    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hpb_data_dic.data_no_len
     *
     * @return the value of hpb_data_dic.data_no_len
     *
     * @mbg.generated
     */
    public Integer getDataNoLen() {
        return dataNoLen;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hpb_data_dic.data_no_len
     *
     * @param dataNoLen the value for hpb_data_dic.data_no_len
     *
     * @mbg.generated
     */
    public void setDataNoLen(Integer dataNoLen) {
        this.dataNoLen = dataNoLen;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hpb_data_dic.limit_flag
     *
     * @return the value of hpb_data_dic.limit_flag
     *
     * @mbg.generated
     */
    public String getLimitFlag() {
        return limitFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hpb_data_dic.limit_flag
     *
     * @param limitFlag the value for hpb_data_dic.limit_flag
     *
     * @mbg.generated
     */
    public void setLimitFlag(String limitFlag) {
        this.limitFlag = limitFlag == null ? null : limitFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hpb_data_dic.high_limit
     *
     * @return the value of hpb_data_dic.high_limit
     *
     * @mbg.generated
     */
    public String getHighLimit() {
        return highLimit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hpb_data_dic.high_limit
     *
     * @param highLimit the value for hpb_data_dic.high_limit
     *
     * @mbg.generated
     */
    public void setHighLimit(String highLimit) {
        this.highLimit = highLimit == null ? null : highLimit.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hpb_data_dic.low_limit
     *
     * @return the value of hpb_data_dic.low_limit
     *
     * @mbg.generated
     */
    public String getLowLimit() {
        return lowLimit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hpb_data_dic.low_limit
     *
     * @param lowLimit the value for hpb_data_dic.low_limit
     *
     * @mbg.generated
     */
    public void setLowLimit(String lowLimit) {
        this.lowLimit = lowLimit == null ? null : lowLimit.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hpb_data_dic.effect_date
     *
     * @return the value of hpb_data_dic.effect_date
     *
     * @mbg.generated
     */
    public Long getEffectDate() {
        return effectDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hpb_data_dic.effect_date
     *
     * @param effectDate the value for hpb_data_dic.effect_date
     *
     * @mbg.generated
     */
    public void setEffectDate(Long effectDate) {
        this.effectDate = effectDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hpb_data_dic.expire_date
     *
     * @return the value of hpb_data_dic.expire_date
     *
     * @mbg.generated
     */
    public Long getExpireDate() {
        return expireDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hpb_data_dic.expire_date
     *
     * @param expireDate the value for hpb_data_dic.expire_date
     *
     * @mbg.generated
     */
    public void setExpireDate(Long expireDate) {
        this.expireDate = expireDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hpb_data_dic.create_time
     *
     * @return the value of hpb_data_dic.create_time
     *
     * @mbg.generated
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hpb_data_dic.create_time
     *
     * @param createTime the value for hpb_data_dic.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hpb_data_dic.show_seq
     *
     * @return the value of hpb_data_dic.show_seq
     *
     * @mbg.generated
     */
    public Integer getShowSeq() {
        return showSeq;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hpb_data_dic.show_seq
     *
     * @param showSeq the value for hpb_data_dic.show_seq
     *
     * @mbg.generated
     */
    public void setShowSeq(Integer showSeq) {
        this.showSeq = showSeq;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hpb_data_dic.update_time
     *
     * @return the value of hpb_data_dic.update_time
     *
     * @mbg.generated
     */
    public Long getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hpb_data_dic.update_time
     *
     * @param updateTime the value for hpb_data_dic.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column hpb_data_dic.user_id
     *
     * @return the value of hpb_data_dic.user_id
     *
     * @mbg.generated
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column hpb_data_dic.user_id
     *
     * @param userId the value for hpb_data_dic.user_id
     *
     * @mbg.generated
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hpb_data_dic
     *
     * @mbg.generated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        HpbDataDic other = (HpbDataDic) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDataTypeNo() == null ? other.getDataTypeNo() == null : this.getDataTypeNo().equals(other.getDataTypeNo()))
            && (this.getDataTypeName() == null ? other.getDataTypeName() == null : this.getDataTypeName().equals(other.getDataTypeName()))
            && (this.getDataNo() == null ? other.getDataNo() == null : this.getDataNo().equals(other.getDataNo()))
            && (this.getDataName() == null ? other.getDataName() == null : this.getDataName().equals(other.getDataName()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()))
            && (this.getDataNoLen() == null ? other.getDataNoLen() == null : this.getDataNoLen().equals(other.getDataNoLen()))
            && (this.getLimitFlag() == null ? other.getLimitFlag() == null : this.getLimitFlag().equals(other.getLimitFlag()))
            && (this.getHighLimit() == null ? other.getHighLimit() == null : this.getHighLimit().equals(other.getHighLimit()))
            && (this.getLowLimit() == null ? other.getLowLimit() == null : this.getLowLimit().equals(other.getLowLimit()))
            && (this.getEffectDate() == null ? other.getEffectDate() == null : this.getEffectDate().equals(other.getEffectDate()))
            && (this.getExpireDate() == null ? other.getExpireDate() == null : this.getExpireDate().equals(other.getExpireDate()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getShowSeq() == null ? other.getShowSeq() == null : this.getShowSeq().equals(other.getShowSeq()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hpb_data_dic
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDataTypeNo() == null) ? 0 : getDataTypeNo().hashCode());
        result = prime * result + ((getDataTypeName() == null) ? 0 : getDataTypeName().hashCode());
        result = prime * result + ((getDataNo() == null) ? 0 : getDataNo().hashCode());
        result = prime * result + ((getDataName() == null) ? 0 : getDataName().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        result = prime * result + ((getDataNoLen() == null) ? 0 : getDataNoLen().hashCode());
        result = prime * result + ((getLimitFlag() == null) ? 0 : getLimitFlag().hashCode());
        result = prime * result + ((getHighLimit() == null) ? 0 : getHighLimit().hashCode());
        result = prime * result + ((getLowLimit() == null) ? 0 : getLowLimit().hashCode());
        result = prime * result + ((getEffectDate() == null) ? 0 : getEffectDate().hashCode());
        result = prime * result + ((getExpireDate() == null) ? 0 : getExpireDate().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getShowSeq() == null) ? 0 : getShowSeq().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        return result;
    }
}