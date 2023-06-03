package com.it.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.it.dao.NoticeDAO;
import com.it.entity.Notice;
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
public class NoticeController extends BaseController {

	@Resource
    NoticeDAO noticeDAO;



	//公告列表
	@ResponseBody
	@RequestMapping("admin/noticeList")
	public HashMap<String,Object> noticeList(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,@RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize,HttpServletRequest request){
		String key = request.getParameter("key");
        HashMap<String,Object> res = new HashMap<String,Object>();
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("key", key);
        List<Notice> noticelist = noticeDAO.selectAll(map);
		PageHelper.startPage(pageNum, pageSize);
		List<Notice> list = noticeDAO.selectAll(map);
		PageInfo<Notice> pageInfo = new PageInfo<Notice>(list);
		res.put("pageInfo", pageInfo);
		res.put("list", noticelist);
		return res;
	}


    //公告添加
    @ResponseBody
    @RequestMapping("admin/noticeAdd")
    public void noticeAdd(Notice notice,HttpServletRequest request) {
        notice.setSavetime(Info.getDateStr());
        noticeDAO.add(notice);
    }


    //编辑页面
    @ResponseBody
    @RequestMapping("admin/noticeShow")
    public HashMap<String,Object> noticeShow(int id,HttpServletRequest request) {
        HashMap<String,Object> res = new HashMap<String,Object>();
        Notice notice = noticeDAO.findById(id);
        res.put("notice", notice);
        return res;
    }


    //编辑页面
    @ResponseBody
    @RequestMapping("noticeShow")
    public HashMap<String,Object> noticeShow1(int id,HttpServletRequest request) {
        HashMap<String,Object> res = new HashMap<String,Object>();
        Notice notice = noticeDAO.findById(id);
        res.put("notice", notice);
        return res;
    }


    //公告编辑
    @ResponseBody
    @RequestMapping("admin/noticeEdit")
    public void noticeEdit(Notice notice,HttpServletRequest request) {
        noticeDAO.update(notice);
    }

    //公告删除
    @ResponseBody
    @RequestMapping("admin/noticeDel")
    public void noticeDel(int id,HttpServletRequest request) {
	    noticeDAO.delete(id);
    }


}
