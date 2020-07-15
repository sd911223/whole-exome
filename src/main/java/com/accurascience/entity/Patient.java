package com.accurascience.entity;

import java.util.List;
import java.util.Map;

/**
 * 患者信息表
 */
public class Patient {
    private String patientId;
    private String patientName;
    private String sex;
    private Integer age;
    private String doctorId;
    private List<Map<String, String>> vcf;//vcf列表

    public List<Map<String, String>> getVcf() {
        return vcf;
    }

    public void setVcf(List<Map<String, String>> vcf) {
        this.vcf = vcf;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
}
