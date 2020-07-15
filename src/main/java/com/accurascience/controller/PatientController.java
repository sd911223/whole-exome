package com.accurascience.controller;

import com.accurascience.entity.Patient;
import com.accurascience.service.PatientService;
import com.accurascience.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 患者控制器
 * @Author zhuchaojie
 */
@RestController
public class PatientController {
    @Autowired
    private PatientService ps;
    /**
     * 患者信息查询
     * @param doctorId 医生id
     * @param inputContent 用户输入查询内容
     * @return 患者列表
     */
    @RequestMapping(value = { "/patient/list" })
    public Map<String , Object> patientListByDoctorId(@RequestParam(value="pageNumber") Integer pageNumber
            , @RequestParam(value="pageSize") Integer pageSize, @RequestParam(value="doctorId") String doctorId
            , @RequestParam(value="inputContent",required = false, defaultValue = "")String inputContent) {
        Map<String , Object> result = ps.patientListByDoctorId(pageNumber, pageSize, doctorId, inputContent);
        return result;
    }

    /**
     * 患者添加
     * @param patientName
     * @param sex
     * @param age
     * @param doctorId
     * @return 添加状态
     */
    @PostMapping("/patient/add")
    public String insertUser(@RequestParam(value="patientName") String patientName, @RequestParam(value="sex") String sex,
                             @RequestParam(value="age") Integer age, @RequestParam(value="doctorId") String doctorId) {
        String msg = ps.insertUser(age,sex,doctorId,patientName);
        return msg;

    }
    /**
     * 患者详情查询
     * @param patientId
     * @return 患者详情
     */
    @GetMapping("/patient/detail")
    public Patient userDetail(@RequestParam(value="patientId") String patientId) {
        Patient patient = ps.patientDetail(patientId);
        return patient;

    }
    /**
     * 删除患者
     * @param patientId
     * @return
     */
    @GetMapping("/patient/delete")
    public String deletePatient(@RequestParam(value="patientId") String patientId) {
        String msg = ps.deletePatient(patientId);
        return msg;

    }
}
