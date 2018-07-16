package com.uom.cps3230.messagingsystem;

import com.uom.cps3230.messagingsystem.enums.MessagingSystemStates;
import junit.framework.Assert;
import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;
import nz.ac.waikato.modeljunit.GreedyTester;
import nz.ac.waikato.modeljunit.StopOnFailureListener;
import nz.ac.waikato.modeljunit.Tester;
import nz.ac.waikato.modeljunit.coverage.ActionCoverage;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionPairCoverage;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Random;

public class MessagingSystemModelTest implements FsmModel {
	private MessagingSystemStates modelState;
	private boolean mainpage, welcome,requestloginkey,login, sendmessage, readmessage,logout, loggedinmenu, cannotsendmessage;

	WebDriver browser = new ChromeDriver();

	public MessagingSystemStates getModelState() {
		return modelState;
	}

	public MessagingSystemStates getState() {
		return modelState;
	}

	public void reset(final boolean b) {
		modelState = MessagingSystemStates.MAIN_PAGE;
		mainpage = true;
		welcome = false;
		requestloginkey = false;
		login = false;
		loggedinmenu = false;
		sendmessage = false;
		readmessage = false;
		logout = false;
		cannotsendmessage = false;
		if (b) {
			browser.get("http://127.0.0.1:8080/mainpage.jsp");
		}
	}

    public boolean MainPageToMainPage1Guard() {
        return getState().equals(MessagingSystemStates.MAIN_PAGE);
    }
    public @Action void MainPageToMainPage1() {
        browser.findElement(By.name("agentID")).sendKeys("spy-0");
        browser.findElement(By.name("agentName")).sendKeys("joe");
        browser.findElement(By.name("supervisorID")).sendKeys("s0");
        browser.findElement(By.name("e")).click();
        Assert.assertEquals("The model's main-page state doesn't match the SUT's state.", mainpage, browser.getTitle().equals("MainPage"));
    }

    public boolean MainPageToMainPage2Guard() {
        return getState().equals(MessagingSystemStates.MAIN_PAGE);
    }
    public @Action void MainPageToMainPage2() {
        browser.findElement(By.name("agentID")).sendKeys("a0");
        browser.findElement(By.name("agentName")).sendKeys("joe");
        browser.findElement(By.name("supervisorID")).sendKeys("s6");
        browser.findElement(By.name("e")).click();
        browser.findElement(By.name("main")).click();
        Assert.assertEquals("The model's main-page state doesn't match the SUT's state.", mainpage, browser.getTitle().equals("MainPage"));
    }

	public boolean MainPageToWelcomeGuard() {
		return getState().equals(MessagingSystemStates.MAIN_PAGE);
	}
	public @Action void MainPageToWelcome() {

	    browser.findElement(By.name("agentID")).sendKeys("a0");
	    browser.findElement(By.name("agentName")).sendKeys("joe");
	    browser.findElement(By.name("supervisorID")).sendKeys("s0");
        browser.findElement(By.name("e")).click();
        mainpage = false;
        welcome = true;
		modelState = MessagingSystemStates.WELCOME_PAGE;
		Assert.assertEquals("The model's welcome state doesn't match the SUT's state.", welcome, browser.getTitle().equals("Welcome"));
	}

    public boolean WelcomeToRequestLoginKeyGuard() {
        return getState().equals(MessagingSystemStates.WELCOME_PAGE);
    }
    public @Action void WelcomeToRequestLoginKey() {
        browser.findElement(By.name("r")).click();
        welcome = false;
        requestloginkey = true;
        modelState = MessagingSystemStates.REQUESTLOGINKEY_PAGE;
        Assert.assertEquals("The model's request-login-key state doesn't match the SUT's state.", requestloginkey, browser.getTitle().equals("RequestLoginKey"));
    }

