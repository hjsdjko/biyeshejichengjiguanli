package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * 答辩秘书
 *
 * @author 
 * @email
 */
@TableName("dabianmishu")
public class DabianmishuEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public DabianmishuEntity() {

	}

	public DabianmishuEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @TableField(value = "id")

    private Integer id;


    /**
     * 账户
     */
    @TableField(value = "username")

    private String username;


    /**
     * 密码
     */
    @TableField(value = "password")

    private String password;


    /**
     * 答辩秘书姓名
     */
    @TableField(value = "dabianmishu_name")

    private String dabianmishuName;


    /**
     * 头像
     */
    @TableField(value = "dabianmishu_photo")

    private String dabianmishuPhoto;


    /**
     * 性别
     */
    @TableField(value = "sex_types")

    private Integer sexTypes;


    /**
     * 联系方式
     */
    @TableField(value = "dabianmishu_phone")

    private String dabianmishuPhone;


    /**
     * 邮箱
     */
    @TableField(value = "dabianmishu_email")

    private String dabianmishuEmail;


    /**
     * 添加时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @TableField(value = "insert_time",fill = FieldFill.INSERT)

    private Date insertTime;


    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @TableField(value = "create_time",fill = FieldFill.INSERT)

    private Date createTime;


    /**
	 * 设置：主键
	 */
    public Integer getId() {
        return id;
    }
    /**
	 * 获取：主键
	 */

    public void setId(Integer id) {
        this.id = id;
    }
    /**
	 * 设置：账户
	 */
    public String getUsername() {
        return username;
    }
    /**
	 * 获取：账户
	 */

    public void setUsername(String username) {
        this.username = username;
    }
    /**
	 * 设置：密码
	 */
    public String getPassword() {
        return password;
    }
    /**
	 * 获取：密码
	 */

    public void setPassword(String password) {
        this.password = password;
    }
    /**
	 * 设置：答辩秘书姓名
	 */
    public String getDabianmishuName() {
        return dabianmishuName;
    }
    /**
	 * 获取：答辩秘书姓名
	 */

    public void setDabianmishuName(String dabianmishuName) {
        this.dabianmishuName = dabianmishuName;
    }
    /**
	 * 设置：头像
	 */
    public String getDabianmishuPhoto() {
        return dabianmishuPhoto;
    }
    /**
	 * 获取：头像
	 */

    public void setDabianmishuPhoto(String dabianmishuPhoto) {
        this.dabianmishuPhoto = dabianmishuPhoto;
    }
    /**
	 * 设置：性别
	 */
    public Integer getSexTypes() {
        return sexTypes;
    }
    /**
	 * 获取：性别
	 */

    public void setSexTypes(Integer sexTypes) {
        this.sexTypes = sexTypes;
    }
    /**
	 * 设置：联系方式
	 */
    public String getDabianmishuPhone() {
        return dabianmishuPhone;
    }
    /**
	 * 获取：联系方式
	 */

    public void setDabianmishuPhone(String dabianmishuPhone) {
        this.dabianmishuPhone = dabianmishuPhone;
    }
    /**
	 * 设置：邮箱
	 */
    public String getDabianmishuEmail() {
        return dabianmishuEmail;
    }
    /**
	 * 获取：邮箱
	 */

    public void setDabianmishuEmail(String dabianmishuEmail) {
        this.dabianmishuEmail = dabianmishuEmail;
    }
    /**
	 * 设置：添加时间
	 */
    public Date getInsertTime() {
        return insertTime;
    }
    /**
	 * 获取：添加时间
	 */

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
    /**
	 * 设置：创建时间
	 */
    public Date getCreateTime() {
        return createTime;
    }
    /**
	 * 获取：创建时间
	 */

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Dabianmishu{" +
            "id=" + id +
            ", username=" + username +
            ", password=" + password +
            ", dabianmishuName=" + dabianmishuName +
            ", dabianmishuPhoto=" + dabianmishuPhoto +
            ", sexTypes=" + sexTypes +
            ", dabianmishuPhone=" + dabianmishuPhone +
            ", dabianmishuEmail=" + dabianmishuEmail +
            ", insertTime=" + insertTime +
            ", createTime=" + createTime +
        "}";
    }
}
