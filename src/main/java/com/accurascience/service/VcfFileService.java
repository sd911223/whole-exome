package com.accurascience.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * vcf文件业务逻辑层
 * @author zhuchaojie
 *
 */
public interface VcfFileService {
	/**
	 * 插入vcf文件
	 * @param vcfFile
	 * @return
	 */
	Map<String, Object> insert(MultipartFile vcfFile, String geneType, 
			 String omimIdArray, String patientId, String doctorId, String email, UUID uuid);

	/**
	 * vcf解析
	 * @param vcfFile
	 * @param geneType
	 * @param omimIdArray
	 * @param email
	 * @param uuid
	 */
	void vcfDecode(MultipartFile vcfFile,String geneType,
			  String omimIdArray, String email, UUID uuid);
	/**
	 * 返回医生下属的vcf解析列表和数据大小（只展示已经处理好的列表）
	 * @param pageNumber 页码
	 * @param pageSize 页面大小
	 * @param doctorId 医生ID
	 */
//	Map<String, Object> listVcfResult(Integer pageNumber, Integer pageSize, String doctorId);

	/**
	 * 统计未处理完成的vcf的数量
	 * @param doctorId 医生ID
	 * @return 未处理完成的vcf数量
	 */
	Map<String, Integer> handleStatus(String doctorId);
	/**
	 * 展示解析详情
	 * @param id
	 */
	Map<String, Object> jsonResult(String id);
	/**
	 * 删除解析结果
	 * @param id
	 */
	String deleteVcf(String id);

	/**
	 * 导出pdf报告
	 * @param id
	 */
	void exportPdf(String id, HttpServletResponse response);
}
