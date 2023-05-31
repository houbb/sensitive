package com.github.houbb.sensitive.test.model.sensitive;

import com.github.houbb.sensitive.annotation.strategy.*;

/**
 * @author binbin.hou
 * date 2018/12/29
 * @since 0.0.1
 */
public class UserAnnotationBean {

    @SensitiveStrategyChineseName
    private String username;

    @SensitiveStrategyPassword
    private String password;

    @SensitiveStrategyPassport
    private String passport;

    @SensitiveStrategyIdNo
    private String idNo;

    @SensitiveStrategyCardId
    private String bandCardId;

    @SensitiveStrategyPhone
    private String phone;

    @SensitiveStrategyEmail
    private String email;

    @SensitiveStrategyAddress
    private String address;

    @SensitiveStrategyBirthday
    private String birthday;

    @SensitiveStrategyGps
    private String gps;

    @SensitiveStrategyIp
    private String ip;

    @SensitiveStrategyMaskAll
    private String maskAll;

    @SensitiveStrategyMaskHalf
    private String maskHalf;

    @SensitiveStrategyMaskRange
    private String maskRange;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getBandCardId() {
        return bandCardId;
    }

    public void setBandCardId(String bandCardId) {
        this.bandCardId = bandCardId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMaskAll() {
        return maskAll;
    }

    public void setMaskAll(String maskAll) {
        this.maskAll = maskAll;
    }

    public String getMaskHalf() {
        return maskHalf;
    }

    public void setMaskHalf(String maskHalf) {
        this.maskHalf = maskHalf;
    }

    public String getMaskRange() {
        return maskRange;
    }

    public void setMaskRange(String maskRange) {
        this.maskRange = maskRange;
    }

    @Override
    public String toString() {
        return "UserAnnotationBean{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", passport='" + passport + '\'' +
                ", idNo='" + idNo + '\'' +
                ", bandCardId='" + bandCardId + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", birthday='" + birthday + '\'' +
                ", gps='" + gps + '\'' +
                ", ip='" + ip + '\'' +
                ", maskAll='" + maskAll + '\'' +
                ", maskHalf='" + maskHalf + '\'' +
                ", maskRange='" + maskRange + '\'' +
                '}';
    }

}
