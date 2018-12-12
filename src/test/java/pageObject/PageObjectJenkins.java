package pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Collection;
import java.util.Iterator;

public class PageObjectJenkins {
    private WebDriverWait wait;
    private final WebDriver driver;

    //Подготовка элементов страницы
    //элементы авторизации
    @FindBy(xpath = "//input[@name='j_username']")
    private WebElement autorizUsername;
    @FindBy(xpath = "//input[@name='j_password']")
    private WebElement autorizPassword;
    @FindBy(xpath = "//input[@name='Submit']")
    private WebElement autorizSubmit;

    //ссылки для первой проверки
    @FindBy(xpath = "//div[@id='tasks']/div/a[text()='Manage Jenkins']")
    private WebElement manageJenkins_link;
    @FindBy(xpath = "//a[.//dt[text()='Manage Users']]")
    private WebElement manageUsers_link;
    @FindBy(xpath = "//a[text()='Create User']")
    private WebElement createUser_link;

    //пустые текстовые поля и поля ввода пароля
    By emptyTextField_locator = By.xpath("//input[@type='text' and contains(text(),'')]");
    By emptyPasswordField_locator = By.xpath("//input[@type='password' and contains(text(),'')]");

    //поля создания нового пользователя и кнопка подтверждения
    By username_locator = By.xpath("//td[contains(text(),'Username')]/following-sibling::td/input");
    By password_locator = By.xpath("//td[contains(text(),'Password')]/following-sibling::td/input");
    By confirmPassword_locator = By.xpath("//td[contains(text(),'Confirm password')]/following-sibling::td/input");
    By fullName_locator = By.xpath("//td[contains(text(),'Full name')]/following-sibling::td/input");
    By email_locator = By.xpath("//td[contains(text(),'E-mail address')]/following-sibling::td/input");
    By submit_locator = By.xpath("//button[@id='yui-gen1-button']");

    //для удаления пользователя
    By yesButton_locator = By.xpath("//button[text()='Yes']");
    By confirmDelete_locator = By.xpath("//form[contains(.,'Are you sure about deleting the user from Jenkins?')]");

    public PageObjectJenkins(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, 30);
    }

    //авторизация на сайте
    public PageObjectJenkins autorization(String username, String password) {
        autorizUsername.sendKeys(username);
        autorizPassword.sendKeys(password);
        autorizSubmit.submit();
        return this;
    }

    //клики по ссылкам:
    public PageObjectJenkins clickManageJenkins() {
        manageJenkins_link.click();
        return this;
    }

    public PageObjectJenkins clickManageUsers() {
        manageUsers_link.click();
        return this;
    }

    public PageObjectJenkins clickCreateUser() {
        createUser_link.click();
        return this;
    }

    public PageObjectJenkins clickUserDelete(String username) {
        driver.findElement(By.xpath("//a[@href='user/" + username + "/delete']")).click();
        return this;
    }

    //клик по кнопке "Yes" для подтверждения удаления пользователя
    public PageObjectJenkins clickYes() {
        driver.findElement(yesButton_locator).click();
        return this;
    }

    //наличие формы заполнения данных, для создания нового пользователя
    public boolean isFormPresentForReal() {
        Collection<WebElement> forms = driver.findElements(By.tagName("form"));
        if (forms.isEmpty()) {
            return false;
        }

        Iterator<WebElement> i = forms.iterator();
        boolean form_found = false;
        WebElement form;

        while (i.hasNext()) {
            form = i.next();
            if ((form.findElements(emptyTextField_locator).size() == 3) &&
                    (form.findElements(emptyPasswordField_locator).size() == 2)) {
                form_found = true;
            }
        }
        return form_found;
    }

    //ввод данных для создания нового пользователя
    public PageObjectJenkins setUsername(String username) {
        driver.findElement(username_locator).sendKeys(username);
        return this;
    }

    public PageObjectJenkins setPassword(String password) {
        driver.findElement(password_locator).sendKeys(password);
        return this;
    }

    public PageObjectJenkins setConfirmPassword(String confirmPassword) {
        driver.findElement(confirmPassword_locator).sendKeys(confirmPassword);
        return this;
    }

    public PageObjectJenkins setFullName(String FullName) {
        driver.findElement(fullName_locator).sendKeys(FullName);
        return this;
    }

    public PageObjectJenkins setEmail(String email) {
        driver.findElement(email_locator).sendKeys(email);
        return this;
    }

    //метод комбинирует ввод данных пользователя
    public PageObjectJenkins setFields(String username, String passwod,
                                       String confirmPassword,
                                       String fullName,
                                       String email) {
        setUsername(username);
        setPassword(passwod);
        setConfirmPassword(confirmPassword);
        setFullName(fullName);
        setEmail(email);
        return this;
    }

    //отправка формы
    public PageObjectJenkins submitForm() {
        driver.findElement(submit_locator).submit();
        return this;
    }

    //нахождение элемента на странице по тегу и тексту
    public boolean isElementDisplayed(String tag, String text) {
        String xpath = "//" + tag + "[text()='" + text + "']";
        return driver.findElement(By.xpath(xpath)).isDisplayed();
    }

    //нахождение пользователя на странице по имени
    public boolean isSomeUserDisplayed(String name) {
        return driver.findElement(By.xpath("//tr[.//td/a[text()='" + name + "']]")).isDisplayed();
    }

    //наличие элемента на странице по локатору
    public boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    //наличие информации о пользователе на странице по имени
    public boolean isSomeUserPresent(String name) {
        return isElementPresent(By.xpath("//tr[.//td/a[text()='" + name + "']]"));
    }

    //метод проверяет наличие ссылки удаления, принадлежащей конкретному пользователю по имени
    public boolean isDeleteLinkPresent(String name) {
        return isElementPresent(By.xpath("//a[@href='user/" + name + "/delete']"));
    }

    //отображение формы подтверждения удаления пользователя
    public boolean idDeleteFormDisplayed() {
        return driver.findElement(confirmDelete_locator).isDisplayed();
    }
}
