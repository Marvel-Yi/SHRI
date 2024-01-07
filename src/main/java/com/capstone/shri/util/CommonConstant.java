package com.capstone.shri.util;

public interface CommonConstant {
    String EMAIL_SUBJECT_REGISTRATION = "SHRI Registration";

    String EMAIL_SUBJECT_INTEREST_SUBMISSION = "Interest Submission";

    String EMAIL_SUBJECT_INTEREST_RESPONSE = "Interest Response";

    String EMAIL_SUBJECT_UPLOAD_DOCUMENT = "Please upload certification documents required for you SHRI application";

    int LOGIN_EXPIRE_SECONDS = 24 * 60 * 60;

    int USER_TYPE_ADMIN = 1;

    int USER_TYPE_ORDINARY = 0;

    int TYPE_UNPROCESSED = 0;

    int TYPE_PROCESSED = 1;

    int DOCUMENT_MAX_SIZE = 3145728;

    int EDUCATION_CERTIFICATE = 0;

    int LANGUAGE_CERTIFICATE = 1;

    int EMPLOYMENT_CERTIFICATE = 2;

    int APPLICATION_STATUS_UNDER_REVIEW = 1;

    int APPLICATION_STATUS_REJECTION = 3;

    int APPLICATION_STATUS_OFFER = 4;
}
