package pageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Tst_with_PageObjectJenkins {
    String base_url = "http://localhost:8080/";
    WebDriver driver = null;


    String autorizationName = ""; //todo input admin username & admin password
    String autorizationPassword = "";

    String admin = "admin";
    String someuser = "someuser";
    String somePassword = "somepassword";
    String confirmSomePassword = "somepassword";
    String fullName = "Some Full Name";
    String email = "some@addr.com";

    @BeforeClass
    public void beforeClass() throws Exception{
        System.setProperty("webdriver.chrome.driver", "C:/webDrivers/chromedriver_win32/chromedriver.exe");
        driver = new ChromeDriver();
    }

    @AfterClass
    public void afterClass(){
        driver.quit();
    }

    @Test
    public void tst_Jenkins(){
        driver.get(base_url);

//        PageObjectJenkins page = new PageObjectJenkins(driver);
        PageObjectJenkins page = PageFactory.initElements(driver, PageObjectJenkins.class);

        //авторизация
        page.autorization(autorizationName, autorizationPassword);

        //После клика по ссылке «Manage Jenkins» на странице появляется элемент dt с текстом «Manage Users»
        //и элемент dd с текстом «Create/delete/modify users that can log in to this Jenkins».
        page.clickManageJenkins();
        Assert.assertTrue(page.isElementDisplayed("dt","Manage Users"));
        Assert.assertTrue(page.isElementDisplayed("dd","Create/delete/modify users that can log in to this Jenkins"));

        //После клика по ссылке, внутри которой содержится элемент dt с текстом «Manage Users»,
        //становится доступна ссылка «Create User».
        page.clickManageUsers();
        Assert.assertTrue(page.isElementDisplayed("a", "Create User"));

        //После клика по ссылке «Create User» появляется форма с тремя полями типа text
        //и двумя полями типа password, причём все поля должны быть пустыми.
        page.clickCreateUser();

        Assert.assertTrue(page.isFormPresentForReal());

        //После заполнения полей формы («Username» = «someuser», «Password» = «somepassword»,
        //«Confirm password» = «somepassword», «Full name» = «Some Full Name», «E-mail address» = «some@addr.dom»)
        //и клика по кнопке с надписью «Create User» на странице появляется строка таблицы (элемент tr),
        //в которой есть ячейка (элемент td) с текстом «someuser».
        page.setFields(someuser, somePassword, confirmSomePassword, fullName, email).submitForm();

        Assert.assertTrue(page.isSomeUserDisplayed(someuser));

        //После клика по ссылке с атрибутом href равным «user/someuser/delete»
        //появляется текст «Are you sure about deleting the user from Jenkins?».

        page.clickUserDelete(someuser);
        Assert.assertTrue(page.idDeleteFormDisplayed());

        //После клика по кнопке с надписью «Yes» на странице отсутствует строка таблицы (элемент tr),
        //с ячейкой (элемент td) с текстом «someuser». На странице отсутствует ссылка
        // с атрибутом href равным «user/someuser/delete».

        page.clickYes();
        Assert.assertFalse(page.isSomeUserPresent(someuser));
        Assert.assertFalse(page.isDeleteLinkPresent(someuser));

        //На странице отсутствует ссылка с атрибутом href равным «user/admin/delete».
        Assert.assertFalse(page.isDeleteLinkPresent(admin));
    }
}
