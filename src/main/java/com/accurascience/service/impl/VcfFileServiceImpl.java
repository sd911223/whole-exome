package com.accurascience.service.impl;

import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;

import com.accurascience.util.EmailUtil;
import com.accurascience.util.PdfUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.accurascience.dao.VcfFileDao;
import com.accurascience.entity.VcfFile;
import com.accurascience.service.VcfFileService;
import com.accurascience.util.ShellUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
@Service
public class VcfFileServiceImpl implements VcfFileService {
	private static final Logger logger = LoggerFactory.getLogger(VcfFileService.class);
    @Autowired
    private VcfFileDao vfd;
    @Value("${vcf.file.path}")
	private String path; 
    @Autowired
    private ShellUtil su;
    @Autowired
	private EmailUtil eu;
    @Autowired
	private PdfUtil pu;
	@SuppressWarnings("unchecked")
	@Override
	//@Transactional//事务控制
	public Map<String, Object> insert(MultipartFile vcfFile, String geneType,
			 String omimIdArray, String patientId, String doctorId, String email, UUID uuid ) {
		// TODO Auto-generated method stub
		//Map<String, String> map = new LinkedHashMap<String, String>(1);
		Map<String, Object> msg = new HashMap<String, Object>(1);
		//npe
		if(vcfFile == null) {
			return (Map<String, Object>) msg.put("msg", "no-data");
		}
		//文件名
		String fileName = vcfFile.getOriginalFilename();
		//文件类型判断
		if(!fileName.toLowerCase().endsWith(".vcf")) {
		
			return (Map<String, Object>) msg.put("msg", "file_type_error");
		}
		//文件类型转换
		Blob blob;
		try {
			blob = new SerialBlob(vcfFile.getBytes());
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block

			return (Map<String, Object>) msg.put("msg", "file_type_change_error");
		}
		//生成参数对象
		VcfFile vf = new VcfFile();
		vf.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));//创建日期
		//System.out.println(Timestamp.valueOf(LocalDateTime.now()));
		vf.setFile(null);//文件,暂时不存入文件
		vf.setFileName(fileName);//文件名
		//UUID uuid = UUID.randomUUID();
        vf.setId(uuid.toString().replaceAll("-", "").trim());//id
		//患者id
        vf.setPatientId(patientId);
        //医生ID
		vf.setDoctorId(doctorId);
		//处理状态(0:未开始处理 1：处理完成 -1：处理异常)
		vf.setStatus(0);
        //解析之前，存入原始文件
		vfd.insert(vf);

		//把vcf存入文件夹
		File file = new File(path+uuid.toString().replaceAll("-", "").trim()+"/"+fileName);
		//获取父目录
		File fileParent = file.getParentFile();
		if (!fileParent.exists()){
			fileParent.mkdirs();
		}
		//储存文件
		try {
			file.createNewFile();
			logger.info("文件是否存在："+file.exists());
			FileUtils.copyInputStreamToFile(vcfFile.getInputStream(),file);
		} catch (IOException e) {
			e.printStackTrace();
		}


		//开始解析
