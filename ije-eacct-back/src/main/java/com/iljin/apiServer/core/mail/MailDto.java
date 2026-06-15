package com.iljin.apiServer.core.mail;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MailDto {
    private String from;
    private String to;
    private String subject;
    private String text;

    @Builder
    public MailDto(String from, String to, String subject, String text) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.text = text;
    }
}
