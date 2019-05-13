package uk.gov.digital.ho.hocs.document.application;

public enum LogEvent {
    DOCUMENT_CREATED,
    DOCUMENT_CREATION_FAILURE,
    DOCUMENT_CONVERSION_FAILURE,
    DOCUMENT_DELETED,
    DOCUMENT_UPDATED,
    DOCUMENT_UDPATE_FAILURE,
    DOCUMENT_NOT_FOUND,
    DOCUMENT_VIRUS_SCAN_FAILURE,
    DOCUMENT_CLIENT_PROCESS_SUCCESS,
    DOCUMENT_CLIENT_FAILURE,

    S3_DOWNLOAD_FAILURE,
    S3_UPLOAD_FAILURE,
    S3_TRUSTED_COPY_REQUEST,
    S3_FILE_NOT_FOUND,

    REST_HELPER_NOT_FOUND,
    REST_HELPER_INTERNAL_SERVER_ERROR,
    REST_HELPER_MALFORMED_RESPONSE,
    AUDIT_EVENT_CREATED,
    AUDIT_FAILED,

    UNCAUGHT_EXCEPTION;
    
    public static final String EVENT = "event_id";
}
