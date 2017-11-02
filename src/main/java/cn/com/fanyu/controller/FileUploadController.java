package cn.com.fanyu.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import cn.com.fanyu.utils.ResultCode;
import cn.com.fanyu.utils.ResultJson;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/upload")
public class FileUploadController {
    @RequestMapping(value = "/singleUpload")
    public String singleUpload() {
        return "singleUpload";
    }

    @RequestMapping(value = "/singleSave", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    public @ResponseBody
    String singleSave(@RequestParam("file") MultipartFile file, @RequestParam("desc") String desc) {
        System.out.println("File Description:" + desc);
        String fileName = null;
        if (!file.isEmpty()) {
            try {
                fileName = file.getOriginalFilename();
                byte[] bytes = file.getBytes();
                BufferedOutputStream buffStream =
                        new BufferedOutputStream(new FileOutputStream(new File("F:/cp/" + fileName)));
                buffStream.write(bytes);
                buffStream.close();
//                return "You have successfully uploaded " + fileName;
                Map map =new HashMap<>();
                map.put("url",fileName);
                return new ResultJson(ResultCode.SUCCESS_CODE, "", "", JSON.toJSONString(map)).toString();
            } catch (Exception e) {
                return new ResultJson(ResultCode.FAILE_CODE, "", "You failed to upload " + fileName + ": " + e.getMessage(), "").toString();
            }
        } else {
            return new ResultJson(ResultCode.FAILE_CODE, "", "Unable to upload. File is empty.", "").toString();
        }
    }

    @RequestMapping(value = "/multipleUpload")
    public String multiUpload() {
        return "multipleUpload";
    }

    @RequestMapping(value = "/multipleSave", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    public @ResponseBody
    String multipleSave(@RequestParam("file") MultipartFile[] files) {
        String fileName = null;
        String msg = "";
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                try {
                    fileName = files[i].getOriginalFilename();
                    byte[] bytes = files[i].getBytes();
                    BufferedOutputStream buffStream =
                            new BufferedOutputStream(new FileOutputStream(new File("F:/cp/" + fileName)));
                    buffStream.write(bytes);
                    buffStream.close();
                    msg += "You have successfully uploaded " + fileName + "<br/>";
                } catch (Exception e) {
                    return "You failed to upload " + fileName + ": " + e.getMessage() + "<br/>";
                }
            }
            return msg;
        } else {
            return "Unable to upload. File is empty.";
        }
    }
}