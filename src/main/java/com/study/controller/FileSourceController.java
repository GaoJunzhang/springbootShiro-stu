package com.study.controller;

import com.study.config.OSSConfigure;
import com.study.model.FileInfo;
import com.study.model.User;
import com.study.service.FileInfoService;
import com.study.service.UserService;
import com.study.util.ExcelImportUtils;
import com.study.util.ExcelUtil;
import com.study.util.OSSManageUtil;
import com.study.util.VTools;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by 张高俊 on 2017/10/15.
 */
@Controller
@RequestMapping("/filesource")
public class FileSourceController {
    @Resource
    private FileInfoService fileInfoService;

    @Resource
    private UserService userService;

    @Value("${sys.file.savepath}")
    private String path;

    //上传到阿里云oss
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String uploadApp(@RequestParam("lefile") MultipartFile appFile, @RequestParam("imgfile") MultipartFile imgfile,
                            @RequestParam("appVideoFile") MultipartFile appVideoFile,
                            @RequestParam("pdffile") MultipartFile pdffile, FileInfo fileInfo) {
        if (appFile.isEmpty()) {
            return "redirect:/addfileView?q=fi is empty";

        }
        if (fileInfo == null) {
            return "redirect:/addfileView?q=file is empty";
        }
        try {
            String apkUrl = OSSManageUtil.uploadFile(appFile,appFile.getName());
            String imgUrl = "";
            if(!imgfile.isEmpty()){

                imgUrl = OSSManageUtil.uploadFile(imgfile,imgfile.getName());
            }
            String appVideoUrl = "";
            if(!appVideoFile.isEmpty()){

                appVideoUrl = OSSManageUtil.uploadFile(appVideoFile,appVideoFile.getName());
            }
            String pdfUrl = "";
            if(!pdffile.isEmpty()){

                pdfUrl = OSSManageUtil.uploadFile(pdffile,pdffile.getName());
            }
            System.out.println("应用地址："+apkUrl);
            System.out.println("图片地址："+imgUrl);
            fileInfo.setFileaddress(apkUrl);
            fileInfo.setFileimgsrc(imgUrl);
            fileInfo.setAppvideourl(appVideoUrl);
            fileInfo.setPdfurl(pdfUrl);
            fileInfo.setCreatetime(VTools.getCurrentDate());
            fileInfoService.save(fileInfo);
        } catch (Exception e) {
            e.printStackTrace();
//            log.error("【app上传失败】 :", e);
            return "redirect:/addfileView";
        }
        return "redirect:/fileInfo";
    }
    //    导入用户数据
    @RequestMapping(value = "/uploadExcel")
    public String uploadExcel(@RequestParam("excelfile")MultipartFile file,RedirectAttributes model){
        model.addFlashAttribute("flg", 1);
        //判断文件是否为空
        if(file==null){
            model.addFlashAttribute("msg", "文件不能为空");
            return "redirect:/usersPage";
        }
        String fileName=file.getOriginalFilename();
        //验证文件名是否合格
        if(!ExcelImportUtils.validateExcel(fileName)){
            model.addFlashAttribute("msg", "文件必须为excel");
            return "redirect:/usersPage";
        }
        //进一步判断文件内容是否为空（即判断其大小是否为0或其名称是否为null）
        long size=file.getSize();
        if(StringUtils.isEmpty(fileName) || size==0){
            model.addFlashAttribute("msg", "文件不能为空");
            return "redirect:/usersPage";
        }
        //批量导入

        String message = userService.batchImport(fileName,file);
        model.addFlashAttribute("msg", message);
        return "redirect:/usersPage";

    }

