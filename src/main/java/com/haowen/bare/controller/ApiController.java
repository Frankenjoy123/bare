package com.haowen.bare.controller;

import com.alibaba.excel.EasyExcel;
import com.haowen.bare.excel.DownloadUtil;
import com.haowen.bare.excel.ExcelItem;
import com.haowen.bare.result.BareResult;
import com.haowen.bare.service.BareService;
import com.haowen.bare.utils.ResponseUtil;
import com.haowen.bare.utils.ReturnObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.util.List;

/**
 * 外部接口
 */
@Api(tags = "短视频/图片去水印")
@RestController
public class ApiController {

    @Resource
    private BareService bareService;

    private static final String EXCEL_PATH = "E:\\多多\\多多视频批量下载任务.xlsx";

    private static final String BASE_VIDEO_PATH = "E:\\多多\\视频库";

    /**
     * 获取无水印资源地址列表
     *
     * @param link 复制的链接
     */
    @ApiOperation(value = "聚合接口", response = BareResult.class)
    @PostMapping("/bare")
    private ReturnObject<BareResult> bare(
            @NotBlank(message = "请输入复制链接")
            @ApiParam("复制的链接")
            @RequestParam("link") String link) throws Exception {
        return ResponseUtil.ok(bareService.parse(link));
    }

    @ApiOperation(value = "单个聚合接口", response = BareResult.class)
    @PostMapping("/bare/single")
    private ReturnObject<BareResult> bareSingle(@RequestBody Item item) throws Exception {
        String link = item.getData();
        return ResponseUtil.ok(bareService.parse(link));
    }


    @ApiOperation(value = "excel聚合接口", response = BareResult.class)
    @GetMapping("/bare/parseFromExcel")
    private ReturnObject<BareResult> parseFromExcel() throws Exception {

        List<ExcelItem> list = EasyExcel.read(EXCEL_PATH).head(ExcelItem.class).sheet().doReadSync();

        for (ExcelItem excelItem : list){
            String link = excelItem.getLink();
            BareResult bareResult = bareService.parse(link);

            if (bareResult == null){
                continue;
            }

            List<BareResult.Video> videos = bareResult.getVideos();
            if (CollectionUtils.isEmpty(videos)){
                continue;
            }

            BareResult.Video video = videos.get(0);

            String saveDir = BASE_VIDEO_PATH +  File.separator + excelItem.getProduct();

            String fileName = excelItem.getCode() + ".mp4";

            try {
                DownloadUtil.downLoadFromUrl(video.getUrl() , fileName , saveDir);
            } catch (Exception e) {
                System.err.printf(e.getMessage());
            }

        }

        return ResponseUtil.ok(null);
    }


    private static final class Item{
        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}