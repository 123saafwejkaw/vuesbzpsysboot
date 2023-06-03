package com.it.controller;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.it.dao.*;
import com.it.entity.*;
import com.it.util.FileUploadUtil;
import com.it.util.FtpUtil;
import com.it.util.UserCFDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


/**
 * 上传图片公共接口等工具类
 * @author Administrator
 *
 */
@Controller  
public class UtilController  extends BaseController {
	//数据库切换
	//DataSourceContextHolder.setCustomerType("dataSourceThree");
	//切换后关闭
	//DataSourceContextHolder.clearCustomerType();
    @Resource
    JobDAO jobDAO;
    @Resource
    MemberDAO memberDAO;
    @Resource
    AreaDAO areaDAO;
    @Resource
    CategoryDAO categoryDAO;
    @Resource
    ApplyDAO applyDAO;

    @Resource
    FavDAO favDAO;


	//上传图片
	@ResponseBody
	@RequestMapping("admin/uploadImg")
	public Map uploadImg(MultipartFile file,HttpServletRequest request) {
		String prefix="";
        String dateStr="";
        //保存上传
        OutputStream out = null;
        InputStream fileInput=null;
        try{
            if(file!=null){
                //原始文件名
                String originalName = file.getOriginalFilename();
                //提取后缀
                prefix=originalName.substring(originalName.lastIndexOf(".")+1);
                Date date = new Date();
                //唯一标识UUID字符串
                String uuid = UUID.randomUUID()+"";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateStr = simpleDateFormat.format(date);
                //String filepath = "D:\\ebooksysimages\\" + dateStr+"\\"+uuid+"." + prefix;
                String filepath = request.getRealPath("/upload/")+"/"+uuid+"." + prefix;
//                System.out.println("filepath==="+filepath);

                File files=new File(filepath);
                //打印查看上传路径
//                System.out.println(filepath);
                if(!files.getParentFile().exists()){
                    files.getParentFile().mkdirs();
                }
//                将MultipartFile对象中的文件内容传输到目标文件中
                file.transferTo(files);
                Map<String,Object> map2=new HashMap<>();
                Map<String,Object> map=new HashMap<>();
                //成功上传时，返回的 Map对象包含代码为 0、消息为空、数据部分包含上传图片的路径。
                map.put("code",0);
                map.put("msg","");
                map.put("data",map2);
                //map2.put("src","/images/"+ dateStr+"/"+uuid+"." + prefix);
                map2.put("src",uuid+"." + prefix);
                return map;
            }

        }catch (Exception e){
        }finally{
            try {
                if(out!=null){
                    out.close();
                }
                if(fileInput!=null){
                    fileInput.close();
                }
            } catch (IOException e) {
            }
        }

        // 上传失败时，返回的 Map 对象包含代码为 1、消息为空。
        Map<String,Object> map=new HashMap<>();
        map.put("code",1);
        map.put("msg","");
        return map;
	}


	//热门岗位
    @ResponseBody
    @RequestMapping("hotJob")
    public HashMap<String,Object> hotJob(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,@RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize,HttpServletRequest request){
        HashMap<String,Object> res = new HashMap<String,Object>();
        HashMap map = new HashMap();
        List<Apply> joblist = applyDAO.selectHot(map);
        for(Apply apply:joblist){
            Job job = jobDAO.findById(apply.getJobid());
            Area area = areaDAO.findById(job.getAreaid());
            job.setArea(area);
            Category category = categoryDAO.findById(job.getCategoryid());
            job.setCategory(category);
            Member company = memberDAO.findById(job.getCompanyid());
            job.setCompany(company);
            apply.setJob(job);
        }
        res.put("list", joblist);
        return res;
    }