//        StringBuffer sb = new StringBuffer();
//        //把vcf存入文件夹
//		File file = new File(path+uuid.toString().replaceAll("-", "").trim()+"/"+fileName);
//		//获取父目录
//		File fileParent = file.getParentFile();
//		if (!fileParent.exists()){
//			fileParent.mkdirs();
//		}
//		try {
//			file.createNewFile();
//			vcfFile.transferTo(file);
//			logger.info("文件是否存在："+file.exists());
//			//执行vcf解析
//			int msg = su.analysisVcf(path+uuid.toString().replaceAll("-", "").trim()+"/"+fileName, geneType,
//	     		path+uuid.toString().replaceAll("-", "").trim()+"/operate", omimIdArray);
//			if(msg == 0) {//成功执行shell脚本
//				//读取解析之后的结果json文件
//				//新分析结果
//				File jsonFile = new File(path+uuid.toString().replaceAll("-", "").trim()+"/operate"+"/tables.json");
//		        BufferedInputStream in = new BufferedInputStream(new FileInputStream(jsonFile));
//		        BufferedReader br = new BufferedReader(new InputStreamReader(in));
//		        String line;
//		        while ((line = br.readLine()) != null) {
//		        	sb.append(line);
//		        }
//		        map.put("data", sb.toString());
//		        //sb.setLength(0);
//		        br.close();
//		        in.close();
//
//
//			}
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			logger.error(fileName+"上传失败！");
//			//设置处理状态为：-1
//			vfd.updateResult("",-1,uuid.toString().replaceAll("-", "").trim());
//			return (Map<String, Object>) error.put("msg", "file_error");
//
//		}
//		//将解析结果插入数据库
//		Integer count = vfd.updateResult(sb.toString(),1,uuid.toString().replaceAll("-", "").trim());
//		vf = null;
//		blob = null;
//		//解析结束
//
//		if(count == 1) {
//			logger.info(fileName+"上传成功！");
//			//发送邮件
//            eu.sendHtmlMail(email, "vcf解析任务通知", "<h2>您的"+fileName+"解析任务完成了！</h2>");
//			return this.getJsonResult(map);//转化成json对象并返回
//		}else {
//			logger.error(fileName+"上传失败！");
//			//return "db_error";
//			return (Map<String, Object>) error.put("msg", "db_error");
//		}
		//异步的方式解析vcf
		//this.vcfDecode(vcfFile, geneType, omimIdArray, email, uuid);
		//返回信息
		msg.put("msg", "file-uploaded");
		return msg;
	}

	/**
	 * vcf解析
	 * @param vcfFile
	 * @param geneType
	 * @param omimIdArray
	 * @param email
	 * @return
	 */
	@Override
	@Async("taskExecutor")
	public void vcfDecode(MultipartFile vcfFile,String geneType,
										 String omimIdArray, String email, UUID uuid) {
		// TODO Auto-generated method stub
		//Map<String, String> map = new LinkedHashMap<String, String>(1);
		//Map<String, Object> error = new HashMap<String, Object>(1);
		//文件名
		String fileName = vcfFile.getOriginalFilename();
		//UUID uuid = UUID.randomUUID();

		//开始解析
		StringBuffer sb = new StringBuffer();
		logger.error(uuid.toString());
		try {
			//执行vcf解析
			int msg = su.analysisVcf(path+uuid.toString().replaceAll("-", "").trim()+"/"+fileName, geneType,
					path+uuid.toString().replaceAll("-", "").trim()+"/operate", omimIdArray);
			if(msg == 0) {//成功执行shell脚本
				//读取解析之后的结果json文件
				//新分析结果
				File jsonFile = new File(path+uuid.toString().replaceAll("-", "").trim()+"/operate"+"/tables.json");
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(jsonFile));
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				//map.put("data", sb.toString());
				//sb.setLength(0);
				br.close();
				in.close();


			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			logger.error(fileName+"上传失败！");
			//设置处理状态为：-1
			vfd.updateResult("",-1,uuid.toString().replaceAll("-", "").trim());
			//return (Map<String, Object>) error.put("msg", "file_error");

		}
		//将解析结果插入数据库
		Integer count = vfd.updateResult(sb.toString(),1,uuid.toString().replaceAll("-", "").trim());
		//解析结束

		if(count == 1) {
			logger.info(fileName+"上传成功！");
			//发送邮件
			eu.sendHtmlMail(email, "vcf解析任务通知", "<h2>您的"+fileName+"解析任务完成了！</h2>");
			//return this.getJsonResult(map);//转化成json对象并返回
		}else {
			logger.error(fileName+"上传失败！");
			//return "db_error";
			//return (Map<String, Object>) map.put("msg", "db_error");
		}
	}

