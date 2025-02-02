package uk.gov.digital.ho.hocs.document.dto.camel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProcessDocumentRequest {

    @JsonProperty(value = "uuid", required = true)
    private final String uuid;

    @JsonProperty("fileLink")
    private final String fileLink;

}
