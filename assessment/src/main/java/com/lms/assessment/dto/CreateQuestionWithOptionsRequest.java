package com.lms.assessment.dto;

import java.util.List;

public class CreateQuestionWithOptionsRequest {

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
