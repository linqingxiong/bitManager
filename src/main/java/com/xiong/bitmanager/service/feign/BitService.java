package com.xiong.bitmanager.service.feign;

import com.xiong.bitmanager.common.BitResult;
import com.xiong.bitmanager.pojo.dto.bm.req.*;
import com.xiong.bitmanager.pojo.dto.bm.res.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @ClassName BitService
 * @Description TODO
 * @Author admin
 * @Date 2024/7/16 17:19
 * @Version 1.0
 **/
@FeignClient(
        name = "bitFeignService",
        url = "${myfeign.bmbrowser}",
        configuration = {}
)
public interface BitService {

    @PostMapping("/health")
    BitResult health();

    /* 代理检测 */
    @PostMapping("/checkagent")
    BitResult<CheckAgentResultDto> checkagent(CheckAgentReqDto checkAgentRequest);

    @PostMapping("/browser/list")
    BitResult<GetBrowserListResultDto> getBrowserList(GetBrowserListReqDto getBrowserListReqDto);

    @PostMapping("/browser/detail")
    BitResult<GetBrowserDetailResultDto> getBrowserDetail(GetBrowserDetailReqDto getBrowserDetailReqDto);

    @PostMapping("/browser/open")
    BitResult<OpenBrowserResultDto> openBrowser(OpenBrowserReqDto openBrowserReqDto);

    @PostMapping("/browser/close")
    BitResult<String> closeBrowser(CloseBrowserReqDto closeBrowserReqDto);

    @PostMapping("/browser/pids")
    BitResult<GetPidResultDto> getPids(GetPidReqDto getPidReqDto);

    @PostMapping("/browser/update")
    BitResult<CreateBrowserResultDto> createBrowser(CreateBrowserReqDto createBrowserReqDto);

    @PostMapping("/browser/update/partial")
    BitResult<UpdatePartialResultDto> updatePartial(UpdatePartialReqDto updatePartialReqDto);

    @PostMapping("/browser/fingerprint/random")
    BitResult<FingerprintRandomResultDto> fingerprintRandom(FingerprintRandomReqDto fingerprintRandomReqDto);

    @PostMapping("/browser/proxy/update")
    BitResult<Void> updateProxy(UpdateProxyReqDto updateProxyReqDto);
}
