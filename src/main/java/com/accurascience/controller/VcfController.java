package com.accurascience.controller;

import java.util.Map;
import java.util.UUID;

import com.accurascience.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.accurascience.service.VcfFileService;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件上传控制器
 * @author zhuchaojie
 *
 */
@RestController
public class VcfController {
	@Autowired
	private VcfFileService vfs;

	/**
	 * 上传vcf文件，并解析
	 * @param vcfFile
	 * @param geneType
	 * @param omimIdArray
	 * @param patientName
	 * @param email
	 * @return
	 */
    @PostMapping("/upload/vcf")
	public Map<String, Object> vcfUpload(@RequestParam("vcf") MultipartFile vcfFile, @RequestParam("gene_type") String geneType, 
			@RequestParam("omim_id_array") String omimIdArray, @RequestParam("patientId") String patientId
			, @RequestParam("doctorId") String doctorId
			, @RequestParam("email") String email) {
    	//id
		UUID uuid = UUID.randomUUID();
    	//调用vcf原始信息存入
    	Map<String, Object> result = vfs.insert(vcfFile, geneType, omimIdArray, patientId, doctorId, email, uuid);
    	//调用vcf解析
        vfs.vcfDecode(vcfFile, geneType, omimIdArray, email, uuid);
    	//返回分析结果
		return result;
	}
	/**
	 * 返回医生下属的vcf解析列表和数据大小（只展示已经处理好的列表）
	 * @param pageNumber 页码
	 * @param pageSize 页面大小
	 * @param doctorId 医生ID
	 */
//	@GetMapping("/vcf/list")
//	public Map<String, Object> listVcfResult(@RequestParam("pageNumber") Integer pageNumber
//			, @RequestParam("pageSize") Integer pageSize, @RequestParam("doctorId") String doctorId){
//
//		Map<String, Object> result = vfs.listVcfResult(pageNumber, pageSize, doctorId);
//		return result;
//	}

	/**
	 * 统计未处理,已处理，总数完成的vcf数量
	 * @param doctorId
	 * @return 数量
	 */
	@GetMapping("/vcf/processCount")
	public Map<String, Integer> handleStatus(@RequestParam("doctorId")String doctorId) {
		return vfs.handleStatus(doctorId);
	}
	/**
	 * 展示解析详情
	 * @param id
	 */
	@GetMapping("/vcf/detail")
	public Map<String, Object> jsonResult(@RequestParam("id")String id){
		Map<String, Object> result = vfs.jsonResult(id);
		return result;

	}
	/**
	 * 删除vcf解析结果
	 * @param id
	 * @return
	 */
	@GetMapping("/vcf/delete")
	public String deleteVcf(@RequestParam(value="id") String id) {
		String msg = vfs.deleteVcf(id);
		return msg;
	}
	/**
	 * 导出解析报告pdf
	 * @param id
	 * @return
	 */
	@GetMapping("/vcf/export")
	public void exportVcf(@RequestParam(value="id") String id, HttpServletResponse response) {
		vfs.exportPdf(id, response);
	}
}
