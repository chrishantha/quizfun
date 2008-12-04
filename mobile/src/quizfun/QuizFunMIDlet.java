/*
 * QuizFun - A quiz game
 * Copyright (C) 2008
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package quizfun;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.*;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import org.kxml2.io.KXmlParser;
import org.netbeans.microedition.lcdui.SplashScreen;
import org.netbeans.microedition.lcdui.WaitScreen;
import org.netbeans.microedition.util.Executable;
import org.netbeans.microedition.util.SimpleCancellableTask;
import org.xmlpull.v1.XmlPullParser;
import quizfun.dto.Answer;
import quizfun.dto.Question;
import quizfun.exception.ApplicationException;
import quizfun.util.EncodeURL;

/**
 * @author M. Isuru Tharanga Chrishantha Perera
 */
public class QuizFunMIDlet extends MIDlet implements CommandListener {

    private boolean midletPaused = false;
    private boolean login = false;
    /**
     * Session ID
     */
    private String sessionId;
    /**
     * Exit <code>Command</code>
     */
    private Command exitCommand;
    /**
     * Login Task
     */
    private SimpleCancellableTask loginTask;
    /**
     * Game Task
     */
    private SimpleCancellableTask gameTask;
    private WaitScreen waitScreen;
    private SplashScreen splashScreen;
    /**
     * Login <code>Form</code>
     */
    private Form loginForm;
    /**
     * Password <code>TextField</code>
     */
    private TextField passwordTextField;
    /**
     * Username <code>TextField</code>
     */
    private TextField usernameTextField;
    private Alert alertFailure;
    private Alert alertSuccess;
    private Alert alertAbout;
    /**
     * The Displayable displayed after failing wait screen
     */
    private Displayable failureDisplayable;
    /**
     * The Displayable displayed after successful wait screen
     */
    private Displayable successDisplayable;
    /**
     * The Main menu
     */
    private List menu;
    /**
     * Cancel <code>cancelCommand</code>
     */
    private Command cancelCommand;
    /**
     * Login <code>Command</code>
     */
    private Command loginCommand;
    /**
     * Back <code>Command</code>
     */
    private Command backCommand;
    /**
     * Done <code>Command</code>
     */
    private Command doneCommand;
    /**
     * Select Module <code>Form</code>
     */
    private Form moduleForm;
    /**
     * Module Code input <code>TextField</code>
     */
    private TextField moduleTextField;
    /**
     * Question list
     */
    private Vector questionVector = new Vector(5);
    /**
     * Game level
     */
    private int level;
    /**
     * Question <code>Form</code>
     */
    private Form questionForm;
    /**
     * Question
     */
    private StringItem questionItem;
    /**
     * Answers
     */
    private ChoiceGroup answerCg;
    /**
     * Enumerate through question. There is no going back.
     * This is for prototype only.
     */
    private Enumeration questionEnumeration;
    /**
     * Current Question displayed
     */
    private Question currentQuestion;
    /**
     * Happy Alerts
     */
    private Alert[] happyAlerts;
    /**
     * Happy Alerts
     */
    private Alert[] sadAlerts;

    //private List
    private void initialize() {
    }

    /**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     */
    public void startMIDlet() {
        switchDisplayable(null, getSplashScreen());
    }

    public void resumeMIDlet() {
    }

