package com.accurascience.service;

import com.accurascience.entity.Patient;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 *患者相关业务逻辑层
 */
public interface PatientService {
    /**
     * 查询患者信息
     * @param doctorId
     * @param inputContent 用户输入查询内容
     * @return
     */
    Map<String, Object> patientListByDoctorId(Integer pageNumber, Integer pageSize, String doctorId, String inputContent);
    /**
     * 添加新患者
     * @param age
     * @return
     */
    String insertUser(Integer age, String sex, String doctorId, String patientName);
    /**
     * 展示患者详情
     * @param patientId
     * @return
     */
    Patient patientDetail(String patientId);

    /**
     * 删除患者
     * @param patientId
     * @return
     */
    String deletePatient(String patientId);

}
