package com.it.util;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class Info {
	
	//public static String popheight = "alliframe.height=document.body.clientHeight+";
	public static String popheight = "alliframe.style.height=document.body.scrollHeight+";
	
	// 获取用户信息的方法，根据request对象获取当前登录用户的信息，返回一个HashMap对象。
    public static HashMap getUser(HttpServletRequest request)
    {
    	HashMap map = (HashMap)(request.getSession().getAttribute("admin")==null?request.getSession().getAttribute("user"):request.getSession().getAttribute("admin"));
        return map;
    }
	
	//计算两个日期之间相差的天数的方法。参数为两个日期字符串，返回相差的天数。
	public static int getBetweenDayNumber(String dateA, String dateB) {
		long dayNumber = 0;
		//1小时=60分钟=3600秒=3600000
		long mins = 60L * 1000L;
		//long day= 24L * 60L * 60L * 1000L;计算天数之差
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
		   Date d1 = df.parse(dateA);
		   Date d2 = df.parse(dateB);
		   dayNumber = (d2.getTime() - d1.getTime()) / mins;
		} catch (Exception e) {
		   e.printStackTrace();
		}
		return (int) dayNumber;
		}


	//将数据写入Excel文件的方法。参数包括文件名、属性数组、数据列表、HttpServletRequest对象和HttpServletResponse对象。
    public static void writeExcel(String fileName,String[] pros,java.util.List<List> list,HttpServletRequest request, HttpServletResponse response){
        WritableWorkbook  wwb = null;
        try {
            //首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象
            wwb = Workbook.createWorkbook(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(wwb!=null){
            //创建一个可写入的工作表
            //Workbook的createSheet方法有两个参数，第一个是工作表的名称，第二个是工作表在工作薄中的位置
            WritableSheet ws = wwb.createSheet("sheet1", 0);
            ws.setColumnView(0,20);
            ws.setColumnView(1,20);
            ws.setColumnView(2,20);
            ws.setColumnView(3,20);
            ws.setColumnView(4,20);
            ws.setColumnView(5,20);

            try {


                for(int i=0;i<pros.length;i++)
                {
                    Label label1 = new Label(i, 0,"");

                    label1.setString(pros[i]);
                    ws.addCell(label1);
                }

            } catch (RowsExceededException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (WriteException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            //下面开始添加单元格
            int i=1;
            for(List t:list){
                try {

                    Iterator it = t.iterator();
                    int jj=0;
                    while(it.hasNext())
                    {
                        Label label1 = new Label(jj, i,"");

                        String a = it.next().toString();
                        label1.setString(a);
                        ws.addCell(label1);
                        jj++;
                    }

                    i++;
                } catch (RowsExceededException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (WriteException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            try {
                //从内存中写入文件中
                wwb.write();
                //关闭资源，释放内存
                wwb.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            try {
                request.getRequestDispatcher("upload?filename="+fileName.substring(fileName.lastIndexOf("/")+1)).forward(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



//生成文件上传的HTML代码的方法
	 public static String getFileUpInfo()
		{
			String jscode = "";
			jscode+="<script src=/vehiclemanagementsys/js/popup.js></script>";
			jscode+="<font onclick=\"uploaddoc()\" src=\"js/nopic.jpg\" style='cursor:hand' id=txt >点击此处上传</font>";
			jscode+="&nbsp;&nbsp;<input type=text readonly style='border:0px' size=30  name=\"docname\" id=\"docname\" value=\"\" />";
			return jscode;
		}
	//根据给定的HashMap对象生成对应的HTML表单代码。
	  public static String tform(HashMap map)
		{
	    	String jscode = "";
	    	try{
	    	jscode+="<script type=\"text/javascript\">\n";
	    	jscode+="function getPvalue()\n";
	    	jscode+="{\n";
	    	
	    	Set set = map.entrySet();
	    	 Iterator it = set.iterator();
	    	 while(it.hasNext())
	    	 {
	    	  String pm=((Object)it.next()).toString();
	    	  String str1 = "";
	    	  String str2 = "";
	    	  String[] strs = pm.split("=");
	    	  str1 = strs[0];
	    	  if(strs.length==1)str2="";
	    	  if(strs.length==2)str2=strs[1];
	    	  str2 = str2.replaceAll("\r\n", "-----");
	    	 
	    	  if(!str1.equals("content")){
	    	  
	    	  jscode+=" if(document.getElementsByName(\""+str1+"\").length>1)\n";
	    	  jscode+=" {\n";
	    	  jscode+=" var radios = document.getElementsByName(\""+str1+"\");\n";
	    	 
	    	  jscode+=" if(radios[0].type=='radio'){\n";
	    	  jscode+=" for(var i=0;i<radios.length;i++)\n";
	    	  jscode+=" {\n";
	    	  jscode+=" if(radios[i].value==\""+str2+"\")\n";
	    	  jscode+=" {\n";
	    	  jscode+=" radios[i].checked=\"checked\";\n";
	    	  jscode+=" }\n";
	    	  jscode+=" }\n";
	    	  jscode+=" }\n";
	    	   
	    	  
	    	  jscode+=" if(radios[0].type=='checkbox'){\n";
	    	  
	    	  
	    	  
	    	  jscode+=" for(var i=0;i<radios.length;i++)\n";
	    	  jscode+=" {\n";
	    	   
	    	  jscode+=" if(\""+str2+"\".indexOf(radios[i].value)>-1)\n";
	    	  jscode+=" {\n";
	    	  
	    	  jscode+=" radios[i].checked=\"checked\";\n";
	    	  
	    	 
	    	  if(str2.indexOf(" - ")>-1){
	    		for(String strch:str2.split(" ~ ")){  
		    	  
	    		  String strchname = strch.substring(0,strch.lastIndexOf(" - "));
	    		  jscode+=" if(document.getElementsByName('"+strchname+"').length>0)\n";
		    	  jscode+=" {\n";
		    	  jscode+=" document.getElementsByName('"+strchname+"')[0].value='"+strch.substring(strch.lastIndexOf(":")+1)+"';\n";
		    	  jscode+=" }\n";
		    	  
		    	  
	    		}
	    	 }
	    	  
	    	  
	    	  jscode+=" }\n";
	    	  jscode+=" }\n";
	    	  jscode+=" }\n";
	    	  
	    	  jscode+=" if(radios.type=='select'){\n";
	    	  jscode+=" f1."+str1+".value=\""+str2+"\";\n";
	    	  jscode+=" }\n";
	    	  
	    	  
	    	  jscode+=" }else{\n";
	    	 jscode+=" if(f1."+str1+")\n";
	    	  jscode+="{\n";
	    	  jscode+="f1."+str1+".value=\""+str2+"\";\n";
	    	  jscode+="}\n";
	    	  jscode+="}\n";
	    	  
	    	
	    	  jscode+="if(document.getElementById(\"txt\"))\n";
	    	  jscode+="{\n";
	    	  jscode+="document.getElementById(\"txt\").src=\"/vehiclemanagementsys/upfile/"+map.get("filename")+"\";\n";
	    	  jscode+="}\n";
	    	  
	    	  jscode+="if(document.getElementById(\"txt2\"))\n";
	    	  jscode+="{\n";
	    	  jscode+="document.getElementById(\"txt2\").src=\"/vehiclemanagementsys/upfile/"+map.get("filename2")+"\";\n";
	    	  jscode+="}\n";
	    	  
	    	  jscode+="if(document.getElementById(\"txt3\"))\n";
	    	  jscode+="{\n";
	    	  jscode+="document.getElementById(\"txt3\").src=\"/vehiclemanagementsys/upfile/"+map.get("filename3")+"\";\n";
	    	  jscode+="}\n";
	    	  
	    	  jscode+="if(document.getElementById(\"txt4\"))\n";
	    	  jscode+="{\n";
	    	  jscode+="document.getElementById(\"txt4\").src=\"/vehiclemanagementsys/upfile/"+map.get("filename4")+"\";\n";
	    	  jscode+="}\n";
	    	  
	    	  jscode+="if(document.getElementById(\"txt5\"))\n";
	    	  jscode+="{\n";
	    	  jscode+="document.getElementById(\"txt5\").src=\"/vehiclemanagementsys/upfile/"+map.get("filename5")+"\";\n";
	    	  jscode+="}\n";
	    	  
	    	  }
	    	 }
	    	  
	    	  jscode+="}\n";
	    	
	    	 
	    	 
	    jscode+=" getPvalue();\n";
	    jscode+="</script>\n";
	    	}catch (Exception e) {
				e.printStackTrace();
			}
	      return jscode;
		}


	//生成一个通用的文件名，用于文件上传时保存文件。
	public static String generalFileName(String srcFileName) {
		try{
		   int index=srcFileName.lastIndexOf(".");
		   return StrUtil.generalSrid()+srcFileName.substring(index).toLowerCase();
		}catch(Exception e){
			return StrUtil.generalSrid();
		}
	}
//
	public synchronized static String getID() {
		Random random = new Random();
		StringBuffer ret = new StringBuffer(20);
		String rand = String.valueOf(Math.abs(random.nextInt()));
		ret.append(getDateStr());
		ret.append(rand.substring(0,6));
		
		return ret.toString();
	} 
	
	


	
	public static String getImgUpInfo(int height)
	{
		String jscode = "";
		jscode+="<img style=\"cursor: hand\" onclick=\"uploadimg()\" src=\"/vehiclemanagementsys/js/nopic.jpg\" id=txt height=\""+height+"\"/>";
		jscode+="<input type=hidden name=\"filename\" id=\"filename\" value=\"\" />";
		jscode+="<script type=\"text/javascript\"  src=\"/vehiclemanagementsys/js/popups.js\"></script>";
		return jscode;
	}
	
	
	
	public static String getImgUpInfo2(int height)
	{
		String jscode = "";
		jscode+="<img style=\"cursor: hand\" onclick=\"uploadimg2()\" src=\"/vehiclemanagementsys/js/nopic.jpg\" id=txt2 height=\""+height+"\"/>";
		jscode+="<input type=hidden name=\"filename2\" id=\"filename2\" value=\"\" />";
		jscode+="<script type=\"text/javascript\"  src=\"/vehiclemanagementsys/js/popup.js\"></script>";
		return jscode;
	}
	
	public static String getImgUpInfo3(int height)
	{
		String jscode = "";
		jscode+="<img style=\"cursor: hand\" onclick=\"uploadimg3()\" src=\"/vehiclemanagementsys/js/nopic.jpg\" id=txt3 height=\""+height+"\"/>";
		jscode+="<input type=hidden name=\"filename3\" id=\"filename3\" value=\"\" />";
		jscode+="<script type=\"text/javascript\"  src=\"/vehiclemanagementsys/js/popup.js\"></script>";
		return jscode;
	}
	
	public static String getImgUpInfo4(int height)
	{
		String jscode = "";
		jscode+="<img style=\"cursor: hand\" onclick=\"uploadimg4()\" src=\"/vehiclemanagementsys/js/nopic.jpg\" id=txt4 height=\""+height+"\"/>";
		jscode+="<input type=hidden name=\"filename4\" id=\"filename4\" value=\"\" />";
		jscode+="<script type=\"text/javascript\"  src=\"/vehiclemanagementsys/js/popup.js\"></script>";
		return jscode;
	}
	
	public static String getImgUpInfo5(int height)
	{
		String jscode = "";
		jscode+="<img style=\"cursor: hand\" onclick=\"uploadimg5()\" src=\"/vehiclemanagementsys/js/nopic.jpg\" id=txt5 height=\""+height+"\"/>";
		jscode+="<input type=hidden name=\"filename5\" id=\"filename5\" value=\"\" />";
		jscode+="<script type=\"text/javascript\"  src=\"/vehiclemanagementsys/js/popup.js\"></script>";
		return jscode;
	}
	
	public static String fck(int height,String content)
	{
		String jscode = "<TEXTAREA   name=\"content\" id=\"content\">"+content+"</TEXTAREA>";
		jscode+="<script type=\"text/javascript\"  src=\"/vehiclemanagementsys/fckeditor/fckeditor.js\"></script>";
		jscode+="<script language=\"javascript\">";
		jscode+="function fckinit()";
		jscode+="{";
		jscode+=" var of = new FCKeditor(\"content\");";
		jscode+="of.BasePath=\"/vehiclemanagementsys/fckeditor/\";";
		jscode+="of.Height = \""+height+"\";";
		jscode+="of.ToolbarSet=\"Default\";";
		jscode+="of.ReplaceTextarea();";
		jscode+="}";
		jscode+="fckinit();";
		jscode+="</script>";
		
		return jscode;
	}

	
	public synchronized static String subStr(String source,int length) {
		if(source.length()>length)
		{
			source=source.substring(0,length)+"...";
		}
		
		return source;
	} 
	
	

	public static String getDateStr(){
		String dateString="";
		try{//yyyyMMddHHmmss
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentTime_1=new Date();
		dateString=formatter.format(currentTime_1);
		}catch(Exception e){
		     }
		 return dateString;
		 }
	
	

	
	public static String getOrderNo(){
		String dateString="";
		try{//yyyyMMddHHmmss
		SimpleDateFormat formatter=new SimpleDateFormat("MMddHHmmss");
		Date currentTime_1=new Date();
		dateString=formatter.format(currentTime_1);
		}catch(Exception e){
		     }
		 return "DD"+dateString;
		 } 
	
	
	


	
	 public static String getUTFStr(String str) {
		 if(str==null){
			 return "";
		 }
		 
		 try {
			str =  new String(str.getBytes("ISO-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	 }
	 
	 public static String getGBKStr(String str) throws UnsupportedEncodingException{
		 if(str==null){
			 return "";
		 }
		 return  new String(str.getBytes("ISO-8859-1"),"GBK");
	 }
	 
	 public static String getGB2312Str(String str) throws UnsupportedEncodingException{
		 if(str==null){
			 return "";
		 }
		 return  new String(str.getBytes("ISO-8859-1"),"gb2312");
	 }

	 
	 /**
		 *得到多少天之后之前的日期��

		 */
		public static String getDay(String date,int day) {
			String b = date.substring(0,10);
			String c = b.substring(0,4);
			String d = b.substring(5,7);
			String f = b.substring(8,10);
			String aa = c+"/"+d+"/"+f;
			String a = "";
			DateFormat dateFormat =  DateFormat.getDateInstance(DateFormat.MEDIUM);
				GregorianCalendar grc=new GregorianCalendar();
				grc.setTime(new Date(aa));
	            grc.add(GregorianCalendar.DAY_OF_MONTH,day);
	            String resu = dateFormat.format(grc.getTime());
	        String t[]= resu.split("-");
	        String sesuu = "";
	        for(int i=0;i<t.length;i++)
	        {
	        	if(t[i].length()==1)
	        	{
	        		t[i]="0"+t[i];
	        	}
	        	sesuu += t[i]+"-";
	        }
	        
			return sesuu.substring(0,10);
		}
		

	    
	    
	    /**
	 	 * 计算两个时期之间的天数
	 	 * 
	 	 */
	    public static int dayToday(String DATE1, String DATE2) {
	       int i = 0;
	       if(DATE1.indexOf(" ")>-1)
	       DATE1 = DATE1.substring(0,DATE1.indexOf(" "));
	       if(DATE2.indexOf(" ")>-1)
	       DATE2 = DATE2.substring(0,DATE2.indexOf(" "));
	       
	       String[] d1 = DATE1.split("-");
	       if(d1[1].length()==1)
	       {
	    	   DATE1 = d1[0]+"-0"+d1[1];
	       }else{
	    	   DATE1 = d1[0]+"-"+d1[1];
	       }
	       
	       if(d1[2].length()==1)
	       {
	    	   DATE1 = DATE1+"-0"+d1[2];
	       }else{
	    	   DATE1 = DATE1+"-"+d1[2];
	       }
	       
	       String[] d2 = DATE2.split("-");
	       if(d2[1].length()==1)
	       {
	    	   DATE2 = d2[0]+"-0"+d2[1];
	       }else{
	    	   DATE2 = d2[0]+"-"+d2[1];
	       }
	       
	       if(d2[2].length()==1)
	       {
	    	   DATE2 = DATE2+"-0"+d2[2];
	       }else{
	    	   DATE2 = DATE2+"-"+d2[2];
	       }
	       
	       
	       for(int j=0;j<10000;j++)
	       {
	    	i=j;
	    	String gday = Info.getDay(DATE1, j);
	    	if(gday.equals(DATE2))
	    	{
	    		break;
	    	}
	       }
	        return i;
	    }
	    

	    


	    /**
	 	 * 比较时间大小
	 	 * 
	 	 */
	    public static String compare_datezq(String DATE1, String DATE2) {
	       DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        try {
	            Date dt1 = df.parse(DATE1);
	            Date dt2 = df.parse(DATE2);
	            if (dt1.getTime() > dt2.getTime()) {
	               
	                return "big";
	            } else if (dt1.getTime() < dt2.getTime()) {
	              
	                return "small";
	            } else {
	                return "den";
	            }
	        } catch (Exception exception) {
	            exception.printStackTrace();
	        }
	        return "den";
	    }
	    
	    /**
	 	 * 过滤html代码
	 	 * 
	 	 */
	    public static String filterStrIgnoreCase(String source, String from, String to){
	        String sourceLowcase=source.toLowerCase();
	        String sub1,sub2,subLowcase1,subLowcase2;
	        sub1=sub2=subLowcase1=subLowcase2="";
	        int start=0,end;
	        boolean done=true;
	        if(source==null) return null;
	        if(from==null||from.equals("")||to==null||to.equals("")) 
	         return source;
	        while(done){
	         start=sourceLowcase.indexOf(from,start);
	         if(start==-1) {
	          break;
	         }
	         subLowcase1=sourceLowcase.substring(0,start);
	         sub1=source.substring(0,start);
	         end=sourceLowcase.indexOf(to,start);
	         if(end==-1){
	          end=sourceLowcase.indexOf("/>",start); 
	          if(end==-1) {
	           done=false;
	          }
	         }
	         else{
	          end=end+to.length();
	          subLowcase2=sourceLowcase.substring(end,source.length());
	          sub2=source.substring(end,source.length());
	          sourceLowcase=subLowcase1+subLowcase2;
	          source=sub1+sub2;
	         }
	         //System.out.println(start+" "+end);
	        }
	        return source;
	   }
	    
	   
	    public static void delPic(String path,String img)
		{
			 if(img!=null)
			 {
				 if(!img.equals(""))
				 {
			  File file1=new File(path + "/" + img); 
		       if(file1.exists() ) {
		    	file1.deleteOnExit();
		         file1.delete(); 
		       }}}
		}
	    
	    
	    //计算两个时间间隔多少小时
	    public static Long getDatePoor(String stime, String etime) {
	    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	long hournum = 0;
			try {
				Date startDate = df.parse(stime);
				Date endDate = df.parse(etime);
				long nd = 1000 * 24 * 60 * 60;
		    	long nh = 1000 * 60 * 60;
		    	long nm = 1000 * 60;
		    	// long ns = 1000;
		    	// 获得两个时间的毫秒时间差异
		    	long diff = endDate.getTime() - startDate.getTime();
		    	// 计算差多少天
		    	long day = diff / nd;
		    	// 计算差多少小时
		    	long hour = diff / nh;
		    	long y = diff % nh;
		    	//计算余分钟
		    	long b = y/(1000*60);
		    	// 计算差多少分钟
		    	long min = diff % nd % nh / nm;
		    	// 计算差多少秒//输出结果
		    	// long sec = diff % nd % nh % nm / ns;
		    	if(b>30){hournum = hour+1;}else{
		    		hournum = hour;
		    	}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return hournum;
	    	}

    public static String getAutoId(){
        String dateString="";
        try{//yyyyMMddHHmmss
            java.text.SimpleDateFormat formatter=new java.text.SimpleDateFormat("MMddhhmmss");
            java.util.Date currentTime_1=new java.util.Date();
            dateString=formatter.format(currentTime_1);
        }catch(Exception e){
        }
        return dateString;
    }


    //过滤html标签
	public static String delHTMLTag(String htmlStr){
		String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
		String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
		String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式

		Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
		Matcher m_script=p_script.matcher(htmlStr);
		htmlStr=m_script.replaceAll(""); //过滤

		Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
		Matcher m_style=p_style.matcher(htmlStr);
		htmlStr=m_style.replaceAll(""); //过滤style标签

		Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
		Matcher m_html=p_html.matcher(htmlStr);
		htmlStr=m_html.replaceAll(""); //过滤html标签

		return htmlStr.trim(); //返回文本字符串
	}


//	    public static void main(String[] args) {
//	    	long a = getDatePoor("2018-12-10 10:18:41","2018-12-10 12:38:41");
//			System.out.println(a);
//		}
		
}

		