    public boolean RequestLoginKeyToWelcomeGuard() {
        return getState().equals(MessagingSystemStates.REQUESTLOGINKEY_PAGE);
    }
    public @Action void RequestLoginKeyToWelcome() {
        browser.findElement(By.name("c_agentID")).sendKeys("a0");
        browser.findElement(By.name("c_supervisorID")).sendKeys("s1");
        browser.findElement(By.name("r")).click();
        browser.findElement(By.name("back")).click();
        requestloginkey = false;
        welcome = true;
        modelState = MessagingSystemStates.WELCOME_PAGE;
        Assert.assertEquals("The model's welcome state doesn't match the SUT's state.", welcome, browser.getTitle().equals("Welcome"));
    }

    public boolean RequestLoginKeyToLoginGuard() {
        return getState().equals(MessagingSystemStates.REQUESTLOGINKEY_PAGE);
    }
    public @Action void RequestLoginKeyToLogin() {
        browser.findElement(By.name("c_agentID")).sendKeys("a0");
        browser.findElement(By.name("c_supervisorID")).sendKeys("s0");
        browser.findElement(By.name("r")).click();
        browser.findElement(By.name("l")).click();
        browser.findElement(By.name("l")).click();
        requestloginkey = false;
        login = true;
        modelState = MessagingSystemStates.LOGIN_PAGE;
        Assert.assertEquals("The model's login state doesn't match the SUT's state.", login, browser.getCurrentUrl().contains("LoginServlet"));
    }

    public boolean LoginToLoggedInMenuGuard() {
        return getState().equals(MessagingSystemStates.LOGIN_PAGE);
    }
    public @Action void LoginToLoggedInMenu() {
        browser.findElement(By.name("back")).click();
        login = false;
        loggedinmenu = true;
        modelState = MessagingSystemStates.LOGGEDINMENU_PAGE;
        Assert.assertEquals("The model's logged-in-menu state doesn't match the SUT's state.", loggedinmenu, browser.getTitle().equals("LoggedInMenu"));
    }

    public boolean LoggedInMenuToLoggedInMenuGuard() {
        return getState().equals(MessagingSystemStates.LOGGEDINMENU_PAGE);
    }
    public @Action void LoggedInMenuToLoggedInMenu() {
        browser.findElement(By.name("choice")).sendKeys("4");
        browser.findElement(By.name("e")).click();
        Assert.assertEquals("The model's logged-in-menu state doesn't match the SUT's state.", loggedinmenu, browser.getTitle().equals("LoggedInMenu"));
    }

    public boolean LoggedInMenuToSendMessageGuard() {
        return getState().equals(MessagingSystemStates.LOGGEDINMENU_PAGE);
    }
    public @Action void LoggedInMenuToSendMessage() {
        browser.findElement(By.name("choice")).sendKeys("1");
        browser.findElement(By.name("e")).click();
        browser.findElement(By.name("receiverID")).sendKeys("s0");
        browser.findElement(By.name("messageContent")).sendKeys("hello");
        loggedinmenu = false;
        sendmessage = true;
        modelState = MessagingSystemStates.SENDMESSAGE_PAGE;
        Assert.assertEquals("The model's send-message state doesn't match the SUT's state.", sendmessage, browser.getTitle().equals("SendMessage"));
    }

    public boolean LoggedInMenuToReadMessageGuard() {
        return getState().equals(MessagingSystemStates.LOGGEDINMENU_PAGE);
    }
    public @Action void LoggedInMenuToReadMessage() {
        browser.findElement(By.name("choice")).sendKeys("2");
        browser.findElement(By.name("e")).click();
        loggedinmenu = false;
        readmessage = true;
        modelState = MessagingSystemStates.READMESSAGE_PAGE;
        Assert.assertEquals("The model's read-message state doesn't match the SUT's state.", readmessage, browser.getTitle().equals("ReadMessage"));
    }

    public boolean LoggedInMenuToLogoutGuard() {
        return getState().equals(MessagingSystemStates.LOGGEDINMENU_PAGE);
    }
    public @Action void LoggedInMenuToLogout() {
        browser.findElement(By.name("choice")).sendKeys("3");
        browser.findElement(By.name("e")).click();
        loggedinmenu = false;
        logout = true;
        modelState = MessagingSystemStates.LOGOUT_PAGE;
        Assert.assertEquals("The model's logout state doesn't match the SUT's state.", logout, browser.getTitle().equals("Logout"));
    }

