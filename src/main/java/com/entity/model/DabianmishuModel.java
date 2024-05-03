package com.entity.model;

import com.entity.DabianmishuEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;


/**
 * 答辩秘书
 * 接收传参的实体类
 *（实际开发中配合移动端接口开发手动去掉些没用的字段， 后端一般用entity就够用了）
 * 取自ModelAndView 的model名称
 */
public class DabianmishuModel implements Serializable {
    private static final long serialVersionUID = 1L;




    /**
     * 主键
     */
    private Integer id;


    /**
     * 账户
     */
    private String username;


    /**
     * 密码
     */
    private String password;


    /**
     * 答辩秘书姓名
     */
    private String dabianmishuName;


    /**
     * 头像
     */
    private String dabianmishuPhoto;


    /**
     * 性别
     */
    private Integer sexTypes;


    /**
     * 联系方式
     */
    private String dabianmishuPhone;


    /**
     * 邮箱
     */
    private String dabianmishuEmail;


    /**
     * 添加时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    private Date insertTime;


    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    private Date createTime;


    /**
	 * 获取：主键
	 */
    public Integer getId() {
        return id;
    }


    /**
	 * 设置：主键
	 */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
	 * 获取：账户
	 */
    public String getUsername() {
        return username;
    }


    /**
	 * 设置：账户
	 */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
	 * 获取：密码
	 */
    public String getPassword() {
        return password;
    }


    /**
	 * 设置：密码
	 */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
	 * 获取：答辩秘书姓名
	 */
    public String getDabianmishuName() {
        return dabianmishuName;
    }


    /**
	 * 设置：答辩秘书姓名
	 */
    public void setDabianmishuName(String dabianmishuName) {
        this.dabianmishuName = dabianmishuName;
    }
    /**
	 * 获取：头像
	 */
    public String getDabianmishuPhoto() {
        return dabianmishuPhoto;
    }


    /**
	 * 设置：头像
	 */
    public void setDabianmishuPhoto(String dabianmishuPhoto) {
        this.dabianmishuPhoto = dabianmishuPhoto;
    }
    /**
	 * 获取：性别
	 */
    public Integer getSexTypes() {
        return sexTypes;
    }


    /**
	 * 设置：性别
	 */
    public void setSexTypes(Integer sexTypes) {
        this.sexTypes = sexTypes;
    }
    /**
	 * 获取：联系方式
	 */
    public String getDabianmishuPhone() {
        return dabianmishuPhone;
    }


    /**
	 * 设置：联系方式
	 */
    public void setDabianmishuPhone(String dabianmishuPhone) {
        this.dabianmishuPhone = dabianmishuPhone;
    }
    /**
	 * 获取：邮箱
	 */
    public String getDabianmishuEmail() {
        return dabianmishuEmail;
    }


    /**
	 * 设置：邮箱
	 */
    public void setDabianmishuEmail(String dabianmishuEmail) {
        this.dabianmishuEmail = dabianmishuEmail;
    }
    /**
	 * 获取：添加时间
	 */
    public Date getInsertTime() {
        return insertTime;
    }


    /**
	 * 设置：添加时间
	 */
    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
    /**
	 * 获取：创建时间
	 */
    public Date getCreateTime() {
        return createTime;
    }


    /**
	 * 设置：创建时间
	 */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    }
