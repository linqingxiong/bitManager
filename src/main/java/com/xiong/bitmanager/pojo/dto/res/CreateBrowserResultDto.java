package com.xiong.bitmanager.pojo.dto.res;

import com.xiong.bitmanager.pojo.dto.BrowserFingerPrint;
import lombok.Data;
import java.util.Date;

/**
 * @ClassName CreateBrowserResultDto
 * @Description TODO
 * @Author admin
 * @Date 2024/12/23 11:40
 * @Version 1.0
 **/
@Data
public class CreateBrowserResultDto {
    private String id;
    private int seq;
    private String code;
    private String platform;
    private String url;
    private String name;
    private String userName;
    private String password;
    private String cookie;
    private boolean isGlobalProxyInfo;
    private boolean isIpv6;
    private String proxyType;
    private String agentId;
    private String ipCheckService;
    private String host;
    private String proxyUserName;
    private String proxyPassword;
    private String lastIp;
    private String lastCountry;
    private boolean isIpNoChange;
    private String ip;
    private String country;
    private String province;
    private String city;
    private String dynamicIpUrl;
    private boolean isDynamicIpChangeIp;
    private String remark;
    private int status;
    private String operUserName;
    private boolean isDelete;
    private String delReason;
    private boolean isMostCommon;
    private boolean isRemove;
    private String tempStr;
    private String createdBy;
    private String userId;
    private Date createdTime;
    private String recycleBinRemark;
    private String mainUserId;
    private boolean abortImage;
    private boolean abortMedia;
    private boolean stopWhileNetError;
    private boolean stopWhileCountryChange;
    private boolean syncTabs;
    private boolean syncCookies;
    private boolean syncIndexedDb;
    private boolean syncBookmarks;
    private boolean syncAuthorization;
    private boolean syncHistory;
    private boolean syncGoogleAccount;
    private boolean allowedSignin;
    private boolean syncSessions;
    private String workbench;
    private boolean clearCacheFilesBeforeLaunch;
    private boolean clearCookiesBeforeLaunch;
    private boolean clearHistoriesBeforeLaunch;
    private boolean randomFingerprint;
    private boolean muteAudio;
    private boolean disableGpu;
    private boolean enableBackgroundMode;
    private boolean syncExtensions;
    private boolean syncUserExtensions;
    private boolean syncLocalStorage;
    private boolean credentialsEnableService;
    private boolean disableTranslatePopup;
    private boolean stopWhileIpChange;
    private boolean disableClipboard;
    private boolean disableNotifications;
    private boolean memorySaver;
    private BrowserFingerPrint browserFingerPrint;

}
