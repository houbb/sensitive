package com.github.houbb.sensitive.test.model.sensitive;

import com.github.houbb.sensitive.annotation.Sensitive;
import com.github.houbb.sensitive.core.api.strategory.*;

/**
 * @author binbin.hou
 * date 2018/12/29
 * @since 0.0.1
 */
public class UserIdNo {

    @Sensitive(strategy = StrategyChineseName.class)
    private String username;

    @Sensitive(strategy = StrategyCardId.class)
    private String idCard;

    @Sensitive(strategy = StrategyPassword.class)
    private String password;

    @Sensitive(strategy = StrategyEmail.class)
    private String email;

    @Sensitive(strategy = StrategyPhone.class)
    private String phone;

    @Sensitive(strategy = StrategyIdNo.class)
    private String idNo;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    @Override
    public String toString() {
        return "UserIdNo{" +
                "username='" + username + '\'' +
                ", idCard='" + idCard + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", idNo='" + idNo + '\'' +
                '}';
    }

}
