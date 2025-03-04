package com.xiong.bitmanager;

import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xiong.bitmanager.common.BitResult;
import com.xiong.bitmanager.pojo.dto.BrowserFingerPrint;
import com.xiong.bitmanager.pojo.dto.req.*;
import com.xiong.bitmanager.pojo.dto.res.*;
import com.xiong.bitmanager.service.feign.BitService;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v130.network.Network;
import org.openqa.selenium.devtools.v130.network.model.Headers;
import org.openqa.selenium.devtools.v130.network.model.LoadingFailed;
import org.openqa.selenium.devtools.v130.network.model.RequestWillBeSent;
import org.openqa.selenium.devtools.v130.network.model.ResponseReceived;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest
@Slf4j
class DhgateTests {
    @Resource
    private BitService bitService;
    // 创建一个线程池来处理异步日志记录
    private static final ExecutorService logExecutor = Executors.newFixedThreadPool(4);
    private String findItemcode = "1011573598";
    private String keyword = "1011573598";
    private String keys = "my order";
    private String shippingTypeId = "Seller-Defined STD Logitstics";
    private int maxPage = 20;
    private String countryId = "us";
    private Map<String, Integer> skuQuantity = new HashMap<>();

    {
        skuQuantity.put("986397_66_1000", 3);
        skuQuantity.put("986398_67_1000", 3);
    }

    private Map<String, String> skuSkuidMap = new HashMap<>();

    private boolean regiserIs = false;

