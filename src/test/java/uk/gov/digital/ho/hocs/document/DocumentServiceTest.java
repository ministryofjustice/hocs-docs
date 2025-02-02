package uk.gov.digital.ho.hocs.document;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.document.aws.S3DocumentService;
import uk.gov.digital.ho.hocs.document.client.auditclient.AuditClient;
import uk.gov.digital.ho.hocs.document.client.documentclient.DocumentClient;
import uk.gov.digital.ho.hocs.document.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.document.model.DocumentData;
import uk.gov.digital.ho.hocs.document.model.DocumentStatus;
import uk.gov.digital.ho.hocs.document.model.DocumentType;
import uk.gov.digital.ho.hocs.document.repository.DocumentRepository;

import java.util.HashSet;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private S3DocumentService s3DocumentService;

    private DocumentDataService documentService;

    @Mock
    private DocumentClient documentClient;

    @Mock
    private AuditClient auditClient;

    private boolean auditActive = true;

    @Before
    public void setUp() {
        this.documentService = new DocumentDataService(
                documentRepository, s3DocumentService, auditClient, documentClient, auditActive);
    }

    @Test
    public void shouldCreateDocumentWithValidParams() {

        UUID uuid = UUID.randomUUID();
        String displayName = "name";
        String fileName = "fileName";
        DocumentType documentType = DocumentType.ORIGINAL;

        UUID documentUUID = documentService.createDocument(uuid, displayName, fileName, documentType).getUuid();

        verify(documentRepository, times(1)).save(any(DocumentData.class));
        verify(documentClient, times(1)).processDocument(documentUUID, fileName);
        verifyNoMoreInteractions(documentRepository);
        verifyZeroInteractions(s3DocumentService);
    }

    @Test(expected = ApplicationExceptions.EntityCreationException.class)
    public void shouldNotCreateDocumentWhenDocumentUUIDIsNullException() {

        String displayName = "name";
        String fileName = "fileName";
        DocumentType documentType = DocumentType.ORIGINAL;

        documentService.createDocument(null, displayName, fileName, documentType);
    }

    @Test()
    public void shouldNotCreateDocumentWhenDocumentUUIDIsNull() {

        UUID uuid = UUID.randomUUID();
        DocumentType documentType = DocumentType.ORIGINAL;
        String fileName = "fileName";

        try {
            documentService.createDocument(uuid, null, fileName, documentType);
        } catch (ApplicationExceptions.EntityCreationException e) {
            // Do Nothing.
        }

        verifyZeroInteractions(documentClient);
        verifyNoMoreInteractions(documentRepository);
        verifyZeroInteractions(s3DocumentService);
    }

    @Test(expected = ApplicationExceptions.EntityCreationException.class)
    public void shouldNotCreateDocumentWhenDocumentTypeIsNullException() {

        UUID uuid = UUID.randomUUID();
        String displayName = "name";
        String fileName = "fileName";

        documentService.createDocument(uuid, displayName,  fileName,null);
    }

    @Test()
    public void shouldNotCreateDocumentWhenDocumentTypeIsNull() {

        UUID uuid = UUID.randomUUID();
        String displayName = "name";
        String fileName = "fileName";


        try {
            documentService.createDocument(uuid, displayName, fileName, null);
        } catch (ApplicationExceptions.EntityCreationException e) {
            // Do Nothing.
        }

        verifyZeroInteractions(documentClient);
        verifyNoMoreInteractions(documentRepository);
        verifyZeroInteractions(s3DocumentService);

    }

    @Test(expected = ApplicationExceptions.EntityCreationException.class)
    public void shouldNotCreateDocumentWhenDocumentDisplayNameIsNullException() {

        UUID uuid = UUID.randomUUID();
        DocumentType documentType = DocumentType.ORIGINAL;
        String fileName = "fileName";

        documentService.createDocument(uuid, null, fileName, documentType);
    }

    @Test()
    public void shouldNotCreateDocumentWhenDocumentDisplayNameIsNull() {

        String displayName = "name";
        DocumentType documentType = DocumentType.ORIGINAL;
        String fileName = "fileName";

        try {
            documentService.createDocument(null, displayName, fileName, documentType);
        } catch (ApplicationExceptions.EntityCreationException e) {
            // Do Nothing.
        }

        verifyZeroInteractions(documentClient);
        verifyNoMoreInteractions(documentRepository);
        verifyZeroInteractions(s3DocumentService);
    }

    @Test
    public void shouldUpdateDocumentWithValidParams() throws ApplicationExceptions.EntityCreationException, ApplicationExceptions.EntityNotFoundException {

        UUID uuid = UUID.randomUUID();
        String displayName = "name";
        DocumentType documentType = DocumentType.ORIGINAL;
        DocumentData documentData = new DocumentData(uuid, documentType, displayName);
        DocumentStatus documentStatus = DocumentStatus.UPLOADED;
        String link = "";

        when(documentRepository.findByUuid(uuid)).thenReturn(documentData);

        documentService.updateDocument(uuid, documentStatus, link, null);

        verify(documentRepository, times(1)).findByUuid(uuid);
        verify(documentRepository, times(1)).save(documentData);

        verifyZeroInteractions(documentClient);
        verifyNoMoreInteractions(documentRepository);
        verifyZeroInteractions(s3DocumentService);
    }

    @Test(expected = ApplicationExceptions.EntityNotFoundException.class)
    public void shouldNotUpdateDocumentWhenNoDocumentFound() throws ApplicationExceptions.EntityCreationException, ApplicationExceptions.EntityNotFoundException {

        UUID uuid = UUID.randomUUID();
        DocumentStatus documentStatus = DocumentStatus.UPLOADED;
        String link = "";

        when(documentRepository.findByUuid(uuid)).thenReturn(null);

        documentService.updateDocument(uuid, documentStatus, link, link);
    }

    @Test(expected = ApplicationExceptions.EntityNotFoundException.class)
    public void shouldNotUpdateDocumentWhenDocumentUUIDIsNullException() {

        DocumentStatus documentStatus = DocumentStatus.UPLOADED;
        String link = "";

        documentService.updateDocument(null, documentStatus, link, link);
    }

    @Test()
    public void shouldNotUpdateDocumentWhenDocumentUUIDIsNull() {

        DocumentStatus documentStatus = DocumentStatus.UPLOADED;
        String link = "";

        try {
            documentService.updateDocument(null, documentStatus, link, link);
        } catch (ApplicationExceptions.EntityNotFoundException e) {
            // Do Nothing.
        }

        verify(documentRepository, times(1)).findByUuid(null);

        verifyNoMoreInteractions(documentRepository);
        verifyZeroInteractions(s3DocumentService);

    }

    @Test(expected = ApplicationExceptions.EntityCreationException.class)
    public void shouldNotUpdateDocumentWhenDocumentStatusIsNullException() {

        UUID uuid = UUID.randomUUID();
        String displayName = "name";
        DocumentType documentType = DocumentType.ORIGINAL;
        DocumentData documentData = new DocumentData(uuid, documentType, displayName);
        String link = "";

        when(documentRepository.findByUuid(uuid)).thenReturn(documentData);

        documentService.updateDocument(uuid, null, link, link);
    }

    @Test()
    public void shouldNotUpdateDocumentWhenDocumentStatusIsNull() {

        UUID uuid = UUID.randomUUID();
        String displayName = "name";
        DocumentType documentType = DocumentType.ORIGINAL;
        DocumentData documentData = new DocumentData(uuid, documentType, displayName);
        String link = "";

        when(documentRepository.findByUuid(uuid)).thenReturn(documentData);

        try {
            documentService.updateDocument(uuid, null, link, link);
        } catch (ApplicationExceptions.EntityCreationException e) {
            // Do Nothing.
        }

        verify(documentRepository, times(1)).findByUuid(uuid);

        verifyNoMoreInteractions(documentRepository);
        verifyZeroInteractions(s3DocumentService);

    }

    @Test(expected = ApplicationExceptions.EntityCreationException.class)
    public void shouldNotUpdateDocumentWhenDocumentFileLinkIsNullException() {

        UUID uuid = UUID.randomUUID();
        String displayName = "name";
        DocumentType documentType = DocumentType.ORIGINAL;
        DocumentData documentData = new DocumentData(uuid, documentType, displayName);
        DocumentStatus documentStatus = DocumentStatus.UPLOADED;
        String link = "";

        when(documentRepository.findByUuid(uuid)).thenReturn(documentData);

        documentService.updateDocument(uuid, documentStatus, null, link);
    }

    @Test()
    public void shouldNotUpdateDocumentWhenDocumentFileLinkIsNull() {

        UUID uuid = UUID.randomUUID();
        String displayName = "name";
        DocumentType documentType = DocumentType.ORIGINAL;
        DocumentData documentData = new DocumentData(uuid, documentType, displayName);
        DocumentStatus documentStatus = DocumentStatus.UPLOADED;
        String link = "";

        when(documentRepository.findByUuid(uuid)).thenReturn(documentData);

        try {
            documentService.updateDocument(uuid, documentStatus, null, link);
        } catch (ApplicationExceptions.EntityCreationException e) {
            // Do Nothing.
        }

        verify(documentRepository, times(1)).findByUuid(uuid);

        verifyNoMoreInteractions(documentRepository);
        verifyZeroInteractions(s3DocumentService);
    }

    @Test
    public void shouldReturnDocumentListForCaseAndForType() {

        UUID uuid = UUID.randomUUID();

        when(documentRepository.findAllByExternalReferenceUUIDAndType(uuid,"DRAFT" )).thenReturn(new HashSet<>());

        documentService.getDocumentsByReferenceForType(uuid, "DRAFT");

        verify(documentRepository, times(1)).findAllByExternalReferenceUUIDAndType(uuid, "DRAFT");
        verifyNoMoreInteractions(documentRepository);
    }

    @Test
    public void shouldAuditSuccessfulCreateDocument() {

        UUID uuid = UUID.randomUUID();
        String displayName = "name";
        DocumentType documentType = DocumentType.ORIGINAL;
        String fileName = "fileName";

        documentService.createDocument(uuid, displayName, fileName, documentType);

        verify(auditClient, times(1)).createDocumentAudit(any());
        verifyNoMoreInteractions(auditClient);

    }

    @Test
    public void shouldAuditSuccessfulUpdateDocument() {

        UUID uuid = UUID.randomUUID();
        String displayName = "name";
        DocumentType documentType = DocumentType.ORIGINAL;
        DocumentData documentData = new DocumentData(uuid, documentType, displayName);
        when(documentRepository.findByUuid(uuid)).thenReturn(documentData);

        documentService.updateDocument(uuid, DocumentStatus.UPLOADED,"", "");

        verify(auditClient, times(1)).updateDocumentAudit(documentData);
        verifyNoMoreInteractions(auditClient);
    }

    @Test
    public void shouldAuditSuccessfulDeleteDocument() {

        UUID uuid = UUID.randomUUID();
        String displayName = "name";
        DocumentType documentType = DocumentType.ORIGINAL;
        DocumentData documentData = new DocumentData(uuid, documentType, displayName);
        when(documentRepository.findByUuid(uuid)).thenReturn(documentData);

        documentService.deleteDocument(uuid);

        verify(auditClient, times(1)).deleteDocumentAudit(any());
        verifyNoMoreInteractions(auditClient);

    }

    @Test(expected = ApplicationExceptions.EntityCreationException.class)
    public void shouldNotAuditWhenCreateDocumentFails() {

        String displayName = "name";
        DocumentType documentType = DocumentType.ORIGINAL;
        String fileName = "fileName";

        documentService.createDocument(null, displayName, fileName, documentType);

        verifyZeroInteractions(auditClient);

    }

    @Test(expected = ApplicationExceptions.EntityNotFoundException.class)
    public void shouldNotAuditWhenUpdateDocumentFails() {

        UUID uuid = UUID.randomUUID();
        when(documentRepository.findByUuid(uuid)).thenReturn(null);

        documentService.updateDocument(uuid, DocumentStatus.UPLOADED,"", "");

        verifyZeroInteractions(auditClient);
    }

    @Test(expected = NullPointerException.class)
    public void shouldNotAuditWhenDeleteDocumentFails() {

        UUID uuid = UUID.randomUUID();
        when(documentRepository.findByUuid(uuid)).thenReturn(null);

        documentService.deleteDocument(uuid);

        verifyNoMoreInteractions(auditClient);

    }
}