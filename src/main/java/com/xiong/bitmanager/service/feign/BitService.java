package com.xiong.bitmanager.service.feign;

import com.xiong.bitmanager.common.BitResult;
import com.xiong.bitmanager.pojo.dto.req.*;
import com.xiong.bitmanager.pojo.dto.res.*;
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
        url = "http://127.0.0.1:54345",
        configuration = {}
)
public interface BitService {
    @PostMapping("/browser/list")
    BitResult<GetBrowserListResultDto> getBrowserDetail(GetBrowserListReqDto getBrowserListReqDto);

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
}
