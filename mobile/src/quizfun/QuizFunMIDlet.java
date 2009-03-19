/*
 * QuizFun - A quiz game
 * Copyright (C) 2008,2009
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
    /**
     * Failure Alert
     */
    private Alert alertFailure;
    /**
     * Success Alert
     */
    private Alert alertSuccess;
    /**
     * About Alert
     */
    private Alert alertAbout;
    /**
     * Information Alert for displaying Hint and Reference.
     */
    private Alert alertInfo;
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
     * Show Reference <code>Command</code>
     */
    private Command referenceCommand;
    /**
     * Show Hint <code>Command</code>
     */
    private Command hintCommand;
    /**
     * Select Module <code>Form</code>
     */
    private Form moduleForm;
    /**
     * Select Game <code>Form</code>
     */
    private Form gameForm;
    /**
     * Single player mode. Used in server post data.
     */
    private boolean singlePlayerMode;
    /**
     * Module Code input <code>TextField</code>
     */
    private TextField moduleTextField;
    /**
     * Game ID input <code>TextField</code>
     */
    private TextField gameTextField;
    /**
     * Question list. Assuming 5 questions per level
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
    /**
     * Correct Answer Messages
     */
    private String[] correctMessages;
    /**
     * Wrong Answer Messages
     */
    private String[] wrongMessages;
    /**
     * Check whether hint or reference is used.
     * Reduce marks if help used.
     */
    private boolean helpUsed;
    /**
     * Marks
     */
    private int marks;
    /**
     * Check whether game is over.
     */
    private boolean gameOver;

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

    /**
     * Login task
     */
    public SimpleCancellableTask getLoginTask() {
        if (loginTask == null) {
            loginTask = new SimpleCancellableTask();
            loginTask.setExecutable(new LoginExecutable());
        }
        return loginTask;
    }

    /**
     * Game task
     */
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
            // Reset values.
            marks = 0;
            gameOver = false;
            int selectedIndex = menu.getSelectedIndex();
            switch (selectedIndex) {
                case 0:
                    // New Game
                    singlePlayerMode = true;
                    switchDisplayable(null, getModuleForm());
                    break;
                case 1:
                    // Join Game
                    singlePlayerMode = false;
                    switchDisplayable(null, getGameForm());
                    break;
                case 2:
                    // About
                    switchDisplayable(getAlertAbout(), getMenu());
                    break;
            }
        } else if (displayable == moduleForm) {
            if (command == backCommand) {
                // Go back to menu
                switchDisplayable(null, getMenu());
            } else if (command == doneCommand) {
                String code = moduleTextField.getString();
                if (code == null || code.trim().length() == 0) {
                    getAlertFailure().setString("Please enter a module code.");
                    switchDisplayable(getAlertFailure(), moduleForm);
                } else {
                    // Execute the game task while displaying the wait screen
                    WaitScreen screen = getWaitScreen();
                    screen.setTask(getGameTask());
                    successDisplayable = getQuestionForm();
                    failureDisplayable = moduleForm;
                    // Start with level 1.
                    level = 1;
                    switchDisplayable(null, screen);
                }
            }
        } else if (displayable == gameForm) {
            if (command == backCommand) {
                // Go back to menu
                switchDisplayable(null, getMenu());
            } else if (command == doneCommand) {
                String gameId = gameTextField.getString();
                if (gameId == null || gameId.trim().length() == 0) {
                    getAlertFailure().setString("Please enter a game id.");
                    switchDisplayable(getAlertFailure(), gameForm);
                } else {
                    // Execute the game task while displaying the wait screen
                    WaitScreen screen = getWaitScreen();
                    screen.setTask(getGameTask());
                    successDisplayable = getQuestionForm();
                    failureDisplayable = gameForm;
                    // Start with level 1.
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
                    if (answer.getAnswer().equals(selectedAnswer)) {
                        // Check whether the selected answer is correct.
                        if (answer.isCorrect()) {
                            correct = true;
                        }
                        currentQuestion.setSelectedAnswer(answer);
                    }
                    if (answer.isCorrect()) {
                        correctAnswer = answer;
                    }
                }

                Alert alert = null;
                Random random = new Random();
                Alert[] alerts;
                String[] messages;
                String message;
                if (correct) {
                    alerts = getHappyAlerts();
                    messages = getCorrectMessages();
                    alert = alerts[random.nextInt(alerts.length)];
                    message = messages[random.nextInt(messages.length)];


                    // Marking Scheme is fixed for now.
                    // The total is 100, and therefore no calculations required.
                    int questionMarks = 0;
                    if (level == 1) {
                        if (helpUsed) {
                            questionMarks = 2;
                        } else {
                            questionMarks = 4;
                        }
                    } else if (level == 2) {
                        if (helpUsed) {
                            questionMarks = 3;
                        } else {
                            questionMarks = 6;
                        }
                    } else if (level == 3) {
                        if (helpUsed) {
                            questionMarks = 5;
                        } else {
                            questionMarks = 10;
                        }
                    }
                    marks = marks + questionMarks;

                    if (helpUsed) {
                        message = message + "\n\nYou got it correct with help. Only 50% given.";
                    }
                    message = message + "\n\nMarks: " + questionMarks + "\n" + getMarksString();

                    alert.setString(message);
                } else {
                    alerts = getSadAlerts();
                    messages = getWrongMessages();
                    alert = alerts[random.nextInt(alerts.length)];
                    message = messages[random.nextInt(messages.length)];
                    alert.setString(message + "\n\nCorrect answer is '" + correctAnswer.getAnswer() + "'\n" + getMarksString());
                }

                if (questionEnumeration.hasMoreElements()) {
                    setQuestionDisplay();
                    switchDisplayable(alert, questionForm);
                } else {
                    // Increment the level
                    level++;
                    // Next level should be retreived from the server.
                    WaitScreen screen = getWaitScreen();
                    screen.setTask(getGameTask());
                    successDisplayable = getQuestionForm();
                    failureDisplayable = getMenu();
                    switchDisplayable(alert, screen);
                }
            } else if (command == hintCommand || command == referenceCommand) {
                // Help used for current question.
                helpUsed = true;
                String message = null;
                Alert alert = getAlertInfo();
                boolean hint = (command == hintCommand);
                if (hint) {
                    message = currentQuestion.getHint();
                    alert.setTitle("Hint");
                } else {
                    message = currentQuestion.getReference();
                    alert.setTitle("Reference");
                }

                if (message != null && message.trim().length() > 0) {
                    alert.setString(message);
                } else {
                    if (hint) {
                        alert.setString("No hint found!");
                    } else {
                        alert.setString("No reference found!");
                    }
                }
                switchDisplayable(alert, questionForm);
            }
        } else if (displayable == loginForm) {
            if (command == loginCommand) {
                // Execute the login task while displaying the wait screen
                WaitScreen screen = getWaitScreen();
                screen.setTask(getLoginTask());
                successDisplayable = getMenu();
                failureDisplayable = loginForm;
                switchDisplayable(null, screen);
            }
        } else if (displayable == splashScreen) {
            if (command == SplashScreen.DISMISS_COMMAND) {
                // Go to login form after the splash screen
                switchDisplayable(null, getLoginForm());
            }
        } else if (displayable == waitScreen) {
            if (command == WaitScreen.FAILURE_COMMAND) {
                // Switch the display to failureDisplayable set before executing the task
                switchDisplayable(getAlertFailure(), failureDisplayable);
            } else if (command == WaitScreen.SUCCESS_COMMAND) {
                // Switch the display to successDisplayable set before executing the task
                switchDisplayable(getAlertSuccess(), successDisplayable);
            }
        }
    }

    /**
     * Get the main menu.
     */
    public List getMenu() {
        if (menu == null) {
            Image image = null;
            try {
                image = Image.createImage("/quizfun/resources/bookmark.png");
            } catch (IOException ex) {
                logException(ex);
            }
            menu = new List("QuizFun Menu", Choice.IMPLICIT);
            menu.append("New Game", image);
            menu.append("Join Game", image);
            menu.append("About", image);
            menu.addCommand(getExitCommand());
            menu.setCommandListener(this);
        }
        return menu;
    }

    /**
     * Returns an initialized instance of waitScreen component.
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
     * Returns an initialized instance of splashScreen component.
     * @return the initialized component instance
     */
    public SplashScreen getSplashScreen() {
        if (splashScreen == null) {

            splashScreen = new SplashScreen(getDisplay());
            splashScreen.setTitle("QuizFun");
            splashScreen.setCommandListener(this);
            splashScreen.setText("QuizFun Game");
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
     * Returns an initialized instance of exitCommand component.
     * @return the initialized component instance
     */
    public Command getExitCommand() {
        if (exitCommand == null) {
            exitCommand = new Command("Exit", Command.EXIT, 0);
        }
        return exitCommand;
    }

    /**
     * Returns an initialized instance of backCommand component.
     * @return the initialized component instance
     */
    public Command getBackCommand() {
        if (backCommand == null) {
            backCommand = new Command("Back", Command.BACK, 0);
        }
        return backCommand;
    }

    /**
     * Returns an initialized instance of doneCommand component.
     * @return the initialized component instance
     */
    public Command getDoneCommand() {
        if (doneCommand == null) {
            doneCommand = new Command("Done", Command.OK, 0);
        }
        return doneCommand;
    }

    /**
     * Returns an initialized instance of cancelCommand component.
     * @return the initialized component instance
     */
    public Command getCancelCommand() {
        if (cancelCommand == null) {
            cancelCommand = new Command("Cancel", Command.CANCEL, 0);
        }
        return cancelCommand;
    }

    /**
     * Returns an initialized instance of loginCommand component.
     * @return the initialized component instance
     */
    public Command getLoginCommand() {
        if (loginCommand == null) {
            loginCommand = new Command("Login", Command.OK, 0);
        }
        return loginCommand;
    }

    /**
     * Returns an initialized instance of hintCommand component.
     * @return the initialized component instance
     */
    public Command getHintCommand() {
        if (hintCommand == null) {
            hintCommand = new Command("Hint", Command.OK, 5);
        }
        return hintCommand;
    }

    /**
     * Returns an initialized instance of referenceCommand component.
     * @return the initialized component instance
     */
    public Command getReferenceCommand() {
        if (referenceCommand == null) {
            referenceCommand = new Command("Reference", Command.OK, 10);
        }
        return referenceCommand;
    }

    /**
     * Get the login form
     */
    public Form getLoginForm() {
        if (loginForm == null) {
            String username = null;
            try {
                // Try to load the username from record store.
                // User will not have to type the username again
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
     * Returns an initialized instance of alertFailure component.
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
     * Returns an initialized instance of alertSuccess component.
     * @return the initialized component instance
     */
    public Alert getAlertSuccess() {
        if (alertSuccess == null) {
            Image image = null;
            try {
                image = Image.createImage("/quizfun/resources/easymoblog.png");
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

    /**
     * About Alert for QuizFun
     */
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

    /**
     * Returns an initialized instance of alertInfo component.
     * @return the initialized component instance
     */
    public Alert getAlertInfo() {
        if (alertInfo == null) {
            Image image = null;
            try {
                image = Image.createImage("/quizfun/resources/ktip.png");
            } catch (IOException ex) {
                logException(ex);
            }
            alertInfo = new Alert("", "", image, AlertType.INFO);
            alertInfo.setTimeout(Alert.FOREVER);
        }
        return alertInfo;
    }

    /**
     * Returns an array of alerts for displaying when an answer is correct
     * @return An array of <code>Alert</code>
     */
    public Alert[] getHappyAlerts() {
        if (happyAlerts == null) {
            happyAlerts = new Alert[3];
            String title = "Correct!";
            String alertText = "";

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

    /**
     * Returns an array of alerts for displaying when an answer is wrong
     * @return An array of <code>Alert</code>
     */
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

    /**
     * Get the list of messages displayed when the answer is correct.
     */
    public String[] getCorrectMessages() {
        if (correctMessages == null) {
            correctMessages = new String[7];
            correctMessages[0] = "Well Done! Keep Going!!";
            correctMessages[1] = "Ooh, sounds good!!";
            correctMessages[2] = "Yes, go ahead!";
            correctMessages[3] = "Damn! You are good!";
            correctMessages[4] = "Good Choice!";
            correctMessages[5] = "Congratulations!!";
            correctMessages[6] = "Hey, that's a good choice!!";
        }
        return correctMessages;
    }

    /**
     * Get the list of messages displayed when the answer is wrong.
     */
    public String[] getWrongMessages() {
        if (wrongMessages == null) {
            wrongMessages = new String[8];
            wrongMessages[0] = "Your answer is wrong!!";
            wrongMessages[1] = "Yo, No Way! Didn't you see the correct one?";
            wrongMessages[2] = "Ooops! Wrong Choice!";
            wrongMessages[3] = "It's a shame!";
            wrongMessages[4] = "Mind your step!!";
            wrongMessages[5] = "Excuse me!!";
            wrongMessages[6] = "You can't give up!";
            wrongMessages[7] = "I think you are crying now...";
        }
        return wrongMessages;
    }

    /**
     * Get the game form
     */
    public Form getGameForm() {
        if (gameForm == null) {
            gameForm = new Form("Game");
            gameTextField = new TextField("Enter Game ID:", "", 255, TextField.NUMERIC);
            gameForm.append(gameTextField);
            gameForm.addCommand(getBackCommand());
            gameForm.addCommand(getDoneCommand());
            gameForm.setCommandListener(this);
        }
        return gameForm;
    }

    /**
     * Get the module form
     */
    public Form getModuleForm() {
        if (moduleForm == null) {
            String moduleCode = null;
            try {
                // Try to load the module code from record store.
                RecordStore settings = RecordStore.openRecordStore("QuizFun", true);
                moduleCode = new String(settings.getRecord(2));
                settings.closeRecordStore();
            // No settings file existed
            } catch (Exception ex) {
                logException(ex);
            }
            moduleForm = new Form("Module");
            moduleTextField = new TextField("Enter Module Code:", moduleCode, 255, TextField.NON_PREDICTIVE);
            moduleForm.append(moduleTextField);
            moduleForm.addCommand(getBackCommand());
            moduleForm.addCommand(getDoneCommand());
            moduleForm.setCommandListener(this);
        }
        return moduleForm;
    }

    /**
     * Get the question form
     */
    public Form getQuestionForm() {
        if (questionForm == null) {
            questionForm = new Form("");
            // Create an exclusive (radio) choice group
            answerCg = new ChoiceGroup("", Choice.EXCLUSIVE);
            questionForm.append(answerCg);
            questionForm.addCommand(getExitCommand());
            questionForm.addCommand(getDoneCommand());
            questionForm.addCommand(getHintCommand());
            questionForm.addCommand(getReferenceCommand());
            questionForm.setCommandListener(this);
        }
        return questionForm;
    }

    /**
     * Display the current question.
     */
    private void setQuestionDisplay() {
        // Assuming that questionEnumeration, questionItem and answerCg is initialized.
        currentQuestion = (Question) questionEnumeration.nextElement();
        Vector answers = currentQuestion.getAnswers();
        answerCg.setLabel(currentQuestion.getQuestion());
        answerCg.deleteAll();
        Enumeration enumeration = answers.elements();
        while (enumeration.hasMoreElements()) {
            Answer answer = (Answer) enumeration.nextElement();
            answerCg.append(answer.getAnswer(), null);
        }
        helpUsed = false;
    }

    /**
     * Get Total Marks String for Display
     */
    public String getMarksString() {
        return "Total Marks: " + marks + "%  ";
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

                String moduleCode = null;
                if (moduleTextField != null) {
                    moduleCode = moduleTextField.getString();
                }

                try {
                    // Save the values in record store.
                    RecordStore settings = RecordStore.openRecordStore("QuizFun", true);

                    try {
                        settings.setRecord(1, username.getBytes(), 0, username.length());
                    // First time writing to the settings file
                    } catch (RecordStoreException rse) {
                        settings.addRecord(username.getBytes(), 0, username.length());
                    }

                    if (moduleCode != null) {
                        try {
                            settings.setRecord(2, moduleCode.getBytes(), 0, moduleCode.length());
                        // First time writing to the settings file
                        } catch (RecordStoreException rse) {
                            settings.addRecord(moduleCode.getBytes(), 0, moduleCode.length());
                        }
                    }


                    settings.closeRecordStore();
                } catch (Exception ex) {
                    logException(ex);
                }

                // Notify destroyed
                notifyDestroyed();
            } // synchronized
        }
    }

    /**
     * Executable for connecting with QuizFun server.
     */
    abstract class AbstractExecutable implements Executable {

        //private final String baseUrl = "http://localhost:8080/QuizFun/m/";

        // Get the base server url for app property.
        private final String baseUrl = getAppProperty("QuizFun-Server-URL");
        /**
         * User agent type
         */
        private final String agent = "Profile/MIDP-2.0 Configuration/CLDC-1.1";
        /**
         * Content type must be "application/x-www-form-urlencoded" to post data to the server.
         */
        private final String type = "application/x-www-form-urlencoded";

        public void execute() throws Exception {
            try {
                // Call init
                init();
                // Construct the URL to connect
                String url = baseUrl + getURL();
                System.out.println("Connecting to " + url);
                HttpConnection hc = (HttpConnection) Connector.open(url);
                hc.setRequestMethod(HttpConnection.POST);
                hc.setRequestProperty("User-Agent", agent);
                hc.setRequestProperty("Content-Type", type);
                if (sessionId != null) {
                    // Set the session id.
                    // This is important for session management.
                    hc.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);
                }

                // Get the post data
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

                // Process the HttpConnection
                process(hc);

                // Close the HttpConnection
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

        /**
         * Callback method to perform initialization.
         */
        protected abstract void init();

        /**
         * Get the URL suffix for connecting with QuizFun server.
         */
        protected abstract String getURL();

        /**
         * Get any data for posting.
         */
        protected abstract String getPostData();

        /**
         * Process the HttpConnection
         */
        protected abstract void process(HttpConnection hc) throws Exception;
    }

    /**
     * Executable for login
     */
    class LoginExecutable extends AbstractExecutable {

        protected void init() {
            resetAlertFailure();
            resetAlertSuccess();
        }

        protected String getURL() {
            return "login";
        }

        protected String getPostData() {
            // Send user name and password
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
            } else {
                throw new ApplicationException(message);
            }
        }
    }

    /**
     * Executable for game play
     */
    class GameExecutable extends AbstractExecutable {

        protected void init() {
            resetAlertFailure();
            resetAlertSuccess();
        }

        protected String getURL() {
            return "game";
        }

        protected String getPostData() {
            String str = "level=" + String.valueOf(level) +
                    "&marks=" + String.valueOf(marks);
            if (singlePlayerMode) {
                if (moduleTextField != null) {
                    str += "&module=" + EncodeURL.encode(moduleTextField.getString());
                }
            } else {
                if (gameTextField != null) {
                    str += "&game=" + gameTextField.getString();
                }
            }

            // check singlePlayerMode
            if (!questionVector.isEmpty()) {
                String answers = "";
                Enumeration enumeration = questionVector.elements();
                while (enumeration.hasMoreElements()) {
                    Question q = (Question) enumeration.nextElement();
                    Answer selectedAnswer = q.getSelectedAnswer();
                    answers += "&qa=" + q.getId() + "_" + selectedAnswer.getId();
                }
                str += answers;
            }
            return str;
        }

        protected void process(HttpConnection hc) throws ApplicationException, Exception {
            questionVector.removeAllElements();
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
                } else if (name.equals("game-over")) {
                    String text = parser.nextText();
                    gameOver = Boolean.TRUE.toString().equals(text);
                }

                parser.require(XmlPullParser.END_TAG, null, name);
            }

            parser.require(XmlPullParser.END_TAG, null, "game");
            parser.next();

            parser.require(XmlPullParser.END_DOCUMENT, null, null);

            if (gameOver) {
                Alert alert = getAlertSuccess();
                alert.setString("Game Over\n\n" + getMarksString());
                successDisplayable = getMenu();
                return;
            }

            if (error) {
                throw new ApplicationException(errorMessage);
            }

            getQuestionForm().setTitle("QuizFun - Level " + level);
            questionEnumeration = questionVector.elements();
            setQuestionDisplay();

            Alert alert = getAlertSuccess();
            String message = "QuizFun\nLevel " + level;
            if (level > 1) {
                message = message + "\n" + getMarksString();
            }
            alert.setString(message);
        }

        /**
         * Parse XML and create Question Object
         */
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
                } else if (name.equals("ref")) {
                    String text = parser.nextText();
                    question.setReference(text);
                } else if (name.equals("hint")) {
                    String text = parser.nextText();
                    question.setHint(text);
                } else if (name.equals("answer")) {
                    parseAnswer(parser, answers);
                }

                parser.require(XmlPullParser.END_TAG, null, name);
            }
        }

        /**
         * Parse XML and create Answer Object
         */
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
