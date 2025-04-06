package com.xiong.bitmanager.pojo.dto.bm;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @ClassName sdfs
 * @Description TODO
 * @Author admin
 * @Date 2024/12/26 16:28
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
public class BrowserFingerPrint {
    private String id;
    private String coreProduct;
    private long seq;
    private String coreVersion;
    private String browserID;
    private String ostype;
    private String os;
    private String architecture;
    private String osVersion;
    private String platformVersion;
    private String version;
    private String userAgent;
    private boolean isIpCreateTimeZone;
    private String timeZone;
    private int timeZoneOffset;
    private String webRTC;
    private boolean ignoreHttpsErrors;
    private String position;
    private boolean isIpCreatePosition;
    private double lat;
    private double lng;
    private String precisionData;
    private boolean isIpCreateLanguage;
    private String languages;
    private boolean isIpCreateDisplayLanguage;
    private String displayLanguages;
    private String resolutionType;
    private String resolution;
    private long openWidth;
    private long openHeight;
    private String fontType;
    private String font;
    private String canvas;
    private String canvasValue;
    private String webGL;
    private String webGLValue;
    private String webGLMeta;
    private String webGLManufacturer;
    private String webGLRender;
    private String audioContext;
    private String audioContextValue;
    private String mediaDevice;
    private String speechVoices;
    private String speechVoicesValue;
    private String hardwareConcurrency;
    private String deviceMemory;
    private boolean deviceInfoEnabled;
    private String computerName;
    private String macAddr;
    private boolean clientRectNoiseEnabled;
    private long clientRectNoiseValue;
    private String doNotTrack;
    private String portScanProtect;
    private String portWhiteList;
    private long isDelete;
    private long colorDepth;
    private String totalDiskSpace;
    private long devicePixelRatio;
    private boolean disableSslCipherSuitesFlag;
    private String plugins;
    private boolean enablePlugins;
    private boolean windowSizeLimit;
    private String createdBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    private String updateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private boolean isValidUsername;
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
    private boolean syncExtensions;
    private boolean syncUserExtensions;
    private boolean syncLocalStorage;
    private boolean credentialsEnableService;
    private boolean disableTranslatePopup;
    private boolean stopWhileIPChange;
    private boolean disableClipboard;
    private boolean disableNotifications;
    private boolean memorySaver;
    private JSONObject webgpu;
    private boolean batchRandom;
    private boolean batchUpdateFingerPrint;
    private String launchArgs;
    private String uamodel;
    private long defaultAccuracy;
}
