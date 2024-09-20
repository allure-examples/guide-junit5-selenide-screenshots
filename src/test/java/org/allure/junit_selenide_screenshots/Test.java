package org.allure.junit_selenide_screenshots;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class Test {

    private static final String GLOBAL_PARAMETER = "global value";

    @BeforeAll
    static void setupAllureReports() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(false)
        );
    }

    @org.junit.jupiter.api.Test
    public void lambdaStepTest() {
        open("https://www.saucedemo.com");

        final String localParameter = "parameter value";
        Allure.step(String.format("Parent lambda step with parameter [%s]", localParameter), (step) -> {
            step.parameter("parameter", localParameter);
            Allure.step(String.format("Nested lambda step with global parameter [%s]", GLOBAL_PARAMETER));

            //String pngFileName = screenshot("my_file_name");
            screenshot();
        });

        $(".login_logo").shouldBe(Condition.visible);
    }

    @Attachment(type = "image/png")
    public byte[] screenshot_annotated() throws IOException {
        String screenshotAsBase64 = Selenide.screenshot(OutputType.BASE64);
        return Base64.getDecoder().decode(screenshotAsBase64);
    }

    public void screenshot() throws IOException {
        byte[] screenshot = Selenide.screenshot(OutputType.BYTES);

        try (InputStream is = new ByteArrayInputStream(screenshot)) {
            Allure.attachment("image.png", is);
        }
    }
}
