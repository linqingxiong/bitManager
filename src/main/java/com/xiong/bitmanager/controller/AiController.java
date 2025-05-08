package com.xiong.bitmanager.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xiong.bitmanager.common.ResponseResult;
import com.xiong.bitmanager.pojo.dto.req.ChatRequestDto;
import com.xiong.bitmanager.pojo.dto.req.Img2detailDto;
import com.xiong.bitmanager.pojo.dto.req.Kw2detailDto;
import com.xiong.bitmanager.pojo.dto.res.PddetailDtoRes;
import com.xiong.bitmanager.service.feign.BmServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ai")
@Slf4j
public class AiController {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;
    @Autowired
    private BmServerService bmServerService;

    @PostMapping("/img2detail")
    public ResponseResult<PddetailDtoRes> img2detail(@RequestBody Img2detailDto img2detailDto) {
        try {
            img2detailDto.setPrompt(buildDynamicPromptWithImg(img2detailDto.getDescKws(), img2detailDto.getTitleKws()));
            img2detailDto.setImages(img2detailDto.getImages().stream().map(img -> imageToBase64WithPrefix(new File(StrUtil.replace(img, "/files", uploadDir)))).collect(Collectors.toList()));
            ResponseResult<String> res = bmServerService.img2detail(img2detailDto);
            if (res.getCode() != 0) {
                return ResponseResult.error(res.getMessage());
            } else {
                log.info("img2detail res: {}", res);
                PddetailDtoRes bean = JSONUtil.toBean(res.getData().replaceAll("(?i)^```json", "")  // 去开头标记（不区分大小写）
                        .replaceAll("```$", ""), PddetailDtoRes.class);
                return ResponseResult.success(bean);
            }
        } catch (Exception e) {
            return ResponseResult.error("img2detail failed: " + e.getMessage());
        }
    }

    @PostMapping("/kw2detail")
    public ResponseResult<PddetailDtoRes> Kw2detail(@RequestBody Kw2detailDto kw2detailDto) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("title", kw2detailDto.getTitleKws());
            jsonObject.set("desc", kw2detailDto.getDescKws());
            ResponseResult<String> res = bmServerService.generateText(new ChatRequestDto(jsonObject.toString()));
            if (res.getCode() != 0) {
                return ResponseResult.error(res.getMessage());
            } else {
                log.info("Kw2detail res: {}", res.getData());
                return ResponseResult.success(JSONUtil.parseObj(res.getData()).toBean(PddetailDtoRes.class));
            }
        } catch (Exception e) {
            log.error("kw2detail",e);
            return ResponseResult.error("img2detail failed: " + e.getMessage());
        }
    }

    /**
     * 本地图片转 Base64（带前缀）
     */
    public static String imageToBase64WithPrefix(File file) {
        return "data:image/jpeg;base64," + Base64.encode(file);
    }

    private String buildDynamicPromptWithImg(List<String> descKeywords, List<String> titleKeywords) {
        return StrUtil.format(
                "你是一个法国二手卖家，根据以下图片信息：\n"
                        + "1. 用法语生成产品标题（包含{}）\n"
                        + "2. 用法语撰写产品描述（包含{}）\n"
                        + "3. 对应的中文翻译\n"
                        + "4. 随机生成法语人名\n\n"
                        + "要求：\n"
                        + "- 整理成严格合法的 JSON 格式\n"
                        + "- Key 为：frTitle, frDesc, cnTitle, cnDesc, firstName, lastName\n"
                        + "- 描述要口语化，包含 3-5 个技术参数\n"
                        + "- 中文翻译需准确达意\n\n"
                        + "示例输出：\n"
                        + "{}",
                formatKeywords(titleKeywords),
                formatKeywords(descKeywords),
                getExampleResponse()
        );
    }

    public static void main(String[] args) {
        System.out.println(new AiController().buildDynamicPrompt("无特定关键词", "无特定关键词"));
    }

    private String buildDynamicPrompt(String descKeywords, String titleKeywords) {
        return StrUtil.format(
                "你是一个法国商品卖家，生成以下信息：\n"
                        + "1. 用法语生成产品标题（根据这些信息{}）\n"
                        + "2. 用法语撰写产品描述（根据这些信息{}）\n"
                        + "3. 对应的中文翻译\n"
                        + "4. 随机生成法语人名\n"
                        + "5. 根据标题与描述信息生成尺码，没有相关信息就不填\n\n"
                        + "5. 根据标题与描述信息生成价格，没有相关信息就不填\n\n"
                        + "要求：\n"
                        + "- 整理成严格合法的 JSON 格式\n"
                        + "- Key 为：frTitle, frDesc, cnTitle, cnDesc, firstName, lastName,size,price\n"
                        + "- 描述要口语化，包含 3-5 个技术参数\n"
                        + "- 中文翻译需准确达意\n\n"
                        + "示例输出：\n"
                        + "{}",
                titleKeywords,
                descKeywords,
                getExampleResponse()
        );
    }


    private String formatKeywords(List<String> keywords) {
        return CollUtil.isEmpty(keywords) ? "无特定关键词" : String.join("、", keywords);
    }

    private String getExampleResponse() {
        return JSONUtil.createObj()
                .set("frTitle", "Chaussures Nike Air Max 270 d'occasion")
                .set("frDesc", "Paire de Nike Air Max 270 en excellent état...")
                .set("cnTitle", "二手 Nike Air Max 270 运动鞋")
                .set("cnDesc", "Nike Air Max 270 运动鞋，成色极佳...")
                .set("firstName", "Émilie")
                .set("lastName", "Dubois")
                .set("size", "21.5")
                .set("price", "50")
                .toStringPretty();
    }
}
