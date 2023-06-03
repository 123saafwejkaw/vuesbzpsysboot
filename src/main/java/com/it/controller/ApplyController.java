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
import java.util.HashMap;
import java.util.List;

@Controller
public class ApplyController extends BaseController {

	@Resource
    ApplyDAO applyDAO;
    @Resource
    JobDAO jobDAO;
    @Resource
    VitaeDAO vitaeDAO;
    @Resource
    MemberDAO memberDAO;
    @Resource
    AreaDAO areaDAO;
    @Resource
    CategoryDAO categoryDAO;


	//申请列表
    @ResponseBody
    @RequestMapping("admin/applyList")
    public HashMap<String,Object> applyList(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,@RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize,HttpServletRequest request){
        String key = request.getParameter("key");
        HashMap<String,Object> res = new HashMap<String,Object>();
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("key", key);
        List<Apply> applylist = applyDAO.selectAll(map);
        PageHelper.startPage(pageNum, pageSize);
        List<Apply> list = applyDAO.selectAll(map);
        PageInfo<Apply> pageInfo = new PageInfo<Apply>(list);
        res.put("pageInfo", pageInfo);
        res.put("list", applylist);
        return res;
    }



    //企业申请列表
    @ResponseBody
    @RequestMapping("applyList")
    public HashMap<String,Object> applyList1(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,@RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize,HttpServletRequest request){
        Member sessionmember = (Member) request.getSession().getAttribute("sessionmember");
        String key = request.getParameter("key");
        HashMap<String,Object> res = new HashMap<String,Object>();
        HashMap map = new HashMap();
        map.put("key", key);
        map.put("companyid", sessionmember.getId());
        List<Apply> applylist = applyDAO.selectAll(map);
        PageHelper.startPage(pageNum, pageSize);
        List<Apply> list = applyDAO.selectAll(map);
        for(Apply apply:list){
            Member company = memberDAO.findById(apply.getCompanyid());
            apply.setCompany(company);

            Member member = memberDAO.findById(apply.getMemberid());
            apply.setMember(member);

            Job job = jobDAO.findById(apply.getJobid());
            Area area = areaDAO.findById(job.getAreaid());
            Category category = categoryDAO.findById(job.getCategoryid());
            job.setArea(area);
            job.setCategory(category);
            apply.setJob(job);

        }
        PageInfo<Apply> pageInfo = new PageInfo<Apply>(list);
        res.put("pageInfo", pageInfo);
        res.put("list", applylist);
        return res;
    }

    //求职者申请列表
    @ResponseBody
    @RequestMapping("applyLb")
    public HashMap<String,Object> applyLb(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,@RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize,HttpServletRequest request){
        Member sessionmember = (Member) request.getSession().getAttribute("sessionmember");
        String key = request.getParameter("key");
        HashMap<String,Object> res = new HashMap<String,Object>();
        HashMap map = new HashMap();
        map.put("key", key);
        map.put("memberid", sessionmember.getId());
        List<Apply> applylist = applyDAO.selectAll(map);
        PageHelper.startPage(pageNum, pageSize);
        List<Apply> list = applyDAO.selectAll(map);
        for(Apply apply:list){
            Member company = memberDAO.findById(apply.getCompanyid());
            apply.setCompany(company);

            Job job = jobDAO.findById(apply.getJobid());
            Area area = areaDAO.findById(job.getAreaid());
            Category category = categoryDAO.findById(job.getCategoryid());
            job.setArea(area);
            job.setCategory(category);
            apply.setJob(job);

        }
        PageInfo<Apply> pageInfo = new PageInfo<Apply>(list);
        res.put("pageInfo", pageInfo);
        res.put("list", applylist);
        return res;
    }

    //申请添加
    @ResponseBody
    @RequestMapping("applyAdd")
    public HashMap<String,Object> applyAdd(Apply apply,HttpServletRequest request) {
        HashMap<String,Object> res = new HashMap<String,Object>();
        Member sessionmember = (Member) request.getSession().getAttribute("sessionmember");
        if(sessionmember!=null){
            int message = 0;
            try {
                Vitae vitae = vitaeDAO.findVitae(sessionmember.getId());
                Job job = jobDAO.findById(apply.getJobid());
                apply.setCompanyid(job.getCompanyid());
                apply.setVitaeid(vitae.getId());
                apply.setMemberid(sessionmember.getId());
                apply.setSavetime(Info.getDateStr());
                apply.setStatus("待查阅");
                applyDAO.add(apply);
            }catch (Exception e){
                message = 1;
            }
            if(message == 1){
                res.put("data", 300);
            }else{
                res.put("data", 200);

            }
        }else{
            res.put("data", 400);
        }
        return res;
    }


    //编辑页面
    @ResponseBody
    @RequestMapping("admin/applyShow")
    public HashMap<String,Object> applyShow(int id,HttpServletRequest request) {
        HashMap<String,Object> res = new HashMap<String,Object>();
        Apply apply = applyDAO.findById(id);
        res.put("apply", apply);
        return res;
    }


    //编辑页面
    @ResponseBody
    @RequestMapping("applyShow")
    public HashMap<String,Object> applyShow1(int id,HttpServletRequest request) {
        HashMap<String,Object> res = new HashMap<String,Object>();
        Apply apply = applyDAO.findById(id);
        res.put("apply", apply);
        return res;
    }


    //申请编辑
    @ResponseBody
    @RequestMapping("admin/applyEdit")
    public void applyEdit(Apply apply,HttpServletRequest request) {
        applyDAO.update(apply);
    }

    //申请删除
    @ResponseBody
    @RequestMapping("admin/applyDel")
    public void applyDel(int id,HttpServletRequest request) {
	    applyDAO.delete(id);
    }


    /**
     * 简历审核
     * @param id
     * @param status
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("editapplystatus")
    public HashMap<String,Object> editapplystatus(int id,String status,HttpServletRequest request) {
        HashMap<String,Object> res = new HashMap<String,Object>();
        Apply apply = applyDAO.findById(id);
        apply.setStatus(status);
        applyDAO.update(apply);
        return res;
    }
}
