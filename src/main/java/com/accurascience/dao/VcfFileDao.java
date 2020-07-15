package com.accurascience.dao;

import org.apache.ibatis.annotations.*;

import com.accurascience.entity.VcfFile;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Map;

/**
 * vcf文件持久层（mybatis实现）
 * @author zhuchaojie
 *
 */

@Mapper
public interface VcfFileDao {
	/**
	 * 插入vcf文件
	 * @param vcfFile
	 * @return 执行状态
	 */
    @Insert("INSERT INTO vcf_file(id, file_name, file, create_date, patient_id, json_result, doctor_id, status) VALUES "
    		+ "(#{vcfFile.id}, #{vcfFile.fileName}, #{vcfFile.file}, #{vcfFile.createDate}"
    		+ ", #{vcfFile.patientId}, #{vcfFile.jsonResult}, #{vcfFile.doctorId}, #{vcfFile.status})")
	Integer insert(@Param("vcfFile")VcfFile vcfFile);
	/**
	 * 更新解析结果以及处理状态
	 * @param jsonResult
	 * @param id 表ID
	 * @param status 处理状态
	 * @return 执行状态
	 */
	@Update("update vcf_file set json_result=#{jsonResult}, status=#{status} where id=#{id}")
	Integer updateResult(@Param("jsonResult")String jsonResult, @Param("status")Integer status, @Param("id")String id);
	/**
	 * 更新处理状态
	 * @param status
	 * @param id 表id
	 * @return 执行状态
	 */
//	@Update("update vcf_file set status=#{status} where id=#{id}")
//	Integer updateStatus(@Param("status")String status, @Param("id")String id);

	/**
	 * 统计未处理完成的vcf的数量
	 * @param doctorId 医生ID
	 * @return 未处理完成的vcf数量
	 */
	@Select("select count(1) from vcf_file where doctor_id=#{doctorId} and status=0")
	Integer handleStatus(@Param("doctorId")String doctorId);
	/**
	 * 返回医生下属的vcf解析列表
	 * @param patientId 患者ID
	 */
	@Results({
			@Result(column="id", property="id", jdbcType= JdbcType.VARCHAR, id=true),
			@Result(column="file_name", property="fileName", jdbcType=JdbcType.VARCHAR),
			@Result(column="create_date", property="createDate", jdbcType=JdbcType.TIMESTAMP),
	})
	@Select("select v.id, v.file_name, date_format(v.create_date, '%Y-%m-%d %H:%i:%s') as create_date, " +
			"CAST(v.status AS char) as status from vcf_file as v " +
			" where v.patient_id=#{patientId}")
	List<Map<String, String>> listVcfResult(@Param("patientId")String patientId);
	/**
	 * 返回医生下属的vcf解析数量（只展示已经处理好的列表）
	 * @param doctorId 医生ID
	 */
	@Select("select count(1) from vcf_file where doctor_id=#{doctorId} and status=1")
	Integer countVcfResult(@Param("doctorId")String doctorId);
	/**
	 * 展示解析详情
	 * @param id
	 */
	@Select("select json_result from vcf_file where id=#{id}")
	String jsonResult(@Param("id")String id);
	/**
	 * 删除解析结果
	 * @param id
	 */
	@Delete("delete from vcf_file where id=#{id}")
	Integer deleteVcf(@Param("id")String id);
	/**
	 * 删除解析结果
	 * @param id
	 */
	@Delete("delete from vcf_file where patient_id=#{patientId}")
	Integer deleteVcfByPatient(@Param("patientId")String patientId);
}
