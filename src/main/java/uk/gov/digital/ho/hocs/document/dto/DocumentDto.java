package uk.gov.digital.ho.hocs.document.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import uk.gov.digital.ho.hocs.document.model.DocumentData;
import uk.gov.digital.ho.hocs.document.model.DocumentStatus;
import uk.gov.digital.ho.hocs.document.model.DocumentType;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Slf4j
public class DocumentDto {

    @JsonProperty("UUID")
    private UUID uuid;

    @JsonProperty("externalReferenceUUID")
    private UUID externalReferenceUUID;

    @JsonProperty("type")
    private DocumentType type;

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("fileLink")
    private String fileLink;

    @JsonProperty("pdfLink")
    private String pdfLink;

    @JsonProperty("status")
    private DocumentStatus status;

    @JsonProperty("created")
    private LocalDateTime created;

    @JsonProperty("updated")
    private LocalDateTime updated;

    @JsonProperty("deleted")
    private Boolean deleted;

    public static DocumentDto from(DocumentData documentData) {


        return new DocumentDto(
                documentData.getUuid(),
                documentData.getExternalReferenceUUID(),
                documentData.getType(),
                documentData.getDisplayName(),
                urlEncode(documentData.getFileLink()),
                urlEncode(documentData.getPdfLink()),
                documentData.getStatus(),
                documentData.getCreated(),
                documentData.getUpdated(),
                documentData.getDeleted()
        );
    }

    private static String urlEncode(String value) {

        if(value != null) {
            try {
                return URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }
}