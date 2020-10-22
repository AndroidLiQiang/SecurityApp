package com.example.mobiletest.bean;

import java.io.Serializable;

public class UserInfoBean implements Serializable {
    /**
     * userInfoBean : {"age":"24","name":"何嘉晨","sex":"男"}
     * code : 200
     * message : 登录成功
     */

    private UserInfoBeanBean userInfoBean;

    public UserInfoBeanBean getUserInfoBean() {
        return userInfoBean;
    }

    public void setUserInfoBean(UserInfoBeanBean userInfoBean) {
        this.userInfoBean = userInfoBean;
    }

    public static class UserInfoBeanBean {
        /**
         * age : 24
         * name : 何嘉晨
         * sex : 男
         */

        private String age;
        private String name;
        private String sex;

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }
}
