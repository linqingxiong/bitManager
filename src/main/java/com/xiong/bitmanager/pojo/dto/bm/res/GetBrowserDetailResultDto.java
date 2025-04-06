package com.xiong.bitmanager.pojo.dto.bm.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiong.bitmanager.pojo.dto.bm.BrowserFingerPrint;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName GetBrowserDetailResultDto
 * @Description TODO
 * @Author admin
 * @Date 2024/12/26 16:41
 * @Version 1.0
 **/
@Data
public class GetBrowserDetailResultDto {
    private String id;
    private long seq;
    private String code;
    private String platform;
    private String platformIcon;
    private String url;
    private String name;
    private String userName;
    private String password;
    private String cookie;
    private String otherCookie;
    private boolean isGlobalProxyInfo;
    private boolean isIpv6;
    private long proxyMethod;
    private String proxyType;
    private String agentID;
    private String ipCheckService;
    private String host;
    private long port;
    private String proxyUserName;
    private String proxyPassword;
    private String lastIP;
    private String lastCountry;
    private boolean isIPNoChange;
    private String ip;
    private String country;
    private String province;
    private String city;
    private String dynamicIPChannel;
    private String dynamicIPURL;
    private boolean isDynamicIPChangeIP;
    private String remark;
    private long status;
    private String operUserName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime closeTime;
    private long isDelete;
    private String delReason;
    private long isMostCommon;
    private long isRemove;
    private String createdBy;
    private String userID;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    private String updateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private String recycleBinRemark;
    private String mainUserID;
    private boolean abortImage;
    private boolean abortMedia;
    private boolean stopWhileNetError;
    private boolean stopWhileCountryChange;
    private boolean syncTabs;
    private boolean syncCookies;
    private boolean syncIndexedDB;
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
    private boolean disableGPU;
    private boolean enableBackgroundMode;
    private long abortImageMaxSize;
    private boolean syncExtensions;
    private boolean syncUserExtensions;
    private boolean syncLocalStorage;
    private boolean credentialsEnableService;
    private boolean disableTranslatePopup;
    private boolean stopWhileIPChange;
    private boolean disableClipboard;
    private boolean disableNotifications;
    private boolean memorySaver;
    private BrowserFingerPrint browserFingerPrint;
    private boolean belongToMe;
    private long isShare;
    private boolean isValidUsername;
    private long createNum;
    private boolean isRandomFinger;
    private long remarkType;
    private String refreshProxyURL;
    private long duplicateCheck;
    private String randomKey;
    private String syncBrowserAccount;
    private String cookieBak;
    private long manual;
    private boolean clearCacheWithoutExtensions;
    private boolean syncPaymentsAndAddress;
    private Object[] extendIDS;
    private long isSynOpen;
    private String coreProduct;
    private long sort;
}
