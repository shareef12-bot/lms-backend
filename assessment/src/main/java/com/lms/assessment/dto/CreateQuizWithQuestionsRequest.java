//package com.lms.assessment.dto;
//
//import java.util.List;
//
//public class CreateQuizWithQuestionsRequest {
//
//    private String title;
//    private String courseId;
//    private List<QuestionRequest> questions;
//
//    // ---------- inner DTO ----------
//    public static class QuestionRequest {
//        private String text;
//
//        public String getText() {
//            return text;
//        }
//
//        public void setText(String text) {
//            this.text = text;
//        }
//    }
//
//    // ---------- getters & setters ----------
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getCourseId() {
//        return courseId;
//    }
//
//    public void setCourseId(String courseId) {
//        this.courseId = courseId;
//    }
//
//    public List<QuestionRequest> getQuestions() {
//        return questions;
//    }
//
//    public void setQuestions(List<QuestionRequest> questions) {
//        this.questions = questions;
//    }
//}
package com.lms.assessment.dto;

import java.util.List;

public class CreateQuizWithQuestionsRequest {

    private String title;
    private String courseId;
    private List<QuestionRequest> questions;

    public static class QuestionRequest {
        private String text;
        private List<CreateOptionRequest> options;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<CreateOptionRequest> getOptions() {
            return options;
        }

        public void setOptions(List<CreateOptionRequest> options) {
            this.options = options;
        }
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public List<QuestionRequest> getQuestions() { return questions; }
    public void setQuestions(List<QuestionRequest> questions) { this.questions = questions; }
}