    @Test
    void contextLoads() {
        BitResult<GetBrowserListResultDto> getBrowserListResultDtoBitResult = bitService.getBrowserDetail(new GetBrowserListReqDto(0, 10, "敦煌测试"));
        log.info("getBrowserListResultDtoBitResult:{}", JSONUtil.toJsonStr(getBrowserListResultDtoBitResult));
        if (!getBrowserListResultDtoBitResult.getData().getList().isEmpty()) {
            GetBrowserListResultDto.BrowerInfo browerInfo = getBrowserListResultDtoBitResult.getData().getList().get(0);
            BitResult<GetPidResultDto> pidsResult = bitService.getPids(new GetPidReqDto(browerInfo.getId()));
            log.info("pidsResult:{}", JSONUtil.toJsonStr(pidsResult));
            if (pidsResult.getData().containsKey(browerInfo.getId())) {
                BitResult<String> closeBrowserResultDtoBitResult = bitService.closeBrowser(new CloseBrowserReqDto(browerInfo.getId()));
                log.info("closeBrowserResultDtoBitResult:{}", JSONUtil.toJsonStr(closeBrowserResultDtoBitResult));
                sleep(5);
            }
            String id = browerInfo.getId();
            BitResult<GetBrowserDetailResultDto> browserDetail = bitService.getBrowserDetail(new GetBrowserDetailReqDto().setId(id));
            BrowserFingerPrint browserFingerPrint = browserDetail.getData().getBrowserFingerPrint();
            BitResult<FingerprintRandomResultDto> fingerprintRandomResultDtoBitResult = bitService.fingerprintRandom(new FingerprintRandomReqDto(browserFingerPrint.getBrowserID()));
            log.info("fingerprintRandomResultDtoBitResult:{}", JSONUtil.toJsonStr(fingerprintRandomResultDtoBitResult));

            BitResult<OpenBrowserResultDto> openBrowserResultDtoBitResult = bitService.openBrowser(new OpenBrowserReqDto(browerInfo.getId()));
            log.info("openBrowserResultDtoBitResult:{}", JSONUtil.toJsonStr(openBrowserResultDtoBitResult));

            String driverUrl = openBrowserResultDtoBitResult.getData().getDriver();


            // ChromeDriver 的可执行文件路径
            File driverPath = new File(driverUrl);

            // 创建 ChromeDriverService 的构建器
            ChromeDriverService.Builder serviceBuilder = new ChromeDriverService.Builder()
                    .usingDriverExecutable(driverPath);

            // 设置环境变量（如果需要）
//            Map<String, String> envVars = new HashMap<>();
//            envVars.put("DISPLAY", ":0"); // 仅在 Linux 上使用 Xvfb 时需要
//            serviceBuilder.withEnvironment(envVars);

            // 构建并启动 ChromeDriverService
            ChromeDriverService service = serviceBuilder.build();

            // 创建 ChromeOptions（可选）
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--enable-automation");
            options.addArguments("--incognito");
            options.setExperimentalOption("debuggerAddress", openBrowserResultDtoBitResult.getData().getHttp());
//            options.addArguments("--no-sandbox"); // 添加启动参数

            // 使用 ChromeDriverService 和 ChromeOptions 初始化 WebDriver
            ChromeDriver driver = new ChromeDriver(service, options);


            sleep(1);
            driver.manage().deleteAllCookies();
            // 打印 网络请求日志
//            showNetworkLog(driver);
            // 使用 WebDriver 进行操作
            driver.get("https://www.dhgate.com");

            // 获取当前活动窗口的句柄
            String currentWindowHandle = driver.getWindowHandle();
            // 获取所有窗口的句柄
            Set<String> allWindowHandles = driver.getWindowHandles();
            // 关闭除当前活动窗口之外的所有窗口
            for (String handle : allWindowHandles) {
                if (!handle.equals(currentWindowHandle)) {
                    driver.switchTo().window(handle);
                    driver.close();
                }
            }
            // 切换回原始窗口
            driver.switchTo().window(currentWindowHandle);
//            WebElement closeCoupon = untilFind(driver, By.className("wapToappCouponClose"), 30);
//            if (closeCoupon != null){
//                log.info("点击关闭 coupon");
//                closeCoupon.click();
//            }
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("document.body.style.zoom='50%'");
            // 点击关闭优惠券
            WebElement wapToappCouponClose = untilFindOrNull(driver, By.className("wapToappCouponClose"), 10);
            if (wapToappCouponClose != null) {
                wapToappCouponClose.click();
            }

            untilFind(driver, By.className("new-head-category"), 10).click();
            sleepMs(500);

            waitLoading(driver);
            WebElement categoryOutName = findElementOrNull(driver, By.className("category-out-name"));
            if (categoryOutName == null) {
                WebElement signOrJoin = driver.findElement(By.className("category-sign-inner"));
                // 注册或登录
                if (regiserIs) {
                    signOrJoin.findElements(By.tagName("span")).get(1).click();
                    WebElement oneClickSignin = untilFindOrNull(driver, By.className("j-local-single-submit"), 10);
                    if (oneClickSignin != null) {
                        oneClickSignin.click();
                    } else {
                        WebElement signinUsername = untilFind(driver, By.id("registerEmail"), 10);
                        signinUsername.click();
                        sendKey(signinUsername, "asd2fasfddfh54rtr@gmall.com");
                        WebElement signinPassword = untilFind(driver, By.id("pageRegisterPassword"), 10);
                        signinPassword.click();
                        sendKey(signinPassword, "dh1357900");
                        driver.findElement(By.className("login-tips")).click();
                        driver.findElement(By.className("j-popup-register-btn")).click();
                        untilFind(driver, By.className("newBuyAutoBindCouponClose"), 30).click();
                    }
                } else {
                    signOrJoin.findElement(By.tagName("span")).click();
                    WebElement oneClickSignin = untilFindOrNull(driver, By.className("j-local-single-submit"), 10);
                    if (oneClickSignin != null) {
                        oneClickSignin.click();
                    } else {
                        WebElement signinUsername = untilFind(driver, By.id("signinUsername"), 10);
                        signinUsername.click();
                        sendKey(signinUsername, "asdfasfefq2ewrtr@gmall.com");
                        WebElement signinPassword = untilFind(driver, By.id("signinPassword"), 10);
                        signinPassword.click();
                        sendKey(signinPassword, "dh1357900");
                        driver.findElement(By.className("j-popup-signin-btn")).click();
                    }
                }
            } else {
                // todo 检测账号是否对应
                WebElement category = driver.findElement(By.className("j-categoryWarp"));
                int width = category.getSize().getWidth();
                int height = category.getSize().getHeight();
// 创建Actions对象
                Actions actions = new Actions(driver);
                actions.moveToElement(category).moveByOffset(width / 2 + 10, 0).click().perform();
            }

            Actions actions = new Actions(driver);

            log.info("点击搜索框");
            WebElement searchBar = untilFind(driver, By.className("newhead-search"), 10);
            searchBar.click();
            sleepMs(300);
            log.info("清空搜索框");
            WebElement searchInput = untilFind(driver, By.id("J_searchInput"), 10);
            searchInput.clear();
            log.info("输入关键词");
            sendKey(searchInput, keyword);
            log.info("点击搜索");
            WebElement searchBtn = untilFind(driver, By.className("search-btnWarp"), 10);
            sleepMs(300);
            searchBtn.click();
            sleep(1);
            boolean firstSearch = true;
            WebElement findProduct = null;
            int pageNum = 1;
            int idx = 0;
            while (firstSearch || pageNum < maxPage) {
                if (!firstSearch) {
                    WebElement showmore = untilFindOrNull(driver, By.className("j-showmore"), 10);
                    if (showmore != null) {
                        log.info("点击下一页：{}", pageNum);
                        scrollAndClick(driver, actions, showmore);
                    } else {
                        log.info("找到最后一页了，没找到");
                        break;
                    }
                    pageNum++;
                }
                firstSearch = false;
                List<WebElement> productList = untilFind(driver, By.id("J_list"), 30).findElements(By.tagName("li"));
                for (; idx < productList.size(); idx++) {
                    WebElement productWebElement = productList.get(idx);
                    actions.moveToElement(productWebElement).perform();
                    if (StrUtil.equals(productWebElement.findElement(By.tagName("a")).getAttribute("itemcode"), findItemcode)) {
                        log.info("找到啦");
                        findProduct = productWebElement;
                        break;
                    }
                }
                if (findProduct != null) {
                    break;
                }
            }


            if (findProduct == null) {
                throw new RuntimeException("未找到产品");
            }
            log.info("点击产品");
            actions.moveToElement(findProduct).perform();
            findProduct.click();
//            untilFind(driver, By.className("itemAttrList"), 30).click();

            sleep(1);
            log.info("查看产品详情");
            WebElement desc = untilFind(driver, By.id("hapdescriptions"), 10);
            actions.moveToElement(desc).perform();
            WebElement seeall = findElementOrNull(driver, By.className("desc-seeall"));
            if (seeall != null) {
                sleepMs(300);
                actions.moveToElement(seeall).perform();
                jsScrollY(driver, 200);

                seeall.click();
                sleep(1);
            }
            sleep(1);
            log.info("点击加购");
            driver.findElement(By.className("layer-bottom-box")).findElements(By.tagName("span")).get(0).click();
            log.info("弹出 sku 选择");
            List<WebElement> skus = untilFindsPresence(driver, By.className("j-skuAttrWrap"), 10);
            JSONArray skusJson = new JSONArray();
            Map<String, WebElement> skuAttrValMap = new HashMap<>();
            for (WebElement sku : skus) {
                if (StrUtil.contains(sku.getAttribute("class"), "hide")) {
                    log.info("跳过隐藏 sku");
                    continue;
                }
                JSONObject skuJson = new JSONObject();
                WebElement optionsList = sku.findElement(By.className("options-list"));
                String attrName = optionsList.getAttribute("data-name");
                String attrId = optionsList.findElement(By.tagName("ul")).getAttribute("group_id");
                skuJson.set("attrName", attrName);
                skuJson.set("attrId", attrId);
                List<WebElement> skuAttrs = optionsList.findElements(By.tagName("li"));
                JSONArray skuAttrValsJson = new JSONArray();
                for (WebElement skuAttr : skuAttrs) {
//                    actions.moveToElement(skuAttr).perform();
//                    skuAttr.click();
//                    sleepMs(600);
//                    break;
                    JSONObject skuAttrValJson = new JSONObject();
                    String attrValId = skuAttr.getAttribute("attr_id");
                    skuAttrValJson.set("attrValId", attrValId);
                    WebElement img = findElementOrNull(skuAttr, By.tagName("img"));
                    String attrValName = "";
                    if (img != null) {
                        attrValName = img.getAttribute("alt");
                    } else {
                        attrValName = skuAttr.findElement(By.tagName("span")).getText();
                    }
                    skuAttrValJson.set("attrValName", attrValName);
                    skuAttrValMap.put(attrValId, skuAttr);
                    skuAttrValsJson.put(skuAttrValJson);
                }
                skuJson.set("attrVals", skuAttrValsJson);
                skusJson.put(skuJson);
            }

            boolean firstFor = true;

            for (String skuStr : skuQuantity.keySet()) {
                if (!firstFor) {
                    driver.navigate().back();
                }
                firstFor = false;
                String[] attrVals = skuStr.split("_");
                // 选择 sku
                for (String attrVal : attrVals) {
                    WebElement element = skuAttrValMap.get(StrUtil.trim(attrVal));
                    if (element == null) {
                        throw new RuntimeException("未找到该sku：" + skuStr);
                    }
                    log.info("点击 sku 属性：" + attrVal);
                    actions.moveToElement(element).perform();
                    element.click();
                }
                sleep(1);
                skuSkuidMap.put(skuStr, untilFindPresence(driver, By.className("j-selectSkuAttr"), 10).getAttribute("data-skuid"));
                log.info("点击加购确认");
                driver.findElement(By.className("j-addToCart")).click();
                untilFinds(driver, By.className("j-orderContbox"), 30);

//                Optional<WebElement> findPdElement = orders.stream().filter(order -> StrUtil.equals(order.getAttribute("itemcode"), findItemcode)).findAny();
//                if (findPdElement.isPresent()) {
//                    Set<String> skuids = findPdElement.get().findElements(By.className("j-od-box")).stream()
//                            .map(pdBox -> {
//                                // 解码 URL 编码的字符串
//                                String decodedJson = URLDecoder.decode(pdBox.getAttribute("data-info"), StandardCharsets.UTF_8);
//                                String skuId = JSONUtil.parseObj(decodedJson).getStr("skuId");
//                                if (StrUtil.isBlank(skuId)){
//                                    throw new RuntimeException("购物车查询加购信息错误");
//                                }
//                                return skuId;
//                            }).collect(Collectors.toSet());
//                    if (skuids.size() > skuSkuidMap.size()){
//                        // 返回继续加购
//                    }else if (skuSkuidMap.){
//
//                    }
//                } else {
//                    log.info("购物车未找到该产品：" + findItemcode);
//                }
            }

            // 选择 sku
//            actions.moveToElement(skuAttr).perform();
//            skuAttr.click();

//            for (Object o : skusJson) {
//                JSONObject skuJson = (JSONObject) o;
//                Object o1 = skuJson.getJSONArray("attrVals").getJSONObject(0).get("element");
//                log.info(o1.getClass().getName());
////                element.click();
////                sleep(1);
//            }
            String countryid = untilFind(driver, By.className("shipAdressEntrance"), 30).getAttribute("data-countryid");
            log.info("对比国家");
            if (!StrUtil.equalsIgnoreCase(countryid, countryId)) {
                driver.findElement(By.className("shipAdressEntranceWrap")).click();
                untilFinds(driver, By.className("country-list"), 30).stream()
                        .filter(webElement -> StrUtil.equalsIgnoreCase(webElement.findElement(By.tagName("li")).getAttribute("data-whithercountryid"), countryId))
                        .findAny().get().click();
            }
            waitLoading(driver);
            log.info("对比加购产品");
            AtomicReference<List<WebElement>> pds = new AtomicReference<>(untilFindsPresence(driver, By.className("j-od-box"), 30));
            for (int i = 0; i < pds.get().size(); i++) {
                AtomicReference<WebElement> pdBox = new AtomicReference<>(pds.get().get(i));
                int finalI = i;
                waitLoading(driver, () -> {
                    pds.set(untilFindsPresence(driver, By.className("j-od-box"), 30));
                    pdBox.set(pds.get().get(finalI));
                });
                String decodedJson = URLDecoder.decode(pdBox.get().getAttribute("data-info"), StandardCharsets.UTF_8);
                String skuId = JSONUtil.parseObj(decodedJson).getStr("skuId");
                for (Map.Entry<String, String> stringStringEntry : skuSkuidMap.entrySet()) {
                    String s = stringStringEntry.getKey();
                    String sid = stringStringEntry.getValue();
                    if (StrUtil.equals(sid, skuId)) {
                        log.info("检查加购商品是否正确");
                        String quantityStr = pdBox.get().findElement(By.className("j-crud-num")).getAttribute("value");
                        Integer quantity = Integer.valueOf(quantityStr);
                        if (!quantity.equals(skuQuantity.get(s))) {
                            WebElement quantityElm = pdBox.get().findElement(By.className("j-crud-num"));
                            quantityElm.click();
                            quantityElm.sendKeys(Keys.chord(Keys.CONTROL, "a"));
                            sendKey(quantityElm, String.valueOf(skuQuantity.get(s)));
                            WebElement element = pdBox.get().findElement(By.className("unit-price"));
                            actions.moveToElement(element).perform();
                            element.click();
                            waitLoading(driver, () -> {
                                pds.set(untilFindsPresence(driver, By.className("j-od-box"), 30));
                                pdBox.set(pds.get().get(finalI));
                            });
                            WebElement checkimg = pdBox.get().findElement(By.className("od-checkimg"));
                            if (!StrUtil.contains(checkimg.getAttribute("class"), " checked ")) {
                                checkimg.click();
                                waitLoading(driver, () -> {
                                    pds.set(untilFindsPresence(driver, By.className("j-od-box"), 30));
                                    pdBox.set(pds.get().get(finalI));
                                });
                            }
                        }
                    }

                }
                skuSkuidMap.forEach((s, sid) -> {
                });

            }
            untilFind(driver, By.className("j-checkOut"), 10).click();
            sleep(3);

            if (StrUtil.contains(driver.getCurrentUrl(), "placeOrder.html")) {
                // placeorder 页面
                WebElement moreShippingAddress = untilFindOrNull(driver, By.className("moreShippingAddress"), 10);
                if (moreShippingAddress != null) {
                    driver.findElement(By.className("j-contactName")).sendKeys("dfsfefsdf");
                    driver.findElement(By.className("j-billEdits-areacode")).sendKeys("1");
                    driver.findElement(By.className("j-billEdits-telephone")).sendKeys("5644523456");
                    driver.findElement(By.className("j-inputAddress")).click();
                    untilFind(driver, By.className("j-searchquickInput"), 5).sendKeys("1861 100 th avenue");
                    untilFind(driver, By.className("j-querystateCityInfo"), 5).findElement(By.tagName("li")).click();
                    driver.findElement(By.className("j-billEdits-address2")).sendKeys("fd");
                    driver.findElement(By.className("j-billEdits-state")).click();
                    untilFind(driver, By.className("j-searchStatesInput"), 10).sendKeys("Marshall Islands");
                    untilFind(driver, By.className("j-statesLayerContent"), 10).findElement(By.tagName("p")).click();
                    untilFind(driver, By.className("j-countryCityLayerContent"), 10).findElements(By.tagName("p")).get(0).click();
                    driver.findElement(By.className("j-billEdits-city")).sendKeys("Otho");
                    driver.findElement(By.className("j-billEdits-zipcode")).sendKeys("75222-0114");
                    driver.findElement(By.className("j-saveNewBillBtn")).click();
                }
            }

            // todo 验证订单情况
            log.info("设置 remark");
//            untilFind(driver, By.className("j-pay-warp"), 30);
            List<WebElement> remarks = untilFindsPresence(driver, By.className("j-remark"), 30);
            WebElement remark = remarks.get(remarks.size() - 1);
            scroll(driver, actions, remark);
            remark.findElement(By.tagName("textarea")).sendKeys(keys);
            List<WebElement> shippingCostWarps = driver.findElements(By.className("j-shipping-cost-warp"));
            scrollAndClick(driver, actions, shippingCostWarps.get(shippingCostWarps.size() - 1));
            Optional<WebElement> shippmentOp = untilFindPresence(driver, By.className("j-shipList"), 30).findElements(By.tagName("li")).stream().filter(element -> {
                String other = element.getAttribute("other");
                String[] strArr = other.split(",");
                try {
                    String shippingTypeId = StrUtil.trimEnd(StrUtil.trimStart(strArr[0].split(":")[1]));
                    String freightAmountRealFinal = StrUtil.trimEnd(StrUtil.trimStart(strArr[1].split(":")[1]));
                    return StrUtil.equals(shippingTypeId, this.shippingTypeId);
                } catch (Exception e) {
                    return false;
                }
            }).findAny();
            shippmentOp.ifPresent(webElement -> scrollAndClick(driver, actions, webElement));
            scrollAndClick(driver, actions, untilFindPresence(driver, By.className("j-coupon"), 10));
            untilFindPresence(driver, By.className("j-dhCouponWarp"), 30).findElements(By.tagName("p"))
                    .stream().filter(webElement -> StrUtil.equals(webElement.getAttribute("type"), "del"))
                    .findAny().ifPresent(webElement -> {
                        scrollAndClick(driver, actions, webElement);
                    });
            driver.findElement(By.className("j-ok")).click();
//            untilFind(driver, By.className("creditcard-newcard"), 30).click();
            retry(() -> untilFind(driver, By.className("j-pay-newcard"), 30).click());
            log.info("填写卡片信息");
            WebElement cardNumber = untilFindPresence(driver, By.className("j-pay-addcard-number"), 10);
            scroll(driver, actions, cardNumber);
            cardNumber.clear();
            cardNumber.sendKeys("5520325721385079");
            new Select(driver.findElement(By.className("j-pay-addcard-month"))).selectByValue("09");
            new Select(driver.findElement(By.className("j-pay-addcard-year"))).selectByValue("2025");
            WebElement cardCvv = driver.findElement(By.className("j-pay-addcard-cvv"));
            cardCvv.clear();
            cardCvv.sendKeys("687");
            driver.findElement(By.className("pay-addcard-confim-btn")).click();
            retry(() -> {
                WebElement payCvv = untilFind(driver, By.className("j-pay-cvv"), 30);
                WebElement payCvvInput = payCvv.findElement(By.tagName("input"));
                payCvvInput.clear();
                payCvvInput.sendKeys("687");
            });
            untilFind(driver, By.className("placeOrder-submitBox"), 30);
            ((JavascriptExecutor) driver).executeScript("document.getElementsByClassName('j-submit')[0].click()");
            log.info("执行完了");


            // ... 更多操作 ...
//            WebElement form = untilFind(driver, By.tagName("form"), 60);
//            WebElement title_1 = form.findElement(By.tagName("input"));
//            title_1.click();
//            sleep(2);
//            sendKey(title_1,"Nike Baskets Chaussures sport'07 White 37.5");
//            WebElement button = untilFind(driver, By.cssSelector("form button:first-child"), 60);
//            sleep(2);
//            button.click();
//            untilFind(driver, By.cssSelector("form button[type='submit']"), 60).click();
//            untilFind(driver, By.xpath("//form/descendant::h3[contains(text(), 'Dites-nous en plus')]"), 60);
//
//            List<WebElement> labels = driver.findElements(
//                    By.cssSelector("form label:has(span)")
//            );
//            log.info("labels size:{}", labels.size());
//            for (WebElement label : labels) {
//                WebElement select = label.findElement(By.xpath(
//                        "following-sibling::*[1]//*[local-name()='button']"
//                ));
//                sleep(2);
//                select.click();
////                sleep(1);
////                WebElement firstLi = label.findElement(By.xpath(
////                        "following-sibling::*[2]//*[local-name()='li'][1]"
////                ));
////                firstLi.click();
//            }
//            untilFind(driver, By.cssSelector("form button[type='submit']"), 30).click();

            // 最后关闭 WebDriver 和 ChromeDriverService
//            driver.quit();
//            service.stop();

        }
    }

