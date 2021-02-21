package de.taulinger.junitplay;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.joining;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.TestInfo;

/**
 *
 * @author taulinger
 */
public class TestUtils {

    private static final long startTime = System.currentTimeMillis();

    public static enum Browser {
        Firefox, Chromium, Webkit
    }

    public static enum ClientType {
        Desktop, Mobile
    }

    private com.microsoft.playwright.Browser browser;
    private BrowserContext context;
    protected Page page;
    protected TestInfo testInfo;

    public void setup(Browser browsertype, ClientType clientType) {
        BrowserType browserType = null;
        switch (browsertype) {
            case Firefox:
                browserType = Playwright
                        .create()
                        .firefox();
                break;
            case Chromium:
                browserType = Playwright
                        .create()
                        .chromium();
                break;
            case Webkit:
                browserType = Playwright
                        .create()
                        .webkit();
                break;

        }
        if (browserType != null) {

            //proxy
            //browser = browserType.launch(new BrowserType.LaunchOptions().setProxy().withServer("http://myProxyUrl:myProxyPort").done());
            browser = browserType.launch();

            if (clientType.equals(ClientType.Mobile)) {
                this.page = newPage(this.context = newMobileBrowserContext());
                navigate(Map.of("mobile", "true"));

            } else {
                this.page = newPage(this.context = newDesktopBrowserContext());
                navigate(Map.of());

            }
        }

    }

    @BeforeEach
    void injectTestInfo(TestInfo testInfo) {
        this.testInfo = testInfo;
    }

    @AfterEach
    public void cleanup() {

        takeScreenshot();

        if (browser != null) {
            browser.close();
            page = null;
        }
        if (context != null) {
            context.close();
            context = null;
        }
        if (page != null) {
            page.close();
            context = null;
        }
        testInfo = null;
    }

    protected void navigate(Map<String, String> queryParams) {

        final String baseUrl = "https://geoportal.bayern.de/bayernatlas/";

        if (queryParams.isEmpty()) {
            page.navigate(baseUrl);

        } else {
            final String queryParamsAsString = queryParams.entrySet().stream().
                    map(entry -> "&" + entry.getKey() + "=" + entry.getValue())
                    .collect(joining("&"));
            page.navigate(baseUrl + "?" + queryParamsAsString);
        }

    }

    protected void takeScreenshot() {
        if (page != null && testInfo != null) {

            final StringBuilder sb = new StringBuilder();

            final List<String> classNames = new ArrayList<>();
            Class parent = testInfo.getTestClass().get().getEnclosingClass();
            while (parent != null) {
                classNames.add(parent.getSimpleName());
                parent = parent.getEnclosingClass();
            }
            Collections.reverse(classNames);
            classNames.add(testInfo.getTestClass().get().getSimpleName());
            classNames.forEach(cn -> sb.append(cn).append("."));

            final String then = testInfo.getTestMethod().get().getName();
            sb
                    .append(then.substring(0, 1).toUpperCase())
                    .append(then.substring(1))
                    .append(testInfo.getDisplayName().split(" ")[1]);
            page.screenshot(new Page.ScreenshotOptions().withPath(Paths.get("screenshots", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date(startTime)), sb + ".png")));

        }
    }

    protected Page newPage(BrowserContext browserContext) {
        return browserContext.newPage();
    }

    protected BrowserContext newDesktopBrowserContext() {
        return browser.newContext(new com.microsoft.playwright.Browser.NewContextOptions().withViewport(800, 600));
    }

    protected BrowserContext newMobileBrowserContext() {
        return browser.newContext(new com.microsoft.playwright.Browser.NewContextOptions().withViewport(375, 667).withDeviceScaleFactor(2));
    }

    static class ReplaceCamelCase extends DisplayNameGenerator.Standard {

        @Override
        public String generateDisplayNameForClass(Class<?> testClass) {
            return replaceCamelCase(super.generateDisplayNameForClass(testClass));
        }

        @Override
        public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
            return replaceCamelCase(super.generateDisplayNameForNestedClass(nestedClass));
        }

        @Override
        public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
            return this.replaceCamelCase(testMethod.getName())
                    + DisplayNameGenerator.parameterTypesAsString(testMethod);
        }

        String replaceCamelCase(String camelCase) {
            StringBuilder result = new StringBuilder();
            result.append(Character.toUpperCase(camelCase.charAt(0)));
            for (int i = 1; i < camelCase.length(); i++) {
                if (Character.isUpperCase(camelCase.charAt(i))) {
                    result.append(' ');
                    result.append(Character.toLowerCase(camelCase.charAt(i)));
                } else {
                    result.append(camelCase.charAt(i));
                }
            }
            result.append(" ");
            return result.toString();
        }
    }

}