    /**
     * 下载文件
     * @param
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("downloadFile")
    public HashMap<String, Object> downloadFile(String filename, HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> res = new HashMap<String, Object>();
//        if (smember != null) {

        String fileName = filename;
        if (fileName != null) {
            //设置文件路径
            String realPath = request.getRealPath("/upload/") + "/";
            File file = new File(realPath, fileName);
            if (file.exists()) {
                response.setContentType("application/octet-stream");//
                response.setHeader("content-type", "application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
//        }else{
//            res.put("code","-1");
//        }
        return res;
    }





    //猜你喜欢
    @ResponseBody
    @RequestMapping("recommend")
    public HashMap<String,Object> recommend(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,@RequestParam(defaultValue = "1",value = "pageSize") Integer pageSize,HttpServletRequest request){
        String key = request.getParameter("key");
        HashMap<String,Object> res = new HashMap<String,Object>();

//        List<Foods> sceniclist = foodsDAO.selectAll(map);
        Member member = (Member)request.getSession().getAttribute("sessionmember");
        //会员集合
        List<Member> ulist = memberDAO.selectAll(null);
        String[] uarray=new String[ulist.size()];
        for(int i=0;i<ulist.size();i++){
            uarray[i]=String.valueOf(ulist.get(i).getId());
        }


        //对象集合
        HashMap map = new HashMap();
        map.put("status","已通过");
        List<Job> joblist = jobDAO.selectAll(map);
        String[] dyarray=new String[joblist.size()];
        for(int i=0;i<joblist.size();i++){
            dyarray[i]=String.valueOf(joblist.get(i).getId());
        }


        //评分
        int [][] arr2 = new int[ulist.size()][];
        int count = 0;
        //System.out.println("arr2.length=="+arr2.length);
        for(int i=0;i<arr2.length;i++){
            Member mb = ulist.get(i);
            //System.out.println("userobj"+i+"    "+userobj);
            //创建一维数组
            int[] tmpArr = new int[joblist.size()];
            //创建二维数组
            for(int j=0;j < tmpArr.length;j++){
                int zs = 0;
                Job job = joblist.get(j);
                HashMap m1 = new HashMap();
                m1.put("jobid", job.getId());
                m1.put("type", "1");
                m1.put("memberid", mb.getId());
                //收藏 权重 +2
                List<Fav> sclist = favDAO.selectAll(m1);
                if(sclist.size() > 0){
                    zs += 2;
                }

                //点赞权重 +1
                m1.put("type", "2");
                List<Fav> dzlist = favDAO.selectAll(m1);
                if(dzlist.size() > 0){
                    zs += 1;
                }

                //浏览权重 +1
                m1.put("type", "3");
                List<Fav> lllist = favDAO.selectAll(m1);
                    zs += lllist.size();

                //投递权重 +5
                m1.put("status", "应聘成功");
                System.out.println("aaa="+m1);
                List<Apply> applyList = applyDAO.selectAll(m1);
                if(applyList.size() > 0){
                    zs += 5;
                }


                tmpArr[j] = zs;
//                System.out.println("dyobj"+j+"    "+dyobj);
                //tmpArr[j] = (++count);
            }
            arr2[i] = tmpArr;
        }
        for(int m=0;m<arr2.length;m++){
            for(int n=0;n<arr2[m].length;n++){
                System.out.print(arr2[m][n]+"  ");
            }
            System.out.println();
        }

        UserCFDemo u = new UserCFDemo();
        u.users = uarray;
        u.movies = dyarray;
        u.allUserMovieStarList = arr2;
        u.membernum = ulist.size();
        u.mvnum = joblist.size();
        List<String> rtnlist = u.mvlist(String.valueOf(member.getId()));
        String aa = "";
        List<Job> tjproductlist = new ArrayList<Job>();
        for(int m = 0;m< rtnlist.size();m++){
            Job job = jobDAO.findById(Integer.valueOf(rtnlist.get(m)));
            Area area = areaDAO.findById(job.getAreaid());
            job.setArea(area);
            Category category = categoryDAO.findById(job.getCategoryid());
            job.setCategory(category);
            Member company = memberDAO.findById(job.getCompanyid());
            job.setCompany(company);
            tjproductlist.add(job);
            System.out.println("推荐的对象==="+job.getTitle());
        }
        res.put("list", tjproductlist);
        return res;
    }
}