    /**
     * Switches a current displayable in a display. The <code>display</code> instance is taken from <code>getDisplay</code> method. This method is used by all actions in the design for switching displayable.
     * @param alert the Alert which is temporarily set to the display; if <code>null</code>, then <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     */
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {
        Display display = getDisplay();
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }
    }

    public SimpleCancellableTask getLoginTask() {
        if (loginTask == null) {
            loginTask = new SimpleCancellableTask();
            loginTask.setExecutable(new LoginExecutable());
        }
        return loginTask;
    }

    public SimpleCancellableTask getGameTask() {
        if (gameTask == null) {
            gameTask = new SimpleCancellableTask();
            gameTask.setExecutable(new GameExecutable());
        }
        return gameTask;
    }

    /**
     * Called by a system to indicated that a command has been invoked on a particular displayable.
     * @param command the Command that was invoked
     * @param displayable the Displayable where the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {
        if (command == exitCommand) {
            exitMIDlet();
            return;
        }
        if (displayable == menu) {
            int selectedIndex = menu.getSelectedIndex();
            switch (selectedIndex) {
                case 0:
                    switchDisplayable(null, getModuleForm());
                    break;
                case 1:
                    break;
                case 2:
                    switchDisplayable(getAlertAbout(), getMenu());
                    break;
            }
        } else if (displayable == moduleForm) {
            if (command == backCommand) {
                switchDisplayable(null, getMenu());
            } else if (command == doneCommand) {
                String code = moduleTextField.getString();
                if (code == null || code.trim().length() == 0) {
                    getAlertFailure().setString("Please enter a module code.");
                    switchDisplayable(getAlertFailure(), moduleForm);
                } else {
                    WaitScreen screen = getWaitScreen();
                    screen.setTask(getGameTask());
                    successDisplayable = getQuestionForm();
                    failureDisplayable = moduleForm;
                    level = 1;
                    switchDisplayable(null, screen);
                }
            }
        } else if (displayable == questionForm) {
            if (command == doneCommand) {
                boolean correct = false;
                Answer correctAnswer = null;
                Vector answers = currentQuestion.getAnswers();
                String selectedAnswer = answerCg.getString(answerCg.getSelectedIndex());
                Enumeration enumeration = answers.elements();
                while (enumeration.hasMoreElements()) {
                    Answer answer = (Answer) enumeration.nextElement();
                    if (answer.isCorrect()) {
                        correctAnswer = answer;
                        if (answer.getAnswer().equals(selectedAnswer)) {
                            correct = true;
                        }
                    }
                }

                Alert alert = null;
                Random random = new Random();
                Alert[] alerts = null;
                if (correct) {
                    alerts = getHappyAlerts();
                    alert = alerts[random.nextInt(alerts.length)];
                } else {
                    alerts = getSadAlerts();
                    alert = alerts[random.nextInt(alerts.length)];
                    alert.setString("Your answer is wrong!\nCorrect answer is '" + correctAnswer.getAnswer() + "'");
                }

                if (questionEnumeration.hasMoreElements()) {
                    setQuestionDisplay();
                    switchDisplayable(alert, questionForm);
                } else {
                    switchDisplayable(alert, getMenu());
                }
            }
        } else if (displayable == loginForm) {
            if (command == loginCommand) {
                WaitScreen screen = getWaitScreen();
                screen.setTask(getLoginTask());
                successDisplayable = getMenu();
                failureDisplayable = loginForm;
                switchDisplayable(null, screen);
            }
        } else if (displayable == splashScreen) {
            if (command == SplashScreen.DISMISS_COMMAND) {
                switchDisplayable(null, getLoginForm());
            }
        } else if (displayable == waitScreen) {
            if (command == WaitScreen.FAILURE_COMMAND) {
                switchDisplayable(getAlertFailure(), failureDisplayable);
            } else if (command == WaitScreen.SUCCESS_COMMAND) {
                switchDisplayable(getAlertSuccess(), successDisplayable);
            }
        }
    }

    public List getMenu() {
        if (menu == null) {
            Image image = null;
            try {
                image = Image.createImage("/quizfun/resources/bookmark.png");
            } catch (IOException ex) {
                logException(ex);
            }
            menu = new List("QuizFun Menu", Choice.IMPLICIT);
            menu.append("Single player game", image);
            menu.append("Multi-player game", image);
            menu.append("About", image);
            menu.addCommand(getExitCommand());
            menu.setCommandListener(this);
        }
        return menu;
    }

    /**
     * Returns an initiliazed instance of waitScreen component.
     * @return the initialized component instance
     */
    public WaitScreen getWaitScreen() {
        if (waitScreen == null) {
            waitScreen = new WaitScreen(getDisplay());
            waitScreen.setTitle("");
            waitScreen.setCommandListener(this);
            waitScreen.setText("Please Wait ...");
            try {
                Image image = Image.createImage("/quizfun/resources/exec.png");
                waitScreen.setImage(image);
            } catch (IOException ex) {
                logException(ex);
            }
        }
        return waitScreen;
    }

    /**
     * Returns an initiliazed instance of splashScreen component.
     * @return the initialized component instance
     */
    public SplashScreen getSplashScreen() {
        if (splashScreen == null) {

            splashScreen = new SplashScreen(getDisplay());
            splashScreen.setTitle("QuizFun");
            splashScreen.setCommandListener(this);
            splashScreen.setText("QuizFun - A mobile game");
            try {
                Image image = Image.createImage("/quizfun/resources/easymoblog.png");
                splashScreen.setImage(image);
            } catch (IOException ex) {
                logException(ex);
            }
        }
        return splashScreen;
    }

    /**
     * Returns an initiliazed instance of exitCommand component.
     * @return the initialized component instance
     */
    public Command getExitCommand() {
        if (exitCommand == null) {
            exitCommand = new Command("Exit", Command.EXIT, 0);
        }
        return exitCommand;
    }

    /**
     * Returns an initiliazed instance of backCommand component.
     * @return the initialized component instance
     */
    public Command getBackCommand() {
        if (backCommand == null) {
            backCommand = new Command("Back", Command.BACK, 0);
        }
        return backCommand;
    }

    /**
     * Returns an initiliazed instance of doneCommand component.
     * @return the initialized component instance
     */
    public Command getDoneCommand() {
        if (doneCommand == null) {
            doneCommand = new Command("Done", Command.OK, 0);
        }
        return doneCommand;
    }

    /**
     * Returns an initiliazed instance of cancelCommand component.
     * @return the initialized component instance
     */
    public Command getCancelCommand() {
        if (cancelCommand == null) {
            cancelCommand = new Command("Cancel", Command.CANCEL, 0);
        }
        return cancelCommand;
    }

    /**
     * Returns an initiliazed instance of loginCommand component.
     * @return the initialized component instance
     */
    public Command getLoginCommand() {
        if (loginCommand == null) {
            loginCommand = new Command("Login", Command.OK, 0);
        }
        return loginCommand;
    }

    public Form getLoginForm() {
        if (loginForm == null) {
            String username = null;
            try {
                RecordStore settings = RecordStore.openRecordStore("QuizFun", true);
                username = new String(settings.getRecord(1));
                settings.closeRecordStore();
            // No settings file existed
            } catch (Exception ex) {
                logException(ex);
            }
            loginForm = new Form("QuizFun - Login");
            usernameTextField = new TextField("Username:", username, 255, TextField.NON_PREDICTIVE);
            passwordTextField = new TextField("Password:", "", 255, TextField.PASSWORD);
            loginForm.append(usernameTextField);
            loginForm.append(passwordTextField);
            loginForm.addCommand(getExitCommand());
            loginForm.addCommand(getLoginCommand());
            loginForm.setCommandListener(this);
        }
        return loginForm;
    }

    /**
     * Returns an initiliazed instance of alertFailure component.
     * @return the initialized component instance
     */
    public Alert getAlertFailure() {
        if (alertFailure == null) {
            Image image = null;
            try {
                image = Image.createImage("/quizfun/resources/messagebox_critical.png");
            } catch (IOException ex) {
                logException(ex);
            }
            alertFailure = new Alert("Error", "", image, AlertType.ERROR);
            alertFailure.setTimeout(Alert.FOREVER);

        }
        return alertFailure;
    }

    private void resetAlertFailure() {
        getAlertFailure().setString("Unknown Error.");
    }

    /**
     * Returns an initiliazed instance of alertSuccess component.
     * @return the initialized component instance
     */
    public Alert getAlertSuccess() {
        if (alertSuccess == null) {
            Image image = null;
            try {
                image = Image.createImage("/quizfun/resources/messagebox_info.png");
            } catch (IOException ex) {
                logException(ex);
            }
            alertSuccess = new Alert("Information", "", image, AlertType.INFO);
            alertSuccess.setTimeout(Alert.FOREVER);
        }
        return alertSuccess;
    }

    private void resetAlertSuccess() {
        getAlertSuccess().setString("");
    }

    public Alert getAlertAbout() {
        if (alertAbout == null) {
            Image image = null;
            try {
                image = Image.createImage("/quizfun/resources/wizard.png");
            } catch (IOException ex) {
                logException(ex);
            }
            alertAbout = new Alert("About QuizFun", "3SFE515 Software Engineering Group Project.\nBy\nIsuru, Hiranya, Nevindaree and Madura" +
                    "\n\nInformatics Institute of Technology.", image, AlertType.INFO);
            alertAbout.setTimeout(Alert.FOREVER);

        }
        return alertAbout;
    }

    public Alert[] getHappyAlerts() {
        if (happyAlerts == null) {
            happyAlerts = new Alert[3];
            String title = "Correct!";
            String alertText = "You answer is correct!";

            Image image = null;
            try {
                image = Image.createImage("/quizfun/resources/cool-48x48.png");
            } catch (IOException ex) {
                logException(ex);
            }
            Alert alert = new Alert(title, alertText, image, AlertType.INFO);
            alert.setTimeout(Alert.FOREVER);
            happyAlerts[0] = alert;

            image = null;
            try {
                image = Image.createImage("/quizfun/resources/happy-48x48.png");
            } catch (IOException ex) {
                logException(ex);
            }
            alert = new Alert(title, alertText, image, AlertType.INFO);
            alert.setTimeout(Alert.FOREVER);
            happyAlerts[1] = alert;

            image = null;
            try {
                image = Image.createImage("/quizfun/resources/winky-48x48.png");
            } catch (IOException ex) {
                logException(ex);
            }
            alert = new Alert(title, alertText, image, AlertType.INFO);
            alert.setTimeout(Alert.FOREVER);
            happyAlerts[2] = alert;
        }
        return happyAlerts;
    }

    public Alert[] getSadAlerts() {
        if (sadAlerts == null) {
            sadAlerts = new Alert[6];
            String title = "Wrong!!";
            String alertText = "";

            Image image = null;
            try {
                image = Image.createImage("/quizfun/resources/hmm-48x48.png");
            } catch (IOException ex) {
                logException(ex);
            }
            Alert alert = new Alert(title, alertText, image, AlertType.WARNING);
            alert.setTimeout(Alert.FOREVER);
            sadAlerts[0] = alert;

            image = null;
            try {
                image = Image.createImage("/quizfun/resources/nervous-48x48.png");
            } catch (IOException ex) {
                logException(ex);
            }
            alert = new Alert(title, alertText, image, AlertType.WARNING);
            alert.setTimeout(Alert.FOREVER);
            sadAlerts[1] = alert;

            image = null;
            try {
                image = Image.createImage("/quizfun/resources/sad-48x48.png");
            } catch (IOException ex) {
                logException(ex);
            }
            alert = new Alert(title, alertText, image, AlertType.WARNING);
            alert.setTimeout(Alert.FOREVER);
            sadAlerts[2] = alert;

            image = null;
            try {
                image = Image.createImage("/quizfun/resources/shame-48x48.png");
            } catch (IOException ex) {
                logException(ex);
            }
            alert = new Alert(title, alertText, image, AlertType.WARNING);
            alert.setTimeout(Alert.FOREVER);
            sadAlerts[3] = alert;

            image = null;
            try {
                image = Image.createImage("/quizfun/resources/surprise-48x48.png");
            } catch (IOException ex) {
                logException(ex);
            }
            alert = new Alert(title, alertText, image, AlertType.WARNING);
            alert.setTimeout(Alert.FOREVER);
            sadAlerts[4] = alert;

            image = null;
            try {
                image = Image.createImage("/quizfun/resources/weepy-48x48.png");
            } catch (IOException ex) {
                logException(ex);
            }
            alert = new Alert(title, alertText, image, AlertType.WARNING);
            alert.setTimeout(Alert.FOREVER);
            sadAlerts[5] = alert;
        }
        return sadAlerts;
    }

    public Form getModuleForm() {
        if (moduleForm == null) {
            moduleForm = new Form("Module");
            moduleTextField = new TextField("Enter Module Code:", "", 255, TextField.NON_PREDICTIVE);
            moduleForm.append(moduleTextField);
            moduleForm.addCommand(getBackCommand());
            moduleForm.addCommand(getDoneCommand());
            moduleForm.setCommandListener(this);
        }
        return moduleForm;
    }

    public Form getQuestionForm() {
        if (questionForm == null) {
            questionForm = new Form("");
            questionItem = new StringItem("Question:", "");
            questionForm.append(questionItem);
            // Create an exclusive (radio) choice group
            answerCg = new ChoiceGroup("Answers:", Choice.EXCLUSIVE);
            questionForm.append(answerCg);
            questionForm.addCommand(getExitCommand());
            questionForm.addCommand(getDoneCommand());
            questionForm.setCommandListener(this);
        }
        return questionForm;
    }

    private void setQuestionDisplay() {
        // Assuming that questionEnumeration, questionItem and answerCg is initialized.
        currentQuestion = (Question) questionEnumeration.nextElement();
        Vector answers = currentQuestion.getAnswers();
        questionItem.setText(currentQuestion.getQuestion());
        answerCg.deleteAll();
        Enumeration enumeration = answers.elements();
        while (enumeration.hasMoreElements()) {
            Answer answer = (Answer) enumeration.nextElement();
            answerCg.append(answer.getAnswer(), null);
        }
    }

    /**
     * Returns a display instance.
     * @return the display instance.
     */
    public Display getDisplay() {
        return Display.getDisplay(this);
    }

    /**
     * Exits MIDlet.
     */
    public void exitMIDlet() {
        switchDisplayable(null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * Called when MIDlet is started.
     * Checks whether the MIDlet have been already started and initialize/starts or resumes the MIDlet.
     */
    public void startApp() {
        if (midletPaused) {
            resumeMIDlet();
        } else {
            initialize();
            startMIDlet();
        }
        midletPaused = false;
    }

    /**
     * Called when MIDlet is paused.
     */
    public void pauseApp() {
        midletPaused = true;
    }

    /**
     * Called to signal the MIDlet to terminate.
     * @param unconditional if true, then the MIDlet has to be unconditionally terminated and all resources has to be released.
     */
    public void destroyApp(boolean unconditional) {
        // If there is no criteria that will keep us from terminating
        if (unconditional) {
            synchronized (this) {
                if (usernameTextField == null) {
                    return;
                }

                String username = usernameTextField.getString();
                if (username == null) {
                    return;
                }

                try {

                    RecordStore settings = RecordStore.openRecordStore("QuizFun", true);

                    try {
                        settings.setRecord(1, username.getBytes(), 0, username.length());
                    // First time writing to the settings file
                    } catch (RecordStoreException rse) {
                        settings.addRecord(username.getBytes(), 0, username.length());
                    }

                    settings.closeRecordStore();
                } catch (Exception ex) {
                    logException(ex);
                }

                notifyDestroyed();
            } // synchronized
        }
    }

    /**
     * Executable for connecting with QuizFun server.
     */
    abstract class AbstractExecutable implements Executable {

        //private final String baseUrl = "http://localhost:8080/QuizFun/m/";
        private final String baseUrl = getAppProperty("QuizFun-Server-URL");
        private final String agent = "Profile/MIDP-2.0 Configuration/CLDC-1.1";
        private final String type = "application/x-www-form-urlencoded";

        public void execute() throws Exception {
            try {
                init();
                String url = baseUrl + getURL();
                System.out.println("Connecting to " + url);
                HttpConnection hc = (HttpConnection) Connector.open(url);
                hc.setRequestMethod(HttpConnection.POST);
                hc.setRequestProperty("User-Agent", agent);
                hc.setRequestProperty("Content-Type", type);
                if (sessionId != null) {
                    hc.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);
                }


                String postData = getPostData();
                if (postData != null) {
                    //System.out.println("postData: " + postData);
                    hc.setRequestProperty("Content-Length", String.valueOf(postData.length()));
                    byte postBytes[] = postData.getBytes();
                    OutputStream os = hc.openOutputStream();
                    for (int i = 0; i < postBytes.length; i++) {
                        os.write(postBytes[i]);
                    }
                    os.close();
                }

                process(hc);

                hc.close();
            } catch (ApplicationException ex) {
                getAlertFailure().setString(ex.getMessage());
                logException(ex);
                throw ex;
            } catch (Exception ex) {
                getAlertFailure().setString("Error connecting with server.");
                logException(ex);
                throw ex;
            }
        }

        protected abstract void init();

        protected abstract String getURL();

        protected abstract String getPostData();

        protected abstract void process(HttpConnection hc) throws Exception;
    }

    class LoginExecutable extends AbstractExecutable {

        protected void init() {
            resetAlertFailure();
            resetAlertSuccess();
        }

        protected String getURL() {
            return "login";
        }

        protected String getPostData() {
            String str = "username=" + EncodeURL.encode(usernameTextField.getString()) + "&password=" + EncodeURL.encode(passwordTextField.getString());
            return str;
        }

        protected void process(HttpConnection hc) throws ApplicationException, Exception {
            login = false;
            String message = null;

            //Initilialize XML parser
            KXmlParser parser = new KXmlParser();

            parser.setInput(new InputStreamReader(hc.openInputStream()));

            parser.nextTag();

            parser.require(XmlPullParser.START_TAG, null, "login");

            //Iterate through our XML file
            while (parser.nextTag() != XmlPullParser.END_TAG) {
                parser.require(XmlPullParser.START_TAG, null, null);
                String name = parser.getName();
                String text = parser.nextText();

                if (name.equals("message")) {
                    message = text;
                } else if (name.equals("login-failed")) {
                    login = Boolean.FALSE.toString().equals(text);
                } else if (name.equals("session-id")) {
                    sessionId = text;
                }

                parser.require(XmlPullParser.END_TAG, null, name);
            }

            parser.require(XmlPullParser.END_TAG, null, "login");
            parser.next();

            parser.require(XmlPullParser.END_DOCUMENT, null, null);

            //Take action based on login value
            if (login) {
                Alert alert = getAlertSuccess();
                alert.setString(message);
                alert.setTimeout(2000);
            } else {
                throw new ApplicationException(message);
            }
        }
    }

    class GameExecutable extends AbstractExecutable {

        protected void init() {
            resetAlertFailure();
            resetAlertSuccess();
        }

        protected String getURL() {
            return "game";
        }

        protected String getPostData() {
            String str = "module=" + EncodeURL.encode(moduleTextField.getString()) + "&level=" + String.valueOf(level);
            return str;
        }

        protected void process(HttpConnection hc) throws ApplicationException, Exception {
            boolean error = false;
            String errorMessage = null;

            //Initilialize XML parser
            KXmlParser parser = new KXmlParser();

            parser.setInput(new InputStreamReader(hc.openInputStream()));

            parser.nextTag();

            parser.require(XmlPullParser.START_TAG, null, "game");

            //Iterate through our XML file
            while (parser.nextTag() != XmlPullParser.END_TAG) {
                parser.require(XmlPullParser.START_TAG, null, null);
                String name = parser.getName();

                if (name.equals("error-msg")) {
                    String text = parser.nextText();
                    error = true;
                    errorMessage = text;
                } else if (name.equals("question")) {
                    parseQuestion(parser);
                }

                parser.require(XmlPullParser.END_TAG, null, name);
            }

            parser.require(XmlPullParser.END_TAG, null, "game");
            parser.next();

            parser.require(XmlPullParser.END_DOCUMENT, null, null);

            if (error) {
                throw new ApplicationException(errorMessage);
            }

            getQuestionForm().setTitle("QuizFun - Level " + level);
            questionEnumeration = questionVector.elements();
            setQuestionDisplay();

            Alert alert = getAlertSuccess();
            alert.setString("QuizFun\n\nLevel " + level);
        }

        private void parseQuestion(KXmlParser parser) throws Exception {
            Question question = new Question();
            Vector answers = new Vector(4);
            question.setAnswers(answers);
            questionVector.addElement(question);
            while (parser.nextTag() != XmlPullParser.END_TAG) {
                parser.require(XmlPullParser.START_TAG, null, null);
                String name = parser.getName();
                if (name.equals("id")) {
                    String text = parser.nextText();
                    question.setId(text);
                } else if (name.equals("value")) {
                    String text = parser.nextText();
                    question.setQuestion(text);
                } else if (name.equals("answer")) {
                    parseAnswer(parser, answers);
                }

                parser.require(XmlPullParser.END_TAG, null, name);
            }
        }

        private void parseAnswer(KXmlParser parser, Vector answers) throws Exception {
            Answer answer = new Answer();
            while (parser.nextTag() != XmlPullParser.END_TAG) {
                parser.require(XmlPullParser.START_TAG, null, null);
                String name = parser.getName();
                String text = parser.nextText();
                if (name.equals("id")) {
                    answer.setId(text);
                } else if (name.equals("value")) {
                    answer.setAnswer(text);
                } else if (name.equals("correct")) {
                    answer.setCorrect(Boolean.TRUE.toString().equals(text));
                }

                parser.require(XmlPullParser.END_TAG, null, name);
            }
            answers.addElement(answer);
        }
    }
    private boolean debug = true;

    private void logException(Exception ex) {
        if (debug) {
            ex.printStackTrace();
        }
    }
}