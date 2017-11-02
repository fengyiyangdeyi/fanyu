//package cn.com.fanyu.utils;
//
//import cn.com.fanyu.dao.FyGlobalValueRepository;
//import cn.com.fanyu.domain.FyGlobalValue;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Lazy
//@Service
//public class GlobalValue {
//    @Autowired
//    private static FyGlobalValueRepository fyGlobalValueRepository;
////    public static String IMG_URL="";
//    public static String ROBOT_SWICH="";
//    static {
//        List<FyGlobalValue> list=fyGlobalValueRepository.findByStatus(1);
//        for (FyGlobalValue g:list) {
//            String key = g.getGlobalKey();
//            if(key.equals("IMG_URL")){
////                IMG_URL=g.getGlobalKey();
//            }else if(key.equals("ROBOT_SWICH")){
//                ROBOT_SWICH=g.getGlobalValue();
//            }
//        }
//    }
//}