    @RequestMapping("/editfileView")
    public String editFile(String id, RedirectAttributes model){
        FileInfo fileInfo = fileInfoService.selectById(Integer.parseInt(id));
        model.addFlashAttribute("id",fileInfo.getId());
        model.addFlashAttribute("filename",fileInfo.getFilename());
        model.addFlashAttribute("fileaddress",fileInfo.getFileaddress());
        model.addFlashAttribute("filedescription",fileInfo.getFiledescription());
        model.addFlashAttribute("fileimgsrc",fileInfo.getFileimgsrc());
        model.addFlashAttribute("createtime",fileInfo.getCreatetime());
        model.addFlashAttribute("enable",fileInfo.getEnable());
        model.addFlashAttribute("filetype",fileInfo.getFiletype());
        model.addFlashAttribute("subtitle",fileInfo.getSubtitle());
        model.addFlashAttribute("disciplines",fileInfo.getDisciplines());
        model.addFlashAttribute("appVideoUrl",fileInfo.getAppvideourl());
        model.addFlashAttribute("pdfUrl",fileInfo.getPdfurl());
        return "redirect:/editfileView";
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public String editFileInfo(@RequestParam("lefile") MultipartFile appFile, @RequestParam("imgfile") MultipartFile imgfile,
                               @RequestParam("fileaddresstem")String fileaddresstem, @RequestParam("fileaimgtem")String fileaimgtem,
                               @RequestParam("appVideoUrltem")String appVideoUrltem, @RequestParam("pdfUrltem")String pdfUrltem,
                               @RequestParam("appVideoFile") MultipartFile appVideoFile,
                               @RequestParam("pdffile") MultipartFile pdffile,FileInfo fileInfo){
        OSSConfigure ossConfigure = null;
        try {
            ossConfigure = new OSSConfigure();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if(!appFile.isEmpty()){
                OSSManageUtil.deleteFile(ossConfigure,fileaddresstem.replace(ossConfigure.getAccessUrl()+"/",""));
                fileInfo.setFileaddress(OSSManageUtil.uploadFile(appFile,appFile.getName()));
            }else {
                fileInfo.setFileaddress(fileaddresstem);
            }
            if(!imgfile.isEmpty()){

                OSSManageUtil.deleteFile(ossConfigure,fileaimgtem.replace(ossConfigure.getAccessUrl()+"/",""));
                fileInfo.setFileimgsrc(OSSManageUtil.uploadFile(imgfile,imgfile.getName()));
            }else {
                fileInfo.setFileimgsrc(fileaimgtem);
            }
            if(!appVideoFile.isEmpty()){

                OSSManageUtil.deleteFile(ossConfigure,appVideoUrltem.replace(ossConfigure.getAccessUrl()+"/",""));
                fileInfo.setAppvideourl(OSSManageUtil.uploadFile(appVideoFile,appVideoFile.getName()));
            }else {
                fileInfo.setAppvideourl(appVideoUrltem);
            }
            if(!pdffile.isEmpty()){

                OSSManageUtil.deleteFile(ossConfigure,pdfUrltem.replace(ossConfigure.getAccessUrl()+"/",""));
                fileInfo.setPdfurl(OSSManageUtil.uploadFile(pdffile,pdffile.getName()));
            }else {
                fileInfo.setPdfurl(pdfUrltem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fileInfo.setCreatetime(VTools.getCurrentDate());
        fileInfoService.updateNotNull(fileInfo);
        return "redirect:/fileInfo";
    }


    @RequestMapping(value = "/viewUserFile",method = RequestMethod.GET)
    public String viewserFile(@RequestParam(value = "userid")String userid,RedirectAttributes model){
        User user = userService.selectByUserId(Integer.parseInt(userid));
        model.addFlashAttribute("userid",userid);
        model.addFlashAttribute("username",user.getUsername());

        return "redirect:/viewUserFile";
    }
    @RequestMapping("/download")
    public void downstudents(HttpServletResponse response)throws IOException{
        String[] headers = { "资源名", "资源地址", "副标题","学科","资源类型","资源介绍","资源图片","应用视频","pdf文件","创建时间","是否启用"};
        String fileName = "资源明细"+System.currentTimeMillis()+".xls"; //文件名
        String sheetName = "资源明细";//sheet名
        List<FileInfo> list = fileInfoService.findAll();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String [][]values = new String[list.size()][];
        for(int i=0;i<list.size();i++){
            values[i] = new String[headers.length];
            //将对象内容转换成string
            FileInfo fileInfo = list.get(i);
            values[i][0] = fileInfo.getFilename()+"";
            values[i][1] = fileInfo.getFileaddress()+"";
            values[i][2] = fileInfo.getSubtitle();
            values[i][3] = fileInfo.getDisciplines()+"";
            if ("0".equals(fileInfo.getFiletype())){
                values[i][4] = "应用";
            }else {
                values[i][4] = "视频";
            }
            values[i][5] = fileInfo.getFiledescription()+"";
            values[i][6] = fileInfo.getFileimgsrc()+"";
            values[i][7]= fileInfo.getAppvideourl()+"";
            values[i][8]=fileInfo.getPdfurl()+"";
            values[i][9] = fileInfo.getCreatetime()+"";
            if ("0".equals(fileInfo.getEnable())){
                values[i][10] = "禁用";
            }else {
                values[i][10] = "可用";
            }
        }
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, headers, values, null);
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
