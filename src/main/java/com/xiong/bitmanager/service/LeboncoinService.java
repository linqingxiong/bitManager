package com.xiong.bitmanager.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.xiong.bitmanager.common.BitResult;
import com.xiong.bitmanager.pojo.dto.req.*;
import com.xiong.bitmanager.pojo.dto.res.*;
import com.xiong.bitmanager.pojo.po.LbPic;
import com.xiong.bitmanager.pojo.po.LbProduct;
import com.xiong.bitmanager.service.feign.BitService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LeboncoinService {

    @Autowired
    private BitService bitService;


    public void executeContextLoads(LbProduct lbProduct) {
//        String name = "自动化测试2";
//        String title = "Nike Baskets Chaussures sport'07 White 37.5";
//        String key = "Chaussures neuves jamais portées\n" +
//                "Taille 37.5\n" +
//                "envoie rapide sous 24h\n" +
//                "Emballage précis,avec boîte à chaussures";
//        List<String> imgPaths = new ArrayList<>();
//        imgPaths.add("D:\\Program Files\\WeChat\\WeChat Files\\lqx19941128\\FileStorage\\File\\2024-07\\7.15图\\7.15图\\3\\1.png");
//        imgPaths.add("D:\\Program Files\\WeChat\\WeChat Files\\lqx19941128\\FileStorage\\File\\2024-07\\7.15图\\7.15图\\3\\2.png");
//        imgPaths.add("D:\\Program Files\\WeChat\\WeChat Files\\lqx19941128\\FileStorage\\File\\2024-07\\7.15图\\7.15图\\3\\3.png");
//        imgPaths.add("D:\\Program Files\\WeChat\\WeChat Files\\lqx19941128\\FileStorage\\File\\2024-07\\7.15图\\7.15图\\3\\4.png");
//        imgPaths.add("D:\\Program Files\\WeChat\\WeChat Files\\lqx19941128\\FileStorage\\File\\2024-07\\7.15图\\7.15图\\3\\5.png");

        String name = lbProduct.getName();
        String title = lbProduct.getTitle();
        String key = lbProduct.getKey();
        List<String> imgPaths = lbProduct.getPics().stream().map(LbPic::getImgUrl).collect(Collectors.toList());

                BitResult<GetBrowserListResultDto> getBrowserListResultDtoBitResult = bitService.getBrowserDetail(new GetBrowserListReqDto(0, 10, name));
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
            options.setExperimentalOption("debuggerAddress", openBrowserResultDtoBitResult.getData().getHttp());
//            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox"); // 添加启动参数

            // 使用 ChromeDriverService 和 ChromeOptions 初始化 WebDriver
            WebDriver driver = new ChromeDriver(service, options);
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
            if (justifuEndElement != null){
                justifuEndElement.findElement(By.tagName("button")).click();
            }
            WebElement form = untilFind(driver, By.tagName("form"), 60);
            WebElement title_1 = form.findElement(By.tagName("input"));
            title_1.click();
            sleep(2);

            sendKey(title_1, title);
//            form = untilFind(driver, By.tagName("form"), 60);
//            List<WebElement> selectButtons = form.findElements(By.className("items-start"));
            List<WebElement> selectButtons = untilFinds(driver, By.className("items-start"), 60);
            WebElement button = selectButtons.get(1);
            sleep(2);
            button.click();
            sleep(2);
            clickContinue(driver);
            sleep(2);
            WebElement descElement = untilFind(driver, By.tagName("textarea"), 60);
            descElement.click();
            sleep(1);
            sendKey(descElement, key);
            sleep(1);
            clickContinue(driver);
            WebElement priceCentsElement = untilFind(driver, By.id("price_cents"), 60);
            sleep(1);
            priceCentsElement.click();
            sendKey(priceCentsElement, "40");
            sleep(1);
            clickContinue(driver);
            form = untilFind(driver, By.tagName("form"), 60);
            WebElement modifyShipElement = form.findElement(By.tagName("button"));
            sleep(1);
            modifyShipElement.click();

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
            sendKey(msIpt, "5");
            sleep(1);
            shipDialog.findElement(By.tagName("footer")).findElement(By.tagName("button")).click();
            sleep(1);
            clickContinue(driver);
            for (int j = 0; j < imgPaths.size(); j++) {
                String imgPath = imgPaths.get(j);
                untilFind(driver, By.cssSelector("div[role='presentation']"), 10).findElement(By.tagName("input")).sendKeys(imgPath);
                untilInvisibilit(driver, By.cssSelector("div[role='status']"), 60);
            }
            sleep(1);
            clickContinue(driver);
            WebElement itemsStartElement = untilFind(driver, By.className("items-start"), 60);
            WebElement addrInput = itemsStartElement.findElement(By.tagName("input"));
            sleep(1);
            addrInput.click();
            sleep(1);
            List<WebElement> orignLi = untilFindsOrEmpty(driver, By.tagName("li"), 10);
            if (!orignLi.isEmpty()) {
                sleep(1);
                orignLi.get(0).click();
            } else {
                sendKey(addrInput, "31");
                List<WebElement> li = untilFinds(driver, By.tagName("li"), 60);
                sleep(1);
                li.get(0).click();
            }
            clickContinue(driver);
            WebElement element = untilFind(driver, By.cssSelector("div[data-qa-id='stickyBarElement']"), 60)
                    .findElement(By.tagName("button"));
            sleep(1);
            element.click();


            // 最后关闭 WebDriver 和 ChromeDriverService
//            driver.quit();
//            service.stop();

        }
    }
    private void clickContinue(WebDriver driver) {
        sleep(1);
        WebElement form = untilFind(driver, By.tagName("form"), 60);
        WebElement continuerBtn = form.findElements(By.tagName("button")).stream()
                .filter(webElement -> webElement.getText().equals("Continuer")).findAny().get();
        sleep(1);
        continuerBtn.click();
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
        }catch (Exception e){
            return null;
        }
    }

    private void untilInvisibilit(WebDriver driver, By by, int ofSecounds) {
        // 创建 WebDriverWait 实例，等待时间设为 10 秒
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ofSecounds));
        // 等待消失
        wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    private List<WebElement> untilFinds(WebDriver driver, By by, int ofSecounds) {
        // 创建 WebDriverWait 实例，等待时间设为 10 秒
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ofSecounds));
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
        jsScrollY(driver, 100);
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
