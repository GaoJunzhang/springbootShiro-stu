package com.study.controller;

import com.aliyun.oss.model.OSSObjectSummary;
import com.study.model.FileInfo;
import com.study.service.FileInfoService;
import com.study.util.OSSManageUtil;
import com.study.util.VTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2017/12/7.
 */
@Controller
public class FileSourcesController {

    @Value("${accessUrl}")
    private String accessUrl;

    @Resource
    private FileInfoService fileInfoService;

    @RequestMapping(value = "/ossFileSources")
    public String ossFileSources(Model model) {

        OSSManageUtil ossManageUtil = new OSSManageUtil();
        List<OSSObjectSummary> objectSummaries = null;
        try {
            objectSummaries = ossManageUtil.getOSSobjectSummary("vstu");
            List<Map> listApp = new ArrayList<>();
            List<Map> listVideo = new ArrayList<>();
            String strTemp = "";
            for (int i = 0; i < objectSummaries.size(); i++) {
                strTemp = objectSummaries.get(i).getKey();
                if (strTemp.contains("lefile")) {
                    Map map = new HashMap();
                    map.put("address", accessUrl + "/" + strTemp);
                    map.put("name", strTemp.replace("lefile/", ""));
                    if (strTemp.contains(".apk")) {
                        listApp.add(map);
                    }else {
                        listVideo.add(map);
                    }
                }
            }
            model.addAttribute("fileListApp", listApp);
            model.addAttribute("fileListVideo", listVideo);
            return "fileSources/fileSources";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/ossFileSource")
    public String ossFileSource(Model model, String filename) {
        if (filename.contains(".apk")){
            model.addAttribute("isVideo",true);
        }else {
            model.addAttribute("isVideo",false);
        }
        model.addAttribute("fileD",filename);
        model.addAttribute("filename", accessUrl + "/lefile/" + filename);
        return "fileSources/filesource";
    }

    @RequestMapping(value = "/fileSourceAdd", method = RequestMethod.POST)
    public String fileSourceAdd(@RequestParam("imgfile") MultipartFile imgfile,
                                @RequestParam("appVideoFile") MultipartFile appVideoFile,
                                @RequestParam("pdffile") MultipartFile pdffile, FileInfo fileInfo) {
        if (fileInfo == null) {
            return "redirect:/fileSources";
        }
        try {
            String imgUrl = "";
            if (!imgfile.isEmpty()) {

                imgUrl = OSSManageUtil.uploadFile(imgfile, imgfile.getName());
            }
            String appVideoUrl = "";
            if (!appVideoFile.isEmpty()) {

                appVideoUrl = OSSManageUtil.uploadFile(appVideoFile, appVideoFile.getName());
            }
            String pdfUrl = "";
            if (!pdffile.isEmpty()) {

                pdfUrl = OSSManageUtil.uploadFile(pdffile, pdffile.getName());
            }
            fileInfo.setFileimgsrc(imgUrl);
            fileInfo.setAppvideourl(appVideoUrl);
            fileInfo.setPdfurl(pdfUrl);
            fileInfo.setCreatetime(VTools.getCurrentDate());
            fileInfoService.save(fileInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/fileSources";
        }
        return "redirect:/fileInfo";
    }
}
