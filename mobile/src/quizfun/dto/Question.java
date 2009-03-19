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
package quizfun.dto;

import java.util.Vector;

/**
 * @author M. Isuru Tharanga Chrishantha Perera
 */
public class Question {

    private String id;
    private String question;
    private String hint;
    private String reference;
    private Vector answers;

    /**
     * Selected Answer for this question.
     */
    private Answer selectedAnswer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Vector getAnswers() {
        return answers;
    }

    public void setAnswers(Vector answers) {
        this.answers = answers;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Answer getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(Answer selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }
}