    public boolean SendMessageToLoggedInMenuGuard() {
        return getState().equals(MessagingSystemStates.SENDMESSAGE_PAGE);
    }
    public @Action void SendMessageToLoggedInMenu() {
        browser.findElement(By.name("s")).click();

        if(!browser.getTitle().equals("LoggedInMenu")) {
            sendmessage = false;
            cannotsendmessage = true;
            modelState = MessagingSystemStates.CANNOTSENDMESSAGE_PAGE;
            Assert.assertEquals("The model's cannot-send-message state doesn't match the SUT's state.", "Cannot Send Message", browser.findElement(By.id("error")).getText());
        } else {
            sendmessage = false;
            loggedinmenu = true;
            modelState = MessagingSystemStates.LOGGEDINMENU_PAGE;
            Assert.assertEquals("The model's logged-in-menu state doesn't match the SUT's state.", loggedinmenu, browser.getTitle().equals("LoggedInMenu"));
        }
    }

    public boolean ReadMessageToLoggedInMenuGuard() {
        return getState().equals(MessagingSystemStates.READMESSAGE_PAGE);
    }
    public @Action void ReadMessageToLoggedInMenu() {
        browser.findElement(By.name("r")).click();
        browser.findElement(By.name("back")).click();
        sendmessage = false;
        loggedinmenu = true;
        modelState = MessagingSystemStates.LOGGEDINMENU_PAGE;
        Assert.assertEquals("The model's logged-in-menu state doesn't match the SUT's state.", loggedinmenu, browser.getTitle().equals("LoggedInMenu"));
    }

    public boolean LogoutToMainPageGuard() {
        return getState().equals(MessagingSystemStates.LOGOUT_PAGE);
    }
    public @Action void LogoutToMainPage() {
        browser.findElement(By.name("l")).click();
        browser.findElement(By.name("back")).click();
        logout = false;
        mainpage = true;
        modelState = MessagingSystemStates.MAIN_PAGE;
        Assert.assertEquals("The model's mainpage state doesn't match the SUT's state.", mainpage, browser.getTitle().equals("MainPage"));
    }

    public boolean SendMessageToCannotSendMessageGuard() {
        return getState().equals(MessagingSystemStates.SENDMESSAGE_PAGE);
    }
    public @Action void SendMessageToCannotSendMessage() {
        browser.findElement(By.name("s")).click();

        if(!browser.getTitle().equals("LoggedInMenu")) {
            sendmessage = false;
            cannotsendmessage = true;
            modelState = MessagingSystemStates.CANNOTSENDMESSAGE_PAGE;
            Assert.assertEquals("The model's cannot-send-message state doesn't match the SUT's state.", "Cannot Send Message", browser.findElement(By.id("error")).getText());
        } else {
            sendmessage = false;
            loggedinmenu = true;
            modelState = MessagingSystemStates.LOGGEDINMENU_PAGE;
            Assert.assertEquals("The model's logged-in-menu state doesn't match the SUT's state.", loggedinmenu, browser.getTitle().equals("LoggedInMenu"));
        }
    }

    public boolean CannotSendMessageToMainPageGuard() {
        return getState().equals(MessagingSystemStates.CANNOTSENDMESSAGE_PAGE);
    }
    public @Action void CannotSendMessageToMainPage() {
        browser.findElement(By.name("main")).click();
        cannotsendmessage = false;
        mainpage = true;
        modelState = MessagingSystemStates.MAIN_PAGE;
        Assert.assertEquals("The model's mainpage state doesn't match the SUT's state.", mainpage, browser.getTitle().equals("MainPage"));
    }

	@Test
	public void MessagingSystemModelRunner() {
		final Tester tester = new GreedyTester(new MessagingSystemModelTest());
		tester.setRandom(new Random());
		tester.buildGraph();
		tester.addListener(new StopOnFailureListener());
		tester.addListener("verbose");
		tester.addCoverageMetric(new TransitionPairCoverage());
		tester.addCoverageMetric(new StateCoverage());
		tester.addCoverageMetric(new ActionCoverage());
		tester.generate(6000);
		tester.printCoverage();
	}
}
