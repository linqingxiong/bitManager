package com.xiong.bitmanager.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xiong.bitmanager.common.BitResult;
import com.xiong.bitmanager.common.ResponseResult;
import com.xiong.bitmanager.pojo.dao.LbOperationLogMapper;
import com.xiong.bitmanager.pojo.dto.bm.req.*;
import com.xiong.bitmanager.pojo.dto.bm.res.CheckAgentResultDto;
import com.xiong.bitmanager.pojo.dto.bm.res.GetBrowserDetailResultDto;
import com.xiong.bitmanager.pojo.dto.bm.res.GetPidResultDto;
import com.xiong.bitmanager.pojo.dto.bm.res.OpenBrowserResultDto;
import com.xiong.bitmanager.pojo.dto.feign.AiSelectOptionRequest;
import com.xiong.bitmanager.pojo.dto.feign.AiSelectOptionRes;
import com.xiong.bitmanager.pojo.po.*;
import com.xiong.bitmanager.service.feign.BitService;
import com.xiong.bitmanager.service.feign.BmServerService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LeboncoinService {

    @Autowired
    private BitService bitService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ProxyConfigService proxyConfigService;

    @Autowired
    private LbOperationLogMapper lbOperationLogMapper;
    @Autowired
    private LbProductService lbProductService;
    @Autowired
    private LbPicService lbPicService;

    @Autowired
    private BmServerService bmServerService;

    public void putProductsOnline2(PutOnlineTask task) {
        LbProduct lbProduct = lbProductService.getById(task.getProductId());
        lbProduct.setPics(lbPicService.getPicsByProductId(lbProduct.getId()).stream().map(LbPic::getImgUrl).collect(Collectors.toList()));
        String browserId = task.getBrowserId();
        String title = lbProduct.getTitle1();
        String desc = lbProduct.getDesc2();
        String price = lbProduct.getPrice();
        String shippingCost = lbProduct.getShippingCost();
        String addrKeyword = lbProduct.getAddrKeyword();
        List<String> imgPaths = lbProduct.getPics();

        LbOperationLog operationLog = new LbOperationLog();
        operationLog.setBrowserId(browserId);
        operationLog.setProductId(lbProduct.getId());
        operationLog.setOperationType("上架商品");
        operationLog.setOperationTime(LocalDateTime.now());
        operationLog.setOperationParam("产品id：" + lbProduct.getId());
        WebDriver driver = null;
        try {
            BitResult<GetBrowserDetailResultDto> browserDetail = bitService.getBrowserDetail(new GetBrowserDetailReqDto(browserId));
            if (browserDetail.isSuccess()) {
                BitResult<GetPidResultDto> pidsResult = bitService.getPids(new GetPidReqDto(browserId));
                log.info("pidsResult:{}", JSONUtil.toJsonStr(pidsResult));
                if (pidsResult.getData().containsKey(browserId)) {
                    BitResult<String> closeBrowserResultDtoBitResult = bitService.closeBrowser(new CloseBrowserReqDto(browserId));
                    log.info("closeBrowserResultDtoBitResult:{}", JSONUtil.toJsonStr(closeBrowserResultDtoBitResult));
                    sleep(5);
                }

                if (StrUtil.isNotBlank(task.getProxyIp())) {
                    List<String> proxyIps = StrUtil.split(task.getProxyIp(), ",");
                    boolean updateProxySuccess = false;
                    for (ProxyConfig activeConfig : proxyConfigService.getActiveConfigs()) {
                        BitResult<CheckAgentResultDto> checkagent = bitService.checkagent(new CheckAgentReqDto(activeConfig));
                        if (checkagent.isSuccess()
                                && checkagent.getData().getSuccess()) {
                            CheckAgentResultDto.CheckResult checkAgentResult = checkagent.getData().getData();
                            if (!checkAgentResult.getUsed()) {
                                if (proxyIps.stream().anyMatch(ip -> StrUtil.startWith(checkAgentResult.getIp(), ip))) {
                                    UpdateProxyReqDto updateProxyReqDto = new UpdateProxyReqDto().setIp(checkAgentResult.getIp())
                                            .setCity(checkAgentResult.getCity())
                                            .setIds(Arrays.asList(browserId))
                                            .setCountry(checkAgentResult.getCountryCode())
                                            .setHost(activeConfig.getHost())
                                            .setPort(String.valueOf(activeConfig.getPort()))
                                            .setProvince(checkAgentResult.getStateProv())
                                            .setProxyMethod(2)
                                            .setProxyType(activeConfig.getProxyType())
                                            .setProxyUserName(activeConfig.getProxyUserName())
                                            .setProxyPassword(activeConfig.getProxyPassword());
                                    BitResult<Void> voidBitResult = bitService.updateProxy(updateProxyReqDto);
                                    Assert.isTrue(voidBitResult.isSuccess(), "更新代理失败");
                                    log.info("更新代理成功：{}", checkAgentResult.getIp());
                                    updateProxySuccess = true;
                                    break;
                                } else {
                                    log.warn("ip:{} 不符合{}", checkAgentResult.getIp(), proxyIps);
                                }
                            } else {
                                log.warn("代理已使用{}", checkAgentResult.getIp());
                            }
                        } else {
                            log.warn("checkagent失败{}", JSONUtil.toJsonStr(checkagent));
                        }
                    }
                    if (!updateProxySuccess) {
                        throw new RuntimeException("代理更新失败");
                    }
                }
                BitResult<OpenBrowserResultDto> openBrowserResultDtoBitResult = bitService.openBrowser(new OpenBrowserReqDto(browserId));
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
                options.setExperimentalOption("debuggerAddress", openBrowserResultDtoBitResult.getData().getHttp());
//            options.addArguments("--headless");
                options.addArguments("--disable-gpu");
                options.addArguments("--headless=new");
                options.addArguments("--start-maximized");
                options.addArguments("--no-sandbox"); // 添加启动参数

                // 使用 ChromeDriverService 和 ChromeOptions 初始化 WebDriver
                driver = new ChromeDriver(service, options);
                driver.manage().window().maximize();  // 最大化窗口
                Actions actions = new Actions(driver);
                // 使用 WebDriver 进行操作
                driver.get("https://www.leboncoin.fr/deposer-une-annonce");
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

                // ... 更多操作 ...
                WebElement justifuEndElement = untilFindOrNull(driver, By.className("justify-end"), 10);
                if (justifuEndElement != null) {
                    justifuEndElement.findElement(By.tagName("button")).click();
                }

                boolean continueJob = true;
                while (continueJob) {
                    List<WebElement> webElements = untilFinds(driver, 30
                            , ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//form//div[@data-spark-component='form-field']"))
                            , ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//form//input"))
                    );
                    if (webElements.isEmpty()) {
                        try {
                            if (untilFind(driver, By.xpath("//h2"), 20).getText().equals("Nous avons bien reçu votre annonce !")) {
                                log.info("成功");
                                break;
                            }
                        } catch (Exception e) {
                            throw new RuntimeException("找不到啊");
                        }
                    }
                    boolean clickcontinue = true;
                    switch (webElements.get(0).getTagName()) {
                        case "div":
                            List<WebElement> formField = webElements;
//                            List<WebElement> allInputs = formField.stream()
//                                    .filter(webElement -> StrUtil.isBlank(webElement
//                                            .findElement(By.xpath(".//*[(self::input and not(@type='hidden')) or self::textarea]")).getAttribute("value"))).toList();
//                            List<WebElement> needInputs = allInputs.stream().filter(webElement -> webElement.getText().contains("*")).toList();
                            List<WebElement> needInputs = formField;
                            for (WebElement needInput : needInputs) {
                                try {
                                    WebElement inputElement = needInput.findElement(By.tagName("input"));
                                    if (StrUtil.isNotBlank(inputElement.getAttribute("disabled"))) {
                                        log.info("{} disabled,无需点击", inputElement.getAttribute("name"));
                                        continue;
                                    }
                                    if (StrUtil.equals(inputElement.getAttribute("aria-hidden"), "true")) {
                                        log.info("{} aria-hidden,无需点击", inputElement.getAttribute("name"));
                                        continue;
                                    }
                                    if (inputElement.getAttribute("name").equals("donation")) {
                                        continue;
                                    }
                                    inputElement.click();
                                } catch (NoSuchElementException nsee) {
                                    needInput.click();
                                }
                                sleep(1);
                                List<WebElement> li = needInput.findElements(By.tagName("li"));
                                if (!li.isEmpty()) {
                                    String label = needInput.findElement(By.tagName("label")).getText();
                                    log.info("准备选择" + label);
                                    if (StrUtil.equalsIgnoreCase(label, "État")) {
                                        li.get(2).click();
                                        continue;
                                    }
                                    AiSelectOptionRequest aiSelectOptionRequest = new AiSelectOptionRequest();
                                    aiSelectOptionRequest.setJsonData(JSONUtil.toJsonStr(lbProduct));
                                    aiSelectOptionRequest.setTargetKey(label);
                                    List<String> contents = new ArrayList<>();
                                    contents.add(lbProduct.getTitle1());
                                    contents.add(lbProduct.getDesc2());
                                    aiSelectOptionRequest.setContents(contents);
                                    aiSelectOptionRequest.setOptions(li.stream().map(WebElement::getText).toList());
                                    ResponseResult<AiSelectOptionRes> aiSelectOptionResResponseResult = bmServerService.selectOption(aiSelectOptionRequest);
                                    AiSelectOptionRes data = aiSelectOptionResResponseResult.getData();
                                    log.info("选择:{}", JSONUtil.toJsonStr(data));
                                    String selectedValue = data.getSelectedValue();
                                    Optional<WebElement> any = li.stream().filter(webElement -> StrUtil.equalsIgnoreCase(webElement.getText(), selectedValue)).findAny();
                                    if (any.isPresent()) {
                                        any.get().click();
                                    } else {
                                        li.get(0).click();
                                    }
                                } else {
                                    WebElement element = needInput.findElement(By.xpath(".//*[(self::input and not(@type='hidden')) or self::textarea]"));
                                    WebElement lable = findOrNull(needInput, By.tagName("label"));
                                    if (lable == null) {
                                        log.info("{} 无label,跳过", element.getAttribute("name"));
                                        continue;
                                    }
                                    if (lable.getText().contains("*") && StrUtil.isNotBlank(element.getAttribute("value"))) {
                                        log.info("必填，非选择项，已填，跳过");
                                        continue;
                                    }
                                    String name = element.getAttribute("name");
                                    switch (name) {
                                        case "subject":
                                            log.info("填写标题:{}", title);
                                            element.click();
                                            sleep(1);
                                            sendKey(element, title);
                                            clickcontinue = false;

                                            List<WebElement> selectButtons = untilFinds(driver, By.className("items-start"), 60);
                                            // get 1
                                            WebElement button = selectButtons.get(0);
                                            sleep(2);
                                            button.click();
                                            sleep(2);
//                                            WebElement choisissez = untilFinds(driver, By.xpath("//form//button"), 60).stream().filter(webElement -> webElement.getText().equals("Choisissez")).findFirst().get();
//                                            if (choisissez == null) {
//                                                sleep(2);
//                                                untilFinds(driver, By.xpath("//*[contains(@class, 'items-start')]"), 60).get(0).click();
//                                            } else {
//                                                clickcontinue = false;
//                                                choisissez.click();
//                                                List<WebElement> categories = untilFinds(driver, By.xpath("//form//li"), 60);
//
//                                                List<String> fstCategoryNameList = categories
//                                                        .stream().filter(webElement -> {
//                                                            try {
//                                                                webElement.findElement(By.tagName("svg"));
//                                                                return true;
//                                                            } catch (NoSuchElementException nse) {
//                                                                return false;
//                                                            }
//                                                        }).map(WebElement::getText).toList();
//                                                AiSelectCategoryDto aiSelectCategoryDto = new AiSelectCategoryDto();
//                                                aiSelectCategoryDto.setTitle(title);
//                                                aiSelectCategoryDto.setDesc(desc);
//                                                aiSelectCategoryDto.setCategoryNames(fstCategoryNameList);
//                                                ResponseResult<AiSelectCategoryRes> aiSelectCategoryResResponseResult = bmServerService.selectCategory(aiSelectCategoryDto);
//                                                AiSelectCategoryRes data = aiSelectCategoryResResponseResult.getData();
//                                                String selectCategoryName = data.getCategoryName();
//                                                Optional<WebElement> first = categories.stream().filter(webElement -> StrUtil.equalsIgnoreCase(webElement.getText(), selectCategoryName)).findFirst();
//                                                if (first.isPresent()) {
//                                                    first.get().click();
//                                                } else {
//                                                    throw new RuntimeException("未找到指定类目:" + selectCategoryName);
//                                                }
//                                                sleep(1);
//                                                categories = untilFinds(driver, By.xpath("//form//li"), 60);
//                                                List<String> secCategoryNameList = categories
//                                                        .stream().filter(webElement -> {
//                                                            try {
//                                                                webElement.findElement(By.tagName("svg"));
//                                                                return false;
//                                                            } catch (NoSuchElementException nse) {
//                                                                return true;
//                                                            }
//                                                        }).map(WebElement::getText).toList();
//                                                if (secCategoryNameList.size() <= 1) {
//                                                    categories.get(0).click();
//                                                } else {
//                                                    AiSelectCategoryDto aiSelectCategoryDto2 = new AiSelectCategoryDto();
//                                                    aiSelectCategoryDto2.setTitle(title);
//                                                    aiSelectCategoryDto2.setDesc(desc);
//                                                    aiSelectCategoryDto2.setCategoryNames(secCategoryNameList);
//                                                    ResponseResult<AiSelectCategoryRes> aiSelectCategoryResResponseResult2 = bmServerService.selectCategory(aiSelectCategoryDto2);
//                                                    AiSelectCategoryRes data2 = aiSelectCategoryResResponseResult2.getData();
//                                                    String selectCategoryName2 = data2.getCategoryName();
//                                                    Optional<WebElement> first2 = categories.stream().filter(webElement -> StrUtil.equalsIgnoreCase(webElement.getText(), selectCategoryName2)).findFirst();
//                                                    if (first2.isPresent()) {
//                                                        first2.get().click();
//                                                    } else {
//                                                        throw new RuntimeException("未找到指定类目:" + selectCategoryName2);
//                                                    }
//                                                    sleep(10);
//                                                }
//                                            }
                                            break;
                                        case "body":
                                            log.info("填写描述:{}", desc);
//                                            element.click();
//                                            sleep(1);
                                            sendKey(element, desc);
                                            break;
                                        case "location":
                                            log.info("选择地址:{}", addrKeyword);
                                            WebElement addrInput = element;
                                            addrInput.click();
                                            sleep(1);
                                            List<WebElement> orignLi = untilFindsOrEmpty(driver, By.tagName("li"), 10);
                                            if (!orignLi.isEmpty()) {
                                                sleep(1);
                                                orignLi.get(0).click();
                                            } else {
                                                sendKey(addrInput, addrKeyword);
                                                List<WebElement> addrInputLi = untilPresences(driver, By.tagName("li"), 30);
                                                sleep(1);
                                                Optional<WebElement> any = addrInputLi.stream().filter(l -> StrUtil.equals(l.getAttribute("role"), "option")
                                                        && StrUtil.equals(l.getAttribute("aria-disabled"), "false")).findAny();
                                                if (any.isPresent()) {
                                                    any.get().click();
                                                } else {
                                                    throw new RuntimeException("未找到指定地址:" + addrKeyword);
                                                }
                                            }
                                            break;
                                        case "escrow.lastname":
                                            log.info("填写姓:{}", lbProduct.getLastName());
                                            sendKey(element, lbProduct.getLastName());
                                            break;
                                        case "escrow.firstname":
                                            log.info("填写名:{}", lbProduct.getFirstName());
                                            sendKey(element, lbProduct.getFirstName());
                                            break;
                                        case "price_cents":
                                            log.info("填写价格:{}", price);
                                            element.click();
                                            sleep(1);
                                            sendKey(element, price);
                                            break;
                                        default:

                                    }
                                }
                            }
                            try {
                                if (clickcontinue) {
                                    clickContinue(driver, actions);
                                } else {
                                    clickcontinue = true;
                                }
                            } catch (StaleElementReferenceException | java.util.NoSuchElementException nse) {
                                try {
                                    log.info("点击取消广告推广");
                                    untilFind(driver, By.cssSelector("div[data-qa-id='stickyBarElement']"), 30)
                                            .findElement(By.tagName("button")).click();
                                } catch (TimeoutException te) {
                                    log.info("填写姓名");
                                    WebElement lastNameInput = untilFind(driver, By.id("escrow.lastname"), 30);
                                    sendKey(lastNameInput, lbProduct.getLastName());
                                    WebElement firstNameInput = untilFind(driver, By.id("escrow.firstname"), 10);
                                    sendKey(firstNameInput, lbProduct.getFirstName());
                                    sleep(1);
                                    clickContinue(driver, actions);
                                    log.info("继续点击取消广告推广");
                                    untilFind(driver, By.cssSelector("div[data-qa-id='stickyBarElement']"), 30)
                                            .findElement(By.tagName("button")).click();
                                }
                            }
                            break;
                        case "input":
                            boolean clickcontinue2 = true;
                            List<WebElement> formInputs = webElements;
                            if (formInputs.stream().map(webElement -> webElement.getAttribute("id")).collect(Collectors.toList()).contains("shipping_cost")) {
                                log.info("选物流");
//                                driver.findElements(
//                                        By.xpath("//form//button")
//                                ).get(0).click();
                                Optional<WebElement> change = driver.findElements(
                                        By.xpath("//form//button")
                                ).stream().filter(webElement -> StrUtil.equalsIgnoreCase(webElement.getText(), "Modifier les modes de livraison")).findFirst();
                                if (change.isEmpty()) {
                                    throw new RuntimeException("为找到修改物流按钮");
                                }
                                change.get().click();
                                WebElement shipDialog = untilFind(driver, By.cssSelector("div[role='dialog']"), 60);

                                List<WebElement> lines = shipDialog.findElements(By.className("w-full"));
                                int i;
                                for (i = 0; i < lines.size(); i++) {
                                    WebElement line = lines.get(i);
                                    WebElement lineBtn = null;
                                    try {
                                        lineBtn = line.findElement(By.tagName("button"));
                                    } catch (Exception e) {
//                    log.info("没有找到按钮",e);
                                    }
                                    if (lineBtn == null) {
                                        break;
                                    }
                                    if (lineBtn.getAttribute("aria-checked").equals("true")) {
                                        sleep(1);
                                        scrollAndClick(driver, actions, lineBtn);
                                    }
                                }
                                sleep(1);
                                WebElement myShip = lines.get(i - 1);
                                scrollAndClick(driver, actions, myShip.findElement(By.tagName("button")));
                                sleep(1);
                                WebElement msIpt = myShip.findElement(By.tagName("input"));
                                msIpt.click();
                                sleep(1);
                                sendKey(msIpt, shippingCost);
                                sleep(1);
                                shipDialog.findElement(By.tagName("footer")).findElement(By.tagName("button")).click();
                                sleep(1);
                            } else if (formInputs.stream().map(webElement -> webElement.getAttribute("type")).toList().contains("file")) {
                                log.info("上传图片");
                                untilPresences(driver, By.xpath("//form//input"), 30).get(0).sendKeys(StrUtil.join("\n", imgPaths.stream().map(i -> new File(StrUtil.replaceFirst(i, "/files/", "uploads/")).getAbsolutePath()).collect(Collectors.toList())));
                                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
                                By photoEditBtnLocator = By.xpath("//*[@data-test-id='photoEditBtn']");
                                By photoBoxLoadingLocator = By.xpath("//*[@data-test-id='photoBox' and .//div[@data-state='loading']]");
                                // 等待加载完成且编辑按钮出现
                                wait.until(d -> {
                                    List<WebElement> editBtns = d.findElements(photoEditBtnLocator);
                                    List<WebElement> loadingBoxes = d.findElements(photoBoxLoadingLocator);
                                    return !editBtns.isEmpty() && loadingBoxes.isEmpty();
                                });
                                try {
                                    clickcontinue2 = false;
                                    clickContinue(driver, actions);
                                    WebElement dialog = untilFind(driver, By.cssSelector("div[role='dialog']"), 20);
                                    dialog.findElement(By.cssSelector("button[type='submit']")).click();
                                    continue;
                                } catch (Exception e) {
                                } finally {

                                }
                            }
                            try {
                                if (clickcontinue2) {
                                    clickContinue(driver, actions);
                                } else {
                                    clickcontinue2 = true;
                                }
                            } catch (java.util.NoSuchElementException nse) {
                                try {
                                    log.info("点击取消广告推广");
                                    untilFind(driver, By.cssSelector("div[data-qa-id='stickyBarElement']"), 30)
                                            .findElement(By.tagName("button")).click();
                                } catch (TimeoutException te) {
                                    log.info("填写姓名");
                                    WebElement lastNameInput = untilFind(driver, By.id("escrow.lastname"), 30);
                                    sendKey(lastNameInput, lbProduct.getLastName());
                                    WebElement firstNameInput = untilFind(driver, By.id("escrow.firstname"), 30);
                                    sendKey(firstNameInput, lbProduct.getFirstName());
                                    sleep(1);
                                    clickContinue(driver, actions);
                                    log.info("继续点击取消广告推广");
                                    untilFind(driver, By.cssSelector("div[data-qa-id='stickyBarElement']"), 30)
                                            .findElement(By.tagName("button")).click();
                                }
                            }
                            break;
                        default:
                            clickContinue(driver, actions);
                    }
                }


                // 最后关闭 WebDriver 和 ChromeDriverService
//            driver.quit();
//            service.stop();

                operationLog.setOperationResult("成功");
            } else {
                throw new Exception("未找到浏览器窗口：" + browserId);
            }
        } catch (Exception e) {
            log.error("putProductsOnline 错误: {}", lbProduct, e);
            operationLog.setOperationResult("失败");
            operationLog.setErrorMessage(e.getMessage());
        } finally {
            String screenshotUrl = "";
            try {
                screenshotUrl = screenShot(driver);
            } catch (Exception e) {
            }
            operationLog.setScreenShotUrl(screenshotUrl);
            lbOperationLogMapper.insert(operationLog);
            try {
                bitService.closeBrowser(new CloseBrowserReqDto(browserId));
            } catch (Exception e) {
                log.error("closeBrowser failed for browserId: {}", browserId, e);
            }
        }
    }

    private String screenShot(WebDriver driver) throws Exception {
        // 在需要截图的地方调用
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        byte[] screenshotBytes = takesScreenshot.getScreenshotAs(OutputType.BYTES);

// 存储截图
        return fileService.storeScreenshot(screenshotBytes);
    }

    private List<WebElement> untilPresences(WebDriver driver, By by, int timeoutSeconds) {
        // 创建 WebDriverWait 实例，等待时间设为 10 秒
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        // 等待 form 变得可见
        List<WebElement> until = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
        sleep(2);
        return until;
    }

    private void clickContinue(WebDriver driver, Actions actions) {
        sleep(1);
        log.info("点击 Continue");
        WebElement form = untilFind(driver, By.tagName("form"), 60);
        WebElement continuerBtn = form.findElements(By.tagName("button")).stream()
                .filter(webElement -> webElement.getText().equals("Continuer")).findAny().get();
        sleep(1);
        try {
            scrollAndClick(driver, actions, continuerBtn);

        } catch (ElementClickInterceptedException e) {
            log.info("点击 Continue 失败");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            try {
                WebElement closeAlertButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[@role='alert']//button"))
                );
                log.info("点击弹窗");
                closeAlertButton.click();
                sleep(1);
            } catch (TimeoutException te) {
                throw new RuntimeException("未找到弹窗关闭按钮");
            } catch (Exception e0) {
                throw new RuntimeException("找到弹窗关闭按钮失败");
            }
            log.info("继续点击 Continue");
            continuerBtn = form.findElements(By.tagName("button")).stream()
                    .filter(webElement -> webElement.getText().equals("Continuer")).findAny().get();
            scrollAndClick(driver, actions, continuerBtn);
        }
        sleep(1);
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
            return null;
        }
    }

    private WebElement findOrNull(WebElement p, By by) {
        try {
            return p.findElement(by);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private void untilInvisibilit(WebDriver driver, By by, int ofSecounds) {
        // 创建 WebDriverWait 实例，等待时间设为 10 秒
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ofSecounds));
        // 等待消失
        wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    private List<WebElement> untilFinds(WebDriver driver, int timeoutSeconds, final ExpectedCondition<?>... conditions) {
        // 创建 WebDriverWait 实例，等待时间设为 10 秒
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        List<WebElement> until = new ArrayList<>();
        for (int i = 0; i < conditions.length; i++) {
            try {
                until = (List<WebElement>) wait.until(conditions[i]);
            } catch (Exception e) {

            }
            if (!until.isEmpty()) {
                break;
            }
        }
        return until;
    }

    private List<WebElement> untilFinds(WebDriver driver, By by, int timeoutSeconds) {
        // 创建 WebDriverWait 实例，等待时间设为 10 秒
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        // 等待 form 变得可见
        List<WebElement> until = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
        sleep(2);
        return until;
    }

    @SneakyThrows
    private void sleep(long seconds) {
        long milliseconds = seconds * 1000;
        TimeUnit.MILLISECONDS.sleep(milliseconds + RandomUtil.randomInt(1000));
    }

    @SneakyThrows
    private void sendKey(WebElement element, String key) {
        for (int i = 0; i < key.toCharArray().length; i++) {
            TimeUnit.MILLISECONDS.sleep(RandomUtil.randomInt(50, 300));
            element.sendKeys(String.valueOf(key.toCharArray()[i]));
        }
    }

    private void scrollAndClick(WebDriver driver, Actions actions, WebElement webElement) {
        scroll(driver, actions, webElement);
        webElement.click();
    }

    private void scroll(WebDriver driver, Actions actions, WebElement webElement) {
        actions.moveToElement(webElement).perform();
        jsScrollY(driver, 300);
    }

    private void jsScrollY(WebDriver driver, int y) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, " + y + ");");
    }

    private List<WebElement> untilFindsOrEmpty(WebDriver driver, By by, int ofSecounds) {
        try {
            return untilFinds(driver, by, ofSecounds);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