    private WebElement untilFind(WebDriver driver, By by, int ofSecounds) {
        // 创建 WebDriverWait 实例，等待时间设为 10 秒
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ofSecounds));
        // 等待 form 变得可见
        WebElement until = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        sleep(2);
        return until;
    }

    private WebElement untilFindOrNull(WebDriver driver, By by, int ofSecounds) {
        try {
            return untilFind(driver, by, ofSecounds);
        } catch (Exception e) {
        }
        return null;
    }

    private List<WebElement> untilFinds(WebDriver driver, By by, int ofSecounds) {
        // 创建 WebDriverWait 实例，等待时间设为 10 秒
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ofSecounds));
        // 等待 form 变得可见
        List<WebElement> until = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
        sleep(2);
        return until;
    }

    private List<WebElement> untilFindsPresence(WebDriver driver, By by, int ofSecounds) {
        // 创建 WebDriverWait 实例，等待时间设为 10 秒
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ofSecounds));
        // 等待 form 变得可见
        List<WebElement> until = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
        sleep(2);
        return until;
    }

    private WebElement untilFindPresence(WebDriver driver, By by, int ofSecounds) {
        // 创建 WebDriverWait 实例，等待时间设为 10 秒
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ofSecounds));
        // 等待 form 变得可见
        WebElement until = wait.until(ExpectedConditions.presenceOfElementLocated(by));
        sleep(2);
        return until;
    }


    @SneakyThrows
    private void sleep(long seconds) {
        long milliseconds = seconds * 1000;
        TimeUnit.MILLISECONDS.sleep(milliseconds + RandomUtil.randomInt(1000));
    }

    @SneakyThrows
    private void sleepMs(long milliseconds) {
        TimeUnit.MILLISECONDS.sleep(milliseconds);
    }

    @SneakyThrows
    private void sendKey(WebElement element, String key) {
        for (int i = 0; i < key.toCharArray().length; i++) {
            TimeUnit.MILLISECONDS.sleep(RandomUtil.randomInt(50, 300));
            element.sendKeys(String.valueOf(key.toCharArray()[i]));
        }
    }

    private WebElement findElementOrNull(WebElement element, By by) {
        try {
            return element.findElement(by);
        } catch (Exception e) {
        }
        return null;
    }

    private WebElement findElementOrNull(WebDriver driver, By by) {
        try {
            return driver.findElement(by);
        } catch (Exception e) {
        }
        return null;
    }

    private void jsScrollY(WebDriver driver, int y) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, " + y + ");");
    }

    private void jsSetAttrValue(WebDriver driver, WebElement element, String attr, String value) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].setAttribute(arguments[1], arguments[2]);",
                element,  // 目标元素
                attr,  // 属性名
                value  // 新的属性值
        );
    }

    private void jsSetInnerText(WebDriver driver, WebElement element, String text) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].innerText = arguments[1];",
                element,  // 目标元素
                text  // 新的文本内容
        );
    }

    private void waitLoading(WebDriver driver) {
        sleep(1);
        while (true) {
            boolean loadingHide = driver.findElement(By.className("j-loading-warp")).getAttribute("class").contains("dhm-hide");
            if (loadingHide) {
                break;
            } else {
                sleep(1);
            }
        }
    }

    private void waitLoading(WebDriver driver, Dosth dosth) {
        waitLoading(driver);
        dosth.doit();
    }

    private void scrollAndClick(WebDriver driver, Actions actions, WebElement webElement) {
        scroll(driver, actions, webElement);
        webElement.click();
    }

    private void scrollAndSend(WebDriver driver, Actions actions, WebElement webElement, String keys) {
        scroll(driver, actions, webElement);
        webElement.sendKeys(keys);
    }

    private void scroll(WebDriver driver, Actions actions, WebElement webElement) {
        actions.moveToElement(webElement).perform();
        jsScrollY(driver, 100);
    }

    private WebElement scroll2element(WebDriver driver, Actions actions, By by) {
        WebElement webElement;
        List<WebElement> elements = driver.findElements(by);
        webElement = untilFindOrNull(driver, by, 5);
        while (webElement == null) {
            // 每次滚动 300 像素
            actions.sendKeys(Keys.PAGE_DOWN).perform();
            webElement = untilFindOrNull(driver, by, 5);
        }
        // 将鼠标移动到目标元素
        actions.moveToElement(webElement).perform();
        return webElement;
    }

    // 辅助方法：检查元素是否在视口中
    private static boolean isElementInView(WebDriver driver, WebElement element) {
        return (boolean) ((JavascriptExecutor) driver).executeScript(
                "var rect = arguments[0].getBoundingClientRect();" +
                        "return rect.top >= 0 && rect.left >= 0 && rect.bottom <= window.innerHeight && rect.right <= window.innerWidth;",
                element
        );
    }

    private void retry(Dosth retryJob, int milliseconds, int retryCount) {
        while (retryCount > 0) {
            retryCount--;
            try {
                retryJob.doit();
                break;
            } catch (Exception e) {
                if (retryCount == 0) {
                    throw new RuntimeException(e);
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(milliseconds);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                log.error("retry error:{}", e.getMessage());
            }
        }
    }

    private void retry(Dosth retryJob) {
        retry(retryJob, 500, 3);
    }


    interface Dosth {
        void doit();
    }


    private void showNetworkLog(ChromeDriver driver) {
        try {
            // 获取 DevTools 实例
            DevTools devTools = driver.getDevTools();
            devTools.createSession();

            // 启用 Network 域
            devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

            // 添加监听器，监听请求发送事件
            devTools.addListener(Network.requestWillBeSent(), this::logRequestDetailsAsync);

            // 添加监听器，监听响应接收事件
            devTools.addListener(Network.responseReceived(), this::logResponseDetailsAsync);

            // 添加监听器，监听加载失败事件（例如404错误）
            devTools.addListener(Network.loadingFailed(), this::logLoadingFailedAsync);

            // 打开网页
//            driver.get("http://example.com");  // 替换为实际的 URL

            // 等待几秒查看效果
//            Thread.sleep(5000);

        } catch (Exception e) {
            log.error("An error occurred: ", e);
        } finally {
//            // 关闭浏览器
//            driver.quit();
//            // 关闭线程池
//            logExecutor.shutdown();
        }
    }

    // 异步记录请求详细信息
    private void logRequestDetailsAsync(RequestWillBeSent request) {
        logExecutor.submit(() -> {
            printRequestDetails(request);
        });
    }

    // 异步记录响应详细信息
    private void logResponseDetailsAsync(ResponseReceived response) {
        logExecutor.submit(() -> {
            printResponseDetails(response);
        });
    }

    // 异步记录加载失败信息
    private void logLoadingFailedAsync(LoadingFailed loadFailed) {
        logExecutor.submit(() -> {
            log.warn("Loading Failed for: {}", loadFailed.getRequestId());
            if (loadFailed.getErrorText() != null) {
                log.warn("Error Text: {}", loadFailed.getErrorText());
            }
        });
    }

    // 打印请求详细信息
    private void printRequestDetails(RequestWillBeSent request) {
        log.info("=== Request Details ===");
        log.info("Request ID: {}", request.getRequestId());
        log.info("Request URL: {}", request.getRequest().getUrl());
        log.info("Request Method: {}", request.getRequest().getMethod());
        log.info("Request Headers: \n{}", formatHeaders(request.getRequest().getHeaders()));
        log.info("Request Post Data: {}", request.getRequest().getPostData().orElse("N/A"));
        log.info("=======================");
    }

    // 打印响应详细信息
    private void printResponseDetails(ResponseReceived response) {
        log.info("=== Response Details ===");
        log.info("Request ID: {}", response.getRequestId());
        log.info("Response URL: {}", response.getResponse().getUrl());
        log.info("Status Code: {}", response.getResponse().getStatus());
        log.info("Status Text: {}", response.getResponse().getStatusText());
        log.info("Response Headers: \n{}", formatHeaders(response.getResponse().getHeaders()));
        log.info("Response MIME Type: {}", response.getResponse().getMimeType());
//        log.info("Response Content Length: {}", response.getResponse().getk());
        log.info("=======================");
    }

    // 格式化并打印请求或响应头
    private String formatHeaders(Headers headers) {
        StringBuilder sb = new StringBuilder();
        if (headers != null) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        } else {
            sb.append("N/A");
        }
        return sb.toString();
    }

}
