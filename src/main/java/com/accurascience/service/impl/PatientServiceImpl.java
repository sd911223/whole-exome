package com.accurascience.service.impl;

import com.accurascience.dao.PatientDao;
import com.accurascience.dao.VcfFileDao;
import com.accurascience.entity.Patient;
import com.accurascience.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PatientServiceImpl implements PatientService {
    private static final Logger logger = LoggerFactory.getLogger(PatientServiceImpl.class);
    @Autowired
    private PatientDao pd;
    @Autowired
    private VcfFileDao vfd;
    @Override
    public  Map<String, Object> patientListByDoctorId(Integer pageNumber, Integer pageSize, String doctorId
            , String inputContent) {
        Map<String, Object> map = new HashMap<String, Object>(2);//结果集合
        //防止过多占用内存
        if(pageSize > 20){
            pageSize = 20;
        }
        //计算数据库起始索引的值
        Integer startIndex = pageNumber*pageSize-pageSize;
        //查询数据数量
        Integer count = pd.countPatientByDoctorId(doctorId, inputContent);
        if(count==0){
            map.put("data", new ArrayList<>(0));
            map.put("count",0);
            return map;
        }
        //执行查询sql
        List<Patient> list =pd.patientListByDoctorId(startIndex, pageSize, doctorId, inputContent);
        //添加vcf列表
        for(int i=0;i<list.size();i++){
            String patientId = list.get(i).getPatientId();
            List<Map<String, String>> vcf = vfd.listVcfResult(patientId);
            list.get(i).setVcf(vcf);
        }
        //生成结果
        map.put("data", list);
        map.put("count",count);
        return map;
    }

    @Override
    public String insertUser(Integer age, String sex, String doctorId, String patientName) {
        Patient p = new Patient();
        //id
        UUID uuid = UUID.randomUUID();
        p.setPatientId(uuid.toString().replaceAll("-", ""));
        p.setAge(age);
        p.setSex(sex);
        p.setPatientName(patientName);
        p.setDoctorId(doctorId);
        Integer count = pd.insertUser(p);
        String msg = count==1?"success":"failed";
        logger.warn("医生执行了添加患者的操作。成功。");
        return msg;
    }

    @Override
    public Patient patientDetail(String patientId) {
        return pd.patientDetail(patientId);
    }

    @Override
    @Transactional//事务管理
    public String deletePatient(String patientId) {
        //删除患者
        pd.deletePatient(patientId);
        //删除患者下属的vcf解析
        Integer result = vfd.deleteVcfByPatient(patientId);
        return result==1?"success":"failed";
    }

}
