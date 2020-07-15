package com.accurascience.dao;

import com.accurascience.entity.Patient;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Map;

/**
 * 患者相关持久层
 */
@Mapper
public interface PatientDao {
    //表字段与实体类映射
    @Results({
            @Result(column="patient_id", property="patientId", jdbcType= JdbcType.VARCHAR, id=true),
            @Result(column="patient_name", property="patientName", jdbcType=JdbcType.VARCHAR),
            @Result(column="sex", property="sex", jdbcType=JdbcType.VARCHAR),
            @Result(column="age", property="age", jdbcType=JdbcType.INTEGER),
            @Result(column="doctor_id", property="doctorId", jdbcType=JdbcType.VARCHAR),
    })
    /**
     * 查询患者信息列表
     * @param doctorId
     * @return
     */
    @Select("<script>" +
            "select * from patient  where doctor_id=#{doctorId} " +
            "<if test='inputContent!=\"\"'> and patient_name like'%' #{inputContent} '%' </if>"+
            " limit #{startIndex} , #{pageSize}" +
            "</script>")
    List<Patient> patientListByDoctorId(@Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize
            , @Param("doctorId") String doctorId, @Param("inputContent") String inputContent);
    /**
     * 统计患者数量
     * @param doctorId
     * @return
     */
    @Select("<script>" +
            "select count(1) from patient  where doctor_id=#{doctorId} " +
            "<if test='inputContent!=\"\"'> and patient_name like'%' #{inputContent} '%' </if>"+
            "</script>")
    Integer countPatientByDoctorId(@Param("doctorId") String doctorId, @Param("inputContent") String inputContent);
    /**
     * 添加新患者
     * @param patient
     * @return
     */
    @Insert("INSERT INTO patient(patient_id, patient_name, sex, age, doctor_id) VALUES "
            + "(#{patient.patientId}, #{patient.patientName}, #{patient.sex}, #{patient.age}, #{patient.doctorId})")
    Integer insertUser(@Param("patient") Patient patient);
    /**
     * 模糊查询为用户提供输入建议
     * @param doctorId
     * @param content 用户输入内容
     * @return
     */
//    @Results({
//            @Result(column="patient_id", property="patientId", jdbcType= JdbcType.VARCHAR, id=true),
//            @Result(column="patient_name", property="patientName", jdbcType=JdbcType.VARCHAR),
//    })
//    @Select("select patient_id, patient_name from patient  where doctor_id=#{doctorId} and patient_name like'%' #{content} '%'")
//    List<Map<String, String>> patientListForInput(@Param("doctorId") String doctorId, @Param("content") String content);
    /**
     * 展示患者详情
     * @param patientId
     * @return
     */
    @Select("select * from patient  where patient_id=#{patientId}")
    Patient patientDetail(@Param("patientId") String patientId);

    /**
     * 删除患者
     * @param patientId
     * @return
     */
    @Delete("delete from patient where patient_id=#{patientId}")
    Integer deletePatient(@Param("patientId") String patientId);
}
