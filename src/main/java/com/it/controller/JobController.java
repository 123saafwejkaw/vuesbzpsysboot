package com.it.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.it.dao.*;
import com.it.entity.*;
import com.it.util.Info;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class JobController extends BaseController {

	@Resource
    JobDAO jobDAO;
    @Resource
    CategoryDAO categoryDAO;
    @Resource
    AreaDAO areaDAO;
    @Resource
    MemberDAO memberDAO;

    @Resource
    FavDAO favDAO;


	//管理员招聘信息列表
	@ResponseBody
	@RequestMapping("admin/jobList")
	public HashMap<String,Object> jobList(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,@RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize,HttpServletRequest request){
		String key = request.getParameter("key");
        String key1 = request.getParameter("key1");
        String key2 = request.getParameter("key2");
        String key3 = request.getParameter("key3")==null?"":request.getParameter("key3");
        String status = request.getParameter("status");
        String orderby = request.getParameter("orderby");
        HashMap<String,Object> res = new HashMap<String,Object>();
		HashMap map = new HashMap();
		map.put("key", key);
        map.put("key1", key1);
        map.put("key2", key2);
        map.put("key3", key3);
        map.put("status", status);
        map.put("orderby", orderby);
        List<Job> joblist = jobDAO.selectAll(map);
        for(Job job:joblist){
            Area area = areaDAO.findById(job.getAreaid());
            job.setArea(area);
            Category category = categoryDAO.findById(job.getCategoryid());
            job.setCategory(category);
            Member company = memberDAO.findById(job.getCompanyid());
            job.setCompany(company);
        }
		PageHelper.startPage(pageNum, pageSize);
		List<Job> list = jobDAO.selectAll(map);
        for(Job job:list){
            Area area = areaDAO.findById(job.getAreaid());
            job.setArea(area);
            Category category = categoryDAO.findById(job.getCategoryid());
            job.setCategory(category);
            Member company = memberDAO.findById(job.getCompanyid());
            job.setCompany(company);
        }
		PageInfo<Job> pageInfo = new PageInfo<Job>(list);
		res.put("pageInfo", pageInfo);
		res.put("list", joblist);
		return res;
	}


    //企业招聘信息列表
    @ResponseBody
    @RequestMapping("jobLb")
    public HashMap<String,Object> jobLb(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,@RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize,HttpServletRequest request){
        Member sessionmember = (Member) request.getSession().getAttribute("sessionmember");
	    String key = request.getParameter("key");
        HashMap<String,Object> res = new HashMap<String,Object>();
        HashMap map = new HashMap();
        map.put("key", key);
        map.put("companyid", sessionmember.getId());
        List<Job> joblist = jobDAO.selectAll(map);
        PageHelper.startPage(pageNum, pageSize);
        List<Job> list = jobDAO.selectAll(map);
        for(Job job:list){
            Area area = areaDAO.findById(job.getAreaid());
            job.setArea(area);
            Category category = categoryDAO.findById(job.getCategoryid());
            job.setCategory(category);
            Member company = memberDAO.findById(job.getCompanyid());
            job.setCompany(company);
        }
        PageInfo<Job> pageInfo = new PageInfo<Job>(list);
        res.put("pageInfo", pageInfo);
        res.put("list", joblist);
        return res;
    }


    //招聘信息添加
    @ResponseBody
    @RequestMapping("jobAdd")
    public void jobAdd(Job job,HttpServletRequest request) {
        Member sessionmember = (Member) request.getSession().getAttribute("sessionmember");
        job.setCompanyid(sessionmember.getId());
        job.setSavetime(Info.getDateStr().substring(0, 10));
        job.setStatus("待审核");
        jobDAO.add(job);
    }


    //编辑页面
    @ResponseBody
    @RequestMapping("admin/jobShow")
    public HashMap<String,Object> jobShow(int id,HttpServletRequest request) {
        HashMap<String,Object> res = new HashMap<String,Object>();
        Job job = jobDAO.findById(id);
        res.put("job", job);
        return res;
    }


    //编辑页面
    @ResponseBody
    @RequestMapping("jobShow")
    public HashMap<String,Object> jobShow1(int id,HttpServletRequest request) {
        HashMap<String,Object> res = new HashMap<String,Object>();
        Job job = jobDAO.findById(id);
        job.setLookcs(job.getLookcs()+1);
        jobDAO.update(job);

        HashMap map = new HashMap();
        map.put("jobid",id);
        map.put("type", "2");
        List<Fav> dzlist = favDAO.selectAll(map);
        job.setZnum(dzlist.size());

        Member company = memberDAO.findById(job.getCompanyid());
        job.setCompany(company);
        Area area = areaDAO.findById(job.getAreaid());
        job.setArea(area);
        Category category = categoryDAO.findById(job.getCategoryid());
        job.setCategory(category);
        res.put("job", job);
        return res;
    }


    //招聘信息编辑
    @ResponseBody
    @RequestMapping("jobEdit")
    public void jobEdit(Job job,HttpServletRequest request) {
        jobDAO.update(job);
    }

    //招聘信息删除
    @ResponseBody
    @RequestMapping("admin/jobDel")
    public void jobDel(int id,HttpServletRequest request) {
	    jobDAO.delete(id);
    }

    /**
     * 招聘信息审核
     * @param id
     * @param status
     * @param request
     */
    @ResponseBody
    @RequestMapping("admin/editjobstatus")
    public void editjobstatus(int id,String status,HttpServletRequest request) {
        Job job = jobDAO.findById(id);
        job.setStatus(status);
        jobDAO.update(job);
    }




}
