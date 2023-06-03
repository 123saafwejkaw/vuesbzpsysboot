package com.it.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.it.dao.WelfareDAO;
import com.it.entity.Welfare;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
public class WelfareController {
	@Resource
    WelfareDAO welfareDAO;
	
	
	//后台福利列表
    @ResponseBody
	@RequestMapping("admin/welfareList")
	public HashMap<String,Object> welfareList(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,@RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize,HttpServletRequest request) {
        String key = request.getParameter("key");
        HashMap<String,Object> res = new HashMap<String,Object>();
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("key", key);
        List<Welfare> welfarelist = welfareDAO.selectAll(map);
        PageHelper.startPage(pageNum, pageSize);
        List<Welfare> list = welfareDAO.selectAll(map);
        PageInfo<Welfare> pageInfo = new PageInfo<Welfare>(list);
        res.put("pageInfo", pageInfo);
        res.put("list", welfarelist);
        return res;
	}
	
	
	
	//福利添加
    @ResponseBody
	@RequestMapping("admin/welfareAdd")
	public void welfareAdd(Welfare welfare,HttpServletRequest request) {
		welfareDAO.add(welfare);
	}
	
	
	//编辑页面
    @ResponseBody
	@RequestMapping("admin/welfareShow")
	public HashMap<String,Object> welfareShow(int id,HttpServletRequest request) {
        HashMap<String,Object> res = new HashMap<String,Object>();
        Welfare welfare = welfareDAO.findById(id);
        res.put("welfare", welfare);
        return res;
	}
	
	
	//福利编辑
    @ResponseBody
	@RequestMapping("admin/welfareEdit")
	public void welfareEdit(Welfare welfare,HttpServletRequest request) {
		welfareDAO.update(welfare);
	}
	
	//福利删除
    @ResponseBody
	@RequestMapping("admin/welfareDel")
	public void welfareDel(int id,HttpServletRequest request) {
		welfareDAO.delete(id);
	}
	
	

}