//	@Override
//	public Map<String, Object> listVcfResult(Integer pageNumber, Integer pageSize, String patientId) {
//		Map<String, Object> map = new HashMap<String, Object>(2);//结果集合
//		//防止过多占用内存
//		if(pageSize > 20){
//			pageSize = 20;
//		}
//		//计算数据库起始索引的值
//		Integer startIndex = pageNumber*pageSize-pageSize;
//		//查询数据数量
//		Integer count = vfd.countVcfResult(patientId);
//		if(count==0){
//			map.put("data", new ArrayList<>(0));
//			map.put("count",0);
//			return map;
//		}
//		//执行查询sql
//		List list = vfd.listVcfResult(patientId);
//		//生成结果
//		map.put("data", list);
//		map.put("count",count);
//		return map;
//	}

	@Override
	public Map<String, Integer> handleStatus(String doctorId) {
		Map<String, Integer> map = new HashMap<String, Integer>(3);

		//未处理数量
		Integer processCount = vfd.handleStatus(doctorId);
		map.put("processCount", processCount);
		//已处理完成数量
		Integer processedCount = vfd.countVcfResult(doctorId);
		map.put("processedCount", processedCount);
		//总数量
		map.put("count", processedCount+processCount);
		return map;
	}

	@Override
	public Map<String, Object> jsonResult(String id) {
		//解析结果
		String jsonString = vfd.jsonResult(id);
		//转化为json对象
		Map<String, String> map = new HashMap<String, String>(1);
		map.put("data",jsonString);
		Map<String, Object> result = this.getJsonResult(map);
		return result;
	}

	@Override
	public String deleteVcf(String id) {
		Integer result = vfd.deleteVcf(id);
		return result==1?"success":"failed";
	}

	@Override
	public void exportPdf(String id, HttpServletResponse response) {
		//查询vcf解析详情
		String jsonResult = vfd.jsonResult(id);
		//转化成json对象
		JSONObject json = (JSONObject) JSONObject.parse(jsonResult);
		//响应中写入pdf输出流
		try {
			//清除缓存
			response.reset();
			// 指定下载的文件名
			response.setHeader("Content-Disposition",
					"attachment;filename=vc_report_"+new Date()+".pdf");
			OutputStream out = response.getOutputStream();
            pu.createPDF(json, out);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * json格式化方法
	 */
	private Map<String, Object> getJsonResult(Map<String, String> result ){
		Map<String, Object> cleanResult = new LinkedHashMap<String, Object>(result.size());
		
		for (String key : result.keySet()) {
            
			String value = result.get(key).replaceAll("\\\\'", "\\'")/*单引号处理*/;
			try {
				if(value.startsWith("[") && value.endsWith("]") ) {//数组
					cleanResult.put(key, JSON.parseArray(value));
					continue;
				}
				if(value.startsWith("{") && value.endsWith("}") ) {//对象
					cleanResult.put(key, JSONObject.parseObject(value, Feature.OrderedField));
					
					continue;
					
				}
			} catch (JSONException e) {//异常，存入字符串
				// TODO Auto-generated catch block
				e.printStackTrace();
				cleanResult.put(key, value);
			}
			//一般字符串
			cleanResult.put(key, value);
			
		}
		return cleanResult;
		
	}
	public static void main(String[] args) throws IOException {
		VcfFileServiceImpl  vf = new VcfFileServiceImpl();
		File jsonFile = new File("/Users/zhuchaojie/Desktop/tables.json");
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(jsonFile));  
        BufferedReader br = new BufferedReader(new InputStreamReader(in));  
        String line; 
        String sb ="";
        while ((line = br.readLine()) != null) {  
        	sb += line;
        }
        Map<String, String> map = new HashMap<String, String>(1);
        map.put("data", sb);
        
        System.out.println(JSONObject.toJSONString(vf.getJsonResult(map)));
	}
}
