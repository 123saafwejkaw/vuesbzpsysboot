package com.it.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.it.dao.SysuserDAO;
import com.it.entity.Sysuser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SysuserController {
	@Resource
    SysuserDAO sysuserDAO;
	//后台用户列表
	@ResponseBody
	@RequestMapping("admin/sysuserList")
	public HashMap<String, Object> sysuserList(@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, @RequestParam(defaultValue = "1", value = "pageSize") Integer pageSize, HttpServletRequest request) {
		String key = request.getParameter("key");
		String utype = request.getParameter("utype");
		HashMap<String, Object> res = new HashMap<String, Object>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("key", key);
		map.put("utype", utype);
		List<Sysuser> memberlist = sysuserDAO.selectAll(map);
		PageHelper.startPage(pageNum, pageSize);
		List<Sysuser> list = sysuserDAO.selectAll(map);
		PageInfo<Sysuser> pageInfo = new PageInfo<Sysuser>(list);
		res.put("pageInfo", pageInfo);
		res.put("list", memberlist);
		return res;
	}

	//判断用户是否登录
    @ResponseBody
    @RequestMapping("admin/checkadmin")
    public HashMap<String,Object> checkadmin(HttpServletRequest request) {
        HashMap<String,Object> res = new HashMap<String,Object>();
        Sysuser admin = (Sysuser)request.getSession().getAttribute("admin");
        if (admin !=null) {
            Sysuser user = sysuserDAO.findById(admin.getId());
            res.put("admin", user);
            res.put("data", 200);
        } else {
            res.put("data", 400);
        }
        return res;
    }
	@ResponseBody
	@RequestMapping("admin/checkSysUname")
	public HashMap<String,Object> checkSysUname(String uname,HttpServletRequest request) {
		HashMap<String,Object> res = new HashMap<String,Object>();
		HashMap map = new HashMap();
		map.put("uname",uname);
		List<Sysuser> userlist = sysuserDAO.selectAll(map);
		if (userlist.size() == 0) {
			res.put("data", 0);
		} else {
			res.put("data", -1);
		}
		return res;
	}


	// 后台登录
    @ResponseBody
	@RequestMapping("admin/login")
	public HashMap<String,Object> login(Sysuser sysuser, HttpServletRequest request) {
        HashMap<String,Object> res = new HashMap<String,Object>();
		HashMap map = new HashMap();
		map.put("uname", sysuser.getUname());
		map.put("upass", sysuser.getUpass());
		List<Sysuser> list = sysuserDAO.selectAll(map);
		if (list.size() == 0) {
		    res.put("data", 400);
		} else {
            res.put("data", 200);
			request.getSession().setAttribute("admin", list.get(0));
		}
        return res;
	}

	@ResponseBody
	@RequestMapping("admin/adminAdd")
	public HashMap<String, Object> adminAdd(Sysuser sysuser, HttpServletRequest request) {
		HashMap<String, Object> res = new HashMap<String, Object>();
		sysuser.setUtype("学校");
		sysuser.setDelstatus("0");
		sysuserDAO.add(sysuser);
		res.put("data", 200);
		return res;
	}


	// 后台退出
    @ResponseBody
	@RequestMapping("admin/adminExit")
	public void adminExit(HttpServletRequest request) {
		request.getSession().removeAttribute("admin");
	}




	// 编辑个人信息
    @ResponseBody
	@RequestMapping("admin/adminEdit")
	public HashMap<String,Object> adminEdit(Sysuser sysuser, HttpServletRequest request) {
        sysuserDAO.update(sysuser);
		return null;
	}

	// 修改密码
    @ResponseBody
	@RequestMapping("admin/upassedit")
	public HashMap<String,Object> userpasswordEdit(HttpServletRequest request) {
        HashMap<String,Object> res = new HashMap<String,Object>();
		Sysuser admin = (Sysuser) request.getSession().getAttribute("admin");
		Sysuser sysuser = (Sysuser) sysuserDAO.findById(admin.getId());
		String upass = request.getParameter("upass");
		String newupass = request.getParameter("newupass");
		if (upass.equals(sysuser.getUpass())) {
			sysuser.setUpass(newupass);
			sysuserDAO.update(sysuser);
			res.put("data", 200);
		} else {
            res.put("data", 400);
		}
        return res;

	}



	// 核对用户名的唯一性
	@RequestMapping("admin/checkUser")
	public void checkUname(String uname, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			HashMap map = new HashMap();
			map.put("uname", uname);
			List<Sysuser> list = sysuserDAO.selectAll(map);
			if (list.size() == 0) {
				out.print(0);
			} else {
				out.print(1);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//编辑页面
	@ResponseBody
	@RequestMapping("admin/sysuserShow")
	public HashMap<String,Object> sysuserShow(int id,HttpServletRequest request) {
		HashMap<String,Object> res = new HashMap<String,Object>();
		Sysuser sysuser = sysuserDAO.findById(id);
		res.put("sysuser", sysuser);
		return res;
	}
}
